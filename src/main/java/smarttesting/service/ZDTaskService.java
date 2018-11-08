package smarttesting.service;

import org.quartz.CronExpression;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smarttesting.data.ZDTaskMapper;
import smarttesting.data.ZDTaskResultMapper;
import smarttesting.data.model.ZDCase;
import smarttesting.data.model.ZDTask;
import smarttesting.data.model.ZDTaskResult;
import smarttesting.service.job.ClusteredScheduler;
import smarttesting.service.job.SmartTestingTaskJob;
import smarttesting.service.model.CallerResult;
import smarttesting.service.model.ServiceResultFail;
import smarttesting.utils.JSON;
import smarttesting.utils.Pagination;
import smarttesting.utils.Query;
import smarttesting.utils.uuid.UUID;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author
 */
@Service
public class ZDTaskService {

    private static Logger log = LoggerFactory.getLogger(ZDTaskService.class);

    @Resource
    private ZDTaskMapper       zdTaskMapper;
    @Resource
    private ZDTaskResultMapper zdTaskResultMapper;

    @Resource
    private ClusteredScheduler clusteredJob;
    @Resource
    private ZDCaseService      zdCaseService;


    public List<ZDTask> findAll(Query query) {
        List<ZDTask> zdUserList = zdTaskMapper.select(query);
        return zdUserList;
    }

    public Pagination<ZDTask> find(Query query) {

        long count = zdTaskMapper.count(query);
        Query.PageCriteria pageCriteria = query.getPageCriteria();
        Pagination<ZDTask> pageResult = new Pagination<ZDTask>(pageCriteria.getPageIndex(), pageCriteria.getPageSize(), (int) count);

        if (count != 0) {
            List<ZDTask> data = zdTaskMapper.select(query);
            pageResult.withResult(data, (int) count);
        }

        return pageResult;
    }

    @Transactional
    public ZDTask add(ZDTask zdTask) {
        zdTaskMapper.insert(zdTask);
        log.info("ADD TASK：{}", JSON.write(zdTask));
        return zdTask;
    }

    public ZDTask edit(ZDTask zdTask) {
        zdTaskMapper.update(zdTask);
        log.info("EDIT TASK：{}", JSON.write(zdTask));
        return zdTask;
    }

    @Transactional
    public ZDTask startTask(ZDTask zdTask) {
        try {
            ZDTask zdTask1 = zdTaskMapper.select(new Query().with("id", zdTask.getId())).get(0);
            zdTask.setCon(zdTask1.getCon());
            new CronExpression(zdTask1.getCon());
        } catch (ParseException e) {
            throw new ServiceResultFail("启动任务失败，表达式不正确！");
        }
        zdTask.setRun(true);
        zdTaskMapper.update(zdTask);
        Long id = zdTask.getId();
        try {
            clusteredJob.remove("c." + id).add("c." + id, SmartTestingTaskJob.class, zdTask.getCon());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceResultFail("启动任务失败！");
        }
        log.info("RUN TASK：{}", JSON.write(zdTask));
        return zdTask;
    }

    @Transactional
    public ZDTask stopTask(ZDTask zdTask) {

        try {
            ZDTask zdTask1 = zdTaskMapper.select(new Query().with("id", zdTask.getId())).get(0);
            zdTask.setCon(zdTask1.getCon());
            new CronExpression(zdTask1.getCon());
        } catch (ParseException e) {
            throw new ServiceResultFail("停止任务失败，表达式不正确！");
        }
        zdTask.setRun(false);
        zdTaskMapper.update(zdTask);
        try {
            Long id = zdTask.getId();
            clusteredJob.remove("c." + id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceResultFail("停止任务失败！");
        }
        log.info("STOP TASK：{}", JSON.write(zdTask));
        return zdTask;
    }


    @Transactional
    public ZDTaskResult addZDTaskResult(ZDTaskResult zdTaskResult) {
        zdTaskResultMapper.insert(zdTaskResult);
        return zdTaskResult;
    }

    public Long[] delete(Long[] zdInterfaceIds) {
        for (Long id : zdInterfaceIds) {
            zdTaskMapper.delete(new Query().with("id", id));
        }

        return zdInterfaceIds;
    }


    public ZDTaskResult findZDTaskSingleResult(String rid) {
        List<ZDTaskResult> zdTaskResults = zdTaskResultMapper.select(new Query().with("rid", rid));
        if (zdTaskResults != null && zdTaskResults.size() > 0) {
            return zdTaskResults.get(0);
        }
        return null;
    }


    public String runTask(Long taskId, JobKey jobKey) {


        String requestId = new UUID().toString32();
        String result = "";

        try {

            log.info("ID: {} START DO TASK====================================> ", requestId);
            ZDTaskResult zdTaskResult = new ZDTaskResult();
            zdTaskResult.setStart(new Date());
            log.info("ID: {} JobKey :" + JSON.write(jobKey), requestId);
            ZDTask zdTask = this.find(new Query().with("id", taskId)).singleResult();

            log.info("ID: {} ZDTask :" + JSON.write(zdTask), requestId);

            String cases = zdTask.getCases();
            String[] caseIds = cases.split(",");

            List<CallerResult> callerResultsF = new ArrayList<CallerResult>();

            Map body = new HashMap();
            int count = 0;
            int passC = 0;
            int failC = 0;
            int errorC = 0;

            Map<String, Map> groups = new LinkedHashMap<String, Map>();

            {

                String groupId = new UUID().toString32();
                Map groupBody = new HashMap();
                groupBody.put("groupId", groupId);

                Map<String, Map> rowBody = new LinkedHashMap<String, Map>();
                int groupCount = 0;
                int groupPassC = 0;
                int groupFailC = 0;
                int groupErrorC = 0;

                for (String caseIdString : caseIds) {
                    try {
                        String callId = new UUID().toString32();
                        Long caseId = Long.parseLong(caseIdString);
                        ZDCase zdCase = zdCaseService.find(new Query().with("id", caseId)).singleResult();
                        List<CallerResult> callerResults = zdCaseService.run(zdCase);
                        CallerResult callerResult = callerResults.get(0);
                        callerResultsF.add(callerResult);
                        count++;
                        groupCount++;
                        if ("true".equals(callerResult.getResponseResult())) {
                            passC++;
                            groupPassC++;
                        } else if ("false".equals(callerResult.getResponseResult())) {
                            failC++;
                            groupFailC++;
                        } else {
                            errorC++;
                            groupErrorC++;
                        }
                        rowBody.put(callId, JSON.read(JSON.write(callerResult), Map.class));
                        log.info("ID: {} CallerResult :" + JSON.write(callerResult), requestId);
                    } catch (Exception e) {

                    }
                }

                groupBody.put("groupCount", groupCount);
                groupBody.put("groupPassC", groupPassC);
                groupBody.put("groupFailC", groupFailC);
                groupBody.put("groupErrorC", groupErrorC);
                groupBody.put("rowBody", rowBody);

                groups.put(groupId, groupBody);


            }

            zdTask.setLastrun(requestId);
            this.edit(zdTask);


            zdTaskResult.setTime(new Date());
            zdTaskResult.setEnd(new Date());
            zdTaskResult.setTid(taskId);
            zdTaskResult.setRid(requestId);

            body.put("duration", zdTaskResult.getEnd().getTime() - zdTaskResult.getStart().getTime());
            body.put("startT", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(zdTaskResult.getStart()));
            body.put("count", count);
            body.put("passC", passC);
            body.put("failC", failC);
            body.put("errorC", errorC);
            body.put("groups", groups);
            body.put("tName", zdTask.getName());

            result = JSON.write(body);

            zdTaskResult.setResult(result);

            this.addZDTaskResult(zdTaskResult);

        } catch (Exception e) {
            log.error("ID: {} DO TASK [" + taskId + "] FAIL：" + e.getMessage(), requestId, e);
        }

        log.info("ID: {} END DO TASK====================================> ", requestId);
        return result;

    }

}

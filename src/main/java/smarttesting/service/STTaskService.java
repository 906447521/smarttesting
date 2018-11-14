package smarttesting.service;

import org.quartz.CronExpression;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smarttesting.data.STTaskMapper;
import smarttesting.data.STTaskResultMapper;
import smarttesting.data.model.STCase;
import smarttesting.data.model.STScene;
import smarttesting.data.model.STTask;
import smarttesting.data.model.STTaskResult;
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
public class STTaskService {

    private static Logger log = LoggerFactory.getLogger(STTaskService.class);

    @Resource
    private STTaskMapper       zdTaskMapper;
    @Resource
    private STTaskResultMapper zdTaskResultMapper;

    @Resource
    private ClusteredScheduler clusteredJob;
    @Resource
    private STCaseService      zdCaseService;
    @Resource
    private STSceneService     zdSceneService;


    public List<STTask> findAll(Query query) {
        List<STTask> zdUserList = zdTaskMapper.select(query);
        return zdUserList;
    }

    public Pagination<STTask> find(Query query) {

        long count = zdTaskMapper.count(query);
        Query.PageCriteria pageCriteria = query.getPageCriteriaIfNullWithMaxInteger();
        Pagination<STTask> pageResult = new Pagination<STTask>(pageCriteria.getPageIndex(), pageCriteria.getPageSize(), (int) count);

        if (count != 0) {
            List<STTask> data = zdTaskMapper.select(query);
            pageResult.withResult(data, (int) count);
        }

        return pageResult;
    }

    @Transactional
    public STTask add(STTask zdTask) {
        zdTask.setRun(false);
        zdTaskMapper.insert(zdTask);
        log.info("ADD TASK：{}", JSON.write(zdTask));
        return zdTask;
    }

    public STTask edit(STTask zdTask) {
        zdTaskMapper.update(zdTask);
        log.info("EDIT TASK：{}", JSON.write(zdTask));
        return zdTask;
    }

    @Transactional
    public STTask startTask(STTask zdTask) {
        try {
            STTask zdTask1 = zdTaskMapper.select(new Query().with("id", zdTask.getId())).get(0);
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
    public STTask stopTask(STTask zdTask) {

        try {
            STTask zdTask1 = zdTaskMapper.select(new Query().with("id", zdTask.getId())).get(0);
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
    public STTaskResult addZDTaskResult(STTaskResult zdTaskResult) {
        zdTaskResultMapper.insert(zdTaskResult);
        return zdTaskResult;
    }

    public Long[] delete(Long[] zdInterfaceIds) {
        for (Long id : zdInterfaceIds) {
            zdTaskMapper.delete(new Query().with("id", id));
        }

        return zdInterfaceIds;
    }


    public STTaskResult findZDTaskSingleResult(String rid) {
        List<STTaskResult> zdTaskResults = zdTaskResultMapper.select(new Query().with("rid", rid));
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


            STTaskResult zdTaskResult = new STTaskResult();
            zdTaskResult.setStart(new Date());
            STTask zdTask = this.find(new Query().with("id", taskId)).singleResult();


            String scenes = zdTask.getScenes();
            String[] sceneIds = scenes.split(",");


            List<CallerResult> callerResultsF = new ArrayList<CallerResult>();

            Map body = new HashMap();
            int count = 0;
            int passC = 0;
            int failC = 0;
            int errorC = 0;

            Map<String, Map> groups = new LinkedHashMap<String, Map>();

            {
                for (String sceneId : sceneIds) {
                    try {


                        STScene zdScene = zdSceneService.find(new Query().with("id", sceneId)).singleResult();

                        if (zdScene == null) {
                            log.info("ID: {} 测试集 : " + sceneId + " 已经被删除，跳过执行 ", requestId);
                            continue;
                        }

                        String groupId = new UUID().toString32();
                        Map groupBody = new HashMap();
                        groupBody.put("groupId", groupId);
                        groupBody.put("sceneId", sceneId);
                        groupBody.put("sceneName", zdScene.getName());

                        Map<String, Map> rowBody = new LinkedHashMap<String, Map>();
                        int groupCount = 0;
                        int groupPassC = 0;
                        int groupFailC = 0;
                        int groupErrorC = 0;

                        String cases = zdScene.getCases();
                        String[] caseIds = cases.split(",");

                        for (String caseIdString : caseIds) {


                            try {

                                String callId = new UUID().toString32();
                                Long caseId = Long.parseLong(caseIdString);
                                STCase zdCase = zdCaseService.find(new Query().with("id", caseId)).singleResult();

                                if (zdCase == null) {
                                    log.info("ID: {} 用例 : " + caseId + " 已经被删除，跳过执行 ", requestId);
                                    continue;
                                }

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


                            } catch (Exception e) {

                            }
                        }


                        groupBody.put("groupCount", groupCount);
                        groupBody.put("groupPassC", groupPassC);
                        groupBody.put("groupFailC", groupFailC);
                        groupBody.put("groupErrorC", groupErrorC);
                        groupBody.put("rowBody", rowBody);

                        groups.put(groupId, groupBody);


                    } catch (Exception e) {

                    }
                }


            }

            Date date = new Date();

            Map map = new HashMap();
            map.put("duration", date.getTime() - zdTaskResult.getStart().getTime());
            map.put("count", count);
            map.put("passC", passC);
            map.put("failC", failC);
            map.put("errorC", errorC);

            zdTask.setLastrun(requestId);
            zdTask.setLastrunstatus(JSON.write(map));
            zdTask.setLastruntime(date);
            this.edit(zdTask);


            zdTaskResult.setTime(date);
            zdTaskResult.setEnd(date);
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


            log.info("ID: {} RESULT : \r\t {} ", new Object[]{requestId, result});


        } catch (Exception e) {
            log.error("ID: {} DO TASK [" + taskId + "] FAIL：" + e.getMessage(), requestId, e);
        }

        log.info("ID: {} END DO TASK====================================> ", requestId);
        return result;

    }

}

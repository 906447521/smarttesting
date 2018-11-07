package smarttesting.service.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import smarttesting.data.model.ZDCase;
import smarttesting.data.model.ZDTask;
import smarttesting.data.model.ZDTaskResult;
import smarttesting.service.ZDCaseService;
import smarttesting.service.ZDInterfaceService;
import smarttesting.service.ZDTaskService;
import smarttesting.service.ZDUserService;
import smarttesting.service.model.CallerResult;
import smarttesting.utils.JSON;
import smarttesting.utils.Query;
import smarttesting.utils.uuid.UUID;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author
 */
@Service
public class CaseJob implements Job {

    private static Logger log = LoggerFactory.getLogger(CaseJob.class);

    @Resource
    private ZDUserService      zdUserService;
    @Resource
    private ZDTaskService      zdTaskService;
    @Resource
    private ZDCaseService      zdCaseService;
    @Resource
    private ZDInterfaceService zdInterfaceService;

    public CaseJob() {
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        String requestId = new UUID().toString32();
        log.info("ID: {} START DO TASK====================================> ", requestId);
        Long taskId = 0L;
        ZDTaskResult zdTaskResult = new ZDTaskResult();
        zdTaskResult.setStart(new Date());
        try {
            JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
            log.info("ID: {} JobKey :" + JSON.write(jobKey), requestId);
            String[] id = jobKey.getName().split("\\.");
            taskId = Long.parseLong(id[1]);
            ZDTask zdTask = zdTaskService.find(new Query().with("id", taskId)).singleResult();

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
            zdTaskService.edit(zdTask);


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
            zdTaskResult.setResult(JSON.write(body));


            zdTaskService.addZDTaskResult(zdTaskResult);

        } catch (Exception e) {
            log.error("ID: {} DO TASK [" + taskId + "] FAIL：" + e.getMessage(), requestId, e);
        }

        log.info("ID: {} END DO TASK====================================> ", requestId);
    }
}

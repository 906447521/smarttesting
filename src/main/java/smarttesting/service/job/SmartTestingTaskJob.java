package smarttesting.service.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.springframework.stereotype.Service;
import smarttesting.service.ZDTaskService;

import javax.annotation.Resource;

/**
 * @author
 */
@Service
public class SmartTestingTaskJob implements Job {

    @Resource
    private ZDTaskService zdTaskService;

    public SmartTestingTaskJob() {
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
        String[] id = jobKey.getName().split("\\.");
        Long taskId = Long.parseLong(id[1]);
        zdTaskService.runTask(taskId, jobKey);
    }
}

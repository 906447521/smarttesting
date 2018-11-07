package smarttesting.service.job;

import org.quartz.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 */
@Component
public class ClusteredJob {

    @Resource
    org.springframework.scheduling.quartz.SchedulerFactoryBean
            schedulerFactoryBean;

    public ClusteredJob add
            (String jobName, Class jobClass, String Cron) throws Exception {

        add(jobName, jobClass, Cron, null);
        return this;
    }

    public ClusteredJob add
            (String jobName, Class jobClass, String Cron, Map data) throws Exception {

        if (data == null) {
            data = new HashMap();
        }

        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        String jobGroupName = jobName + ".g";
        String triggerName = jobName + ".t";
        String triggerGroupName = jobName + ".tg";
        scheduler.scheduleJob(
                JobBuilder.newJob()
                        .withIdentity(jobName, jobGroupName)
                        .storeDurably(true)
                        .ofType(jobClass)
                        .setJobData(new JobDataMap(data))
                        .build(),
                TriggerBuilder.newTrigger()
                        .withIdentity(triggerName, triggerGroupName)
                        .withSchedule(
                                CronScheduleBuilder.cronSchedule(Cron).withMisfireHandlingInstructionDoNothing())
                        .build());
        return this;
    }

    public ClusteredJob remove
            (String jobName) throws Exception {


        String jobGroupName = jobName + ".g";
        String triggerName = jobName + ".t";
        String triggerGroupName = jobName + ".tg";

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
        scheduler.pauseTrigger(triggerKey);
        scheduler.unscheduleJob(triggerKey);
        scheduler.deleteJob(new JobKey(jobName, jobGroupName));
        return this;
    }

    public ClusteredJob pause
            (String jobName) throws Exception {
        String jobGroupName = jobName + ".g";
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.pauseJob(new JobKey(jobName, jobGroupName));
        return this;
    }

}

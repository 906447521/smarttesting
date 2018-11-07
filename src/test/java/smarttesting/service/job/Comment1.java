package smarttesting.service.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;
import smarttesting.service.ZDUserService;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author
 */
@Service
public class Comment1 implements Job {

    @Resource
    ZDUserService zdUserService;

    public Comment1() {
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date()));
    }
}

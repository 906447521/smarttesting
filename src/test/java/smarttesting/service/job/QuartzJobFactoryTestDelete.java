package smarttesting.service.job;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QuartzJobFactoryTestDelete {


    @Test
    public void test() throws Exception {
        ClassPathXmlApplicationContext xmlApplicationContext = new ClassPathXmlApplicationContext("spring.xml");
        ClusteredScheduler quartzScheduler = xmlApplicationContext.getBean(ClusteredScheduler.class);
        quartzScheduler.remove("job1");

    }
}
package smarttesting.service.job;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QuartzJobFactoryTestAdd {


    @Test
    public void test() throws Exception {
        ClassPathXmlApplicationContext xmlApplicationContext = new ClassPathXmlApplicationContext("spring.xml");
        ClusteredScheduler quartzScheduler = xmlApplicationContext.getBean(ClusteredScheduler.class);
        quartzScheduler.add(
                "job1",
                Comment1.class,
                "*/10 * * * * ?");


    }
}
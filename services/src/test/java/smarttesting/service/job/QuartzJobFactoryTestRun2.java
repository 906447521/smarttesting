package smarttesting.service.job;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QuartzJobFactoryTestRun2 {


    @Test
    public void test() throws Exception {
        ClassPathXmlApplicationContext xmlApplicationContext = new ClassPathXmlApplicationContext("spring.xml");
        synchronized (xmlApplicationContext) {
            xmlApplicationContext.wait();
        }


    }
}
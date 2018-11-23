package smarttesting.service.engine;

import org.junit.Test;
import smarttesting.service.model.CallerRequest;

public class ZDJmeterRunnerTest {
    @Test
    public void run() {
        CallerJMeter runner = new CallerJMeter("/developer/servers/apache-jmeter-5.0");
        CallerRequest request = new CallerRequest();
        request.setRequestURL(
                " -n -t /developer/git/zdtest/src/main/resources/files/defaults.jmx -r -l /export/jmeter/defaults.jtl -e -o /export/jmeter/report");
        runner.run(request);
    }
}
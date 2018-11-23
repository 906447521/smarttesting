package smarttesting.utils;

import org.apache.commons.io.FileUtils;
import smarttesting.Context;
import smarttesting.utils.uuid.UUID;

import java.io.*;

/**
 * @author
 */
public class JMeter {
    String jmeterOut;
    String rltOut;
    String uuid = new UUID().toString32();
    String jMeterHome;
    String cmd;

    public void setJMeterHome(String jMeterHome) {
        this.jMeterHome = jMeterHome;
    }

    String readAnswer(InputStream inputStream) throws IOException {
        InputStreamReader ir = new InputStreamReader(inputStream);
        BufferedReader in = new BufferedReader(ir);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        in.close();
        return stringBuilder.toString();
    }

    void writeTmp(String script) throws IOException {
        jmeterOut = script;
    }

    void clear() {
        FileUtils.deleteQuietly(new File(jmeterOut));
    }

    public String exec(String script) {

        Process proc;
        try {
            writeTmp(script);
            File file = new File(script);
            if (!file.exists()) {
                throw new FileNotFoundException("file not found: " + file.getPath());
            }
            String fileName = file.getName().replace(".", "_");
            String reportBase = Context.getBaseDir() + "/jmeter/";
            String reportDir = reportBase + fileName;
            String reportHtmlDir = reportDir + "_";
            FileUtils.forceMkdir(new File(reportBase));
            FileUtils.deleteDirectory(new File(reportHtmlDir));
            FileUtils.forceMkdir(new File(reportHtmlDir));
            FileUtils.deleteDirectory(new File(reportDir));
            FileUtils.forceMkdir(new File(reportDir));
            if (jMeterHome == null || "".equals(jMeterHome.trim())) {
                cmd = "jmeter -n -t " + jmeterOut + " -l " + reportDir + "/result.jtl -e -o " + reportHtmlDir;
            } else {
                cmd = jMeterHome + "/bin/jmeter -n -t " + jmeterOut + " -l " + reportDir + "/result.jtl -e -o " + reportHtmlDir;
            }
            System.out.println(" =>  " + cmd);
            proc = Runtime.getRuntime().exec(cmd);
            proc.waitFor();
            String error = readAnswer(proc.getErrorStream());
            String result = readAnswer(proc.getInputStream());
            return "".equals(error) ? result : error;
        } catch (Exception e) {
            return e.getMessage();
        } finally {

        }

    }

    public static void main(String[] args) {
        JMeter jMeter = new JMeter();
        jMeter.setJMeterHome("/developer/servers/apache-jmeter-5.0");
        String script = "/developer/git/zdtest/src/main/resources/files/defaults.jmx";
        System.out.println(jMeter.exec(script));
    }
}

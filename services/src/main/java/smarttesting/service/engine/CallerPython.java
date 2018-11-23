package smarttesting.service.engine;

import smarttesting.service.model.CallerRequest;
import smarttesting.service.model.CallerResult;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author
 */
public class CallerPython {
    String pythonHome;

    public CallerPython(String pythonHome) {
        this.pythonHome = pythonHome;
    }


    public CallerResult run(CallerRequest request) {
        CallerResult result = new CallerResult(request);
        InputStream in = null;

        String shell = "";
        try {
            Runtime.getRuntime().exec("cmd cd");
            shell = "cmd ./jmeter.bat " + request.getRequestURL();
        } catch (Exception e) {
            shell = "sh  ./jmeter     " + request.getRequestURL();
        }

        System.out.println(shell);
        try {
            Process pro = Runtime.getRuntime().exec(shell);
            pro.waitFor();
            in = pro.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            System.out.println("INFO:" + read.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}

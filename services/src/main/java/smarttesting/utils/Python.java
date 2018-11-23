package smarttesting.utils;

import org.apache.commons.io.FileUtils;
import smarttesting.Context;
import smarttesting.utils.uuid.UUID;

import java.io.*;

/**
 * @author
 */
public class Python {

    String pyOut;
    String rltOut;
    String uuid = new UUID().toString32();
    String pythonHome;
    String cmd;

    public void setPythonHome(String pythonHome) {
        this.pythonHome = pythonHome;
    }

    String readPyAnswer(InputStream inputStream) throws IOException {
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

    void writeTmpPy(String script) throws IOException {
        pyOut = Context.getBaseDir() + "/tmp/" + uuid + ".py";
        FileUtils.writeStringToFile(new File(pyOut), script);
    }

    void clear() {
        FileUtils.deleteQuietly(new File(pyOut));
    }

    public String exec(String script) {

        Process proc;
        try {
            writeTmpPy(script);
            if (pythonHome == null || "".equals(pythonHome.trim())) {
                cmd = "python " + pyOut;
            } else {
                File file = FileUtils.getFile(pythonHome + "/bin", "python3");
                cmd = pythonHome + "/bin/" + (file.exists() ? "python3" : "python") + " " + pyOut;
            }
            System.out.println(" =>  " + cmd);
            proc = Runtime.getRuntime().exec(cmd);
            proc.waitFor();
            String error = readPyAnswer(proc.getErrorStream());
            String result = readPyAnswer(proc.getInputStream());
            return "".equals(error) ? result : error;
        } catch (Exception e) {
            return e.getMessage();
        } finally {
            clear();
        }

    }

    public static void main(String[] args) {
        Python python = new Python();
//        python.setPythonHome("/Library/Frameworks/Python.framework/Versions/3.7");
        String script = "import sys \n" +
                "def a():\n" +
                " return 1\n" +
                "print a()";
        System.out.println(script);
        System.out.println(python.exec(script));
    }
}

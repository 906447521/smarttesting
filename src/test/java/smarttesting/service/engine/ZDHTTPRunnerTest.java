package smarttesting.service.engine;

import org.junit.Test;
import smarttesting.service.model.CallerRequest;
import smarttesting.utils.JSON;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.IdentityHashMap;

public class ZDHTTPRunnerTest {

    @Test
    public void a()

    {

        String a= "[{\"a\":\"2\",\"b\":\"3\",\"c\":{\"c_a\":1,\"c_b\":2}},{\"a\":\"2\",\"b\":\"3\",\"c\":{\"c_a\":1,\"c_b\":2}}]";

        System.out.println(JSON.read("{ \"ids[]\":1, \"ids[]\":2 }", IdentityHashMap.class));

        IdentityHashMap map = new IdentityHashMap();
        map.put(new String("a"), "1");
        map.put("a", "2");
        System.out.println(map);
        IdentityHashMap headerValue = JSON.read("{ \"ids[]\":1, \"ids[]\":2 }", IdentityHashMap.class);
        System.out.println(headerValue);

        IdentityHashMap headerValue2 = JSON.read("{ \"ids\":\"[1,2]\"}", IdentityHashMap.class);
        System.out.println(headerValue2);
    }

    @Test
    public void run() {


        CallerHTTP runner = new CallerHTTP();
        CallerRequest request = new CallerRequest();
        request.setRequestURL("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm");
        request.setRequestHeaderProperties("{}");
        request.setRequestBody("{\"tel\":\"13888888888\"}");
        request.setResponseCharset("GBK");

        System.out.println(JSON.writerPretty(runner.run(request)));


    }

    @Test
    public void jsonRun() {
        String result = "";
        BufferedReader reader = null;
        try {
            URL url = new URL("http://127.0.0.1:6060/data/test/map_json.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("accept", "application/json");
            byte[] writebytes = "{\"name\":\"zdtest\",\"id\":1,\"c\":\"3\",\"d\":\"2\"}".getBytes();
            conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
            OutputStream outwritestream = conn.getOutputStream();
            outwritestream.write(writebytes);
            outwritestream.flush();
            outwritestream.close();
            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                result = reader.readLine();
                System.out.println(result);
            } else {
                System.out.println(conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
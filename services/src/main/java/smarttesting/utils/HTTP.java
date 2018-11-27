package smarttesting.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * @author
 */
public class HTTP implements Closeable {

    public static HTTP open(String url) {
        return new HTTP(url);
    }

    HttpURLConnection   conn;
    String              URL;
    String              RequestContentType;
    String              RequestMethod;
    String              RequestCharset;
    Map<String, String> RequestHeaderProperties;
    String              ResponseCharset;

    private HTTP(String url) {
        this.URL = url;
        withRequestContentType("application/x-www-form-urlencoded");
        withRequestMethod("POST");
        withRequestHeaderProperties(new HashMap<String, String>());
        withResponseCharset("UTF8");
    }


    // application/json
    // application/x-www-form-urlencoded
    // multipart/form-data
    // conn.setRequestProperty("Content-Type", this.ContentType);
    public HTTP withRequestContentType(String RequestContentType) {
        this.RequestContentType = RequestContentType;
        return this;
    }

    // post
    // get
    //  try {
    //            conn.setRequestMethod("POST");
    //        } catch (ProtocolException e) {
    //            e.printStackTrace();
    //        }
    public HTTP withRequestMethod(String RequestMethod) {
        this.RequestMethod = RequestMethod;
        return this;
    }

    // utf8   gbk
    // conn.setRequestProperty("Accept-Charset", charset);
    public HTTP withRequestCharset(String RequestCharset) {
        this.RequestCharset = RequestCharset;
        return this;
    }

    // utf8   gbk
    public HTTP withResponseCharset(String ResponseCharset) {
        this.ResponseCharset = ResponseCharset;
        return this;
    }

    //Accept:application/json, text/javascript, */*; q=0.01
    //Accept-Encoding:gzip, deflate, br
    //Accept-Language:zh-CN,zh;q=0.9,en;q=0.8,ja;q=0.7
    //Connection:keep-alive
    //Cookie:iconSize=16x16; jenkins-timestamper-offset=-28800000;
    //Host:127.0.0.1:6060
    //Referer:http://127.0.0.1:6060/project/detail_case.html?id=37
    //User-Agent:Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36
    //X-Requested-With:XMLHttpRequest
    public HTTP withRequestHeaderProperties(Map<String, String> RequestHeaderProperties) {
        this.RequestHeaderProperties = RequestHeaderProperties;
        return this;
    }


    public Response send(Request request) throws IOException {

        Response response = new Response();
        OutputStream out = null;
        InputStream in = null;
        try {

            URL wsUrl = new URL(this.URL);
            conn = (HttpURLConnection) wsUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", this.RequestContentType);
            conn.setRequestMethod(this.RequestMethod);

            if (RequestHeaderProperties != null) {
                for (Map.Entry<String, String> entry : RequestHeaderProperties.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            if (request != null) {

                String query;
                if (!"application/json".equals(this.RequestContentType)) {
                    query = request.getQueryString();
                } else {
                    query = request.getJsonString();
                }


                if (!"".equals(query.trim())) {

                    System.out.println("Query: [" + this.RequestContentType + "]  " + query);
                    byte[] writeBytes = query.getBytes();

                    conn.setDoOutput(true);
                    out = conn.getOutputStream();
                    out.write(writeBytes);
                    out.flush();
                }

            }

            response.status = conn.getResponseCode();

            try {
                in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, this.ResponseCharset));
                StringBuilder buffer = new StringBuilder();
                String s;
                while ((s = reader.readLine()) != null) {
                    buffer.append(s);
                }
                response.value = buffer.toString();
            } catch (Exception e) {
                response.value = e.getMessage();
                try {
                    in = conn.getErrorStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, this.ResponseCharset));
                    StringBuilder buffer = new StringBuilder();
                    String s;
                    while ((s = reader.readLine()) != null) {
                        buffer.append(s);
                    }
                    response.value = buffer.toString();
                } catch (Exception e2) {

                }
            }


        } finally {
            IO.closeQuietly(in, out);
        }
        return response;
    }

    public void release() {
        if (conn != null) {
            conn.disconnect();
        }

    }

    public void close() throws IOException {
        release();
    }

    public static class Request {

        Object formData;
        int index = 0;

        public void parameter(Object key, Object value) {
            if (formData == null) {
                formData = new HashMap();
            }
            ((Map) formData).put(key, value);
        }

        public void parameter(Object object) {
            formData = object;
        }

        public String getJsonString() {
            if (formData instanceof String) {
                if ("".equals(((String) formData).trim())) {
                    return "";
                }
            }
            String body = JSON.write(formData);
            return body;
        }

        public String getQueryString() {
            if (formData instanceof Map) {
                Set<Map.Entry<String, Object>> entrySet = ((Map) formData).entrySet();
                Map.Entry[] entries = entrySet.toArray(new Map.Entry[entrySet.size()]);
                StringBuilder stringBuilder = new StringBuilder();
                index = 0;
                for (int i = 0; i < entries.length; i++) {
                    Map.Entry<String, Object> entry = entries[i];
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (value != null) {
                        append(stringBuilder, key, value);
                    }

                }
                return stringBuilder.toString();
            } else if (formData instanceof List) {
                return "";
            } else if (formData instanceof String) {
                return (String) formData;
            } else {
                return "";
            }

        }

        public void append(StringBuilder stringBuilder, String key, Object value) {

            if (value instanceof ArrayList) {
                ArrayList<Object> values = (ArrayList<Object>) value;
                if (values != null) {
                    for (Object val : values) {
                        append(stringBuilder, key + "[]", val);
                    }
                }

            } else if (value instanceof Map) {
                Map<String, Object> maps = (Map) value;
                if (maps != null) {
                    Set<Map.Entry<String, Object>> entrySet = maps.entrySet();
                    for (Map.Entry<String, Object> entry : entrySet) {
                        append(stringBuilder, key + "." + entry.getKey(), entry.getValue());
                    }

                }
            } else {
                if (index != 0) {
                    stringBuilder.append("&");
                }
                index++;
                stringBuilder.append(key).append("=").append(value);
            }
        }
    }


    public static class Response {
        String value;
        int    status;

        public String getValue() {
            return value;
        }

        public int getStatus() {
            return status;
        }
    }


}

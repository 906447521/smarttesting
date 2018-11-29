package smarttesting.service.engine;

import com.fasterxml.jackson.core.type.TypeReference;
import smarttesting.service.model.CallerRequest;
import smarttesting.service.model.CallerResult;
import smarttesting.utils.HTTP;
import smarttesting.utils.JSON;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
public class CallerHTTP implements Caller {

    @Override
    public CallerResult run(CallerRequest request) {

        CallerResult result = new CallerResult(request);
        try {

            result.setBegin(System.currentTimeMillis());

            HTTP http = HTTP.open(request.getRequestURL());

            String ResponseCharset = request.getResponseCharset();
            String RequestMethod = request.getRequestMethod();
            String RequestContentType = request.getRequestContentType();
            String RequestHeaderProperties = request.getRequestHeaderProperties();
            String RequestBody = request.getRequestBody();

            if ("gbk".equals(ResponseCharset == null ? "" : ResponseCharset.toLowerCase())) {
                http.withResponseCharset("GBK");
            }
            if ("get".equals(RequestMethod == null ? "" : RequestMethod.toLowerCase())) {
                http.withRequestMethod("GET");
            }
            if (!("".equals(RequestContentType) || RequestContentType == null)) {
                http.withRequestContentType(RequestContentType);
            }

            HTTP.Request httpRequest = new HTTP.Request();

            if (RequestHeaderProperties != null) {
                try {
                    LinkedHashMap headerValue = JSON.read(RequestHeaderProperties, LinkedHashMap.class);
                    http.withRequestHeaderProperties(headerValue);
                } catch (Exception e) {

                }
            }

            Object parameter;
            if (RequestBody != null) {

                try {
                    parameter = JSON.readUnsafe(RequestBody, LinkedHashMap.class);
                } catch (Exception e) {
                    try {
                        parameter = JSON.readUnsafe(RequestBody, new TypeReference<List<Map>>() {
                        });
                    } catch (Exception e1) {
                        parameter = RequestBody;
                    }
                }
                httpRequest.parameter(parameter);
            }


            HTTP.Response response = http.send(httpRequest);

            http.release();
            result.setResponseCode(String.valueOf(response.getStatus()));
            result.setResponseBody(response.getValue());


        } catch (Exception e) {
            result.setResponseCode("0");
            result.setResponseBody(e.getMessage());
            e.printStackTrace();
        } finally {
            result.setEnd(System.currentTimeMillis());
            result.setDuration(result.getEnd() - result.getBegin());
        }

        try {
            result.setResponseResult(
                    String.valueOf(
                            ScriptOgnl.doScript(
                                    result.getResponseCode(),
                                    result.getResponseBody(),
                                    request.getResultScript()
                            )));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}

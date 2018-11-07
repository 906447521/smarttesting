package smarttesting.service.model;

/**
 * @author
 */
public class CallerResult {


    public CallerResult(CallerRequest request) {
        this.request = request;
    }

    CallerRequest request;
    String        responseCode;
    String        responseBody;
    String        responseResult;  // 预期执行结果执行后的值
    Long begin    = System.currentTimeMillis();
    Long end      = System.currentTimeMillis();
    Long duration = 1L;

    public String getResponseResult() {
        return responseResult;
    }

    public void setResponseResult(String responseResult) {
        this.responseResult = responseResult;
    }

    public CallerRequest getRequest() {
        return request;
    }

    public void setRequest(CallerRequest request) {
        this.request = request;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Long getBegin() {
        return begin;
    }

    public void setBegin(Long begin) {
        this.begin = begin;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}

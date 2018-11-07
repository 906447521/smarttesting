package smarttesting.service.model;

/**
 * @author
 */
public class CallerRequest {

    private String requestCaseName;
    private String requestMethod;
    private String requestCharset;
    private String requestURL;
    private String requestHeaderProperties;
    private String requestContentType;
    private String requestBody;
    private String responseCharset;
    private String resultScript;

    public String getResultScript() {
        return resultScript;
    }

    public void setResultScript(String resultScript) {
        this.resultScript = resultScript;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestCaseName() {
        return requestCaseName;
    }

    public void setRequestCaseName(String requestCaseName) {
        this.requestCaseName = requestCaseName;
    }

    public String getRequestCharset() {
        return requestCharset;
    }

    public void setRequestCharset(String requestCharset) {
        this.requestCharset = requestCharset;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getRequestHeaderProperties() {
        return requestHeaderProperties;
    }

    public void setRequestHeaderProperties(String requestHeaderProperties) {
        this.requestHeaderProperties = requestHeaderProperties;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseCharset() {
        return responseCharset;
    }

    public void setResponseCharset(String responseCharset) {
        this.responseCharset = responseCharset;
    }
}

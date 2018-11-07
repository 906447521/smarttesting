package smarttesting.data.model;

/**
 * @author
 */
@Table(value = "zd_interface")
public class ZDInterface {
    @Column(value = "id")
    private Long   id;
    @Column(value = "project_id")
    private Long   projectId;
    @Column(value = "name")
    private String name;

    // 接口类型，默认http
    @Column(value = "type")
    private String type;

    // 接口地址
    @Column(value = "url")
    private String url;

    // 接口方法，GET POST等
    @Column(value = "method")
    private String method;

    // 接口入参（头），COOKIE写在这个里面
    @Column(value = "request_header")
    private String requestHeader;

    // 接口返回字符串类型
    @Column(value = "response_charset")
    private String responseCharset;

    public String getResponseCharset() {
        return responseCharset;
    }

    public void setResponseCharset(String responseCharset) {
        this.responseCharset = responseCharset;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }
}

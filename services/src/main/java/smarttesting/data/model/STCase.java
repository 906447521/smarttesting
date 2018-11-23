package smarttesting.data.model;

/**
 * @author
 */
@Table(value = "zd_case")
public class STCase {
    @Column(value = "id")
    private Long id;

    // 项目
    @Column(value = "project_id")
    private Long projectId;

    // 接口
    @Column(value = "interface_id")
    private Long interfaceId;

    //
    @Column(value = "name")
    private String name;

    //
    @Column(value = "method")
    private String method;

    @Column(value = "url")
    private String url;

    // 接口域名后缀
    @Column(value = "url_suffix")
    private String urlSuffix;

    //
    @Column(value = "content_type")
    private String contentType;

    // 入参
    @Column(value = "request_body")
    private String requestBody;

    // 预期结果
    @Column(value = "result_script")
    private String resultScript;


    private STInterface zdInterface;


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResultScript() {
        return resultScript;
    }

    public void setResultScript(String resultScript) {
        this.resultScript = resultScript;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUrlSuffix() {
        return urlSuffix;
    }

    public void setUrlSuffix(String urlSuffix) {
        this.urlSuffix = urlSuffix;
    }

    public STInterface getZdInterface() {
        return zdInterface;
    }

    public void setZdInterface(STInterface zdInterface) {
        this.zdInterface = zdInterface;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(Long interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}

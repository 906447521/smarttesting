package smarttesting.data.model;

/**
 * @author
 */
public class STScene {
    @Column(value = "id")
    private Long id;

    // 项目
    @Column(value = "project_id")
    private Long   projectId;
    //
    @Column(value = "name")
    private String name;
    //
    @Column(value = "cases")
    private String cases;

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

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }
}

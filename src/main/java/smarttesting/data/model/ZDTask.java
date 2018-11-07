package smarttesting.data.model;

/**
 * @author
 */
@Table(value = "zd_task")
public class ZDTask {
    @Column(value = "id")
    private Long   id;
    @Column(value = "project_id")
    private Long   projectId;
    @Column(value = "name")
    private String name;
    @Column(value = "con")
    private String con;
    @Column(value = "cases")
    private String cases;
    @Column(value = "lastrun")
    private String lastrun;

    public String getLastrun() {
        return lastrun;
    }

    public void setLastrun(String lastrun) {
        this.lastrun = lastrun;
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

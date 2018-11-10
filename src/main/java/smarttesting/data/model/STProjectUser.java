package smarttesting.data.model;

/**
 * @author
 */
public class STProjectUser {

    @Column(value = "id")
    private Long   id;
    @Column(value = "project_id")
    private Long   projectId;
    @Column(value = "user_name")
    private String userName;
    @Column(value = "role")
    private Integer role;

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}

package smarttesting.data.model;

/**
 * @author
 */
@Table(value = "zd_user")
public class STUser {

    @Column(value = "id")
    private Long   id;
    @Column(value = "name")
    private String name;
    @Column(value = "group")
    private String group;

    @Column(value = "oldPwd")
    private String oldPwd;

    @Column(value = "pwd")
    private String pwd;

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}

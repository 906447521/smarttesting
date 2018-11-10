package smarttesting.data.model;

/**
 * @author
 */
@Table(value = "zd_project")
public class STProject {

    @Column(value = "id")
    private Long   id;
    @Column(value = "name")
    private String name;
    @Column(value = "creator")
    private String creator;

    private int caseCount;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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

    public int getCaseCount() {
        return caseCount;
    }

    public void setCaseCount(int caseCount) {
        this.caseCount = caseCount;
    }
}

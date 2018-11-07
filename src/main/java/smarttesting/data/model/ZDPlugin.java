package smarttesting.data.model;

/**
 * @author
 */
@Table(value = "zd_plugin")
public class ZDPlugin {

    @Column(value = "id")
    private Long   id;
    @Column(value = "name")
    private String name;
    @Column(value = "home")
    private String home;

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

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }
}

package smarttesting.data.model;

import java.util.Date;

/**
 * @author
 */
@Table(value = "zd_task")
public class STTask {
    @Column(value = "id")
    private Long    id;
    @Column(value = "project_id")
    private Long    projectId;
    @Column(value = "name")
    private String  name;
    @Column(value = "con")
    private String  con;
    @Column(value = "scenes")
    private String  scenes;
    @Column(value = "run")
    private Boolean run;
    @Column(value = "lastrun")
    private String  lastrun;
    @Column(value = "lastruntime")
    private Date    lastruntime;
    @Column(value = "lastrunstatus")
    private String  lastrunstatus;

    public Boolean getRun() {
        return run;
    }

    public void setRun(Boolean run) {
        this.run = run;
    }

    public Date getLastruntime() {
        return lastruntime;
    }

    public void setLastruntime(Date lastruntime) {
        this.lastruntime = lastruntime;
    }

    public String getLastrunstatus() {
        return lastrunstatus;
    }

    public void setLastrunstatus(String lastrunstatus) {
        this.lastrunstatus = lastrunstatus;
    }

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

    public String getScenes() {
        return scenes;
    }

    public void setScenes(String scenes) {
        this.scenes = scenes;
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

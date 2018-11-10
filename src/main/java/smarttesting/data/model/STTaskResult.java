package smarttesting.data.model;

import java.util.Date;

/**
 * @author
 */
public class STTaskResult {
    @Column(value = "id")
    private Long   id;
    @Column(value = "tid")
    private Long   tid;
    @Column(value = "rid")
    private String rid;
    @Column(value = "result")
    private String result;
    @Column(value = "time")
    private Date   time;
    @Column(value = "start")
    private Date   start;
    @Column(value = "end")
    private Date   end;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}

package smarttesting.utils.sso;

import java.util.Date;

public class Ticket {

    private final static ThreadLocal<Ticket> holder = new ThreadLocal<Ticket>();
    private int _version = 0;
    private String _userName;
    private String _userData;
    private String _appPath;
    private Date _expires;
    private Date _issueDate;

    private boolean _isPersistent = false;

    public int getVersion() {
        return _version;
    }

    public String getUsername() {
        return _userName;
    }

    public String getUserData() {
        return _userData;
    }

    public String getAppPath() {
        return _appPath;
    }

    public Date getExpires() {
        return _expires;
    }

    public Date getIssueDate() {
        return _issueDate;
    }

    public boolean isPersistent() {
        return _isPersistent;
    }

    public boolean isExpired() {
        return (new Date()).after(_expires);
    }

    public Ticket(String username) throws Exception {
        this(username, null, null, null, null, 2, false);
    }

    public Ticket(String username, String userdata) throws Exception {
        this(username, userdata, null, null, null, 2, false);
    }

    public Ticket(String username, long expire) throws Exception {
        this(username, null, null, new Date(), new Date(System.currentTimeMillis() + expire), 2, false);
    }

    public Ticket(String username, String userdata, String appPath, Date issued, Date expires, int version, boolean isPersistent) throws Exception {
        if (username == null || username == null)
            throw new Exception("username");
        else
            _userName = username;
        if (userdata == null)
            _userData = "";
        else
            _userData = userdata;
        if (appPath == null)
            _appPath = "/";
        else if (!appPath.startsWith("/"))
            _appPath = "/" + appPath;
        else
            _appPath = appPath;
        if (issued == null)
            _issueDate = new Date();
        else
            _issueDate = issued;
        if (expires == null)
            _expires = new Date(System.currentTimeMillis() + 30 * 1000 * 60);
        else
            _expires = expires;
        if (version > 0)
            _version = version;
        else
            _version = 1;

    }

    public String toString() {
        return "version=" + _version + "," + "userName=" + _userName + "," + "userData=" + _userData + "," + "appPath=" + _appPath + "," + "isPersistent=" + _isPersistent + ","
                + "issueDate=" + _issueDate + "," + "expires=" + _expires;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Ticket ticket = (Ticket) o;

        if (_isPersistent != ticket._isPersistent)
            return false;
        if (_version != ticket._version)
            return false;
        if (_appPath != null ? !_appPath.equals(ticket._appPath) : ticket._appPath != null)
            return false;
        if (_expires != null ? !_expires.equals(ticket._expires) : ticket._expires != null)
            return false;
        if (_issueDate != null ? !_issueDate.equals(ticket._issueDate) : ticket._issueDate != null)
            return false;
        if (_userData != null ? !_userData.equals(ticket._userData) : ticket._userData != null)
            return false;
        if (_userName != null ? !_userName.equals(ticket._userName) : ticket._userName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _version;
        result = 31 * result + (_userName != null ? _userName.hashCode() : 0);
        result = 31 * result + (_userData != null ? _userData.hashCode() : 0);
        result = 31 * result + (_appPath != null ? _appPath.hashCode() : 0);
        result = 31 * result + (_expires != null ? _expires.hashCode() : 0);
        result = 31 * result + (_issueDate != null ? _issueDate.hashCode() : 0);
        result = 31 * result + (_isPersistent ? 1 : 0);
        return result;
    }

    public static void setTicket(Ticket ticket) {
        holder.set(ticket);
    }

    public static Ticket getTicket() {
        return holder.get();
    }

    public static void remove() {
        holder.remove();
    }

}
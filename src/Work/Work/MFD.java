package Work;

import java.util.ArrayList;
import java.util.List;

public class MFD {
    private String username;
    private String pwd;
    private List<UFD> ufds = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public List getUfds() {
        return ufds;
    }

    public void setUfds(UFD ufd) {
        this.ufds.add(ufd);
    }
}

package Work;

import java.util.Date;

public class FCB {
    private String filename;
    private String fileid;
    private String username;
    private String filepath;
    private long length;
    private String filetype;
    private int filemode;//0-readonly 1-writeonly 2-write/read
    private String shareinfo;
    private Date buildtime;
    private Date readtime;
    private Date writetime;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public int getFilemode() {
        return filemode;
    }

    public void setFilemode(int filemode) {
        this.filemode = filemode;
    }

    public String getShareinfo() {
        return shareinfo;
    }

    public void setShareinfo(String shareinfo) {
        this.shareinfo = shareinfo;
    }

    public Date getBuildtime() {
        return buildtime;
    }

    public void setBuildtime(Date buildtime) {
        this.buildtime = buildtime;
    }

    public Date getReadtime() {
        return readtime;
    }

    public void setReadtime(Date readtime) {
        this.readtime = readtime;
    }

    public Date getWritetime() {
        return writetime;
    }

    public void setWritetime(Date writetime) {
        this.writetime = writetime;
    }
}

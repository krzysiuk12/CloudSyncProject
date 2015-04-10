package pl.edu.agh.iosr.cloud.common.files;

import java.util.Date;

/**
 * Created by Krzysztof Kicinger on 2015-04-11.
 */
public class CloudPath {

    private Long id;
    private String path;
    private CloudPathType type;
    private String fileName;
    private String extension;
    private long size;
    private Date lastModificationDate;

    public CloudPath() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public CloudPathType getType() {
        return type;
    }

    public void setType(CloudPathType type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }
}

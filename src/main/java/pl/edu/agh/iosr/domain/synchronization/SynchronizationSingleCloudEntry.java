package pl.edu.agh.iosr.domain.synchronization;

import pl.edu.agh.iosr.domain.cloud.CloudType;
import pl.edu.agh.iosr.domain.cloud.files.CloudPath;

/**
 * Created by Krzysztof Kicinger on 2015-05-20.
 */
public class SynchronizationSingleCloudEntry {

    private String sessionId;
    private CloudPath cloudPath;
    private String fileName;

    public SynchronizationSingleCloudEntry() {
    }

    public SynchronizationSingleCloudEntry(String sessionId, CloudPath cloudPath, String fileName) {
        this.sessionId = sessionId;
        this.cloudPath = cloudPath;
        this.fileName = fileName;
    }

    public SynchronizationSingleCloudEntry(String sessionId, CloudPath cloudPath) {
        this(sessionId, cloudPath, null);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public CloudPath getCloudPath() {
        return cloudPath;
    }

    public void setCloudPath(CloudPath cloudPath) {
        this.cloudPath = cloudPath;
    }

    public String getFileName() {
/*        if (CloudType.GOOGLE_DRIVE.equals(cloudPath.getType())) {
            return cloudPath.getPath();
        } else {
            return cloudPath.getPath().substring(cloudPath.getPath().lastIndexOf("/"));
        }*/
        return fileName;
    }

    public CloudPath getDirectory() {
        if (CloudType.GOOGLE_DRIVE.equals(cloudPath.getType())) {
            return new CloudPath("root", CloudType.GOOGLE_DRIVE);
        } else {
            return new CloudPath(cloudPath.getPath().substring(0, cloudPath.getPath().lastIndexOf("/")), cloudPath.getType());
        }
    }
}

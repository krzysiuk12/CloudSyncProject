package pl.edu.agh.iosr.cloud.googledrive.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.services.drive.Drive;
import pl.edu.agh.iosr.cloud.common.CloudType;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.cloud.common.session.CloudSessionStatus;

/**
 * Created by Mateusz Drożdż on 18.04.15.
 */
public class GoogleDriveCloudSession extends CloudSession {

    @JsonIgnore
    private Drive drive;

    public GoogleDriveCloudSession() {
    }

    public GoogleDriveCloudSession(String sessionId, String authorizationCode, String accessToken, CloudSessionStatus status, Drive drive) {
        super(sessionId, authorizationCode, accessToken, CloudType.GOOGLE_DRIVE, status);
        this.drive = drive;
    }

    public Drive getDrive() {
        return drive;
    }

    public void setDrive(Drive drive) {
        this.drive = drive;
    }
}

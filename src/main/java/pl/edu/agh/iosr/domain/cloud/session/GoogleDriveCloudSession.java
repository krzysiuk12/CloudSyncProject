package pl.edu.agh.iosr.domain.cloud.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.services.drive.Drive;
import pl.edu.agh.iosr.domain.cloud.CloudType;
import pl.edu.agh.iosr.domain.cloud.session.CloudSession;
import pl.edu.agh.iosr.domain.cloud.session.CloudSessionStatus;

/**
 * Created by Mateusz Drożdż on 18.04.15.
 */
public class GoogleDriveCloudSession extends CloudSession {

    @JsonIgnore
    private Drive drive;

    public GoogleDriveCloudSession() {
    }

    public GoogleDriveCloudSession(String authorizationCode, String accessToken, CloudSessionStatus status, Drive drive) {
        super(authorizationCode, accessToken, CloudType.GOOGLE_DRIVE, status);
        this.drive = drive;
    }

    public Drive getDrive() {
        return drive;
    }

    public void setDrive(Drive drive) {
        this.drive = drive;
    }
}

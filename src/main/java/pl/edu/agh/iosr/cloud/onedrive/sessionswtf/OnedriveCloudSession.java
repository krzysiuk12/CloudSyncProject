package pl.edu.agh.iosr.cloud.onedrive.sessionswtf;

import pl.edu.agh.iosr.cloud.common.CloudType;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.cloud.common.session.CloudSessionStatus;

public class OnedriveCloudSession extends CloudSession {

    public OnedriveCloudSession(String sessionId, String authorizationCode, String accessToken, CloudSessionStatus status) {
        super(sessionId, authorizationCode, accessToken, CloudType.ONE_DRIVE, status);
    }

}
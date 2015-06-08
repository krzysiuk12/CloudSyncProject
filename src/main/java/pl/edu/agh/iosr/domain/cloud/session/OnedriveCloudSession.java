package pl.edu.agh.iosr.domain.cloud.session;

import pl.edu.agh.iosr.domain.cloud.CloudType;
import pl.edu.agh.iosr.domain.cloud.session.CloudSession;
import pl.edu.agh.iosr.domain.cloud.session.CloudSessionStatus;

public class OnedriveCloudSession extends CloudSession {

    public OnedriveCloudSession(String authorizationCode, String accessToken, CloudSessionStatus status) {
        super(authorizationCode, accessToken, CloudType.ONE_DRIVE, status);
    }

}

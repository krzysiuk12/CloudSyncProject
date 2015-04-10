package pl.edu.agh.iosr.cloud.common.session;

import pl.edu.agh.iosr.cloud.common.CloudType;

/**
 * Class represents session object connected with cloud service.
 *
 * Created by Krzysztof Kicinger on 2015-04-11.
 */
public abstract class CloudSession extends BasicSession {

    private String authorizationCode;
    private String accessToken;
    private CloudType cloudType;
    private CloudSessionStatus status;

    protected CloudSession() {
    }

    protected CloudSession(String sessionId, String authorizationCode, String accessToken, CloudType cloudType, CloudSessionStatus status) {
        super(sessionId);
        this.authorizationCode = authorizationCode;
        this.accessToken = accessToken;
        this.cloudType = cloudType;
        this.status = status;
    }

    protected CloudSession(String sessionId) {
        super(sessionId);
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public CloudType getCloudType() {
        return cloudType;
    }

    public void setCloudType(CloudType cloudType) {
        this.cloudType = cloudType;
    }

    public CloudSessionStatus getStatus() {
        return status;
    }

    public void setStatus(CloudSessionStatus status) {
        this.status = status;
    }
}

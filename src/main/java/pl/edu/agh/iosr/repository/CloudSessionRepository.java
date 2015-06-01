package pl.edu.agh.iosr.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import pl.edu.agh.iosr.accounts.UserAccount;
import pl.edu.agh.iosr.cloud.common.CloudType;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.cloud.common.session.UserSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Krzysztof Kicinger on 2015-05-20.
 */
@Repository
public class CloudSessionRepository implements ICloudSessionRepository {

    private String cloudSessionSeparator;
    private int cloudSessionSecondPartLength;
    private int cloudSessionThirdPartLength;
    private Map<UserAccount, UserSession> cloudSessionMap = new HashMap<>();

    @Autowired
    public CloudSessionRepository(@Value("${cloudsession.separator}") String cloudSessionSeparator, @Value("${cloudsession.secondpart.length}") int cloudSessionSecondPartLength, @Value("${cloudsession.thirdpart.length}") int cloudSessionThirdPartLength) {
        this.cloudSessionSeparator = cloudSessionSeparator;
        this.cloudSessionSecondPartLength = cloudSessionSecondPartLength;
        this.cloudSessionThirdPartLength = cloudSessionThirdPartLength;
    }

    @Override
    public String addCloudSession(String login, CloudSession cloudSession) {
        String sessionId = getUniqueSessionId(cloudSession.getCloudType());
        cloudSession.setSessionId(sessionId);
        UserSession userSession = getUserSession(login);
        putCloudSessionIntoUserSession(userSession.getUserAccount(), cloudSession);
        cloudSessionMap.put(userSession.getUserAccount(), userSession);
        return sessionId;
    }

    @Override
    public CloudSession getCloudSessionById(String sessionId) {
        for(UserSession userSession : cloudSessionMap.values()) {
            if(userSession.getDropboxCloudSession() != null && sessionId.equals(userSession.getDropboxCloudSession().getSessionId())) {
                return userSession.getDropboxCloudSession();
            } else if(userSession.getGoogleDriveCloudSession() != null && sessionId.equals(userSession.getGoogleDriveCloudSession().getSessionId())) {
                return userSession.getGoogleDriveCloudSession();
            } else if (userSession.getOnedriveCloudSession() != null && sessionId.equals(userSession.getOnedriveCloudSession().getSessionId())) {
                return userSession.getOnedriveCloudSession();
            }
        }
        return null;
    }

    @Override
    public UserSession getUserSessionByLogin(String login) {
        return getUserSession(login);
    }

    @Override
    public CloudSession getCloudSessionByCloudType(String login, CloudType cloudType) {
        UserSession userSession = getUserSession(login);
        switch (cloudType) {
            case DROPBOX: return userSession.getDropboxCloudSession();
            case GOOGLE_DRIVE: return userSession.getGoogleDriveCloudSession();
            case ONE_DRIVE: return userSession.getOnedriveCloudSession();
        }
        return null;
    }

    @Override
    public void removeCloudSession(String sessionId) {
        cloudSessionMap.remove(sessionId);
    }

    private UserSession getUserSession(String login) {
        UserAccount testedUserAccount = new UserAccount(login != null ? login : "DEFAULT");
        if(!cloudSessionMap.containsKey(testedUserAccount)) {
            cloudSessionMap.put(testedUserAccount, new UserSession(testedUserAccount));
        }
        return cloudSessionMap.get(testedUserAccount);
    }

    private void putCloudSessionIntoUserSession(UserAccount userAccount, CloudSession cloudSession) {
        UserSession userSession = cloudSessionMap.get(userAccount);
        if(cloudSession.getCloudType() == CloudType.DROPBOX) {
            userSession.setDropboxCloudSession(cloudSession);
        } else if(cloudSession.getCloudType() == CloudType.GOOGLE_DRIVE) {
            userSession.setGoogleDriveCloudSession(cloudSession);
        } else if(cloudSession.getCloudType() == CloudType.ONE_DRIVE) {
            userSession.setOnedriveCloudSession(cloudSession);
        }
    }

    private String getUniqueSessionId(CloudType type) {
        while(true) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(type.name().toLowerCase()).append(cloudSessionSeparator);
            stringBuilder.append(UUID.randomUUID().toString().substring(0, cloudSessionSecondPartLength)).append(cloudSessionSeparator);
            stringBuilder.append(UUID.randomUUID().toString().substring(0, cloudSessionThirdPartLength));
            if(!cloudSessionMap.containsKey(stringBuilder.toString())) {
                return stringBuilder.toString();
            }
        }
    }

}

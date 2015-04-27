package pl.edu.agh.iosr.cloud.dropbox.services;

import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudSessionService;
import pl.edu.agh.iosr.cloud.common.session.BasicSession;
import pl.edu.agh.iosr.cloud.common.session.CloudSessionStatus;
import pl.edu.agh.iosr.cloud.dropbox.configuration.DropboxCloudConfiguration;
import pl.edu.agh.iosr.cloud.dropbox.session.DropboxCloudSession;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Krzysztof Kicinger on 2015-04-11.
 */
@Service
public class DropboxCloudSessionService implements ICloudSessionService {

    private DropboxCloudConfiguration dropboxCloudConfiguration;
    private Map<String, DropboxCloudSession> dropboxCloudSessions;

    @Autowired
    public DropboxCloudSessionService(DropboxCloudConfiguration dropboxCloudConfiguration) {
        this.dropboxCloudConfiguration = dropboxCloudConfiguration;
        this.dropboxCloudSessions = new HashMap<String, DropboxCloudSession>();
    }

    @Override
    public BasicSession loginUser(String login, String authorizationCode) throws Exception {
        DbxRequestConfig dbxRequestConfig = dropboxCloudConfiguration.getRequestConfig();
        DbxWebAuthNoRedirect webAuth = dropboxCloudConfiguration.getWebAuth();
        String authorizationUrl = getAuthorizationUrl();
        DbxAuthFinish authFinish = webAuth.finish(authorizationCode);
        DbxClient dbxClient = new DbxClient(dbxRequestConfig, authFinish.accessToken);
        DropboxCloudSession dropboxCloudSession = new DropboxCloudSession(UUID.randomUUID().toString(), authorizationCode, authFinish.accessToken, CloudSessionStatus.ACTIVE, dbxClient);
        //TODO: Persistence
        dropboxCloudSessions.put(dropboxCloudSession.getSessionId(), dropboxCloudSession);
        return dropboxCloudSession;
    }

    @Override
    public void logoutUser(String sessionId) {
        dropboxCloudSessions.remove(sessionId);
    }

    @Override
    public DropboxCloudSession getSession(String sessionId) {
        return dropboxCloudSessions.get(sessionId);
    }

    public String getAuthorizationUrl() throws GeneralSecurityException, IOException {
        return dropboxCloudConfiguration.getWebAuth().start();
    }

}

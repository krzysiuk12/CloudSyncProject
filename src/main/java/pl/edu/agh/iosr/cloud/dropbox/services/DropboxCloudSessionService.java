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
import pl.edu.agh.iosr.cloud.dropbox.configuration.DropboxConnector;
import pl.edu.agh.iosr.cloud.dropbox.session.DropboxCloudSession;
import pl.edu.agh.iosr.repository.ICloudSessionRepository;

@Service
public class DropboxCloudSessionService implements ICloudSessionService {

    private final DropboxConnector dropboxConnector;
    private final ICloudSessionRepository cloudSessionRepository;

    @Autowired
    public DropboxCloudSessionService(DropboxConnector dropboxConnector, ICloudSessionRepository cloudSessionRepository) {
        this.dropboxConnector = dropboxConnector;
        this.cloudSessionRepository = cloudSessionRepository;
    }

    @Override
    public String getAuthorizationUrl() {
        return dropboxConnector.getWebAuth().start();
    }

    @Override
    public BasicSession loginUser(String login, String authorizationCode) throws Exception {
        DbxRequestConfig dbxRequestConfig = dropboxConnector.getRequestConfig();
        DbxWebAuthNoRedirect webAuth = dropboxConnector.getWebAuth();
        getAuthorizationUrl();
        DbxAuthFinish authFinish = webAuth.finish(authorizationCode);
        DbxClient dbxClient = new DbxClient(dbxRequestConfig, authFinish.accessToken);
        DropboxCloudSession dropboxCloudSession = new DropboxCloudSession(authorizationCode, authFinish.accessToken, CloudSessionStatus.ACTIVE, dbxClient);
        cloudSessionRepository.addCloudSession(dropboxCloudSession);
        return dropboxCloudSession;
    }

    @Override
    public void logoutUser(String sessionId) {
        cloudSessionRepository.removeCloudSession(sessionId);
    }

    @Override
    public DropboxCloudSession getSession(String sessionId) {
        return (DropboxCloudSession) cloudSessionRepository.getCloudSessionById(sessionId);
    }

}

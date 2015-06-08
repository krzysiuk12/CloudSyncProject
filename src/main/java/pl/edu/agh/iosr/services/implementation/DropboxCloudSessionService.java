package pl.edu.agh.iosr.services.implementation;

import com.dropbox.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.services.interfaces.ICloudSessionService;
import pl.edu.agh.iosr.domain.cloud.session.CloudSessionStatus;
import pl.edu.agh.iosr.configuration.dropbox.DropboxConnector;
import pl.edu.agh.iosr.domain.cloud.session.DropboxCloudSession;
import pl.edu.agh.iosr.repository.interfaces.ICloudSessionRepository;

@Service
public class DropboxCloudSessionService implements ICloudSessionService<DropboxCloudSession> {

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
    public DropboxCloudSession loginUser(String login, String authorizationCode) {
        try {
            DbxRequestConfig dbxRequestConfig = dropboxConnector.getRequestConfig();
            DbxWebAuthNoRedirect webAuth = dropboxConnector.getWebAuth();
            getAuthorizationUrl();
            DbxAuthFinish authFinish = webAuth.finish(authorizationCode);
            DbxClient dbxClient = new DbxClient(dbxRequestConfig, authFinish.accessToken);
            DropboxCloudSession dropboxCloudSession = new DropboxCloudSession(authorizationCode, authFinish.accessToken, CloudSessionStatus.ACTIVE, dbxClient);
            cloudSessionRepository.addCloudSession(login, dropboxCloudSession);
            return dropboxCloudSession;
        } catch (DbxException e) {
            throw new IllegalArgumentException("Entered invalid authorization code: " + authorizationCode, e);
        }
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

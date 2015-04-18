package pl.edu.agh.iosr.cloud.googledrive.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudSessionService;
import pl.edu.agh.iosr.cloud.common.session.BasicSession;
import pl.edu.agh.iosr.cloud.common.session.CloudSessionStatus;
import pl.edu.agh.iosr.cloud.googledrive.configuration.GoogleDriveCloudConfiguration;
import pl.edu.agh.iosr.cloud.googledrive.session.GoogleDriveCloudSession;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mateusz Drożdż on 18.04.15.
 */
@Service
public class GoogleDriveCloudSessionService implements ICloudSessionService {

    private GoogleDriveCloudConfiguration googleDriveCloudConfiguration;
    private Map<String, GoogleDriveCloudSession> googleDriveCloudSessions;
    private String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

    /**
     * Global Drive API client.
     */
    private Drive drive;

    @Autowired
    public GoogleDriveCloudSessionService(GoogleDriveCloudConfiguration googleDriveCloudConfiguration) {
        this.googleDriveCloudConfiguration = googleDriveCloudConfiguration;
        this.googleDriveCloudSessions = new HashMap<String, GoogleDriveCloudSession>();
    }

    @Override
    public BasicSession loginUser(String login, String authorizationCode) throws Exception {
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                googleDriveCloudConfiguration.getHttpTransport(),
                googleDriveCloudConfiguration.getJsonFactory(),
                googleDriveCloudConfiguration.getAppKey(),
                googleDriveCloudConfiguration.getAppKeySecret(),
                Collections.singleton(DriveScopes.DRIVE))
                .setApprovalPrompt("auto")
                .build();
        String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();

        GoogleTokenResponse response = flow.newTokenRequest(authorizationCode).setRedirectUri(REDIRECT_URI).execute();
        GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);

        drive = new Drive.Builder(
                googleDriveCloudConfiguration.getHttpTransport(),
                googleDriveCloudConfiguration.getJsonFactory(),
                credential)
                .setApplicationName(googleDriveCloudConfiguration.getAppName())
                .build();

        GoogleDriveCloudSession googleDriveCloudSession = new GoogleDriveCloudSession(UUID.randomUUID().toString(), authorizationCode, response.getAccessToken(), CloudSessionStatus.ACTIVE, drive);
        googleDriveCloudSessions.put(googleDriveCloudSession.getSessionId(), googleDriveCloudSession);

        return googleDriveCloudSession;
    }

    @Override
    public void logoutUser(String sessionId) {
        googleDriveCloudSessions.remove(sessionId);
    }

    @Override
    public GoogleDriveCloudSession getSession(String sessionId) {
        return googleDriveCloudSessions.get(sessionId);
    }
}

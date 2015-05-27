package pl.edu.agh.iosr.cloud.googledrive.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudSessionService;
import pl.edu.agh.iosr.cloud.common.session.BasicSession;
import pl.edu.agh.iosr.cloud.common.session.CloudSessionStatus;
import pl.edu.agh.iosr.cloud.googledrive.configuration.GoogleDriveCloudConfiguration;
import pl.edu.agh.iosr.cloud.googledrive.session.GoogleDriveCloudSession;
import pl.edu.agh.iosr.repository.ICloudSessionRepository;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleDriveCloudSessionService implements ICloudSessionService {

    private GoogleDriveCloudConfiguration googleDriveCloudConfiguration;
    private ICloudSessionRepository cloudSessionRepository;

    /**
     * Global Drive API client.
     */
    private Drive drive;

    private static GoogleAuthorizationCodeFlow flow = null;

    private static final JacksonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

    @Autowired
    public GoogleDriveCloudSessionService(GoogleDriveCloudConfiguration googleDriveCloudConfiguration, ICloudSessionRepository cloudSessionRepository) {
        this.googleDriveCloudConfiguration = googleDriveCloudConfiguration;
        this.cloudSessionRepository = cloudSessionRepository;
    }

    @Override
    public BasicSession loginUser(String login, String authorizationCode) {
        try {
            Credential credential = exchangeCode(authorizationCode);
            drive = new Drive.Builder(
                    HTTP_TRANSPORT,
                    JSON_FACTORY,
                    credential)
                    .build();


            GoogleDriveCloudSession googleDriveCloudSession = new GoogleDriveCloudSession(authorizationCode, credential.getAccessToken(), CloudSessionStatus.ACTIVE, drive);
            cloudSessionRepository.addCloudSession(googleDriveCloudSession);

            return googleDriveCloudSession;
        } catch (IOException e) {
            throw new IllegalArgumentException("Entered invalid authorization code: " + authorizationCode, e);
        }
    }

    @Override
    public void logoutUser(String sessionId) {
        cloudSessionRepository.removeCloudSession(sessionId);
    }

    @Override
    public GoogleDriveCloudSession getSession(String sessionId) {
        return (GoogleDriveCloudSession) cloudSessionRepository.getCloudSessionById(sessionId);
    }

    @Override
    public String getAuthorizationUrl() throws GeneralSecurityException, IOException {
        GoogleAuthorizationCodeFlow flow = getFlow();
        return flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
    }

    /**
     * Exchange an authorization code for OAuth 2.0 credentials.
     *
     * @param authorizationCode Authorization code to exchange for OAuth 2.0
     *                          credentials.
     * @return OAuth 2.0 credentials.
     */
    private Credential exchangeCode(String authorizationCode) throws IOException {
        GoogleAuthorizationCodeFlow flow = getFlow();
        GoogleTokenResponse response = flow
                .newTokenRequest(authorizationCode)
                .setRedirectUri(REDIRECT_URI)
                .execute();
        return flow.createAndStoreCredential(response, null);
    }

    /**
     * Build an authorization flow and store it as a static class attribute.
     *
     * @return GoogleAuthorizationCodeFlow instance.
     * @throws IOException Unable to load client_secret.json.
     */
    private GoogleAuthorizationCodeFlow getFlow() throws IOException {
        if (flow == null) {
            GoogleClientSecrets.Details details = new GoogleClientSecrets.Details();
            details.setClientId(googleDriveCloudConfiguration.getAppKey());
            details.setClientSecret(googleDriveCloudConfiguration.getAppKeySecret());
            GoogleClientSecrets googleClientSecrets = new GoogleClientSecrets();
            googleClientSecrets.setInstalled(details);

            flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, googleClientSecrets, Collections.singleton(DriveScopes.DRIVE))
                    .setAccessType("online")
                    .setApprovalPrompt("force")
                    .build();
        }
        return flow;
    }

}

package pl.edu.agh.iosr.services.implementation;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.domain.cloud.CloudConfiguration;
import pl.edu.agh.iosr.services.interfaces.ICloudSessionService;
import pl.edu.agh.iosr.domain.cloud.session.CloudSessionStatus;
import pl.edu.agh.iosr.domain.cloud.session.OnedriveCloudSession;
import pl.edu.agh.iosr.repository.interfaces.ICloudSessionRepository;

import javax.ws.rs.core.MediaType;

@Service
public class OnedriveCloudSessionService implements ICloudSessionService<OnedriveCloudSession> {

    private static final String AUTHORIZATION_RESPONSE_CODE = "code";
    private static final String AUTHORIZATION_SCOPE = "wl.signin wl.offline_access onedrive.readwrite";
    private final static String REDIRECT_URI = "https://login.live.com/oauth20_desktop.srf";
    private static final String GRANT_TYPE = "authorization_code";

    private final CloudConfiguration cloudConfiguration;
    private final ICloudSessionRepository cloudSessionRepository;
    private final Client client;


    @Autowired
    public OnedriveCloudSessionService(@Value("${oneDrive.appName}") String appName, @Value("${oneDrive.appKey}") String appKey, @Value("${oneDrive.appKeySecret}") String appKeySecret, Client client, ICloudSessionRepository cloudSessionRepository) {
        this.cloudConfiguration = new CloudConfiguration(appName, appKey, appKeySecret);
        this.client = client;
        this.cloudSessionRepository = cloudSessionRepository;
    }

    @Override
    public OnedriveCloudSession loginUser(String login, String authorizationCode) {
        String accessToken = redeemCodeForToken(authorizationCode);

        OnedriveCloudSession theSession = new OnedriveCloudSession(authorizationCode, accessToken, CloudSessionStatus.ACTIVE);
        cloudSessionRepository.addCloudSession(login, theSession);

        return theSession;
    }

    private String redeemCodeForToken(String authorizationCode) {
        JSONObject responseJson = requestForAccessToken(authorizationCode);

        if (responseJson.has("access_token")) {
            return responseJson.get("access_token").toString();
        }

        throw new IllegalArgumentException("Entered invalid authorization code: " + authorizationCode);
    }

    private WebResource queryObtainTokenResource(String code) {
        return client.resource("https://login.live.com/oauth20_token.srf")
                    .queryParam("client_id", cloudConfiguration.getAppKey())
                    .queryParam("client_secret", cloudConfiguration.getAppKeySecret())
                    .queryParam("grant_type", GRANT_TYPE)
                    .queryParam("redirect_uri", REDIRECT_URI)
                    .queryParam("code", code);
    }

    private JSONObject requestForAccessToken(String code) {
        WebResource webResource = queryObtainTokenResource(code);
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        String rawResponse = response.getEntity(String.class);
        return new JSONObject(rawResponse);
    }

    @Override
    public void logoutUser(String sessionId) {
        cloudSessionRepository.removeCloudSession(sessionId);
    }

    @Override
    public String getAuthorizationUrl() throws Exception {
        return client.resource("https://login.live.com/oauth20_authorize.srf")
                .queryParam("client_id", cloudConfiguration.getAppKey())
                .queryParam("client_secret", cloudConfiguration.getAppKeySecret())
                .queryParam("scope", AUTHORIZATION_SCOPE)
                .queryParam("response_type", AUTHORIZATION_RESPONSE_CODE)
                .queryParam("redirect_uri", REDIRECT_URI).toString();
    }

    @Override
    public OnedriveCloudSession getSession(String sessionId) {
        return (OnedriveCloudSession) cloudSessionRepository.getCloudSessionById(sessionId);
    }

}

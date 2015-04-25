package pl.edu.agh.iosr.cloud.onedrive.services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.CloudConfiguration;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudSessionService;
import pl.edu.agh.iosr.cloud.common.session.BasicSession;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.cloud.common.session.CloudSessionStatus;
import pl.edu.agh.iosr.cloud.onedrive.sessionswtf.OnedriveCloudSession;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OnedriveCloudSessionService implements ICloudSessionService {

    private final static String REDIRECT_URI = "https://login.live.com/oauth20_desktop.srf";
    private static final String GRANT_TYPE = "authorization_code";

    private final CloudConfiguration cloudConfiguration;
    private final Map<String, CloudSession> sessions = new HashMap<>();
    private final Client client;

    @Autowired
    public OnedriveCloudSessionService(CloudConfiguration cloudConfiguration, Client client) {
        this.cloudConfiguration = cloudConfiguration;
        this.client = client;
    }

    @Override
    public BasicSession loginUser(String login, String authorizationCode) throws Exception {
        String accessToken = redeemCodeForToken(authorizationCode);

        OnedriveCloudSession theSession = new OnedriveCloudSession(UUID.randomUUID().toString(), authorizationCode, accessToken, CloudSessionStatus.ACTIVE);
        sessions.put(theSession.getSessionId(), theSession);

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
        sessions.remove(sessionId);
    }

    @Override
    public CloudSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

}

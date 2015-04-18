package pl.edu.agh.iosr.cloud.googledrive.configuration;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.stereotype.Component;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudConfiguration;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by Mateusz Drożdż on 18.04.15.
 */
@Component
public class GoogleDriveCloudConfiguration extends ICloudConfiguration {

    private static final String APP_NAME = "CloudSyncIosrProject";
    private static final String APP_KEY = "2828043487-aepcph91ibr6vurdij6qo2nckv2dmgt7.apps.googleusercontent.com";
    private static final String APP_KEY_SECRET = "s4CWlPWyq4n0-rMmRosqnUxt";

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport httpTransport;

    @Override
    public String getAppName() {
        return APP_NAME;
    }

    @Override
    public String getAppKey() {
        return APP_KEY;
    }

    @Override
    public String getAppKeySecret() {
        return APP_KEY_SECRET;
    }

    public JsonFactory getJsonFactory() {
        return JSON_FACTORY;
    }

    public HttpTransport getHttpTransport() throws GeneralSecurityException, IOException {
        if (httpTransport == null) {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        }
        return httpTransport;
    }

}

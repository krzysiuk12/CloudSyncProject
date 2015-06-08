package pl.edu.agh.iosr.domain.cloud.session;

import com.dropbox.core.DbxClient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.edu.agh.iosr.domain.cloud.CloudType;
import pl.edu.agh.iosr.domain.cloud.session.CloudSession;
import pl.edu.agh.iosr.domain.cloud.session.CloudSessionStatus;

/**
 * Created by Krzysztof Kicinger on 2015-04-11.
 */
public class DropboxCloudSession extends CloudSession {

    @JsonIgnore
    private DbxClient client;

    public DropboxCloudSession(String authorizationCode, String accessToken, CloudSessionStatus status, DbxClient client) {
        super(authorizationCode, accessToken, CloudType.DROPBOX, status);
        this.client = client;
    }

    public DbxClient getClient() {
        return client;
    }

    public void setClient(DbxClient client) {
        this.client = client;
    }
}

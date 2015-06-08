package pl.edu.agh.iosr.cloud.it;

import com.dropbox.core.DbxClient;
import pl.edu.agh.iosr.services.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.domain.cloud.session.CloudSession;
import pl.edu.agh.iosr.domain.cloud.session.CloudSessionStatus;
import pl.edu.agh.iosr.configuration.dropbox.DefaultDropboxConnector;
import pl.edu.agh.iosr.services.implementation.DropboxCloudManagementService;
import pl.edu.agh.iosr.domain.cloud.session.DropboxCloudSession;
import pl.edu.agh.iosr.cloud.util.CloudConfigurations;

import java.util.concurrent.Executors;

public class DropboxManagementAccessDeniedIT extends AbstractCloudManagementAccessDeniedIT {

    @Override
    protected ICloudManagementService createManagementService() {
        return new DropboxCloudManagementService(Executors.newSingleThreadExecutor());
    }

    @Override
    protected CloudSession createInvalidSession() {
        DefaultDropboxConnector connector = new DefaultDropboxConnector(CloudConfigurations.dropbox());
        String invalidToken = "DEFINITELY_INVALID_TOKEN";
        return new DropboxCloudSession("someCode", invalidToken, CloudSessionStatus.ACTIVE, new DbxClient(connector.getRequestConfig(), invalidToken));
    }
}

package pl.edu.agh.iosr.cloud.it;

import com.dropbox.core.DbxClient;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudSessionService;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.cloud.common.session.CloudSessionStatus;
import pl.edu.agh.iosr.cloud.dropbox.configuration.DefaultDropboxConnector;
import pl.edu.agh.iosr.cloud.dropbox.configuration.DropboxConnector;
import pl.edu.agh.iosr.cloud.dropbox.services.DropboxCloudManagementService;
import pl.edu.agh.iosr.cloud.dropbox.services.DropboxCloudSessionService;
import pl.edu.agh.iosr.cloud.dropbox.session.DropboxCloudSession;
import pl.edu.agh.iosr.cloud.util.CloudConfigurations;
import pl.edu.agh.iosr.repository.CloudSessionRepository;

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

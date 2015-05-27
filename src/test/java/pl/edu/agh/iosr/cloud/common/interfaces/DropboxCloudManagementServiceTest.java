package pl.edu.agh.iosr.cloud.common.interfaces;

import pl.edu.agh.iosr.cloud.dropbox.services.DropboxCloudManagementService;
import pl.edu.agh.iosr.cloud.dropbox.services.DropboxCloudSessionService;
import pl.edu.agh.iosr.cloud.dropbox.session.DropboxCloudSession;
import pl.edu.agh.iosr.repository.ICloudSessionRepository;

import java.util.concurrent.Executors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class DropboxCloudManagementServiceTest extends AbstractCloudManagementServiceTest {
    @Override
    protected ICloudManagementService createCloudManagementService(ICloudSessionRepository sessionRepository) {
        return new DropboxCloudManagementService(createSessionService(), Executors.newSingleThreadExecutor());
    }

    private DropboxCloudSessionService createSessionService() {
        DropboxCloudSessionService sessionService = mock(DropboxCloudSessionService.class);
        //TODO: wipe this out; this is session repository responsibility
        DropboxCloudSession session = mock(DropboxCloudSession.class);
        given(session.getAccessToken()).willReturn("THE_VALID_TOKEN");

        given(sessionService.getSession("VALID_SESSION_ID")).willReturn(session);

        return sessionService;
    }
}

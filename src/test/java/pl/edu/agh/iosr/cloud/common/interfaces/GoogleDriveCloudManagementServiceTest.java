package pl.edu.agh.iosr.cloud.common.interfaces;

import pl.edu.agh.iosr.cloud.googledrive.services.GoogleDriveCloudManagementService;
import pl.edu.agh.iosr.cloud.googledrive.services.GoogleDriveCloudSessionService;
import pl.edu.agh.iosr.cloud.googledrive.session.GoogleDriveCloudSession;
import pl.edu.agh.iosr.repository.ICloudSessionRepository;

import java.util.concurrent.Executors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class GoogleDriveCloudManagementServiceTest extends AbstractCloudManagementServiceTest {
    @Override
    protected ICloudManagementService createCloudManagementService(ICloudSessionRepository sessionRepository) {
        return new GoogleDriveCloudManagementService(createSessionService(), Executors.newSingleThreadExecutor());
    }

    private GoogleDriveCloudSessionService createSessionService() {
        GoogleDriveCloudSessionService sessionService = mock(GoogleDriveCloudSessionService.class);
        //TODO: wipe this out; this is session repository responsibility
        GoogleDriveCloudSession session = mock(GoogleDriveCloudSession.class);
        given(session.getAccessToken()).willReturn("ya29.gAGfdcE7FwSjTrKTBNIp1TfvBLPS95_9JGeghwCs3gyiOFzFPAMZY3mjxUbCIKywLgxmL8FYNZQY0g");

        given(sessionService.getSession("VALID_SESSION_ID")).willReturn(session);

        return sessionService;
    }
}

package pl.edu.agh.iosr.cloud.onedrive.services;

import com.sun.jersey.api.client.Client;
import pl.edu.agh.iosr.cloud.common.interfaces.AbstractCloudManagementServiceTest;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.repository.ICloudSessionRepository;

import java.util.concurrent.Executors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class OnedriveCloudManagementServiceTest extends AbstractCloudManagementServiceTest {

    @Override
    protected OnedriveCloudManagementService createCloudManagementService(ICloudSessionRepository sessionRepository) {
        return new OnedriveCloudManagementService(createSessionService(), Executors.newSingleThreadExecutor(), new Client());
    }

    private OnedriveCloudSessionService createSessionService() {
        OnedriveCloudSessionService sessionService = mock(OnedriveCloudSessionService.class);
        //TODO: wipe this out; this is session repository responsibility
        CloudSession session = mock(CloudSession.class);
        given(session.getAccessToken()).willReturn("THE_VALID_TOKEN");

        given(sessionService.getSession("VALID_SESSION_ID")).willReturn(session);

        return sessionService;
    }

}
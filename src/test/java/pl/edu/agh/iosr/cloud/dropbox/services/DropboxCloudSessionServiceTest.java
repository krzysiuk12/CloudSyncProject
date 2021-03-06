package pl.edu.agh.iosr.cloud.dropbox.services;

import pl.edu.agh.iosr.cloud.common.interfaces.AbstractCloudSessionServiceTest;
import pl.edu.agh.iosr.services.implementation.DropboxCloudSessionService;
import pl.edu.agh.iosr.services.interfaces.ICloudSessionService;
import pl.edu.agh.iosr.cloud.util.BetamaxProxyAwareDropboxConnector;
import pl.edu.agh.iosr.repository.interfaces.ICloudSessionRepository;

import static org.mockito.Mockito.mock;

public class DropboxCloudSessionServiceTest extends AbstractCloudSessionServiceTest {

    @Override
    protected ICloudSessionService createCloudSessionService() {
        BetamaxProxyAwareDropboxConnector dropboxConnector = BetamaxProxyAwareDropboxConnector.fromRecorder(recorder);
        return new DropboxCloudSessionService(dropboxConnector, mock(ICloudSessionRepository.class));
    }
}

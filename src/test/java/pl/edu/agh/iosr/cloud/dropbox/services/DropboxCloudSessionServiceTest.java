package pl.edu.agh.iosr.cloud.dropbox.services;

import pl.edu.agh.iosr.cloud.common.CloudConfiguration;
import pl.edu.agh.iosr.cloud.common.interfaces.AbstractCloudSessionServiceTest;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudSessionService;
import pl.edu.agh.iosr.cloud.util.BetamaxProxyAwareDropboxConnector;
import pl.edu.agh.iosr.repository.ICloudSessionRepository;

import java.net.InetSocketAddress;

import static org.mockito.Mockito.mock;

public class DropboxCloudSessionServiceTest extends AbstractCloudSessionServiceTest {

    @Override
    protected ICloudSessionService createCloudSessionService() {
        CloudConfiguration configuration = new CloudConfiguration("testApp", "someKey", "someKeySecret");
        InetSocketAddress proxyAddress = new InetSocketAddress(recorder.getProxyHost(), recorder.getHttpsProxyPort());
        BetamaxProxyAwareDropboxConnector dropboxConnector = new BetamaxProxyAwareDropboxConnector(configuration, proxyAddress);

        return new DropboxCloudSessionService(dropboxConnector, mock(ICloudSessionRepository.class));
    }
}

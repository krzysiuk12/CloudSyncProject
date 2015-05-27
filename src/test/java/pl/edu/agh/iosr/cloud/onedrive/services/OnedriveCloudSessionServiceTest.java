package pl.edu.agh.iosr.cloud.onedrive.services;

import com.sun.jersey.api.client.Client;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudSessionService;
import pl.edu.agh.iosr.cloud.common.interfaces.AbstractCloudSessionServiceTest;
import pl.edu.agh.iosr.repository.CloudSessionRepository;

public class OnedriveCloudSessionServiceTest extends AbstractCloudSessionServiceTest {

    @Override
    protected ICloudSessionService createCloudSessionService() {
        return new OnedriveCloudSessionService("testApp", "THE_CLIENT_ID", "THE_CLIENT_SECRET", new Client(), new CloudSessionRepository("-", 4, 5));
    }
}
package pl.edu.agh.iosr.cloud.googledrive.services;

import pl.edu.agh.iosr.services.implementation.GoogleDriveCloudSessionService;
import pl.edu.agh.iosr.services.interfaces.ICloudSessionService;
import pl.edu.agh.iosr.cloud.common.interfaces.AbstractCloudSessionServiceTest;
import pl.edu.agh.iosr.configuration.googledrive.GoogleDriveCloudConfiguration;
import pl.edu.agh.iosr.repository.interfaces.ICloudSessionRepository;

import static org.mockito.Mockito.mock;

public class GoogleDriveCloudSessionServiceTest extends AbstractCloudSessionServiceTest {

    @Override
    protected ICloudSessionService createCloudSessionService() {
        GoogleDriveCloudConfiguration configuration = new GoogleDriveCloudConfiguration("testApp", "someAppKey", "someKeySecret");
        return new GoogleDriveCloudSessionService(configuration, mock(ICloudSessionRepository.class));
    }
}
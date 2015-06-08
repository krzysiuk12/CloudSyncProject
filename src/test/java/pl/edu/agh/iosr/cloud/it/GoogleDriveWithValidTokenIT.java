package pl.edu.agh.iosr.cloud.it;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import pl.edu.agh.iosr.domain.cloud.session.CloudSessionStatus;
import pl.edu.agh.iosr.services.implementation.GoogleDriveCloudManagementService;
import pl.edu.agh.iosr.domain.cloud.session.GoogleDriveCloudSession;

import java.util.concurrent.Executors;

public class GoogleDriveWithValidTokenIT {

    private final String accessToken = System.getProperty("test.google.token");

    private GoogleDriveCloudManagementService managementService;
    private GoogleDriveCloudSession session;

    @Before
    public void setUp() throws Exception {
        managementService = new GoogleDriveCloudManagementService(Executors.newSingleThreadExecutor());
        //FIXME below
        session = new GoogleDriveCloudSession("someCode", accessToken, CloudSessionStatus.ACTIVE, null);
    }

    @Ignore
    @Test
    public void testEndToEnd() throws Exception {
        //FIXME has no delete. wtf with paths vs ids?
    }

}
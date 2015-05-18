package pl.edu.agh.iosr.cloud.it;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.session.BasicSession;
import pl.edu.agh.iosr.cloud.googledrive.services.GoogleDriveCloudManagementService;
import pl.edu.agh.iosr.cloud.googledrive.services.GoogleDriveCloudSessionService;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:**/applicationConfig.xml" })
public class GoogleDriveIT {

    private final String authorizationCode = System.getProperty("test.google.code");

    @Autowired
    private GoogleDriveCloudSessionService sessionService;
    @Autowired
    private GoogleDriveCloudManagementService managementService;
    private BasicSession session;

    @Before
    public void setUp() throws Exception {
        session = sessionService.loginUser("santiago", authorizationCode);
    }

    @Ignore
    @Test
    public void testEndToEnd() throws Exception {
        Boolean deleted = managementService.deleteFile(session.getSessionId(), new CloudPath("/__E2E_TEST"));
        List<FileMetadata> listed = managementService.listAllDirectoryFiles(session.getSessionId(), new CloudPath("root"));
        assertThat(true).isTrue();
    }

}
package pl.edu.agh.iosr.cloud.it;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.edu.agh.iosr.cloud.common.CloudType;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.session.BasicSession;
import pl.edu.agh.iosr.cloud.dropbox.services.DropboxCloudManagementService;
import pl.edu.agh.iosr.cloud.dropbox.services.DropboxCloudSessionService;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:**/applicationConfig.xml" })
public class DropboxIT {

    private final String authorizationCode = System.getProperty("test.dropbox.code");

    @Autowired
    private DropboxCloudSessionService sessionService;
    @Autowired
    private DropboxCloudManagementService managementService;
    private BasicSession session;

    @Before
    public void setUp() throws Exception {
        session = sessionService.loginUser("santiago", authorizationCode);
    }

    @Ignore
    @Test
    public void testEndToEnd() throws Exception {
        managementService.deleteFile(session.getSessionId(), new CloudPath("/__E2E_TEST_FILE", CloudType.DROPBOX));

        List<FileMetadata> listed = managementService.listAllDirectoryFiles(session.getSessionId(), new CloudPath("/", CloudType.DROPBOX));
        for (FileMetadata fileMetadata : listed) {
            assertThat(fileMetadata.getFileName()).isNotEqualTo("__E2E_TEST_FILE");
        }

        managementService.uploadFile(session.getSessionId(), new CloudPath("/", CloudType.DROPBOX), "__E2E_TEST_FILE", createInputStream());

        List<FileMetadata> listedAfterUpload = managementService.listAllDirectoryFiles(session.getSessionId(), new CloudPath("/", CloudType.DROPBOX));
        assertThat(FluentIterable.from(listedAfterUpload).transform(metadataIntoFilename())).contains("__E2E_TEST_FILE");

        Boolean deleted = managementService.deleteFile(session.getSessionId(), new CloudPath("/__E2E_TEST_FILE", CloudType.DROPBOX));
        assertThat(deleted).isTrue();

        List<FileMetadata> listedAfterDelete = managementService.listAllDirectoryFiles(session.getSessionId(), new CloudPath("/", CloudType.DROPBOX));
        for (FileMetadata fileMetadata : listedAfterDelete) {
            assertThat(fileMetadata.getFileName()).isNotEqualTo("__E2E_TEST_FILE");
        }

        assertThat(true).isTrue();
    }

    private Function<FileMetadata, String> metadataIntoFilename() {
        return new Function<FileMetadata, String>() {
            @Nullable
            @Override
            public String apply(FileMetadata fileMetadata) {
                return fileMetadata.getFileName();
            }
        };
    }

    private InputStream createInputStream() throws IOException {
        PipedInputStream givenContentStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(givenContentStream);
        outputStream.write("hello world".getBytes(StandardCharsets.US_ASCII));
        outputStream.close();

        return givenContentStream;
    }

}
package pl.edu.agh.iosr.cloud.it;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxRequestConfig;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.iosr.cloud.common.CloudConfiguration;
import pl.edu.agh.iosr.cloud.common.CloudType;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.session.CloudSessionStatus;
import pl.edu.agh.iosr.cloud.dropbox.configuration.DefaultDropboxConnector;
import pl.edu.agh.iosr.cloud.dropbox.services.DropboxCloudManagementService;
import pl.edu.agh.iosr.cloud.dropbox.session.DropboxCloudSession;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class DropboxIT {

    private final String accessToken = System.getProperty("test.dropbox.token");

    private DropboxCloudManagementService managementService;
    private DropboxCloudSession session;

    @Before
    public void setUp() throws Exception {
        managementService = new DropboxCloudManagementService(Executors.newSingleThreadExecutor());
        session = new DropboxCloudSession("someCode", accessToken, CloudSessionStatus.ACTIVE, createDbxClient(accessToken));
    }

    private DbxClient createDbxClient(String token) {
        return new DbxClient(createRequestConfig(), token);
    }

    private DbxRequestConfig createRequestConfig() {
        String appName = "CloudSyncIosrProject";
        String appKey = "nlsu5a4n7jcr3il";
        String appKeySecret = "c1tufi0cm9vjoh0";
        DefaultDropboxConnector connector = new DefaultDropboxConnector(new CloudConfiguration(appName, appKey, appKeySecret));
        return connector.getRequestConfig();
    }

    @Test
    public void testEndToEnd() throws Exception {
        managementService.deleteFile(session, new CloudPath("/__E2E_TEST_FILE", CloudType.DROPBOX)).get();

        List<FileMetadata> listed = managementService.listAllDirectoryFiles(session, new CloudPath("/", CloudType.DROPBOX)).get();
        assertThat(listed).extracting(FileMetadata::getFileName).doesNotContain("__E2E_TEST_FILE");

        managementService.uploadFile(session, new CloudPath("/", CloudType.DROPBOX), "__E2E_TEST_FILE", createInputStreamWithText("hello world")).get();

        List<FileMetadata> listedAfterUpload = managementService.listAllDirectoryFiles(session, new CloudPath("/", CloudType.DROPBOX)).get();
        assertThat(listedAfterUpload).extracting(FileMetadata::getFileName).contains("__E2E_TEST_FILE");

        PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream grabbedDataStream = new PipedInputStream(outputStream);
        managementService.downloadFile(session, new CloudPath("/__E2E_TEST_FILE", CloudType.DROPBOX), outputStream).get();
        assertThat(IOUtils.toString(grabbedDataStream)).isEqualTo("hello world");

        Boolean deleted = managementService.deleteFile(session, new CloudPath("/__E2E_TEST_FILE", CloudType.DROPBOX)).get();
        assertThat(deleted).isTrue();

        List<FileMetadata> listedAfterDelete = managementService.listAllDirectoryFiles(session, new CloudPath("/", CloudType.DROPBOX)).get();
        assertThat(listedAfterDelete).extracting(FileMetadata::getFileName).doesNotContain("__E2E_TEST_FILE");
    }

    private InputStream createInputStreamWithText(String text) throws IOException {
        PipedInputStream givenContentStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(givenContentStream);
        outputStream.write(text.getBytes(StandardCharsets.US_ASCII));
        outputStream.close();

        return givenContentStream;
    }
}
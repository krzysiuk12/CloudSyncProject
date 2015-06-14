package pl.edu.agh.iosr.cloud.it;

import com.sun.jersey.api.client.Client;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.iosr.domain.cloud.CloudType;
import pl.edu.agh.iosr.domain.cloud.files.CloudPath;
import pl.edu.agh.iosr.domain.cloud.files.FileMetadata;
import pl.edu.agh.iosr.domain.cloud.session.CloudSessionStatus;
import pl.edu.agh.iosr.domain.cloud.session.OnedriveCloudSession;
import pl.edu.agh.iosr.services.implementation.OnedriveCloudManagementService;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class OnedriveWithValidTokenIT {

    private final String accessToken = System.getProperty("test.onedrive.token");

    private OnedriveCloudManagementService managementService;
    private OnedriveCloudSession session;

    @Before
    public void setUp() throws Exception {
        managementService = new OnedriveCloudManagementService(Executors.newSingleThreadExecutor(), new Client());
        session = new OnedriveCloudSession("someCode", accessToken, CloudSessionStatus.ACTIVE);
    }

    //TODO: extract to some shared place cause this is copy-paste

    @Test
    public void testEndToEnd() throws Exception {
        managementService.deleteFile(session, new CloudPath("/__E2E_TEST_FILE", CloudType.ONE_DRIVE)).get();

        List<FileMetadata> listed = managementService.listAllDirectoryFiles(session, new CloudPath("/", CloudType.ONE_DRIVE)).get();
        assertThat(listed).extracting(FileMetadata::getFileName).doesNotContain("__E2E_TEST_FILE");

        managementService.uploadFile(session, new CloudPath("/", CloudType.ONE_DRIVE), "__E2E_TEST_FILE", createInputStreamWithText("hello world")).get();

        List<FileMetadata> listedAfterUpload = managementService.listAllDirectoryFiles(session, new CloudPath("/", CloudType.ONE_DRIVE)).get();
        assertThat(listedAfterUpload).extracting(FileMetadata::getFileName).contains("__E2E_TEST_FILE");

        PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream grabbedDataStream = new PipedInputStream(outputStream);
        managementService.downloadFile(session, new CloudPath("/__E2E_TEST_FILE", CloudType.ONE_DRIVE), outputStream).get();
        assertThat(IOUtils.toString(grabbedDataStream)).isEqualTo("hello world");

        Boolean deleted = managementService.deleteFile(session, new CloudPath("/__E2E_TEST_FILE", CloudType.ONE_DRIVE)).get();
        assertThat(deleted).isTrue();

        List<FileMetadata> listedAfterDelete = managementService.listAllDirectoryFiles(session, new CloudPath("/", CloudType.ONE_DRIVE)).get();
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
package pl.edu.agh.iosr.cloud.onedrive.services;

import co.freeside.betamax.Betamax;
import co.freeside.betamax.Recorder;
import com.sun.jersey.api.client.Client;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import pl.edu.agh.iosr.domain.cloud.CloudType;
import pl.edu.agh.iosr.domain.cloud.files.CloudPath;
import pl.edu.agh.iosr.domain.cloud.files.FileMetadata;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressAwareFuture;
import pl.edu.agh.iosr.domain.cloud.session.OnedriveCloudSession;
import pl.edu.agh.iosr.services.implementation.OnedriveCloudManagementService;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class OnedriveCloudManagementServiceTest {

    @Rule
    public Recorder recorder = new Recorder();

    private OnedriveCloudManagementService underTest;
    private OnedriveCloudSession session;

    @Before
    public void setUp() throws Exception {
        underTest = new OnedriveCloudManagementService(Executors.newSingleThreadExecutor(), new Client());
        session = createSession();
    }

    private OnedriveCloudSession createSession() {
        OnedriveCloudSession session = mock(OnedriveCloudSession.class);
        given(session.getAccessToken()).willReturn("THE_VALID_TOKEN");

        return session;
    }

    @Betamax(tape = "onedrive_listDirs")
    @Test
    public void testListRootDir() throws Exception {
        // given
        CloudPath rootPath = new CloudPath("/", CloudType.ONE_DRIVE);

        // when
        ProgressAwareFuture<List<FileMetadata>> future = underTest.listAllDirectoryFiles(session, rootPath);
        List<FileMetadata> files = future.get();

        // then
        assertThat(files).hasSize(3);
        assertThat(files.get(0).getFileName()).isEqualTo("Dokumenty");
        assertThat(files.get(1).getFileName()).isEqualTo("Obrazy");
        assertThat(files.get(2).getFileName()).isEqualTo("Bez nazwy.txt");
    }

    @Betamax(tape = "onedrive_download")
    @Test
    public void testDownload() throws Exception {
        // given
        CloudPath path = new CloudPath("/some_note.txt", CloudType.ONE_DRIVE);
        PipedInputStream grabbedContentStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(grabbedContentStream);

        // when
        ProgressAwareFuture<Boolean> future = underTest.downloadFile(session, path, outputStream);
        Boolean success = future.get();
        String downloadedContent = IOUtils.toString(grabbedContentStream);

        // then
        assertThat(success).isTrue();
        assertThat(downloadedContent).contains("litwo ojczyzno moja");
        assertThat(downloadedContent).contains("blablabla");
    }

    @Betamax(tape = "onedrive_upload")
    @Test
    public void testUpload() throws Exception {
        // given
        CloudPath path = new CloudPath("/uploaded.txt", CloudType.ONE_DRIVE);
        PipedInputStream givenContentStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(givenContentStream);
        outputStream.write("hello world".getBytes(StandardCharsets.US_ASCII));
        outputStream.close();

        // when
        ProgressAwareFuture<FileMetadata> future = underTest.uploadFile(session, path, null, givenContentStream);
        FileMetadata file = future.get();

        // then
        //TODO: more fancy check - some unified hash stored in file metadata
        assertThat(file.getSize()).isEqualTo(11);
    }
}
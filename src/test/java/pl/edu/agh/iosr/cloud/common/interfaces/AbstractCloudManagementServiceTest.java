package pl.edu.agh.iosr.cloud.common.interfaces;

import co.freeside.betamax.Betamax;
import co.freeside.betamax.Recorder;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import pl.edu.agh.iosr.cloud.common.CloudType;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;
import pl.edu.agh.iosr.cloud.onedrive.services.OnedriveCloudManagementService;
import pl.edu.agh.iosr.repository.ICloudSessionRepository;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public abstract class AbstractCloudManagementServiceTest {

    @Rule
    public Recorder recorder = new Recorder();

    private OnedriveCloudManagementService underTest;
    private final String sessionId = "VALID_SESSION_ID";

    @Before
    public void setUp() throws Exception {
        underTest = createCloudManagementService(createSessionRepository());
    }

    protected abstract OnedriveCloudManagementService createCloudManagementService(ICloudSessionRepository sessionRepository);

    private ICloudSessionRepository createSessionRepository() {
        ICloudSessionRepository repository = mock(ICloudSessionRepository.class);
        //TODO: in mgmt service use our user session and based on that obtain the cloud session
        CloudSession session = mock(CloudSession.class);
        given(session.getAccessToken()).willReturn("THE_VALID_TOKEN");

        given(repository.getCloudSessionById(sessionId)).willReturn(session);

        return repository;
    }

    @Betamax(tape = "onedrive_listDirs")
    @Test
    public void testListRootDir() throws Exception {
        // given
        CloudPath rootPath = new CloudPath("/", CloudType.ONE_DRIVE);

        // when
        List<FileMetadata> paths = underTest.listAllDirectoryFiles(sessionId, rootPath);

        // then
        assertThat(paths).hasSize(3);
        assertThat(paths.get(0).getFileName()).isEqualTo("Dokumenty");
        assertThat(paths.get(1).getFileName()).isEqualTo("Obrazy");
        assertThat(paths.get(2).getFileName()).isEqualTo("Bez nazwy.txt");
    }

    @Betamax(tape = "onedrive_download")
    @Test
    public void testDownload() throws Exception {
        // given
        CloudPath path = new CloudPath("/some_note.txt", CloudType.ONE_DRIVE);
        PipedInputStream grabbedContentStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(grabbedContentStream);
        CloudTask<Boolean> task = underTest.downloadFile(sessionId, path, outputStream);

        // when
        //TODO: inject the executor service into mgmtservice so that it executes that
        Executor executorService = Executors.newSingleThreadExecutor();
        executorService.execute(task);
        task.get();
        //TODO: close stream in download task
        outputStream.close();
        String downloadedContent = IOUtils.toString(grabbedContentStream);

        // then
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
        CloudTask<FileMetadata> task = underTest.uploadFile(sessionId, path, null, givenContentStream);

        // when
        Executor executorService = Executors.newSingleThreadExecutor();
        executorService.execute(task);
        FileMetadata file = task.get();

        // then
        //TODO: more fancy check - some unified hash stored in file metadata
        assertThat(file.getSize()).isEqualTo(11);
    }
}
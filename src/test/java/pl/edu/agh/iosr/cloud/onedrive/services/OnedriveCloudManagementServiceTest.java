package pl.edu.agh.iosr.cloud.onedrive.services;

import co.freeside.betamax.Betamax;
import co.freeside.betamax.Recorder;
import com.sun.jersey.api.client.Client;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class OnedriveCloudManagementServiceTest {

    @Rule
    public Recorder recorder = new Recorder();

    private OnedriveCloudManagementService underTest;
    private final String sessionId = "VALID_SESSION_ID";

    @Before
    public void setUp() throws Exception {
        underTest = new OnedriveCloudManagementService(createSessionService(), createExecutor(), new Client());
    }

    private Executor createExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    private OnedriveCloudSessionService createSessionService() {
        OnedriveCloudSessionService sessionService = mock(OnedriveCloudSessionService.class);
        //TODO: redesign as mock returns mock
        CloudSession session = mock(CloudSession.class);
        given(session.getAccessToken()).willReturn("THE_VALID_TOKEN");

        given(sessionService.getSession(sessionId)).willReturn(session);

        return sessionService;
    }

    @Betamax(tape="onedrive_listDirs")
    @Test
    public void testListRootDir() throws Exception {
        // given
        CloudPath rootPath = new CloudPath("/");

        // when
        List<FileMetadata> paths = underTest.listAllDirectoryFiles(sessionId, rootPath);

        // then
        assertThat(paths).hasSize(3);
        assertThat(paths.get(0).getFileName()).isEqualTo("Dokumenty");
        assertThat(paths.get(1).getFileName()).isEqualTo("Obrazy");
        assertThat(paths.get(2).getFileName()).isEqualTo("Bez nazwy.txt");
    }

    @Betamax(tape="onedrive_download")
    @Test
    public void testDownload() throws Exception {
        // given
        CloudPath path = new CloudPath("/some_note.txt");
        PipedInputStream grabbedContentStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(grabbedContentStream);

        // when
        underTest.downloadFile(sessionId, path, outputStream);
        outputStream.close();
        String downloadedContent = IOUtils.toString(grabbedContentStream);

        // then
        assertThat(downloadedContent).contains("litwo ojczyzno moja");
        assertThat(downloadedContent).contains("blablabla");
    }
}
package pl.edu.agh.iosr.cloud.onedrive.services;

import co.freeside.betamax.Betamax;
import co.freeside.betamax.Recorder;
import com.sun.jersey.api.client.Client;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import pl.edu.agh.iosr.cloud.common.files.CoolCloudPath;
import pl.edu.agh.iosr.cloud.common.files.CoolFileMetadata;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;

import java.io.IOException;
import java.io.OutputStream;
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
        given(session.getAccessToken()).willReturn("EwBoAq1DBAAUGCCXc8wU/zFu9QnLdZXy+YnElFkAAa1+uVVKBrMK1IISFMN3IcO5HJeQ6uanTY5ugh+ROiZjPTaVBdpRp1YZ5yuWpo+mLHTEO0zBr5cP1lDdnRFQp9af8OILalvpIp8KoS5z6fKnJH5H5U3GLDgu3JxSkGWL+23VwT5igCfzgKpfEGql7GHVvDXtDhmTHATLpGy/bGDLOwVuMVefXE8kqY/ihq1hpjbm6deL7cbDo5g4T0NZ1UfaHW5JwF3pWTnnpuGZ8t+Ea2Ygh4w2cyWVw1/pWB92wfpBWdqziHCBZSIv6g/BPU8derLL5sCHgIesTXsnawO6EhBTg0C4EcYTRP0T9iE6cVEZrsPuresa/eVtdBeXSncDZgAACNmrqMHQ9wC/OAFBAmpFPqs+2HUrnLGHi1TTLoir7Y3/Ir1ppGJQpQ1T5QgS+QlhpN3qT/HZAlyaTY+fL3uUmypEKIeWdcbQsTdtkEu+h0UgEklp4z1NKn5/evTUNHJtEjC8LiLhIoSGd9fFy1AYGX9FqUTXagGg5h1aNhBnMFtBfOj4DLiTgaATD/LfStgvmASfViKlGOzSX21ItrlnudDMF4vkTTYAO8PB90aAnWtv9+TxsXH1MleYR8+db8N65Ir2WMouPx/hV8gUXXp2j49MkFf3f6FYA+YTC4wEzoZ+TbQBXrfwrDVr1nRZ1oeHSKK2cNJmLrtqyEf5cnW2Z2mGWOoaau6Rdol3cAd7Wm3FbEaN+Br9uSAbesdQkx379LdATz4HKZJothFHW7ZryiWGaPNPSosWfnAy+eYjzM0FNSxYAQ==");

        given(sessionService.getSession(sessionId)).willReturn(session);

        return sessionService;
    }

    @Betamax(tape="onedrive_listDirs")
    @Test
    public void testListRootDir() throws Exception {
        // given
        CoolCloudPath rootPath = new CoolCloudPath("/");

        // when
        List<CoolFileMetadata> paths = underTest.listAllDirectoryFiles(sessionId, rootPath);

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
        CoolCloudPath path = new CoolCloudPath("/some_note.txt");
        PipedInputStream pipedInputStream = new PipedInputStream();

        // when
        underTest.downloadFile(sessionId, path, new PipedOutputStream(pipedInputStream));
        pipedInputStream.close();
        String downloadedContent = IOUtils.toString(pipedInputStream);

        // then
        assertThat(downloadedContent).contains("litwo ojczyzno moja");
        assertThat(downloadedContent).contains("blablabla");
    }
}
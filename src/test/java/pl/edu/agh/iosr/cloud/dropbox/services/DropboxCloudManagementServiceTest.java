package pl.edu.agh.iosr.cloud.dropbox.services;

import co.freeside.betamax.Betamax;
import co.freeside.betamax.Recorder;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxRequestConfig;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import pl.edu.agh.iosr.cloud.common.CloudType;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.session.CloudSessionStatus;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressAwareFuture;
import pl.edu.agh.iosr.cloud.dropbox.session.DropboxCloudSession;
import pl.edu.agh.iosr.cloud.util.BetamaxProxyAwareDropboxConnector;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class DropboxCloudManagementServiceTest {

    @Rule
    public Recorder recorder = new Recorder();

    private DropboxCloudManagementService underTest;
    private DropboxCloudSession session;

    @Before
    public void setUp() throws Exception {
        underTest = new DropboxCloudManagementService(Executors.newSingleThreadExecutor());
        session = createSession();
    }

    private DropboxCloudSession createSession() {
        String token = "s9KNb-J7l-AAAAAAAAAAax44r3KDwOJpV-_m5DIXiGiRnOmyIRtYcawJklm9dJKN";
        return new DropboxCloudSession("someCode", token, CloudSessionStatus.ACTIVE, createDbxClient(token));
    }

    private DbxClient createDbxClient(String token) {
        return new DbxClient(createRequestConfig(), token);
    }

    private DbxRequestConfig createRequestConfig() {
        BetamaxProxyAwareDropboxConnector connector = BetamaxProxyAwareDropboxConnector.fromRecorder(recorder);
        return connector.getRequestConfig();
    }

    @Betamax(tape = "dropbox_listDirs")
    @Test
    public void testListRootDir() throws Exception {
        // given
        CloudPath rootPath = new CloudPath("/", CloudType.DROPBOX);

        // when
        ProgressAwareFuture<List<FileMetadata>> future = underTest.listAllDirectoryFiles(session, rootPath);
        List<FileMetadata> files = future.get();

        // then
        ImmutableSet<String> filenames = FluentIterable.from(files).transform(getFilenamesMapper()).toImmutableSet();
        assertThat(filenames).containsOnly("src", "test1", "test2", "Get Started with Dropbox.pdf", "some_note.txt");
    }

    private Function<FileMetadata, String> getFilenamesMapper() {
        return new Function<FileMetadata, String>() {
            @Nullable
            @Override
            public String apply(@Nullable FileMetadata fileMetadata) {
                if (fileMetadata == null) {
                    return null;
                }

                return fileMetadata.getFileName();
            }
        };
    }
}

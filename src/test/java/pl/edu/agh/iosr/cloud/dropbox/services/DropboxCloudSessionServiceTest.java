package pl.edu.agh.iosr.cloud.dropbox.services;

import co.freeside.betamax.Betamax;
import co.freeside.betamax.Recorder;
import com.dropbox.core.DbxException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import pl.edu.agh.iosr.cloud.common.CloudConfiguration;
import pl.edu.agh.iosr.cloud.common.session.BasicSession;
import pl.edu.agh.iosr.cloud.util.BetamaxProxyAwareDropboxConnector;
import pl.edu.agh.iosr.repository.ICloudSessionRepository;

import java.net.InetSocketAddress;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class DropboxCloudSessionServiceTest {

    @Rule
    public Recorder recorder = new Recorder();

    private DropboxCloudSessionService underTest;

    @Before
    public void setUp() throws Exception {
        CloudConfiguration configuration = new CloudConfiguration("testApp", "someKey", "someKeySecret");
        InetSocketAddress proxyAddress = new InetSocketAddress(recorder.getProxyHost(), recorder.getHttpsProxyPort());
        BetamaxProxyAwareDropboxConnector dropboxConnector = new BetamaxProxyAwareDropboxConnector(configuration, proxyAddress);
        underTest = new DropboxCloudSessionService(dropboxConnector, mock(ICloudSessionRepository.class));
    }

    @Betamax(tape="dropbox_validLogin")
    @Test
    public void testForValidCode() throws Exception {
        // given
        String authorizationCode = "THE_VALID_CODE";

        // when
        BasicSession session = underTest.loginUser("some login", authorizationCode);

        // then
        //TODO: introduce assertJ assertion library
        //TODO: assertion over the access token so session need to store that
        assertThat(session).isNotNull();
    }

    //TODO: match by body too
    @Betamax(tape="dropbox_invalidLogin")
    @Test(expected = DbxException.BadRequest.class)
    public void testForCodeThatIsInvalid() throws Exception {
        // given
        String authorizationCode = "bubu";

        // when
        underTest.loginUser("some login", authorizationCode);

        // then
        // expect exception
    }
}
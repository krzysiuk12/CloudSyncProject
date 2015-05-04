package pl.edu.agh.iosr.cloud.dropbox.services;

import co.freeside.betamax.Betamax;
import co.freeside.betamax.Recorder;
import com.dropbox.core.DbxException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import pl.edu.agh.iosr.cloud.common.session.BasicSession;
import pl.edu.agh.iosr.cloud.dropbox.configuration.DropboxCloudConfiguration;

import static org.fest.assertions.Assertions.assertThat;

public class DropboxCloudSessionServiceTest {

    @Rule
    public Recorder recorder = new Recorder();

    private DropboxCloudSessionService underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new DropboxCloudSessionService(new DropboxCloudConfiguration());
    }

    @Ignore
    @Betamax(tape="onedrive_validLogin")
    @Test
    public void testForValidCode() throws Exception {
        // given
        String authorizationCode = "s9KNb-J7l-AAAAAAAAAAB1Z6nzSodQPWgAN1jwCiWeU";

        // when
        BasicSession session = underTest.loginUser("some login", authorizationCode);

        // then
        //TODO: assertion over the access token so session need to store that
        assertThat(session).isNotNull();
    }

    @Ignore
//    @Betamax(tape="dropbox_invalidLogin")
    @Test()
    public void testForCodeThatIsInvalid() throws Exception {
        // given
        String authorizationCode = "bubu";

        // when
        underTest.loginUser("some login", authorizationCode);

        // then
        // expect exception
    }
}
package pl.edu.agh.iosr.cloud.googledrive.services;

import co.freeside.betamax.Betamax;
import co.freeside.betamax.Recorder;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.sun.jersey.api.client.Client;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import pl.edu.agh.iosr.cloud.common.CloudConfiguration;
import pl.edu.agh.iosr.cloud.common.session.BasicSession;
import pl.edu.agh.iosr.cloud.googledrive.configuration.GoogleDriveCloudConfiguration;
import pl.edu.agh.iosr.cloud.onedrive.services.OnedriveCloudSessionService;

import static org.fest.assertions.Assertions.assertThat;

public class GoogleDriveCloudSessionServiceTest {

    @Rule
    public Recorder recorder = new Recorder();

    private GoogleDriveCloudSessionService underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new GoogleDriveCloudSessionService();
        //TODO: fix
        underTest.setGoogleDriveCloudConfiguration(new GoogleDriveCloudConfiguration());
    }

    @Betamax(tape="googledrive_validLogin")
    @Test
    public void testForValidCode() throws Exception {
        // given
        //TODO: fix something in tape cause betamax matches valid request to every authorization code
        String authorizationCode = "4/fisZLDYA35m0TW_G67PtqOZ913HhC7uYSu9UiY3YUow.QsMxQsXges8frjMoGjtSfTpy7cxAmgIh";

        // when
        BasicSession session = underTest.loginUser("some login", authorizationCode);

        // then
        //TODO: assertion over the access token so session need to store that
        assertThat(session).isNotNull();
    }

    @Betamax(tape="googledrive_invalidLogin")
    //TODO: more cool exception
    @Test(expected = TokenResponseException.class)
    public void testForCodeThatIsInvalid() throws Exception {
        // given
        String authorizationCode = "bubu";

        // when
        underTest.loginUser("some login", authorizationCode);

        // then
        // expect exception
    }
}
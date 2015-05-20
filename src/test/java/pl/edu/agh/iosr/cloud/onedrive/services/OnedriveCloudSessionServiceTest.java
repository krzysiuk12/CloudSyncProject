package pl.edu.agh.iosr.cloud.onedrive.services;

import co.freeside.betamax.Betamax;
import co.freeside.betamax.Recorder;
import com.sun.jersey.api.client.Client;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import pl.edu.agh.iosr.cloud.common.session.BasicSession;
import pl.edu.agh.iosr.repository.CloudSessionRepository;

import static org.fest.assertions.Assertions.assertThat;

public class OnedriveCloudSessionServiceTest {

    @Rule
    public Recorder recorder = new Recorder();

    private OnedriveCloudSessionService underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new OnedriveCloudSessionService("testApp", "THE_CLIENT_ID", "THE_CLIENT_SECRET", new Client(), new CloudSessionRepository("-", 4, 5));
    }

    @Betamax(tape="onedrive_validLogin")
    @Test
    public void testForValidCode() throws Exception {
        // given
        String authorizationCode = "THE_VALID_CODE";

        // when
        BasicSession session = underTest.loginUser("some login", authorizationCode);

        // then
        //TODO: assertion over the access token so session need to store that
        assertThat(session).isNotNull();
    }

    @Betamax(tape="onedrive_invalidLogin")
    @Test(expected = IllegalArgumentException.class)
    public void testForCodeThatIsInvalid() throws Exception {
        // given
        String authorizationCode = "bubu";

        // when
        underTest.loginUser("some login", authorizationCode);

        // then
        // expect exception
    }

}
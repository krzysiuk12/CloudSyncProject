package pl.edu.agh.iosr.cloud.onedrive.services;

import co.freeside.betamax.Betamax;
import co.freeside.betamax.Recorder;
import com.sun.jersey.api.client.Client;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import pl.edu.agh.iosr.cloud.common.CloudConfiguration;
import pl.edu.agh.iosr.cloud.common.session.BasicSession;

import static org.fest.assertions.Assertions.assertThat;

public class OnedriveCloudSessionServiceTest {

    @Rule
    public Recorder recorder = new Recorder();

    private OnedriveCloudSessionService underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new OnedriveCloudSessionService(createConfiguration(), new Client());
    }

    private CloudConfiguration createConfiguration() {
        return new CloudConfiguration("testApp", "000000004015302A", "3nbUeQnDqrFLSZwPKpjLBrYubFelq7TL");
    }

    @Betamax(tape="onedrive_validLogin")
    @Test
    public void testForValidCode() throws Exception {
        // given
        String authorizationCode = "4a5353fc-c08e-a4b3-f8f7-a08d7182a27c";

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
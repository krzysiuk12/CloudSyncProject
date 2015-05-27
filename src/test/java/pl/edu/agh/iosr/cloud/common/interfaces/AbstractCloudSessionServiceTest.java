package pl.edu.agh.iosr.cloud.common.interfaces;

import co.freeside.betamax.Betamax;
import co.freeside.betamax.MatchRule;
import co.freeside.betamax.Recorder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudSessionService;
import pl.edu.agh.iosr.cloud.common.session.BasicSession;

import static org.fest.assertions.Assertions.assertThat;

public abstract class AbstractCloudSessionServiceTest {

    @Rule
    public Recorder recorder = new Recorder();

    private ICloudSessionService underTest;

    @Before
    public void setUp() throws Exception {
        underTest = createCloudSessionService();
    }

    protected abstract ICloudSessionService createCloudSessionService();

    @Betamax(tape = "validLogin", match = {MatchRule.uri, MatchRule.method, MatchRule.body})
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

    @Betamax(tape = "invalidLogin", match = {MatchRule.uri, MatchRule.method, MatchRule.body})
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
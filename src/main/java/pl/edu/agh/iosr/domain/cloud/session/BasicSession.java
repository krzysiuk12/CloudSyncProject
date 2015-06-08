package pl.edu.agh.iosr.domain.cloud.session;

/**
 * Class represents session object which is common for each session, contains only necessary information for
 * authentication functionality.
 *
 * Created by Krzysztof Kicinger on 2015-04-11.
 */
public abstract class BasicSession {

    private Long id;
    private String sessionId;

    public BasicSession() {
    }

    public BasicSession(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}

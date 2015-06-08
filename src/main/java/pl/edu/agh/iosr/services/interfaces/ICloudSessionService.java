package pl.edu.agh.iosr.services.interfaces;

import pl.edu.agh.iosr.domain.cloud.session.CloudSession;

/**
 * Interface provides contract for session manager that each cloud implementation must be consistent with.
 *
 * Created by Krzysztof Kicinger on 2015-04-11.
 */
public interface ICloudSessionService<Session extends CloudSession> {

    public String getAuthorizationUrl() throws Exception;

    /**
     * Implementation of this method is obliged to create session (connection) with provider being target of method
     * invocation.
     *
     * @param login Application login
     * @param authorizationCode Code received from provider
     * @return BasicSession with sessionId being not null
     */
    public Session loginUser(String login, String authorizationCode) throws Exception ;

    /**
     * Implementation of this method must shutdown connection with provider and remove Session object from pool.
     *
     * @param sessionId Unique session identifier
     */
    public void logoutUser(String sessionId) throws Exception;

    /**
     * Implementation method is responsible for returning CloudSession object specific for provider being target
     * of method implementation.
     *
     * @param sessionId Unique session identifier
     * @return CloudSession specific for cloud provider
     */
    public CloudSession getSession(String sessionId);

}

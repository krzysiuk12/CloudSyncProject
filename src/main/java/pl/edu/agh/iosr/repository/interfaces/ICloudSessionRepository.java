package pl.edu.agh.iosr.repository.interfaces;

import pl.edu.agh.iosr.domain.cloud.CloudType;
import pl.edu.agh.iosr.domain.cloud.session.CloudSession;
import pl.edu.agh.iosr.domain.cloud.session.UserSession;

/**
 * Created by Krzysztof Kicinger on 2015-05-20.
 */
public interface ICloudSessionRepository {

    public String addCloudSession(String login, CloudSession cloudSession);

    public CloudSession getCloudSessionById(String sessionId);

    public UserSession getUserSessionByLogin(String login);

    public CloudSession getCloudSessionByCloudType(String login, CloudType cloudType);

    public void removeCloudSession(String sessionId);

}

package pl.edu.agh.iosr.repository;

import pl.edu.agh.iosr.cloud.common.CloudType;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.cloud.common.session.UserSession;

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

package pl.edu.agh.iosr.repository;

import pl.edu.agh.iosr.cloud.common.session.CloudSession;

/**
 * Created by Krzysztof Kicinger on 2015-05-20.
 */
public interface ICloudSessionRepository {

    public String addCloudSession(CloudSession cloudSession);

    public CloudSession getCloudSessionById(String sessionId);

    public void removeCloudSession(String sessionId);

}

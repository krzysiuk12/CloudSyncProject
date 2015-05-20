package pl.edu.agh.iosr.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import pl.edu.agh.iosr.cloud.common.CloudType;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Krzysztof Kicinger on 2015-05-20.
 */
@Repository
public class CloudSessionRepository implements ICloudSessionRepository {

    private String cloudSessionSeparator;
    private int cloudSessionSecondPartLength;
    private int cloudSessionThirdPartLength;
    private Map<String, CloudSession> cloudSessionMap = new HashMap<>();

    @Autowired
    public CloudSessionRepository(@Value("${cloudsession.separator}") String cloudSessionSeparator, @Value("${cloudsession.secondpart.length}") int cloudSessionSecondPartLength, @Value("${cloudsession.thirdpart.length}") int cloudSessionThirdPartLength) {
        this.cloudSessionSeparator = cloudSessionSeparator;
        this.cloudSessionSecondPartLength = cloudSessionSecondPartLength;
        this.cloudSessionThirdPartLength = cloudSessionThirdPartLength;
    }

    @Override
    public String addCloudSession(CloudSession cloudSession) {
        String sessionId = getUniqueSessionId(cloudSession.getCloudType());
        cloudSession.setSessionId(sessionId);
        cloudSessionMap.put(sessionId, cloudSession);
        return sessionId;
    }

    @Override
    public CloudSession getCloudSessionById(String sessionId) {
        return cloudSessionMap.get(sessionId);
    }

    @Override
    public void removeCloudSession(String sessionId) {
        cloudSessionMap.remove(sessionId);
    }

    private String getUniqueSessionId(CloudType type) {
        while(true) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(type.name().toLowerCase()).append(cloudSessionSeparator);
            stringBuilder.append(UUID.randomUUID().toString().substring(0, cloudSessionSecondPartLength)).append(cloudSessionSeparator);
            stringBuilder.append(UUID.randomUUID().toString().substring(0, cloudSessionThirdPartLength));
            if(!cloudSessionMap.containsKey(stringBuilder.toString())) {
                return stringBuilder.toString();
            }
        }
    }

}

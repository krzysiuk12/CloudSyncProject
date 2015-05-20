package pl.edu.agh.iosr.synchronization.service;

import pl.edu.agh.iosr.cloud.common.files.CloudPath;

/**
 * Created by Krzysztof Kicinger on 2015-05-18.
 */
public interface ISynchronizationService {

    public Boolean move(String sourceSessionId, CloudPath sourceCloudFile, String destinationSessionId, CloudPath destinationCloudPath) throws Exception;

    public Boolean copy(String sourceSessionId, CloudPath sourceCloudFile, String destinationSessionId, CloudPath destinationCloudPath) throws Exception;

}

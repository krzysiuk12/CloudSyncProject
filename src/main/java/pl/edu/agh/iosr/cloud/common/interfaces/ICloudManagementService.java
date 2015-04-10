package pl.edu.agh.iosr.cloud.common.interfaces;

import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Krzysztof Kicinger on 2015-04-09.
 */
public interface ICloudManagementService {

    /**
     *
     * @param sessionId
     * @param cloudDirectory
     * @return
     */
    public CloudTask<List<CloudPath>> listAllDirectoryFiles(String sessionId, CloudPath cloudDirectory);

    /**
     *
     * @param sessionId
     * @param cloudPath
     * @param outputStream
     * @return
     */
    public CloudTask<CloudPath> downloadFile(String sessionId, CloudPath cloudPath, OutputStream outputStream);

    /**
     *
     * @param sessionId
     * @param cloudPath
     * @param fileInputStream
     * @return
     */
    public CloudTask<CloudPath> uploadFile(String sessionId, CloudPath cloudPath, InputStream fileInputStream);

    /**
     *
     * @param sessionId
     * @param cloudPath
     * @return
     */
    public CloudTask<Boolean> deleteFile(String sessionId, CloudPath cloudPath);

}

package pl.edu.agh.iosr.cloud.common.interfaces;

import pl.edu.agh.iosr.cloud.common.files.CloudPath;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    public List<CloudPath> listAllDirectoryFiles(String sessionId, CloudPath cloudDirectory) throws ExecutionException, InterruptedException;

    /**
     *
     * @param sessionId
     * @param cloudPath
     * @param outputStream
     * @return
     */
    public CloudPath downloadFile(String sessionId, CloudPath cloudPath, OutputStream outputStream);

    /**
     *
     * @param sessionId
     * @param cloudPath
     * @param fileInputStream
     * @return
     */
    public CloudPath uploadFile(String sessionId, CloudPath cloudPath, InputStream fileInputStream);

    /**
     *
     * @param sessionId
     * @param cloudPath
     * @return
     */
    public Boolean deleteFile(String sessionId, CloudPath cloudPath);

}

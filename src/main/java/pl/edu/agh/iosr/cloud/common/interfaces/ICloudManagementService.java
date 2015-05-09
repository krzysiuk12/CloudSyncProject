package pl.edu.agh.iosr.cloud.common.interfaces;

import pl.edu.agh.iosr.cloud.common.files.CoolCloudPath;
import pl.edu.agh.iosr.cloud.common.files.CoolFileMetadata;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ICloudManagementService {

    /**
     *
     * @param sessionId
     * @param cloudDirectory
     * @return
     */
    public List<CoolFileMetadata> listAllDirectoryFiles(String sessionId, CoolCloudPath cloudDirectory) throws ExecutionException, InterruptedException;

    /**
     *
     * @param sessionId
     * @param path
     * @param outputStream
     * @return
     */
    public void downloadFile(String sessionId, CoolCloudPath path, OutputStream outputStream) throws ExecutionException, InterruptedException;

    /**
     *
     * @param sessionId
     * @param path
     * @param fileInputStream
     * @return
     */
    public void uploadFile(String sessionId, CoolCloudPath path, InputStream fileInputStream);

    /**
     *
     * @param sessionId
     * @param path
     * @return
     */
    public Boolean deleteFile(String sessionId, CoolCloudPath path);

}

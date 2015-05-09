package pl.edu.agh.iosr.cloud.common.interfaces;

import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;

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
    public List<FileMetadata> listAllDirectoryFiles(String sessionId, CloudPath cloudDirectory) throws ExecutionException, InterruptedException;

    /**
     *
     * @param sessionId
     * @param path
     * @param outputStream
     * @return
     */
    public void downloadFile(String sessionId, CloudPath path, OutputStream outputStream) throws ExecutionException, InterruptedException;

    /**
     *
     * @param sessionId
     * @param path
     * @param fileInputStream
     * @return
     */
    public void uploadFile(String sessionId, CloudPath path, InputStream fileInputStream);

    /**
     *
     * @param sessionId
     * @param path
     * @return
     */
    public Boolean deleteFile(String sessionId, CloudPath path);

}

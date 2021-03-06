package pl.edu.agh.iosr.services.interfaces;

import pl.edu.agh.iosr.domain.cloud.files.CloudPath;
import pl.edu.agh.iosr.domain.cloud.files.FileMetadata;
import pl.edu.agh.iosr.domain.cloud.session.CloudSession;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressAwareFuture;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Interface should be used as common root for all classes responsible for cloud management.
 */
public interface ICloudManagementService<Session extends CloudSession> {

    /**
     * Implementation of this method should provide functionality of listing all file in the cloud within given
     * cloud path.
     *
     * @param session User's session
     * @param cloudDirectory Path to the directory in the cloud.
     * @return
     */
    public ProgressAwareFuture<List<FileMetadata>> listAllDirectoryFiles(Session session, CloudPath cloudDirectory) throws ExecutionException, InterruptedException;

    /**
     * Implementation of this method is obliged to provide functionality of downloading file from the cloud and
     * point output stream to the file content.
     *
     * @param session User's session
     * @param path Path to the file in the cloud
     * @param outputStream File output stream
     * @return
     */
    public ProgressAwareFuture<Boolean> downloadFile(Session session, CloudPath path, OutputStream outputStream) throws ExecutionException, InterruptedException;

    /**
     * Impelemtation of this method should provide functionality of uploading file, which is pointed from input stream.
     *
     * @param session User's session
     * @param directory Path to the file in the cloud where file should be uploaded
     * @param inputStream File stream
     * @return
     */
    public ProgressAwareFuture<FileMetadata> uploadFile(Session session, CloudPath directory, String fileName, InputStream inputStream) throws ExecutionException, InterruptedException;

    /**
     * Implementation of this method should delete file from the cloud.
     *
     * @param session User's session
     * @param path Path to the file in the cloud
     * @return
     */
    public ProgressAwareFuture<Boolean> deleteFile(Session session, CloudPath path) throws ExecutionException, InterruptedException;

}

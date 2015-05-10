package pl.edu.agh.iosr.cloud.dropbox.tasks.factories;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxWriteMode;
import org.apache.log4j.Logger;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.dropbox.tasks.DropboxCallable;
import pl.edu.agh.iosr.cloud.dropbox.tasks.UploadFileTask;
import pl.edu.agh.iosr.cloud.dropbox.tasks.params.UploadFileTaskParams;

import java.io.InputStream;

/**
 * Created by Krzysztof Kicinger on 2015-05-10.
 */
public class UploadFileTaskFactory {

    private Logger logger = Logger.getLogger(this.getClass());

    public UploadFileTask create(final DbxClient dbxClient, CloudPath filePath, String fileName, Integer fileSize, InputStream inputStream) {
        UploadFileTaskParams uploadFileTaskParams = new UploadFileTaskParams(filePath, fileName, fileSize, inputStream);
        DropboxCallable<CloudPath> callable = getTask(dbxClient, uploadFileTaskParams);
        return new UploadFileTask(callable);
    }

    private DropboxCallable<CloudPath> getTask(final DbxClient client, final UploadFileTaskParams uploadTaskParams) {

        final String targetPath = (uploadTaskParams.getDirectory() + uploadTaskParams.getFileName()).replaceAll("//", "/");

        return new DropboxCallable<CloudPath>() {
            @Override
            public CloudPath call() throws Exception {
                try {
                    this.setProgress(0.1f);
                    DbxEntry.File uploadedFile = client.uploadFile(targetPath, DbxWriteMode.add(), uploadTaskParams.getFileSize(), uploadTaskParams.getInputStream());
                    this.setProgress(0.8f);
                    CloudPath path = new CloudPath(uploadedFile.path);
                    this.setProgress(1.0f);
                    return path;
                } catch (Exception ex) {
                    logger.error("Problem with uploading: " + ex.getMessage(), ex);
                }
                return null;
            }
        };
    }

}

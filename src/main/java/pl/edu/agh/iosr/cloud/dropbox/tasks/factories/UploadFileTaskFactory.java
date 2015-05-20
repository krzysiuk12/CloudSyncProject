package pl.edu.agh.iosr.cloud.dropbox.tasks.factories;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxWriteMode;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import pl.edu.agh.iosr.cloud.common.CloudType;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.files.FileType;
import pl.edu.agh.iosr.cloud.common.tasks.Progress;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;
import pl.edu.agh.iosr.cloud.dropbox.tasks.UploadFileTask;
import pl.edu.agh.iosr.cloud.dropbox.tasks.params.UploadFileTaskParams;

import java.io.InputStream;
import java.util.concurrent.Callable;

/**
 * Created by Krzysztof Kicinger on 2015-05-10.
 */
public class UploadFileTaskFactory {

    private Logger logger = Logger.getLogger(this.getClass());

    public UploadFileTask create(final DbxClient dbxClient, CloudPath filePath, String fileName, Integer fileSize, InputStream inputStream) {
        UploadFileTaskParams uploadFileTaskParams = new UploadFileTaskParams(filePath, fileName, fileSize, inputStream);
        return getTask(dbxClient, uploadFileTaskParams);
    }

    private UploadFileTask getTask(final DbxClient client, final UploadFileTaskParams uploadTaskParams) {
        final String targetPath = (uploadTaskParams.getDirectory().getPath() + uploadTaskParams.getFileName()).replaceAll("//", "/");
        final ProgressMonitor progressMonitor = new ProgressMonitor();
        return new UploadFileTask(progressMonitor, new Callable<FileMetadata>() {
            @Override
            public FileMetadata call() throws Exception {
                try {
                    progressMonitor.setProgress(new Progress(0.1f));
                    DbxEntry.File uploadedFile = client.uploadFile(targetPath, DbxWriteMode.add(), uploadTaskParams.getFileSize(), uploadTaskParams.getInputStream());
                    progressMonitor.setProgress(new Progress(0.8f));
                    FileMetadata.Builder fileBuilder = FileMetadata.newBuilder();
                    fileBuilder.setFileName(uploadedFile.name);
                    fileBuilder.setPath( new CloudPath(uploadedFile.path, CloudType.DROPBOX));
                    if(uploadedFile.isFile()) {
                        fileBuilder.setType(FileType.SIMPLE_FILE);
                        fileBuilder.setSize(((DbxEntry.File) uploadedFile).numBytes);
                        fileBuilder.setLastModificationTime(new DateTime(((DbxEntry.File) uploadedFile).lastModified));
                        fileBuilder.setExtension(uploadedFile.path.substring(uploadedFile.path.lastIndexOf(".") + 1));
                    } else {
                        fileBuilder.setType(FileType.DIRECTORY);
                    }
                    progressMonitor.setProgress(new Progress(1.0f));
                    return fileBuilder.build();
                } catch (Exception ex) {
                    logger.error("Problem with uploading: " + ex.getMessage(), ex);
                }
                return null;
            }
        });
    }

}

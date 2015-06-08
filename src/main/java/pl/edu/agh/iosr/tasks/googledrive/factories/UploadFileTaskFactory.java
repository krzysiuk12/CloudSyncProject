package pl.edu.agh.iosr.tasks.googledrive.factories;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import pl.edu.agh.iosr.domain.cloud.CloudType;
import pl.edu.agh.iosr.domain.cloud.files.CloudPath;
import pl.edu.agh.iosr.domain.cloud.files.FileMetadata;
import pl.edu.agh.iosr.domain.cloud.files.FileType;
import pl.edu.agh.iosr.domain.cloud.tasks.Progress;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressMonitor;
import pl.edu.agh.iosr.tasks.googledrive.UploadFileTask;
import pl.edu.agh.iosr.tasks.googledrive.params.UploadFileTaskParams;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * Created by Mateusz Drożdż on 21.05.15.
 */
public class UploadFileTaskFactory {

    public static final String GOOGLE_DIRECTORY = "application/vnd.google-apps.folder";
    private Logger logger = Logger.getLogger(this.getClass());

    public UploadFileTask create(final Drive service, CloudPath directory, String fileName, Long fileSize, InputStream inputStream) {
        UploadFileTaskParams taskParams = new UploadFileTaskParams(directory, fileName, fileSize, inputStream);
        return getTask(service, taskParams);

    }

    public UploadFileTask create(final Drive service, CloudPath directory, String fileName, InputStream inputStream) {
        UploadFileTaskParams taskParams = new UploadFileTaskParams(directory, fileName, 0L, inputStream);
        return getTask(service, taskParams);

    }

    private UploadFileTask getTask(final Drive service, final UploadFileTaskParams params) {

        final ProgressMonitor progressMonitor = new ProgressMonitor();

        return new UploadFileTask(progressMonitor, new Callable<FileMetadata>() {
            @Override
            public FileMetadata call() throws Exception {
                progressMonitor.setProgress(new Progress(0.1f));
                // File's metadata.
                File body = new File();
                body.setTitle(params.getFileName());

                // Set the parent folder.
                String parentId = params.getDirectory().getPath();
                if (parentId != null && parentId.length() > 0) {
                    body.setParents(
                            Arrays.asList(new ParentReference().setId(parentId)));
                }
                System.out.println("Uploading file with name: " + params.getFileName());
                System.out.println("Uploading file with parentId: " + parentId);

                // File's content.
                // todo: mimeType hardcoded
                InputStreamContent inputStreamContent = new InputStreamContent(null, params.getInputStream());
                try {
                    File file = service.files().insert(body, inputStreamContent).execute();
                    System.out.println("File uploaded");
                    progressMonitor.setProgress(new Progress(0.8f));
                    // MediaHttpUploader uploader = insert.getMediaHttpUploader();
                    // todo: set progress listener like: MediaHttpUploader uploader = insert.getMediaHttpUploader();
                    // uploader.setProgressListener(new FileUploadProgressListener());

                    FileMetadata.Builder fileBuilder = FileMetadata.newBuilder();
                    fileBuilder.setFileName(file.getTitle());
                    fileBuilder.setPath(new CloudPath(file.getId(), CloudType.GOOGLE_DRIVE));
                    if (GOOGLE_DIRECTORY.equals(file.getMimeType())) {
                        fileBuilder.setType(FileType.DIRECTORY);
                    } else {
                        fileBuilder.setType(FileType.SIMPLE_FILE);
                        fileBuilder.setSize(file.size());
                        fileBuilder.setExtension(file.getFileExtension() != null ? file.getFileExtension() : "");
                        fileBuilder.setLastModificationTime(new DateTime(file.getModifiedDate()));
                    }
                    progressMonitor.setProgress(new Progress(1.0f));
                    return fileBuilder.build();
                } catch (IOException e) {
                    logger.error("Error while uploading file", e);
                }
                return null;

            }
        });
    }

}

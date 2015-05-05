package pl.edu.agh.iosr.cloud.googledrive.tasks.factories;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.CoolCloudPath;
import pl.edu.agh.iosr.cloud.common.files.CoolFileMetadata;
import pl.edu.agh.iosr.cloud.common.files.FileType;
import pl.edu.agh.iosr.cloud.googledrive.tasks.GoogleDriveCallable;
import pl.edu.agh.iosr.cloud.googledrive.tasks.ListAllDirectoryFilesTask;
import pl.edu.agh.iosr.cloud.googledrive.tasks.params.ListAllDirectoryFilesTaskParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mateusz Drożdż on 18.04.15.
 */
public class ListAllDirectoryFilesTaskFactory {

    private Logger logger = Logger.getLogger(this.getClass());

    public ListAllDirectoryFilesTask create(final Drive service, CoolCloudPath directory) {
        ListAllDirectoryFilesTaskParams params = new ListAllDirectoryFilesTaskParams(directory);
        GoogleDriveCallable<List<CoolFileMetadata>> callable = getTask(service, params);
        return new ListAllDirectoryFilesTask(callable);
    }

    private GoogleDriveCallable<List<CoolFileMetadata>> getTask(final Drive service, final ListAllDirectoryFilesTaskParams params) {
        return new GoogleDriveCallable<List<CoolFileMetadata>>() {
            @Override
            public List<CoolFileMetadata> call() throws Exception {
                try {
                    setProgress(0.1f);
                    List<CoolFileMetadata> files = getResult(service, params.getDirectory().getPath());
                    setProgress(1.0f);
                    return files;
                } catch (IOException ex) {
                    logger.error("Problem during files listing.", ex);
                }
                return new LinkedList<>();
            }
        };

    }

    private List<CoolFileMetadata> getResult(Drive service, String directoryId) throws IOException {
        List<CoolFileMetadata> result = new ArrayList<>();
        // todo: fix: directory id/path, cloudPath properties
        Drive.Files.List request = service.files().list().setQ("'" + directoryId + "' in parents");
        do {
            try {
                FileList files = request.execute();
                for (File file : files.getItems()) {
                    CoolFileMetadata.Builder fileBuilder = CoolFileMetadata.newBuilder();
                    fileBuilder.setFileName(file.getTitle());
                    fileBuilder.setExtension(file.getFileExtension());
                    fileBuilder.setLastModificationTime(new DateTime(file.getModifiedDate().getValue()));
                    fileBuilder.setSize(file.getFileSize() != null ? file.getFileSize() : 0);
                    if ("application/vnd.google-apps.folder".equals(file.getMimeType()) && file.getFileExtension() == null) {
                        fileBuilder.setType(FileType.DIRECTORY);
                    } else {
                        fileBuilder.setType(FileType.SIMPLE_FILE);
                    }
                    result.add(fileBuilder.build());
                }
                request.setPageToken(files.getNextPageToken());
            } catch (IOException e) {
                request.setPageToken(null);
                throw e;
            }
        } while (request.getPageToken() != null
                && request.getPageToken().length() > 0);

        return result;
    }
}

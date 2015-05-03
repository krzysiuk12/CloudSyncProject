package pl.edu.agh.iosr.cloud.googledrive.tasks.factories;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.apache.log4j.Logger;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.CloudPathType;
import pl.edu.agh.iosr.cloud.googledrive.tasks.GoogleDriveCallable;
import pl.edu.agh.iosr.cloud.googledrive.tasks.ListAllDirectoryFilesTask;
import pl.edu.agh.iosr.cloud.googledrive.tasks.params.ListAllDirectoryFilesTaskParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mateusz Drożdż on 18.04.15.
 */
public class ListAllDirectoryFilesTaskFactory {

    private Logger logger = Logger.getLogger(this.getClass());

    public ListAllDirectoryFilesTask create(final Drive service, CloudPath directory) {
        ListAllDirectoryFilesTaskParams params = new ListAllDirectoryFilesTaskParams(directory);
        GoogleDriveCallable<List<CloudPath>> callable = getTask(service, params);
        return new ListAllDirectoryFilesTask(callable);
    }

    private GoogleDriveCallable<List<CloudPath>> getTask(final Drive service, final ListAllDirectoryFilesTaskParams params) {
        return new GoogleDriveCallable<List<CloudPath>>() {
            @Override
            public List<CloudPath> call() throws Exception {
                try {
                    setProgress(0.1f);
                    List<CloudPath> files = getResult(service, params.getDirectory().getPath());
                    setProgress(1.0f);
                    return files;
                } catch (IOException ex) {
                    logger.error("Problem during files listing.", ex);
                }
                return new LinkedList<CloudPath>();
            }
        };

    }

    private List<CloudPath> getResult(Drive service, String directoryId) throws IOException {
        List<CloudPath> result = new ArrayList<CloudPath>();
        // todo: fix: directory id/path, cloudPath properties
        Drive.Files.List request = service.files().list().setQ("'" + directoryId + "' in parents");
        do {
            try {
                FileList files = request.execute();
                for (File file : files.getItems()) {
                    CloudPath cloudFile = new CloudPath();
                    cloudFile.setId(file.getId());
                    cloudFile.setFileName(file.getTitle());
                    cloudFile.setExtension(file.getFileExtension());
                    cloudFile.setLastModificationDate(new Date(file.getModifiedDate().getValue()));
                    cloudFile.setSize(file.getFileSize() != null ? file.getFileSize() : 0);
                    if ("application/vnd.google-apps.folder".equals(file.getMimeType()) && file.getFileExtension() == null) {
                        cloudFile.setType(CloudPathType.DIRECTORY);
                    } else {
                        cloudFile.setType(CloudPathType.SIMPLE_FILE);
                    }
                    result.add(cloudFile);
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

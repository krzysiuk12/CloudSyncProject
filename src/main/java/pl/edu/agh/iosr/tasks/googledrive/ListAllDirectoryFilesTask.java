package pl.edu.agh.iosr.tasks.googledrive;

import pl.edu.agh.iosr.domain.cloud.files.FileMetadata;
import pl.edu.agh.iosr.domain.cloud.tasks.CloudTask;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressMonitor;

import java.util.List;

/**
 * Created by Mateusz Drożdż on 18.04.15.
 */
public class ListAllDirectoryFilesTask extends CloudTask<List<FileMetadata>> {

    private GoogleDriveCallable<List<FileMetadata>> callable;

    public ListAllDirectoryFilesTask(ProgressMonitor progressMonitor, GoogleDriveCallable<List<FileMetadata>> callable) {
        super(progressMonitor, callable);
        this.callable = callable;
    }

}

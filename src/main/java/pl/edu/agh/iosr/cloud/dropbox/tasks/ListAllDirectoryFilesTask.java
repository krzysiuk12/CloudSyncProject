package pl.edu.agh.iosr.cloud.dropbox.tasks;

import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;

import java.util.List;

public class ListAllDirectoryFilesTask extends CloudTask<List<FileMetadata>> {

    private DropboxCallable<List<FileMetadata>> callable;

    public ListAllDirectoryFilesTask(ProgressMonitor progressMonitor, DropboxCallable<List<FileMetadata>> callable) {
        super(progressMonitor, callable);
        this.callable = callable;
    }

}

package pl.edu.agh.iosr.cloud.dropbox.tasks;

import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;

import java.util.List;
import java.util.concurrent.Callable;

public class ListAllDirectoryFilesTask extends CloudTask<List<FileMetadata>> {

    private Callable<List<FileMetadata>> callable;

    public ListAllDirectoryFilesTask(ProgressMonitor progressMonitor, Callable<List<FileMetadata>> callable) {
        super(progressMonitor, callable);
        this.callable = callable;
    }

}

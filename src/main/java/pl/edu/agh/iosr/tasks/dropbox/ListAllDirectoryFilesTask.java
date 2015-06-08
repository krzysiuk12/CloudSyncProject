package pl.edu.agh.iosr.tasks.dropbox;

import pl.edu.agh.iosr.domain.cloud.files.FileMetadata;
import pl.edu.agh.iosr.domain.cloud.tasks.CloudTask;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressMonitor;

import java.util.List;
import java.util.concurrent.Callable;

public class ListAllDirectoryFilesTask extends CloudTask<List<FileMetadata>> {

    private Callable<List<FileMetadata>> callable;

    public ListAllDirectoryFilesTask(ProgressMonitor progressMonitor, Callable<List<FileMetadata>> callable) {
        super(progressMonitor, callable);
        this.callable = callable;
    }

}

package pl.edu.agh.iosr.cloud.googledrive.tasks;

import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;

import java.util.concurrent.Callable;

/**
 * Created by Mateusz Drożdż on 21.05.15.
 */
public class UploadFileTask extends CloudTask<FileMetadata> {
    private Callable<FileMetadata> callable;

    public UploadFileTask(ProgressMonitor progressMonitor, Callable<FileMetadata> callable) {
        super(progressMonitor, callable);
        this.callable = callable;
    }
}

package pl.edu.agh.iosr.cloud.dropbox.tasks;

import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;

import java.util.concurrent.Callable;

/**
 * Created by Krzysztof Kicinger on 2015-05-10.
 */
public class UploadFileTask extends CloudTask<FileMetadata> {

    private Callable<FileMetadata> callable;

    public UploadFileTask(ProgressMonitor progressMonitor, Callable<FileMetadata> callable) {
        super(progressMonitor, callable);
        this.callable = callable;
    }

}

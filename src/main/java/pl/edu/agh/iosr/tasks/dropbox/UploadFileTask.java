package pl.edu.agh.iosr.tasks.dropbox;

import pl.edu.agh.iosr.domain.cloud.files.FileMetadata;
import pl.edu.agh.iosr.domain.cloud.tasks.CloudTask;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressMonitor;

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

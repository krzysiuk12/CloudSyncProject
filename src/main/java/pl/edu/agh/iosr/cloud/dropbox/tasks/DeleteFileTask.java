package pl.edu.agh.iosr.cloud.dropbox.tasks;

import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;

/**
 * Created by Krzysztof Kicinger on 2015-05-10.
 */
public class DeleteFileTask extends CloudTask<Boolean> {

    private DropboxCallable<Boolean> callable;

    public DeleteFileTask(ProgressMonitor progressMonitor, DropboxCallable<Boolean> callable) {
        super(progressMonitor, callable);
        this.callable = callable;
    }

}
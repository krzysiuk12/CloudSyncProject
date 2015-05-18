package pl.edu.agh.iosr.cloud.dropbox.tasks;

import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;

import java.util.concurrent.Callable;

/**
 * Created by Krzysztof Kicinger on 2015-05-10.
 */
public class DeleteFileTask extends CloudTask<Boolean> {

    private Callable<Boolean> callable;

    public DeleteFileTask(ProgressMonitor progressMonitor, Callable<Boolean> callable) {
        super(progressMonitor, callable);
        this.callable = callable;
    }

}
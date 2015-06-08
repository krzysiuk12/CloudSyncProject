package pl.edu.agh.iosr.tasks.dropbox;

import pl.edu.agh.iosr.domain.cloud.tasks.CloudTask;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressMonitor;

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
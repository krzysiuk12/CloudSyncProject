package pl.edu.agh.iosr.tasks.googledrive;

import pl.edu.agh.iosr.domain.cloud.tasks.CloudTask;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressMonitor;

import java.util.concurrent.Callable;

/**
 * Created by Mateusz Drożdż on 21.05.15.
 */
public class DownloadFileTask extends CloudTask<Boolean> {

    private Callable<Boolean> callable;

    public DownloadFileTask(ProgressMonitor progressMonitor, Callable<Boolean> callable) {
        super(progressMonitor, callable);
        this.callable = callable;
    }
}

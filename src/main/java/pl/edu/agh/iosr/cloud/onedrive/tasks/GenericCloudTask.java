package pl.edu.agh.iosr.cloud.onedrive.tasks;

import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;

import java.util.concurrent.Callable;

public class GenericCloudTask<T> extends CloudTask<T> {
    public GenericCloudTask(ProgressMonitor progressMonitor, Callable<T> callable) {
        super(progressMonitor, callable);
    }
}

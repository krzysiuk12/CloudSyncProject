package pl.edu.agh.iosr.domain.cloud.tasks;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Class performs wrapper role for all tasks performed between application and cloud providers.
 *
 * @param <T> Returned object's type
 *
 * Created by Krzysztof Kicinger on 2015-04-09.
 */
public abstract class CloudTask<T> extends FutureTask<T> {

    private final Callable<T> callable;
    protected ProgressMonitor progressMonitor;

    protected CloudTask(ProgressMonitor progressMonitor, Callable<T> callable) {
        super(callable);
        //TODO: such a wtf with this callable
        this.callable = callable;
        this.progressMonitor = progressMonitor;
    }

    public ProgressMonitor getProgressMonitor() {
        return progressMonitor;
    }

    public Callable<T> getCallable() {
        return callable;
    }
}

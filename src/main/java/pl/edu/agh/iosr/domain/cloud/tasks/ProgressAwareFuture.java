package pl.edu.agh.iosr.domain.cloud.tasks;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ProgressAwareFuture<T> implements Future<T> {

    private final Future<T> future;
    private final ProgressMonitor progressMonitor;

    public ProgressAwareFuture(Future<T> future, ProgressMonitor progressMonitor) {
        this.future = future;
        this.progressMonitor = progressMonitor;
    }

    public Progress getProgress() {
        return progressMonitor.getProgress();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return future.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return future.isCancelled();
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return future.get();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return future.get(timeout, unit);
    }
}

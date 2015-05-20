package pl.edu.agh.iosr.execution;

import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;

import java.util.concurrent.ExecutionException;

/**
 * Created by Krzysztof Kicinger on 2015-05-20.
 */
public interface IExecutionService {

    public <T> T execute(CloudTask<T> cloudTask) throws InterruptedException, ExecutionException;

}

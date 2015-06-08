package pl.edu.agh.iosr.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.domain.cloud.tasks.CloudTask;
import pl.edu.agh.iosr.services.interfaces.IExecutionService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Krzysztof Kicinger on 2015-05-20.
 */
@Service
public class AsynchronousExecutionService implements IExecutionService {

    private ExecutorService executorService;

    @Autowired
    public AsynchronousExecutionService(@Value("${executors.poolSize}") int executorsPoolSize) {
        this.executorService = Executors.newFixedThreadPool(executorsPoolSize);
    }

    @Override
    public <T> T execute(CloudTask<T> cloudTask) throws InterruptedException, ExecutionException {
        executorService.execute(cloudTask);
        return null;
    }
}

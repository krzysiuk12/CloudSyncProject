package pl.edu.agh.iosr.cloud.onedrive.services;

import com.sun.jersey.api.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;
import pl.edu.agh.iosr.cloud.onedrive.tasks.OnedriveTaskFactory;
import pl.edu.agh.iosr.execution.IExecutionService;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Service
public class OnedriveCloudManagementService implements ICloudManagementService {

    private final OnedriveCloudSessionService onedriveSessionService;
    private final IExecutionService executionService;
    private final Client client;

    @Autowired
    public OnedriveCloudManagementService(OnedriveCloudSessionService onedriveSessionService, Client client, IExecutionService executionService) {
        this.onedriveSessionService = onedriveSessionService;
        this.executionService = executionService;
        this.client = client;
    }

    @Override
    public List<FileMetadata> listAllDirectoryFiles(String sessionId, CloudPath cloudDirectory) throws ExecutionException, InterruptedException {
        CloudSession session = onedriveSessionService.getSession(sessionId);
        //TODO: sooooooooooo cool. TaskFactory as session-scoped bean
        OnedriveTaskFactory taskFactory = new OnedriveTaskFactory(client, session);
        ProgressMonitor progressMonitor = new ProgressMonitor();
        FutureTask<List<FileMetadata>> task = new FutureTask<>(taskFactory.createListChildrenTask(cloudDirectory, progressMonitor));

/*        executorService.execute(task);
        return task.get();*/
        return null;
    }

    @Override
    public CloudTask<Boolean> downloadFile(String sessionId, CloudPath path, OutputStream outputStream) throws ExecutionException, InterruptedException {
        CloudSession session = onedriveSessionService.getSession(sessionId);
        OnedriveTaskFactory taskFactory = new OnedriveTaskFactory(client, session);
        ProgressMonitor progressMonitor = new ProgressMonitor();
        FutureTask<Object> task = new FutureTask<>(taskFactory.createDownloadTask(path, progressMonitor, outputStream));

/*        executorService.execute(task);
        task.get();*/
        return null;
    }

    @Override
    public CloudTask<FileMetadata> uploadFile(String sessionId, CloudPath directory, String fileName,  InputStream inputStream) throws ExecutionException, InterruptedException {
        CloudSession session = onedriveSessionService.getSession(sessionId);
        OnedriveTaskFactory taskFactory = new OnedriveTaskFactory(client, session);
        ProgressMonitor progressMonitor = new ProgressMonitor();
        FutureTask<FileMetadata> task = new FutureTask<>(taskFactory.createUploadTask(directory, progressMonitor, inputStream));

/*        executorService.execute(task);
        // return task.get();*/
        return null;
    }

    @Override
    public Boolean deleteFile(String sessionId, CloudPath path) throws ExecutionException, InterruptedException {
        CloudSession session = onedriveSessionService.getSession(sessionId);
        OnedriveTaskFactory taskFactory = new OnedriveTaskFactory(client, session);
        ProgressMonitor progressMonitor = new ProgressMonitor();
        FutureTask<Boolean> task = new FutureTask<>(taskFactory.createDeleteTask(path, progressMonitor));

/*        executorService.execute(task);
        return task.get();*/
        return null;
    }
}

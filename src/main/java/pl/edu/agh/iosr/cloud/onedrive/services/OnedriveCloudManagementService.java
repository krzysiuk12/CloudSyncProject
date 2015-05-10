package pl.edu.agh.iosr.cloud.onedrive.services;

import com.sun.jersey.api.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;
import pl.edu.agh.iosr.cloud.onedrive.tasks.OnedriveTaskFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

@Service
public class OnedriveCloudManagementService implements ICloudManagementService {

    private final OnedriveCloudSessionService onedriveSessionService;
    private final Executor executorService;
    private final Client client;

    @Autowired
    public OnedriveCloudManagementService(OnedriveCloudSessionService onedriveSessionService, Executor executorService, Client client) {
        this.onedriveSessionService = onedriveSessionService;
        this.executorService = executorService;
        this.client = client;
    }

    @Override
    public List<FileMetadata> listAllDirectoryFiles(String sessionId, CloudPath cloudDirectory) throws ExecutionException, InterruptedException {
        CloudSession session = onedriveSessionService.getSession(sessionId);
        //TODO: sooooooooooo cool. TaskFactory as session-scoped bean
        OnedriveTaskFactory taskFactory = new OnedriveTaskFactory(client, session);
        ProgressMonitor progressMonitor = new ProgressMonitor();
        FutureTask<List<FileMetadata>> task = new FutureTask<>(taskFactory.createListChildrenTask(cloudDirectory, progressMonitor));

        executorService.execute(task);
        return task.get();
    }

    @Override
    public void downloadFile(String sessionId, CloudPath path, OutputStream outputStream) throws ExecutionException, InterruptedException {
        CloudSession session = onedriveSessionService.getSession(sessionId);
        OnedriveTaskFactory taskFactory = new OnedriveTaskFactory(client, session);
        ProgressMonitor progressMonitor = new ProgressMonitor();
        FutureTask<Object> task = new FutureTask<>(taskFactory.createDownloadTask(path, progressMonitor, outputStream));

        executorService.execute(task);
        task.get();
    }

    @Override
    public FileMetadata uploadFile(String sessionId, CloudPath path, InputStream fileInputStream) throws ExecutionException, InterruptedException {
        CloudSession session = onedriveSessionService.getSession(sessionId);
        OnedriveTaskFactory taskFactory = new OnedriveTaskFactory(client, session);
        ProgressMonitor progressMonitor = new ProgressMonitor();
        FutureTask<FileMetadata> task = new FutureTask<>(taskFactory.createUploadTask(path, progressMonitor, fileInputStream));

        executorService.execute(task);
        return task.get();
    }

    @Override
    public Boolean deleteFile(String sessionId, CloudPath path) {
        return null;
    }
}

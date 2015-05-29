package pl.edu.agh.iosr.cloud.onedrive.services;

import com.sun.jersey.api.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressAwareFuture;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;
import pl.edu.agh.iosr.cloud.onedrive.sessionswtf.OnedriveCloudSession;
import pl.edu.agh.iosr.cloud.onedrive.tasks.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class OnedriveCloudManagementService implements ICloudManagementService<OnedriveCloudSession> {

    private final ExecutorService executorService;
    private final Client client;

    @Autowired
    public OnedriveCloudManagementService(ExecutorService executorService, Client client) {
        this.executorService = executorService;
        this.client = client;
    }

    @Override
    public ProgressAwareFuture<List<FileMetadata>> listAllDirectoryFiles(OnedriveCloudSession session, CloudPath cloudDirectory) throws ExecutionException, InterruptedException {
        //TODO: sooooooooooo cool. TaskFactory as session-scoped bean
        OnedriveTaskFactory taskFactory = new OnedriveTaskFactory(client, session);
        ProgressMonitor progressMonitor = new ProgressMonitor();
        OnedriveListChildrenTask listChildrenTask = taskFactory.createListChildrenTask(cloudDirectory, progressMonitor);
        Future<List<FileMetadata>> future = executorService.submit(listChildrenTask);

        return new ProgressAwareFuture<>(future, progressMonitor);
    }

    @Override
    public ProgressAwareFuture<Boolean> downloadFile(OnedriveCloudSession session, CloudPath path, OutputStream outputStream) throws ExecutionException, InterruptedException {
        OnedriveTaskFactory taskFactory = new OnedriveTaskFactory(client, session);
        ProgressMonitor progressMonitor = new ProgressMonitor();
        OnedriveDownloadTask downloadTask = taskFactory.createDownloadTask(path, progressMonitor, outputStream);
        Future<Boolean> future = executorService.submit(downloadTask);

        return new ProgressAwareFuture<>(future, progressMonitor);
    }

    @Override
    public ProgressAwareFuture<FileMetadata> uploadFile(OnedriveCloudSession session, CloudPath directory, String fileName,  InputStream inputStream) throws ExecutionException, InterruptedException {
        OnedriveTaskFactory taskFactory = new OnedriveTaskFactory(client, session);
        ProgressMonitor progressMonitor = new ProgressMonitor();
        OnedriveUploadTask uploadTask = taskFactory.createUploadTask(directory, progressMonitor, inputStream);
        Future<FileMetadata> future = executorService.submit(uploadTask);

        return new ProgressAwareFuture<>(future, progressMonitor);
    }

    @Override
    public ProgressAwareFuture<Boolean> deleteFile(OnedriveCloudSession session, CloudPath path) throws ExecutionException, InterruptedException {
        OnedriveTaskFactory taskFactory = new OnedriveTaskFactory(client, session);
        ProgressMonitor progressMonitor = new ProgressMonitor();
        OnedriveDeleteTask deleteTask = taskFactory.createDeleteTask(path, progressMonitor);
        Future<Boolean> future = executorService.submit(deleteTask);

        return new ProgressAwareFuture<>(future, progressMonitor);
    }
}

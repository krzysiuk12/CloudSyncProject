package pl.edu.agh.iosr.cloud.dropbox.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressAwareFuture;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;
import pl.edu.agh.iosr.cloud.dropbox.session.DropboxCloudSession;
import pl.edu.agh.iosr.cloud.dropbox.tasks.DeleteFileTask;
import pl.edu.agh.iosr.cloud.dropbox.tasks.DownloadFileTask;
import pl.edu.agh.iosr.cloud.dropbox.tasks.ListAllDirectoryFilesTask;
import pl.edu.agh.iosr.cloud.dropbox.tasks.UploadFileTask;
import pl.edu.agh.iosr.cloud.dropbox.tasks.factories.DeleteFileTaskFactory;
import pl.edu.agh.iosr.cloud.dropbox.tasks.factories.DownloadFileTaskFactory;
import pl.edu.agh.iosr.cloud.dropbox.tasks.factories.ListAllDirectoryFilesTaskFactory;
import pl.edu.agh.iosr.cloud.dropbox.tasks.factories.UploadFileTaskFactory;
import pl.edu.agh.iosr.execution.IExecutionService;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class DropboxCloudManagementService implements ICloudManagementService {

    private final DropboxCloudSessionService cloudSessionService;
    private final ExecutorService executorService;

    @Autowired
    public DropboxCloudManagementService(DropboxCloudSessionService cloudSessionService, ExecutorService executorService) {
        this.cloudSessionService = cloudSessionService;
        this.executorService = executorService;
    }

    @Override
    public ProgressAwareFuture<List<FileMetadata>> listAllDirectoryFiles(String sessionId, CloudPath cloudDirectory) throws ExecutionException, InterruptedException {
        DropboxCloudSession dropboxCloudSession = cloudSessionService.getSession(sessionId);
        ListAllDirectoryFilesTask task = new ListAllDirectoryFilesTaskFactory().create(dropboxCloudSession.getClient(), cloudDirectory);

        return new ProgressAwareFuture<>(executorService.submit(task.getCallable()), task.getProgressMonitor());
    }

    @Override
    public ProgressAwareFuture<Boolean> downloadFile(String sessionId, CloudPath path, OutputStream outputStream) throws ExecutionException, InterruptedException {
        DropboxCloudSession dropboxCloudSession = cloudSessionService.getSession(sessionId);
        DownloadFileTask downloadFileTask = new DownloadFileTaskFactory().create(dropboxCloudSession.getClient(), path, outputStream);

        return new ProgressAwareFuture<>(executorService.submit(downloadFileTask.getCallable()), downloadFileTask.getProgressMonitor());
    }

    @Override
    public ProgressAwareFuture<FileMetadata> uploadFile(String sessionId, CloudPath directory, String fileName, InputStream inputStream) throws ExecutionException, InterruptedException {
        DropboxCloudSession dropboxCloudSession = cloudSessionService.getSession(sessionId);
        UploadFileTask uploadFileTask = new UploadFileTaskFactory().create(dropboxCloudSession.getClient(), directory, fileName, -1, inputStream);
        return new ProgressAwareFuture<>(executorService.submit(uploadFileTask.getCallable()), uploadFileTask.getProgressMonitor());
    }

    @Override
    public ProgressAwareFuture<Boolean> deleteFile(String sessionId, CloudPath path) throws ExecutionException, InterruptedException {
        DropboxCloudSession dropboxCloudSession = cloudSessionService.getSession(sessionId);
        DeleteFileTask task = new DeleteFileTaskFactory().create(dropboxCloudSession.getClient(), path);

        return new ProgressAwareFuture<>(executorService.submit(task.getCallable()), task.getProgressMonitor());
    }

}

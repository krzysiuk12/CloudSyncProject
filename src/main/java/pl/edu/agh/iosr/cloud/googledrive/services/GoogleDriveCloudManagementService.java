package pl.edu.agh.iosr.cloud.googledrive.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressAwareFuture;
import pl.edu.agh.iosr.cloud.googledrive.session.GoogleDriveCloudSession;
import pl.edu.agh.iosr.cloud.googledrive.tasks.DownloadFileTask;
import pl.edu.agh.iosr.cloud.googledrive.tasks.ListAllDirectoryFilesTask;
import pl.edu.agh.iosr.cloud.googledrive.tasks.UploadFileTask;
import pl.edu.agh.iosr.cloud.googledrive.tasks.factories.DownloadFileTaskFactory;
import pl.edu.agh.iosr.cloud.googledrive.tasks.factories.ListAllDirectoryFilesTaskFactory;
import pl.edu.agh.iosr.cloud.googledrive.tasks.factories.UploadFileTaskFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class GoogleDriveCloudManagementService implements ICloudManagementService {

    private final GoogleDriveCloudSessionService cloudSessionService;
    private final ExecutorService executorService;

    @Autowired
    public GoogleDriveCloudManagementService(GoogleDriveCloudSessionService cloudSessionService, ExecutorService executorService) {
        this.cloudSessionService = cloudSessionService;
        this.executorService = executorService;
    }

    @Override
    public ProgressAwareFuture<List<FileMetadata>> listAllDirectoryFiles(String sessionId, CloudPath cloudDirectory) throws ExecutionException, InterruptedException {
        GoogleDriveCloudSession cloudSession = cloudSessionService.getSession(sessionId);
        ListAllDirectoryFilesTask task = new ListAllDirectoryFilesTaskFactory().create(cloudSession.getDrive(), cloudDirectory);
        Future<List<FileMetadata>> future = executorService.submit(task.getCallable());

        return new ProgressAwareFuture<>(future, task.getProgressMonitor());
    }

    @Override
    public ProgressAwareFuture<Boolean> downloadFile(String sessionId, CloudPath path, OutputStream outputStream) throws ExecutionException, InterruptedException {
        GoogleDriveCloudSession cloudSession = cloudSessionService.getSession(sessionId);
        DownloadFileTask task = new DownloadFileTaskFactory().create(cloudSession.getDrive(), path, outputStream);
        Future<Boolean> future = executorService.submit(task.getCallable());

        return new ProgressAwareFuture<>(future, task.getProgressMonitor());
    }

    @Override
    public ProgressAwareFuture<FileMetadata> uploadFile(String sessionId, CloudPath path, String fileName, InputStream inputStream) throws ExecutionException, InterruptedException {
        GoogleDriveCloudSession cloudSession = cloudSessionService.getSession(sessionId);
        UploadFileTask task = new UploadFileTaskFactory().create(cloudSession.getDrive(), path, fileName, inputStream);
        Future<FileMetadata> future = executorService.submit(task.getCallable());

        return new ProgressAwareFuture<>(future, task.getProgressMonitor());
    }

    @Override
    public ProgressAwareFuture<Boolean> deleteFile(String sessionId, CloudPath path) {
        return null;
    }
}

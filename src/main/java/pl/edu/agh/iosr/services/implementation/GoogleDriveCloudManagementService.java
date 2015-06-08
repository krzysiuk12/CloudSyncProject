package pl.edu.agh.iosr.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.domain.cloud.files.CloudPath;
import pl.edu.agh.iosr.domain.cloud.files.FileMetadata;
import pl.edu.agh.iosr.services.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressAwareFuture;
import pl.edu.agh.iosr.domain.cloud.session.GoogleDriveCloudSession;
import pl.edu.agh.iosr.tasks.googledrive.DownloadFileTask;
import pl.edu.agh.iosr.tasks.googledrive.ListAllDirectoryFilesTask;
import pl.edu.agh.iosr.tasks.googledrive.UploadFileTask;
import pl.edu.agh.iosr.tasks.googledrive.factories.DownloadFileTaskFactory;
import pl.edu.agh.iosr.tasks.googledrive.factories.ListAllDirectoryFilesTaskFactory;
import pl.edu.agh.iosr.tasks.googledrive.factories.UploadFileTaskFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class GoogleDriveCloudManagementService implements ICloudManagementService<GoogleDriveCloudSession> {

    private final ExecutorService executorService;

    @Autowired
    public GoogleDriveCloudManagementService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public ProgressAwareFuture<List<FileMetadata>> listAllDirectoryFiles(GoogleDriveCloudSession session, CloudPath cloudDirectory) throws ExecutionException, InterruptedException {
        ListAllDirectoryFilesTask task = new ListAllDirectoryFilesTaskFactory().create(session.getDrive(), cloudDirectory);
        Future<List<FileMetadata>> future = executorService.submit(task.getCallable());

        return new ProgressAwareFuture<>(future, task.getProgressMonitor());
    }

    @Override
    public ProgressAwareFuture<Boolean> downloadFile(GoogleDriveCloudSession session, CloudPath path, OutputStream outputStream) throws ExecutionException, InterruptedException {
        DownloadFileTask task = new DownloadFileTaskFactory().create(session.getDrive(), path, outputStream);
        Future<Boolean> future = executorService.submit(task.getCallable());

        return new ProgressAwareFuture<>(future, task.getProgressMonitor());
    }

    @Override
    public ProgressAwareFuture<FileMetadata> uploadFile(GoogleDriveCloudSession session, CloudPath path, String fileName, InputStream inputStream) throws ExecutionException, InterruptedException {
        UploadFileTask task = new UploadFileTaskFactory().create(session.getDrive(), path, fileName, inputStream);
        Future<FileMetadata> future = executorService.submit(task.getCallable());

        return new ProgressAwareFuture<>(future, task.getProgressMonitor());
    }

    @Override
    public ProgressAwareFuture<Boolean> deleteFile(GoogleDriveCloudSession sessionId, CloudPath path) {
        return null;
    }
}

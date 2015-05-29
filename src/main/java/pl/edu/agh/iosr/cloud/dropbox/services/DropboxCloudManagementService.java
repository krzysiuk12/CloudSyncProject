package pl.edu.agh.iosr.cloud.dropbox.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressAwareFuture;
import pl.edu.agh.iosr.cloud.dropbox.session.DropboxCloudSession;
import pl.edu.agh.iosr.cloud.dropbox.tasks.DeleteFileTask;
import pl.edu.agh.iosr.cloud.dropbox.tasks.DownloadFileTask;
import pl.edu.agh.iosr.cloud.dropbox.tasks.ListAllDirectoryFilesTask;
import pl.edu.agh.iosr.cloud.dropbox.tasks.UploadFileTask;
import pl.edu.agh.iosr.cloud.dropbox.tasks.factories.DeleteFileTaskFactory;
import pl.edu.agh.iosr.cloud.dropbox.tasks.factories.DownloadFileTaskFactory;
import pl.edu.agh.iosr.cloud.dropbox.tasks.factories.ListAllDirectoryFilesTaskFactory;
import pl.edu.agh.iosr.cloud.dropbox.tasks.factories.UploadFileTaskFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@Service
public class DropboxCloudManagementService implements ICloudManagementService<DropboxCloudSession> {

    private final ExecutorService executorService;

    @Autowired
    public DropboxCloudManagementService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public ProgressAwareFuture<List<FileMetadata>> listAllDirectoryFiles(DropboxCloudSession session, CloudPath cloudDirectory) throws ExecutionException, InterruptedException {
        ListAllDirectoryFilesTask task = new ListAllDirectoryFilesTaskFactory().create(session.getClient(), cloudDirectory);
        return new ProgressAwareFuture<>(executorService.submit(task.getCallable()), task.getProgressMonitor());
    }

    @Override
    public ProgressAwareFuture<Boolean> downloadFile(DropboxCloudSession session, CloudPath path, OutputStream outputStream) throws ExecutionException, InterruptedException {
        DownloadFileTask downloadFileTask = new DownloadFileTaskFactory().create(session.getClient(), path, outputStream);
        return new ProgressAwareFuture<>(executorService.submit(downloadFileTask.getCallable()), downloadFileTask.getProgressMonitor());
    }

    @Override
    public ProgressAwareFuture<FileMetadata> uploadFile(DropboxCloudSession session, CloudPath directory, String fileName, InputStream inputStream) throws ExecutionException, InterruptedException {
        UploadFileTask uploadFileTask = new UploadFileTaskFactory().create(session.getClient(), directory, fileName, -1, inputStream);
        return new ProgressAwareFuture<>(executorService.submit(uploadFileTask.getCallable()), uploadFileTask.getProgressMonitor());
    }

    @Override
    public ProgressAwareFuture<Boolean> deleteFile(DropboxCloudSession session, CloudPath path) throws ExecutionException, InterruptedException {
        DeleteFileTask task = new DeleteFileTaskFactory().create(session.getClient(), path);
        return new ProgressAwareFuture<>(executorService.submit(task.getCallable()), task.getProgressMonitor());
    }

}

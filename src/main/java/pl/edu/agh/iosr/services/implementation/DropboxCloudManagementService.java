package pl.edu.agh.iosr.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.domain.cloud.files.CloudPath;
import pl.edu.agh.iosr.domain.cloud.files.FileMetadata;
import pl.edu.agh.iosr.services.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressAwareFuture;
import pl.edu.agh.iosr.domain.cloud.session.DropboxCloudSession;
import pl.edu.agh.iosr.tasks.dropbox.DeleteFileTask;
import pl.edu.agh.iosr.tasks.dropbox.DownloadFileTask;
import pl.edu.agh.iosr.tasks.dropbox.ListAllDirectoryFilesTask;
import pl.edu.agh.iosr.tasks.dropbox.UploadFileTask;
import pl.edu.agh.iosr.tasks.dropbox.factories.DeleteFileTaskFactory;
import pl.edu.agh.iosr.tasks.dropbox.factories.DownloadFileTaskFactory;
import pl.edu.agh.iosr.tasks.dropbox.factories.ListAllDirectoryFilesTaskFactory;
import pl.edu.agh.iosr.tasks.dropbox.factories.UploadFileTaskFactory;

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

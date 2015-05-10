package pl.edu.agh.iosr.cloud.dropbox.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
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
import java.util.concurrent.Executors;

@Service
public class DropboxCloudManagementService implements ICloudManagementService {

    private DropboxCloudSessionService cloudSessionService;
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    public DropboxCloudManagementService(DropboxCloudSessionService cloudSessionService) {
        this.cloudSessionService = cloudSessionService;
    }

    @Override
    public List<FileMetadata> listAllDirectoryFiles(String sessionId, CloudPath cloudDirectory) throws ExecutionException, InterruptedException {
        DropboxCloudSession dropboxCloudSession = cloudSessionService.getSession(sessionId);
        ListAllDirectoryFilesTask task = new ListAllDirectoryFilesTaskFactory().create(dropboxCloudSession.getClient(), cloudDirectory);
        executorService.execute(task);
        return task.get();
    }

    @Override
    public Boolean downloadFile(String sessionId, CloudPath path, OutputStream outputStream) throws ExecutionException, InterruptedException {
        DropboxCloudSession dropboxCloudSession = cloudSessionService.getSession(sessionId);
        DownloadFileTask task = new DownloadFileTaskFactory().create(dropboxCloudSession.getClient(), path, outputStream);
        executorService.execute(task);
        return task.get();
    }

    @Override
    public FileMetadata uploadFile(String sessionId, CloudPath directory, String fileName, Integer fileSize, InputStream inputStream) throws ExecutionException, InterruptedException {
        DropboxCloudSession dropboxCloudSession = cloudSessionService.getSession(sessionId);
        UploadFileTask task = new UploadFileTaskFactory().create(dropboxCloudSession.getClient(), directory, fileName, fileSize, inputStream);
        executorService.execute(task);
        return new FileMetadata(task.get(), null, null, null, 0L, null);
    }

    @Override
    public Boolean deleteFile(String sessionId, CloudPath path) throws ExecutionException, InterruptedException {
        DropboxCloudSession dropboxCloudSession = cloudSessionService.getSession(sessionId);
        DeleteFileTask task = new DeleteFileTaskFactory().create(dropboxCloudSession.getClient(), path);
        executorService.execute(task);
        return task.get();
    }

}

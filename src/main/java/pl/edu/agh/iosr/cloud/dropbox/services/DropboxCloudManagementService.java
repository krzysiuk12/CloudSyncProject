package pl.edu.agh.iosr.cloud.dropbox.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;
import pl.edu.agh.iosr.cloud.dropbox.session.DropboxCloudSession;
import pl.edu.agh.iosr.cloud.dropbox.tasks.DeleteFileTask;
import pl.edu.agh.iosr.cloud.dropbox.tasks.ListAllDirectoryFilesTask;
import pl.edu.agh.iosr.cloud.dropbox.tasks.factories.DeleteFileTaskFactory;
import pl.edu.agh.iosr.cloud.dropbox.tasks.factories.DownloadFileTaskFactory;
import pl.edu.agh.iosr.cloud.dropbox.tasks.factories.ListAllDirectoryFilesTaskFactory;
import pl.edu.agh.iosr.cloud.dropbox.tasks.factories.UploadFileTaskFactory;
import pl.edu.agh.iosr.execution.IExecutionService;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class DropboxCloudManagementService implements ICloudManagementService {

    private DropboxCloudSessionService cloudSessionService;
    private IExecutionService executionService;

    @Autowired
    public DropboxCloudManagementService(DropboxCloudSessionService cloudSessionService, IExecutionService executionService) {
        this.cloudSessionService = cloudSessionService;
        this.executionService = executionService;
    }

    @Override
    public List<FileMetadata> listAllDirectoryFiles(String sessionId, CloudPath cloudDirectory) throws ExecutionException, InterruptedException {
        DropboxCloudSession dropboxCloudSession = cloudSessionService.getSession(sessionId);
        ListAllDirectoryFilesTask task = new ListAllDirectoryFilesTaskFactory().create(dropboxCloudSession.getClient(), cloudDirectory);
        return executionService.execute(task);
    }

    @Override
    public CloudTask<Boolean> downloadFile(String sessionId, CloudPath path, OutputStream outputStream) throws ExecutionException, InterruptedException {
        DropboxCloudSession dropboxCloudSession = cloudSessionService.getSession(sessionId);
        return new DownloadFileTaskFactory().create(dropboxCloudSession.getClient(), path, outputStream);
    }

    @Override
    public CloudTask<FileMetadata> uploadFile(String sessionId, CloudPath directory, String fileName, InputStream inputStream) throws ExecutionException, InterruptedException {
        DropboxCloudSession dropboxCloudSession = cloudSessionService.getSession(sessionId);
        return new UploadFileTaskFactory().create(dropboxCloudSession.getClient(), directory, fileName, -1, inputStream);
    }

    @Override
    public Boolean deleteFile(String sessionId, CloudPath path) throws ExecutionException, InterruptedException {
        DropboxCloudSession dropboxCloudSession = cloudSessionService.getSession(sessionId);
        DeleteFileTask task = new DeleteFileTaskFactory().create(dropboxCloudSession.getClient(), path);
        return executionService.execute(task);
    }

}

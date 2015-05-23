package pl.edu.agh.iosr.cloud.googledrive.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;
import pl.edu.agh.iosr.cloud.googledrive.session.GoogleDriveCloudSession;
import pl.edu.agh.iosr.cloud.googledrive.tasks.DownloadFileTask;
import pl.edu.agh.iosr.cloud.googledrive.tasks.ListAllDirectoryFilesTask;
import pl.edu.agh.iosr.cloud.googledrive.tasks.UploadFileTask;
import pl.edu.agh.iosr.cloud.googledrive.tasks.factories.DownloadFileTaskFactory;
import pl.edu.agh.iosr.cloud.googledrive.tasks.factories.ListAllDirectoryFilesTaskFactory;
import pl.edu.agh.iosr.cloud.googledrive.tasks.factories.UploadFileTaskFactory;
import pl.edu.agh.iosr.execution.IExecutionService;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class GoogleDriveCloudManagementService implements ICloudManagementService {

    @Autowired
    private GoogleDriveCloudSessionService cloudSessionService;
    private IExecutionService executionService;

    @Autowired
    public GoogleDriveCloudManagementService(GoogleDriveCloudSessionService cloudSessionService, IExecutionService executionService) {
        this.cloudSessionService = cloudSessionService;
        this.executionService = executionService;
    }

    @Override
    public List<FileMetadata> listAllDirectoryFiles(String sessionId, CloudPath cloudDirectory) throws ExecutionException, InterruptedException {
        GoogleDriveCloudSession cloudSession = cloudSessionService.getSession(sessionId);
        ListAllDirectoryFilesTask task = new ListAllDirectoryFilesTaskFactory().create(cloudSession.getDrive(), cloudDirectory);
        executionService.execute(task);
        return task.get();
    }

    @Override
    public CloudTask<Boolean> downloadFile(String sessionId, CloudPath path, OutputStream outputStream) throws ExecutionException, InterruptedException {
        GoogleDriveCloudSession cloudSession = cloudSessionService.getSession(sessionId);
        DownloadFileTask task = new DownloadFileTaskFactory().create(cloudSession.getDrive(), path, outputStream);
        return task;
    }

    @Override
    public CloudTask<FileMetadata> uploadFile(String sessionId, CloudPath path, String fileName, InputStream inputStream) throws ExecutionException, InterruptedException {
        GoogleDriveCloudSession cloudSession = cloudSessionService.getSession(sessionId);
        UploadFileTask task = new UploadFileTaskFactory().create(cloudSession.getDrive(), path, fileName, inputStream);
        return task;
    }

    @Override
    public Boolean deleteFile(String sessionId, CloudPath path) {
        return null;
    }
}

package pl.edu.agh.iosr.cloud.googledrive.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.files.CoolCloudPath;
import pl.edu.agh.iosr.cloud.common.files.CoolFileMetadata;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.cloud.googledrive.session.GoogleDriveCloudSession;
import pl.edu.agh.iosr.cloud.googledrive.tasks.ListAllDirectoryFilesTask;
import pl.edu.agh.iosr.cloud.googledrive.tasks.factories.ListAllDirectoryFilesTaskFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class GoogleDriveCloudManagementService implements ICloudManagementService {

    @Autowired
    private GoogleDriveCloudSessionService cloudSessionService;
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Override
    public List<CoolFileMetadata> listAllDirectoryFiles(String sessionId, CoolCloudPath cloudDirectory) throws ExecutionException, InterruptedException {
        GoogleDriveCloudSession cloudSession = cloudSessionService.getSession(sessionId);
        ListAllDirectoryFilesTask task = new ListAllDirectoryFilesTaskFactory().create(cloudSession.getDrive(), cloudDirectory);
        executorService.execute(task);
        return task.get();
    }

    @Override
    public void downloadFile(String sessionId, CoolCloudPath path, OutputStream outputStream) throws ExecutionException, InterruptedException {
    }

    @Override
    public void uploadFile(String sessionId, CoolCloudPath path, InputStream fileInputStream) {
    }

    @Override
    public Boolean deleteFile(String sessionId, CoolCloudPath path) {
        return null;
    }
}

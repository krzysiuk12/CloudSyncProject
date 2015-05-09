package pl.edu.agh.iosr.cloud.dropbox.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.CoolCloudPath;
import pl.edu.agh.iosr.cloud.common.files.CoolFileMetadata;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.cloud.dropbox.session.DropboxCloudSession;
import pl.edu.agh.iosr.cloud.dropbox.tasks.ListAllDirectoryFilesTask;
import pl.edu.agh.iosr.cloud.dropbox.tasks.factories.ListAllDirectoryFilesTaskFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Krzysztof Kicinger on 2015-04-11.
 */
@Service
public class DropboxCloudManagementService implements ICloudManagementService {

    private DropboxCloudSessionService cloudSessionService;
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    public DropboxCloudManagementService(DropboxCloudSessionService cloudSessionService) {
        this.cloudSessionService = cloudSessionService;
    }

    @Override
    public List<CoolFileMetadata> listAllDirectoryFiles(String sessionId, CoolCloudPath cloudDirectory) throws ExecutionException, InterruptedException {
        DropboxCloudSession dropboxCloudSession = cloudSessionService.getSession(sessionId);
        ListAllDirectoryFilesTask task = new ListAllDirectoryFilesTaskFactory().create(dropboxCloudSession.getClient(), cloudDirectory);
        executorService.execute(task);
        return task.get();
    }

    @Override
    public CloudPath downloadFile(String sessionId, CoolCloudPath path, OutputStream outputStream) throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public CloudPath uploadFile(String sessionId, CloudPath cloudPath, InputStream fileInputStream) {
        return null;
    }

    @Override
    public Boolean deleteFile(String sessionId, CloudPath cloudPath) {
        return null;
    }

}

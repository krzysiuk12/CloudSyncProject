package pl.edu.agh.iosr.cloud.onedrive.services;

import com.sun.jersey.api.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.cloud.onedrive.tasks.OnedriveTaskFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

@Service
public class OnedriveCloudManagementService implements ICloudManagementService {

    private final OnedriveCloudSessionService onedriveSessionService;
    private final Executor executorService;
    private final Client client;

    @Autowired
    public OnedriveCloudManagementService(OnedriveCloudSessionService onedriveSessionService, Executor executorService, Client client) {
        this.onedriveSessionService = onedriveSessionService;
        this.executorService = executorService;
        this.client = client;
    }

    @Override
    public List<CloudPath> listAllDirectoryFiles(String sessionId, CloudPath cloudDirectory) throws ExecutionException, InterruptedException {
        CloudSession session = onedriveSessionService.getSession(sessionId);
        //TODO: sooooooooooo cool. TaskFactory as session-scoped bean
        OnedriveTaskFactory taskFactory = new OnedriveTaskFactory(client, session);
        FutureTask<List<CloudPath>> task = new FutureTask<>(taskFactory.createListChildrenTask(cloudDirectory.getPath()));

        executorService.execute(task);
        return task.get();
    }

    @Override
    public CloudPath downloadFile(String sessionId, CloudPath cloudPath, OutputStream outputStream) {
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

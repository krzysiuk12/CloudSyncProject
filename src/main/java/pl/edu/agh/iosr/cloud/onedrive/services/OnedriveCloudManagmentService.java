package pl.edu.agh.iosr.cloud.onedrive.services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

@Service
public class OnedriveCloudManagmentService implements ICloudManagementService {

    private final OnedriveCloudSessionService onedriveSessionService;
    private final Executor executorService;
    private final Client client;

    @Autowired
    public OnedriveCloudManagmentService(OnedriveCloudSessionService onedriveSessionService, Executor executorService, Client client) {
        this.onedriveSessionService = onedriveSessionService;
        this.executorService = executorService;
        this.client = client;
    }

    @Override
    public List<CloudPath> listAllDirectoryFiles(String sessionId, CloudPath cloudDirectory) throws ExecutionException, InterruptedException {
        final CloudSession session = onedriveSessionService.getSession(sessionId);
        CloudTask<List<CloudPath>> task = new CloudTask<List<CloudPath>>(new Callable<List<CloudPath>>() {
            @Override
            public List<CloudPath> call() throws Exception {
                WebResource webResource = queryListDirectories("/", session.getAccessToken());
                ClientResponse clientResponse = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
                String rawResponse = clientResponse.getEntity(String.class);
                //TODO: fix. for now wild injecting json to cloudPath.filename
//                JSONObject responseJson = new JSONObject(rawResponse);
                CloudPath megaPath = new CloudPath();
                megaPath.setFileName(rawResponse);
                return Collections.singletonList(megaPath);
            }
        }

        ) {
            @Override
            public float getProgress() {
                return 1.0f;
            }
        };
        executorService.execute(task);
        return task.get();
    }

    private WebResource queryListDirectories(String path, String accessToken) {
        return client.resource(String.format("https://api.onedrive.com/v1.0/drive/root:%s:/children", path))
                .queryParam("access_token", accessToken);
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

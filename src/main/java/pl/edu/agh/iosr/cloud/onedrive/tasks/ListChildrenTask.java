package pl.edu.agh.iosr.cloud.onedrive.tasks;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.edu.agh.iosr.cloud.common.files.CoolCloudPath;
import pl.edu.agh.iosr.cloud.common.files.CoolFileMetadata;
import pl.edu.agh.iosr.cloud.common.files.FileType;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.cloud.common.tasks.Progress;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ListChildrenTask implements Callable<List<CoolFileMetadata>> {

    private final Client client;
    private final CloudSession session;
    private final CoolCloudPath rootPath;
    private final ProgressMonitor progressMonitor;

    ListChildrenTask(Client client, CloudSession session, CoolCloudPath rootPath, ProgressMonitor progressMonitor) {
        this.client = client;
        this.session = session;
        this.rootPath = rootPath;
        this.progressMonitor = progressMonitor;
    }

    @Override
    public List<CoolFileMetadata> call() throws Exception {
        WebResource webResource = queryListDirectories(rootPath, session.getAccessToken());
        //TODO: extract obtaining and processing of the client response to some entity
        ClientResponse clientResponse = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        updateProgress(0.5);
        String rawResponse = clientResponse.getEntity(String.class);

        JSONObject responseJson = new JSONObject(rawResponse);
        JSONArray files = responseJson.getJSONArray("value");
        List<CoolFileMetadata> cloudPaths = new ArrayList<>();
        for (int i = 0; i < files.length(); i++) {
            cloudPaths.add(fileFromJson(files.getJSONObject(i)));
            double newProgress = 0.5 + 0.5 * ( (double) i / files.length() );
            updateProgress(newProgress);
        }

        return cloudPaths;
    }

    private void updateProgress(double value) {
        progressMonitor.setProgress(new Progress(value));
    }

    private CoolFileMetadata fileFromJson(JSONObject file) {
        CoolFileMetadata.Builder fileBuilder = CoolFileMetadata.newBuilder();
        fileBuilder.setPath(new CoolCloudPath(rootPath + file.getString("name")));
        fileBuilder.setFileName(file.getString("name"));
        fileBuilder.setSize(file.getInt("size"));
        fileBuilder.setType(file.has("folder") ? FileType.DIRECTORY : FileType.SIMPLE_FILE);

        return fileBuilder.build();
    }

    private WebResource queryListDirectories(CoolCloudPath path, String accessToken) {
        return client.resource(String.format("https://api.onedrive.com/v1.0/drive/root:%s:/children", path.getPath()))
                .queryParam("access_token", accessToken);
    }
}

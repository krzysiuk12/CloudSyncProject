package pl.edu.agh.iosr.cloud.onedrive.tasks;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.CloudPathType;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ListChildrenTask implements Callable<List<CloudPath>> {

    private final Client client;
    private final CloudSession session;
    private final String rootPath;

    ListChildrenTask(Client client, CloudSession session, String rootPath) {
        this.client = client;
        this.session = session;
        this.rootPath = rootPath;
    }

    @Override
    public List<CloudPath> call() throws Exception {
        //TODO: progress monitoring

        WebResource webResource = queryListDirectories(rootPath, session.getAccessToken());
        //TODO: extract obtaining and processing of the client response to some entity
        ClientResponse clientResponse = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        String rawResponse = clientResponse.getEntity(String.class);

        JSONObject responseJson = new JSONObject(rawResponse);
        JSONArray files = responseJson.getJSONArray("value");
        List<CloudPath> cloudPaths = new ArrayList<>();
        for (int i = 0; i < files.length(); i++) {
            cloudPaths.add(fileFromJson(files.getJSONObject(i)));
        }

        return cloudPaths;
    }

    private CloudPath fileFromJson(JSONObject file) {
        CloudPath path = new CloudPath();
        path.setPath(rootPath + file.getString("name"));
        path.setFileName(file.getString("name"));
        path.setSize(file.getInt("size"));
        path.setType(file.has("folder") ? CloudPathType.DIRECTORY : CloudPathType.SIMPLE_FILE);
        return path;
    }

    private WebResource queryListDirectories(String path, String accessToken) {
        return client.resource(String.format("https://api.onedrive.com/v1.0/drive/root:%s:/children", path))
                .queryParam("access_token", accessToken);
    }
}

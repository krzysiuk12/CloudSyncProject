package pl.edu.agh.iosr.tasks.onedrive;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.edu.agh.iosr.domain.cloud.CloudType;
import pl.edu.agh.iosr.domain.cloud.files.CloudPath;
import pl.edu.agh.iosr.domain.cloud.files.FileMetadata;
import pl.edu.agh.iosr.domain.cloud.files.FileType;
import pl.edu.agh.iosr.domain.cloud.session.CloudSession;
import pl.edu.agh.iosr.domain.cloud.tasks.Progress;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressMonitor;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class OnedriveListChildrenTask implements Callable<List<FileMetadata>> {

    private final Client client;
    private final CloudSession session;
    private final CloudPath rootPath;
    private final ProgressMonitor progressMonitor;

    OnedriveListChildrenTask(Client client, CloudSession session, CloudPath rootPath, ProgressMonitor progressMonitor) {
        this.client = client;
        this.session = session;
        this.rootPath = rootPath;
        this.progressMonitor = progressMonitor;
    }

    @Override
    public List<FileMetadata> call() throws Exception {
        WebResource webResource = queryListDirectories(rootPath, session.getAccessToken());
        //TODO: extract obtaining and processing of the client response to some entity
        ClientResponse clientResponse = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        updateProgress(0.5);
        String rawResponse = clientResponse.getEntity(String.class);

        JSONObject responseJson = new JSONObject(rawResponse);
        JSONArray files = responseJson.getJSONArray("value");
        List<FileMetadata> cloudPaths = new ArrayList<>();
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

    private FileMetadata fileFromJson(JSONObject file) {
        FileMetadata.Builder fileBuilder = FileMetadata.newBuilder();
        fileBuilder.setPath(new CloudPath(rootPath.getPath() + file.getString("name"), CloudType.ONE_DRIVE));
        fileBuilder.setFileName(file.getString("name"));
        fileBuilder.setSize(file.getInt("size"));
        fileBuilder.setType(file.has("folder") ? FileType.DIRECTORY : FileType.SIMPLE_FILE);

        return fileBuilder.build();
    }

    private WebResource queryListDirectories(CloudPath path, String accessToken) {
        return client.resource(String.format("https://api.onedrive.com/v1.0/drive/root:%s:/children", path.getPath()))
                .queryParam("access_token", accessToken);
    }
}

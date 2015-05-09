package pl.edu.agh.iosr.cloud.onedrive.tasks;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.CoolCloudPath;
import pl.edu.agh.iosr.cloud.common.files.CoolFileMetadata;
import pl.edu.agh.iosr.cloud.common.files.FileType;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.cloud.common.tasks.Progress;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;

import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class OnedriveDownloadTask implements Callable<CloudPath> {

    private final Client client;
    private final CloudSession session;
    private final CoolCloudPath path;
    private final ProgressMonitor progressMonitor;
    private final OutputStream stream;

    public OnedriveDownloadTask(Client client, CloudSession session, CoolCloudPath path, ProgressMonitor progressMonitor, OutputStream stream) {
        this.client = client;
        this.session = session;
        this.path = path;
        this.progressMonitor = progressMonitor;
        this.stream = stream;
    }

    @Override
    public CloudPath call() throws Exception {
        WebResource webResource = queryDownloadFile(path, session.getAccessToken());
        ClientResponse clientResponse = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        updateProgress(1.0);
        String rawResponse = clientResponse.getEntity(String.class);

        stream.write(rawResponse.getBytes());

        return new CloudPath();
    }

    private WebResource queryDownloadFile(CoolCloudPath path, String accessToken) {
        return client.resource(String.format("https://api.onedrive.com/v1.0/drive/root:%s:/content", path.getPath()))
                .queryParam("access_token", accessToken);
    }

    private void updateProgress(double value) {
        progressMonitor.setProgress(new Progress(value));
    }

    private CoolFileMetadata fileFromJson(JSONObject file) {
        CoolFileMetadata.Builder fileBuilder = CoolFileMetadata.newBuilder();
        fileBuilder.setPath(new CoolCloudPath(path + file.getString("name")));
        fileBuilder.setFileName(file.getString("name"));
        fileBuilder.setSize(file.getInt("size"));
        fileBuilder.setType(file.has("folder") ? FileType.DIRECTORY : FileType.SIMPLE_FILE);

        return fileBuilder.build();
    }
}

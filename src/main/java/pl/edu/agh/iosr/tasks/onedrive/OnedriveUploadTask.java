package pl.edu.agh.iosr.tasks.onedrive;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.json.JSONObject;
import pl.edu.agh.iosr.domain.cloud.files.CloudPath;
import pl.edu.agh.iosr.domain.cloud.files.FileMetadata;
import pl.edu.agh.iosr.domain.cloud.files.FileType;
import pl.edu.agh.iosr.domain.cloud.session.CloudSession;
import pl.edu.agh.iosr.domain.cloud.tasks.Progress;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressMonitor;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.concurrent.Callable;

public class OnedriveUploadTask implements Callable<FileMetadata> {

    private final Client client;
    private final CloudSession session;
    private final CloudPath path;
    private final ProgressMonitor progressMonitor;
    private final InputStream stream;

    public OnedriveUploadTask(Client client, CloudSession session, CloudPath path, ProgressMonitor progressMonitor, InputStream stream) {
        this.client = client;
        this.session = session;
        this.path = path;
        this.progressMonitor = progressMonitor;
        this.stream = stream;
    }

    @Override
    public FileMetadata call() throws Exception {
        WebResource webResource = queryDownloadFile(path, session.getAccessToken());
        String streamContent = IOUtils.toString(stream);
        ClientResponse clientResponse = webResource.accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, streamContent);
        updateProgress(1.0);
        String rawResponse = clientResponse.getEntity(String.class);

        return createMetadata(rawResponse);
    }

    private FileMetadata createMetadata(String rawResponse) {
        JSONObject responseJson = new JSONObject(rawResponse);
        FileMetadata.Builder metadataBuilder = FileMetadata.newBuilder();

        metadataBuilder.setFileName(responseJson.getString("name"));
        metadataBuilder.setLastModificationTime(new DateTime(responseJson.getString("lastModifiedDateTime")));
        metadataBuilder.setSize(responseJson.getInt("size"));
        metadataBuilder.setPath(path);
        metadataBuilder.setType(responseJson.has("file") ? FileType.SIMPLE_FILE : FileType.DIRECTORY);

        return metadataBuilder.build();
    }

    private WebResource queryDownloadFile(CloudPath path, String accessToken) {
        return client.resource(String.format("https://api.onedrive.com/v1.0/drive/root:%s:/content", path.getPath()))
                .queryParam("access_token", accessToken);
    }

    private void updateProgress(double value) {
        progressMonitor.setProgress(new Progress(value));
    }
}

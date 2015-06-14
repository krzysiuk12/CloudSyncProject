package pl.edu.agh.iosr.tasks.onedrive;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import pl.edu.agh.iosr.domain.cloud.files.CloudPath;
import pl.edu.agh.iosr.domain.cloud.session.CloudSession;
import pl.edu.agh.iosr.domain.cloud.tasks.Progress;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressMonitor;

import javax.ws.rs.core.MediaType;
import java.io.OutputStream;
import java.util.concurrent.Callable;

public class OnedriveDownloadTask implements Callable<Boolean> {

    private final Client client;
    private final CloudSession session;
    private final CloudPath path;
    private final ProgressMonitor progressMonitor;
    private final OutputStream stream;

    public OnedriveDownloadTask(Client client, CloudSession session, CloudPath path, ProgressMonitor progressMonitor, OutputStream stream) {
        this.client = client;
        this.session = session;
        this.path = path;
        this.progressMonitor = progressMonitor;
        this.stream = stream;
    }

    @Override
    public Boolean call() throws Exception {
        WebResource webResource = queryDownloadFile(path, session.getAccessToken());
        ClientResponse clientResponse = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        updateProgress(1.0);
        String rawResponse = clientResponse.getEntity(String.class);

        stream.write(rawResponse.getBytes());
        stream.close();

        return true;
    }

    private WebResource queryDownloadFile(CloudPath path, String accessToken) {
        return client.resource(String.format("https://api.onedrive.com/v1.0/drive/root:%s:/content", path.getPath()))
                .queryParam("access_token", accessToken);
    }

    private void updateProgress(double value) {
        progressMonitor.setProgress(new Progress(value));
    }
}

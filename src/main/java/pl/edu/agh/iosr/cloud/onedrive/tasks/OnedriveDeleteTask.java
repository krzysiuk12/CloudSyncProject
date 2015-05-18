package pl.edu.agh.iosr.cloud.onedrive.tasks;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.cloud.common.tasks.Progress;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;

import javax.ws.rs.core.MediaType;
import java.util.concurrent.Callable;

public class OnedriveDeleteTask implements Callable<Boolean> {

    private final Client client;
    private final CloudSession session;
    private final CloudPath path;
    private final ProgressMonitor progressMonitor;

    public OnedriveDeleteTask(Client client, CloudSession session, CloudPath path, ProgressMonitor progressMonitor) {
        this.client = client;
        this.session = session;
        this.path = path;
        this.progressMonitor = progressMonitor;
    }

    @Override
    public Boolean call() throws Exception {
        WebResource webResource = queryDownloadFile(path, session.getAccessToken());
        webResource.accept(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
        updateProgress(1.0);
        //TODO: grab response and return some feedback

        return true;
    }

    private WebResource queryDownloadFile(CloudPath path, String accessToken) {
        return client.resource(String.format("https://api.onedrive.com/v1.0/drive/root:%s", path.getPath()))
                .queryParam("access_token", accessToken);
    }

    private void updateProgress(double value) {
        progressMonitor.setProgress(new Progress(value));
    }

}

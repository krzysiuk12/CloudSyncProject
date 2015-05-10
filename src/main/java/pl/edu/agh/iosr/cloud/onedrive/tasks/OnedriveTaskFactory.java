package pl.edu.agh.iosr.cloud.onedrive.tasks;

import com.sun.jersey.api.client.Client;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;

import java.io.InputStream;
import java.io.OutputStream;

public class OnedriveTaskFactory {

    private final Client client;
    private final CloudSession session;

    public OnedriveTaskFactory(Client client, CloudSession session) {
        this.client = client;
        this.session = session;
    }

    public OnedriveListChildrenTask createListChildrenTask(CloudPath rootPath, ProgressMonitor progressMonitor) {
        return new OnedriveListChildrenTask(client, session, rootPath, progressMonitor);
    }

    public OnedriveDownloadTask createDownloadTask(CloudPath path, ProgressMonitor progressMonitor, OutputStream stream) {
        return new OnedriveDownloadTask(client, session, path, progressMonitor, stream);
    }

    public OnedriveUploadTask createUploadTask(CloudPath path, ProgressMonitor progressMonitor, InputStream stream) {
        return new OnedriveUploadTask(client, session, path, progressMonitor, stream);
    }
}

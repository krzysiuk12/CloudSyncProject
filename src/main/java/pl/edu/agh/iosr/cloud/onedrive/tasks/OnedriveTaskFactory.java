package pl.edu.agh.iosr.cloud.onedrive.tasks;

import com.sun.jersey.api.client.Client;
import pl.edu.agh.iosr.cloud.common.files.CoolCloudPath;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;

import java.io.OutputStream;

public class OnedriveTaskFactory {

    private final Client client;
    private final CloudSession session;

    public OnedriveTaskFactory(Client client, CloudSession session) {
        this.client = client;
        this.session = session;
    }

    public ListChildrenTask createListChildrenTask(CoolCloudPath rootPath, ProgressMonitor progressMonitor) {
        return new ListChildrenTask(client, session, rootPath, progressMonitor);
    }

    public OnedriveDownloadTask createDownloadTask(CoolCloudPath path, ProgressMonitor progressMonitor, OutputStream stream) {
        return new OnedriveDownloadTask(client, session, path, progressMonitor, stream);
    }
}

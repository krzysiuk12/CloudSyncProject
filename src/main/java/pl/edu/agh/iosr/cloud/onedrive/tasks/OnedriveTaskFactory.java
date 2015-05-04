package pl.edu.agh.iosr.cloud.onedrive.tasks;

import com.sun.jersey.api.client.Client;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;

public class OnedriveTaskFactory {

    private final Client client;
    private final CloudSession session;

    public OnedriveTaskFactory(Client client, CloudSession session) {
        this.client = client;
        this.session = session;
    }

    public ListChildrenTask createListChildrenTask(String rootPath, ProgressMonitor progressMonitor) {
        return new ListChildrenTask(client, session, rootPath, progressMonitor);
    }

}

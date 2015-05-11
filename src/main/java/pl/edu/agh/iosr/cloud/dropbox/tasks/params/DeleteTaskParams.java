package pl.edu.agh.iosr.cloud.dropbox.tasks.params;

import pl.edu.agh.iosr.cloud.common.files.CloudPath;

/**
 * Created by Krzysztof Kicinger on 2015-05-10.
 */
public class DeleteTaskParams {

    private final CloudPath path;

    public DeleteTaskParams(CloudPath path) {
        this.path = path;
    }

    public CloudPath getPath() {
        return path;
    }

}

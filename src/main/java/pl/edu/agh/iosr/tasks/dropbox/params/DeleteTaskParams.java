package pl.edu.agh.iosr.tasks.dropbox.params;

import pl.edu.agh.iosr.domain.cloud.files.CloudPath;

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

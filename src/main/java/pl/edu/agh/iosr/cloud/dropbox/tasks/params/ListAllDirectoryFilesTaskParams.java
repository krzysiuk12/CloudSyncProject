package pl.edu.agh.iosr.cloud.dropbox.tasks.params;

import pl.edu.agh.iosr.cloud.common.files.CloudPath;

/**
 * Created by Krzysztof Kicinger on 2015-04-13.
 */
public class ListAllDirectoryFilesTaskParams {

    private CloudPath directory;

    public ListAllDirectoryFilesTaskParams(CloudPath directory) {
        this.directory = directory;
    }

    public CloudPath getDirectory() {
        return directory;
    }
}

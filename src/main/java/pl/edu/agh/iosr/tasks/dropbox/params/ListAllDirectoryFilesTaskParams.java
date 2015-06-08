package pl.edu.agh.iosr.tasks.dropbox.params;

import pl.edu.agh.iosr.domain.cloud.files.CloudPath;

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

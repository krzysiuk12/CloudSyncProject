package pl.edu.agh.iosr.cloud.googledrive.tasks.params;

import pl.edu.agh.iosr.cloud.common.files.CloudPath;

/**
 * Created by Mateusz Drożdż on 18.04.15.
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

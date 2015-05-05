package pl.edu.agh.iosr.cloud.googledrive.tasks.params;

import pl.edu.agh.iosr.cloud.common.files.CoolCloudPath;

/**
 * Created by Mateusz Drożdż on 18.04.15.
 */
public class ListAllDirectoryFilesTaskParams {

    private CoolCloudPath directory;

    public ListAllDirectoryFilesTaskParams(CoolCloudPath directory) {
        this.directory = directory;
    }

    public CoolCloudPath getDirectory() {
        return directory;
    }
}

package pl.edu.agh.iosr.cloud.dropbox.tasks.params;

import pl.edu.agh.iosr.cloud.common.files.CoolCloudPath;

/**
 * Created by Krzysztof Kicinger on 2015-04-13.
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

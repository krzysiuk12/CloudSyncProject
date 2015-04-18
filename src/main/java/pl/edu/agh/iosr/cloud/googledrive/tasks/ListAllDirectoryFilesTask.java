package pl.edu.agh.iosr.cloud.googledrive.tasks;

import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;

import java.util.List;

/**
 * Created by Mateusz Drożdż on 18.04.15.
 */
public class ListAllDirectoryFilesTask extends CloudTask<List<CloudPath>> {

    private GoogleDriveCallable<List<CloudPath>> callable;

    public ListAllDirectoryFilesTask(GoogleDriveCallable<List<CloudPath>> callable) {
        super(callable);
        this.callable = callable;
    }

    @Override
    public float getProgress() {
        return callable.getProgress();
    }
}

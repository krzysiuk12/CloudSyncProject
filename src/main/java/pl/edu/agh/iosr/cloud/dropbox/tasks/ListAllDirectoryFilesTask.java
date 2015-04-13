package pl.edu.agh.iosr.cloud.dropbox.tasks;

import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;

import java.util.List;

/**
 * Created by Krzysztof Kicinger on 2015-04-13.
 */
public class ListAllDirectoryFilesTask extends CloudTask<List<CloudPath>> {

    private DropboxCallable<List<CloudPath>> callable;

    public ListAllDirectoryFilesTask(DropboxCallable<List<CloudPath>> callable) {
        super(callable);
        this.callable = callable;
    }

    @Override
    public float getProgress() {
        return callable.getProgress();
    }

}

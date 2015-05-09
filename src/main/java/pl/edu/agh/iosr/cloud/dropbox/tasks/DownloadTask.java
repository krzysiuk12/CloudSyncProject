package pl.edu.agh.iosr.cloud.dropbox.tasks;

import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;

public class DownloadTask extends CloudTask<CloudPath> {

    private DropboxCallable<CloudPath> callable;

    public DownloadTask(DropboxCallable<CloudPath> callable) {
        super(callable);
        this.callable = callable;
    }

    @Override
    public float getProgress() {
        return callable.getProgress();
    }
}

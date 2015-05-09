package pl.edu.agh.iosr.cloud.dropbox.tasks;

import pl.edu.agh.iosr.cloud.common.files.CoolCloudPath;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;

public class DownloadTask extends CloudTask<CoolCloudPath> {

    private DropboxCallable<CoolCloudPath> callable;

    public DownloadTask(DropboxCallable<CoolCloudPath> callable) {
        super(callable);
        this.callable = callable;
    }

    @Override
    public float getProgress() {
        return callable.getProgress();
    }
}

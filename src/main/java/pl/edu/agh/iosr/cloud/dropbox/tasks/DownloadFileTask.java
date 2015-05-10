package pl.edu.agh.iosr.cloud.dropbox.tasks;

import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;

/**
 * Created by Krzysztof Kicinger on 2015-05-10.
 */
public class DownloadFileTask extends CloudTask<Boolean> {

    private DropboxCallable<Boolean> callable;

    public DownloadFileTask(DropboxCallable<Boolean> callable) {
        super(callable);
        this.callable = callable;
    }

    @Override
    public float getProgress() {
        return callable.getProgress();
    }
}

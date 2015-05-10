package pl.edu.agh.iosr.cloud.dropbox.tasks;

import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;

/**
 * Created by Krzysztof Kicinger on 2015-05-10.
 */
public class DeleteFileTask extends CloudTask<Boolean> {

    private DropboxCallable<Boolean> callable;

    public DeleteFileTask(DropboxCallable<Boolean> callable) {
        super(callable);
        this.callable = callable;
    }

    @Override
    public float getProgress() {
        return callable.getProgress();
    }

}
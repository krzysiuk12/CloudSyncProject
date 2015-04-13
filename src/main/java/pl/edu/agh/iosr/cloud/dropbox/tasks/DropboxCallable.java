package pl.edu.agh.iosr.cloud.dropbox.tasks;

import java.util.concurrent.Callable;

/**
 * Created by Krzysztof Kicinger on 2015-04-13.
 */
public abstract class DropboxCallable<T> implements Callable<T> {

    private float progress = 0.0f;

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
}

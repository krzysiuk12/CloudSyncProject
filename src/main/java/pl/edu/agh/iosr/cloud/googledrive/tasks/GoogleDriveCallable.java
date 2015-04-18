package pl.edu.agh.iosr.cloud.googledrive.tasks;

import java.util.concurrent.Callable;

/**
 * Created by Mateusz Drożdż on 18.04.15.
 */
public abstract class GoogleDriveCallable<T> implements Callable<T> {

    private float progress = 0.0f;

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
}

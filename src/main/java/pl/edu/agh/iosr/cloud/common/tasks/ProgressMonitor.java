package pl.edu.agh.iosr.cloud.common.tasks;

public class ProgressMonitor {

    private Progress progress;

    //TODO: make this thread-safe (volatile or lock?)

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }
}

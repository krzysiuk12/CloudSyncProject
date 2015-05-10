package pl.edu.agh.iosr.cloud.common.tasks;

public class ProgressMonitor {

    private Progress progress;

    public ProgressMonitor() {
        this.progress = new Progress(0.0f);
    }

    public synchronized Progress getProgress() {
        return progress;
    }

    public synchronized void setProgress(Progress progress) {
        this.progress = progress;
    }

}

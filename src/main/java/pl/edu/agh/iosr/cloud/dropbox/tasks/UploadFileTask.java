package pl.edu.agh.iosr.cloud.dropbox.tasks;

import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;

/**
 * Created by Krzysztof Kicinger on 2015-05-10.
 */
public class UploadFileTask extends CloudTask<CloudPath> {

    private DropboxCallable<CloudPath> callable;

    public UploadFileTask(ProgressMonitor progressMonitor, DropboxCallable<CloudPath> callable) {
        super(progressMonitor, callable);
        this.callable = callable;
    }

}

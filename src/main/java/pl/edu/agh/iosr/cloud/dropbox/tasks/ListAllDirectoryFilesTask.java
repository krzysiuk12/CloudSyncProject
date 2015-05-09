package pl.edu.agh.iosr.cloud.dropbox.tasks;

import pl.edu.agh.iosr.cloud.common.files.CoolFileMetadata;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;

import java.util.List;

public class ListAllDirectoryFilesTask extends CloudTask<List<CoolFileMetadata>> {

    private DropboxCallable<List<CoolFileMetadata>> callable;

    public ListAllDirectoryFilesTask(DropboxCallable<List<CoolFileMetadata>> callable) {
        super(callable);
        this.callable = callable;
    }

    @Override
    public float getProgress() {
        return callable.getProgress();
    }

}

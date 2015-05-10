package pl.edu.agh.iosr.cloud.dropbox.tasks;

import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;

import java.util.List;

public class ListAllDirectoryFilesTask extends CloudTask<List<FileMetadata>> {

    private DropboxCallable<List<FileMetadata>> callable;

    public ListAllDirectoryFilesTask(DropboxCallable<List<FileMetadata>> callable) {
        super(callable);
        this.callable = callable;
    }

    public float getProgress() {
        return callable.getProgress();
    }

}

package pl.edu.agh.iosr.cloud.dropbox.tasks.params;

import pl.edu.agh.iosr.cloud.common.files.CloudPath;

import java.io.InputStream;

/**
 * Created by Krzysztof Kicinger on 2015-05-10.
 */
public class UploadFileTaskParams {

    private final CloudPath directory;
    private final String fileName;
    private final Integer fileSize;
    private final InputStream inputStream;

    public UploadFileTaskParams(CloudPath directory, String fileName, Integer fileSize, InputStream inputStream) {
        this.directory = new CloudPath(directory != null ? directory.getPath() + "/" : "/");
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.inputStream = inputStream;
    }

    public CloudPath getDirectory() {
        return directory;
    }

    public String getFileName() {
        return fileName;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

}

package pl.edu.agh.iosr.tasks.googledrive.params;

import pl.edu.agh.iosr.domain.cloud.CloudType;
import pl.edu.agh.iosr.domain.cloud.files.CloudPath;

import java.io.InputStream;

/**
 * Created by Mateusz Drożdż on 21.05.15.
 */
public class UploadFileTaskParams {
    private final CloudPath directory;
    private final String fileName;
    private final Long fileSize;
    private final InputStream inputStream;

    public UploadFileTaskParams(CloudPath directory, String fileName, Long fileSize, InputStream inputStream) {
        this.directory = new CloudPath(directory != null ? directory.getPath() : "root", CloudType.GOOGLE_DRIVE);
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

    public Long getFileSize() {
        return fileSize;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}

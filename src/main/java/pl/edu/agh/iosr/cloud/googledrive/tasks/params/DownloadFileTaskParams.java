package pl.edu.agh.iosr.cloud.googledrive.tasks.params;

import pl.edu.agh.iosr.cloud.common.files.CloudPath;

import java.io.OutputStream;

/**
 * Created by Mateusz Drożdż on 21.05.15.
 */
public class DownloadFileTaskParams {
    private final CloudPath filePath;
    private final OutputStream outputStream;

    public DownloadFileTaskParams(CloudPath filePath, OutputStream outputStream) {
        this.filePath = filePath;
        this.outputStream = outputStream;
    }

    public CloudPath getFilePath() {
        return filePath;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}

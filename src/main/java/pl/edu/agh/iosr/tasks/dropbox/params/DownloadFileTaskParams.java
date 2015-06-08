package pl.edu.agh.iosr.tasks.dropbox.params;

import pl.edu.agh.iosr.domain.cloud.files.CloudPath;

import java.io.OutputStream;

/**
 * Created by Krzysztof Kicinger on 2015-05-10.
 */
public class DownloadFileTaskParams {

    private final CloudPath cloudPath;
    private final OutputStream outputStream;

    public DownloadFileTaskParams(CloudPath cloudPath, OutputStream outputStream) {
        this.cloudPath = cloudPath;
        this.outputStream = outputStream;
    }

    public CloudPath getCloudPath() {
        return cloudPath;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}

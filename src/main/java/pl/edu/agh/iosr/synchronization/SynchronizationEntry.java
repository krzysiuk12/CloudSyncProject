package pl.edu.agh.iosr.synchronization;

import pl.edu.agh.iosr.cloud.common.files.CloudPath;

import java.util.List;

/**
 * Created by Krzysztof Kicinger on 2015-05-20.
 */
public class SynchronizationEntry {

    private CloudPath sourcePath;
    private List<CloudPath> destinationPaths;

    public SynchronizationEntry() {
    }

    public SynchronizationEntry(CloudPath sourcePath, List<CloudPath> destinationPaths) {
        this.sourcePath = sourcePath;
        this.destinationPaths = destinationPaths;
    }

    public CloudPath getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(CloudPath sourcePath) {
        this.sourcePath = sourcePath;
    }

    public List<CloudPath> getDestinationPaths() {
        return destinationPaths;
    }

    public void setDestinationPaths(List<CloudPath> destinationPaths) {
        this.destinationPaths = destinationPaths;
    }
}

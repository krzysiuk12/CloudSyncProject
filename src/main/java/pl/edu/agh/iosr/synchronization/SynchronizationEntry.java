package pl.edu.agh.iosr.synchronization;

import java.util.List;

/**
 * Created by Krzysztof Kicinger on 2015-05-20.
 */
public class SynchronizationEntry {

    private SynchronizationSingleCloudEntry source;
    private List<SynchronizationSingleCloudEntry> destination;
    private SynchronizationType type;

    public SynchronizationEntry() {
    }

    public SynchronizationEntry(SynchronizationSingleCloudEntry source, List<SynchronizationSingleCloudEntry> destination, SynchronizationType type) {
        this.source = source;
        this.destination = destination;
        this.type = type;
    }

    public SynchronizationSingleCloudEntry getSource() {
        return source;
    }

    public void setSource(SynchronizationSingleCloudEntry source) {
        this.source = source;
    }

    public List<SynchronizationSingleCloudEntry> getDestination() {
        return destination;
    }

    public void setDestination(List<SynchronizationSingleCloudEntry> destination) {
        this.destination = destination;
    }

    public SynchronizationType getType() {
        return type;
    }

    public void setType(SynchronizationType type) {
        this.type = type;
    }

}

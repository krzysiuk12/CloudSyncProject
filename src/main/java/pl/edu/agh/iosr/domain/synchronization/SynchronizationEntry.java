package pl.edu.agh.iosr.domain.synchronization;

import java.util.List;

/**
 * Created by Krzysztof Kicinger on 2015-05-20.
 */
public class SynchronizationEntry {

    private String login;
    private SynchronizationSingleCloudEntry source;
    private List<SynchronizationSingleCloudEntry> destination;
    private SynchronizationType type;

    public SynchronizationEntry() {
    }

    public SynchronizationEntry(String login, SynchronizationSingleCloudEntry source, List<SynchronizationSingleCloudEntry> destination, SynchronizationType type) {
        this.login = login;
        this.source = source;
        this.destination = destination;
        this.type = type;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

package pl.edu.agh.iosr.cloud.common.files;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.edu.agh.iosr.cloud.common.CloudType;

public class CloudPath {

    private final String path;
    private final CloudType type;

    @JsonCreator
    public CloudPath(@JsonProperty("path") String path, @JsonProperty("type") CloudType type) {
        this.path = path;
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public CloudType getType() {
        return type;
    }
}

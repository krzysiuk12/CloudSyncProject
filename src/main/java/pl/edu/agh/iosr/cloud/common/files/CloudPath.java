package pl.edu.agh.iosr.cloud.common.files;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CloudPath {

    private final String path;

    @JsonCreator
    public CloudPath(@JsonProperty("path") String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

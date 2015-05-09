package pl.edu.agh.iosr.cloud.common.files;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CoolCloudPath {

    private final String path;

    @JsonCreator
    public CoolCloudPath(@JsonProperty("path") String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

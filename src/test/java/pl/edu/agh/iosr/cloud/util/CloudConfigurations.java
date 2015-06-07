package pl.edu.agh.iosr.cloud.util;

import pl.edu.agh.iosr.cloud.common.CloudConfiguration;

public abstract class CloudConfigurations {

    public static CloudConfiguration dropbox() {
        return new CloudConfiguration("CloudSyncIosrProject", "nlsu5a4n7jcr3il", "c1tufi0cm9vjoh0");
    }
}

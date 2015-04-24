package pl.edu.agh.iosr.cloud.common;

public class CloudConfiguration {

    private final String appName;
    private final String appKey;
    private final String appKeySecret;

    public CloudConfiguration(String appName, String appKey, String appKeySecret) {
        this.appName = appName;
        this.appKey = appKey;
        this.appKeySecret = appKeySecret;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getAppKeySecret() {
        return appKeySecret;
    }
}

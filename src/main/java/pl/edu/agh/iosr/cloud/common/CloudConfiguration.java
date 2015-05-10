package pl.edu.agh.iosr.cloud.common;

import pl.edu.agh.iosr.cloud.common.interfaces.ICloudConfiguration;

public class CloudConfiguration implements ICloudConfiguration {

    private final String appName;
    private final String appKey;
    private final String appKeySecret;

    public CloudConfiguration(String appName, String appKey, String appKeySecret) {
        this.appName = appName;
        this.appKey = appKey;
        this.appKeySecret = appKeySecret;
    }

    @Override
    public String getAppName() {
        return appName;
    }

    @Override
    public String getAppKey() {
        return appKey;
    }

    @Override
    public String getAppKeySecret() {
        return appKeySecret;
    }

}

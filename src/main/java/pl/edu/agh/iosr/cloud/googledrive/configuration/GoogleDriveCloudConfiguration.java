package pl.edu.agh.iosr.cloud.googledrive.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.edu.agh.iosr.cloud.common.CloudConfiguration;

/**
 * Created by Mateusz Drożdż on 18.04.15.
 */
@Component
public class GoogleDriveCloudConfiguration { //extends ICloudConfiguration {

    private CloudConfiguration cloudConfiguration;

    @Autowired
    public GoogleDriveCloudConfiguration(@Value("${googleDrive.appName}") String appName, @Value("${googleDrive.appKey}") String appKey, @Value("${googleDrive.appKeySecret}") String appKeySecret) {
        this.cloudConfiguration = new CloudConfiguration(appName, appKey, appKeySecret);
    }

    public String getAppName() {
        return cloudConfiguration.getAppName();
    }

    public String getAppKey() {
        return cloudConfiguration.getAppKey();
    }

    public String getAppKeySecret() {
        return cloudConfiguration.getAppKeySecret();
    }

}

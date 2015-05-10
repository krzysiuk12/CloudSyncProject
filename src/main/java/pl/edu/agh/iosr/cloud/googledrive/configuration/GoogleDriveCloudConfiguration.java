package pl.edu.agh.iosr.cloud.googledrive.configuration;

import org.springframework.stereotype.Component;

/**
 * Created by Mateusz Drożdż on 18.04.15.
 */
@Component
public class GoogleDriveCloudConfiguration { //extends ICloudConfiguration {

    private static final String APP_NAME = "CloudSyncIosrProject";
    private static final String APP_KEY = "2828043487-aepcph91ibr6vurdij6qo2nckv2dmgt7.apps.googleusercontent.com";
    private static final String APP_KEY_SECRET = "s4CWlPWyq4n0-rMmRosqnUxt";

    //@Override
    public String getAppName() {
        return APP_NAME;
    }

    //@Override
    public String getAppKey() {
        return APP_KEY;
    }

    //@Override
    public String getAppKeySecret() {
        return APP_KEY_SECRET;
    }

}

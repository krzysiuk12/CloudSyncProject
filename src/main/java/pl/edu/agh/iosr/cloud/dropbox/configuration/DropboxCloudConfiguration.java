package pl.edu.agh.iosr.cloud.dropbox.configuration;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.edu.agh.iosr.cloud.common.CloudConfiguration;

import java.util.Locale;

/**
 * Created by Krzysztof Kicinger on 2015-04-09.
 */
@Component
public class DropboxCloudConfiguration {

    private CloudConfiguration cloudConfiguration;
    private DbxAppInfo appInfo;
    private DbxRequestConfig requestConfig;
    private DbxWebAuthNoRedirect webAuth;

    @Autowired
    public DropboxCloudConfiguration(@Value("${dropbox.appName}") String appName, @Value("${dropbox.appKey}") String appKey, @Value("${dropbox.appKeySecret}") String appKeySecret) {
        this.cloudConfiguration = new CloudConfiguration(appName, appKey, appKeySecret);
    }

    public DbxAppInfo getAppInfo() {
        if (appInfo == null) {
            appInfo = new DbxAppInfo(getAppKey(), getAppKeySecret());
        }
        return appInfo;
    }

    public DbxRequestConfig getRequestConfig() {
        if(requestConfig == null) {
            requestConfig = new DbxRequestConfig(getAppName(), Locale.getDefault().toString());
        }
        return requestConfig;
    }

    public DbxWebAuthNoRedirect getWebAuth() {
        if (webAuth == null) {
            webAuth = new DbxWebAuthNoRedirect(getRequestConfig(), getAppInfo());
        }
        return webAuth;
    }

    private String getAppName() {
        return cloudConfiguration.getAppName();
    }

    private String getAppKey() {
        return cloudConfiguration.getAppKey();
    }

    private String getAppKeySecret() {
        return cloudConfiguration.getAppKeySecret();
    }

}

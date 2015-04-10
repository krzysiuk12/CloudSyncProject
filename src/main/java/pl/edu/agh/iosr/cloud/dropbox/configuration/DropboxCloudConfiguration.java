package pl.edu.agh.iosr.cloud.dropbox.configuration;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import org.springframework.stereotype.Component;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudConfiguration;

import java.util.Locale;

/**
 * Created by Krzysztof Kicinger on 2015-04-09.
 */
@Component
public class DropboxCloudConfiguration extends ICloudConfiguration {

    private static final String APP_NAME = "CloudSyncIosrProject";
    private static final String APP_KEY = "nlsu5a4n7jcr3il";
    private static final String APP_KEY_SECRET = "c1tufi0cm9vjoh0";

    private DbxAppInfo appInfo;
    private DbxRequestConfig requestConfig;
    private DbxWebAuthNoRedirect webAuth;

    @Override
    protected String getAppName() {
        return APP_NAME;
    }

    @Override
    protected String getAppKey() {
        return APP_KEY;
    }

    @Override
    protected String getAppKeySecret() {
        return APP_KEY_SECRET;
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
}

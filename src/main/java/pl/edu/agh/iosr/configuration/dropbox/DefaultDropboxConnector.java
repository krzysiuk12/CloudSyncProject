package pl.edu.agh.iosr.configuration.dropbox;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import pl.edu.agh.iosr.domain.cloud.CloudConfiguration;

import java.util.Locale;

public class DefaultDropboxConnector implements DropboxConnector {

    private final CloudConfiguration cloudConfiguration;
    private DbxRequestConfig requestConfig;
    private DbxWebAuthNoRedirect webAuth;

    public DefaultDropboxConnector(CloudConfiguration cloudConfiguration) {
        this.cloudConfiguration = cloudConfiguration;
    }

    @Override
    public DbxRequestConfig getRequestConfig() {
        if (requestConfig == null) {
            requestConfig = new DbxRequestConfig(cloudConfiguration.getAppName(), Locale.getDefault().toString());
        }
        return requestConfig;
    }

    @Override
    public DbxWebAuthNoRedirect getWebAuth() {
        if (webAuth == null) {
            webAuth = new DbxWebAuthNoRedirect(getRequestConfig(), createAppInfo());
        }
        return webAuth;
    }

    private DbxAppInfo createAppInfo() {
        return new DbxAppInfo(cloudConfiguration.getAppKey(), cloudConfiguration.getAppKeySecret());
    }
}

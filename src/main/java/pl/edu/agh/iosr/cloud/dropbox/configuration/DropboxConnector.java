package pl.edu.agh.iosr.cloud.dropbox.configuration;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;

public interface DropboxConnector {
    DbxRequestConfig getRequestConfig();
    DbxWebAuthNoRedirect getWebAuth();
}

package pl.edu.agh.iosr.configuration.dropbox;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;

public interface DropboxConnector {
    DbxRequestConfig getRequestConfig();
    DbxWebAuthNoRedirect getWebAuth();
}

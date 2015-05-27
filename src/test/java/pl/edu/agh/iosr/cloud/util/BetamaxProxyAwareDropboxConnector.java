package pl.edu.agh.iosr.cloud.util;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.http.StandardHttpRequestor;
import pl.edu.agh.iosr.cloud.common.CloudConfiguration;
import pl.edu.agh.iosr.cloud.dropbox.configuration.DropboxConnector;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Locale;

public class BetamaxProxyAwareDropboxConnector implements DropboxConnector {

    private final CloudConfiguration configuration;
    private final SocketAddress proxyAddress;
    private DbxRequestConfig requestConfig;
    private DbxWebAuthNoRedirect webAuth;

    public BetamaxProxyAwareDropboxConnector(CloudConfiguration configuration, SocketAddress proxyAddress) {
        this.configuration = configuration;
        this.proxyAddress = proxyAddress;
    }

    @Override
    public DbxRequestConfig getRequestConfig() {
        if (requestConfig == null) {
            //TODO: understand clearly wtf is going on + refactor
            StandardHttpRequestor requestor = StandardHttpRequestor.Instance;
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            try {
                final SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new SecureRandom());
                requestor = new StandardHttpRequestor(new Proxy(Proxy.Type.HTTP, proxyAddress)) {
                    @Override
                    protected void configureConnection(HttpsURLConnection conn) throws IOException {
                        super.configureConnection(conn);
                        conn.setSSLSocketFactory(sc.getSocketFactory());
                    }
                };
            } catch (KeyManagementException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            requestConfig = new DbxRequestConfig(configuration.getAppName(), Locale.getDefault().toString(), requestor);
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
        return new DbxAppInfo(configuration.getAppKey(), configuration.getAppKeySecret());
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

public class HttpHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);

    /**
     * Allows using self signed certificates https connections
     * makes all the clients and servers trusted no matter the certificate
     */
    public static void noStrictSSL(){

        LOGGER.warn("Strict SSL is set to false! Invalid certificates will be accepted for connection!");

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier ((hostname, session) -> true);
        } catch (GeneralSecurityException e) {
            LOGGER.error("The configuration for no strict SSL went wrong");
        }
    }

    /**
     * Allows using self signed certificates https connections
     * makes all the clients and servers trusted no matter the certificate
     * Only if the override property strict.ssl is set
     */
    public static void noStrictSSLIfConfigured() {
        if ("false".equalsIgnoreCase(System.getProperty("strict.ssl")))
        {
            noStrictSSL();
        }

    }

}

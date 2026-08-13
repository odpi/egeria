/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

/**
 * Turn off client-side checking of certificates.  There are two options, one to turn it off all the time and the other is
 * controlled through the -Dstrict.ssl=false property.
 */
public class HttpHelper
{

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);

    /**
     * Records whether noStrictSSL() has been called, for callers (such as HTTP client implementations that
     * do not honour HttpsURLConnection's static defaults) that need to independently disable hostname
     * verification.
     */
    private static volatile boolean strictSSL = true;

    /**
     * Reports whether strict SSL checking is still in force, ie whether noStrictSSL() has not been called.
     *
     * @return true if certificates and hostnames are still being validated
     */
    public static boolean isStrictSSL()
    {
        return strictSSL;
    }

    /**
     * Allows the use of self-signed certificates on https connections.
     * The client will trust the server no matter which certificate is sent.
     */
    public static void noStrictSSL(){

        LOGGER.warn("Strict SSL is set to false! Invalid certificates will be accepted for connection!");

        strictSSL = false;

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager()
                {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                    {
                    }
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                    {
                    }
                }
        };

        // Install the all-trusting trust manager
        try
        {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLContext.setDefault(sc);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier ((hostname, session) -> true);
        }
        catch (GeneralSecurityException e)
        {
            LOGGER.error("The configuration for no strict SSL went wrong");
        }
    }


    /**
     * Allows using self-signed certificates https connections.
     * If -Dstrict.ssl=false is set, the client will trust the server no matter the certificate passed.
     */
    public static void noStrictSSLIfConfigured()
    {
        if ("false".equalsIgnoreCase(System.getProperty("strict.ssl")))
        {
            noStrictSSL();
        }
    }
}

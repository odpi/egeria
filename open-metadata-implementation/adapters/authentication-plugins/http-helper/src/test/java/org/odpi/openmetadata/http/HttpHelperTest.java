/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.http;

import org.mockito.Mockito;
import org.testng.annotations.Test;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import static org.testng.Assert.*;

public class HttpHelperTest {

    @Test
    /**
     * Tests hostname verifier before and after running noStrictSSL.
     */
    public void testNoStrictSSLHostnameVerifier() {
        HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
        SSLSession sslSession = Mockito.mock(SSLSession.class);
        String value = "some.hostname";
        assertFalse(HttpsURLConnection.getDefaultHostnameVerifier().verify(value,sslSession));

        HttpHelper.noStrictSSL();

        assertNotEquals(hostnameVerifier,HttpsURLConnection.getDefaultHostnameVerifier());
        assertTrue(HttpsURLConnection.getDefaultHostnameVerifier().verify(value,sslSession));
    }

}

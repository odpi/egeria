#!/usr/local/bin/groovy
import javax.net.ssl.HttpsURLConnection
import java.security.cert.X509Certificate
import java.security.cert.CertificateException

// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the ODPi Egeria project.

// Function to convert array to String

// Will configure an OMAG Server Platform - which should already be running - for FVT testing

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

// Retrieve configuration - with defaults to aid in local testing (using default ports)
// Maven plugin works best with properties, gradle with system properties, so use either
user=(properties["user"] ?: System.properties["user"]) ?: "garygeeke";
baseURL=(properties["baseURL"] ?: System.properties["baseURL"]) ?: "https://localhost:9443";
serverMem=(properties["servermem"] ?: System.properties["servermem"]) ?: "serverinmem";
retries=(properties["retries"] ?: System.properties["retries"]) ?: 50;
delay=(properties["delay"] ?: System.properties["delay"]) ?: 2;

// SSL setup to avoid self-signed errors for testing
def trustAllCerts = [
        new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            }
        }
] as TrustManager[]

try
{
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
}
catch (Exception e)
{
    System.out.println(e);
    System.exit(-1);
}

// Wait until the platform is ready
connected=false;
i=retries;
while (!connected && i>0)
{
    try {

        System.out.println("=== Checking platform at " + baseURL + " is available (" + i + " attempts remaining) ===");
        post0 = new java.net.URL(baseURL + "/open-metadata/platform-services/users/" + user + "/server-platform/origin").openConnection();
        post0RC = post0.getResponseCode();
        println(post0RC);
        if (post0RC.equals(200)) {
            connected = true;
            println(post0.getInputStream().getText());
        } else {
            i--;
            Thread.sleep(1000 * delay);
        }
    } catch (Throwable t)
    {
        // TODO: look at whether some exceptions should be deemed irrecoverable rather than retry
        i--;
        Thread.sleep(1000 * delay);
    }
}

// -- Inmemory

// --- Configure the platform - any errors here and we exit
System.out.println("=== Configuring server: " + serverMem + " ===");
post1 = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + serverMem + "/local-repository/mode/in-memory-repository" ).openConnection()
post1.setRequestMethod("POST")
post1.setRequestProperty("Content-Type", "application/json")
postRC1 = post1.getResponseCode();
println(postRC1);
if(postRC1.equals(200)) {
    println(post1.getInputStream().getText());
}

// --- Enable OMAS - any errors here and we exit
System.out.println("=== Enabling Asset Consumer OMAS: " + serverMem + " ===");
post2 = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + serverMem + "/access-services/asset-consumer/no-topics" ).openConnection()
post2.setRequestMethod("POST")
post2.setRequestProperty("Content-Type", "application/json")
postRC2 = post2.getResponseCode();
println(postRC2);
if(postRC2.equals(200)) {
    println(post2.getInputStream().getText());
}



// --- Enable OMAS - any errors here and we exit
System.out.println("=== Enabling Asset Owner OMAS: " + serverMem + " ===");
post2 = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + serverMem + "/access-services/asset-owner/no-topics" ).openConnection()
post2.setRequestMethod("POST")
post2.setRequestProperty("Content-Type", "application/json")
postRC2 = post2.getResponseCode();
println(postRC2);
if(postRC2.equals(200)) {
    println(post2.getInputStream().getText());
}


// --- Launch the server - any errors here and we exit
System.out.println("=== Starting server: " + serverMem + " ===");
post3 = new URL(baseURL + "/open-metadata/platform-services/users/" + user + "/server-platform/servers/" + serverMem + "/instance" ).openConnection()
post3.setRequestMethod("POST")
post3.setRequestProperty("Content-Type", "application/json")
postRC3 = post3.getResponseCode();
println(postRC3);
if(postRC3.equals(200)) {
    println(post3.getInputStream().getText());
}

// --- We're done
System.out.println("=== Configuration complete ===")

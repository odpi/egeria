#!/usr/local/bin/groovy
import javax.net.ssl.HttpsURLConnection

// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the ODPi Egeria project.

// Function to convert array to String

// Will configure a server chassis - which should already be running - for FVT testing

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate

// Retrieve configuration - with defaults to aid in local testing (using default ports)
// Maven plugin works best with properties, gradle with system properties, so use either
user=(properties["user"] ?: System.properties["user"]) ?: "garygeeke";
baseURL=(properties["baseURL"] ?: System.properties["baseURL"]) ?: "https://localhost:9443";
serverMem=(properties["serverInMemory"] ?: System.properties["serverInMemory"]) ?: "serverinmem";
serverGraph=(properties["serverLocalGraph"] ?: System.properties["serverLocalGraph"]) ?: "servergraph";
maxRetries=Integer.parseInt((properties["retries"] ?: System.properties["retries"]) ?: 12 as String)
delay=Integer.parseInt((properties["delay"] ?: System.properties["delay"]) ?: 10 as String)

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
connected=false
retries=maxRetries
while (!connected && retries>0) {
    try {
        System.out.println("=== Checking platform at " + baseURL + " is available (" + retries + " attempts remaining) ===")
        platformCheckRequest = new URL(baseURL + "/open-metadata/platform-services/users/" + user + "/server-platform/origin").openConnection()
        platformCheckResponse = platformCheckRequest.getResponseCode()
        println(platformCheckResponse)
        if (platformCheckResponse.equals(200)) {
            connected = true
            println(platformCheckRequest.getInputStream().getText())
        } else {
            retries--
            Thread.sleep(1000 * delay)
        }
    } catch (Throwable t) {
        // TODO: look at whether some exceptions should be deemed irrecoverable rather than retry
        retries--
        Thread.sleep(1000 * delay)
    }
}

// -- Inmemory

// --- Configure the platform with in memory server - any errors here and we exit
System.out.println("=== Configuring server: " + serverMem + " ===")
inMemoryRepositoryRequest = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + serverMem + "/local-repository/mode/in-memory-repository" ).openConnection()
inMemoryRepositoryRequest.setRequestMethod("POST")
inMemoryRepositoryRequest.setRequestProperty("Content-Type", "application/json")
inMemoryRepositoryResponse = inMemoryRepositoryRequest.getResponseCode()
println(inMemoryRepositoryResponse)
if(inMemoryRepositoryResponse.equals(200)) {
    println(inMemoryRepositoryRequest.getInputStream().getText())
}

// --- Enable Data Engine OMAS - any errors here and we exit
System.out.println("=== Enabling Data Engine OMAS: " + serverMem + " ===")
addDataEngineToInMemoryServerRequest = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + serverMem + "/access-services/data-engine" ).openConnection()
addDataEngineToInMemoryServerRequest.setRequestMethod("POST")
addDataEngineToInMemoryServerRequest.setRequestProperty("Content-Type", "application/json")
addDataEngineToInMemoryServerResponse = addDataEngineToInMemoryServerRequest.getResponseCode()
println(addDataEngineToInMemoryServerResponse)
if(addDataEngineToInMemoryServerResponse.equals(200)) {
    println(addDataEngineToInMemoryServerRequest.getInputStream().getText())
}


// --- Launch the server - any errors here and we exit
System.out.println("=== Starting server: " + serverMem + " ===")
launchInMemoryServerRequest = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + serverMem + "/instance" ).openConnection()
launchInMemoryServerRequest.setRequestMethod("POST")
launchInMemoryServerRequest.setRequestProperty("Content-Type", "application/json")
launchInMemoryServerResponse = launchInMemoryServerRequest.getResponseCode()
println(launchInMemoryServerResponse)
if(launchInMemoryServerResponse.equals(200)) {
    println(launchInMemoryServerRequest.getInputStream().getText())
}

// -- Graph
// --- Configure the platform - any errors here and we exit
System.out.println("=== Configuring server: " + serverGraph + " ===")
localGraphRepositoryRequest = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + serverGraph + "/local-repository/mode/local-graph-repository" ).openConnection()
localGraphRepositoryRequest.setRequestMethod("POST")
localGraphRepositoryRequest.setRequestProperty("Content-Type", "application/json")
localGraphRepositoryResponse = localGraphRepositoryRequest.getResponseCode()
println(localGraphRepositoryResponse)
if(localGraphRepositoryResponse.equals(200)) {
    println(localGraphRepositoryRequest.getInputStream().getText())
}

// --- Enable Data Engine OMAS - any errors here and we exit
System.out.println("=== Enabling Data Engine OMAS: " + serverGraph + " ===")
addDataEngineToLocalGraphServerRequest = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + serverGraph + "/access-services/data-engine" ).openConnection()
addDataEngineToLocalGraphServerRequest.setRequestMethod("POST")
addDataEngineToLocalGraphServerRequest.setRequestProperty("Content-Type", "application/json")
addDataEngineToLocalGraphServerResponse = addDataEngineToLocalGraphServerRequest.getResponseCode()
println(addDataEngineToLocalGraphServerResponse)
if(addDataEngineToLocalGraphServerResponse.equals(200)) {
    println(addDataEngineToLocalGraphServerRequest.getInputStream().getText())
}


// --- Launch the server - any errors here and we exit
System.out.println("=== Starting server: " + serverGraph + " ===")
launchLocalGraphServerRequest = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + serverGraph + "/instance" ).openConnection()
launchLocalGraphServerRequest.setRequestMethod("POST")
launchLocalGraphServerRequest.setRequestProperty("Content-Type", "application/json")
launchLocalGraphServerResponse = launchLocalGraphServerRequest.getResponseCode()
println(launchLocalGraphServerResponse)
if(launchLocalGraphServerResponse.equals(200)) {
    println(launchLocalGraphServerRequest.getInputStream().getText())
}

// --- We're done
System.out.println("=== Configuration complete ===")

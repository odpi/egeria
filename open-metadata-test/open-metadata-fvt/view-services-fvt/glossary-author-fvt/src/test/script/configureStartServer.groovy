#!/usr/local/bin/groovy
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
//import org.odpi.openmetadata.adminservices.rest.ViewServiceRequestBody

import javax.net.ssl.HttpsURLConnection

// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the ODPi Egeria project.

// Function to convert array to String

// Will configure an OMAG Server Platform - which should already be running - for FVT testing

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.cert.CertificateException
import java.security.cert.X509Certificate

// Retrieve configuration - with defaults to aid in local testing (using default ports)
// Maven plugin works best with properties, gradle with system properties, so use either
user=(properties["user"] ?: System.properties["user"]) ?: "garygeeke";
baseURL=(properties["baseURL"] ?: System.properties["baseURL"]) ?: "https://localhost:9443";
serverMem=(properties["servermem"] ?: System.properties["servermem"]) ?: "serverinmem";
serverView=(properties["serverview"] ?: System.properties["serverview"]) ?: "serverview";
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
            Thread.sleep(1000 * Integer.valueOf(delay));
        }
    } catch (Throwable t)
    {
        // TODO: look at whether some exceptions should be deemed irrecoverable rather than retry
        i--;
        Thread.sleep(1000 * Integer.valueOf(delay));
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

// --- Enable Subject Area OMAS - any errors here and we exit
System.out.println("=== Enabling Subject Area OMAS: " + serverMem + " ===");
post2 = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + serverMem + "/access-services/subject-area" ).openConnection()
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

/*
ViewServiceRequestBody viewServiceRequestBody = new ViewServiceRequestBody()
viewServiceRequestBody.setOMAGServerName(serverMem)
viewServiceRequestBody.setOMAGServerPlatformRootURL(baseURL)
viewServiceRequestBody.setViewServiceOptions(new HashMap<>())
*/


//post.getOutputStream().write(message.getBytes("UTF-8"));
postRC3 = post3.getResponseCode();
println(postRC3);
if(postRC3.equals(200)) {
    println(post3.getInputStream().getText());
}

// -- View Server
// --- Configure the platform - any errors here and we exit
System.out.println("=== Configuring server: " + serverView + " ===");
post1g = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + serverView + "/configuration" ).openConnection()
post1g.setRequestMethod("GET")
post1g.setRequestProperty("Content-Type", "application/json")
postRC1g = post1g.getResponseCode();
println(postRC1g);
if(postRC1g.equals(200)) {
    println(post1g.getInputStream().getText());
}

System.out.println("=== Configuring server: " + serverView + " to set localUrl===");
post11g = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + serverView + "/server-url-root?url=" + baseURL).openConnection()
post11g.setRequestMethod("POST")
post11g.setRequestProperty("Content-Type", "application/json")
postRC11g = post11g.getResponseCode();
//println(postRC11g);
if(postRC11g.equals(200)) {
    println(post11g.getInputStream().getText());
}


//    /open-metadata/admin-services/users/" + user+ "/servers/" + serverName + "audit-log-destinations/default
System.out.println("=== Configuring server: " + serverView + " Add audit ===");
post1g = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + serverView + "/audit-log-destinations/default" ).openConnection()
post1g.setRequestMethod("POST")
post1g.setRequestProperty("Content-Type", "application/json")
postRC1g = post1g.getResponseCode();
println(postRC1g);
if(postRC1g.equals(200)) {
    println(post1g.getInputStream().getText());
}


System.out.println("=== Configuring server: " + serverView + " ===");
post1g = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + serverView + "/configuration" ).openConnection()
post1g.setRequestMethod("GET")
post1g.setRequestProperty("Content-Type", "application/json")
postRC1g = post1g.getResponseCode();
println(postRC1g);
if(postRC1g.equals(200)) {
    println(post1g.getInputStream().getText());
}



// --- Enable Glossary Author service - any errors here and we exit
System.out.println("=== Enabling Glossary Author view Service   : " + serverView + " ===");
//{{baseURL}}/open-metadata/admin-services/users/{{user}}/servers/cocoView1/view-services/glossary-author
post2g = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + serverView + "/view-services/glossary-author" ).openConnection()
post2g.setRequestMethod("POST")
post2g.setRequestProperty("Content-Type", "application/json")
///
//https://localhost:9443/open-metadata/admin-services/users/garygeeke/servers/cocoView1/view-services/glossary-author
post2g.setDoOutput(true)
HashMap<String, String> viewServiceRequestBodyMap = new HashMap<>()
viewServiceRequestBodyMap.put("class","ViewServiceConfig")
viewServiceRequestBodyMap.put("omagserverName",serverMem)
viewServiceRequestBodyMap.put("omagserverPlatformRootURL",baseURL)
//JsonOutput.toJson()
def jopt = JsonOutput.toJson(viewServiceRequestBodyMap)
OutputStreamWriter requestBodyWriter = new OutputStreamWriter(post2g.getOutputStream())
//System.out.println("=== Request JSON is  " + jopt + " ===");
requestBodyWriter.write(jopt)
requestBodyWriter.flush()
System.out.println(post2g.getURL())
//post2g.
postRC2g = post2g.getResponseCode();
println(postRC2g);
if(postRC2g.equals(200)) {
    println(post2g.getInputStream().getText());
} else {
    println("Error " + post2g.getErrorStream().getText())
}



// --- Launch the server - any errors here and we exit
System.out.println("=== Starting server: " + serverView + " ===");
post3g = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + serverView + "/instance" ).openConnection()
post3g.setRequestMethod("POST")
post3g.setRequestProperty("Content-Type", "application/json")
postRC3g = post3.getResponseCode();
println(postRC3g);
if(postRC3g.equals(200)) {
    println(post3g.getInputStream().getText());
}

/*
post1g = new URL(baseURL + "/open-metadata/admin-services/users/" + user + "/servers/" + serverView + "/configuration" ).openConnection()
post1g.setRequestMethod("GET")
post1g.setRequestProperty("Content-Type", "application/json")
postRC1g = post1g.getResponseCode();
println(postRC1g);
if(postRC1g.equals(200)) {
    println(post1g.getInputStream().getText());
}
*/


// --- We're done
System.out.println("=== Configuration complete ===")

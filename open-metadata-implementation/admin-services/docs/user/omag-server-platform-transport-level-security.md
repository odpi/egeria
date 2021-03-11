<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Transport Level Security in Egeria & Certificates

In this document you'll find information on:
 * A background to TLS
 * Using TLS with our server chassis
 * Using TLS with our clients
 * How our clients work

## TLS protocol

Transport Level Security (TLS) protects communications over network connections through Encryption, Authentication & Integrity.

It is one layer of many.

### One-way SSL exchange

This is typical when browsing the web - as a user you are most concerned that the server you are connecting
to is authentic

With this approach, the server is not able to guarantee your authenticity at the transport level. This means you can
be assured of the identity of the server, but it can't be assure who you are. Rather that is typically done further up the protocol stack. 

[Transport Level Security(Wikipedia)](https://en.wikipedia.org/wiki/Transport_Layer_Security)
![SSL exchange](ssl-oneway.svg)

### Two-way (mutual) SSL exchange

In this model trust is established both ways. This is more typical when interconnecting different systems
and applications which are known in advance. 

[Mutual Authentication(Wikipedia)](https://en.wikipedia.org/wiki/Mutual_authentication)
![SSL exchange](ssl-mutual.svg)

### Additional Notes 
####Building diagrams

Note: Rendered image files are checked in, however when updating, the diagrams can be regenerated using ![PlantUML](https://plantuml.com)
 
For example: 
`plantuml -svg ssl-oneway.puml` 

The diagrams are best rendered to svg, however notes do not render with a background if using the IntelliJ markdown plugin.
They do render correctly if opened directly in IntelliJ, as well as in a browser

It's also recommended to install the IntelliJ 'PlantUML' plugin to get a real-time preview whilst updating the diagrams.

## Egeria's Server Chassis

Egeria's server chassis is a Spring boot based application.
We refer to it as the 'platform', which hosts Egeria Servers, but in terms of network communications and SSL,
ia server is usually seen as an application listening on a network port, be aware of this when
reading the links and references mentioned here.

The Egeria Server Chassis, which we refer to as the platform, services requests over a REST based API 
from applications. In this regard it's role in SSL network requests described above is that of a 
network server, with those applications performing that of a network client.

However the Server Chassis also interacts with other systems including other Egeria platforms, Kafka amongst others.
In this regard they are fulfilling a network client role.

As a spring application Egeria's configuration for it's network server role allows the following spring properties
to be set:

* `server.ssl-key-store`                Used by tomcat/spring boot to locate keys that identify the server
* `server.ssl-key-alias`                Used by tomcat/spring boot to identify the alias of the key tomcat should use for itself
* `server.ssl.key-store-password`       Used by tomcat/spring boot for the keystore password (2 way SSL)
* `server.ssl.trust-store`              Used by tomcat/spring boot to understand what clients it can trust (2 way SSL)
* `server.ssl.trust-store-password`     Used by tomcat/spring boot  for the password of the truststore (2 way SSL)

In addition an additional parameter is provided which causes ssl verification to be skipped:

* `strict.ssl`                          true / false : If set to true skips checks on certificate

For further details on these & other less common configuration options, refer to the Spring Docs

Since the Egeria Server Chassis is also a network client the settings in the next section for 
clients are also required.

## Egeria Java Clients

Standard java properties  need to be set within the JVM being used for the Egeria client code:

* `javax.net.ssl.keyStore`                keyStore for client to use (2 way SSL needs this)
* `javax.net.ssl.keyStorePassword`        password for the keystore  (2 way SSL needs this)
* `javax.net.ssl.trustStore`              trustStore for the client to use (always needs setting as egeria makes client calls)
* `javax.net.ssl.trustStorePassword`      password for the truststore (always - as above)

In addition, for any executable jars provided by egeria - such as samples, an additional
parameter will cause ssl verification to be skipped. This is only recommended for test
and development

* `strict.ssl`                            true / false : If set to true skips checks on certificate

Note that in the case of Java Clients, these are system properties, and do
not use spring conventions. 

## Other clients

Similar principles to those documented for java should apply. If you need further assistance please
contact the team on our slack channel at http://slack.lfai.foundation . A Pull Request (or issue) with contributed documentation
is very welcome !

## Example script to launch Egeria

Example certs are provided [here](../../../../open-metadata-resources/open-metadata-deployment/certificates) 

As an example of running the Egeria server chassis with the certificates generated above, add
the following options when launching the Egeria server chass jar file:

 * -Dserver.ssl.key-store=${KS} 
 * -Dserver.ssl.key-alias=EgeriaServerChassis
 * -Dserver.ssl.key-store-password=egeria
 * -Dserver.ssl.trust-store=EgeriaCA.p12
 * -Dserver.ssl.trust-store-password=egeria
 * -Djavax.net.ssl.keyStore=EgeriaServerChassis 
 * -Djavax.net.ssl.keyStorePassword=egeria
 * -Djavax.net.ssl.trustStore=EgeriaCA.p12
 * -Djavax.net.ssl.trustStorePassword=egeria 
  
We have to use both server.ssl and javax.net values since the former controls how the server chassis works when accepting inbound connections
as the server chassis, and the latter are needed any time code running in that chassis acts as a client, such as connecting to another 
repository.

We have assumed the default keystore passwords, and also that we will use the same key regardless of whether it is the one
that the chassis sends back to it's client after they connect, or the one the chassis may send to those other repositories. They
could be distinct if needed.

## Creating your own certificates

Example configurations and scripts can be found in [open-metadata-resources/open-metadata-deployment/certificates](../../../../open-metadata-resources/open-metadata-deployment/certificates)

An example script (MacOS/Linux)to create certificates is provided in `gensamplecerts.sh`. It is intended only as an example.
It requires the openssl tool & keytool. Deployment frameworks in cloud services may also offer support to
generate certificates and it's likely an enterprise process will be place in larger organizations.

The script creates a Certificate Authority and then specific certificates for different Egeria components.
It could be extended to create certificates for other clients especially if using 2 way SSL.

When the script is run it also makes use of the configuration template `openssl.cnf.`

Together both set some important characteristics that are needed to allow the certificate to work properly
especially with current browsers
 - ensuring basicConstraints are specified
 - ensuring the certificate expiry time is not too far in the future
 - ensuring subjectAltName is specified.


----
* Return to [Configuring an OMAG Server](configuring-the-omag-server-platform.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

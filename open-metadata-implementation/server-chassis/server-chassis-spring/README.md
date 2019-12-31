<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Server Chassis for Spring

The server chassis provides the base server framework to host the Open Metadata
and Governance (OMAG) Servers in the
[OMAG Server Platform](https://egeria.odpi.org/open-metadata-publication/website/omag-server).  

The **server-chassis-spring** module provides the implementation of the server chassis.
Its **main()** method is located in a Java class called
**OMAGServerPlatform** that uses [Spring Boot](https://spring.io/projects/spring-boot)
for the server framework.

When the **OMAGServerPlatform** is started, Spring Boot does a component scan for all Spring
services that are in Java packages stemming from `org.odpi.openmetadata.*`
and that are visible to this module.

To make a new Java package visible to **OMAGServerPlatform**, add its **spring** package
to the **pom.xml** file for **server-chassis-spring**.

For example, this is the snippet of XML in the pom.xml file that adds the
[Asset Owner OMAS](https://egeria.odpi.org/open-metadata-implementation/access-services/asset-owner) services
to the OMAG server platform.

```xml
<dependency>
    <groupId>org.odpi.egeria</groupId>
    <artifactId>asset-owner-spring</artifactId>
</dependency>
```

When the OMAG server platform is first started, the REST APIs
are defined for its endpoint.
However, only the [Administration Services](../../admin-services) 
and [PlatformServices](../../platform-services) are activated at this point.
The other Open Metadata and Governance (OMAG) services will each give an error response if called.

The OMAG Services are selectively activated in one or more
[OMAG Servers](../../../open-metadata-publication/website/omag-server/omag-server.md).
The administration service define the services for an OMAG Server and 
stores them in a configuration document.  This is a one-time activity.

Once the configuration document is in place it is possible to activate and deactivate the
server in the OMAGServerPlatform many times over multiple platform restarts.

Servers an be automatically activated at startup 
by setting spring-boot property `startup.server.list`, typically in the `application.properties` file.
The server names are listed without quotes.
For example:
```
startup.server.list=cocoMDS1, cocoMDS2
```
Each server is started with the administration user id set in the spring-boot property `startup.user`.

For example:
```
startup.user=garygeeke
```
By default, this user id is set to the user id `system`.

When the platform shuts down, if any of the servers that were in the startup list are still running,
they will be shut down before the server completes.

If `startup.server.list` is null then no servers are automatically started or stopped.
```
startup.server.list=
```

Details of how to set up the configuration document, and activate/deactivate
the open metadata services can be found in [admin-services](https://egeria.odpi.org/open-metadata-implementation/admin-services/Using-the-Admin-Services.md).

Swagger API documentation is generated with the chassis and is documented in [Swagger Generation](SwaggerGeneration.md).



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
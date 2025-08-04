<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Spring-based Platform Chassis

The [Platform Chassis](..)
provides the base server framework to host the Open Metadata
and Governance (OMAG) Servers in the
[OMAG Server Platform](https://egeria-project.org/concepts/omag-server).  

The **platform-chassis-spring** module is an implementation of the
platform chassis written using [Spring Boot](../../../developer-resources/Spring.md).
Its **main()** method is located in a Java class called **OMAGServerPlatform**.

When the OMAGServerPlatform is first started, all of its REST APIs
are visible and callable by an external client.
However, only the [Administration Services](../../admin-services) 
and [Platform Services](../../platform-services) are operational at this point.
The other Open Metadata and Governance (OMAG) services will each give an error response if called.

The [OMAG Subsystems](https://egeria-project.org/concepts/omag-subsystem)
supporting the other OMAG services are selectively activated in one or more
[OMAG Servers](https://egeria-project.org/concepts/omag-server) that run
in the OMAGServerPlatform.
You can use the administration service to define and configure the subsystems for an OMAG Server.
This definition is then stored in a configuration document for the server.  This is a one-time activity.

Details of how to set up the configuration document, and activate/deactivate
the open metadata services can be found in the 
[OMAG Server Configuration User Guide](https://egeria-project.org/guides/admin).

Once the configuration document is in place it is possible to activate and deactivate the
server in the OMAGServerPlatform many times over multiple platform restarts.

A detailed description of the internals of the OMAGServerPlatform during server start up
is available [here](https://egeria-project.org/concepts/omag-server-platform).

## Maven build profiles
Default maven build will include **full-platform** profile, with all access service 
and view server functionality. Please be aware this behavior might be subject of a future change.
Default behavior might be change with **-DadminChassisOnly** option which will disable **full-platform** profile.

By running 
```
mvn clean install -DadminChassisOnly
```
 the platform-chassis-spring will contain only the following services:
 * Administration Services - Operational
 * Administration Services - Platform Configuration
 * Administration Services - Server Configuration
 * Server Operations
 * Platform Services
 
 In this case, for any extra functionality, such as desired access services or view server, 
 use [ **loader.path** spring-boot functionality ](https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-executable-jar-format.html#executable-jar-property-launcher-features).
 
 
## Application properties

Since this is a Spring Boot application, OMAGServerPlatform can be
[customized using the application.properties](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)
file found in the `resources` directory and using environment variables.

For example, OMAG servers can be automatically activated at startup 
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

## Adding a new subsystem to the OMAGServerPlatform

When the **OMAGServerPlatform** class is called, Spring Boot does a component scan for all Spring
services that are in Java packages stemming from `org.odpi.openmetadata.*`
and that are visible to this module.

To make a new Java package visible to **OMAGServerPlatform**, add its **spring** package
to the **pom.xml** file for **platform-chassis-spring** and it will be picked up in the component scan.

For example, this is the snippet of XML in the pom.xml file that adds the
[Asset Owner OMAS](https://egeria-project.org/services/omas/asset-owner/overview/) services
to the OMAG server platform.

```xml
<dependency>
    <groupId>org.odpi.egeria</groupId>
    <artifactId>asset-owner-spring</artifactId>
</dependency>
```

## Swagger

Swagger API documentation is generated with the chassis and is documented in [Swagger Generation](SwaggerGeneration.md).

## Spring Boot Actuator

Spring Boot Actuator is used to expose operational information about the running application such as health, metrics, info, dump. 
It uses HTTP endpoints or JMX beans to enable us to interact with it. 

A “discovery page” is added with links to all the endpoints. The “discovery page” is available on /actuator by default.
Once this dependency is on the class-path /info and /health endpoints are available out of the box. 

In order to expose all endpoints over HTTP, use the following property:
```
management.endpoints.web.exposure.include=*
```

The `exclude` property lists the IDs of the endpoints that should not be exposed.
For example, to expose everything over HTTP except the env and beans endpoints, use the following properties:
```
management.endpoints.web.exposure.include=*
management.endpoints.web.exposure.exclude=env,beans
```
The `exclude` property takes precedence over the `include` property.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
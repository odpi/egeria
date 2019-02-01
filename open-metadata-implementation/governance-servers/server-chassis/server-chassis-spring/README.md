<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Server Chassis for Spring

The server chassis provides the base server framework to host the open metadata
services in the [OMAG Server Platform](../../../../open-metadata-publication/website/omag-server).  

The `server-chassis-spring` module provides the implementation of the server chassis.
Its `main()` method is located in a Java class called
`OMAGServerPlatform` that uses [Spring Boot](https://spring.io/projects/spring-boot)
for the server framework.

When the `OMAGServerPlatform` is started, Spring Boot does a component scan for all Spring
services that are in Java packages stemming from `org.odpi.openmetadata/*`
and that are visible to this module.

To make a new Java package visible to `OMAGServerPlatform`, add its `spring` package
to the `pom.xml` file for `server-chassis-spring`.


For example, this is the snippet on XML in the pom.xml file that adds the
[Connected Asset OMAS](../../../access-services/connected-asset) services
to the OMAG server platform.

```xml

        <dependency>
            <groupId>org.odpi.egeria</groupId>
            <artifactId>connected-asset-spring</artifactId>
            <version>${open-metadata.version}</version>
        </dependency>

```

When the OMAG server platform is first started, the REST APIs
are defined for its endpoint.
However, only the [Administration Services](../../admin-services) are activated at this point.
The other services will each give an error response if called.

To activate these services, it is necessary to use the
admin services
to set up a configuration document for the server that defines
how the services should be configured.

Setting up the configuration document is a one-time activity.
Once it is in place it is possible to activate and deactivate the
services in the OMAGServerPlatform many times over multiple server restarts.

Details of how to set up the configuration document, and activate/deactivate
the open metadata services can be found in [admin-services](../../admin-services/README.md).

Swagger API documentation is generated with the chassis and is documented in [SwaggerGeneration.md](./SwaggerGeneration.md).



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Server Chassis for Spring

The server chassis provides an "empty" server platform to host the open metadata
services in the [OMAG Server Platform](../../../../open-metadata-publication/website/omag-server).  

The `server-chassis-spring` provides the server chassis called
`OMAGApplication` that uses Spring Boot.

When the `OMAGApplication` is started, it does a component scan for all spring
services that are in package `org.odpi.openmetadata/*` and that are visible
to this module.

To make a new package visible to `OMAGApplication`, add its spring package
to the `pom.xml` file for this package.  For example, this is the
snippet that adds the
[Connected Asset OMAS](../../../access-services/connected-asset) services to `OMAGApplication`:

```xml
        <dependency>
            <groupId>org.odpi.egeria</groupId>
            <artifactId>connected-asset-spring</artifactId>
            <version>${open-metadata.version}</version>
        </dependency>

```

When the OMAG Server Platform is first started, the 
None of the services are activated at this point and will each give an
error response if called.

To activate these services, it is necessary to use the
admin services
to set up a configuration document for the server that defines
how the services should be configured.

Setting up the configuration document is a one-time activity.
Once it is in place it is possible to activate and deactivate the
services in the OMAGApplication many times over multiple server restarts.

Details of how to set up the configuration document, and activate/deactivate
the open metadata services can be found in [admin-services](../../admin-services/README.md).

Swagger API documentation is generated with the chassis and is documented in [SwaggerGeneration.md](./SwaggerGeneration.md).



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
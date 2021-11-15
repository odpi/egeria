<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 3.4 (December 2021)

Release 3.4 adds:
  * OMAG Server Platform Chassis for Spring & Connector Configuration Factory are built without Egeria graph repository


Details of these and other changes are in the sections that follow.

## OMAG Server Platform Chassis & Connector Configuration Factory change

[Graph repository connector](https://github.com/odpi/egeria/open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/graph-repository-connector/README.md)
it is not dependency of [Connector configuration factory](https://github.com/odpi/egeria/open-metadata-implementation/adapters/open-connectors/connector-configuration-factory/README.md) 
anymore therefore will not be included by the build into [ OMAG Server Platform Chassis ](ohttps://github.com/odpi/egeria/open-metadata-implementation/server-chassis/server-chassis-spring/README.md).
Jar with dependencies of [Graph repository connector](https://github.com/odpi/egeria/open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/graph-repository-connector/README.md) 
it is now part of the build (maven and gradle) and to run a platform that have server(s) with Egeria graph repository,
the connector jar have to be placed into classpath. Easy way is to use Spring boot loader.path functionality. Place the 
jar in the 'lib' folder next to server-chassis-spring jar and run it with -Dloader.path=lib

## Deprecations & Removals

None.

## Known Issues

# Further Help and Support

See the [Community Guide](../Community-Guide.md).

----
* Return to [Release Notes](.)
   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

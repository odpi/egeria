<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
 
# OMAG Server Chassis

The server chassis is the component that provides the base
server infrastructure for the
[OMAG Server Platform](../../../open-metadata-publication/website/omag-server).

It is written using [Spring Boot](https://spring.io/projects/spring-boot).
and manages the REST APIs' endpoint.  Calls to the server chassis
are then routed to the appropriate Egeria module.

Since this is a Spring Boot application, it can be
[customized using the application.properties](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)
file found in the `resources` directory and using environment variables.

The server chassis code is here:
* **[server-chassis-spring](server-chassis-spring)** - server chassis using Spring.




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
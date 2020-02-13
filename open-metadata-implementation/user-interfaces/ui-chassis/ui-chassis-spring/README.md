<!-- SPDX-License-Identifier: Apache-2.0 -->

The ui chassis provides the base server framework to host the open metadata
services in the UI Server Platform.  

The **ui-chassis-spring** module provides the implementation of the ui chassis.
Its **main()** method is located in a Java class called
**UIServerPlatform** that uses [Spring Boot](https://spring.io/projects/spring-boot)
for the server framework.

When the **UIServerPlatform** is started, Spring Boot does a component scan for all Spring
services that are in Java packages stemming from `org.odpi.openmetadata.*`
and that are visible to this module.

To make a new Java package visible to **UIServerPlatform**, add its **spring** package
to the **pom.xml** file for **ui-chassis-spring**.

When the UI server platform is first started, the REST APIs
are defined for its endpoint.
However, only the [Administration Services](https://egeria.odpi.org/open-metadata-implementation/user-interfaces/ui-admin-services) are activated at this point.
The other services will each give an error response if called.

To activate these services, it is necessary to use the
admin services
to set up a configuration document for the server that defines
how the services should be configured.

Setting up the configuration document is a one-time activity.
Once it is in place it is possible to activate and deactivate the
services in the UIServerPlatform many times over multiple server restarts. Also, it can be activate at startup 
by setting spring-boot property  `startup.server.list`. This startup option is made by 
default by user id `system`, and it can be override by setting spring-boot property `startup.user`

Details of how to set up the configuration document, and activate/deactivate
the open metadata services can be found in [admin-services](https://egeria.odpi.org/open-metadata-implementation/admin-services/Using-the-Admin-Services.md).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
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

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
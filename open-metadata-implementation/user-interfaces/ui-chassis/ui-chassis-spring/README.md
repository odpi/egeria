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

The UIServerPlatform allows for customized logging for http requests. At the moment, request logging is only available for the login endpoints. 
You can configure the logging mechanism to log any request header, username or the session ID.

For example adding `%X{username}` in the logging pattern will print the username of the user that loges in.

The available fields to be printed, along with the headers form the request are the following

* username  
* sessionId 
* remoteAddress 
* remoteHost 


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
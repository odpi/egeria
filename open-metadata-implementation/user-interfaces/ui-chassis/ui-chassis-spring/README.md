<!-- SPDX-License-Identifier: Apache-2.0 -->

# UI Chassis

This is a spring boot version of the ui chassis provides the base server framework to host the open metadata view
services in the [UI Server Platform](https://egeria.odpi.org/open-metadata-publication/website/ui-server).  

It is written using [Spring Boot](https://spring.io/projects/spring-boot).
and manages the REST APIs' endpoint.  Calls to the ui chassis result in rest calls
to appropriate Open Metadata or Goverance Services.

## UI Platform

The **ui-chassis-spring** module provides the implementation of the ui chassis.
Its **main()** method is located in a Java class called
**UIServerPlatform** that uses [Spring Boot](https://spring.io/projects/spring-boot)
for the server framework.

When the **UIServerPlatform** is started, Spring Boot does a component scan for all Spring
services that are in Java packages stemming from `org.odpi.openmetadata.*`
and that are visible to this module.

To make a new Java package visible to **UIServerPlatform**, add its **spring** package
to the **pom.xml** file for **ui-chassis-spring**.

## UI Platform configuration

When the UI server platform is first started, the REST APIs
are defined for its endpoint.
However, only the [Administration Services](https://egeria.odpi.org/open-metadata-implementation/user-interfaces/ui-admin-services) are activated at this point.
The other services will each give an error response if called.

To activate these services, it is necessary to use the
[ui admin services](../../ui-admin-services)
to set up a configuration document for the server that defines
how the services should be configured.

Setting up the configuration document is a one-time activity.
Once it is in place it is possible to activate and deactivate the
services in the UIServerPlatform many times over multiple server restarts. Also, it can be activate at startup 
by setting spring-boot property  `startup.server.list`. This startup option is made by 
default by user id `system`, and it can be override by setting spring-boot property `startup.user`

Details of how to set up the configuration document, and activate/deactivate
the open metadata services can be found in [ui-admin-services](https://egeria.odpi.org/open-metadata-implementation/admin-services/Using-the-Admin-Services.md).

## Environment variables used by the UI
 
Since this is a Spring Boot application, environment variables can be
[customized using the application.properties](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)
file found in the `resources` directory and using environment variables. A sample application.properties file is provided, h2 can be used as is for demoing capabilities in 
a development environment. 


* strict.ssl  - set to false if you want to ignore invalid ssl certificate of a server you want to connect to
* startup.user - userId used to startup the list of configured servers. Default is 'system'
* startup.server.list - CSV of servers to be started
* logging.level.root  - logging level
* authentication.source - user store to use to authenticate with db or ldap
* authentication.mode - authentication method either session or token

If using h2
* spring.h2.console.enabled - set to true to enable h2
* spring.h2.console.path - h2 path 
* spring.datasource.url - h2 url
* spring.datasource.username - h2 username
* spring.datasource.password - h2 password
* spring.datasource.driver-class-name - use org.h2.Driver
* spring.jpa.show-sql - use false
* spring.jpa.properties.hibernate.format_sql - use false

If using ldap
* ldap.user.search.base - ldap user search base
* ldap.user.search.filter - ldap filter
* ldap.group.search.base - ldap group search base
* ldap.group.search.filter - filter
* ldap.url - url

#Authentication source (possible values: db,ldap)
authentication.source=db
authentication.mode=token


## Swagger
Swagger API documentation is generated with the chassis and is documented in [Swagger Generation](../../../server-chassis/server-chassis-spring/SwaggerGeneration.md).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
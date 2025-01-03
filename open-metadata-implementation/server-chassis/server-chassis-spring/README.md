<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project.  -->

# OMAG Server Chassis Spring

![egeria-content-status-tech-preview.png](..%2F..%2F..%2Fimages%2Fegeria-content-status-tech-preview.png)

This module provides spring-boot based application that is able to launch single pre-configured OMAG server instance.

### Building the module with Gradle

To build the boot application jar from the current module use:

`./gradlew clean build`

### Starting the application locally

You can run the application locally in the current module with java using following command:

```bash
java -jar build/libs/server-chassis-spring-*-SNAPSHOT.jar --omag.server-config-file=classpath:samples/metadata-repository-server.yml --server.port=9080 --server.ssl.enabled=false
```

```
 Project Egeria - Open Metadata and Governance
    ____   __  ___ ___    ______   _____
   / __ \ /  |/  //   |  / ____/  / ___/ ___   ____ _   __ ___   ____
  / / / // /|_/ // /| | / / __    \__ \ / _ \ / __/| | / // _ \ / __/
 / /_/ // /  / // ___ |/ /_/ /   ___/ //  __// /   | |/ //  __// /
 \____//_/  /_//_/  |_|\____/   /____/ \___//_/    |___/ \___//_/

 :: Powered by Spring Boot (v3.1.1) ::

2023-09-07T10:08:05.779+02:00  INFO 4334 --- [           main] o.o.o.s.springboot.OMAGServer            : Starting OMAGServer using Java 17.0.8 with PID 4334 (/Developer/egeria/open-metadata-implementation/server-chassis/server-chassis-spring/build/libs/server-chassis-spring-5.3-SNAPSHOT.jar started by DEVELOPER in /Developer/egeria/open-metadata-implementation/server-chassis/server-chassis-spring)
2023-09-07T10:08:05.781+02:00  INFO 4334 --- [           main] o.o.o.s.springboot.OMAGServer            : No active profile set, falling back to 1 default profile: "default"
2023-09-07T10:08:07.435+02:00  INFO 4334 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 9080 (http)
2023-09-07T10:08:07.444+02:00  INFO 4334 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2023-09-07T10:08:07.444+02:00  INFO 4334 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.10]
2023-09-07T10:08:07.505+02:00  INFO 4334 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2023-09-07T10:08:07.506+02:00  INFO 4334 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1681 ms
2023-09-07T10:08:08.228+02:00  INFO 4334 --- [           main] EnvironmentConfiguration$$SpringCGLIB$$0 : SSL configuration started working directory: /Developer/egeria/open-metadata-implementation/server-chassis/server-chassis-spring
2023-09-07T10:08:08.685+02:00  INFO 4334 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 2 endpoint(s) beneath base path '/actuator'
2023-09-07T10:08:08.749+02:00  INFO 4334 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 9080 (http) with context path ''
2023-09-07T10:08:08.763+02:00  INFO 4334 --- [           main] o.o.o.s.springboot.OMAGServer            : Started OMAGServer in 3.384 seconds (process running for 3.851)
2023-09-07T10:08:08.786+02:00  INFO 4334 --- [           main] o.o.o.s.s.config.OMAGConfigHelper        : Using configuration from class path resource [samples/metadata-repository-server.yml]
2023-09-07T10:08:09.024+02:00  INFO 4334 --- [           main] o.o.o.s.springboot.OMAGServer            : Sending activation request for server: cocoMDS1 and user: OMAGServer
2023-09-07T10:08:09.208+02:00  INFO 4334 --- [           main] o.o.o.s.springboot.OMAGServer            : Activation succeeded for server: cocoMDS1

```

The command will run the application using parameters provided. The OMAG server instance is created and activated using the configuration file supplied via application property `omag.server-config-file`. 
<br/>To demonstrate basic functionality, we turn ssl off `server.ssl.enabled=false` and run the application on http port 9080 `server.port=9080`.

### Quick-start configuration properties

| Property name           | Environment variable  | Description                                                                                                                                                                                                                                                                                                                                              |
|-------------------------|-----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| omag.server-config-file | OMAG_SERVERCONFIGFILE | [REQUIRED] The [OMAGServerConfig document](https://egeria-project.org/concepts/configuration-document/) file location. <br/>Note the value should be defined as spring Resource i.e. starting with `classpath:` or `file:` <br/> Both JSON and YAML files are supported. See [samples](src%2Fmain%2Fresources%2Fsamples) for sample configuration files. |
| server.port             | SERVER_PORT           | Configures port used by the embedded Tomcat server.                                                                                                                                                                                                                                                                                                      |
| server.ssl.enabled      | SERVER_SSL_ENABLED    | Configures if SSL should be enabled for the embedded Tomcat server.                                                                                                                                                                                                                                                                                      |

Application can be further customized by setting supported spring boot and application specific properties. 
<br/>The default configuration that is already packaged within the JAR distribution is  [application.properties](src%2Fmain%2Fresources%2Fapplication.properties).
<br/>Following Spring application [external configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config) feature, all the properties can be customized at deploy/run time as shown in the quick-start example above.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
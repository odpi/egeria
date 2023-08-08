<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMAG Server Chassis Spring

![egeria-content-status-in-development.png](..%2F..%2F..%2Fimages%2Fegeria-content-status-in-development.png)

This module provides spring-boot based application that is able to launch single pre-configured OMAG server instance.

### Building the module with Gradle

To build the boot application jar from the current module use:

`./gradlew clean build`

### Starting the application locally

You can run the application locally from this module with java using following command:

```bash
java -jar build/libs/server-chassis-spring-*-SNAPSHOT.jar --omag.server-config=classpath:samples/metadata-repository-server.json --server.port=9080 --server.ssl.enabled=false
```

The command will run the application using provided parameters. For demo purpose we turn ssl off and run the application on http port 9080.

### Configuration properties

| Property name      | Environment variable |     | Description                                                                                                                                                                                                         |
|--------------------|----------------------|:----|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| omag.server-config | OMAG_SERVER-CONFIG   |     | The [OMAGServerConfig document](https://egeria-project.org/concepts/configuration-document/) json file location **(Required)**. Note the value should be spring Resource i.e. starting with `classpath:` or `file:` |
| server.port        | SERVER_PORT          |     | Configures port used by the embedded Tomcat server                                                                                                                                                                  |
| server.ssl.enabled | SERVER_SSL_ENABLED   |     | Configures if SSL should be enabled for the embedded Tomcat server                                                                                                                                                  |
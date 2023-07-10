<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMAG Server Chassis Spring

### This module is IN DEVELOPMENT
[More about content status](https://egeria-project.org/release-notes/content-status/)

This module is providing OMAG server chassis spring-boot based application that is able to launch single pre-configured OMAG server instance.

### Building the module with Gradle

To build current module from the project home folder execute following Gradle command:

`./gradlew clean :open-metadata-implementation:server-chassis:server-chassis-spring:build`

### Starting the application

To start the OMAG Server application manually using java from the project home, execute following bash command:

```bash
# Go to project home folder'
cd ../../../
# Execute java using -jar parameter starting the bootJar package and --omag.server.config setting the location of the OMAG server configuration file 
java -jar open-metadata-implementation/server-chassis/server-chassis-spring/build/libs/server-chassis-spring-*-SNAPSHOT.jar --omag.server.config=file:open-metadata-implementation/server-chassis/server-chassis-spring/src/main/resources/metadata-repository-server.json
```
Alternately, for development purpose in IDE such as IntelliJ you can use default Spring Boot run configuration. 

### Application Properties

`omag.server.config` - The OMAG server configuration JSON file location. Notice the 'file:' prefix. With this, spring-boot loads the resource form a file on a given path on the filesystem. 

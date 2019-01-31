<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Starting the OMAG Server

The [OMAG Server's installation directory](../building-egeria-tutorial/task-installing-egeria.md)
contains a Java Archive (Jar) file for the server and a directory of resources.

```text
$ ls
resources				server-chassis-spring-0.3-SNAPSHOT.jar
```
The name of the Java Archive (Jar) file will
depend on the release of ODPi Egeria that you have installed.  
In this example, the release is `0.3-SNAPSHOT`.

The OMAG server is started with the `java` command.
Ensure you have a Java runtime at Version 8 (Update 151) or above installed on your machine.
Check the version of Java you have with the command `java -version`
(You can download Java from [AdoptOpenJDK](https://adoptopenjdk.net/) and select `OpenJDK 8 (LTS)`
and the `HotSpot` version for your operating system.
You only need the JRE but select the JDK if you expect to also write some Java code.)

Start the OMAG Server as follows 

```text
$ java -jar server*
```
The server first displays this banner and then initializes itself.

```text
ODPi Egeria
        ____     __  ___    ___    ______          _____
       / __ \   /  |/  /   /   |  / ____/         / ___/  ___    _____ _   __  ___    _____
      / / / /  / /|_/ /   / /| | / / __           \__ \  / _ \  / ___/| | / / / _ \  / ___/
     / /_/ /  / /  / /   / ___ |/ /_/ /          ___/ / /  __/ / /    | |/ / /  __/ / /
     \____/  /_/  /_/   /_/  |_|\____/          /____/  \___/ /_/     |___/  \___/ /_/

 :: Powered by Spring Boot (v2.1.2.RELEASE) ::


```
When the initialization is complete, you will see this message

```text
Thu Jan 31 13:15:26 GMT 2019 OMAG Server ready for configuration
```

The server at this point is an empty shell (we call it the server chassis because it
provides the platform to run the open metadata and governance services).

Services are initialized by passing it a configuration document.

Understanding how to create a configuration document
[is the next task in this tutorial](task-creating-configuration-documents.md).



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
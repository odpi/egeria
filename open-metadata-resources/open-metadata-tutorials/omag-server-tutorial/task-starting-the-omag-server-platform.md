<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Starting the OMAG Server Platform

The [OMAG Server Platform's installation directory](../building-egeria-tutorial/task-installing-egeria.md)
contains a Java Archive (Jar) file for the server platform itself along with a directory of resources.

```bash
$ ls
resources				server-chassis-spring-2.10-SNAPSHOT.jar
```

The name of the Java Archive (Jar) file will
depend on the release of ODPi Egeria that you have installed.  In this example, the release is **2.10-SNAPSHOT**.

The [OMAG Server Platform](../../../open-metadata-publication/website/omag-server) is started with the **java** command.
Ensure you have a Java runtime at Version 8 (Update 151) or above installed on your machine.
Check the version of Java you have with the command **java -version**
(You can download Java from [AdoptOpenJDK](https://adoptopenjdk.net/) and select **OpenJDK 8 (LTS)**
and the **HotSpot** version for your operating system.
You only need the JRE but select the JDK if you expect to also write some Java code.)

Start the OMAG server platform as follows - the `-Dserver.port` option is needed if you want multiple instances running
on different ports, as required by the tutorials.

You should be in the main distribution directory as setup when installing the server. 

```bash
$ java -Dserver.port=9443 -jar server/server-chassis-spring-2.10-SNAPSHOT.jar
```

The OMAG server platform first displays this banner and then initializes itself.

```text
 ODPi Egeria
    ____   __  ___ ___    ______   _____                                 ____   _         _     ___
   / __ \ /  |/  //   |  / ____/  / ___/ ___   ____ _   __ ___   ____   / _  \ / / __    / /  / _ /__   ____ _  _
  / / / // /|_/ // /| | / / __    \__ \ / _ \ / __/| | / // _ \ / __/  / /_/ // //   |  / _\ / /_ /  | /  _// || |
 / /_/ // /  / // ___ |/ /_/ /   ___/ //  __// /   | |/ //  __// /    /  __ // // /  \ / /_ /  _// / // /  / / / /
 \____//_/  /_//_/  |_|\____/   /____/ \___//_/    |___/ \___//_/    /_/    /_/ \__/\//___//_/   \__//_/  /_/ /_/

 :: Powered by Spring Boot (v2.1.2.RELEASE) :: 
```

When the initialization is complete, you will see this message:

```text
timestamp OMAG server platform ready for more configuration
```

This means your OMAG server platform is running. 

If you get an error that the port is in use, check for any applications using the same port. 

Try the following command (replace 9443 accordingly if using a non standard port):

```bash
$ curl --insecure -X GET https://localhost:9443/open-metadata/platform-services/users/test/server-platform/origin
Egeria OMAG Server Platform
```

This calls the OMAG server platform using a REST API call.  The response **Egeria OMAG Server Platform**
means the curl command communicated with a running OMAG server platform instance.

The OMAG server platform has many REST APIs.  Enter **https://localhost:9443/swagger-ui.html** into your browser to see the list of
available REST APIs.

Broadly speaking, the OMAG server platform supports
* Administration services and
* Open metadata and governance services.

The administration services (the ones beginning **config** and **operational**)
are available all of the time the OMAG server platform is running.

The open metadata and governance services are routed to the OMAG Servers running
on the OMAG server platform.

OMAG servers are started on the server platform
using a **configuration document**.  This configuration document is both configured
and activated in the OMAG server platform using the administration services.

Understanding how to create a configuration document using the administration services
[is the next task in this tutorial](task-creating-configuration-documents.md).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

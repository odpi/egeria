<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Changing the OMAG Server Platform's Network Address

By default the [OMAG Server Platform](../../../open-metadata-publication/website/omag-server) registers with the network
using **https://localhost:9443**.  This is ok for testing, or
where you only want to run one instance of the OMAG Server Platform
on a single machine, but for many situations it is not sufficient.

This tutorial covers changing the network address.

The [OMAG Server Platform's installation directory](../building-egeria-tutorial/task-installing-egeria.md)
there is the Java Archive (Jar) file for the OMAG server platform
and a **resources** directory.

```bash
$ ls
resources				server-chassis-spring-2.10-SNAPSHOT.jar
```

Change to the resources directory and you will see the **banner.txt**
file that is displayed when the OMAG server platform starts up along
with the **application.properties** file.

```bash
$ cd resources
$ ls
application.properties	banner.txt
```

The **application.properties** file provides a means to add configuration
to the [Spring Boot Server Chassis](../../../open-metadata-implementation/server-chassis)
that acts as a base for the OMAG Server Platform.

```bash
$ cat application.properties
# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the ODPi Egeria project.

strict.ssl=true

################################################
### Logging
################################################
logging.level.root=OFF
```

Edit the **application.properties** file and add **server.address=https://localhost:9444**
to the file:

```text
# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the ODPi Egeria project.

strict.ssl=true

################################################
### Logging
################################################
logging.level.root=OFF

server.address=https://localhost:9444
```

[Start the OMAG server platform again](task-starting-omag-server.md)
and issue the following REST call to check the server is running with the new server address.

```bash
$ curl --insecure -X GET https://localhost:9444/open-metadata/platform-services/users/test/server-platform/origin
Egeria OMAG Server Platform
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

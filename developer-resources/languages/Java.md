<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Java

Java is a strongly-typed, compiled language.  The resulting object code runs in
a virtual machine called the **Java Virtual Machine** or **JVM**).

The JVM is supported on most operating systems and so Java programs can run
with the same behavior on almost any machine.
This portability of code is why Java is used
for the Egeria runtime - 
the [OMAG Server Platform](../../open-metadata-implementation/admin-services/docs/concepts/omag-server-platform.md) -
and the clients.

If you want to run Egeria you need to install the **Java Runtime Environment (JRE)**.
To build and test Egeria, you need the **Java Development Kit (JDK) installed**.
The JDK also contains the runtime environment (JRE).

There are various JDKs available, and you may even have one pre-installed on your system. You can check
by running the command `java -version` from the command-line.

Egeria can run with version 8 or version 11.  Any Java code you create must
build and run with both versions.

Java can be installed by:

1. Downloading the **OpenJDK 8 (LTS) HotSpot** JVM from [AdoptOpenJDK](https://adoptopenjdk.net).
1. Running the installer that is downloaded.

----
* Return to [Programming Languages](.)
* Link to [Building Egeria Tutorial](../../open-metadata-resources/open-metadata-tutorials/building-egeria-tutorial)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
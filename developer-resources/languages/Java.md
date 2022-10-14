<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Java

Java is a strongly-typed, compiled language.  The resulting object code runs in
a virtual machine called the **Java Virtual Machine** or **JVM**).

The JVM is supported on most operating systems and so Java programs can run
with the same behavior on almost any machine.
This portability of code is why Java is used
for the Egeria runtime - 
the [OMAG Server Platform](https://egeria-project.org/concepts/omag-server-platform) -
and the clients.

If you want to run Egeria you need to install the **Java Runtime Environment (JRE)**.
To build and test Egeria, you need the **Java Development Kit (JDK) installed**.
The JDK also contains the runtime environment (JRE).

There are various JDKs available, and you may even have one pre-installed on your system. You can check
by running the command `java -version` from the command-line.

Egeria requires Java 11 as a minimum level. Language Constructs up to Java 11 are permitted, but not above.
We use the [Adoptium (was AdoptOpenJDK)](https://adoptopenjdk.net) distribution. Official images & maven artifacts are built with this level.
Additionally code must compile and run on the current latest Java release. This is validated before any code can be merged.


Java can be installed by:

1. Downloading the **OpenJDK 11 (LTS) HotSpot** JVM from [Adoptium](https://adoptopenjdk.net).
2. Running the installer that is downloaded.

Alternatively jdk may be found on your operating system install repositories or via third party tools like HomeBrew (macOS).

Also you must ensure JAVA_HOME is set, and pointing to a JDK. If this is not done, an error such as `Failed to execute goal org.apache.maven.plugins:maven-javadoc-plugin:3.1.1:jar (attach-javadocs) on project open-connector-framework: MavenReportException: Error while generating Javadoc: Unable to find javadoc command: The environment variable JAVA_HOME is not correctly set.` will be seen as the javadoc maven plugin depends on this value to work correctly.

----
* Return to [Programming Languages](.)
* Link to [Building Egeria Tutorial](https://egeria-project.org/education/tutorials/building-egeria-tutorial/overview/)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

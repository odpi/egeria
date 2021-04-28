<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->


# Apache Maven

Maven is the tool that supports our project build.
This includes the code compilation, running unit tests,
validating dependencies and javadoc as well
as build our distribution archive.

The maven processing organizes the modules into a
hierarchy.  Each module has a `pom.xml` file (called "pom file") that defines the
artifact, its parent/children, dependencies and
any special processing that the module builds.  The "top-level pom file" 
is the `pom.xml` file in the 

When the maven command is run, it passes through the
hierarchy of modules multiple times.  Each pass processes a particular lifecycle phase of
the build (this is so, for example, java source files are compiled before the resulting
object files are packaged into a jar file).  This processing
includes locating and downloading
external libraries and dependencies.
The directory where these external dependencies is stored is called `.m2`.

The command to perform a complete rebuild of the project
is
```bash

$ mvn clean install

```
More details on Maven itself is on the [Apache Maven site](https://maven.apache.org/).
More details on Egeria's use of Maven is located in [Dependency Management](../Dependency-Management.md).
The [Building Egeria](../../open-metadata-resources/open-metadata-tutorials/building-egeria-tutorial) tutorial
covers more details on the build process.

## Installing Maven

You can check if Maven installed on your machine by running the command `mvn --version` from the command-line.

Maven can be installed:

- On MacOS, by first installing the [HomeBrew](https://brew.sh) package manager and then running
`brew install maven` from the command-line.
- On Linux operating systems, by using your distribution's package manager (`yum install maven`, `apt-get install maven`, etc).
- On Windows, you should use [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/) Version 2 or above, install an appropriate Linux distribution, and follow
the instructions for Linux.
  
Ensure you are using version 3.5.0 or higher in order to build Egeria.

----
* Return to [Developer Tools](.)


* Link to [Egeria's Community Guide](../../Community-Guide.md)
* Link to the [Egeria Dojo Education](../../open-metadata-resources/open-metadata-tutorials/egeria-dojo)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
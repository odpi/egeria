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
object files are packaged into a jar file).

The command to perform a complete rebuild of the project
is
```bash

$ mvn clean install

```
More details on Maven itself is on the [Apache Maven site](https://maven.apache.org/).
More details on Egeria's use of Maven is located in [Dependency Management](../Dependency-Management.md).

----
* Return to [Tools](.)
* Return to [Developer Resources](..)
* Return to the [Egeria Dojo](../../open-metadata-resources/open-metadata-tutorials/egeria-dojo)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
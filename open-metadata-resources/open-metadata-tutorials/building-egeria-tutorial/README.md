<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Downloading and Building ODPi Egeria Tutorial

ODPi Egeria is an open source project that is delivered both as source code as well as
[Maven Central Repository](https://search.maven.org/) libraries.

This tutorial will guide you through the process of downloading the ODPi Egeria source
code from [GitHub](https://github.com/) and building it so that you can run it
on your local machine.  

Once you have [downloaded Egeria](task-downloading-egeria-source.md), you can also use
[docker-compose](../lab-infrastructure-guide/running-docker-compose.md) to 
start the lab infrastructure for the [Hands-on Labs](../../open-metadata-labs).
This does not require you to build Egeria.

Already lost, or are the instructions below not detailed enough? Check out the [Egeria Dojo](../egeria-dojo),
or consider jumping straight to the [Hands-on Labs](../../open-metadata-labs), which provide a pre-built environment
for you and guided instructions on how to use different APIs.
 
## Prerequisite Technology for Building Egeria

You will need a **Java Development Kit (JDK)** installed on your machine in order to build Egeria.
There are various JDKs available, and you may even have one pre-installed on your system. You can check
if java is already installed by running the command `java -version` from the command-line.

Java can be installed by:

1. Downloading the **OpenJDK 8 (LTS) HotSpot** JVM from [AdoptOpenJDK](https://adoptopenjdk.net).
1. Running the installer that is downloaded.

Maven is the tool used to run the Egeria build.
You can check if Maven installed by running the command `mvn --version` from the command-line.
Ensure you are using version 3.5.0 or higher in order to build Egeria.

Maven can be installed:

- On MacOS, by first installing the [HomeBrew](https://brew.sh) package manager and then running
`brew install maven` from the command-line.
- On Linux operating systems, by using your distribution's package manager (`yum install maven`, `apt-get install maven`, etc).
- On Windows, you should use [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/) Version 2 or above, install an appropriate Linux distribution, and follow
  the instructions for Linux.
  
**Git** is an open source version control system used to store and manage Egeria's files.
You need it installed on your machine to work with Egeria's git repositories stored on
[GitHub](https://github.com/odpi/egeria).
You can check whether it is installed on your system by running `git --version` from the command-line.


## Tutorial tasks

1. [Downloading the Egeria source from GitHub](task-downloading-egeria-source.md)
2. [Building the Egeria source with Apache Maven](task-building-egeria-source.md)

## What next?

* [Run the hands on labs to get experience with using Egeria](../../open-metadata-labs)
or
* [Learn about installing Egeria in your own environment](../installing-egeria-tutorial)
or
* [Jump to the Egeria dojo to learn how to make a contribution to Egeria](../egeria-dojo/egeria-dojo-day-2-3-contribution-to-egeria.md)

----
* Return to [Egeria Dojo](../egeria-dojo)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

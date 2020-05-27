<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Building and developing Egeria

These technologies are needed in order to build and develop Egeria.

If you simply want to try out and explore Egeria, or if you find yourself lost in all of these technologies,
choose one of the [self-contained environments](README.md) instead.

## Git

Git is an open source version control system.  It is what we use to:

- track changes to the underlying Egeria code as the project evolves
- track issues and enhancements, and link these back to the code changes that resolve them
- collaborate on and review the issues, enhancements and code changes

As a result, it gives us a definitive source for the latest and greatest source code for
Egeria itself, its history, and the rationale behind various decisions that are made over
time.

In our [Downloading Egeria from GitHub tutorial](../building-egeria-tutorial/task-downloading-egeria-source.md),
when you are asked to do a `git clone https://github.com/odpi/egeria.git`, what you are
telling your own computer to do is to create a local copy of our source code repository.
It is this local copy that you can then use (locally) to build Egeria itself.

You can check whether it is installed on your system by running `git --version` from the command-line.

Git can be installed:

- On Windows, by downloading either [Git SCM](https://git-scm.com/download/win) or [Git for Windows](https://gitforwindows.org)
and running the installer that is downloaded.
- On MacOS, as part of the **Xcode** suite (running `git --version` will prompt you to install it if it is not already
installed).
- On Linux operating systems, by using your distribution's package manager (`yum install git`, `apt-get install git`, etc).

Interested to learn more?
[GitHub provides some great introductory guides](https://guides.github.com/introduction/git-handbook/).

## Java

Because Egeria is written in Java, you will need a Java Development Kit (JDK) installed in order to build
Egeria. The JDK can also act as the runtime environment (JRE) mentioned in [running Egeria natively](running-natively.md).

There are various JDKs available, and you may even have one pre-installed on your system. You can check
by running the command `java -version` from the command-line.

Java can be installed by:

1. Downloading the **OpenJDK 8 (LTS) HotSpot** JVM from [AdoptOpenJDK](https://adoptopenjdk.net).
1. Running the installer that is downloaded.

## Apache Maven

[Apache Maven](https://maven.apache.org) is a tool to describe and manage the technical aspects of a
software project. For example, it can be used to define various dependencies a project like Egeria has on
other pre-existing software projects or libraries. This information can then be used during the build
process to ensure that all of the necessary software is bundled together.

When you are asked to run `mvn clean install` in the [Building the Egeria Source tutorial](../building-egeria-tutorial/task-building-egeria-source.md),
you are instructing Maven to go through the Egeria source code and build the project: including all of these
external libraries and dependencies (including downloading them automatically, when needed).

You can check if Maven installed by running the command `mvn --version` from the command-line.

Maven can be installed:

- On Windows, by downloading the [binary zip archive](https://maven.apache.org/download.cgi) and
[following Maven's installation instructions for Windows](https://maven.apache.org/guides/getting-started/windows-prerequisites.html).
- On MacOS, by first installing the [HomeBrew](https://brew.sh) package manager and then running
`brew install maven` from the command-line.
- On Linux operating systems, by using your distribution's package manager (`yum install maven`, `apt-get install maven`, etc).

Ensure you are using version 3.5.0 or higher in order to build Egeria.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Installing ODPi Egeria

The [egeria build process](../building-egeria-tutorial) creates the
distribution files for Egeria in the **open-metadata-distribution** module.
The assemblies are located from the egeria build directory as follows:

```bash
$ cd open-metadata-distribution/open-metadata-assemblies/target
$ ls
ls
archive-tmp					egeria-3.3-SNAPSHOT-sources.tar.gz
egeria-3.3-SNAPSHOT-distribution		maven-archiver
egeria-3.3-SNAPSHOT-distribution.tar.gz		open-metadata-assemblies-3.3-SNAPSHOT.jar
egeria-3.3-SNAPSHOT-omag-server			rat.txt
egeria-3.3-SNAPSHOT-omag-server.tar.gz
```

The name of the files is determined by the release level of the code that you
[downloaded from GitHub](../building-egeria-tutorial/task-downloading-egeria-source.md).  In this example,
the release is **egeria-3.3-SNAPSHOT**.

Create a directory for the install and copy the tar file into it.
The two commands shown below create an install directory at the same level in the
file system as the **egeria** build library and then copies the egeria distribution file into it.

```bash
$ mkdir ../../../../egeria-install
$ cp egeria*-distribution.tar.gz ../../../../egeria-install
```

These next commands change to the new directory and lists its contents.

```bash
$ cd ../../../../egeria-install
$ ls
egeria-3.3-SNAPSHOT-distribution.tar.gz
```

It is now possible to unpack the tar file.

```bash
$ tar -xf egeria-3.3-SNAPSHOT-distribution.tar.gz
$ ls
egeria-3.3-SNAPSHOT-distribution.tar.gz	egeria-omag-3.3-SNAPSHOT
```

A new directory is created called **egeria-omag-3.3-SNAPSHOT**.  Change to this
new directory and list its contents as shown below.

```bash
$ cd egeria-omag-3.3-SNAPSHOT
$ ls
LICENSE			clients			server
NOTICE			conformance-suite	user-interface
```

Under **server** is a directory for the
[OMAG Server Platform](../../../open-metadata-publication/website/omag-server) that is used to run
open metadata and governance services.

Change to the OMAG server platform's directory.

```bash
$ cd server/omag-server*
$ resources				server-chassis-spring-3.3-SNAPSHOT.jar
```

This means you have completed this tutorial and are ready to choose the next step.

----
* Return to [Egeria Dojo](../egeria-dojo/egeria-dojo-day-2-3-contribution-to-egeria.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

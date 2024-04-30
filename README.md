<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![Egeria Logo](assets/img/ODPi_Egeria_Logo_color.png)

[![GitHub](https://img.shields.io/github/license/odpi/egeria)](LICENSE)
[![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/3044/badge)](https://bestpractices.coreinfrastructure.org/projects/3044)
[![Maven Central](https://img.shields.io/maven-central/v/org.odpi.egeria/egeria)](https://mvnrepository.com/artifact/org.odpi.egeria)

<!-- [![Azure](https://dev.azure.com/odpi/egeria/_apis/build/status/odpi.egeria)](https://dev.azure.com/odpi/Egeria/_build) -->
<!-- [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=odpi_egeria&metric=alert_status)](https://sonarcloud.io/dashboard?id=odpi_egeria) -->


# Egeria - Open Metadata and Governance
  
Egeria provides the Apache-2.0 licensed [open metadata and governance](https://egeria-project.org)
type system, frameworks, APIs, event payloads and interchange protocols to enable tools,
engines and platforms to exchange metadata in order to get the best
value from data, whilst ensuring it is properly governed.

## Building and running Egeria

This git repository contains the core Egeria code and resources.  This includes the build for Egeria's runtimes.
The project is built using `gradle`.  Clone or download the contents of this repository.
You will also need the [Java 17 SDK installed](https://adoptium.net/).
Then run the following gradle command from the top-level directory to build the code and run the tests.

```bash
./gradlew clean build
```

Once you see the "Build Successful" message, go to the `open-metadata-distributions/omag-server-platform/build` directory.

```bash
cd ./open-metadata-distribution/omag-server-platform/build
```
when you list the directory, you will see four subdirectories, two of which are interesting: 

```bash
ls
```

* `distributions` contains the zipped and compressed distribution file for Egeria's platform.  This file provides a convenient package to copy Egeria onto a specific machine to run. See [Installing Egeria](https://egeria-project.org/education/tutorials/building-egeria-tutorial/overview/#installing-egeria) for information on how to unpack this file.
* `unpacked` contains an unzipped copy of the distribution file, useful for running Egeria on the machine where it is built.

To run Egeria on the machine you used to run gradle, change directory as follows:

```bash
cd unpacked/egeria*gz
```

There you will see another `README.md` with further information.

```bash
cat README.md
```

### Further documentation

If you are new to Egeria, see the [getting started with Egeria](https://getting-started-with-egeria.pdr-associates.com/introduction.html) blogs, or alternatively,
visit [the Egeria website](https://egeria-project.org/).

## Egeria governance

This project aims to operate in a transparent, accessible way for the benefit of the Egeria community.
All participation in this project is open and not bound to any corporate affiliation.

To understand how to join and contribute, see the 
[Community Guide](https://egeria-project.org/guides/community/).  This includes the call schedule.

All participants are bound to the Egeria's [Code of Conduct](CODE_OF_CONDUCT.md).
The governance of the project is described in more detail in the
[Egeria Operations Guide](https://egeria-project.org/guides/project-operations/).

## Acknowledgements

![YourKit](https://www.yourkit.com/images/yklogo.png)

We are grateful to [YourKit, LLC](https://www.yourkit.com) for supporting open source projects with its full-feature
Java Profiler.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

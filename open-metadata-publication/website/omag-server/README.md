<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata and Governance (OMAG) Server Platform

The OMAG server platform provides the server environment for running open metadata
and governance services.

It is included in the [ODPi Egeria Distribution TAR file](../../../open-metadata-distribution/open-metadata-assemblies)
which can be installed on your machine by following the [Installing ODPi Egeria Tutorial](../../../open-metadata-resources/open-metadata-tutorials/building-egeria-tutorial/task-installing-egeria.md).

The OMAG server platform supports two broad groups of services:

* **Administration Services** - used to configure and manage logical OMAG servers running inside the OMAG server platform.
* **Open Metadata and Governance Services** - used to work with metadata and govern the assets of an organization.

Figure 1 shows the OMAG server platform when it first starts up.

![Figure 1](omag-server-platform-start-up.png)
> Figure 1: OMAG server platform at start up

The Administration Services are active at this point, while the open metadata and governance services
will return an error if called since there are no logical OMAG servers running.

The configuration services are used to create configuration documents.  Each configuration document
describes the open metadata and governance services that should be activated in a logical OMAG server.

Figure 2 shows the configuration services creating three configuration documents:
* one for the **cdoMetadataRepository** OMAG server
* one for the **stewardshipServer** OMAG Server
* one for the **dataLakeDiscoveryEngine** OMAG Server

![Figure 2](omag-server-platform-configure.png)
> Figure 2: Creating configuration documents for logical OMAG Servers

The [Administration Services User Guide](../../../open-metadata-implementation/governance-servers/admin-services/Using-the-Admin-Services.md)
provides detailed instructions on creating configuration documents.

Once a configuration document for a logical OMAG server is defined,
the operational services use it to initialize a logical OMAG server.
The logical OMAG server can be started in any OMAG server platform.
It does not have to be the OMAG server platform that created the configuration document.

Figure 3 shows an OMAG server platform with the **cdoMetadataRepository** local OMAG server
running.

![Figure 3](omag-server-platform-initialize-logical-omag-server.png)
> Figure 3: Starting a logical OMAG Server through the operational services

Once the logical OMAG server has initialized successfully, the open metadata and governance services
can route requests to it.

An OMAG server platform can run multiple logical OMAG servers at one time.  Figure 4 shows an OMAG server platform
running multiple servers.

![Figure 4](omag-server-platform-overview.png)
> Figure 4: An OMAG server platform running multiple logical OMAG servers




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
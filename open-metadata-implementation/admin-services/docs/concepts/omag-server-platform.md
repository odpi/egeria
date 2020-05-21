<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMAG Server Platform

The OMAG Server Platform provides a runtime process and platform for
Open Metadata and Governance (OMAG) Services.

The OMAG services are configured and activated in [OMAG Servers](omag-server.md) using the
[Administration Services](../user).
The configuration operations of the admin services create
[configuration documents](configuration-document.md), one for each OMAG Server.
Inside a configuration document is the definition of which OMAG services to activate in the server.
These include the [repository services](../../../repository-services) (any type of server), 
the [access services](../../../access-services) (for metadata access points
and metadata servers), [governance services](../../../governance-servers) (for governance servers) and
[view services](../../../view-services) (for view servers).
Once a configuration document is defined, the OMAG Server can be started and stopped multiple times by
the admin services server instance operations.
       
The OMAG Server Platform also supports some [platform services](../../../platform-services)
to query details of the servers running on the platform.
 
The OMAG Server Platform can host multiple OMAG servers at any one time.
Figure 1 shows different choices for distributing OMAG Servers on the OMAG Platforms.

![Figure 1](egeria-operations-server-choices-no-description.png)
> **Figure 1:** OMAG Server deployment choices.  An OMAG Server may have multiple copies of the
> same type of OMAG Server on a platform (multi-tenant operation for a cloud service),
> or different types of OMAG Server on a platform, or a separate platform for each OMAG Server
>([more information](omag-server.md)).

The choices are as follows:

* **A** - Each OMAG Server has its own dedicated OMAG Server Platform - useful when only one server is needed
in a deployment environment, or there is a desire to keep each server isolated in its own stack.

* **B** - Multiple OMAG Servers are hosted on the same OMAG Server Platform.  The OMAG Server Platform routes
inbound requests to the right server based on the server name specified in the request URL.
The servers may all be of the same type (multi-tenant operation) or be a set of collaborating servers
of different types consolidated on to the same platform.

* **C** - Multiple copies of same server instance running on different platforms to provide
high availability and distribution of workload (horizontal scalability).

Each OMAG server is isolated within the server platform and so the OMAG server platform can be used to 
support [multi-tenant](https://en.wikipedia.org/wiki/Multitenancy)
operation for a cloud service,
or host a variety of different OMAG Servers needed at a particular location.

## Further reading

* [Configuring the OMAG Server Platform](../user/configuring-the-omag-server-platform.md)
* [Overview of the OMAG Server Platform](../../../../open-metadata-publication/website/omag-server)
* [Installing the OMAG Server Platform Tutorial](../../../../open-metadata-resources/open-metadata-tutorials/building-egeria-tutorial/task-installing-egeria.md)
* [Running the OMAG Server Platform Tutorial](../../../../open-metadata-resources/open-metadata-tutorials/omag-server-tutorial)

## Related concepts

* [OMAG Server](omag-server.md)
* [OMAG Subsystems](omag-subsystem.md)
* [Configuration Documents](configuration-document.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
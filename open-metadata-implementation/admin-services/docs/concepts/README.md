<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Administration Services Concepts

The administration services manages the configuration of the
[Open Metadata and Governance (OMAG) Server Platform](omag-server-platform.md)
subsystems within an [OMAG Server](omag-server.md).

Figure 1 shows the different types of OMAG Servers that Egeria supports.  The light colored servers are generic types
of servers, the darker colored servers are those that can actually be run.

![Figure 1](types-of-omag-servers.png#pagewidth)
> **Figure 1:** Hierarchy of OMAG Server Types

The way to understand the diagram is that the arrows should be read as **IS A**.  For example,
the **Repository Proxy IS A Cohort Member** and the **Cohort Member IS A OMAG Server**.
This means that everything documented about a particular type of server is also true for
all server types that point to it through the **IS A** arrow, all of the way down the hierarchy.

Object-oriented software engineers would know of this type of relationship as behavior inheritance.

Follow the links to find out more about each type of server.

* [OMAG Server](omag-server.md) - Generic type of server.
  * [Cohort Member](cohort-member.md) - Supports exchange of metadata between metadata repositories through its
  membership of one or more [Open Metadata Repository Cohorts](../../../repository-services/docs/open-metadata-repository-cohort.md).
    * [Metadata Access Point](metadata-access-point.md) - Supports specialist metadata interfaces for particular types of third party technology.
    These interfaces are called the [Open Metadata Access Services (OMASs)](../../../access-services) or **access services** for short.
       * [Metadata Server](metadata-access-point.md) - the metadata server is a metadata access point with a native open metadata repository.
    * [Repository Proxy](repository-proxy.md) - the repository proxy translates between open metadata protocols and
    a third party metadata repository.
    * [Conformance Test Server](conformance-test-server.png) - the conformance test server hosts the
    [Open Metadata Conformance Suite](../../../../open-metadata-conformance-suite) used to validate the
    implementation of connectors to third party metadata repositories.
  * [View Server](view-server.md) - the view server provides services for web-based user interfaces (UIs).
  * [Governance Server](governance-server-types.md) - The governance server provides active metadata discovery and
  integration between third party tools, platforms and engines (ie technology other that metadata repositories).
    * [Integration Daemon](integration-daemon.md) - the integration daemons are managing the exchange of metadata
    between specific types of third party technology and an open metadata access point
       * [Data Platform Server](data-platform-server.md) - the data platform server provides metadata extraction
       from data stores such as database servers and file systems.
       * [Data Engine Proxy](data-engine-proxy.md) - the data engine proxy provides metadata extraction from processing
       engines that transform and distribute data, such as an ETL engine.
       * [Security Sync Server](security-sync-server.md) - the security sync server pushes security tag settings
       for assets to a security enforcement point such as Apache Ranger.
       * [Virtualizer](virtualizer.md) - the virtualizer is an experimental server for automatically
       configuring a data virtualization platform.
    * [Security Officer Server](security-officer-server.md) - the security officer server is an experimental server
    looking at supporting algorithms that use metadata to automatically configure security tags for assets.
    * [Engine Host](engine-host.md) - the engine hosts support open governance engines that provide value-add services
    to the open metadata ecosystem.
       * [Discovery Server](discovery-server.md) - the discovery server hosts one or more discovery engines that
       drive the analysis of assets to discover new insights into its structure, content and quality.
       * [Stewardship Server](stewardship-server.md) - the stewardship server supports the review and resolution of
       issues and insights discovered by the other governance servers.
    * [Open Lineage Server](open-lineage-server.md) - the open lineage server provides capture of lineage
    in a warehouse for further review and analysis.

Other concepts of interest for administration include:

* [Configuration Document](configuration-document.md) - configuration for a single OMAG Server
* [Configuration Document Store Connector](configuration-document-store-connector.md) - store for a configuration document
* [Event Bus](event-bus.md) - transport for events passing between OMAG Servers.

----
Link to
* [Administration Guide](../user) for the guide to configuring the OMAG Server Platform and
the different types of OMAG Servers.

* [Planning Guide](../../../../open-metadata-publication/website/planning-guide) for information
on planning your deployment of Egeria.

* [Platform Services](../../../platform-services) for information about the services use to manage
the OMAG Servers running on an OMAG Server Platform.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
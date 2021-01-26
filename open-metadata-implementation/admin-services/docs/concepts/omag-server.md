<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata and Governance (OMAG) Server

An **OMAG server** is a software server that
runs inside the [OMAG server platform](omag-server-platform.md).
It supports the integration of one or more technologies by hosting
connectors that interact with that technology, or providing specialist
APIs or event topics (both in and out).

Because of the wide variety of technologies deployed in organization's today,
each with very different capabilities and needs,
the integration and exchange of metadata needs to be organized.
This organization is managed through the Egeria frameworks and services
supported by the OMAG Servers.
There are different types of OMAG Server,
each supporting specific technologies.  The OMAG Server ensures this
type of technology is integrated appropriately for its needs.

The types of OMAG Server are shown in Figure 1. The hierarchy groups
similar types of server together.

![Figure 1](types-of-omag-servers.png#pagewidth)

More detail about each type of OMAG Server can be found by following the links below:

* [Cohort Member](cohort-member.md) - able to exchange metadata through an open metadata repository cohort
  * [Metadata Server](metadata-server.md) - supports a metadata repository that can natively store open metadata types
    as well as specialized metadata APIs for different types of tools (these APIs are called [access services](../../../access-services)).
  * [Metadata Access Point](metadata-access-point.md) - supports the access services like the metadata server but does not have a repository.
    All of the metadata it serves up and stores belongs to the metadata repositories in other members of the cohort.
  * [Repository Proxy](repository-proxy.md) - acts as an open metadata translator for
    a proprietary metadata repository.  It supports open metadata API calls and translates them to the
    proprietary APIs of the repository.  It also translates events from the proprietary repository into
    open metadata events that flow over the cohort.
  * [Conformance test server](conformance-test-server.png) - validates that a member of the
    cohort is conforming with the open metadata protocols.  This server is typically only see in development
    and test cohort rather than production.

* [View Server](view-server.md) - manages specialist services for user interfaces.

* [Governance Server](governance-server-types.md) - supports the use of metadata in the broader IT landscape.
  * [Engine Host](engine-host.md) - provides a runtime for a specific type of [governance engine](../../../engine-services).  
  * [Integration Daemon](integration-daemon.md) - manages the synchronization with third party technology that
    can not call the access services directly.
  * [Data Engine Proxy](data-engine-proxy.md) - Supports the capture of metadata from a data engine.  This includes
    details of the processing of data that it is doing which is valuable when piecing together lineage.
  * [Open Lineage Server](open-lineage-server.md) - Manages the collation of lineage information am maintains it in a format for reporting.
    This includes the state of the lineage at different points in time.

  
The different types of OMAG Servers connect together as shown in Figure 2.  There is an inner
ring of cohort members communicating via the cohort.  Each cohort member is sharing the metadata
they receive with the governance servers and view servers that connect to it.
The governance servers connect out to external tools, engines and platforms.


![Figure 2](omag-server-ecosystem.png#pagewidth)


## Further Information

The configuration for an OMAG Server is defined in a
[configuration document](configuration-document.md).
This configuration document is stored by a
[configuration document store connector](configuration-document-store-connector.md).
Details of the commands to configure an OMAG Server are
[here](../user/configuring-an-omag-server.md) and
the commands to start and stop an OMAG Server are
[here](../user/operating-omag-server.md).

There is design information on the OMAG Server and its relationship to
the OMAG Server Platform [here](../../../../open-metadata-publication/website/omag-server).

----
* Return to the [Administration Guide](../user)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
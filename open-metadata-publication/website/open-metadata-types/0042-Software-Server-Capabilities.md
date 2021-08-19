<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0042 Software Server Capabilities

Within a software server are many capabilities.
These range from full [applications](0050-Applications-and-Processes.md)
to [security plugins](0435-Policy-Management-Capabilities.md) to logging and encryption libraries.

Different organizations and tools can choose the granularity
in which the capabilities are captured in order to provide appropriate
context to data assets and the decisions made around them.

![UML](0042-Software-Server-Capabilities.png#pagewidth)

These are the software server capabilities defined in the open types:

* [APIManager](0050-Applications-and-Processes.md) - A capability that manages callable APIs that typically delegate onto Software Services.
* [Application](0050-Applications-and-Processes.md) - A capability supporting a specific business function.
* [Catalog](0050-Applications-and-Processes.md) - A capability that manages collections of descriptions about people, places, digital assets, things, ...
* [DataManager](0050-Applications-and-Processes.md) - A capability that manages collections of data.
* [Engine](0055-Data-Processing-Engines.md) - A programmable engine for running automated processes.
   * [Workflow Engine](0055-Data-Processing-Engines.md) - An engine capable of running a mixture of human and automated tasks as part of a workflow process.
   * [Reporting Engine](0055-Data-Processing-Engines.md) - An engine capable of creating reports by combining information from multiple data sets.
   * [Analytics Engine](0055-Data-Processing-Engines.md) - An engine capable of running analytics models using data from one or more data sets.
   * [Data Movement Engine](0055-Data-Processing-Engines.md) - An engine capable of copying data from one data store to another.
   * [Data Virtualization Engine](0055-Data-Processing-Engines.md) - An engine capable of creating new data sets by dynamically combining data from one or more data stores or data sets.
* [EventBroker](0050-Applications-and-Processes.md) - A capability that supports event-based services, typically around topics.
* [Software Services](0057-Software-Services.md) - A capability that provides externally callable functions to other services.
   * [Application Service](0057-Software-Services.md) - A software service that supports a reusable business function.
   * [Metadata Integration Service](0057-Software-Services.md) - A software service that exchanges metadata between servers.
   * [Metadata Access Service](0057-Software-Services.md) - A software service that provides access to stored metadata.
   * [Engine Hosting Service](0057-Software-Services.md) - A software service that provides services that delegate to a hosted engine.
   * [User View Service](0057-Software-Services.md) - A software service that provides user interfaces access to digital resources.
* [Network Gateway](0070-Networks-and-Gateways.md) - A connection point enabling network traffic to pass between two networks.
* [Database Manager](0224-Databases.md) - A capability that manages data organized as relational schemas.
* [Enterprise Access Layer](0225-Metadata-Repositories.md) - Repository services for the Open Metadata Access Services (OMAS) supporting federated queries and aggregated events from the connected cohorts.
* [Cohort Member](0225-Metadata-Repositories.md) - A capability enabling a server to access an open metadata repository cohort.
* [Governance Engine](0461-Governance-Engines.md) - A collection of related governance services of the same type.
   * [Governance Action Engine](0461-Governance-Engines.md) - A collection of related governance services supporting the Governance Action Framework (GAF).
   * [Open Discovery Engine](0601-Open-Discovery-Engine.md) - A collection of related governance services supporting the Open Discovery Framework (ODF). 

In addition it is possible to augment software server capabilities with the following classifications:

* [Cloud Service](0090-Cloud-Platforms-and-Services.md) - A capability enabled for a tenant on a cloud platform.
* [Content Collection Manager](0221-Document-Stores.md) - A manager of controlled documents and related media.
* [File System](0220-Files-and-Folders.md) - A capability that supports a store of files organized into a hierarchy of file folders for general use.
* [File Manager](0220-Files-and-Folders.md) - A manager of a collection of files and folders.
* [Notification Manager](0223-Events-and-Logs.md) - A server capability that is distributing events from a topic to its subscriber list.


----

* Return to [Area 0](Area-0-models.md).
* Return to [Overview](.).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
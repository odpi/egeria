<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0215 Software Components

**DeployedSoftwareComponent** describes a code asset that is deployed to implement a 
[software capability](0042-Software-Server-Capabilities.md).
Each software component has a well defined interface describe by an [APISchema](0536-API-Schemas.md) that is
linked to the DeployedSoftwareComponent by the [AssetSchemaType](0503-Asset-Schema.md) relationship.

**DeployedConnector** represents specialist software component called a
**connector** that provides pluggable access to third party
technologies.  These connectors implement the [Open Connector Framework (OCF)](../../../open-metadata-implementation/frameworks/open-connector-framework)
interfaces.

**EmbeddedProcess** describes a processing element nested within a DeployedSoftwareComponent.
The **TransientEmbeddedProcess** describes an EmbeddedProcess that runs only for a short period of time.

These variations are used to provide more information for lineage graphs.

**ProcessHierarchy** defines a parent-child relationship between processes, which can be used to define
more abstract processes that are comprised of lower-level processes; helping to support navigating
the process hierarchy.

![UML](0215-Software-Components.png#pagewidth)



## More information

#### Related Open Metadata Type Definitions

* [Definition of Process](0010-Base-Model.md)
* [Linking of processes into lineage graphs](Area-7-models.md)
* [Ports to show specific input and output flows for a process](0217-Ports.md)
* [PortSchema relationships to describe the structure of data supported by a Port](0520-Process-Schemas.md)


#### Use of these open metadata types

* [Egeria Developer Guide](../developer-guide) for more information on connectors and how to implement them.
* [Lineage](../lineage) describes the different types of lineage and how the open metadata types link
together to form lineage graphs.

----
Return to [Area 2](Area-2-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
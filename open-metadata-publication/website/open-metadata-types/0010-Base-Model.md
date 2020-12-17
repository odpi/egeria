<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0010 Base Model

The base model defines key concepts such as **Referenceable**, **Asset**,
**Infrastructure**, **Process** and **DataSet** along with the root entity for all metadata types, 
**OpenMetadataRoot**.


![UML](0010-Base-Model.png#pagewidth)

**Referenceable** is the super type for many of the open metadata entity
types. A referenceable is something that is important enough to
be assigned a unique (qualified) name within its type.
This unique name is often used outside of the open metadata
ecosystem as its unique identifier.
Referenceable also has provision for storing additional properties.
This is a set of name-value pairs (ie a map) where the values are all strings.

**Asset** represents the most significant type of referenceable.
An asset is something (either physical or digital) that is of
value and so needs to be managed and governed.

Infrastructure, Process and DataSet are examples of Assets.

* **Infrastructure** represents both the physical and digital assets that the organization
runs its business on.  There is more information on Infrastructure in:
   * [0030 Hosts and Platforms](0030-Hosts-and-Platforms.md)
   * [0035 Complex Hosts](0035-Complex-Hosts.md)
   * [0037 Software Server Platforms](0037-Software-Server-Platforms.md)
   * [0040 Software Servers](0040-Software-Servers.md)
   * [0042 Software Server Capabilities](0042-Software-Server-Capabilities.md)

* **Process** describes a well defined set of processing steps and decisions that drive a particular
aspect of the organization's business.  Most processes are automated with software
(see [DeployedSoftwareComponent](0215-Software-Components.md)) but they may also be a manual procedure.
An automated process can be invoked from a remote server through a [DeployedAPI](0212-Deployed-APIs.md).

* **DataSet** represents a collection of related data.  This data does not need to be stored together.
See [DataStore](0210-Data-Stores.md) for the asset that represents a physical store.

More information on assets can be found in [Building an Asset Catalog](../cataloging-assets).

The **Anchors** classification is used internally by the open metadata ecosystem to optimize
the look up of the entity at the root of a cluster of elements that represents a larger object.
Currently there is support for objects uniquely "owned" an asset to store the guid of that asset,
for a schema element to point to the schema type at the root of the schema object and a comment
to store the guid at the start of a comment thread.

Finally, the **Memento** classification identifies that the Referenceable
refers to a real-world asset/artifact that has either been deleted or archived offline.  The metadata
element has been retained to show its role in the [lineage of other assets/artifacts](../lineage).
The properties in this classification identifies
the archive processing and any information that helps to locate
the asset/artifact in the archive (if applicable).

## More information

* [Referenceables](../../../open-metadata-implementation/access-services/docs/concepts/referenceable.md)
* [Assets](../../../open-metadata-implementation/access-services/docs/concepts/assets)
* [Anchors](../../../open-metadata-implementation/access-services/docs/concepts/anchor.md)
* [Lineage](../lineage)


----
Return to [Area 0](Area-0-models.md).



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
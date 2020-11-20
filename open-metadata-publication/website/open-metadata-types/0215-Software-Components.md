<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0215 Software Components

Software components describe the code assets that are deployed to implement software capabilities.
Each software component has a well defined interface describe by an [APISchema](0536-API-Schemas.md) that is
linked to the DeployedSoftwareComponent by the [AssetSchemaType](0503-Asset-Schema.md) relationship.

In Egeria there are specialist software components called
**connectors** that provide pluggable access to third party
technologies.  These connectors implement the [Open Connector Framework (OCF)](../../../open-metadata-implementation/frameworks/open-connector-framework)
interfaces.  There is more information on the different types
of connectors and how to implement them in the [Egeria Developer Guide](../developer-guide).

![UML](0215-Software-Components.png#pagewidth)


## More information

* [Lineage](Area-7-models.md)
* [Process](../../../open-metadata-implementation/access-services/docs/concepts/assets)

----
Return to [Area 2](Area-2-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
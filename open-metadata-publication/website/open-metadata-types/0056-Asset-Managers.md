<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0056 Asset Managers

The **AssetManager** classification represents a technology that manages
metadata about assets and may also provide services to manage and/or govern
the assets themselves (or at least track such actions).

Data catalogs and other types of metadata catalogs are examples of
asset managers.
For example, [Amundsen](https://www.amundsen.io/amundsen/), [Marquez](https://marquezproject.github.io/marquez/) and
[Apache Atlas](https://atlas.apache.org) are examples of data catalogs.
An Egeria deployment using a [Metadata Server](../../../open-metadata-implementation/admin-services/docs/concepts/metadata-server.md)
and one or more [Integration Daemons](../../../open-metadata-implementation/admin-services/docs/concepts/integration-daemon.md)
can also be enabled as an asset manager.

![UML](0056-Asset-Managers.png#pagewidth)

The AssetManager classification on a [SoftwareServerCapability](0042-Software-Server-Capabilities.md) entity
is used by the [Asset Manager OMAS](../../../open-metadata-implementation/access-services/asset-manager)
to represent the third party asset manager that it is exchanging metadata with.

Identities from this third party asset manager are linked to the AssetManager entity
using the [ExternalIdScope](0017-External-Identifiers.md) relationship.


Return to [Area 0](Area-0-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0017 External Identifiers

External Identifiers are used to correlate the identifiers used in third party
metadata catalogs with open metadata elements.  The **ExternalId** entity
describes an external identifier from a specific third party metadata repository.
It includes:

* The identifier value itself in **identifier**.
* The pattern used for the identifier - in terms of how it is generated and managed is stored in **keyPattern**.
  These are the values it can take.  The default (and most used) value is LOCAL_KEY.

  * LOCAL_KEY     - Unique key allocated and used within the scope of a single system.
  * RECYCLED_KEY  - Key allocated and used within the scope of a single system that
                    is periodically reused for different records.
  * NATURAL_KEY   - Key derived from an attribute of the entity, such as email address, passport number.
  * MIRROR_KEY    - Key value copied from another system.
  * AGGREGATE_KEY - Key formed by combining keys from multiple systems.
  * CALLERS_KEY   - Key from another system can bey used if system name provided.
  * STABLE_KEY    - Key value will remain active even if records are merged.
  * OTHER         - Another key pattern.

This mapping can be many-to-many which is why you see
that the **ExternalIdLink** relationship between the open metadata
element (resources) and the external identifier is also many-to-many.

This relationship includes properties to help to map the open metadata element to the external identifier.

There is no guarantee that external identifiers from a third party metadata catalog
are globally unique and so the **ExternalIdScope** relationship links the external
identifier to the open metadata element that represents the third party metadata catalog.
Typically this is a type of [SoftwareServerCapability](0042-Software-Server-Capabilities.md),
for example, [AssetManager](0056-Asset-Managers.md).

![UML](0017-External-Identifiers.png#pagewidth)

## More information

There is an article on the use of external identifiers for correlating metadata elements from
different types of technologies [here](../external-identifiers).  

External identifiers are used extensively in the
[Asset Manager OMAS](../../../open-metadata-implementation/access-services/asset-manager)
in order to correlate the identifiers from third party technologies and open metadata types.

The [Catalog Integrator OMIS](../../../open-metadata-implementation/integration-services/catalog-integrator)
and [Lineage Integrator OMIS](../../../open-metadata-implementation/integration-services/lineage-integrator)
make use of Asset Manager OMAS's support for external identifiers in the interfaces they provide
to [integration connectors](../../../open-metadata-implementation/governance-servers/integration-daemon-services/docs/integration-connector.md).

External identifiers associated with an asset can be queried using the **getAssetProperties()** method from
[Asset Consumer OMAS](../../../open-metadata-implementation/access-services/asset-consumer) and
[Asset Owner OMAS](../../../open-metadata-implementation/access-services/asset-owner).
It is also available through the **getConnectedAssetProperties()** method on connectors that
have been created by Asset Consumer OMAS and Asset Owner OMAS.

----

* Return to [Area 0](Area-0-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Templated cataloging

Templated cataloguing is useful for situations where new assets are regularly created that
are of the same kind.  When a new asset is cataloged, the catalog entry of a similar
asset is supplied and it is used as a template to set up the new asset.

This approach is extremely valuable where there has been investment to provide
a rich set of information about the assets since it ensures this content is applied consistently
to each new asset with a single command.

The [Asset Owner OMAS](../../../open-metadata-implementation/access-services/asset-owner),
[IT Infrastructure OMAS](../../../open-metadata-implementation/access-services/it-infrastructure)
and [Digital Architecture OMAS](../../../open-metadata-implementation/access-services/digital-architecture)
provides the ability to set up and use templates to catalog new assets.

For this to work well, the organization needs to have thought through the types of information
that should be included in the catalog entry for these types of asset and manually set
up the first asset so that it can be used as a template for subsequent assets.

There is no linkage kept between an asset and its template except they may be linked to some of the same
shared definitions.  However, much of the core content of the asset catalog entry
(such as the Asset definition itself, Connection and Schema) are copied into the new entry.
If the same change is needed to all assets of a similar kind created from the same
template, then this needs to be done to each asset entry.  However the consistency of these asset
catalog entries due to the use of the template means that automating this update is much easier.

## Adding automation

Below are other types of automation to minimise the effort in managing your asset catalog.

* [Integrated cataloging](integrated-cataloging.md) - automated extraction of metadata from third party technologies.
* [Discovery and stewardship](discovery-and-stewardship.md) - analysis of asset contents to create metadata


----
* Return to [cataloging assets](.)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
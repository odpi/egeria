<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Scaling the asset catalog through automation

Automation is critical when it comes to managing an asset catalog.
It reduces the administrative work of subject matter experts and asset owners,
increases the reliability, reach and richness of the asset catalog whilst
reducing the cost of its maintenance.

Some automation is easy and reliable, particularly for information that can be extracted
directly from the digital technologies used to implement the assets.
Other automation involves analytics to create a candidate result that may need to be
confirmed and approved by a subject matter expert.  However, even when this human validation
is necessary, the effort required is significantly less that manual maintenance of the catalog. 

Whenever subject matter experts are involved, it typically requires a change to their role
in order to accommodate the time spent on the catalog.
Often these experts are from a different part of the
organization to the people receiving the benefit of their expertise, and so the organization's
appreciation and use of the asset catalog needs to have matured to allow this to happen.

Therefore as we look at the different types of automation, each comes with
its own organizational maturity required to make it successful.

## What automation is possible?

Egeria offers the following types of automation:

* [Manual cataloging](#Manual cataloging)
* [Templated cataloging](#Templated cataloging)
* [Integrated cataloging](#Integrated cataloging)
* [Discovery and stewardship](#Discovery and stewardship)

The idea is that these approaches are selected for each type of asset and blended together to balance
the investment in the automation, against the time commitment of subject matter experts, against
the business value of the resulting catalog.

## Manual cataloging

Manual cataloging uses no automation beyond the management of the metadata once it is created.
Individuals enter information about the assets into Egeria through tools that
call Egeria's Open Metadata Access Services (OMASs).

The [Asset Owner OMAS](../../../open-metadata-implementation/access-services/asset-owner)
is the principle interface for manual cataloging.  It is possible to catalog any type of
asset through this interface although it is biased towards cataloging data assets such
as data stores, data feeds, files, data sets, APIs and events.

In addition there are specific cataloging interfaces for particular types of subject matter expert.

* [IT Infrastructure OMAS](../../../open-metadata-implementation/access-services/it-infrastructure)
provides specialist interfaces for cataloging infrastructure such as servers, host systems and applications.

* [Digital Architecture OMAS](../../../open-metadata-implementation/access-services/digital-architecture)
provides specialist interfaces for architects and integration engineers to manually
catalog reference data sets and processes.
Reference data sets are assets in their own right, and their content can be used as classifiers to augment
the description of other assets.
Processes are also assets that, when linked together, show the lineage of the assets they are
partly responsible for maintaining.


## Templated cataloging

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
 

## Integrated cataloging

Integrated cataloging uses an **integration daemon**
to monitor a specific digital technology that hosts particular types of assets and automatically
maintain the catalog with information about these assets.

For example, an integration daemon may be monitoring a database server, updating the asset catalog each time a
new database is added, or a schema is changed.

Integration daemons support different integration patterns to meet the variety of capabilities
of digital technologies.  For example, it may poll the technology or listen for events from it
that indicate changes in the assets.

There is more information on the use of integrated cataloging on the
[integration daemon](../../../open-metadata-implementation/admin-services/docs/concepts/integration-daemon.md)
page.


## Discovery and stewardship

Integrated cataloging automates the creation the basic asset entry, its connection and
optionally, its schema.  This is what is called technical metadata.

Metadata discovery uses advanced analysis to inspect the content of specific assets to derive
new insights that can augment or validate their catalog entry.

The results can either be automatically applied to the asset's catalog entry or
it can go through a stewardship process to have a subject matter expert confirm the findings (or not).

Discovery and stewardship are the most advanced form of automation for asset cataloging.
Egeria provides the server runtime environment and component framework to
allow third parties to create discovery services and stewardship action implementations.
It has only simple implementations of these components, mostly for demonstration purposes.
This is the area where vendors and other open source projects are expected to
provide additional value.

There is more information on this topic on the
[metadata discovery](../metadata-discovery) page.

----
Return to [main article](.).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
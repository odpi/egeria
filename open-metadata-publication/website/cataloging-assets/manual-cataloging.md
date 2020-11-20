<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Manual cataloging of Assets

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

## Adding automation

Below are three types of automation to minimise the effort in managing your asset catalog.

* [Templated cataloging](templated-cataloging.md) - copying predefined assets.
* [Integrated cataloging](integrated-cataloging.md) - automated extraction of metadata from third party technologies.
* [Discovery and stewardship](discovery-and-stewardship.md) - analysis of asset contents to create metadata


----
* Return to [cataloging assets](.)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
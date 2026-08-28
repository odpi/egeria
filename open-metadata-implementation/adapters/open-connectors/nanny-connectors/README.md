<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# The Nanny Connectors

The Nanny Connectors provide support for the observation, analysis and improvement of an existing metadata 
catalog deployment. The idea is to create digital products that represent collections of reference data and insights
based on the content of the open metadata repositories.

A key component is the [Jacquard Integration connector](#jacquard-digital-product-loom) that assembles the 
open metadata digital products into the Open Metadata Digital Product Catalog.

There are also the tabular data set connectors - one for each type of product.
These connectors assemble a collection of open metadata into a single table structure that can
be provisioned into a destination that supports tabular data (eg CSV file, database table or kafka topic).

Finally, there are the external harvester connectors that harvest data from external sources and
create insights in open metadata for Jacquard products.  These connectors
can also feed tabular data stores with the raw data they are processing.

## Babbage Analytical Engine

In tribute to [Charles Babbage](https://en.wikipedia.org/wiki/Charles_Babbage)

The Babbage Analytical Engine is an integration connector that orchestrates [Lovelace Services](#lovelace-services) that analyze the data in open metadata and create insights.  These insights can be used to improve the quality of the data in open metadata and to identify new opportunities for data-driven innovation.

## Lovelace Services

In tribute to [Ada Lovelace](https://en.wikipedia.org/wiki/Ada_Lovelace)

These are the services that are orchestrated by the Babbage Analytical Engine.  Each on performs a specific task.  They are implemented as [Governance Services](https://egeria-project.org/concepts/governance-service/) and store their analysis as classification on the appropriate open metadata element.

## Mendel Automated Duplicate Manager

In recognition of the work on genetics and inheritance by [Gregor Mendel](https://en.wikipedia.org/wiki/Gregor_Mendel) - the survivorship rules of [duplicate management](https://egeria-project.org/features/duplicate-management/overview/) decide which properties are inherited by the combined element in much the same way.

The Mendel Automated Duplicate Manager is an integration connector that manages the duplicate links and classifications for the elements that are detected as potential duplicates.  It runs in its own integration group (`Egeria:IntegrationGroup:Mendel`) and each refresh makes three passes over the `PeerDuplicateLink` relationships in the open metadata ecosystem.

* The links that are still waiting for a decision - the `DISCOVERED`, `PROPOSED` and `IMPORTED` ones.  Where the linked elements are a close enough match, the status of the link is moved to `VALIDATED` and the `KnownDuplicate` classification is added to both elements, which is the combination that causes the retrieval processing to combine them.  Where they are not a close enough match, a *to do* is created for a steward to make the decision.  The to dos are assigned to the `DuplicateMetadataSteward` person role, which the connector creates if it does not already exist.
* The links that a steward has retired - the `DEPRECATED` and `OBSOLETE` ones.  Once none of an element's duplicate links are live, its `KnownDuplicate` classification is removed so that it is no longer combined with anything.
* The clusters of validated duplicates.  Once a cluster reaches the size set by the `duplicateClusterSize` configuration property (3 by default), its members are combined into a single consolidated element.  The survivorship rules take the properties of the latest version, adding any property that only an earlier version supplies, and combine the members' relationships except where that would break the cardinality rules of the relationship's type - where the type only permits one, the latest member's relationship wins.

Once the first refresh has worked through the duplicate links that were waiting for it, the connector also listens for open metadata events, so a new or updated duplicate link is reviewed as it occurs rather than waiting for the next refresh.  The retirement and consolidation passes stay on the refresh cycle because they depend on all of the duplicate links attached to an element, not just the one that changed.

## Jacquard Digital Product Loom

In tribute to [Joseph Marie Jacquard](https://en.wikipedia.org/wiki/Joseph_Marie_Jacquard)

The Jacquard Digital Product Loom is an integration connector that harvests the data from the open metadata repositories and creates a [digital product](https://egeria-project.org/concepts/digital-product/) that represents the metadata as a tabular data set.  The resulting digital products are organized into a [digital product catalog](https://egeria-project.org/types/7/0710-Digital-Products/).

The digital products support subscriptions.  The active subscriptions are managed by the [Baudot Subscription Manager](#baudot-subscription-manager).

## Baudot Subscription Manager

In tribute to [Emile Baudot](https://en.wikipedia.org/wiki/%C3%89mile_Baudot)

The Baudot Subscription Manager is an integration connector that manages the subscriptions to the Jacquard digital products in the digital product catalog.

## Wedgwood Data Provisioner

In tribute to [Thomas Wedgwood](https://en.wikipedia.org/wiki/Thomas_Wedgwood_(photographer))

The Wedgwood Data Provisioner is a [Governance Action Service](https://egeria-project.org/concepts/governance-action-service/) that provisions data from the digital products in the digital product catalog to other systems or teams for their projects.  It is called from the [Baudot Subscription Manager](#baudot-subscription-manager).

## Liskov Data Sharing Hub Manager

In recognition of the data abstraction work by [Barbara Liskov](https://en.wikipedia.org/wiki/Barbara_Liskov)

The Liskov data sharing hub manager maintains the data dictionary for a data sharing hub.  A data sharing hub is a specialized collection whose members are data stores.  These data stores are related in some way and provide a data-oriented service to other systems or teams for their projects.  The Data Sharing Hub Manager monitors the schema of theses data stores and maintains a data dictionary of the data fields and structures they contain.  The data fields identify similar data in different data stores.

The purpose of the data dictionary is to abstract the data structures away from the technical implementation making it easier to understand and use.  Additional curated information can be added to the data dictionary to provide more context and meaning to the data fields.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
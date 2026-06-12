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

## Liskov Data Hub Manager

In tribute to [Barbara Liskov](https://en.wikipedia.org/wiki/Barbara_Liskov)

The Liskov data hub manager maintains the data dictionary for a data hub.  A data hub is a specialized collection whose members are data stores.  These data stores are related in some way and provide a data-oriented service to other systems or teams for their projects.  The Data Hub Manager monitors the schema of theses data stores and maintains a data dictionary of the data fields and structures they contain.  The data fields identify similar data in different data stores.

The purpose of the data dictionary is to abstract the data structures away from the technical implementation making it easier to understand and use.  Additional curated information can be added to the data dictionary to provide more context and meaning to the data fields.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
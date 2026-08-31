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

The Mendel Automated Duplicate Manager is an integration connector that manages the duplicate links and classifications for the elements that are detected as potential duplicates.  It runs in its own integration group (`Egeria:IntegrationGroup:Mendel`) and each refresh makes four passes over the `PeerDuplicateLink` relationships in the open metadata ecosystem.

* The links that are still waiting for a decision - the `DISCOVERED`, `PROPOSED` and `IMPORTED` ones.  Where the linked elements are a close enough match, the status of the link is moved to `VALIDATED` and the `KnownDuplicate` classification is added to both elements, which is the combination that causes the retrieval processing to combine them.  Where they are not a close enough match, a *to do* is created for a steward to make the decision.  The to dos are assigned to the `DuplicateMetadataSteward` person role, which the connector creates if it does not already exist.
* The validated links that this connector decided itself, whose grounds may since have gone.  A close match can stop being one - the usual way is a qualified name being corrected, which is what a pair that only ever shared a name by mistake looks like once the mistake is fixed.  Nothing else revisits a validated link, so without this pass the two elements would stay combined for ever on the strength of a match that no longer exists.  The link is moved to `DEPRECATED` rather than deleted, so the withdrawal is visible and a steward can validate it again.  Only this connector's own validations are reconsidered: a steward's decision is a judgement the connector is not entitled to overturn, and the two are told apart by the link's `updatedBy`.
* The links that a steward has retired - the `DEPRECATED` and `OBSOLETE` ones.  An element's `KnownDuplicate` classification is removed once it is no longer deduplicated by any route - no live peer link, and no consolidated cluster to be reached through - so that it is no longer combined with anything.
* The clusters of validated duplicates.  Once a cluster reaches the size set by the `duplicateClusterSize` configuration property (3 by default), its members are combined into a single consolidated element.  The consolidated element carries the `ConsolidatedDuplicate` classification and is linked to each member with a `ConsolidatedDuplicateLink` relationship, which is the combination that causes it to be returned in place of the members.

Each pass works from one snapshot of the duplicate links, so the links that a refresh validates are consolidated by the next refresh rather than the same one.

### What a merge leaves behind

The survivorship rules decide what the consolidated element contains.  The properties come from the latest version of the cluster's members, and any property that only an earlier version supplies is added, so nothing a member knows about is lost.  The qualified name is derived rather than inherited - the original with the ISO-8601 time of the merge appended - because a qualified name is unique and the members still hold theirs.  Classifications and relationships from all of the members are combined, except where adding a relationship would break the cardinality rules of its type; where the type permits only one, the latest member's wins.

**Wherever those rules have to choose, the losing value is written to the audit log** - a conflicting property, a conflicting classification, a relationship that could not be carried - so that a steward can see what the consolidated element left behind rather than having to infer it.  The same is done for content that cannot be carried at all: a cluster whose members are of different types can hold properties and classifications that the consolidated element's type does not allow.

Withdrawing a validation is reported too.  If either element of a withdrawn link belongs to a consolidated cluster, the cluster is **not** broken up - its members go on being reached through the element that replaced them - but a message is raised so that the steward knows the cluster now rests on less evidence than it did.

Once the first refresh has worked through the duplicate links that were waiting for it, the connector also listens for open metadata events, so a new or updated duplicate link is reviewed as it occurs rather than waiting for the next refresh.  The withdrawal, retirement and consolidation passes stay on the refresh cycle because they depend on all of the duplicate links attached to an element, not just the one that changed.

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

A data dictionary is only as good as the descriptions of the data stores it is built from, so on each refresh Liskov also works to improve those descriptions.  Egeria's content packs define [governance action types](https://egeria-project.org/concepts/governance-action-type/) that catalog and survey each type of technology, and link them to the [technology type](https://egeria-project.org/concepts/deployed-implementation-type/) they support with a `ResourceList` relationship.  Liskov follows those links from each member's `deployedImplementationType` and, for every member it encounters:

* **Enables cataloguing if it is not already enabled.**  The cataloguing governance action types carry the integration connector that will do the cataloguing as a predefined action target, so Liskov treats cataloguing as already enabled when the member is one of that connector's catalog targets.  Otherwise it starts the governance action type, passing the member as the `newAsset` action target.  This is what reveals the contents of a member - cataloguing a file system directory, for example, creates the assets for the files inside it, and those files are then surveyed in their own right on a later refresh.
* **Requests a survey.**  A new survey is started on every refresh so that the description of the member's contents stays up to date.

Both kinds of request run asynchronously in a governance engine, so their results are picked up by a later refresh.  A request is skipped when an engine action started from the same governance action type is already running, or waiting to run, against the same member - so an outstanding request from an earlier refresh is never duplicated.

By default every survey that is registered for a member's technology type is run.  Most technology types register exactly one, but a file system directory registers four (`survey-folder`, `survey-folder-and-files`, `survey-all-folders` and `survey-all-folders-and-files`).  Where that is more surveying than is wanted, the `excludedSurveyRequestTypes` configuration property takes a comma-separated list of the surveys to skip.  Each value is either the survey's request type (for example, `survey-folder`) or the qualified name of its governance action type (for example, `FileSurvey::survey-folder`).  The property can be set on the connector's connection to apply to every data sharing hub, or on an individual catalog target to apply to just that hub.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
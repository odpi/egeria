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

The Lovelace services live in the [lovelace-insights](../lovelace-insights) module.  The current services are:

* *Award Karma Points* - watches for contributions to the open metadata ecosystem and awards karma points to the users responsible.
* *Build Zone Membership Profile* - populates the ZoneMembershipProfile classification of each governance zone.
* *Profile OpenLineage Runs* - analyses the runs of each job in an OpenLineage log store and records the run profile (frequency, regularity, duration, failure rate and data volume) in the RunMetrics classification of the job's process.
* *Refine OpenLineage Data Scope* - analyses the writes and reads of each dataset in an OpenLineage log store and refines the DataScope classification of the dataset's asset with the write pattern and data collection window.
* *Summarise OpenLineage Data Quality* - summarises the data quality assertions and tests in an OpenLineage log store into a survey report per dataset (and per job) with the pass rate of each quality dimension.

The three OpenLineage services share a log-store reader and are selected independently, so a deployment can run only the analyses it wants.  Each takes the log store folder as its `openLineageLogStore` action target (or a `logStoreDirectory` request parameter) and an `analysisWindowDays` request parameter (default 30).

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

## Darwin Product Dependency Manager

In tribute to [Charles Darwin](https://en.wikipedia.org/wiki/Charles_Darwin), who traced the origin of species - Darwin traces the origin of each digital product's data.

The Darwin Product Dependency Manager is an integration connector that maintains the coarse-grained lineage that is implied by the finer-grained lineage beneath it.  It runs in its own integration group (`Egeria:IntegrationGroup:Darwin`) under the `darwinnpa` userId.  Lineage bubbles up from the very detailed to the coarse-grained, so each refresh works upwards through three levels, and the `iscQualifiedName` of the finer-grained relationship is carried up onto the coarser one at every step.

* **Schema elements to data assets.**  A `DataMapping` relationship between two schema elements shows data being copied from one to the other.  Where the two schema elements are anchored to different data assets, Darwin maintains a `DataFlow` relationship from the source's asset to the target's.
* **Data assets to software servers.**  A software server hosts software capabilities (`SupportedSoftwareCapability`), and a capability owns data assets (`CapabilityAssetUse` with a `useType` of `OWNS`).  Darwin follows the data lineage downstream from each owned data asset in the same way as it does for products (see below) - through intermediate elements, keeping to one information supply chain, and including the asset-level data flows derived a moment earlier - and where a path reaches a data asset owned by a different server's capability it maintains a `DataFlow` relationship between the two servers.
* **Data assets to digital products.**  This is the level that maintains the `DigitalProductDependency` relationships between [digital products](https://egeria-project.org/concepts/digital-product/), described below.  The asset-level data flows are written to the repository first, so the products see them.

Every level is reconciled with the repository the same way.  All of these relationship types are multi-link, so there is one relationship per information supply chain between the same two elements.  Darwin recognizes its own relationships by the `createdBy` in their header: it removes the ones the finer-grained lineage no longer supports and creates the ones that are missing.  Relationships asserted by external users take precedence and are never removed; one that does not name an information supply chain is given the supply chain of the first finer-grained lineage that proves it (on behalf of the owning metadata collection if the relationship is not local).

The product level works through four steps.

* **It indexes the assets of each product** - the members of the product's `CollectionMembership` relationships that are assets.
* **It follows the data lineage downstream from each of those assets.**  A path may pass through any number of intermediate elements (processes, or assets that belong to no product), but every relationship on it must belong to the same [information supply chain](https://egeria-project.org/concepts/information-supply-chain/) - the `iscQualifiedName` on the lineage relationship.  When the path reaches an asset that is a member of another product, that product depends on the product the path started from, through that information supply chain, and the path stops: the dependency on anything further downstream belongs to the product just reached.  A path that has not reached a product within `maxLineageDepth` steps (20 by default) is abandoned.
* **It reconciles the derived dependencies with the stored `DigitalProductDependency` relationships** in the way described above.  A relationship asserted by an external user that no lineage path proves is *unproven*.
* **It records the unproven dependencies as exceptions.**  Darwin has its own `ExceptionType` (`ExceptionType::UnprovenDigitalProductDependency`), created on first use.  Each dependent product with unproven dependencies is linked to it with an `Exception` relationship whose `affectedRelationships` property lists the unproven `DigitalProductDependency` relationships.  The exception is updated as the list changes and removed once nothing is left on it.

## Jacquard Digital Product Loom

In tribute to [Joseph Marie Jacquard](https://en.wikipedia.org/wiki/Joseph_Marie_Jacquard)

The Jacquard Digital Product Loom is an integration connector that harvests the data from the open metadata repositories and creates a [digital product](https://egeria-project.org/concepts/digital-product/) that represents the metadata as a tabular data set.  The resulting digital products are organized into a [digital product catalog](https://egeria-project.org/types/7/0710-Digital-Products/).

The digital products support subscriptions.  The active subscriptions are managed by the [Baudot Subscription Manager](#baudot-subscription-manager).

## Baudot Subscription Manager

In tribute to [Emile Baudot](https://en.wikipedia.org/wiki/%C3%89mile_Baudot)

The Baudot Subscription Manager is a dynamic integration connector that manages the subscriptions to the Jacquard digital products in the digital product catalog.  Each product's notification types are handed to it by Jacquard as catalog targets.  On every refresh - on the interval configured for it in the integration daemon, or when a refresh is requested through the daemon's REST API - it sends the welcome, one-time and periodic notifications that each notification type's subscribers are due; changes to a notification type's monitored resources are notified as the change events arrive.

## Wedgwood Data Provisioner

In tribute to [Thomas Wedgwood](https://en.wikipedia.org/wiki/Thomas_Wedgwood_(photographer))

The Wedgwood Data Provisioner is a [Governance Action Service](https://egeria-project.org/concepts/governance-action-service/) that provisions data from the digital products in the digital product catalog to other systems or teams for their projects.  It is called from the [Baudot Subscription Manager](#baudot-subscription-manager).

## Liskov Data Sharing Hub Manager

In recognition of the data abstraction work by [Barbara Liskov](https://en.wikipedia.org/wiki/Barbara_Liskov)

The Liskov data sharing hub manager maintains the data dictionary for a data sharing hub.  A data sharing hub is a specialized collection whose members are data stores.  These data stores are related in some way and provide a data-oriented service to other systems or teams for their projects.  The Data Sharing Hub Manager monitors the schema of theses data stores and maintains a data dictionary of the data fields and structures they contain.  The data fields identify similar data in different data stores.

The purpose of the data dictionary is to abstract the data structures away from the technical implementation making it easier to understand and use.  Additional curated information can be added to the data dictionary to provide more context and meaning to the data fields.

A data dictionary is only as good as the descriptions of the data stores it is built from, so on each refresh Liskov also works to improve those descriptions.  Egeria's content packs define [governance action types](https://egeria-project.org/concepts/governance-action-type/) that catalog and survey each type of technology, and link them to the [technology type](https://egeria-project.org/concepts/deployed-implementation-type/) they support with a `ResourceList` relationship.  Liskov follows those links from each member's `deployedImplementationType` and, for every member it encounters:

* **Enables cataloguing if it is not already enabled.**  The cataloguing governance action types carry the integration connector that will do the cataloguing as a predefined action target, so Liskov treats cataloguing as already enabled when the member is one of that connector's catalog targets.  Otherwise it starts the governance action type, passing the member as the `newAsset` action target.  This is what reveals the contents of a member - cataloguing a file system directory, for example, creates the assets for the files inside it, and those files are then surveyed in their own right on a later refresh.
* **Requests a survey when one is due.**  A member that has never been surveyed is surveyed straight away.  After that, how often it is surveyed depends on how often its surveys find something new - see [Survey schedule](#survey-schedule) below.

Both kinds of request run asynchronously in a governance engine, so their results are picked up by a later refresh.  A request is skipped when an engine action started from the same governance action type is already running, or waiting to run, against the same member - so an outstanding request from an earlier refresh is never duplicated.

### Survey schedule

Surveying a data store takes resources from both the data store and Egeria, and a data store that has stopped changing does not need surveying every day.  So the interval between surveys of a member adapts to how often it changes:

* A member that has never been surveyed is surveyed straight away.
* After that, the interval starts at the minimum (one day by default).
* Each time a survey finds no change since the survey before it, the interval grows by a day, up to the maximum (seven days by default).
* As soon as a survey finds a change, the interval drops back to the minimum and the cycle repeats.

So a data store that keeps changing is surveyed daily, and one that has stopped changing is surveyed after 1 day, then 2, then 3 and so on, up to once a week.

The time is measured from the start of the latest survey, whether or not it completed, so a survey that keeps failing is retried at the minimum interval rather than on every refresh.  A survey that falls due within the next two hours counts as due.  This is because each survey starts a little after the refresh that requested it, and without that allowance the next day's refresh would find a little less than a day had passed and every one-day interval would become two.

Nothing about the schedule is stored.  It is worked out from the member's survey reports on each refresh, so it survives a restart of the integration daemon.  Each schedule is kept separately for each survey of each member: the survey action engine records the survey's request type (for example, `survey-folder`) as the `purpose` of each survey report it creates, and Liskov matches on that.

#### How survey reports are compared

A survey "found no change" when it reported exactly the same annotations as the survey before it.  This works because an annotation that a later survey finds again is reused rather than recreated.  An annotation matches an earlier one when it is of the same type and all its properties are equal, apart from its qualified name, display name, description, effective dates and extended properties.  A resource profile log annotation - whose detail is written to a log file, a new one for each survey - also ignores which log files it refers to; each survey's log file is linked to the annotation instead.  So two surveys that found the same things report the same annotations, and anything they found differently produces a new annotation.

Two reports are compared in two steps:

1. **Count the annotations each report reported.**  The repository counts the `ReportedAnnotation` relationships without returning them, so this is cheap.  Different counts are a change.
2. **If the counts are equal, compare the annotations' unique identifiers.**  Equal counts are not proof of no change: an annotation can drop out of one survey and come back in the next, reused from an earlier survey, leaving the counts equal and the sets different.  Any difference in either direction - a new annotation, or one the earlier report had that the later one does not - is a change.

The result for each pair of reports is cached for as long as the connector runs, since completed survey reports do not change.

The work is kept to what the decision needs:

* Nothing is compared within the minimum interval of the latest survey, nor once the maximum interval has passed.
* Otherwise the reports are compared only as far back as the time since the latest survey requires, stopping at the first change.
* The member's survey reports are read newest first, a page at a time, and reading stops once enough completed reports from the same survey have been found.  A member that has been surveyed for years may have thousands of reports; at most a handful are read.

Some consequences are worth knowing:

* **A survey that records a value that changes on every run will always find a change.**  Examples are row counts on a busy table, file sizes, or any time held in an annotation property.  That member stays on the minimum interval.  For a busy table this is intended; for a survey that merely records when it ran, it means the member is never given a longer interval.
* **Changes only to the ignored properties are not seen.**  A change to just the extended properties or effective dates of an annotation reuses the annotation, so it does not count as a change.  Nor does a change that shows only inside a profile log file - a folder survey's file name counts, for example - though a change in the number or types of files is seen through the folder survey's other annotations.
* **Reports from before annotations were reused always look different**, because each one had its own annotations.  The interval starts to grow once two surveys made with annotation reuse have run in a row.

#### Configuring the schedule

| Configuration property | Default | Meaning |
|---|---|---|
| `minimumSurveyIntervalDays` | 1 | The fewest days between surveys of a member, and the interval the schedule returns to when a survey finds a change. |
| `maximumSurveyIntervalDays` | 7 | The most days between surveys of a member.  A value below `minimumSurveyIntervalDays` is treated as `minimumSurveyIntervalDays`, which gives a fixed interval. |

Like `excludedSurveyRequestTypes` below, these can be set on the connector's connection to apply to every data sharing hub, or on an individual catalog target to apply to just that hub.

### Choosing which surveys run

By default every survey that is registered for a member's technology type is run.  Most technology types register exactly one, but a file system directory registers four (`survey-folder`, `survey-folder-and-files`, `survey-all-folders` and `survey-all-folders-and-files`).  Where that is more surveying than is wanted, the `excludedSurveyRequestTypes` configuration property takes a comma-separated list of the surveys to skip.  Each value is either the survey's request type (for example, `survey-folder`) or the qualified name of its governance action type (for example, `FileSurvey::survey-folder`).  The property can be set on the connector's connection to apply to every data sharing hub, or on an individual catalog target to apply to just that hub.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
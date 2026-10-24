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
* *Build Member Popularity Profile* - totals the usage activity (from the [UsageActivity](https://egeria-project.org/types/7/0795-Usage-Activity/) classifications maintained by [Pepys](#pepys-activity-observer)) of the members of each root collection, glossary, data sharing hub, collection folder and digital product catalog, and records it in the collection's MemberPopularityProfile classification.  Nested collections are profiled first in the same run, so the activity of the leaf members ripples up through every level to the top-level collections.
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

## Pepys Activity Observer

In tribute to [Samuel Pepys](https://en.wikipedia.org/wiki/Samuel_Pepys) and the diary in which he so faithfully recorded the activity around him.

The Pepys Activity Observer is an integration connector that maintains the [UsageActivity](https://egeria-project.org/types/7/0795-Usage-Activity/) classification of the anchor elements in the open metadata ecosystem - its assets, digital products and glossary terms.  It runs in the `Egeria:IntegrationGroup:OpenMetadataObservability` integration group.

Its catalog targets are the topics that the OMAG Servers write their audit logs to.  It does not find these itself: the *OMAG Server Platform Cataloguer* catalogues each server's audit log topic and adds it to Pepys's catalog targets, using the GUID in its `friendshipGUID` configuration property.  Pepys registers a listener with each topic and counts the anchor element activity messages it receives, each kind of activity in its own count:

| Message | Activity | Count |
|---|---|---|
| `METADATA-OBSERVABILITY-0002`, `-0003` | The anchor element, or an element anchored to it, was retrieved by identifier or name | `explicitGetCount` |
| `METADATA-OBSERVABILITY-0008`, `-0009` | The anchor element, or an element anchored to it, was returned by a search | `findCount` |
| `METADATA-OBSERVABILITY-0006` | The properties of the anchor element were updated | `updatePropertyCount` |
| `METADATA-OBSERVABILITY-0004` | An element anchored to the anchor element was created, linked, unlinked or updated | `updateAttachmentCount` |
| `METADATA-OBSERVABILITY-0005` | Feedback (comments, likes, ratings and informal tags) was added or updated | `updateFeedbackCount` |
| `METADATA-OBSERVABILITY-0007` | An element anchored to the anchor element was deleted | `deleteAttachmentCount` |
| `METADATA-OBSERVABILITY-0011` | Feedback was deleted | `deleteFeedbackCount` |

The creation of an anchor element (`METADATA-OBSERVABILITY-0001`) is not counted.

The counts are gathered in memory as the audit log records arrive and are added to each anchor element's UsageActivity classification on every refresh (every 20 minutes as configured in the core content pack), so a busy element costs one read and one update per refresh rather than one per record.  Counts that cannot be written because the metadata access server is unavailable are kept and tried again on the next refresh; counts for an element that can no longer be found are discarded.  Any counts still waiting when the connector shuts down are written as it disconnects.

The activity of the users whose userIds match one of the patterns in the `ignoredUserIdPatterns` configuration property is not counted.  Each pattern is a Java regular expression matched against the whole userId, so `.*npa` matches every userId ending in npa and a plain userId is an exact match.  The core content pack sets the patterns to `.*npa` and `.*engine`, which match the non-personal accounts of the OMAG servers and integration connectors and the userIds of the governance engines, so that only the activity of people and external tools is recorded.  Patterns are used rather than a list of userIds because the servers' userIds are different in each environment.  Pepys's own userId is always ignored, whether a pattern matches it or not (and when the property is not set) - its reads and updates of the anchor elements are reported in the audit log too, and otherwise each update of a UsageActivity classification would be counted as more activity, without end.  A pattern that is not a valid regular expression is reported (PEPYS-ACTIVITY-OBSERVER-0007) and skipped.

## Darwin Product Dependency Manager

In tribute to [Charles Darwin](https://en.wikipedia.org/wiki/Charles_Darwin), who traced the origin of species - Darwin traces the origin of each digital product's data.

The Darwin Product Dependency Manager is an integration connector that maintains the coarse-grained lineage that is implied by the finer-grained lineage beneath it.  It runs in its own integration group (`Egeria:IntegrationGroup:Darwin`) under the `darwinnpa` userId.  Lineage is built up in layers - schema detail, the data fabric, the system fabric and the data mesh - and each refresh works upwards across the boundaries between them, carrying the `iscQualifiedName` of the finer-grained relationship up onto the coarser one at every step.  The design is described in [docs/darwin-layered-lineage.md](docs/darwin-layered-lineage.md).

* **Schema elements to the data fabric.**  A `DataMapping` relationship between two schema elements shows data being copied from one to the other.  Where the two schema elements are anchored to different data assets, Darwin maintains a `DataFlow` relationship from the source's asset to the target's.  A process at the end of a `DataMapping` is a data fabric element in its own right, so a mapping between a column and a process gives a `DataFlow` between the column's asset and the process.
* **The data fabric to the system fabric.**  An item of IT infrastructure supports software capabilities (`SupportedSoftwareCapability`), and a capability uses data assets and processes (`CapabilityAssetUse`).  A data asset counts for the infrastructure when the `useType` is `OWNS`; a process counts whatever the `useType`.  Darwin follows the lineage downstream from each of those elements in the same way as it does for products (see below) - through intermediate elements, keeping to one information supply chain, and including the data fabric's data flows derived a moment earlier - and where a path reaches an element used by a different item of infrastructure it maintains a relationship between the two items of the same kind as the lineage: a `DataFlow` for data lineage, a `ProcessCall` where a `ProcessCall` reaches the element, and a `ControlFlow` for a path of `ControlFlow` relationships.
* **The data fabric to digital products.**  This is the level that maintains the `DigitalProductDependency` relationships between [digital products](https://egeria-project.org/concepts/digital-product/), described below.  The data fabric's data flows are written to the repository first, so the products see them.  `ControlFlow` is not followed here: a product depends on another product's data, not on the order in which their processes run.

Every level is reconciled with the repository the same way.  All of these relationship types are multi-link, so there is one relationship per information supply chain between the same two elements.  Darwin recognizes its own relationships by the `createdBy` in their header, and tells its relationships of one kind from another of the same type (such as the data flows in the data fabric and between servers) by the `derivation` it records on them - an attribute of every lineage relationship that names the rule that derived it, leaving the `label` to summarize the data that flows.  Relationships created by earlier releases, which recorded the rule in the label, are recognized and moved over to the derivation.  Darwin removes the ones the finer-grained lineage no longer supports and creates the ones that are missing.  A relationship Darwin created that someone else has since updated has been adopted: Darwin treats it from then on as asserted by an external user, so a person's edits are never lost when the lineage beneath changes.  Relationships asserted by external users take precedence and are never removed; one that does not name an information supply chain is given the supply chain of the first finer-grained lineage that proves it (on behalf of the owning metadata collection if the relationship is not local).

Darwin also validates the lineage asserted by external users at each layer against the layer beneath it.  A relationship that the layer beneath does not prove is *unproven*, but only when that layer is catalogued for both of its ends - a data flow between two data assets is only checked when both have schema elements that take part in data mappings, a relationship between items of infrastructure when both have capabilities that use data fabric elements, and a product dependency when both products have assets among their members.  Otherwise the missing detail is no evidence against the relationship, and it is counted as *unchecked* in the audit log.  Darwin has an `ExceptionType` for each relationship type (`ExceptionType::UnprovenDataFlow`, `ExceptionType::UnprovenControlFlow`, `ExceptionType::UnprovenProcessCall` and `ExceptionType::UnprovenDigitalProductDependency`), created on first use, and links the element that depends on unproven relationships - the element the data or control reaches, or the dependent product - to the exception type with an `Exception` relationship whose `affectedRelationships` property lists them.  The exception is updated as the list changes and removed once nothing is left on it.

Darwin also establishes the `iscQualifiedName` wherever it can.  A lineage relationship that names no information supply chain is treated as "supply chain unknown" rather than "no supply chain": it can join a path of any supply chain, and the first relationship on the path that names one fixes the supply chain for the whole path.  A path whose relationships name none proves a relationship of any supply chain in the layer above.  Once a path's supply chain is known - from a relationship on it, or from the externally asserted relationship it proves - the untagged relationships on it are given that supply chain at the end of the refresh: filled in (on behalf of the owning metadata collection if it is not local) when one supply chain is known for them, and, when several are, left untagged with a copy recorded by Darwin for each supply chain (with the derivation `supply chain inferred`, and the label of the relationship it copies).  Only relationships asserted by external users are changed, and Darwin never overwrites a supply chain.  An untagged relationship that Darwin would derive between two elements is not created where a tagged one links them.

A data set and the content it is a view over (`DataSetContent`) are treated as the same element when Darwin follows the data fabric's lineage up to the system fabric and the data mesh.  A walk from a data set also starts from its content, and an element reached belongs to the owners of the data sets over it as well as its own.  This matters for products, whose members are often the abstract data sets while the lineage from traces is attached to the physical elements beneath them.  A `DataSetContent` that names an information supply chain only makes the two equivalent on the paths of that supply chain (or of an unknown one, which it then makes known).  A data set is often a view over a single relational table, whose lineage to and from its processes is recorded at the level of the table (`DataMapping` relationships between the table and the process, as the Open Lineage Cataloguer records them), so these walks also follow the `DataMapping` relationships that run between a table and a process.

The product level works through four steps.

* **It indexes the assets of each product** - the members of the product's `CollectionMembership` relationships that are assets.
* **It follows the data lineage downstream from each of those assets.**  A path may pass through any number of intermediate elements (processes, or assets that belong to no product), but every relationship on it must belong to the same [information supply chain](https://egeria-project.org/concepts/information-supply-chain/) - the `iscQualifiedName` on the lineage relationship.  When the path reaches an asset that is a member of another product, that product depends on the product the path started from, through that information supply chain, and the path stops: the dependency on anything further downstream belongs to the product just reached.  A path that has not reached a product within `maxLineageDepth` steps (20 by default) is abandoned.
* **It reconciles the derived dependencies with the stored `DigitalProductDependency` relationships** in the way described above.  A relationship asserted by an external user that no lineage path proves is *unproven* (or *unchecked*, if either product has no assets among its members).
* **It records the unproven dependencies as exceptions**, as described above, against `ExceptionType::UnprovenDigitalProductDependency`.

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
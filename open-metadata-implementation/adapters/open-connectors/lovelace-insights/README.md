<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

![Stable](../../../../images/egeria-content-status-released.png#pagewidth)

# Lovelace Insight Services

The Lovelace Insight Services module provides the analytical governance services that are orchestrated by the
Babbage Analytical Engine.  Each service examines the open metadata ecosystem (or a store of observations about it,
such as an OpenLineage log store) and records what it finds as classifications, or as survey reports, on the
appropriate open metadata elements.

Each service is a [governance service](https://egeria-project.org/concepts/governance-service/) registered with
the `EgeriaGovernance` governance engine under its own request type.  The Babbage Analytical Engine (an integration
connector in the [nanny connectors](../nanny-connectors)) has the governance action type for each service as one of
its catalog targets, and on every refresh it starts each of them.  A service can also be run on its own by
initiating its governance action type.

The services are:

| Service | Package | Kind | Content pack | What it does |
|---------|---------|------|--------------|--------------|
| Award Karma Points | `organizationinsight.karmapoints` | Watchdog action service | Organization Insight | Watches for contributions to the open metadata ecosystem and awards karma points to the users responsible. |
| Build Zone Membership Profile | `securityinsight.zoneprofile` | Governance action service | Organization Insight | Populates the ZoneMembershipProfile classification of each governance zone. |
| Build Member Popularity Profile | `usageinsight.popularity` | Governance action service | Core | Totals the usage activity of the members of each root collection, glossary, data sharing hub, collection folder and digital product catalog, and records it in the collection's MemberPopularityProfile classification. |
| Profile OpenLineage Runs | `lineageinsight.openlineage` | Governance action service | Open Lineage | Analyses the runs of each job in an OpenLineage log store and records the run profile (frequency, regularity, duration, failure rate and data volume) in the RunMetrics classification of the job's process. |
| Refine OpenLineage Data Scope | `lineageinsight.openlineage` | Governance action service | Open Lineage | Analyses the writes and reads of each dataset in an OpenLineage log store and refines the DataScope classification of the dataset's asset with the write pattern and data collection window. |
| Summarise OpenLineage Data Quality | `lineageinsight.openlineage` | Governance action service | Open Lineage | Summarises the data quality assertions and tests in an OpenLineage log store into a survey report per dataset (and per job) with the pass rate of each quality dimension. |

None of the services has request parameters or action targets except the OpenLineage services, described below.
Each governance action service produces the `service-completed` or `service-failed` guard.

## Award Karma Points

The karma point awards service is a watchdog action service: rather than running once and completing, it listens for
open metadata events.  For each element that is created or updated (other than by a refresh) it awards a karma point
to the user that made the change, recording the running total in the `ContributionRecord` linked to the user's actor
profile.  The contribution record is created the first time a user earns a point.  Updates are synchronized so that
two events for the same user do not lose a point.

## Build Zone Membership Profile

For each governance zone, the zone membership profiler counts the elements that are members of the zone, by type, and
stores the counts in the zone's `ZoneMembershipProfile` classification.  It keeps three views of the zone:

* the anchor elements whose `ZoneMembership` classification names the zone (`totalMembership`, `typeMembership`);
* the elements that are members because their anchor is in the zone, found through the `Anchors` classification
  (`anchoredTotalMembership`, `anchoredTypeMembership`);
* the two combined (`allTotalMembership`, `allTypeMembership`).

The time of the analysis is recorded in `analysisTime`.

## Build Member Popularity Profile

The popularity profiler turns the usage activity recorded by the [Pepys Activity Observer](../nanny-connectors#pepys-activity-observer)
in each element's [UsageActivity](https://egeria-project.org/types/7/0795-Usage-Activity/) classification into a
`MemberPopularityProfile` for the collections that organize those elements.

* It profiles every root collection, glossary, data sharing hub, collection folder and digital product catalog
  (templates are skipped).  A collection's members are the elements linked to it through `CollectionMembership`.
* Each member contributes the counts in its own `UsageActivity`, plus the totals of its own members if it is one of the
  profiled kinds of collection.  These nested collections are profiled first, in the same run, so the activity of the
  leaf members ripples up through every level to the top-level collections in a single run.  A member that is some
  other kind of collection contributes the totals of its stored `MemberPopularityProfile`, if it has one.  A membership
  cycle is detected, and a collection reached again through its own members contributes nothing the second time.
* The profile holds, for each of the seven usage counts (`explicitGetCount`, `findCount`, `updatePropertyCount`,
  `updateAttachmentCount`, `updateFeedbackCount`, `deleteAttachmentCount`, `deleteFeedbackCount`), a `...Total` and a
  `...Profile` map of member GUID to that member's count.  Members with no activity are left out of the maps.
* A profile is only rewritten when its counts have changed.
* The members are read as part of building each collection's profile (the reads set `viewElementGUID`), so the
  profiler's own reads are not counted as the members being found.
* If a collection's profile can not be stored - typically because the governance engine's user is not permitted to
  classify elements in the collection's governance zones - the service logs `LOVELACE-INSIGHTS-0048` and carries on
  with the other collections.  The collection's totals are still added to the collections that contain it.  The run
  ends with a summary message, `LOVELACE-INSIGHTS-0047`.

The arithmetic is in `MemberActivity`, which is unit-tested independently of a metadata server.

## OpenLineage analysis services

The three OpenLineage services share `LovelaceOpenLineageAnalysisServiceBase`, which locates the log store, reads
the events in the analysis window into an `OpenLineageRunHistory` (runs grouped by job; writes, reads and
assertions grouped by dataset) and finds the process or data asset that the
[OpenLineage cataloguer](../integration-connectors/openlineage-integration-connectors) created for each job and
dataset, using the cataloguer's identity rules (namespace and resource name).  Each service then works on the
history and updates only the elements it can identify uniquely.

They ship in the Open Lineage content pack and are selected independently so a deployment can run only the analyses it wants.  Each supports:

* action target `openLineageLogStore` - the folder asset for the directory written by the file-based OpenLineage log store integration connector;
* request parameter `logStoreDirectory` - the directory path, used when there is no action target;
* request parameter `analysisWindowDays` - how many days of history to analyse (default 30; 0 for everything).

The design and the division of work between the event-time cataloguer and these services is described in the
cataloguer's [design note](../integration-connectors/openlineage-integration-connectors/docs/open-lineage-cataloguing.md).



Return to [open-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
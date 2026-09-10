<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

![TechPreview](../../../../images/egeria-content-status-released.png#pagewidth)

# Lovelace Insight Services

The Lovelace Insight Services module provides the analytical governance services that are orchestrated by the
Babbage Analytical Engine.  Each service examines the open metadata ecosystem (or a store of observations about it,
such as an OpenLineage log store) and records what it finds as classifications, or as survey reports, on the
appropriate open metadata elements.

The services are:

| Service | Kind | What it does |
|---------|------|--------------|
| Award Karma Points (`organizationinsight.karmapoints`) | Watchdog action service | Watches for contributions to the open metadata ecosystem and awards karma points to the users responsible. |
| Build Zone Membership Profile (`securityinsight.zoneprofile`) | Governance action service | Populates the ZoneMembershipProfile classification of each governance zone. |
| Profile OpenLineage Runs (`lineageinsight.openlineage`) | Governance action service | Analyses the runs of each job in an OpenLineage log store and records the run profile (frequency, regularity, duration, failure rate and data volume) in the RunMetrics classification of the job's process. |
| Refine OpenLineage Data Scope (`lineageinsight.openlineage`) | Governance action service | Analyses the writes and reads of each dataset in an OpenLineage log store and refines the DataScope classification of the dataset's asset with the write pattern and data collection window. |
| Summarise OpenLineage Data Quality (`lineageinsight.openlineage`) | Governance action service | Summarises the data quality assertions and tests in an OpenLineage log store into a survey report per dataset (and per job) with the pass rate of each quality dimension. |

## OpenLineage analysis services

The three OpenLineage services share `LovelaceOpenLineageAnalysisServiceBase`, which locates the log store, reads
the events in the analysis window into an `OpenLineageRunHistory` (runs grouped by job; writes, reads and
assertions grouped by dataset) and finds the process or data asset that the
[OpenLineage cataloguer](../integration-connectors/openlineage-integration-connectors) created for each job and
dataset, using the cataloguer's identity rules (namespace and resource name).  Each service then works on the
history and updates only the elements it can identify uniquely.

They ship in the Open Lineage content pack (the other Lovelace services ship in the Organization Insight content pack)
and are selected independently so a deployment can run only the analyses it wants.  Each supports:

* action target `openLineageLogStore` - the folder asset for the directory written by the file-based OpenLineage log store integration connector;
* request parameter `logStoreDirectory` - the directory path, used when there is no action target;
* request parameter `analysisWindowDays` - how many days of history to analyse (default 30; 0 for everything).

The design and the division of work between the event-time cataloguer and these services is described in the
cataloguer's [design note](../integration-connectors/openlineage-integration-connectors/docs/open-lineage-cataloguing.md).



Return to [open-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
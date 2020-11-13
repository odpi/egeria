<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0605 Open Discovery Analysis Report

Each time a discovery engine runs an open discovery service,
the open discovery service creates a collection
of annotations.
These annotations are managed in an in-memory cache during the execution of the discovery service
to allow later steps in the discovery service to access
annotations from the previous steps.

When the open discovery service completes, the annotations are published to
the metadata repository as an open discovery analysis report.

The open discovery analysis report is linked to the Asset that was analysed
(see AssetDiscoveryReport relationship),
to the discovery engine that ran the discovery service (see DiscoveryEngineReport relationship) and the
discovery service description itself (see DiscoveryInvocationReport relationship)
so it is possible to navigate to the report from different
perspectives.

![UML](0605-Open-Discovery-Analysis-Reports.png#pagewidth)

The **OpenDiscoveryAnalysisReport** entity is the report header.
It identifies the date of the report and the parameters used.
It may also include a name and description that is supplied
by the initiator of the discovery service run.

Return to [Area 6](Area-6-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
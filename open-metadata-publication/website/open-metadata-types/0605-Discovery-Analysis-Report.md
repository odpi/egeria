<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0605 Discovery Analysis Report

Each time a discovery service runs it creates a collection
of annotations.
These annotations are managed in a cache by the ODF
to allow later steps in the discovery service to access
annotations from the previous steps.
When the service completes, the annotations are published to
the metadata repository as a discovery analysis report.
Notice they are linked both to the server and the
discovery service since a discovery service may be deployed
to multiple metadata discovery servers.

![UML](0605-Discovery-Analysis-Report.png)

The **DiscoveryAnalysisReport** is the report header.
It identifies the date of the report and the parameters used.
It may also include a name and description that is supplied
by the initiator of the discovery service run.

The **DiscoveryServerReport** links the report to the metadata
discovery server that ran the discovery service and the
DiscoveryServiceReport links it to the discovery service.
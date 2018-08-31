<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Area 6 Models - Metadata Discovery

Area 6 provides structures for recording the results of
automated metadata discovery as annotations to
associated assets in
the metadata repository.
Metadata discovery requires different types of analysis.
This analysis may run just once, say when the asset is created,
on demand or based on an event or schedule.

A particular type of analysis is implemented in a
discovery service.
Within the discovery service are one to many discovery steps.
Each step performs some sort of analysis that may result in an
annotation for one or more assets.

The annotations from a particular run of a discovery
service are grouped together into a discovery analysis report.
The annotations may be reviewed and approved by a steward.
The steward may convert the annotation to a harden metadata type,
or they may flag the annotation as invalid.
When the discovery service is rerun, the new annotations
can be matched to the annotations from the previous run.
The steward's actions will impact how the new annotations are processed.

* **[0601 Metadata Discovery Server](0601-Metadata-Discovery-Server.md)**
* **[0605 Discovery Analysis Report](0605-Discovery-Analysis-Report.md)**
* **[0610 Annotation](0610-Annotations.md)**
* **[0611 Annotation Reviews](0611-Annotation-Reviews.md)**
* **[0615 Schema Extraction](0615-Schema-Extraction.md)**
* **[0620 Profiling Annotation](0620-Profiling-Annotation.md)**
* **[0626 Semantic Discovery](0626-Semantic-Discovery.md)**
* **[0630 Relationship Discovery](0630-Relationship-Discovery.md)**
* **[0635 Classification Discovery](0635-Classification-Discovery.md)**
* **[0650 Measurements](0650-Measurements.md)**
* **[0660 Request for Action](0660-Request-for-Action.md)**

Egeria's [Open Discovery Framework (ODF)](../../../open-metadata-implementation/frameworks/open-discovery-framework/README.md)
that supports the development and execution of discovery services.
The ODF runs in an open metadata discovery server.
ODF discovery services use connectors from the
[Open Connector Framework (OCF)](../../../open-metadata-implementation/frameworks/open-connector-framework)
to connect to the data assets and access the known
metadata about them. 
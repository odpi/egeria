<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Area 6 Models - Metadata Discovery

Area 6 provides structures for recording the results of
automated metadata discovery as annotations to associated assets in
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

* **[0601 Open Discovery Engines and Services](0601-Open-Discovery-Engine.md)**
* **[0605 Discovery Analysis Report](0605-Open-Discovery-Analysis-Reports.md)**
* **[0610 Annotation](0610-Annotations.md)**
* **[0611 Annotation Reviews](0612-Annotation-Reviews.md)**
* **[0615 Schema Extraction](0615-Schema-Extraction.md)**
* **[0617 Data Field Analysis](0617-Data-Field-Analysis.md)**
* **[0620 Data Profiling](0620-Data-Profiling.md)**
* **[0625 Data Class Discovery](0625-Data-Class-Discovery.md)**
* **[0630 Semantic Discovery](0630-Semantic-Discovery.md)**
* **[0635 Classification Discovery](0635-Classification-Discovery.md)**
* **[0640 Quality Scores](0640-Quality-Scores.md)**
* **[0650 Relationship Discovery](0650-Relationship-Discovery.md)**
* **[0655 Asset Deduplication](0655-Asset-Deduplication.md)**
* **[0660 Measurements](0660-Data-Source-Measurements.md)**
* **[0690 Request for Action](0690-Request-for-Action.md)**

![UML Packages](area-6-discovery-overview.png#pagewidth)

Egeria's [Open Discovery Framework (ODF)](../../../open-metadata-implementation/frameworks/open-discovery-framework/README.md)
that supports the development and execution of discovery services.
The ODF runs in an open metadata discovery server.
ODF discovery services use connectors from the
[Open Connector Framework (OCF)](../../../open-metadata-implementation/frameworks/open-connector-framework)
to connect to the data assets and access the known
metadata about them. 

Return to [Overview](README.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
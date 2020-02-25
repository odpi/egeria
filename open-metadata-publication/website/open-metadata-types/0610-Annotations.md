<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0610 Annotations

The **Annotation** entity captures the discovered characteristics of an asset.

Annotations are created by the analysis steps in the discovery service.
The attributes of the annotation capture the details of the discovery processing.
The sub-classes of Annotation capture specific details of the discovered metadata.
Each annotation is linked the the discovery analysis report it was generated from.
It also links to each asset that the annotation relates to.
Strings are used in many of the attributes to keep the model open
for discovery service developers and the tools that process them.

![UML](0610-Annotations.png#pagewidth)

* **annotationType** - descriptive string that acts as an identifier for the specific annotation type.  This is a simple means to sub-type any one of the annotation subclasses.
* **summary** - a human readable string to describe the annotation.
* **confidence** - an indicator of the certainty that the annotation is correct.
* **expression** - this attribute is used to provide more detail on how the asset is related to the annotation.
* **explanation** - another description field to assist human analysts reviewing the discovery results.
* **analysisStep** - identifier of the step in the discovery service that detected the annotation.
* **jsonProperties** - the properties that were used to initiate the discovery service.

The types that follow provide more specialized annotations.

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

Return to [Area 6](Area-6-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
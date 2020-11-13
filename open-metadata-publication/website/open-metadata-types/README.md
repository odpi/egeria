<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# The open metadata type system

Knowledge about data is spread amongst many people and systems.
One of the roles of a metadata repository is to provide a place
where this knowledge can be collected and correlated in as automated fashion as possible.
To enable many different tools and processes to populate the metadata repository we need
agreement on what data should be stored and in what format (structures). 

Figure 1 shows the different areas of metadata that a metadata repository needs
to support a wide range of metadata management and governance tasks.

![Figure 1: Common metadata areas](Figure-1-Open-Metadata-Areas.png#pagewidth)
> Figure 1: Common metadata areas

* **[Area 0](Area-0-models.md)** describes base types and infrastructure.  This includes types for Asset, DataSet, Infrastructure, Process, Referenceable, Server and Host.
* **[Area 1](Area-1-models.md)** collects information from people using the data assets.  It includes their use of the assets and their feedback.  It also manages crowd-sourced enhancements to the metadata from other areas before it is approved and incorporated into the governance program.
* **[Area 2](Area-2-models.md)** describes the data assets.  These are the data sources, APIs, analytics models, transformation functions and rule implementations that store and manage data.  The definitions in Area 2 include connectivity information that is used by the open connector framework (and other tools) to get access to the data assets.
* **[Area 3](Area-3-models.md)** describes the glossary.  This is the definitions of terms and concepts and how they relate to one another.  Linking the concepts/terms defined in the glossary to the data assets in Area 2 defines the meaning of the data that is managed by the data assets.  This is a key relationship that helps people locate and understand the data assets they are working with.
* **[Area 4](Area-4-models.md)** defines how the data assets should be governed.  This is where the classifications, policies and rules are defined.
* **[Area 5](Area-5-models.md)** is where standards are established.  This includes data models, schema fragments and reference data that are used to assist developers and architects in using best practice data structures and valid values as they develop new capabilities around the data assets.
* **[Area 6](Area-6-models.md)** provides the additional information that automated metadata discovery engines have discovered about the data assets.  This includes profile information, quality scores and suggested classifications.
* **[Area 7](Area-7-models.md)** provides the structures for recording lineage.


Figure 2 provides more detail of the metadata structures in each area.

![Figure 2: Metadata detail within the metadata areas](Figure-2-Open-Metadata-Areas-Detail.png#pagewidth)
> Figure 2: Metadata detail within the metadata areas

Within each area, the definitions are broken down into numbered packages to help identify
groups of related elements.
The numbering system relates to the area that the elements belong to.
For example, area 1 has models 0100-0199, area 2 has models 0200-299, etc.
Each area's sub-models are dispersed along its range, ensuring there is space to
insert additional models in the future.


Return to [Egeria Overview](../../../index.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
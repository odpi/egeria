<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0461 Governance Engines

A governance engine is a software server capability that is able to run specific services
on demand.  These services, called governance services, typically implement specific logic that
is needed to govern an organization's resources or the metadata associated with them.

Open metadata recognizes two types of governance engine:

* Governance action engines and services support the active governance of metadata and the resources they represent.
  There are different types of governance action engines/services that are defined by
  the [Governance Action Framework (GAF)](../../../open-metadata-implementation/frameworks/governance-action-framework).

* [Discovery engines and services](0601-Open-Discovery-Engine.md) support the analysis of the real world resources.  The results
  of this analysis are stored in a [discovery analysis report](0605-Open-Discovery-Analysis-Reports.md) chained off of the
  corresponding [Asset](0010-Base-Model.md) metadata element.
  The interfaces for discovery are found in the 
  [Open Discovery Framework (ODF)](../../../open-metadata-implementation/frameworks/open-discovery-framework).

![UML](0461-Governance-Engines.png#pagewidth)

The [Open Metadata Engine Services (OMES)](../../../open-metadata-implementation/engine-services) support the
implementation of each type of governance engine.
They run in an [Engine Host](../../../open-metadata-implementation/admin-services/docs/concepts/engine-host.md)
OMAG Server and draw their configuration from the 
**GovernanceEngine** and the linked **GovernanceService** elements in the associated metadata server.

----

Return to [Area 4](Area-4-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
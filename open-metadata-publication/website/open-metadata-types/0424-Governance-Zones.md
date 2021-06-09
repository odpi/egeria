<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0424 Governance Zones

A **GovernanceZone** describes a collection of Assets that are used, or processed in a specific way.
The governance zone definitions define the governance zones in use in the organization and
how they are used.  It is possible to then attach governance policies and controls to the zone
definitions using the [GovernedBy](0401-Governance-Definitions.md) relationship to show how assets assigned
to a zone should be managed and governed.

Linking the zones in a hierarchy implies that the governance definitions linked to a zone that is higher in
the hierarchy also apply to all governance zones linked underneath it.

An Asset may belong to many Governance Zones.  This is defined in the **AssetZoneMembership** classification.
A classification is used rather than a relationship between Asset and GovernanceZoneDefinition to improve
the performance of the asset processing since the classification flows with the Asset.

![UML](0424-Governance-Zones.png#pagewidth)

There is more information on governance zones
[here](../../../open-metadata-implementation/access-services/docs/concepts/governance-zones).

Return to [Area 4](Area-4-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
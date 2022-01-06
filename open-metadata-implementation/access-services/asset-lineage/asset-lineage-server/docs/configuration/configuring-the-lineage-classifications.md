<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Configuring the Lineage Classifications

The Asset Lineage OMAS consumes the events related to the Classifications for lineage entity.
If there is no lineage classifications list provided when the access service is enabled, 
it will use the default one described in [here](../../../asset-lineage-api/docs/events/lineage-event.md).

A custom list of classifications can be set up in the
`LineageClassificationTypes` property, in the Asset Lineage OMAS's options map. The given values will be merged
with the default ones: Confidentiality, AssetZoneMembership, SubjectArea, AssetOwnership, PrimaryCategory, Incomplete.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
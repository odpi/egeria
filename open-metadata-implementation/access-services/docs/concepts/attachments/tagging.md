<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Tagging

Tagging is the process of adding labels to a resource such as an
asset to group related resources, add additional descriptive
information or to make them easier to find.

There are 2 main types of tagging:

* [Informal Tags](informal-tags.md) - adds descriptive labels to resources.
  Informal tags can be used to mine user knowledge about a topic.
  Any user can create, update, delete and assign/unassign public informal tags to resources.
  Private informal tags can only be seen, managed and used by the user that created them.

  Support for informal tags is found in the following services:

  * [Asset Consumer OMAS](../../../asset-consumer) - informal tagging of assets
  * [Community Profile OMAS](../../../community-profile) - informal tagging of personal profiles, collaboration content and community content.

* [Security Tags](security-tags.md) - provides a label, typically to a data field,
  that indicates what the sensitivity of the data stored in that field is.
  Security tags are tightly controlled, both in how they are defined and how they are used.

  Security tags are defined and managed through the following OMASs:
  
  * [Governance Program OMAS](../../../governance-program) - definition of security tags.
  * [Security Officer OMAS](../../../security-officer) - management of auto-assigned security tags.
  * [Asset Owner OMAS](../../../asset-owner) - manual assignment of security tags.
  * [Governance Engine OMAS](../../../governance-engine) - distribution of security tags to security enforcement engines.



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
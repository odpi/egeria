<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Cohort Registry Store Connectors

The cohort registry store connectors are connectors that store the
[open metadata repository cohort](../../../../repository-services/docs/open-metadata-repository-cohort.md)
membership details used and maintained by the [cohort registry](../../../../repository-services/docs/component-descriptions/cohort-registry.md).
The cohort protocols are peer-to-peer and hence there is a cohort registry
(with a [cohort registry store](../../../../repository-services/docs/component-descriptions/connectors/cohort-registry-store-connector.md))
for each [member of a cohort](../../../../admin-services/docs/concepts/cohort-member.md).

Egeria provides a single implementation of a
cohort registry store connector:

* **[cohort-registry-file-store-connector](cohort-registry-file-store-connector)** - provides the means to store
the cohort registry membership details as a JSON file.


----
Return to [repository-services-connectors](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

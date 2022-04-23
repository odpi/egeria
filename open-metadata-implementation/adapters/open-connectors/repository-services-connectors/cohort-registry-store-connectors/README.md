<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Cohort Registry Store Connectors

The cohort registry store connectors are connectors that store the
[open metadata repository cohort](https://egeria-project.org/concepts/cohort-member)
membership details used and maintained by the [cohort registry](https://egeria-project.org/concepts/cohort-registry).
The cohort protocols are peer-to-peer and hence there is a cohort registry
(with a [cohort registry store](https://egeria-project.org/concepts/cohort-registry-store-connector))
for each [member of a cohort](https://egeria-project.org/concepts/cohort-member).

Egeria provides a single implementation of a
cohort registry store connector:

* **[cohort-registry-file-store-connector](cohort-registry-file-store-connector)** - provides the means to store
the cohort registry membership details as a JSON file.


----
Return to [repository-services-connectors](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Cohort Registry

The OMRS **Cohort Registry** resides in each
**[cohort member](../cohort-member.md)**.
It is responsible for registering a member
with a specific **[open metadata repository cohort](../open-metadata-repository-cohort.md)**
and maintaining a list of the other members of this cohort.

The registration process is managed by exchanging [Registry Events](../event-descriptions/registry-events.md)
over the [Cohort Topic](../omrs-event-topic.md).

The cohort registry maintains its record of the membership of the cohort in a
[Cohort Registry Store](../component-descriptions/connectors/cohort-registry-store-connector.md).

## Further information

* [Configuring the cohort registry in an OMAG Server](https://egeria-project.org/concepts/cohort-member)
* [Overview of a cohort](../open-metadata-repository-cohort.md)


----
* Return to [repository services component descriptions](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Cohort member

A cohort member is an [OMAG server](https://egeria-project.org/concepts/omag-server) that is configured to register
with at least one **[Open Metadata Repository Cohort](open-metadata-repository-cohort.md)**.
This registration enables the server to exchange metadata with other cohort members either through the
cohort topics or via federated queries that make use of the cohort member's local repository REST services.

The management of a server's membership of the cohort is handled by the
[Cohort Services](subsystem-descriptions/cohort-services.md).
During server start up, the repository services detects the configuration of at least one cohort and starts
the [Metadata Highway Manager](component-descriptions/metadata-highway-manager.md).
The metadata highway manager creates a [Cohort Manager](component-descriptions/cohort-manager.md) for
each cohort configuration.
The cohort manager creates a [Cohort Registry](component-descriptions/cohort-registry.md) that is responsible for the
exchange of messages called [Registry Events](event-descriptions/registry-events.md).


## Further Information

* [Configuring a cohort member](https://egeria-project.org/concepts/cohort-member)
* [REST API showing the status of a cohort member](component-descriptions/omrs-rest-services.md)

----
* Return to [Repository Services Design](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.


<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Open Metadata Engine Services (OMES)

An engine service provides the specialist methods to run instances of a specific type of [governance service](https://egeria-project.org/concepts/governance-service).  For example, when a request is made to run a governance service, the engine service is called to create the specialized context for the service, create, set up and run the service instance, managing its completion, and any failures it suffers.   

There are 4 specialist engine services, one for each type of governance service.

* **[Governance Action OMES](governance-action)** - provides support for the governance action engines.
* **[Survey Action OMES](survey-action)** - provides support for survey action engines.
* **[Watchdog Action OMES](watchdog-action)** - provides support for watchdog action engines.
* **[Repository Governance OMES](repository-governance)** - provides support for the repository governance engines.

These services run in the [Engine Host OMAG Server](https://egeria-project.org/concepts/engine-host).

----
Return to [open-metadata-implementation](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
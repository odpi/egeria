<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata and Governance Subsystems

A subsystem is a collection of components
within a software server that supports one or more related services.
Subsystems can be organized in a hierarchy where course-grained subsystems can be
decomposed into more fine grained subsystems.

The [OMAG Server](omag-server.md) is a flexible software server whose subsystems
can be activated (or not) through the presence (or absence) of the subsystem's configuration
properties in the OMAG server's [configuration document](configuration-document.md).

The potential subsystems within an [OMAG Server](omag-server.md) are as follows:

* **[Open Metadata Repository Services (OMRS)](../../../repository-services)** for supporting access
  to metadata stored in metadata repositories and the exchange of metadata between repositories
  via an [open metadata repository cohort](../../../repository-services/docs/open-metadata-repository-cohort.md).
  The repository services are further divided into five subsystems that can be activated independently.
  
  * **[Enterprise Repository Services](../../../repository-services/docs/subsystem-descriptions/enterprise-repository-services.md)** provides a virtual
  metadata repository by combining the content of multiple open metadata
  repositories and delivering this metadata through a single API and event topic.
  The Enterprise Repository Services provide the enterprise access metadata support for the OMASs.
  
  * **[Administration Services](../../../repository-services/docs/subsystem-descriptions/administration-services.md)** drive the
  initialization of the OMRS at server startup, provide access to the OMRS's internal status and
  coordinate the orderly termination of OMRS when the open metadata services
  are deactivated.
  
  * **[Cohort Services](../../../repository-services/docs/subsystem-descriptions/cohort-services.md)** manage the local
  server's membership in one or more open metadata repository cohorts.
  
  * **[Local Repository Services](../../../repository-services/docs/subsystem-descriptions/local-repository-services.md)** manage the local
  server's open metadata repository.

  * **[Event Management Services](../../../repository-services/docs/subsystem-descriptions/event-management-services.md)** manage the flow of OMRS Events
  between the other OMRS subsystems.

* **[Open Metadata Access Services (OMAS)](../../../access-services)** for supporting domain-specific services
  for metadata access and governance.  Each access service is its own subsystem and can be activated independently
  to match the the needs of the environment that the OMAG Server is supporting.
  
* **[Discovery Engine Services](../../../governance-servers/discovery-engine-services)** for running automated metadata discovery services in a discovery engine.
  These services are the principle services of the discovery server.

* **[Stewardship Engine Services](../../../governance-servers/stewardship-engine-services)** for managing and resolving issues that are detected in the assets being governed by Egeria.
  These services are the principle services of the stewardship server.
  
* **[Security Sync Services](../../../governance-servers/security-sync-services)** for maintaining metadata and configuration in an security enforcement engine.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
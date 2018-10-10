<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMRS Subsystem Descriptions

The open metadata repository services is divided into four subsystems:

* **[Enterprise Repository Services](enterprise-repository-services.md)** provides a virtual
metadata repository by combining the content of multiple open metadata
repositories and delivering this metadata through a single API and event topic.
The Enterprise Repository Services provide the enterprise access metadata
support for the OMASs.

* **[Administration Services](administration-services.md)** drive the
initialization of the OMRS at server startup, provide access to the OMRS's internal status and
coordinate the orderly termination of OMRS when the open metadata services
are deactivated. OMRS's administration services are called by the server's administration
services.   It is supplied with configuration information including:
  * Connections for the connectors it should use.
  * Information about the local repository (if any).
  * Whether the enterprise repository services should be initialized.
  * Details of any cohorts it should join.

* **[Cohort Services](cohort-services.md)** manage the local
server's membership in one or more open metadata repository cohorts.

* **[Local Repository Services](local-repository-services.md)** manage the local
server's open metadata repository.
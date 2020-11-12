<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Repository Services (OMRS) Implementation

The Open Metadata Repository Services (OMRS) implementation
contains the support for the peer-to-peer metadata exchange and federation.

Its design is documented **[here](../docs/README.md)**.

The code is organized into the following packages under 'org.odpi.openmetadata.repositoryservices':

* **admin**: support for the admin calls from the admin services that control the 
start up, operational management and shutdown of the repository services.

* **archivemanager**: support for open metadata archives and the default set of open metadata types.

* **enterprise**: implementation of the [enterprise repository services](../docs/subsystem-descriptions/enterprise-repository-services.md)
that enable the [access services (OMAS)](../../access-services/README.md) to issue metadata requests across
the [open metadata cohorts](../docs/open-metadata-repository-cohort.md).

* **eventmanagement**: implementation of the [event management services](../docs/subsystem-descriptions/event-management-services.md)
that manage the exchange of [OMRS events](../docs/omrs-event-topic.md) between the repository services subsystems and the
[open metadata cohorts](../docs/open-metadata-repository-cohort.md).

* **localrepository**: implementation of the [local repository services](../docs/subsystem-descriptions/local-repository-services.md)
that support a local metadata repository.  This also supports common services for open metadata
repository connectors such as the
repository helper and repository validator services that are backed by the repository content manager.

* **metadatahighway**: implementation of the [cohort services](../docs/subsystem-descriptions/cohort-services.md)
that enable the server to exchange metadata with other servers.

* **rest**: support for the repository services REST API.

The implementation of the objects used on its APIs and in its Events are located
in the [repository-services-apis](../repository-services-apis) module, and an implementation of the REST API calls
using Spring annotations is located in the [repository-services-spring](../repository-services-spring) module.

----
* Return to [repository-services](..)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
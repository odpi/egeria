<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Local Repository Services

The local repository services manages the interaction with the local server's
metadata collection (stored in the local repository).  They include the following
components:

* **[Local OMRS Repository Connector](../component-descriptions/local-repository-connector.md)** - Implements the OMRS Repository Connector interface that supports access to the local metadata repository.
  * Local OMRS Connector Provider - The OCF Connector Provider factory for the Local OMRS Repository Connector.
  * Local OMRS Metadata Collection - Manages metadata requests for the local repository.
* **[Local OMRS Repository Content Manager](../component-descriptions/typedef-manager.md)** - Provides an in-memory cache of open metadata type definitions
(TypeDefs) that are used for validating of TypeDefs from other open metadata repositories and creation of new open metadata instances (entities and relationships).
* **[Local OMRS Instance Event Processor](../component-descriptions/local-repository-instance-event-processor.md)** - Processes inbound Instance Events on behalf of the local repository.
These events may come from one of the connected open metadata repository cohorts or the OMRS Archive Manager.
* **[OMRS REST Repository Services](../component-descriptions/omrs-rest-services.md)** - Implements the server-side of the In-memory OMRS Repository Connector.
* **[OMRS REST Repository Connector](../component-descriptions/rest-repository-connector.md)** - Implements the OMRS Repository Connector
interface that supports metadata access to a remote open metadata repository service via the OMRS Repository REST API.
  * OMRS REST Connector Provider - The OCF Connector Provider factory for the OMRS REST Repository Connector.
  * OMRS REST Metadata Collection - Manages calls to the OMRS REST Repository Services in a remote open metadata repository.



----
* Return to [repository services subsystem descriptions](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

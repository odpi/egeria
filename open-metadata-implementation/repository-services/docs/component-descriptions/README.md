<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Repository Services (OMRS) components

## Repository Services APIs

* **[Audit Log](audit-log.md)** - Reads and writes audit log messages.
* **[Connectors](connectors/README.md)** - Provides the plug points for connector implementations.
* **[Events](../event-descriptions/README.md)** - Event formats that flow over the cohort.
* **[Exceptions](../exception-descriptions/README.md)** - Specific OMRS Exceptions used on the Java APIs.
* **[REST Properties](../../repository-services-spring)** - Property beans used on OMRS's REST API.

## Repository Services Implementation

### Administration
 
* **[Operational Services](operational-services.md)** - Supports the administration services for the Open Metadata Repository Services (OMRS).
* **[Configuration Factory](configuration-factory.md)** - Generates default values for the Open Metadata Repository Services (OMRS) configuration.
* **[Archive Manager](archive-manager.md)** - Reads and loads the content from open metadata archives.

### Event Management

* **[OMRS Repository Event Manager](event-manager.md)** - Manages the distribution of repository events
(TypeDef and Instance Events) within the local server's OMRS components.
* **[OMRS Event Listener](event-listener.md)** - Receives Registry and Repository (TypeDef and Instance)
events from the OMRS Topic for a cohort.
* **[OMRS Event Publisher](event-publisher.md)** - Sends Registry and Repository (TypeDef and Instance)
events to the OMRS Topic.
This may be the OMRS Topic for a cohort, or the OMRS Topic used by the Open Metadata Access Services (OMAS).

### Enterprise Repository Services
 
* **[Enterprise Connector Manager](enterprise-connector-manager.md)** - Manages the list of open metadata repositories
that the Enterprise OMRS Repository Connector should call to retrieve an enterprise view of the metadata collections
supported by these repositories.
* **[Enterprise Repository Connector](enterprise-repository-connector.md)** - Supports federated queries.
  * Enterprise OMRS Connector Provider - The OCF Connector Provider factory for the Enterprise OMRS Repository Connector.
  * Enterprise OMRS Repository Connector - Implements the OMRS Repository Connector interface that supports enterprise
  access to the list of open metadata repositories registered with the OMRS Enterprise Connector Manager.
  * Enterprise OMRS Metadata Collection - Manages calls to the list of open metadata repositories
  registered with the OMRS Enterprise Connector Manager on behalf of the Enterprise OMRS Repository Connector.
  * Enterprise OMRS Connector Properties - Provides the connected asset properties for the Enterprise OMRS Repository Connector.

### Local Repository Services

* **[Local OMRS Repository Connector](local-repository-connector.md)** - Implements the OMRS Repository Connector interface that supports access to the local metadata repository.
  * Local OMRS Connector Provider - The OCF Connector Provider factory for the Local OMRS Repository Connector.
  * Local OMRS Metadata Collection - Manages metadata requests for the local repository.
* **[Local OMRS Repository Content Manager](typedef-manager.md)** - Provides an in-memory cache of open metadata type definitions
(TypeDefs) that are used for validating of TypeDefs from other open metadata repositories and creation of new open metadata instances (entities and relationships).
* **[Local OMRS Instance Event Processor](local-repository-instance-event-processor.md)** - Processes inbound Instance Events on behalf of the local repository.
These events may come from one of the connected open metadata repository cohorts or the OMRS Archive Manager.
* **[OMRS REST Repository Services](omrs-rest-services.md)** - Implements the server-side of the In-memory OMRS Repository Connector.
* **[OMRS REST Repository Connector](rest-repository-connector.md)** - Implements the OMRS Repository Connector
interface that supports metadata access to a remote open metadata repository service via the OMRS Repository REST API.
  * OMRS REST Connector Provider - The OCF Connector Provider factory for the OMRS REST Repository Connector.
  * OMRS REST Metadata Collection - Manages calls to the OMRS REST Repository Services in a remote open metadata repository.

### Cohort Services

* **[OMRS Metadata Highway Manager](metadata-highway-manager.md)** - Manages the OMRS Cohort Manager for each
open metadata repository cohort that the local server belongs to.
* **[OMRS Cohort Manager](cohort-manager.md)** - Manages the components needed in the local server for
it to act as a member of an open metadata repository cohort.
* **[OMRS Cohort Registry](cohort-registry.md)** - Manages registration exchanges with other members of a
cohort on behalf of the local server.

----
Return to [repository services design](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.



<!-- SPDX-License-Identifier: Apache-2.0 -->

# Repository Connector

The OMRS Repository Connector API defines a call
interface to create, search, query, update and
delete metadata stored in a metadata repository.
The implementation of a specific OMRS connector
determines which type(s) of metadata repository it is able to access.

The OMRS has three repository connector implementations that form part of the core open metadata capability:

* **[Enterprise Repository Connector](../enterprise-repository-connector.md)** -
	This connector can issue calls to multiple OMRS connectors and aggregate the
results as if the metadata was stored in a single repository.
This is how metadata queries are federated across open metadata repositories.  

	Since all implementations of OMRS repository connectors have the same API,
the Enterprise Repository Connector is able to work with
a heterogeneous collection of repositories (see figure 1 below).

* **[Local OMRS Repository Connector](../local-repository-connector.md)** - 
This connector wraps a "real" repository connector (see below) and manages
events and validation for this connector.

* **[OMRS REST Repository Connector](../../../../adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/omrs-rest-repository-connector)** -
	This is the connector used by the Enterprise Repository Connector to make
a direct call to another open metadata repository through
the [OMRS REST API](../omrs-rest-services.md). 

	These are the "real" OMRS Repository Connector implementations that provide open metadata access
to specific types of metadata repositories.
The Atlas connector is shipped in Apache Atlas since it is hosted natively.
The rest are part of Egeria.

* **[Local Atlas Repository Connector](https://issues.apache.org/jira/browse/ATLAS-1773)** -
This is the connector that runs locally in an Apache Atlas metadata repository, pulling in the key parts of
Egeria it needs to support the open metadata standards.
It calls directly to Apache Atlas's internal interface for the metadata repository
and as such is always deployed in an Apache Atlas Server.

* **IGC OMRS Repository Connector** -
This is the connector for retrieving metadata from IBM's Information Governance Catalog.
This connector translates the calls to its OMRS Connector API to IGC's REST API and
then translates the results of these calls to appropriate responses on its API.
This connector runs as the local repository in an OMAG Server.

* **[In-memory OMRS Repository Connector](../../../../adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/inmemory-repository-connector)** -
This connector provides a simple in-memory repository for testing/demos or
small-scale environments where metadata is being managed remotely and cached locally.

* **[Graph OMRS Repository Connector](../../../../adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/graph-repository-connector)** -
This connector is a placeholder for an open graph repository implementation such as
with JanusGraph.


Every OMRS repository connector supports the Open Connector Framework (OCF).
This means that they use connections to define the network address
and user credentials necessary to access a specific instance of a metadata repository.
Figure 1 shows some potential patterns for the use of OMRS Repository Connectors
to access different types of metadata repositories.
The letters above each pattern (A, ..., D) refer to the notes below the diagram.

![Figure 1: OMRS Repository Connector Call Path Patterns](repository-connector-call-paths.png)
> Figure 1: OMRS Repository Connector Call Path Patterns

* A - The **[Local Atlas Repository Connector](https://issues.apache.org/jira/browse/ATLAS-1773)**
is hosted in the Apache Atlas server and calls the local internal metadata repository API.
Every Atlas server will have one of these connectors.
* B	- The **[OMRS REST Repository Connector](../../../../adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/omrs-rest-repository-connector)** 
translates calls to its interface into calls to the [OMRS REST API](../omrs-rest-services.md) of a remote [open metadata repository](../../open-metadata-repository.md).
* C - Other metadata repositories that have their own API can create a connector
to translate OMRS calls to call to their own API. The IGC OMRS Repository
Connector from the [virtual-data-connector demo](../../../../../open-metadata-resources/open-metadata-demos/virtual-data-connector) is an example of this type
of OMRS connector.
* C - Other metadata repositories that have their own API can create a connector to translate OMRS calls to call to their API.  The IGC OMRS Repository Connector is an example of this type of OMRS connector written to support a specific metadata repository that is not Apache Atlas.  This OMRS Connector will be included in the Apache Atlas build so it can be called directly from the Enterprise OMRS Connector rather than via a remote REST call.
* D - The **[Enterprise Repository Connector](../enterprise-repository-connector.md)** converts each
call to its repository connector interface to a call to each of the OMRS repository
connectors that it is configured to work with 
(see **[Enterprise Connector Manager](../enterprise-connector-manager.md)**).
It then aggregates the results together to form the response to its call.

Together the OMRS Repository Connectors provide a flexible way to make calls to many
types of metadata repositories.  


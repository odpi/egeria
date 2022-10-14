<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMRS Repository Connector

Repository Connectors make use of the Egeria [meta-model](../../metadata-meta-model.md) to represent and communicate metadata.

The OMRS Repository Connector API defines a call
interface to create, search, query, update and
delete metadata stored in a metadata repository.
The implementation of a specific OMRS connector
determines which type(s) of metadata repository it is able to access.

The OMRS has three repository connector implementations that form part of the
core open metadata capability for a [cohort member](https://egeria-project.org/concepts/cohort-member):

* **[Enterprise Repository Connector](../enterprise-repository-connector.md)** -
	This connector can issue calls to multiple OMRS connectors and aggregate the
results as if the metadata was stored in a single repository.
This is how metadata queries are federated across open metadata repositories.  

	Since all implementations of OMRS repository connectors have the same API,
the Enterprise Repository Connector is able to work with
a heterogeneous collection of repositories.

* **[Local OMRS Repository Connector](../local-repository-connector.md)** - 
This connector wraps a "real" repository connector (see below) and manages
events and validation for this connector.

* **[OMRS REST Repository Connector](../../../../adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/omrs-rest-repository-connector)** -
	This is the connector used by the Enterprise Repository Connector to make
a direct call to another open metadata repository through
the [OMRS REST API](../omrs-rest-services.md). 

These are the "real" OMRS Repository Connector implementations that provide open metadata access
to specific types of metadata repositories.

* **[Apache Atlas Repository Connector](https://github.com/odpi/egeria-connector-hadoop-ecosystem)** -
This is the connector that runs in an Egeria [repository proxy](https://egeria-project.org/concepts/repository-proxy) server, pulling in the key parts of
Egeria it needs to support the open metadata standards.
It calls directly to Apache Atlas's REST API interface for the metadata repository.

* **[IGC OMRS Repository Connector](https://github.com/odpi/egeria-connector-ibm-information-server)** -
This is the connector for retrieving metadata from IBM's Information Governance Catalog (aka IGC).
This connector translates the calls to its OMRS Connector API to IGC's REST API and
then translates the results of these calls to appropriate responses on its API.
This connector also runs in a [repository proxy](https://egeria-project.org/concepts/repository-proxy) server.

* **[In-memory OMRS Repository Connector](../../../../adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/inmemory-repository-connector)** -
This connector provides a simple in-memory repository for testing/demos or
small-scale environments where metadata is being managed remotely and cached locally.
It has native support for the open metadata types an instances
and so runs in a [metadata access store](https://egeria-project.org/concepts/metadata-access-store).

* **[Graph OMRS Repository Connector](../../../../adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/graph-repository-connector)** -
This connector is provides a high functioning open metadata repository implementation
built on JanusGraph.
It also has native support for the open metadata types an instances
and so runs in a [metadata access store](https://egeria-project.org/concepts/metadata-access-store).


----
Return to [repository services connectors](.).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.


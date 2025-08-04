<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Released](../../../images/egeria-content-status-released.png#pagewidth)

# Open Connector Framework (OCF)

The OCF, as the name suggests, is an open framework for supporting connectors.
Connector provide client-side access to remote digital [Assets](https://egeria-project.org/concepts/asset)
such as data sets, APIs and software components.
OCF Connectors also provide access to metadata about the asset and they may call
the [Governance Action Framework (GAF)](https://egeria-project.org/frameworks/gaf/overview) to log audit messages and execute
appropriate governance actions related to the use of these assets
in real-time.

## Benefits of the OCF

Applications and tools benefit from using OCF connectors because:

* Network and security parameters for accessing the data resources are managed in the metadata repository as part of a named connection.
The application need only supply the identifier of the connection and provided they have the appropriate security credentials then a connector is returned to them for use. 
 
  * There is no need to hard code user ids and passwords in the application code - nor manage key stores for this sensitive information since the metadata repository handles this.
  * If the location of the data changes, then the named connection configuration is changed in the metadata repository and the application will be connected to the new location the next time they request a connector.

* The OCF connector provides two sets of APIs.  The first set provides access to the Asset contents and the second set provides access to the properties of the asset stored in the open metadata repositories.
This provides applications and tools with a simple mechanism to make use of metadata as they process about the Asset.
It is particularly useful for data science tools where these properties can help guide the end user in the use of the Asset.

* OCF connectors are not limited to representing Assets as they are physically implemented.
An OCF connector can represent a simplified logical (virtual) data resource that is designed for the needs of a specific application or tool.
This type of connector delegates the requests it receives to one or more physical data resources.  

Organizations benefit from advocating the use of OCF connectors for their systems because the OCF
connectors provide a consistent approach to governance enforcement and audit logging.
This is particularly important in data-rich environments where individuals are able to combine data
from different Assets creating new potentially sensitive insight.
The common approach to auditing, and the linkage between the data accessed and the metadata that describes its characteristics help to detect and prevent such actions

## Design rationale

The following factors influenced the design of the OCF.

* There are many existing connectors and connector framework in the industry today.
It is important that these existing connectors can be incorporated into the OCF.
Thus the OCF includes placeholders for adapters to external connector providers and connectors.

* Application developers will only adopt a connector framework if it is easy to use.
Thus the connector interfaces allow for the use of native data APIs to minimize the effort an application developer
has to take in order to use the OCF connectors.

* Governance enforcement is a complex topic, typically managed externally to the application development team.
As a result, a separate framework called the governance action framework (GAF) manages governance enforcement.
The role of the OCF is to bridge from the Asset access requests to the GAF where necessary.

* Access to the all properties known about an Asset should be available to the consumers of the Asset.
Therefore the OCF provides a standard interface for accessing these properties.
Different providers of these properties can plug into the OCF.
Egeria provides an implementation of this interface to supply Asset properties stored in open metadata repositories
in the [OCF Metadata Management modules](../../access-services/ocf-metadata-management) service.

## Terminology

There are a number of key components within the OCF:

* **Connector** -  this is a Java object for accessing an asset and its
related metadata and governance functions. See [docs](docs/concepts/connector.md) for more information.

* **Connection** - this is a Java object containing the properties needed to
create a connector instance. See [docs](docs/concepts/connection.md) for more information.

* **Connector Broker** - this is a generic factory for all OCF connectors.
See [docs](docs/concepts/connector-broker.md) for more information.

* **Connector Provider** - this is a factory for a specific type of connector.
It is used by the Connector Broker. See [docs](docs/concepts/connector-provider.md) for more information.

* **Connected Asset Properties** - this is the properties of the Asset that the connector is accessing.
It is hosted by a metadata server.  See [docs](docs/concepts/connected-asset-properties.md) for more information.

## Implementation scenarios

For further information on implementing can be found in the
[Developer's Guide](https://egeria-project.org/guides/developer/overview).


## Open Metadata Types

Open metadata repositories are able to store information needed to use OCF connectors.  Details of the types involved are as follows:

* [Model 0040](https://egeria-project.org/types/0/0040-Software-Servers) defines the structure of an Endpoint.
* [Model 0201](https://egeria-project.org/types/2/0201-Connectors-and-Connections) defines the structures for Connections and Connector Types.
* [Model 0205](https://egeria-project.org/types/2/0205-Connection-Linkage) defines the linkage between the connection and the connected asset.

## Java Implementation

The OCF provides the interface schema and base class implementation for these components.
The Java implementation is located in packages `org.odpi.openmetadata.ocf.*`:

* **org.odpi.openmetadata.frameworks.connectors** - Java interface and base classes for Connector and Connector Provider
plus the implementation of the Connector Broker.

* **org.odpi.openmetadata.frameworks.connectors.ffdc** - Implementation of the OCF's error codes and exceptions.

* **org.odpi.openmetadata.frameworks.connectors.properties.beans** - Implementation of the properties for connections and connected assets.
These are simple POJO objects.

* **org.odpi.openmetadata.frameworks.connectors.properties** - Implementation of the properties for connections and connected assets.
These are read only facades around the beans.

## Related Modules

The [OCF Metadata Management](https://egeria-project.org/services/ocf-metadata-management) supports the retrieval
of connection and connected asset properties from the open metadata
repository/repositories.

The [Asset Consumer OMAS](https://egeria-project.org/services/omas/asset-consumer/overivew) embeds the OCF to provide
client-side support for connectors.

The [Open Metadata Repository Services (OMRS)](https://egeria-project.org/services/omrs)
makes extensive use of OCF connectors for accessing open metadata repository servers and other resources.
These connectors are collectively called the [OMRS Connectors](../../repository-services/docs/component-descriptions/connectors).

Many of the [Open Metadata Governance Servers](https://egeria-project.org/concepts/governance-server) make use of OCF connectors to
loosely-couple integration with a variety of underlying technologies.

The [Developer Guide](https://egeria-project.org/guides/developer/overview) provides more
information on writing connectors for Egeria.

The [Connector Catalog](https://egeria-project.org/connectors) lists the
pre-built connectors supplied by Egeria.


----
Return to [frameworks](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
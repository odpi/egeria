<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Lineage Warehouse Services API

The Lineage Warehouse Services API module provides the property structures and
REST structures used to drive the open lineage.

- [Beans](docs/beans/README.md)
- [Exceptions](docs/exceptions/REAMDE.md)

This module also defines the interfaces that need to be implemented in order for a graph connector to integrate through OLS.

In essence these are the following:

- a graph connector provider, implemented by extending `LineageGraphProviderBase`.
- a graph connector, implemented by extending `LineageGraphConnectorBase`.

The methods in LineageGraphProviderBase need to be overridden to implement those set / retrieval operations according to the specifics of 
the graph database used. 
Currently, the only implementation available for the connector is the
[lineage-warehouse-janus-connector](../../../adapters/open-connectors/governance-daemon-connectors/lineage-warehouse-connectors/lineage-warehouse-janus-connector/README.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
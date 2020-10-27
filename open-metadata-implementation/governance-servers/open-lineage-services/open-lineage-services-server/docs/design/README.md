<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Lineage Services Design

### Open Lineage Server Operational Services

The Open Lineage Server Operational Services class provides the method to initialize the OLS governance server. It initializes the 
[open-janus-connector](../../../../../adapters/open-connectors/governance-daemon-connectors/open-lineage-connectors/open-lineage-janus-connector/README.md
) based on the configuration properties. 
 
### Open Lineage Server Audit Log

The [AuditLogFramework](../../../../../frameworks/audit-log-framework/README.md) is used to log the audit exceptions and messages in the governance
 server implementation.

### Open Lineage Server Handler
The handler for returning a lineage subgraph. It delegates to the 
[open-janus-connector](../../../../../adapters/open-connectors/governance-daemon-connectors/open-lineage-connectors/open-lineage-janus-connector/README.md)
implementation for retrieving the lineage information.

### Open Lineage Server Listener
OpenLineageInTopicListener receives details of each event published from the Asset Lineage Out Topic. 
It receives [Lineage Entity Events](../../../../../access-services/asset-lineage/asset-lineage-api/docs/events/README.md) and updates the lineage
 graph with the open metadata types and relationships needed for lineage.
 
### Open Lineage Server Scheduler
The OLS defines a LineageGraphJob that is scheduled to run at fixed intervals in the background. Its purpose is to create the corresponding nodes
 and relationships needed querying on the lineage graph

### Open Lineage Server
The REST interface exposes the lineage endpoint described [here](../user/README.md).

### Open Lineage Storing Services
The StoringServices class define methods for creating and updating nodes in the graph. It delegates to the 
[open-janus-connector](../../../../../adapters/open-connectors/governance-daemon-connectors/open-lineage-connectors/open-lineage-janus-connector/README.md)
implementation for storing the lineage information.
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
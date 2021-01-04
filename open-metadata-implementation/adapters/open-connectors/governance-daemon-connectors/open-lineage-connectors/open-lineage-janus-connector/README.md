<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Open Lineage Janus Connector

The Open Lineage Janus connector allows the Open Lineages Services to connect with a JanusGraph database.

The Connector can be configured to connect to either an embedded JanusGraph database or a standalone JanusGraph server. 
For more details on possible configurations please use the documentation [offered by JanusGraph](https://docs.janusgraph.org/basics/deployment/).  
   
Configuring the properties of the JanusGraph client when connecting to the database should be done following the [official documentation](https://docs.janusgraph.org/basics/configuration/).
   
--
- Embedded JanusGraph

In order to configure the connector with an embedded JanusGraph lineageGraphConnection configuration should be configured with the following `connectorProviderClassName`:  

```
 "connectorProviderClassName" : "org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph.LineageGraphConnectorProvider"       
```

Example: 
 ```
"lineageGraphConnection": {
    "class": "Connection",
    "displayName": "Lineage Graph Connection",
    "description": "Used for storing lineage in the Open Metadata format",
    "connectorType": {
        "class": "ConnectorType",
        "connectorProviderClassName": "org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph.LineageGraphConnectorProvider"
    },
   "configurationProperties": {
        "gremlin.graph": "org.janusgraph.core.JanusGraphFactory",
        "storage.backend": "berkeleyje",
        "storage.directory": "./egeria-lineage-repositories/lineageGraph/berkeley",
        "index.search.backend": "lucene",
        "index.search.directory": "./egeria-lineage-repositories/lineageGraph/searchindex"
    }
}
```

The above example configures the server with an embedded JanusGraph that uses BerkeleyDB as the storing solution and Lucene for the indexing. 
  
If you are deploying into a container environment and storing to file, ie via BerkeleyDB, it is recommended the storage directory is mapped into something like './data/servers/cocoLineage/lineageGraph' or similar
and that you ensure './data' or a subdirectory mapped to a mapped volume or k8s pvc. This follows the default pattern used
elsewhere in Egeria.

---
 - Standalone JanusGraph server 
 
In order to have the client connect to a standalone database, the following configuration needs to be provided:
  
```
 "connectorProviderClassName" : "org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph.LineageGraphRemoteConnectorProvider"       
```

Setting `remote.schemaManagement.enable` `true`  will set up the schema for the remote JanusGraph server, configuring the vertex labels and indexes used .

The `configurationProperties` provided are passed to the Gremlin driver when creating a session. 
More details about the options available can be found on tinkerpop [configuration section](https://tinkerpop.apache.org/docs/current/reference/#_configuration).      
  
Example:
```
"lineageGraphConnection": {
    "class": "Connection",
    "displayName": "Lineage Graph Connection",
    "description": "Used for storing lineage in the Open Metadata format",
    "connectorType": {
        "class": "ConnectorType",
        "connectorProviderClassName": "org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph.LineageGraphRemoteConnectorProvider"
    },
   "configurationProperties": {
        "port": "8182",
        "hosts": ["localhost"],
        "serializer.className": "org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV3d0",
        "serializer.config": {
            "ioRegistries": [
                "org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry",
                "org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerIoRegistryV3d0"
                ]
        },
        "gremlin.remote.driver.sourceName": "g",
        "remote.schemaManagement.enable": true
    }
},
```
In this example, the connector accesses a standalone JanusGraph server running on the local machine, on port 8182. 
The indexing and storage technologies used by the JansusGraph server are irrelevant for the client in this situation.  
 
----
Return to [open-lineage-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

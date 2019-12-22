<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Lineage Services

The Open Lineage Services provides a historic reporting warehouse for lineage. It listens to events that are send out 
by the Asset Lineage OMAS, and stores lineage data in a database. This lineage can then be queried through
the Open Lineage Services client and by its REST API, for example by a lineage GUI. While the data format of events sent
out by the Asset Lineage Omas are in the Open Metadata format, Open Lineage services store lineage data in a very basic
data format in order to optimize query performance. Instructions on how to configure an OMAG server to run the 
Open Lineage Services can be found further below.

The Open Lineage Services data format is structured as follows:

![Main graph data schema](assets/img/main_graph.png)
*The labels of the edges between columns, tables and processes. Glossary term nodes have been omitted for clarity.*


![Glossary lineage](assets/img/glossary_lineage.png)

*The labels of the edges between glossary term nodes and columns and tables.*

**Glossaryterm**

- Guid
- nodeID
- displayName
- qualifiedName
- glossary.guid
- glossary.name
- category

**Table**

- guid
- nodeID
- displayName
- qualifiedName
- glossaryTerm.displayName
- glossaryTerm.guid
- host.displayName
- host.guid
- database.displayName
- database.guid
- schema.displayName
- schema.guid
- Zones
- System
- Organization
- Geographical Location



**Column**

- Guid
- nodeID
- displayName
- qualifiedName
- glossaryTerm.displayName
- glossaryTerm.guid
- host.displayName
- host.guid
- database.displayName
- database.guid
- schema.displayName
- schema.guid
- table.displayName
- table.guid

**Process**

- guid
- nodeID
- createTime
- updateTime
- formula
- displayName
- processDescriptionURI
- version
- processType

**SubProcess**

- guid
- nodeID
- process.guid
- createTime
- updateTime
- formula
- displayName
- processDescriptionURI
- version
- processType

## OMAG Server Platform configuration

1. Start an [OMAG Server Platform](../../../open-metadata-resources/open-metadata-tutorials/omag-server-tutorial) and
run the default call for setting the server URL, eventbus and the cohort.

2. Configure the Open Lineage Services by providing a database connection object and setting the topic name of Asset 
Lineage OMAS Out topic via the following HTTP request:
```
POST {{base-url}}/open-metadata/admin-services/users/{{user-id}}/servers/{{server-id}}/open-lineage/configuration
```
With the following body: 
```json
{ 
    "class":"OpenLineageConfig",
    "openLineageDescription":"Open Lineage Service is used for the storage and querying of lineage",
    "inTopicName":"omas.omas.assetlineage.outTopic",
    "openLineageWiki":"wiki URL",
    "openLineageBufferGraphConnection":{ 
        "class":"Connection",
        "displayName":"Buffer Graph Connection",
        "description":"Used for storing lineage in the Open Metadata format",
        "connectorType":{ 
            "class":"ConnectorType",
            "connectorProviderClassName":"org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.buffergraph.BufferGraphConnectorProvider"
        },
        "configurationProperties":{ 
            "graphDB":"berkeleydb",
            "graphType":"bufferGraph",
            "storageBackend":"berkeleyje",
            "indexSearchBackend":"lucene"
        }
    },
    "openLineageMainGraphConnection":{ 
        "class":"Connection",
        "displayName":"Main Graph Connection",
        "description":"Used for storing lineage in a format optimized for querying lineage",
        "connectorType":{ 
            "class":"ConnectorType",
            "connectorProviderClassName":"org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.maingraph.MainGraphConnectorProvider"
        },
        "configurationProperties":{ 
            "graphDB":"berkeleydb",
            "graphType":"mainGraph",
            "storageBackend":"berkeleyje",
            "indexSearchBackend":"lucene"
        }
    }
}
```

3. Enable the Open Lineage Services by issuing the following HTTP request:

```
POST {{base-url}}/open-metadata/admin-services/users/{{user-id}}/servers/{{server-id}}/access-services
```

4. Start the instance of the OMAG Server Platform by issuing the following HTTP request:
    
```
POST {{base-url}}/open-metadata/admin-services/users/{{user-id}}/servers/{{server-id}}/instance
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
### Open Lineage Services configuration

For example payloads and endpoints, see the [Postman samples](../samples/OLS.postman_collection.json) 


1. Build the 
[open-lineage-janus-connector](../../../../adapters/open-connectors/governance-daemon-connectors/open-lineage-connectors/open-lineage-janus-connector/README.md) jar by running:

    ```
    mvn clean install
    ```

    The jar can now be found in the target directory of the open-lineage-janus-connector module:

    ```
    /open-metadata-implementation/adapters/open-connectors/governance-daemon-connectors/open-lineage-connectors/open-lineage-janus-connector/target/
    ```

    Add the jar to the classpath of the server-chassis-spring module.

2. Start the [OMAG Server Platform](../../../../../open-metadata-resources/open-metadata-tutorials/omag-server-tutorial) and run the default calls for
 setting the server URL, eventbus and the cohort.

3. Configure the Open Lineage Service, by POSTing a payload like the following
 
    ```json
    {
      "class": "OpenLineageConfig",
      "openLineageDescription": "Open Lineage Service is used for the storage and querying of lineage",
      "inTopicName": "server.omas.omas.assetlineage.outTopic",
      "openLineageWiki": "wiki URL",
      "jobIntervalInSeconds": "120",
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
    }
    ```
    to the following address

    ```
    POST {serverURLRoot}/open-metadata/admin-services/users/{userId}/servers/{serverName}/open-lineage/configuration
    ```

    Update the payload with specific configuration values
    - `inTopicName` - the topic name of Asset Lineage OMAS Out Topic
    - `jobIntervalInSeconds` - interval for Open Lineage Services background processing job. Default is 120 if not specified
    - `lineageGraphConnection` - contains the information needed for configuring the 
    [open-lineage-janus-connector](../../../../adapters/open-connectors/governance-daemon-connectors/open-lineage-connectors/open-lineage-janus-connector/README.md)

    Note that you can configure the connector to run embedded or on a standalone JanusGraph server, by setting the `connectorProviderClassName
    ` parameter to 
    `org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph.LineageGraphConnectorProvider
    ` (embedded) or `org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph
    .LineageGraphRemoteConnectorProvider`
    (standalone server). 
 
4. Start the instance of the OMAG Server by issuing the following HTTP request:
    
    ```
    POST {serverURLRoot}/open-metadata/admin-services/users/{userId}/servers/{serverName}/instance
    ```

### Removing the Open Lineage Services from the server configuration

Remove the Open Lineage Services from the server configuration by issuing the following HTTP request:
    
```
DELETE {serverURLRoot}/open-metadata/admin-services/users/{userId}/servers/{serverName}/open-lineage/configuration
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
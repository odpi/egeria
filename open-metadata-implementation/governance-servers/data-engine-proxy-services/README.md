<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Data Engine Proxy Services

The Data Engine Proxy Services provide the principle subsystem of the 
[Data Engine Proxy](../../admin-services/docs/concepts/data-engine-proxy.md)
server.

This server supports intercommunication from a **Data Engine** and
the [Data Engine OMAS](../../access-services/data-engine).

A data engine is a technology that processes data to either copy it to a new location,
transform it to a new format so it can be consumed in a different way and/or
derive new values from the data.  The data engine may be retrieving data from
multiple data sources and also writing to multiple data sources.

Information about the data that the data engine is consuming and creating, along with
details of the processing it is doing is needed to provide lineage for the
data sources.  This is why it is important that we capture information about a data engine's
processing in open metadata.

The Data Engine OMAS provides the API for capturing the processing of a
data engine.

Where a Data Engine does not have direct support for publishing details of its processing to the
Data Engine OMAS, a data engine proxy connector can be written for the Data Engine that
uses the Data Engines native APIs and events to capture the required 
information and then translates it into calls to the Data Engine OMAS.

The data engine proxy connector runs inside a Data Engine Proxy Server
on the OMAG Server Platform.  The data engine proxy services are 
responsible for hosting the data engine proxy connector.

## Data Engine Proxy Server configuration

1. Start an [OMAG Server Platform](../../../open-metadata-resources/open-metadata-tutorials/omag-server-tutorial)
1. Configure the Data Engine Proxy Server:

    **POST** following JSON object (following shows an example for IBM DataStage)

    ```json
    {
        "class": "DataEngineProxyConfig",
        "accessServiceRootURL": "{serverURLRoot}",
        "accessServiceServerName": "{MetadataServerName}",
        "dataEngineConnection": {
            "class": "Connection",
            "connectorType": {
                "class": "ConnectorType",
                "connectorProviderClassName": "org.odpi.egeria.connectors.ibm.datastage.dataengineconnector.DataStageConnectorProvider"
            },
            "endpoint": {
                "class": "Endpoint",
                "address": "myhost.somewhere.com:9445",
                "protocol": "https"
            },
            "userId": "{dataEngineAccessUserId}",
            "clearPassword": "dataEngineAccessPassword"
        },
        "pollIntervalInSeconds": 60
    }
    ```
    
    to the following address

    ```
    {serverURLRoot}/open-metadata/admin-services/users/{userId}/servers/{serverName}/data-engine-proxy-service/configuration
    ```

    The object *dataEngineConfig* is the information required to implement the specific proxy connector to the data engine. The keys should be modified based on the information needed by the connector.

1. Start the instance of the OMAG Server Platform

    **POST** to the following address
    
    ```
    {serverURLRoot}/open-metadata/admin-services/users/{userId}/servers/{serverName}/instance
    ```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
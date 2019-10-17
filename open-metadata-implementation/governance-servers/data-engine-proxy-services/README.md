<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Data Engine Proxy Services

The Data Engine Proxy Services support intercommunication from a Data Engine and
the Data Engine OMAS. Where a Data Engine is unable to handle this communication
directly, a proxy can be written for the Data Engine that runs server-side in
OMAG Server Platform and brokers calls to the Data Engine itself into calls back
to the Data Engine OMAS. This set of proxy services configures such a proxy to
run server-side within the OMAG Server Platform.

## OMAG Server Platform configuration

1. Start an [OMAG Server Platform](../../../open-metadata-resources/open-metadata-tutorials/omag-server-tutorial)
1. Configure the Data Engine Proxy:

    **POST** following JSON object (following shows an example for IBM DataStage)

    ```json
    {
        "class": "DataEngineProxyConfig",
        "accessServiceRootURL": "http://localhost:8080",
        "accessServiceServerName": "omas",
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
            "userId": "igcuser",
            "clearPassword": "igcpassword"
        },
        "pollIntervalInSeconds": 60
    }
    ```
    
    to the following address

    ```
    http://localhost:8080/open-metadata/admin-services/users/{{user-name}}/servers/{{server-name}}/data-engine-proxy-service/configuration
    ```

    The object *dataEngineConfig* is the information required to implement the specific proxy connector to the data engine. The keys should be modified based on the information needed by the connector.

1. Start the instance of the OMAG Server Platform

    **POST** to the following address
    
    ```
    http://localhost:8080/open-metadata/admin-services/users/{{user-name}}/servers/{{server-name}}/instance
    ```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
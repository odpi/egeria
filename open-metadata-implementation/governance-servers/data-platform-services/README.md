<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Data Platform Services

The Data Platform Services support intercommunication from a Data Platform and
the Data Platform OMAS. Where a Data Platform is unable communicate with Data Platform 
OMAS directly, a Data Platform Service for the Data Platform can run on server-side in
OMAG Server Platform and broker calls to the Data Platform itself into calls back
to the Data Platform OMAS. 

## OMAG Server Platform configuration

1. Start an [OMAG Server Platform](../../../open-metadata-resources/open-metadata-tutorials/omag-server-tutorial)
1. Configure the Data Platform:

    **POST** following JSON object (following shows an example for Apache Cassandra)

    ```json
        {
          "class": "DataPlatformServicesConfig",
          "dataPlatformGUID": "296bc645-2043-499c-bcd9-ecff90e46899",
          "dataPlatformServerURL":"127.0.0.1",
          "dataPlatformOmasInTopicName": "omas.dataplatform.inTopic",
          "dataPlatformServerName":"Apache Cassandra Database",
          "dataPlatformConnectionProvider": "org.odpi.openmetadata.adapters.connectors.datastore.cassandra.CassandraDataStoreProvider",
          "dataPlatformConnection":{
            "class": "Connection",
            "displayName": "Apache Cassandra Database",
            "description": "Distributed wide-column database",
            "connectorType": {
              "class": "ConnectorType",
              "connectorProviderClassName": "org.odpi.openmetadata.adapters.connectors.datastore.cassandra.CassandraDataStoreProvider"
            },
            "endpoint": {
              "class": "Endpoint",
              "address": "127.0.0.1",
              "protocol": "https",
              "additionalProperties":{
                "port":"9042"
              }
            },
            "userId": "test",
            "clearPassword": "test",
            "configurationProperties": {
              "defaultZones": [ "x", "y", "z" ]
            }
          }
        }
    ```
    
    to the following address

    ```
    http://localhost:8080/open-metadata/admin-services/users/{{user-name}}/servers/{{server-name}}/data-platform-service/configuration
    ```

    The object *dataPlatformServicesConfig* is the information required to implement the specific data platform connector to configure the connection to the target data platform. 
    The authentication information should be modified based on the information needed by the connector.

1. Start the instance of the OMAG Server Platform

    **POST** to the following address
    
    ```
    http://localhost:8080/open-metadata/admin-services/users/{{user-name}}/servers/{{server-name}}/instance
    ```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
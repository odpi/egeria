<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Overriding the location for storing configuration documents

By default, [configuration documents](../concepts/configuration-document.md) are stored as a
file in the OMAG server platform's home directory.
The file name is 
```
omag.server.{serverName}.config

```
where serverName is the name of the logical OMAG server (cocoMDS1 for example).

The management of the configuration documents on the disk is implemented by a connector.
To change the connector used for the configuration
(and hence where and how configuration documents are stored), use the following REST API
with the connection object of the new connector as the request body.

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/stores/connection

```

The JSON below is an example of a [Connection](../../../../frameworks/open-connector-framework/docs/concepts/connection.md) object.
```json
{
    "class": "Connection",
    "type": 
    {
        "class": "ElementType",
        "elementTypeId": "114e9f8f-5ff3-4c32-bd37-a7eb42712253",
        "elementTypeName": "Connection",
        "elementTypeVersion": 1,
        "elementTypeDescription": "A set of properties to identify and configure a connector instance.",
        "elementOrigin": "CONFIGURATION"
    },
    "guid": "12137087-2b13-4c4e-b840-97c4282f7416",
    "qualifiedName": "My Custom Configuration Document JSON database Connector",
    "displayName": "My Custom Configuration Document Store Connection",
    "description": "Connection to custom built connector to store configuration documents in JSON Document database.",
    "connectorType": 
    {
        "class": "ConnectorType",
        "type": 
        {
            "class": "ElementType",
            "elementTypeId": "954421eb-33a6-462d-a8ca-b5709a1bd0d4",
            "elementTypeName": "ConnectorType",
            "elementTypeVersion": 1,
            "elementTypeDescription": "A set of properties describing a type of connector.",
            "elementOrigin": "LOCAL_COHORT"
        },
        "guid": "3853e8d0-e343-400c-83cb-3918fed81da6",
        "qualifiedName": "Configuration Document JSON database Connector",
        "displayName": "Configuration Document JSON database Connector",
        "description": "Custom built connector to store configuration documents in JSON Document database.",
        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider"
    },
    "endpoint": 
    {
        "class": "Endpoint",
        "type": 
        {
            "class": "ElementType",
            "elementTypeId": "dbc20663-d705-4ff0-8424-80c262c6b8e7",
            "elementTypeName": "Endpoint",
            "elementTypeVersion": 1,
            "elementTypeDescription": "Description of the network address and related information needed to call a software service.",
            "elementOrigin": "CONFIGURATION"
        },
        "guid": "b4ab2f8c-5f55-4ed7-8762-1ef2ab958db5",
        "qualifiedName": "open-metadata/ConfigDocStore",
        "displayName": "open-metadata/ConfigDocStore",
        "description": "Location of the configuration document store",
        "address": "common-services/open-metadata/ConfigDocStore/Production"
    }
}

```

The admin services also support a GET request to inspect the setting of the connection
and a DELETE request to clear the connection setting back to null (default).
Both requests use the same URL.




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
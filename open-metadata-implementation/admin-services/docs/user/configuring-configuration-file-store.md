<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Overriding the location for storing configuration documents

By default, [configuration documents](../concepts/configuration-document.md) are stored as a
file in the OMAG server platform's home directory.
The file name is 

```
omag.server.{serverName}.config
```

where serverName is the name of the OMAG server (cocoMDS1 for example).

The management of the configuration documents on the disk is implemented by a connector.
To change the connector used for the configuration
(and hence where and how configuration documents are stored), use the following REST API
with the connection object of the new connector as the request body.

```
POST {serverURLRoot}/open-metadata/admin-services/users/{adminUserId}/stores/connection
```

The JSON below is an example of a [Connection](../../../frameworks/open-connector-framework/docs/concepts/connection.md) object.

```json
{
    "class": "Connection",
    "connectorType": 
    {
        "class": "ConnectorType",
        "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider"
    },
    "endpoint": 
    {
        "class": "Endpoint",
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
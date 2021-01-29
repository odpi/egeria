<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Configuring the server security connector

Metadata that is being aggregated from many different sources
is likely to need comprehensive access controls.

Egeria provides [fine-grained security control for metadata
access](../../../common-services/metadata-security).
It is implemented in a server security connector
that is called whenever requests are made for to the server.

Security is configured for a specific [OMAG Server](../concepts/omag-server.md) by adding a connection
for this connector to the server's [Configuration Document](../concepts/configuration-document.md)
using the following command.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/security/connection
```
This passes in a connection used to create the server security connector
in the request body.  

```json
{
    "class": "Connection",
    "connectorType": {
        "class": "ConnectorType",
        "connectorProviderClassName": "{fullyQualifiedJavaClassName}"
    }
}
```

For example, this is the connection that would
set up the [sample server security connector](../../../../open-metadata-resources/open-metadata-samples/open-metadata-security-samples) provided for the Coco Pharmaceuticals case study:
```json
{
    "class": "Connection",
    "connectorType": {
        "class": "ConnectorType",
        "connectorProviderClassName": "org.odpi.openmetadata.metadatasecurity.samples.OpenMetadataServerSecurityProvider"
    }
}
```

# Querying which connector is in use in an OMAG Server

It is possible to query the setting of the server security connector
using the following command:

```
GET {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/security/connection
```

If the response is:
```json
{
    "class": "ConnectionResponse",
    "relatedHTTPCode": 200
}
```
then no connector is set up and no authorization checks are occurring.

If the response looks more like the JSON below, a connector is configured.  The
`connectorProviderClassName` tells you which connector is being used.

```json
{
    "class": "ConnectionResponse",
    "relatedHTTPCode": 200,
    "connection": {
        "class": "Connection",
        "connectorType": {
            "class": "ConnectorType",
            "connectorProviderClassName": "{fullyQualifiedJavaClassName}"
        }
    }
}
```

# Removing the configured server security connector

It is possible to remove the configuration for the connector using
the following command:

```
DELETE {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/security/connection
```

This removes all authorization checking from the server.

----
Return to [Configuring the OMAG Server](configuring-an-omag-server.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
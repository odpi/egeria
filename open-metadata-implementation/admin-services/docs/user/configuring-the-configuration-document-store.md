<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Configuring the configuration document store

The [Configuration Document](../concepts/configuration-document.md)
is the place where the configuration for a single [OMAG Server](../concepts/omag-server.md)
is stored.  This may include security certificates and passwords.

By default the configuration document is stored in its own encrypted file named:

```
omag.server.{serverName}.config
```
in the home directory of the [OMAG Server Platform](../concepts/omag-server-platform.md).

As of v2.0 of Egeria, the contents of the configuration document are
stored in an encrypted JSON format. (For more details, see: [Encrypted File Store Connector](../../../adapters/open-connectors/configuration-store-connectors/configuration-encrypted-file-store-connector/README.md)).
The clear-text contents of the configuration document can still be retrieved
by accessing the admin services endpoint for retrieving the configuration
document; however, this ensures security is applied before a user is able
to retrieve the configuration document's contents:

```
GET {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/configuration/
```

You may also wish to:

* Move the location of the configuration documents
* Write you own alternative store for the configuration documents

All of these options are possible because the configuration document
store is implemented in a
[configuration document store connector](../concepts/configuration-document-store-connector.md).
It is therefore possible to change the implementation or behavior
of this connector with a simple configuration change to the
OMAG Server Platform.

There are two implementations on the configuration document store connector
supplied by Egeria.

* **[configuration-encrypted-file-store-connector](../../../adapters/open-connectors/configuration-store-connectors/configuration-encrypted-file-store-connector)** supports managing
the open metadata configuration as an encrypted JSON file. This is the default configuration document store connector.

* **[configuration-file-store-connector](../../../adapters/open-connectors/configuration-store-connectors/configuration-file-store-connector)** supports managing the
open metadata configuration as a clear text JSON file.

It is also possible to [write your own implementation](../../../adapters/open-connectors/configuration-store-connectors).

The configuration document store connector is configured in the OMAG Server Platform
using the following command.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/stores/connection
```

This passes in a connection used to create the connector to the configuration document storage
in the request body.  For example, this is the connection that would
set up the (unencrypted) file store connector:

```json
{
    "class": "Connection",
    "connectorType": {
        "class": "ConnectorType",
        "connectorProviderClassName": "org.odpi.openmetadata.adapters.adminservices.configurationstore.file.FileBasedServerConfigStoreProvider"
    },
    "endpoint": {
        "class": "Endpoint",
        "address": "omag.server.{0}.config"
    }
}
```

**Note:** in order to use any connector other than the default, you need to also ensure that
the Connector and its ConnectorProvider class are available to the server platform (ie. the
jar file containing them is available in the `LOADER_PATH` location of the server platform).

Alternatively, this connection uses the default encrypted JSON file store, but the files
are stored in a different location (`/my-config/omag.server.{0}.config`).

```json
{
    "class": "Connection",
    "connectorType": {
        "class": "ConnectorType",
        "connectorProviderClassName": "org.odpi.openmetadata.adapters.adminservices.configurationstore.encryptedfile.EncryptedFileBasedServerConfigStoreProvider"
    },
    "endpoint": {
        "class": "Endpoint",
        "address": "/my-config/omag.server.{0}.config"
    }
}
```

# Querying which connector is in use in an OMAG Server Platform

It is possible to query the setting of the configuration document store connector
using the following command:

```
GET {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/stores/connection
```

If the response is:
```json
{
    "class": "ConnectionResponse",
    "relatedHTTPCode": 200
}
```
then no connector is set up and the encrypted JSON file store is used.

If the response looks more like the JSON below, a connector is configured.  The
`connectorProviderClassName` tells you which connector is being used and the `address` shows where the
configuration documents are stored.

```json
{
    "class": "ConnectionResponse",
    "relatedHTTPCode": 200,
    "connection": {
        "class": "Connection",
        "connectorType": {
            "class": "ConnectorType",
            "connectorProviderClassName": "org.odpi.openmetadata.adapters.adminservices.configurationstore.file.FileBasedServerConfigStoreProvider"
        },
        "endpoint": {
            "class": "Endpoint",
            "address": "omag.server.{0}.config"
        }
    }
}
```

# Removing the configured configuration document store connector

It is possible to remove the configuration for the connector using
the following command:

```
DELETE {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/stores/connection
```

This reverts the store to the default encrypted JSON file store.

----
Return to [Configuring the OMAG Server Platform](configuring-the-omag-server-platform.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
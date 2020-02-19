<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Configuring the configuration document store

The [Configuration Document](../concepts/configuration-document.md)
is the place where the configuration for a single [OMAG Server](../concepts/omag-server.md)
is stored.  This may include security certificates and passwords.

By default the configuration document is stored in its own file named:

```
omag.server.{serverName}.config
```
in the home directory of the [OMAG Server Platform](../concepts/omag-server-platform.md).

The contents of the configuration document are stored in JSON format
in clear text.
This may be sufficient for a development environment.  However, for
a production environment you may wish to:

* Move the location of the configuration documents to a secure directory
* Encrypt the contents of the configuration documents
* Write you own secure store for the configuration documents

All of these options are possible because the configuration document
store is implemented in a
[configuration document store connector](../concepts/configuration-document-store-connector.md).
It is therefore possible to change the implementation or behavior
of this connector with a simple configuration change to the
OMAG Server Platform.

There are two implementations on the configuration document store connector
supplied by Egeria.

* **[configuration-file-store-connector](../../../adapters/open-connectors/configuration-store-connectors/configuration-file-store-connector)** supports managing the
open metadata configuration as a clear text JSON file.
This is the default configuration document store connector.

* **[configuration-encrypted-file-store-connector](../../../adapters/open-connectors/configuration-store-connectors/configuration-encrypted-file-store-connector)** supports managing
the open metadata configuration as an encrypted JSON file.

It is also possible to [write your own implementation](../../../adapters/open-connectors/configuration-store-connectors).

The configuration document store connector is configured in the OMAG Server Platform
using the following command.

```
POST {serverURLRoot}/open-metadata/admin-services/users/{adminUserId}/stores/connection
```
This passes in a connection used to create the connector to the configuration document storage
in the request body.  For example, this is the connection that would
set up the encrypted file store connector:
```json
{
    "class": "Connection",
    "connectorType": {
        "class": "ConnectorType",
        "connectorProviderClassName": "org.odpi.openmetadata.adapters.adminservices.configurationstore.encryptedfile.EncryptedFileBasedServerConfigStoreProvider"
    },
    "endpoint": {
        "class": "Endpoint",
        "address": "omag.server.{0}.config"
    }
}
```
Alternatively, this connection uses the default clear text JSON file store, but the files
are stored in a different location (`/my-config/omag.server.{0}.config`).

```json
{
    "class": "Connection",
    "connectorType": {
        "class": "ConnectorType",
        "connectorProviderClassName": "org.odpi.openmetadata.adapters.adminservices.configurationstore.file.FileBasedServerConfigStoreProvider"
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
GET {serverURLRoot}/open-metadata/admin-services/users/{adminUserId}/stores/connection
```

If the response is:
```json
{
    "class": "ConnectionResponse",
    "relatedHTTPCode": 200
}
```
then no connector is set up and the clear text JSON file store is used.

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
            "connectorProviderClassName": "org.odpi.openmetadata.adapters.adminservices.configurationstore.encryptedfile.EncryptedFileBasedServerConfigStoreProvider"
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
DELETE {serverURLRoot}/open-metadata/admin-services/users/{adminUserId}/stores/connection
```

This reverts the store to the default clear text JSON file store.

----
Return to [Configuring the OMAG Server Platform](configuring-the-omag-server-platform.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
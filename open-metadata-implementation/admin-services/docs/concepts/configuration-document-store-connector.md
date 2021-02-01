<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Configuration document store connector

The configuration for an [OMAG Server](omag-server.md)
is managed by the **Configuration Document Store Connector**.

The **[admin-services-api](../../admin-services-api)** module provides the interface
definition for this connector.
Its interface is simple consisting of save, retrieve and delete operations:

```java
/**
 * OMAGServerConfigStore provides the interface to the configuration for an OMAG Server.  This is accessed
 * through a connector.
 */
public interface OMAGServerConfigStore
{
    /**
     * Save the server configuration.
     *
     * @param configuration configuration properties to save
     */
    void saveServerConfig(OMAGServerConfig   configuration);


    /**
     * Retrieve the configuration saved from a previous run of the server.
     *
     * @return server configuration
     */
    OMAGServerConfig  retrieveServerConfig();


    /**
     * Remove the server configuration.
     */
    void removeServerConfig();
}
```

The configuration document is represented by the `OMAGServerConfig` structure.
The name of the server is stored in the `localServerName` property in
`OMAGServerConfig`.

## Sample implementations

The implementations of this connector provided by Egeria are found in the
[configuration-store-connectors](.././../../adapters/open-connectors/configuration-store-connectors)
module.  There are two connectors:

These are the configuration store connectors implemented by Egeria.

* **[configuration-file-store-connector](.././../../adapters/open-connectors/configuration-store-connectors/configuration-file-store-connector)** supports managing the
open metadata configuration as a clear text JSON file.

* **[configuration-encrypted-file-store-connector](.././../../adapters/open-connectors/configuration-store-connectors/configuration-encrypted-file-store-connector)** supports managing
the open metadata configuration as an encrypted JSON file.


## Configuring the connector into an OMAG Server Platform

See [Configuring the Configuration Document Store](../user/configuring-the-configuration-document-store.md)
for the command to install a particular configuration document store connector
into the OMAG Server Platform.

----
* Return to the [Administration Guide](../user)
* Return to the [Developer Guide](../../../../open-metadata-publication/website/developer-guide/extending-egeria-using-connectors.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
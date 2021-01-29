<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Connector - part of the [Open Connector Framework (OCF)](../..)

A connector is a java client object that provides applications with access to a data source or service
(known as an [asset](../../../../../open-metadata-implementation/access-services/docs/concepts/assets/README.md))
along with its related metadata.

An OCF connector provides four APIs.

* **Connector Lifecycle**: manages the lifecycle state of the connector and includes `initialize()`, `start()` and
  `disconnect()`.
* **Metadata store initialization**: if the connector is created by a metadata service then it adds a client to
  the metadata server called [ConnectedAssetProperties](connected-asset-properties.md) to the connector
  between `initialize()` and `start()`.  The ConnectedAssetProperties client can be retrieved from the
  connector instance and used to retrieve metadata about the asset that is stored in the metadata server.
* **Specific initialization for the type of connector**: some types of connectors need additional initialization.
  These methods are called by the component creating the connector before the `start()` method is called.
* **Asset Content**: this API is crafted to provide the most natural interface to the Asset's contents. 
  Therefore the Asset Content API is typically different for each type of connector.

OCF connectors are not limited to representing Assets as they are physically implemented.
An OCF connector can represent a simplified logical (virtual) asset, such as a data set, that is designed for the needs
of a specific application or tool.
This type of connector delegates the requests it receives to one or more physical data resources.
It is called a virtual connector.

## Further Information

See the [Egeria Developer Guide](../../../../../open-metadata-publication/website/developer-guide)
for information on writing connectors.


----
* [Return to OCF Overview](../..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.




<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Consumer OMAS Connector Factory Interface (AssetConsumerConnectorFactoryInterface)

AssetConsumerConnectorFactoryInterface supports the creation of connectors from connections.

[Connectors](../../../../../frameworks/open-connector-framework/docs/concepts/connector.md)
are client-side objects for interacting with [Assets](../../../../docs/concepts/assets)
such as databases, APIs and files.

The configuration for a specific connectors is managed as open metadata in
a [Connection](../../../../../frameworks/open-connector-framework/docs/concepts/connection.md) definition.  

The caller to the Asset Consumer OMAS passes either a Connection object, or the name or GUID for the
connection stored in the open metadata repositories. The Asset Consumer OMAS creates an appropriate
connector as described in the connection and returns it to the caller.

It includes the following operations:

* **getConnectorByName** - Returns the connector corresponding to the supplied connection name.
  
  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/get-connector-by-name-with-java.md)

* **getConnectorByGUID** - Returns the connector corresponding to the supplied connection GUID.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/get-connector-by-guid-with-java.md)

* **getConnectorForAsset** - Returns the connector corresponding to the supplied asset.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/get-connector-by-asset-guid-with-java.md)

* **getConnectorByConnection** - Returns the connector corresponding to the supplied connection.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/get-connector-by-connection-with-java.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
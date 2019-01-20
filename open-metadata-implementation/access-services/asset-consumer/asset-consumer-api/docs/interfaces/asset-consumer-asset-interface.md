<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Consumer's Asset Interface (AssetConsumerAssetInterface)

AssetConsumerAssetInterface provides the ability to query information about an
[asset](../../../../docs/concepts/assets).

These first two methods enable the caller to extract the unique identifier
([guid]()) of an asset from a connection that is linked to it.

* **getAssetForConnection** - returns the unique identifier for the asset connected to the requested connection.
  
  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/get-asset-for-connection-guid-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/get-asset-for-connection-guid-with-rest.md)

* **getAssetForConnectionName** - returns the asset corresponding to the supplied connection name.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/get-asset-for-connection-name-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/get-asset-for-connection-name-with-rest.md)

This method uses the guid of the asset to retrieve properties about the asset.  It is implemented
by the [Connected Asset OMAS](../../../../connected-asset) which is why there is no REST implementation.

* **getAssetProperties** - returns a comprehensive collection of properties about the requested asset.
   
  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/get-asset-properties-with-java.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
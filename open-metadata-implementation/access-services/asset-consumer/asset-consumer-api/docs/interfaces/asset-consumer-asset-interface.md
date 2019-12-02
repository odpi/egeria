<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Consumer's Asset Interface (AssetConsumerAssetInterface)

AssetConsumerAssetInterface provides the ability to query information about an
[asset](../../../../docs/concepts/assets).

The first 3 methods provide the ability to locate assets by names and descriptions.

* **findAssets** - returns a list of unique identifiers for assets that match a search string. This search string may have wild cards in it.
  
  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/find-assets-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/find-assets-with-rest.md)

* **getAssetsByName** - returns a list of unique identifiers for assets that exactly match the supplied name.  This may be the qualified name or the display name.
  
  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/get-asset-list-by-name-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/get-asset-list-by-name-with-rest.md)

* **getAssetsByToken** - returns a list of unique identifiers for assets that exactly match the supplied token either by unique
  identifier ([GUID](../../../../../../open-metadata-publication/website/basic-concepts/guid.md)) or name.
  
  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/get-asset-list-by-token-with-java.md)


These next two methods enable the caller to extract the unique identifier
([GUID](../../../../../../open-metadata-publication/website/basic-concepts/guid.md)) of an asset from a connection that is linked to it.

* **getAssetForConnection** - returns the unique identifier for the asset connected to the requested connection.
  
  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/get-asset-for-connection-guid-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/get-asset-for-connection-guid-with-rest.md)

* **getAssetForConnectionName** - returns the asset corresponding to the supplied connection name.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/get-asset-for-connection-name-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/get-asset-for-connection-name-with-rest.md)

This method uses the guid of the asset to retrieve properties about the asset.  It is implemented
by the [OCF Metadata Management](../../../../../common-services/ocf-metadata-management) module
which is why there is no REST implementation.

* **getAssetProperties** - returns a comprehensive collection of properties about the requested asset.
   
  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/get-asset-properties-with-java.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
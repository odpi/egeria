<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Asset Consumer's Tagging Interface (AssetConsumerTaggingInterface)

AssetConsumerTaggingInterface provides the ability to create both private and public
[tags](../../../../docs/concepts/attachments/tagging.md).

The first three methods cover the life cycle of an individual tag.  This tag can be attached to
any number of assets (including none).

* **createPublicTag** - Creates a new public informal tag and returns the unique identifier for it.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/create-public-tag-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/create-public-tag-with-rest.md)

* **createPrivateTag** - Creates a new private informal tag and returns the unique identifier for it.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/create-private-tag-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/create-private-tag-with-rest.md)
  
* **updateTagDescription** - Updates the description of an existing tag (either private of public).

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/update-tag-description-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/update-tag-description-with-rest.md)
  
* **deleteTag** - Removes a tag from the repository.  All of the relationships to assets are lost.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/delete-tag-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/delete-tag-with-rest.md)

* **addTagToAsset** - Adds a tag (either private of public) to an asset.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/add-tag-to-asset-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/add-tag-to-asset-with-rest.md)

* **removeTagFromAsset** - Removes a tag from the asset that was added by this user.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/remove-tag-from-asset-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/remove-tag-from-asset-with-rest.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
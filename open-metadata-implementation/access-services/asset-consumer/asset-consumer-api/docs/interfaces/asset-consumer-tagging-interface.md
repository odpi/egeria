<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Asset Consumer's Tagging Interface (AssetConsumerTaggingInterface)

AssetConsumerTaggingInterface provides the ability to create both private and public
[tags](../../../../docs/concepts/attachments/tagging.md).

The first three methods cover the life cycle of an individual tag.  This tag can be attached to
any number of assets (including none).

* **createPublicTag** - Creates a new public informal tag and returns the unique identifier for it.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/create-public-tag-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/create-public-tag-with-rest.md)

* **createPrivateTag** - Creates a new private informal tag and returns the unique identifier for it.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/create-private-tag-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/create-private-tag-with-rest.md)
  
* **updateTagDescription** - Updates the description of an existing tag (either private of public).

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/update-tag-description-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/update-tag-description-with-rest.md)
  
* **deleteTag** - Removes a tag from the repository.  All of the relationships to assets are lost.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/delete-tag-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/delete-tag-with-rest.md)

* **getTag** - Return the tag for the supplied unique identifier (guid).

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/get-tag-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/get-tag-with-rest.md)

* **getTagsByName** - Return the list of tags exactly matching the supplied name.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/get-tags-by-name-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/get-tags-by-name-with-rest.md)

* **getMyTagsByName** - Return the list of the calling user's private tags exactly matching the supplied name.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/get-my-tags-by-name-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/get-my-tags-by-name-with-rest.md)

* **findTags** - Return the list of tags matching the supplied search string.  This search string may contain wild card characters.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/find-tags-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/find-tags-with-rest.md)

* **findMyTags** - Return the list of the calling user's private tags matching the supplied search string.  This search string may contain wild card characters.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/find-my-tags-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/find-my-tags-with-rest.md)

* **addTagToAsset** - Adds a tag (either private of public) to an asset.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/add-tag-to-asset-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/add-tag-to-asset-with-rest.md)

* **removeTagFromAsset** - Removes a tag from the asset that was added by this user.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/remove-tag-from-asset-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/remove-tag-from-asset-with-rest.md)

* **getAssetsByTag** - Return the list of assets linked to the supplied tag.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/get-assets-by-tag-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/get-assets-by-tag-with-rest.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
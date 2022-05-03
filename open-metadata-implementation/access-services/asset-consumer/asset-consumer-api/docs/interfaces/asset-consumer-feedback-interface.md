<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Consumer's Feedback Interface (AssetConsumerFeedbackInterface)

AssetConsumerFeedbackInterface provides the ability to attach reviews, likes and
comments to an [asset](https://egeria-project.org/concepts/asset).
These elements can only be attached to one asset so the methods to
add one of these types of feedback elements includes a create of the element
as well as the attachment of it to the identified asset.
Similarly, the remove methods both detach the element from the asset and
delete it from the repository.

Updates and deletes on a feedback element are only allowed by the user that created the element in the first place.

These first three methods enable the caller to work with reviews.

* **addReviewToAsset** - Adds a star rating and optional review text to the asset.
  
  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/add-review-to-asset-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/add-review-to-asset-with-rest.md)

* **removeReviewFromAsset** - Removes a review that was added to the asset by this user.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/remove-review-from-asset-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/remove-review-from-asset-with-rest.md)

* **addLikeToAsset** - Adds a "Like" to the asset.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/add-like-to-asset-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/add-like-to-asset-with-rest.md)

* **removeLikeFromAsset** - Removes a "Like" added to the asset by this user.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/remove-like-from-asset-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/remove-like-from-asset-with-rest.md)
   
* **addCommentToAsset** - Adds a comment to the asset.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/add-comment-to-asset-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/add-comment-to-asset-with-rest.md)

* **addCommentReply** - Adds a comment to another comment.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/add-comment-reply-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/add-comment-reply-with-rest.md)
* **updateComment** -  Update an existing comment.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/update-comment-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/update-comment-with-rest.md)

* **removeCommentFromAsset** - Removes a comment added to the asset by this user.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/remove-comment-from-asset-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/remove-comment-from-asset-with-rest.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
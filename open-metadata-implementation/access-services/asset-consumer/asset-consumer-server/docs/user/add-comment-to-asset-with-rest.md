<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Add comment to asset

Creates a comment and attaches it to an asset.

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/asset-consumer/users/{userId}/assets/{requestType}/comments
```

```java
CommentRequestBody  requestBody = new CommentRequestBody();
requestBody.setCommentType(commentType);
requestBody.setCommentText(commentText);

GUIDResponse restResult
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
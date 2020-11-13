<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Add Comment Reply

Adds a comment to an existing comment as a reply.

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/asset-consumer/users/{userId}/comments/{commentGUID}/replies
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
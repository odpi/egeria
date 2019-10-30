<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Update comment

```java
final String   serverURLRoot = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/comments/{2}/update

invalidParameterHandler.validateOMASServerURL(serverPlatformRootURL, methodName);
invalidParameterHandler.validateUserId(userId, methodName);
invalidParameterHandler.validateGUID(commentGUID, commentGUIDParameter, methodName);

CommentRequestBody  requestBody = new CommentRequestBody();
requestBody.setCommentType(commentType);
requestBody.setCommentText(commentText);

VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                          serverPlatformRootURL + urlTemplate,
                                                          requestBody,
                                                          serverName,
                                                          userId,
                                                          commentGUID);
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
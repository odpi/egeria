<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

```java
final String   methodName  = "addTagToAsset";
final String   assetGUIDParameterName = "requestType";
final String   tagGUIDParameterName = "tagGUID";

final String   serverURLRoot = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/tags/{3}/delete

invalidParameterHandler.validateOMASServerURL(serverPlatformRootURL, methodName);
invalidParameterHandler.validateUserId(userId, methodName);
invalidParameterHandler.validateGUID(requestType, assetGUIDParameterName, methodName);
invalidParameterHandler.validateGUID(tagGUID, tagGUIDParameterName, methodName);

NullRequestBody  requestBody = new NullRequestBody();

VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                          serverPlatformRootURL + urlTemplate,
                                                          requestBody,
                                                          serverName,
                                                          userId,
                                                          requestType,
                                                          tagGUID);
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
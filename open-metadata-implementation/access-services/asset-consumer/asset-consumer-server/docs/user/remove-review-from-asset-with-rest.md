<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

```java
final String   serverURLRoot = "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/reviews/{2}/delete

invalidParameterHandler.validateOMASServerURL(serverPlatformRootURL, methodName);
invalidParameterHandler.validateUserId(userId, methodName);
invalidParameterHandler.validateGUID(reviewGUID, guidParameter, methodName);

VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                          serverPlatformRootURL + urlTemplate,
                                                          nullRequestBody,
                                                          serverName,
                                                          userId,
                                                          reviewGUID);
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
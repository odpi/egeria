<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Update Tag Description

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/asset-consumer/users/{userId}/tags/{tagGUID}/update
```

```java
TagRequestBody  tagProperties = new TagRequestBody();
tagProperties.setTagDescription(tagDescription);

VoidResponse
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
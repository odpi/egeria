<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Update review on asset

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/asset-consumer/users/{userId}/reviews/{reviewGUID}/update
```

```java
ReviewRequestBody requestBody = new ReviewRequestBody();
requestBody.setStarRating(starRating);
requestBody.setReview(review);

VoidResponse
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
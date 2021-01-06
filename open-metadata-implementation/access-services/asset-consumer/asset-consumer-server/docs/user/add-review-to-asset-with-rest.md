<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Add review to asset

Adds a star rating and optional review text to the asset.

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/asset-consumer/users/{userId}/assets/{requestType}/reviews
```

```java
ReviewRequestBody requestBody = new ReviewRequestBody();
requestBody.setStarRating(starRating);
requestBody.setReview(review);

GUIDResponse restResult;        
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Retrieving my personal profile using REST

An individual can retrieve their personal profile with their userId.

```
GET <serverRootURL>/servers/{serverName}/open-metadata/access-services/community-profile/users/{userId}/my-profile
```

If the server is running then the HTTP Status will be 200 and the response
structure will either return the profile, or details of the exception that occurred.





----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
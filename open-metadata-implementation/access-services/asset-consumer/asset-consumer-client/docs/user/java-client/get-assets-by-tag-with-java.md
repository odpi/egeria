<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

# getAssetsByTag

```
    /**
     * Return the list of unique identifiers for assets that are linked to a specific tag either directly, or via one
     * of its schema elements.
     *
     * @param userId the name of the calling user.
     * @param tagGUID unique identifier of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return asset guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<String> getAssetsByTag(String userId,
                                String tagGUID,
                                int    startFrom,
                                int    pageSize) throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException;
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
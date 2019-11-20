<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

# findAssets

```
    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.  The search string is interpreted as a regular expression (RegEx).
     *
     * @param userId calling user
     * @param searchString string to search for in text
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of assets that match the search string.
     *
     * @throws InvalidParameterException the searchString is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    List<String> findAssets(String   userId,
                            String   searchString,
                            int      startFrom,
                            int      pageSize) throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException;
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
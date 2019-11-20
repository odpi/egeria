<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# getAssetsByToken

```
    /**
     * Returns a list of assets that match the token. The following calls are issued in
     * in order for find the asset.
     * - getAssetProperties passing the token as the GUID
     * - getAssetByName passing the token as the name
     *
     * @param userId         userId of user making request.
     * @param assetToken     token used to find the Asset - may be a name or GUID
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return a list of unique identifiers for the matching assets
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<String> getAssetsByToken(String userId,
                                  String assetToken,
                                  int    startFrom,
                                  int    pageSize) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException;
```




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
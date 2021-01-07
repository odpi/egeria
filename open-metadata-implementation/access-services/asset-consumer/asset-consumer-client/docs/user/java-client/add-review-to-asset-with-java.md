<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# addRatingToAsset

```
    /**
     * Adds a star rating and optional review text to the asset.  If the user has already attached
     * a rating then the original one is over-ridden.
     *
     * @param userId      userId of user making request.
     * @param requestType   unique identifier for the asset.
     * @param starRating  StarRating enumeration for not recommended, one to five stars.
     * @param review      user review of asset.  This can be null.
     * @param isPublic    indicates whether the feedback should be shared or only be visible to the originating user
     **
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void addRatingToAsset(String     userId,
                          String     requestType,
                          StarRating starRating,
                          String     review,
                          boolean    isPublic) throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException;
```




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
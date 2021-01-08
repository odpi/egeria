<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# removeLikeFromAsset

```
    /**
     * Removes a "Like" added to the asset by this user.
     *
     * @param userId   userId of user making request.
     * @param requestType unique identifier for the asset where the like is attached.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   removeLikeFromAsset(String     userId,
                               String     requestType) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException;
```




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# removeComment

```
    /**
     * Removes a comment added to the asset by this user.
     *
     * @param userId       userId of user making request.
     * @param requestType    unique identifier for the asset at the head of this comment chain.
     * @param commentGUID  unique identifier for the comment object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    void removeComment(String     userId,
                       String     requestType,
                       String     commentGUID) throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException;
```




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
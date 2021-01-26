<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# updateComment

```
    /**
     * Update an existing comment.
     *
     * @param userId        userId of user making request.
     * @param requestType    unique identifier for the asset at the head of this comment chain.
     * @param commentGUID   unique identifier for the comment to change.
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     * @param isPublic      indicates whether the feedback should be shared or only be visible to the originating user
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   updateComment(String      userId,
                         String      requestType,
                         String      commentGUID,
                         CommentType commentType,
                         String      commentText,
                         boolean     isPublic) throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException;
```




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
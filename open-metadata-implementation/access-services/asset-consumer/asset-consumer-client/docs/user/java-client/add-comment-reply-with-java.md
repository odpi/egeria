<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# addCommentReply

```
    /**
     * Adds a comment to another comment.
     *
     * @param userId        userId of user making request.
     * @param requestType    unique identifier for the asset at the head of this comment chain.
     * @param commentGUID   unique identifier for an existing comment.  Used to add a reply to a comment.
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     * @param isPublic      indicates whether the feedback should be shared or only be visible to the originating user
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String addCommentReply(String      userId,
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
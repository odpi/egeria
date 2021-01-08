<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# addCommentToAsset

```
    /**
     * Adds a comment to the asset.
     *
     * @param userId        userId of user making request.
     * @param requestType     unique identifier for the asset.
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
    String addCommentToAsset(String      userId,
                             String      requestType,
                             CommentType commentType,
                             String      commentText,
                             boolean     isPublic) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException;

```




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
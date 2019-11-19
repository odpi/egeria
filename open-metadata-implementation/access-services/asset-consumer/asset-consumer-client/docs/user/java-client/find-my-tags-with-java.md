<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

# findMyTags

```
    /**
     * Return the list of the calling user's private tags containing the supplied string in either the name or description.
     *
     * @param userId the name of the calling user.
     * @param tag name of tag.  This may include wild card characters.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<InformalTag> findMyTags(String userId,
                                 String tag,
                                 int    startFrom,
                                 int    pageSize) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException;
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# updateTagDescription

```
    /**
     * Updates the description of an existing tag (either private or public).
     *
     * @param userId          userId of user making request.
     * @param tagGUID         unique identifier for the tag.
     * @param tagDescription  description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   updateTagDescription(String userId,
                                String tagGUID,
                                String tagDescription) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException;
```



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# getMeaning

```
    /**
     * Return the full definition (meaning) of a term using the unique identifier of the glossary term
     * that contains the definition.
     *
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the glossary term.
     *
     * @return meaning response object
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    GlossaryTerm getMeaning(String userId,
                            String guid) throws InvalidParameterException,
                                                PropertyServerException,
                                                UserNotAuthorizedException;
```




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# getMeaningByName

```
    /**
     * Return the full definition (meaning) of the terms exactly matching the supplied name.
     *
     * @param userId the name of the calling user.
     * @param term name of term.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return meaning list response or
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<GlossaryTerm> getMeaningByName(String userId,
                                        String term,
                                        int    startFrom,
                                        int    pageSize) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException;

```




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
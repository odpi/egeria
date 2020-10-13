<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# publish entities

 ```
    /**
     * Scan the cohort based on the given entity type and publish the contexts for the found entities to the out topic
     * Returns a list that contains entities GUID that were published out.
     *
     * @param serverName the server name
     * @param userId     the user id
     * @param entityType the entity type
     * @return the list of entities GUIDs that were published out
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<String> publishEntities(String serverName, String userId, String entityType) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException;
 ```
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# provide asset context

 ```
    /**
     * Provide the asset context of an entity - used for data files and relational tables.
     * @param userId     the caller user Id
     * @param guid       the guid of the entity
     * @param entityType the entity type
     * @return a RelationshipsContext     object containing the relationships that describe the entity's context
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws PropertyServerException    the property server exception
     * @throws UserNotAuthorizedException the user not authorized exception
     */
    RelationshipsContext provideAssetContext(String userId, String guid, String entityType)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;
 ```
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
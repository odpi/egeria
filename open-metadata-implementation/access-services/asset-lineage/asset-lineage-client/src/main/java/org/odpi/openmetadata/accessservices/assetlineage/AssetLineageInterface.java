/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The Asset Lineage OMAS provides services to query the lineage of business terms and data assets.
 */
public interface AssetLineageInterface {

    /**
     * Scan the cohort based on the given entity type and publish the contexts for the found entities to the out topic
     * Returns a list that contains entities GUID that were published out.
     *
     * @param serverName        the server name
     * @param userId            the user id
     * @param entityType        the entity type
     * @param updatedAfterDate  take into account just the entities updated after the specified date, if any (otherwise
     *                          it loads everything)
     * @return the list of entities GUIDs that were published out
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<String> publishEntities(String serverName, String userId, String entityType, Optional<LocalDateTime> updatedAfterDate)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

    /**
     * Publishes the asset context of an entity - used for data files and relational tables.
     * @param userId     the caller user Id
     * @param guid       the guid of the entity
     * @param entityType the entity type
     * @return list of the entities' guids in the asset context
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws PropertyServerException    the property server exception
     * @throws UserNotAuthorizedException the user not authorized exception
     */
    List<String> publishAssetContext(String userId, String guid, String entityType)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

}

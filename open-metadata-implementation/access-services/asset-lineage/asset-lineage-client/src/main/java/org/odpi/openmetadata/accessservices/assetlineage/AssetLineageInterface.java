/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage;

import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * The Asset Lineage OMAS provides services to query the lineage of business terms and data assets.
 */
public interface AssetLineageInterface {

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


    /**
     * Provide the asset context of an entity - used for data files and relational tables.
     *
     * @param serverName the server name
     * @param userId     the user id
     * @param guid       the guid
     * @param entityType the entity type
     * @return the map
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws PropertyServerException    the property server exception
     * @throws UserNotAuthorizedException the user not authorized exception
     */
    RelationshipsContext provideAssetContext(String serverName, String userId, String guid, String entityType)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

}

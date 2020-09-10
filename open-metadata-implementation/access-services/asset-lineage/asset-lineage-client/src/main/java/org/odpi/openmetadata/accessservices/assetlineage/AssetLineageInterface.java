/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage;

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
     *
     * @param serverName name of server instance to call
     * @param userId     the name of the calling user
     * @param entityType the name of the relationship type
     * @return a list of unique identifiers (guids) of the available entities with the given type provided as a response
     */
    List<String> publishEntities(String serverName, String userId, String entityType) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException;
}

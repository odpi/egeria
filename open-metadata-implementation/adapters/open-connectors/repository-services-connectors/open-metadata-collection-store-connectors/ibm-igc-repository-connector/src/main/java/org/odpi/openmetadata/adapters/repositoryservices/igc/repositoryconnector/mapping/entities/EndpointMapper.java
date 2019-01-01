/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.ConnectionEndpointMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndpointMapper extends ReferenceableMapper {

    private static final Logger log = LoggerFactory.getLogger(EndpointMapper.class);

    public EndpointMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "host",
                "Host",
                "Endpoint",
                userId
        );

        // IGC 'host_(engine)' is equivalent, so we need to ensure that it is also added to the assets to be
        // handled by this mapper
        addOtherIGCAssetType("host_(engine)");

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "name");
        addSimplePropertyMapping("short_description", "description");

        // This relationship can only be retrieved inverted
        // (relationship in IGC is cannot be traversed in other direction)
        addRelationshipMapper(ConnectionEndpointMapper.getInstance());

    }

}

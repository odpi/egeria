/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.ConnectionConnectorTypeMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.ConnectionEndpointMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.ConnectionToAssetMapper;

public class ConnectionMapper extends ReferenceableMapper {

    public ConnectionMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "data_connection",
                "Data Connection",
                "Connection",
                userId,
                null,
                false
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "name");
        addSimplePropertyMapping("short_description", "description");

        // The list of relationships that should be mapped
        addRelationshipMapper(ConnectionToAssetMapper.getInstance());
        addRelationshipMapper(ConnectionConnectorTypeMapper.getInstance());
        addRelationshipMapper(ConnectionEndpointMapper.getInstance());

    }

}

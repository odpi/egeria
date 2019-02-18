/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.AssetSchemaTypeMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.AssetSchemaTypeMapper_DatabaseSchema;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.DataContentForDataSetMapper;

/**
 * Defines the mapping to the OMRS "DeployedDatabaseSchema" entity.
 */
public class DeployedDatabaseSchemaMapper extends ReferenceableMapper {

    public DeployedDatabaseSchemaMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "database_schema",
                "Database Schema",
                "DeployedDatabaseSchema",
                userId
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "name");
        addSimplePropertyMapping("short_description", "description");

        // The list of relationships that should be mapped
        addRelationshipMapper(AssetSchemaTypeMapper_DatabaseSchema.getInstance());
        addRelationshipMapper(DataContentForDataSetMapper.getInstance());

    }

}

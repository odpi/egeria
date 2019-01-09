/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.AssetSchemaTypeMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.AssetSchemaTypeMapper_DatabaseSchema;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.AttributeForSchemaMapper_TableSchema;

/**
 * Defines the mapping to the OMRS "RelationalDBSchemaType" entity.
 */
public class RelationalDBSchemaTypeMapper extends ReferenceableMapper {

    public static final String IGC_RID_PREFIX = IGCOMRSMetadataCollection.generateTypePrefix("RDBST");

    public RelationalDBSchemaTypeMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "database_schema",
                "Database Schema",
                "RelationalDBSchemaType",
                userId,
                IGC_RID_PREFIX
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "displayName");

        // The list of relationships that should be mapped
        addRelationshipMapper(AssetSchemaTypeMapper_DatabaseSchema.getInstance());
        addRelationshipMapper(AttributeForSchemaMapper_TableSchema.getInstance());

    }

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.AssetSchemaTypeMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.AttributeForSchemaMapper_TableSchema;

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
        addRelationshipMapper(AssetSchemaTypeMapper.getInstance());
        /*addSimpleRelationshipMapping(
                RelationshipMappingSet.SELF_REFERENCE_SENTINEL,
                "AssetSchemaType",
                "schema",
                "describesAssets",
                IGC_RID_PREFIX,
                null
        );*/
        addRelationshipMapper(AttributeForSchemaMapper_TableSchema.getInstance());

/*        // Given a schema may have many tables, this is likely to be more optimal way to retrieve these
        // relationships
        addInvertedRelationshipMapping(
                "database_table",
                "database_schema",
                "AttributeForSchema",
                "attributes",
                "parentSchemas",
                null,
                IGC_RID_PREFIX
        );*/

    }

}

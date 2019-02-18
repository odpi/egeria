/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.AttributeForSchemaMapper_TableColumn;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.SchemaAttributeTypeMapper_DatabaseTable;

/**
 * Defines the mapping to the OMRS "RelationalTableType" entity.
 */
public class RelationalTableTypeMapper extends ReferenceableMapper {

    public static final String IGC_RID_PREFIX = IGCOMRSMetadataCollection.generateTypePrefix("RTT");

    public RelationalTableTypeMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "database_table",
                "Database Table",
                "RelationalTableType",
                userId,
                IGC_RID_PREFIX
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "displayName");

        // The list of relationships that should be mapped
        addRelationshipMapper(SchemaAttributeTypeMapper_DatabaseTable.getInstance());
        addRelationshipMapper(AttributeForSchemaMapper_TableColumn.getInstance());

    }

}

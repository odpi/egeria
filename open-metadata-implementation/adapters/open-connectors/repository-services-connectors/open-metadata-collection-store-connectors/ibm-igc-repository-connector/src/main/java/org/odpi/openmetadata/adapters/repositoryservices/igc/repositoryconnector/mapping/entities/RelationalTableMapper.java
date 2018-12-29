/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.AttributeForSchemaMapper_TableSchema;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.SchemaAttributeTypeMapper_DatabaseTable;

public class RelationalTableMapper extends ReferenceableMapper {

    public RelationalTableMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "database_table",
                "Database Table",
                "RelationalTable",
                userId
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "name");

        // The list of relationships that should be mapped
        addRelationshipMapper(AttributeForSchemaMapper_TableSchema.getInstance());
        addRelationshipMapper(SchemaAttributeTypeMapper_DatabaseTable.getInstance());

    }

}

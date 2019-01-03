/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.classifications.PrimaryKeyMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.AttributeForSchemaMapper_TableColumn;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.DataClassAssignmentMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.ForeignKeyMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.SchemaAttributeTypeMapper_DatabaseColumn;

public class RelationalColumnMapper extends ReferenceableMapper {

    public RelationalColumnMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "database_column",
                "Database Column",
                "RelationalColumn",
                userId
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "name");
        addSimplePropertyMapping("position", "position");
        addSimplePropertyMapping("minimum_length", "minimumLength");
        addSimplePropertyMapping("length", "length");
        addSimplePropertyMapping("fraction", "fraction");
        addSimplePropertyMapping("allows_null_values", "isNullable");
        addSimplePropertyMapping("unique", "isUnique");

        // The list of relationships that should be mapped
        addRelationshipMapper(AttributeForSchemaMapper_TableColumn.getInstance());
        addRelationshipMapper(SchemaAttributeTypeMapper_DatabaseColumn.getInstance());
        addRelationshipMapper(ForeignKeyMapper.getInstance());
        addRelationshipMapper(DataClassAssignmentMapper.getInstance());

        // The list of classifications that should be mapped
        addClassificationMapper(PrimaryKeyMapper.getInstance());

    }

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.classifications.PrimaryKeyMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.*;

public class TabularColumnMapper extends ReferenceableMapper {

    public TabularColumnMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "data_file_field",
                "Data File Field",
                "TabularColumn",
                userId
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "name");
        addSimplePropertyMapping("position", "position");

        // The list of relationships that should be mapped
        addRelationshipMapper(AttributeForSchemaMapper_RecordField.getInstance());
        addRelationshipMapper(SchemaAttributeTypeMapper_FileField.getInstance());
        addRelationshipMapper(DataClassAssignmentMapper.getInstance());

    }

}

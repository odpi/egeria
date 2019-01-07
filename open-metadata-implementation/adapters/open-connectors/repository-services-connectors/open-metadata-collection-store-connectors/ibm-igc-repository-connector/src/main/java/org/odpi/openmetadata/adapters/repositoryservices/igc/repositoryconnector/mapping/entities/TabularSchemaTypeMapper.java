/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.AssetSchemaTypeMapper_FileRecord;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.AttributeForSchemaMapper_RecordField;

public class TabularSchemaTypeMapper extends ReferenceableMapper {

    public TabularSchemaTypeMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "data_file_record",
                "Data File Record",
                "TabularSchemaType",
                userId
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "displayName");

        // The list of relationships that should be mapped
        addRelationshipMapper(AssetSchemaTypeMapper_FileRecord.getInstance());
        addRelationshipMapper(AttributeForSchemaMapper_RecordField.getInstance());

    }

}

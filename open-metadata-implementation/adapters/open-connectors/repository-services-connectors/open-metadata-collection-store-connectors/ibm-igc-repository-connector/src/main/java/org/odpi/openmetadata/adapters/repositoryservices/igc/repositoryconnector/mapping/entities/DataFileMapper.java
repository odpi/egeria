/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.*;

/**
 * Defines the mapping to the OMRS "DataFile" entity.
 */
public class DataFileMapper extends ReferenceableMapper {

    public DataFileMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "data_file",
                "Data File",
                "DataFile",
                userId
        );

        // The list of properties that should be mapped, none other than qualifiedName

        // The list of relationships that should be mapped
        addRelationshipMapper(AssetSchemaTypeMapper_FileRecord.getInstance());
        addRelationshipMapper(NestedFileMapper.getInstance());

    }

}

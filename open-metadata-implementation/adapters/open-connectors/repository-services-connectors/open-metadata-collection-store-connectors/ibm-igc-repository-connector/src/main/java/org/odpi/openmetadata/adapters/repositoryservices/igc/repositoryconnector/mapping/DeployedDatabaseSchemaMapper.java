/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;

public class DeployedDatabaseSchemaMapper extends ReferenceableMapper {

    /**
     * Sets the basic criteria to use for mapping between an IGC 'database_schema' object and an OMRS 'DeployedDatabaseSchema' object.
     *
     * @param dbSchema the IGC 'database_schema' object
     * @param igcomrsRepositoryConnector the IGC repository connector to use for retrieving any additional info required
     * @param userId the userId of the user doing any further detailed information retrievals (currently unused)
     */
    public DeployedDatabaseSchemaMapper(Reference dbSchema, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                dbSchema,
                "database_schema",
                "DeployedDatabaseSchema",
                igcomrsRepositoryConnector,
                userId
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "name");
        addSimplePropertyMapping("short_description", "description");

        // The list of relationships that should be mapped
        addSimpleRelationshipMapping(
                RelationshipMappingSet.SELF_REFERENCE_SENTINEL,
                "AssetSchemaType",
                "describesAssets",
                "schema",
                null,
                RelationalDBSchemaTypeMapper.IGC_RID_PREFIX
        );
        addSimpleRelationshipMapping(
                "database",
                "DataContentForDataSet",
                "supportedDataSets",
                "dataContent"
        );

    }

    /**
     * No classifications to map for RelationalTableType
     */
    @Override
    protected void getMappedClassifications() {
        // Nothing to do
    }

}

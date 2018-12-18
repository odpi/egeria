/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;

public class RelationalDBSchemaTypeMapper extends ReferenceableMapper {

    public static final String IGC_RID_PREFIX = IGCOMRSMetadataCollection.generateTypePrefix("RDBST");

    /**
     * Sets the basic criteria to use for mapping between an IGC 'database_schema' object and an OMRS 'RelationalDBSchemaType' object.
     *
     * @param dbSchema the IGC 'database_schema' object
     * @param igcomrsRepositoryConnector the IGC repository connector to use for retrieving any additional info required
     * @param userId the userId of the user doing any further detailed information retrievals (currently unused)
     */
    public RelationalDBSchemaTypeMapper(Reference dbSchema, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                dbSchema,
                "database_schema",
                "RelationalDBSchemaType",
                igcomrsRepositoryConnector,
                userId
        );

        setIgcRidPrefix(IGC_RID_PREFIX);

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "displayName");

        // The list of relationships that should be mapped
        addSimpleRelationshipMapping(
                RelationshipMappingSet.SELF_REFERENCE_SENTINEL,
                "AssetSchemaType",
                "schema",
                "describesAssets",
                IGC_RID_PREFIX,
                null
        );

        // Given a schema may have many tables, this is likely to be more optimal way to retrieve these
        // relationships
        addInvertedRelationshipMapping(
                "database_table",
                "database_schema",
                "AttributeForSchema",
                "attributes",
                "parentSchemas",
                null,
                IGC_RID_PREFIX
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

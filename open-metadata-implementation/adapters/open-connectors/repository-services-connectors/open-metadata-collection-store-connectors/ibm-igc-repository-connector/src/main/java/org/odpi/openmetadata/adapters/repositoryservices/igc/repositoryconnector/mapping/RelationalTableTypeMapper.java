/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;

public class RelationalTableTypeMapper extends ReferenceableMapper {

    public static final String IGC_RID_PREFIX = IGCOMRSMetadataCollection.generateTypePrefix("RTT");

    /**
     * Sets the basic criteria to use for mapping between an IGC 'database_table' object and an OMRS 'RelationalTableType' object.
     *
     * @param dbTable the IGC 'database_table' object
     * @param igcomrsRepositoryConnector the IGC repository connector to use for retrieving any additional info required
     * @param userId the userId of the user doing any further detailed information retrievals (currently unused)
     */
    public RelationalTableTypeMapper(Reference dbTable, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                dbTable,
                "database_table",
                "RelationalTableType",
                igcomrsRepositoryConnector,
                userId
        );

        setIgcRidPrefix(IGC_RID_PREFIX);

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "displayName");

        // The list of relationships that should be mapped
        addSimpleRelationshipMapping(
                RelationshipMappingSet.SELF_REFERENCE_SENTINEL,
                "SchemaAttributeType",
                "usedInSchemas",
                "type",
                null,
                IGC_RID_PREFIX
        );

        // Given a table may have many columns, this is likely to be more optimal way to retrieve
        // these relationships
        addInvertedRelationshipMapping(
                "database_column",
                "database_table_or_view",
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

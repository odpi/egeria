/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RelationalColumnTypeMapper extends ReferenceableMapper {

    private static final Logger log = LoggerFactory.getLogger(RelationalColumnTypeMapper.class);

    public static final String IGC_RID_PREFIX = IGCOMRSMetadataCollection.generateTypePrefix("RCT");

    /**
     * Sets the basic criteria to use for mapping between an IGC 'database_column' object and an OMRS 'RelationalColumnType' object.
     *
     * @param dbColumn the IGC 'database_column' object
     * @param igcomrsRepositoryConnector the IGC repository connector to use for retrieving any additional info required
     * @param userId the userId of the user doing any further detailed information retrievals (currently unused)
     */
    public RelationalColumnTypeMapper(Reference dbColumn, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                dbColumn,
                "database_column",
                "RelationalColumnType",
                igcomrsRepositoryConnector,
                userId
        );

        setIgcRidPrefix(IGC_RID_PREFIX);

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "displayName");
        addSimplePropertyMapping("type", "dataType");

        // The list of relationships that should be mapped
        addSimpleRelationshipMapping(
                RelationshipMappingSet.SELF_REFERENCE_SENTINEL,
                "SchemaAttributeType",
                "usedInSchemas",
                "type",
                null,
                IGC_RID_PREFIX
        );

    }

    /**
     * No classifications to map for RelationalColumnType
     */
    @Override
    protected void getMappedClassifications() {
        // Nothing to do
    }

}

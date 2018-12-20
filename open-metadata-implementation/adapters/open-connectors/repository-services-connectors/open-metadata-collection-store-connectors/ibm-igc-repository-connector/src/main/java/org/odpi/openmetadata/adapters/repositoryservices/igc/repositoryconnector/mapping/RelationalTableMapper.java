/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;

public class RelationalTableMapper extends ReferenceableMapper {

    private static final String T_RELATIONAL_TABLE = "RelationalTable";

    /**
     * Sets the basic criteria to use for mapping between an IGC 'database_table' object and an OMRS 'RelationalTable' object.
     *
     * @param dbTable the IGC 'database_table' object
     * @param igcomrsRepositoryConnector the IGC repository connector to use for retrieving any additional info required
     * @param userId the userId of the user doing any further detailed information retrievals (currently unused)
     */
    public RelationalTableMapper(Reference dbTable, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                dbTable,
                "database_table",
                T_RELATIONAL_TABLE,
                igcomrsRepositoryConnector,
                userId
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "name");

        // The list of relationships that should be mapped
        addSimpleRelationshipMapping(
                "database_schema",
                "AttributeForSchema",
                "attributes",
                "parentSchemas",
                null,
                RelationalDBSchemaTypeMapper.IGC_RID_PREFIX
        );
        addSimpleRelationshipMapping(
                RelationshipMappingSet.SELF_REFERENCE_SENTINEL,
                "SchemaAttributeType",
                "usedInSchemas",
                "type",
                null,
                RelationalTableTypeMapper.IGC_RID_PREFIX
        );

        // Finally list any properties that will be used to map Classifications
        // (to do the actual mapping, implement the 'getMappedClassifications' function -- example below)

    }

    /**
     * We implement this method to apply any classifications -- since IGC itself doesn't have a "Classification"
     * asset type, we need to apply our own translation between how we're using other IGC asset types and the
     * Classification(s) we want them to represent in OMRS.
     * <br><br>
     * Nothing to do for RelationalTable entities.
     */
    @Override
    protected void getMappedClassifications() {
        // Nothing to do...
    }

}

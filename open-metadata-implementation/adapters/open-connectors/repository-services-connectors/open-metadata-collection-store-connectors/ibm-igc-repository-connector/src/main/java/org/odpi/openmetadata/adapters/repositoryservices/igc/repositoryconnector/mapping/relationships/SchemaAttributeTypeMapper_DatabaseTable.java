/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.RelationalTableTypeMapper;

public class SchemaAttributeTypeMapper_DatabaseTable extends RelationshipMapping {

    private static SchemaAttributeTypeMapper_DatabaseTable instance = new SchemaAttributeTypeMapper_DatabaseTable();
    public static SchemaAttributeTypeMapper_DatabaseTable getInstance() { return instance; }

    private SchemaAttributeTypeMapper_DatabaseTable() {
        super(
                "database_table",
                "database_table",
                SELF_REFERENCE_SENTINEL,
                SELF_REFERENCE_SENTINEL,
                "SchemaAttributeType",
                "usedInSchemas",
                "type",
                null,
                RelationalTableTypeMapper.IGC_RID_PREFIX
        );
    }

}

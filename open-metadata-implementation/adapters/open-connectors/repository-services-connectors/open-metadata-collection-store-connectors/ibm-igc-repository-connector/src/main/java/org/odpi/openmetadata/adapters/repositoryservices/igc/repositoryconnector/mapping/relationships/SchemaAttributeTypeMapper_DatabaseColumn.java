/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.RelationalColumnTypeMapper;

public class SchemaAttributeTypeMapper_DatabaseColumn extends RelationshipMapping {

    private static SchemaAttributeTypeMapper_DatabaseColumn instance = new SchemaAttributeTypeMapper_DatabaseColumn();
    public static SchemaAttributeTypeMapper_DatabaseColumn getInstance() { return instance; }

    private SchemaAttributeTypeMapper_DatabaseColumn() {
        super(
                "database_column",
                "database_column",
                SELF_REFERENCE_SENTINEL,
                SELF_REFERENCE_SENTINEL,
                "SchemaAttributeType",
                "usedInSchemas",
                "type",
                null,
                RelationalColumnTypeMapper.IGC_RID_PREFIX
        );
    }

}

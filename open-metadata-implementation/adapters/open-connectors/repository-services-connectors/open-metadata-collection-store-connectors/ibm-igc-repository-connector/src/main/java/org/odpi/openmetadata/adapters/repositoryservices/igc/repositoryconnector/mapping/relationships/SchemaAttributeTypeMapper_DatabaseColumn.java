/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.RelationalColumnTypeMapper;

public class SchemaAttributeTypeMapper_DatabaseColumn extends RelationshipMapping {

    private static class Singleton {
        private static final SchemaAttributeTypeMapper_DatabaseColumn INSTANCE = new SchemaAttributeTypeMapper_DatabaseColumn();
    }
    public static SchemaAttributeTypeMapper_DatabaseColumn getInstance() {
        return Singleton.INSTANCE;
    }

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

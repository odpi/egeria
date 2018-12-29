/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.RelationalDBSchemaTypeMapper;

public class AttributeForSchemaMapper_TableSchema extends RelationshipMapping {

    private static AttributeForSchemaMapper_TableSchema instance = new AttributeForSchemaMapper_TableSchema();
    public static AttributeForSchemaMapper_TableSchema getInstance() { return instance; }

    private AttributeForSchemaMapper_TableSchema() {
        super(
                "database_schema",
                "database_table",
                "database_tables",
                "database_schema",
                "AttributeForSchema",
                "parentSchemas",
                "attributes",
                RelationalDBSchemaTypeMapper.IGC_RID_PREFIX,
                null
        );
    }

}

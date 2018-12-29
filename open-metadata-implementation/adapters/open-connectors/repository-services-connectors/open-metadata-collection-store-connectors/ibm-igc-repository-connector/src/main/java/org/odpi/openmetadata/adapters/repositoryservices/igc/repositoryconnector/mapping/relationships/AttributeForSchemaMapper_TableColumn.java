/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.RelationalTableTypeMapper;

public class AttributeForSchemaMapper_TableColumn extends RelationshipMapping {

    private static class Singleton {
        private static final AttributeForSchemaMapper_TableColumn INSTANCE = new AttributeForSchemaMapper_TableColumn();
    }
    public static AttributeForSchemaMapper_TableColumn getInstance() {
        return Singleton.INSTANCE;
    }

    private AttributeForSchemaMapper_TableColumn() {
        super(
                "database_table",
                "database_column",
                "database_columns",
                "database_table_or_view",
                "AttributeForSchema",
                "parentSchemas",
                "attributes",
                RelationalTableTypeMapper.IGC_RID_PREFIX,
                null
        );
    }

}

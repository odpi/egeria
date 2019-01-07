/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

public class AttributeForSchemaMapper extends RelationshipMapping {

    private static class Singleton {
        private static final AttributeForSchemaMapper INSTANCE = new AttributeForSchemaMapper();
    }
    public static AttributeForSchemaMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private AttributeForSchemaMapper() {
        super(
                "",
                "",
                "",
                "",
                "AttributeForSchema",
                "parentSchemas",
                "attributes"
        );
        addSubType(AttributeForSchemaMapper_TableSchema.getInstance());
        addSubType(AttributeForSchemaMapper_TableColumn.getInstance());
        addSubType(AttributeForSchemaMapper_RecordField.getInstance());
    }

}

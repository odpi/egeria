/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to map the OMRS "AttributeForSchemaMapper" relationship.
 * @see AttributeForSchemaMapper_RecordField
 * @see AttributeForSchemaMapper_TableColumn
 * @see AttributeForSchemaMapper_TableSchema
 */
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

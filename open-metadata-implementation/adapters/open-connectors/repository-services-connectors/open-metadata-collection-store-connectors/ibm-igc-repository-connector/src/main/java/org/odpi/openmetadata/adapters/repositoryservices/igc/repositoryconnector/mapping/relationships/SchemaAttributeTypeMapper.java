/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to map the OMRS "SchemaAttributeType" relationship.
 * @see SchemaAttributeTypeMapper_DatabaseColumn
 * @see SchemaAttributeTypeMapper_DatabaseTable
 * @see SchemaAttributeTypeMapper_FileField
 */
public class SchemaAttributeTypeMapper extends RelationshipMapping {

    private static class Singleton {
        private static final SchemaAttributeTypeMapper INSTANCE = new SchemaAttributeTypeMapper();
    }
    public static SchemaAttributeTypeMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private SchemaAttributeTypeMapper() {
        super(
                "",
                "",
                "",
                "",
                "SchemaAttributeType",
                "usedInSchemas",
                "type"
        );
        addSubType(SchemaAttributeTypeMapper_DatabaseTable.getInstance());
        addSubType(SchemaAttributeTypeMapper_DatabaseTable.getInstance());
        addSubType(SchemaAttributeTypeMapper_FileField.getInstance());
    }

}

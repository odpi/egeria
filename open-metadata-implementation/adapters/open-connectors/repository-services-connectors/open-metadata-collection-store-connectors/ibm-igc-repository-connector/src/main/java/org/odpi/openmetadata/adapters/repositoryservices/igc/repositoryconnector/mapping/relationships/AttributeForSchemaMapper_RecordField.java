/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

public class AttributeForSchemaMapper_RecordField extends RelationshipMapping {

    private static class Singleton {
        private static final AttributeForSchemaMapper_RecordField INSTANCE = new AttributeForSchemaMapper_RecordField();
    }
    public static AttributeForSchemaMapper_RecordField getInstance() {
        return Singleton.INSTANCE;
    }

    private AttributeForSchemaMapper_RecordField() {
        super(
                "data_file_record",
                "data_file_field",
                "data_file_fields",
                "data_file_record",
                "AttributeForSchema",
                "parentSchemas",
                "attributes"
        );
    }

}

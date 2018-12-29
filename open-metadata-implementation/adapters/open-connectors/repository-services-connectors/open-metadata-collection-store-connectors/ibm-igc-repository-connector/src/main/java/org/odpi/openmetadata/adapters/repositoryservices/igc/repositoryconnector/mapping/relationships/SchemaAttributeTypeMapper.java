/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

public class SchemaAttributeTypeMapper extends RelationshipMapping {

    private static SchemaAttributeTypeMapper instance = new SchemaAttributeTypeMapper();
    public static SchemaAttributeTypeMapper getInstance() { return instance; }

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
    }

}

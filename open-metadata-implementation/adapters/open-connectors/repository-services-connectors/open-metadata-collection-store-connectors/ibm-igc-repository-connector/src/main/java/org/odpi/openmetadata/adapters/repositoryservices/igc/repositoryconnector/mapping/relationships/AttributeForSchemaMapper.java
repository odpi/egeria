/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

public class AttributeForSchemaMapper extends RelationshipMapping {

    private static AttributeForSchemaMapper instance = new AttributeForSchemaMapper();
    public static AttributeForSchemaMapper getInstance() { return instance; }

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
    }

}

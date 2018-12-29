/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

public class ForeignKeyMapper extends RelationshipMapping {

    private static ForeignKeyMapper instance = new ForeignKeyMapper();
    public static ForeignKeyMapper getInstance() { return instance; }

    private ForeignKeyMapper() {
        super(
                "database_column",
                "database_column",
                "defined_foreign_key_referenced",
                "defined_foreign_key_references",
                "ForeignKey",
                "primaryKey",
                "foreignKey"
        );
        addAlternativePropertyFromOne("selected_foreign_key_referenced");
        addAlternativePropertyFromTwo("selected_foreign_key_references");
    }

}

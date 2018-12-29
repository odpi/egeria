/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to represent the DataClassHierarchy relationship in OMRS.
 */
public class DataClassHierarchyMapper extends RelationshipMapping {

    private static DataClassHierarchyMapper instance = new DataClassHierarchyMapper();
    public static DataClassHierarchyMapper getInstance() { return instance; }

    private DataClassHierarchyMapper() {
        super(
                "data_class",
                "data_class",
                "contains_data_classes",
                "parent_data_class",
                "DataClassHierarchy",
                "superDataClass",
                "subDataClasses"
        );
    }

}

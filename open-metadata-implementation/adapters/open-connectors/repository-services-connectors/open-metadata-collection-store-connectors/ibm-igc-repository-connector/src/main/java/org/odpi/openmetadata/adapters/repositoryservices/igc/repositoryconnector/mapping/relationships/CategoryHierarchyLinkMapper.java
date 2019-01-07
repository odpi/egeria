/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to map the OMRS "CategoryHierarchyLink" relationship for IGC "category" assets.
 */
public class CategoryHierarchyLinkMapper extends RelationshipMapping {

    private static class Singleton {
        private static final CategoryHierarchyLinkMapper INSTANCE = new CategoryHierarchyLinkMapper();
    }
    public static CategoryHierarchyLinkMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private CategoryHierarchyLinkMapper() {
        super(
                "category",
                "category",
                "subcategories",
                "parent_category",
                "CategoryHierarchyLink",
                "superCategory",
                "subcategories"
        );
    }

}

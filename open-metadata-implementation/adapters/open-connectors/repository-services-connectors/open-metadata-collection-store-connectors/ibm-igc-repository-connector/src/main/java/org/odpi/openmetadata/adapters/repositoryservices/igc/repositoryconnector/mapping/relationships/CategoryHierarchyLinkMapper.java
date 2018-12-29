/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to represent the CategoryHierarchyLink relationship in OMRS.
 */
public class CategoryHierarchyLinkMapper extends RelationshipMapping {

    private static CategoryHierarchyLinkMapper instance = new CategoryHierarchyLinkMapper();
    public static CategoryHierarchyLinkMapper getInstance() { return instance; }

    private CategoryHierarchyLinkMapper() {
        // TODO: ensure that in cases like these where the 'type' is the same for both ends of the proxy, that
        //  we look at relationships in both directions from a given object of that type (ie. we use BOTH the
        //  igcRelationshipPropertyFromOne and the igcRelationshipPropertyFromTwo properties)
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

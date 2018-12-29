/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to represent the TermCategorization relationship in OMRS.
 */
public class TermCategorizationMapper extends RelationshipMapping {

    private static TermCategorizationMapper instance = new TermCategorizationMapper();
    public static TermCategorizationMapper getInstance() { return instance; }

    private TermCategorizationMapper() {
        super(
                "category",
                "term",
                "terms",
                "parent_category",
                "TermCategorization",
                "categories",
                "terms"
        );
        addAlternativePropertyFromTwo("referencing_categories");
    }

}

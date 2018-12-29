/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to represent the SemanticAssignment relationship in OMRS.
 */
public class SemanticAssignmentMapper extends RelationshipMapping {

    private static class Singleton {
        private static final SemanticAssignmentMapper INSTANCE = new SemanticAssignmentMapper();
    }
    public static SemanticAssignmentMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private SemanticAssignmentMapper() {
        super(
                "main_object",
                "term",
                "assigned_to_terms",
                "assigned_assets",
                "SemanticAssignment",
                "assignedElements",
                "meaning"
        );
    }

}

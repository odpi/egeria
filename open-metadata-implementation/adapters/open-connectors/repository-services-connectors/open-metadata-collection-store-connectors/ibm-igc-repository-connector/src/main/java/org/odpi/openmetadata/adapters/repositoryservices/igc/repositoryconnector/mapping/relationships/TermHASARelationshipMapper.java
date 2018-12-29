/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to represent the SemanticAssignment relationship in OMRS.
 */
public class TermHASARelationshipMapper extends RelationshipMapping {

    private static class Singleton {
        private static final TermHASARelationshipMapper INSTANCE = new TermHASARelationshipMapper();
    }
    public static TermHASARelationshipMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private TermHASARelationshipMapper() {
        super(
                "term",
                "term",
                "has_a",
                "is_of",
                "TermHASARelationship",
                "objects",
                "attributes"
        );
    }

}

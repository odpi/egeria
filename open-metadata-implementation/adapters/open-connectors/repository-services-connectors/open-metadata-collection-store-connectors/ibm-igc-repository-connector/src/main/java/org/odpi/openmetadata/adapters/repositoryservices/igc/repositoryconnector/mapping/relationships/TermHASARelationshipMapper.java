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

    // TODO: has_a_term is necessary for search, but may not reveal inherited locations
    //  (only possible via has_a, which is not searchable)
    //  - if we need inherited locations this will likely need to become a custom mapping

    private TermHASARelationshipMapper() {
        super(
                "term",
                "term",
                "has_a_term",
                "is_of",
                "TermHASARelationship",
                "objects",
                "attributes"
        );
    }

}

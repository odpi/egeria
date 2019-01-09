/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to map the OMRS "TermISATypeOFRelationship" relationship for IGC "term" assets.
 */
public class TermISATypeOFRelationshipMapper extends RelationshipMapping {

    private static class Singleton {
        private static final TermISATypeOFRelationshipMapper INSTANCE = new TermISATypeOFRelationshipMapper();
    }
    public static TermISATypeOFRelationshipMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private TermISATypeOFRelationshipMapper() {
        super(
                "term",
                "term",
                "has_types",
                "is_a_type_of",
                "TermISATypeOFRelationship",
                "supertypes",
                "subtypes"
        );
    }

}

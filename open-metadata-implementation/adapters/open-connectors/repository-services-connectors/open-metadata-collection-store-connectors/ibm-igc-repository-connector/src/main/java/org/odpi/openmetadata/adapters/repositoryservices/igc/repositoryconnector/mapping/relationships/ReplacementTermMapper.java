/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to map the OMRS "ReplacementTerm" relationship for IGC "term" assets.
 */
public class ReplacementTermMapper extends RelationshipMapping {

    private static class Singleton {
        private static final ReplacementTermMapper INSTANCE = new ReplacementTermMapper();
    }
    public static ReplacementTermMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private ReplacementTermMapper() {
        super(
                "term",
                "term",
                "replaced_by",
                "replaces",
                "ReplacementTerm",
                "replacedTerms",
                "replacementTerms"
        );
    }

}

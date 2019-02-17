/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to map the OMRS "Synonym" relationship for IGC "term" assets.
 */
public class SynonymMapper extends RelationshipMapping {

    private static class Singleton {
        private static final SynonymMapper INSTANCE = new SynonymMapper();
    }
    public static SynonymMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private static final String P_SYNONYMS = "synonyms";

    private SynonymMapper() {
        super(
                "term",
                "term",
                P_SYNONYMS,
                P_SYNONYMS,
                "Synonym",
                P_SYNONYMS,
                P_SYNONYMS
        );
    }

}

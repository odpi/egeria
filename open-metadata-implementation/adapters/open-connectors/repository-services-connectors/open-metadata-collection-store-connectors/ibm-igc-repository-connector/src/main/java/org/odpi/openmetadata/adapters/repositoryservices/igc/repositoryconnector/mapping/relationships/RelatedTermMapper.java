/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to map the OMRS "RelatedTerm" relationship for IGC "term" assets.
 */
public class RelatedTermMapper extends RelationshipMapping {

    private static class Singleton {
        private static final RelatedTermMapper INSTANCE = new RelatedTermMapper();
    }
    public static RelatedTermMapper getInstance() {
        return Singleton.INSTANCE;
    }

    // TODO: this seems to result in an infinite loop of events -- probably the same for all such examples
    //  where the types and properties match each other?  (Or was this a left-over from ignoring assigned_to_terms
    //  when initially loading the environment? Reload a fresh environment and re-test...)
    private RelatedTermMapper() {
        super(
                "term",
                "term",
                "related_terms",
                "related_terms",
                "RelatedTerm",
                "seeAlso",
                "seeAlso"
        );
    }

}

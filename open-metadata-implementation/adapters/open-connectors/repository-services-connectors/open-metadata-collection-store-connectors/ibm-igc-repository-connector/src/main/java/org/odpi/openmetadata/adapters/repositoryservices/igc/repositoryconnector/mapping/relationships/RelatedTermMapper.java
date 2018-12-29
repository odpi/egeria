/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to represent the RelatedTerm relationship in OMRS.
 */
public class RelatedTermMapper extends RelationshipMapping {

    private static RelatedTermMapper instance = new RelatedTermMapper();
    public static RelatedTermMapper getInstance() { return instance; }

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

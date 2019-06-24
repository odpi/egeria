/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;

/**
 * Server-side implementation of the Glossary View Open Metadata Access Service (OMAS).
 * Deals in extracting terms.
 */
public class TermService extends GlossaryViewOMAS {

    private static final String TERM_TYPE_NAME = "GlossaryTerm";
    private static final String CATEGORY_TYPE_NAME = "GlossaryCategory";

    private static final String TERM_CATEGORIZATION_RELATIONSHIP_NAME = "TermCategorization";
    private static final String TERM_CATEGORIZATION_RELATIONSHIP_GUID = "696a81f5-ac60-46c7-b9fd-6979a1e7ad27";

    private static final String LIBRARY_TERM_REFERENCE_RELATIONSHIP_NAME = "LibraryTermReference";
    private static final String LIBRARY_TERM_REFERENCE_RELATIONSHIP_GUID = "38c346e4-ddd2-42ef-b4aa-55d53c078d22";

    private static final String TERM_ANCHOR_RELATIONSHIP_NAME = "TermAnchor";
    private static final String TERM_ANCHOR_RELATIONSHIP_GUID = "1d43d661-bdc7-4a91-a996-3239b8f82e56";

    public TermService() {}

    /**
     * Extract the term definition for the given GUID
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param termGUID term GUID
     *
     * @return EntityDetailResponse term
     */
    public GlossaryViewEntityDetailResponse getTerm(String userId, String serverName, String termGUID){
        return getEntityDetailResponse(userId, serverName, termGUID, TERM_TYPE_NAME, "getTerm");
    }

    /**
     * Extract all term definitions for the given category
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param categoryGUID category GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse terms
     */
    public GlossaryViewEntityDetailResponse getTerms(String userId, String serverName, String categoryGUID, Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, categoryGUID, CATEGORY_TYPE_NAME,
                TERM_CATEGORIZATION_RELATIONSHIP_GUID, TERM_CATEGORIZATION_RELATIONSHIP_NAME, from, size, "getTerms");
    }

    /**
     * Extract term definitions for the given glossary GUID via the 'TermAnchor' type relationships
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param glossaryGUID glossary GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse all external glossaries
     */
    public GlossaryViewEntityDetailResponse getTermsViaTermAnchorRelationships(String userId, String serverName,
                                                                               String glossaryGUID, Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, glossaryGUID, TERM_TYPE_NAME, TERM_ANCHOR_RELATIONSHIP_GUID,
                TERM_ANCHOR_RELATIONSHIP_NAME, from, size, "getTermsViaTermAnchorRelationships");
    }

    /**
     * Extract term definitions for the given GUID via the 'TermCategorization' type relationships
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param categoryGUID category GUID
     * @param from from
     * @param size size
     *
     * @return terms
     */
    public GlossaryViewEntityDetailResponse getTermsViaTermCategorizationRelationships(String userId, String serverName,
                                                                                       String categoryGUID,
                                                                                       Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, categoryGUID, TERM_TYPE_NAME,
                TERM_CATEGORIZATION_RELATIONSHIP_GUID, TERM_CATEGORIZATION_RELATIONSHIP_NAME, from, size,
                "getTermsViaTermCategorizationRelationships");
    }

    /**
     * Extract external glossary definitions for the given term
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param termGUID glossary GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse all external glossaries
     */
    public GlossaryViewEntityDetailResponse getExternalGlossaries(String userId, String serverName, String termGUID,
                                                                  Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME,
                LIBRARY_TERM_REFERENCE_RELATIONSHIP_GUID, LIBRARY_TERM_REFERENCE_RELATIONSHIP_NAME, from, size,
                "getExternalGlossaries");
    }

}

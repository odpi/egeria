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

    private static final String RELATED_TERM_RELATIONSHIP_NAME = "RelatedTerm";
    private static final String RELATED_TERM_RELATIONSHIP_GUID = "b1161696-e563-4cf9-9fd9-c0c76e47d063";

    private static final String SYNONYM_RELATIONSHIP_NAME = "Synonym";
    private static final String SYNONYM_RELATIONSHIP_GUID = "74f4094d-dba2-4ad9-874e-d422b69947e2";

    private static final String ANTONYM_RELATIONSHIP_NAME = "Antonym";
    private static final String ANTONYM_RELATIONSHIP_GUID = "ea5e126a-a8fa-4a43-bcfa-309a98aa0185";

    private static final String PREFERRED_TERM_RELATIONSHIP_NAME = "PreferredTerm";
    private static final String PREFERRED_TERM_RELATIONSHIP_GUID = "8ac8f9de-9cdd-4103-8a33-4cb204b78c2a";

    private static final String REPLACEMENT_TERM_RELATIONSHIP_NAME = "ReplacementTerm";
    private static final String REPLACEMENT_TERM_RELATIONSHIP_GUID = "3bac5f35-328b-4bbd-bfc9-3b3c9ba5e0ed";

    private static final String TRANSLATION_RELATIONSHIP_NAME = "Translation";
    private static final String TRANSLATION_RELATIONSHIP_GUID = "6ae42e95-efc5-4256-bfa8-801140a29d2a";

    private static final String IS_A_RELATIONSHIP_NAME = "ISARelationship";
    private static final String IS_A_RELATIONSHIP_GUID = "50fab7c7-68bc-452f-b8eb-ec76829cac85";

    private static final String VALID_VALUE_RELATIONSHIP_NAME = "ValidValue";
    private static final String VALID_VALUE_RELATIONSHIP_GUID = "707a156b-e579-4482-89a5-de5889da1971";

    private static final String USED_IN_CONTEXT_RELATIONSHIP_NAME = "UsedInContext";
    private static final String USED_IN_CONTEXT_RELATIONSHIP_GUID = "2dc524d2-e29f-4186-9081-72ea956c75de";

    private static final String SEMANTIC_ASSIGNMENT_RELATIONSHIP_NAME = "SemanticAssignment";
    private static final String SEMANTIC_ASSIGNMENT_RELATIONSHIP_GUID = "e6670973-645f-441a-bec7-6f5570345b92";

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
     * @param termGUID term GUID
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

    /**
     * Extract related terms
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse related terms
     */
    public GlossaryViewEntityDetailResponse getRelatedTerms(String userId, String serverName, String termGUID,
                                                                  Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME,
                RELATED_TERM_RELATIONSHIP_GUID, RELATED_TERM_RELATIONSHIP_NAME, from, size, "getRelatedTerms");
    }

    /**
     * Extract synonyms
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse synonyms
     */
    public GlossaryViewEntityDetailResponse getSynonyms(String userId, String serverName, String termGUID,
                                                            Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME,
                SYNONYM_RELATIONSHIP_GUID, SYNONYM_RELATIONSHIP_NAME, from, size, "getSynonyms");
    }

    /**
     * Extract antonyms
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse antonyms
     */
    public GlossaryViewEntityDetailResponse getAntonyms(String userId, String serverName, String termGUID,
                                                        Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME,
                ANTONYM_RELATIONSHIP_GUID, ANTONYM_RELATIONSHIP_NAME, from, size,"getAntonyms");
    }

    /**
     * Extract preferred terms
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse preferred terms
     */
    public GlossaryViewEntityDetailResponse getPreferredTerms(String userId, String serverName, String termGUID,
                                                        Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME,
                PREFERRED_TERM_RELATIONSHIP_GUID, PREFERRED_TERM_RELATIONSHIP_NAME, from, size,"getPreferredTerms");
    }

    /**
     * Extract replacement terms
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse replacement terms
     */
    public GlossaryViewEntityDetailResponse getReplacementTerms(String userId, String serverName, String termGUID,
                                                              Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME,
                REPLACEMENT_TERM_RELATIONSHIP_GUID, REPLACEMENT_TERM_RELATIONSHIP_NAME, from, size,
                "getReplacementTerms");
    }

    /**
     * Extract translations
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse translations
     */
    public GlossaryViewEntityDetailResponse getTranslations(String userId, String serverName, String termGUID,
                                                                Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME,
                TRANSLATION_RELATIONSHIP_GUID, TRANSLATION_RELATIONSHIP_NAME, from, size, "getTranslations");
    }

    /**
     * Extract "is a"
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse "is a"
     */
    public GlossaryViewEntityDetailResponse getIsA(String userId, String serverName, String termGUID,
                                                            Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME,
                IS_A_RELATIONSHIP_GUID, IS_A_RELATIONSHIP_NAME, from, size, "getIsA");
    }

    /**
     * Extract valid values
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse valid values
     */
    public GlossaryViewEntityDetailResponse getValidValues(String userId, String serverName, String termGUID,
                                                   Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME,
                VALID_VALUE_RELATIONSHIP_GUID, VALID_VALUE_RELATIONSHIP_NAME, from, size, "getValidValues");
    }

    /**
     * Extract "used in contexts"
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse "used in contexts"
     */
    public GlossaryViewEntityDetailResponse getUsedInContexts(String userId, String serverName, String termGUID,
                                                           Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME,
                USED_IN_CONTEXT_RELATIONSHIP_GUID, USED_IN_CONTEXT_RELATIONSHIP_NAME, from, size, "getUsedInContexts");
    }

    /**
     * Extract assigned elements
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse "used in contexts"
     */
    public GlossaryViewEntityDetailResponse getAssignedElements(String userId, String serverName, String termGUID,
                                                                Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME,
                SEMANTIC_ASSIGNMENT_RELATIONSHIP_GUID, SEMANTIC_ASSIGNMENT_RELATIONSHIP_NAME, from, size, "getAssignedElements");
    }

}

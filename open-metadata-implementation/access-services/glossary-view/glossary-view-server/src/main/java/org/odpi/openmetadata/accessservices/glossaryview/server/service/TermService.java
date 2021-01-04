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
    private static final String GLOSSARY_TYPE_NAME = "Glossary";
    private static final String CATEGORY_TYPE_NAME = "GlossaryCategory";

    private static final String TERM_CATEGORIZATION_RELATIONSHIP_NAME = "TermCategorization";
    private static final String LIBRARY_TERM_REFERENCE_RELATIONSHIP_NAME = "LibraryTermReference";
    private static final String TERM_ANCHOR_RELATIONSHIP_NAME = "TermAnchor";
    private static final String RELATED_TERM_RELATIONSHIP_NAME = "RelatedTerm";
    private static final String SYNONYM_RELATIONSHIP_NAME = "Synonym";
    private static final String ANTONYM_RELATIONSHIP_NAME = "Antonym";
    private static final String PREFERRED_TERM_RELATIONSHIP_NAME = "PreferredTerm";
    private static final String REPLACEMENT_TERM_RELATIONSHIP_NAME = "ReplacementTerm";
    private static final String TRANSLATION_RELATIONSHIP_NAME = "Translation";
    private static final String IS_A_RELATIONSHIP_NAME = "ISARelationship";
    private static final String VALID_VALUE_RELATIONSHIP_NAME = "ValidValue";
    private static final String USED_IN_CONTEXT_RELATIONSHIP_NAME = "UsedInContext";
    private static final String SEMANTIC_ASSIGNMENT_RELATIONSHIP_NAME = "SemanticAssignment";
    private static final String TERM_HAS_A_RELATIONSHIP_NAME = "TermHASARelationship";
    private static final String TERM_IS_A_TYPE_OF_RELATIONSHIP_NAME = "TermISATypeOFRelationship";
    private static final String TERM_TYPED_BY_RELATIONSHIP_NAME = "TermTYPEDBYRelationship";

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
     * Extract all terms definitions
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param from offset start for the return values
     * @param size maximum number of results
     *
     * @return EntityDetailResponse all terms
     */
    public GlossaryViewEntityDetailResponse getAllGlossaryTerms(String userId, String serverName, Integer from, Integer size){
        return getAllEntityDetailsResponse(userId, serverName, TERM_TYPE_NAME, from, size, "getAllGlossaries");
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
                                                                               String glossaryGUID, Integer from, Integer size) {
        return getRelatedEntitiesResponse(userId, serverName, glossaryGUID, GLOSSARY_TYPE_NAME,
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
                                                                                       Integer from, Integer size) {
        return getRelatedEntitiesResponse(userId, serverName, categoryGUID, CATEGORY_TYPE_NAME,
                TERM_CATEGORIZATION_RELATIONSHIP_NAME, from, size, "getTermsViaTermCategorizationRelationships");
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
    public GlossaryViewEntityDetailResponse getExternalGlossaryLinks(String userId, String serverName, String termGUID,
                                                                     Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME,
                LIBRARY_TERM_REFERENCE_RELATIONSHIP_NAME, from, size,"getExternalGlossaryLinks");
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
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME, RELATED_TERM_RELATIONSHIP_NAME,
                from, size, "getRelatedTerms");
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
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME, SYNONYM_RELATIONSHIP_NAME,
                from, size, "getSynonyms");
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
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME, ANTONYM_RELATIONSHIP_NAME,
                from, size,"getAntonyms");
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
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME, PREFERRED_TERM_RELATIONSHIP_NAME,
                from, size,"getPreferredTerms");
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
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME, REPLACEMENT_TERM_RELATIONSHIP_NAME,
                from, size,"getReplacementTerms");
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
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME, TRANSLATION_RELATIONSHIP_NAME,
                from, size, "getTranslations");
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
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME, IS_A_RELATIONSHIP_NAME,
                from, size, "getIsA");
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
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME, VALID_VALUE_RELATIONSHIP_NAME,
                from, size, "getValidValues");
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
    public GlossaryViewEntityDetailResponse getUsedInContexts(String userId, String serverName, String termGUID, Integer from,
                                                              Integer size){
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME, USED_IN_CONTEXT_RELATIONSHIP_NAME,
                from, size, "getUsedInContexts");
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
     * @return EntityDetailResponse semantic assignments
     */
    public GlossaryViewEntityDetailResponse getAssignedElements(String userId, String serverName, String termGUID,
                                                                Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME, SEMANTIC_ASSIGNMENT_RELATIONSHIP_NAME,
                from, size, "getAssignedElements");
    }

    /**
     * Extract attributes
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse semantic assignments
     */
    public GlossaryViewEntityDetailResponse getAttributes(String userId, String serverName, String termGUID, Integer from,
                                                          Integer size){
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME, TERM_HAS_A_RELATIONSHIP_NAME,
                from, size, "getAttributes");
    }

    /**
     * Extract subtypes
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse semantic assignments
     */
    public GlossaryViewEntityDetailResponse getSubtypes(String userId, String serverName, String termGUID, Integer from,
                                                        Integer size){
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME, TERM_IS_A_TYPE_OF_RELATIONSHIP_NAME,
                from, size, "getSubtypes");
    }

    /**
     * Extract types
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse semantic assignments
     */
    public GlossaryViewEntityDetailResponse getTypes(String userId, String serverName, String termGUID, Integer from,
                                                     Integer size){
        return getRelatedEntitiesResponse(userId, serverName, termGUID, TERM_TYPE_NAME, TERM_TYPED_BY_RELATIONSHIP_NAME,
                from, size, "getTypes");
    }

    /**
     * Extract all terms definitions
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param from offset start for the return values
     * @param size maximum number of results
     *
     * @return EntityDetailResponse all glossaries
     */
    public GlossaryViewEntityDetailResponse getAllTerms(String userId, String serverName, Integer from, Integer size) {
        return getAllEntityDetailsResponse(userId, serverName, TERM_TYPE_NAME, from, size, "getAllTerms");
    }
}

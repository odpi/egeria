/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;

/**
 * Server-side implementation of the Glossary View Open Metadata Access Service (OMAS).
 * Deals in extracting terms.
 */
public class TermService extends GlossaryViewOMAS {

    private static TermService instance;
    private static final String TERM_CATEGORIZATION = "TermCategorization";

    public static synchronized TermService getInstance(){
        if(instance == null){
            instance = new TermService();
        }
        return instance;
    }

    private TermService() {
        //
    }

    /**
     * Extract the term definition for the given GUID
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID term GUID
     *
     * @return EntityDetailResponse term
     */
    public GlossaryViewEntityDetailResponse getTerm(String userId, String serverName, String termGUID){
        return getEntityDetailResponse(userId, serverName, termGUID);
    }

    /**
     * Extract all term definitions for the given category
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param categoryGUID category GUID
     *
     * @return EntityDetailResponse terms
     */
    public GlossaryViewEntityDetailResponse getTerms(String userId, String serverName, String categoryGUID){
        return getRelatedEntities(userId, serverName, categoryGUID, TERM_CATEGORIZATION);
    }

}

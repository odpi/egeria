/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;

/**
 * Server-side implementation of the Glossary View Open Metadata Access Service (OMAS).
 * Deals in extracting glossaries
 */
public class GlossaryService extends GlossaryViewOMAS {

    private static GlossaryService instance;
    private static final String GLOSSARY = "Glossary";

    public static synchronized GlossaryService getInstance(){
        if(instance == null){
            instance = new GlossaryService();
        }
        return instance;
    }

    private GlossaryService() {
        //
    }

    /**
     * Extract the glossary definition for the given GUID
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param glossaryGUID glossary GUID
     *
     * @return EntityDetailResponse glossary
     */
    public GlossaryViewEntityDetailResponse getGlossary(String userId, String serverName, String glossaryGUID){
        return getEntityDetailResponse(userId, serverName, glossaryGUID);
    }

    /**
     * Extract all glossary definitions
     *
     * @param userId calling user
     * @param serverName instance to call
     *
     * @return EntityDetailResponse all glossaries
     */
    public GlossaryViewEntityDetailResponse getAllGlossaries(String userId, String serverName){
        return getAllEntityDetailsResponse(userId, serverName, GLOSSARY);
    }

}

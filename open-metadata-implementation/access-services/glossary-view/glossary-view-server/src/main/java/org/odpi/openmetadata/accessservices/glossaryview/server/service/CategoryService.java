/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;

/**
 * Server-side implementation of the Glossary View Open Metadata Access Service (OMAS).
 * Deals in extracting categories
 */
public class CategoryService extends GlossaryViewOMAS {

    private static CategoryService instance;
    private static final String CATEGORY_ANCHOR = "CategoryAnchor";

    public static synchronized CategoryService getInstance(){
        if(instance == null){
            instance = new CategoryService();
        }
        return instance;
    }

    private CategoryService() {
        //
    }

    /**
     * Extract the category definition for the given GUID
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param categoryGUID category GUID
     *
     * @return EntityDetailResponse category
     */
    public GlossaryViewEntityDetailResponse getCategory(String userId, String serverName, String categoryGUID){
        return getEntityDetailResponse(userId, serverName, categoryGUID);
    }

    /**
     * Extract all category definitions for the given glossary
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param glossaryGUID glossary GUID
     *
     * @return EntityDetailResponse categories
     */
    public GlossaryViewEntityDetailResponse getCategories(String userId, String serverName, String glossaryGUID){
        return getRelatedEntities(userId, serverName, glossaryGUID, CATEGORY_ANCHOR);
    }

}

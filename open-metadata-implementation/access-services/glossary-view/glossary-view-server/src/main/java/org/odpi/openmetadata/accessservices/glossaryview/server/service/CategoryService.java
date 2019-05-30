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
    private static final String CATEGORY_HIERARCHY_LINK ="CategoryHierarchyLink";
    private static final String LIBRARY_CATEGORY_REFERENCE = "LibraryCategoryReference";

    public static synchronized CategoryService getInstance(){
        if(instance == null){
            instance = new CategoryService();
        }
        return instance;
    }

    private CategoryService() {}

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

    /**
     * Extract subcategory definitions for the given category
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param categoryGUID category GUID
     *
     * @return EntityDetailResponse subcategories
     */
    public GlossaryViewEntityDetailResponse getSubcategories(String userId, String serverName, String categoryGUID){
        return getRelatedEntities(userId, serverName, categoryGUID, CATEGORY_HIERARCHY_LINK);
    }

    /**
     * Extract external glossary definitions for the given category
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param categoryGUID glossary GUID
     *
     * @return EntityDetailResponse all external glossaries
     */
    public GlossaryViewEntityDetailResponse getExternalGlossaries(String userId, String serverName, String categoryGUID){
        return getRelatedEntities(userId, serverName, categoryGUID, LIBRARY_CATEGORY_REFERENCE);
    }

}

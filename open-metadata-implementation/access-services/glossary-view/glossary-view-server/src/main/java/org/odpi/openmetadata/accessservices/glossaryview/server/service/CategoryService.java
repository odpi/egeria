/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;

/**
 * Server-side implementation of the Glossary View Open Metadata Access Service (OMAS).
 * Deals in extracting categories
 */
public class CategoryService extends GlossaryViewOMAS {

    private static final String CATEGORY_TYPE_NAME = "GlossaryCategory";
    private static final String GLOSSARY_TYPE_NAME = "Glossary";

    private static final String CATEGORY_ANCHOR_RELATIONSHIP_NAME = "CategoryAnchor";
    private static final String CATEGORY_HIERARCHY_LINK_RELATIONSHIP_NAME ="CategoryHierarchyLink";
    private static final String LIBRARY_CATEGORY_REFERENCE_RELATIONSHIP_NAME = "LibraryCategoryReference";

    /**
     * Extract the category definition for the given GUID
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param categoryGUID category GUID
     *
     * @return EntityDetailResponse category
     */
    public GlossaryViewEntityDetailResponse getCategory(String userId, String serverName, String categoryGUID){
        return getEntityDetailResponse(userId, serverName, categoryGUID, CATEGORY_TYPE_NAME, "getCategory");
    }

    /**
     * Extract category definitions for the given glossary GUID via the 'CategoryAnchor' type relationships
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param glossaryGUID glossary GUID
     * @param from offset start for the return values
     * @param size maximum number of results
     *
     * @return EntityDetailResponse all external glossaries
     */
    public GlossaryViewEntityDetailResponse getCategoriesViaCategoryAnchorRelationships(String userId, String serverName,
                                                                                        String glossaryGUID, Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, glossaryGUID, GLOSSARY_TYPE_NAME,
                CATEGORY_ANCHOR_RELATIONSHIP_NAME, from, size, "getCategoriesViaCategoryAnchorRelationships");
    }

    /**
     * Extract subcategory definitions for the given category
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param categoryGUID category GUID
     * @param from offset start for the return values
     * @param size maximum number of results
     *
     * @return EntityDetailResponse subcategories
     */
    public GlossaryViewEntityDetailResponse getSubcategories(String userId, String serverName, String categoryGUID,
                                                             Integer from, Integer size){
        return getSubEntitiesResponse(userId, serverName, categoryGUID, CATEGORY_TYPE_NAME,
                CATEGORY_HIERARCHY_LINK_RELATIONSHIP_NAME, from, size,"getSubcategories");
    }

    /**
     * Extract external glossary definitions for the given category
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param categoryGUID glossary GUID
     * @param from offset start for the return values
     * @param size maximum number of results
     *
     * @return EntityDetailResponse all external glossaries
     */
    public GlossaryViewEntityDetailResponse getExternalGlossaryLinks(String userId, String serverName, String categoryGUID,
                                                                     Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, categoryGUID, CATEGORY_TYPE_NAME,
                LIBRARY_CATEGORY_REFERENCE_RELATIONSHIP_NAME, from, size,"getExternalGlossaryLinks");
    }

    /**
     * Extract all categories definitions
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param from offset start for the return values
     * @param size maximum number of results
     *
     * @return EntityDetailResponse all glossaries
     */
    public GlossaryViewEntityDetailResponse getAllCategories(String userId, String serverName, Integer from, Integer size) {
        return getAllEntityDetailsResponse(userId, serverName, CATEGORY_TYPE_NAME, from, size, "getAllCategories");
    }
}

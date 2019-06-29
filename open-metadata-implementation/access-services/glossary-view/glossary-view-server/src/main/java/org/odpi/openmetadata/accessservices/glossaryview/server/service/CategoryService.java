/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;

/**
 * Server-side implementation of the Glossary View Open Metadata Access Service (OMAS).
 * Deals in extracting categories
 */
public class CategoryService extends GlossaryViewOMAS {

    private static final String GLOSSARY_TYPE_NAME = "Glossary";
    private static final String CATEGORY_TYPE_NAME = "GlossaryCategory";

    private static final String CATEGORY_ANCHOR_RELATIONSHIP_NAME = "CategoryAnchor";
    private static final String CATEGORY_ANCHOR_RELATIONSHIP_GUID = "c628938e-815e-47db-8d1c-59bb2e84e028";

    private static final String CATEGORY_HIERARCHY_LINK_RELATIONSHIP_NAME ="CategoryHierarchyLink";
    private static final String CATEGORY_HIERARCHY_LINK_RELATIONSHIP_GUID ="71e4b6fb-3412-4193-aff3-a16eccd87e8e";

    private static final String LIBRARY_CATEGORY_REFERENCE_RELATIONSHIP_NAME = "LibraryCategoryReference";
    private static final String LIBRARY_CATEGORY_REFERENCE_RELATIONSHIP_GUID = "3da21cc9-3cdc-4d87-89b5-c501740f00b2";

    public CategoryService() {}

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
     * Extract all category definitions for the given glossary
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param glossaryGUID glossary GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse categories
     */
    public GlossaryViewEntityDetailResponse getCategories(String userId, String serverName, String glossaryGUID,
                                                          Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, glossaryGUID, GLOSSARY_TYPE_NAME,
                CATEGORY_ANCHOR_RELATIONSHIP_GUID, CATEGORY_ANCHOR_RELATIONSHIP_NAME, from, size, "getCategories");
    }

    /**
     * Extract category definitions for the given glossary GUID via the 'CategoryAnchor' type relationships
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param glossaryGUID glossary GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse all external glossaries
     */
    public GlossaryViewEntityDetailResponse getCategoriesViaCategoryAnchorRelationships(String userId, String serverName,
                                                                                        String glossaryGUID, Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, glossaryGUID, CATEGORY_TYPE_NAME, CATEGORY_ANCHOR_RELATIONSHIP_GUID,
                CATEGORY_ANCHOR_RELATIONSHIP_NAME, from, size, "getCategoriesViaCategoryAnchorRelationships");
    }

    /**
     * Extract subcategory definitions for the given category
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param categoryGUID category GUID
     * @param from from
     * @param size size
     *
     * @return EntityDetailResponse subcategories
     */
    public GlossaryViewEntityDetailResponse getSubcategories(String userId, String serverName, String categoryGUID,
                                                             Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, categoryGUID, CATEGORY_TYPE_NAME,
                CATEGORY_HIERARCHY_LINK_RELATIONSHIP_GUID, CATEGORY_HIERARCHY_LINK_RELATIONSHIP_NAME, from, size,
                "getSubcategories");
    }

    /**
     * Extract external glossary definitions for the given category
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param categoryGUID glossary GUID
     * @param from
     * @param size
     *
     * @return EntityDetailResponse all external glossaries
     */
    public GlossaryViewEntityDetailResponse getExternalGlossaries(String userId, String serverName, String categoryGUID,
                                                                  Integer from, Integer size){
        return getRelatedEntitiesResponse(userId, serverName, categoryGUID, CATEGORY_TYPE_NAME,
                LIBRARY_CATEGORY_REFERENCE_RELATIONSHIP_GUID, LIBRARY_CATEGORY_REFERENCE_RELATIONSHIP_NAME, from, size,
                "getExternalGlossaries");
    }

}

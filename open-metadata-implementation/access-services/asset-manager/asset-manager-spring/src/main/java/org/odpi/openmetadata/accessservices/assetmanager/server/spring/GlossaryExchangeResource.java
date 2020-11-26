/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.accessservices.assetmanager.server.GlossaryExchangeRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;

import org.springframework.web.bind.annotation.*;


/**
 * GlossaryExchangeResource is the server-side implementation of the Asset Manager OMAS's
 * support for glossaries.  It matches the GlossaryExchangeClient.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-manager/users/{userId}")

@Tag(name="Asset Manager OMAS",
     description="The Asset Manager OMAS provides APIs and events for managing metadata exchange with third party asset managers, such as data catalogs.",
        externalDocs=@ExternalDocumentation(description="Asset Manager Open Metadata Access Service (OMAS)",
                url="https://egeria.odpi.org/open-metadata-implementation/access-services/asset-manager/"))

public class GlossaryExchangeResource
{
    private GlossaryExchangeRESTServices restAPI = new GlossaryExchangeRESTServices();

    /**
     * Default constructor
     */
    public GlossaryExchangeResource()
    {
    }


    /* ========================================================
     * The glossary is the root object for a glossary.  All of the glossary's categories and terms are anchored
     * to it so that if the glossary is deleted, all of the categories and terms within it are also deleted.
     */


    /**
     * Create a new metadata element to represent the root of a glossary.  All categories and terms are linked
     * to a single glossary.  They are owned by this glossary and if the glossary is deleted, any linked terms and
     * categories are deleted as well.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries")

    public GUIDResponse createGlossary(@PathVariable String              serverName,
                                       @PathVariable String              userId,
                                       @RequestBody  GlossaryRequestBody requestBody)
    {
        return restAPI.createGlossary(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a glossary using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new glossary.
     *
     * All categories and terms are linked to a single glossary.  They are owned by this glossary and if the
     * glossary is deleted, any linked terms and categories are deleted as well.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/from-template/{templateGUID}")

    public GUIDResponse createGlossaryFromTemplate(@PathVariable String              serverName,
                                                   @PathVariable String              userId,
                                                   @PathVariable String              templateGUID,
                                                   @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createGlossaryFromTemplate(serverName, userId, templateGUID, requestBody);
    }


    /**
     * Update the metadata element representing a glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to update
     * @param requestBody new properties for this element
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}")

    public VoidResponse updateGlossary(@PathVariable String              serverName,
                                       @PathVariable String              userId,
                                       @PathVariable String              glossaryGUID,
                                       @RequestBody  GlossaryRequestBody requestBody)
    {
        return restAPI.updateGlossary(serverName, userId, glossaryGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a glossary.  This will delete the glossary and all categories
     * and terms.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/remove")

    public VoidResponse removeGlossary(@PathVariable String                        serverName,
                                       @PathVariable String                        userId,
                                       @PathVariable String                        glossaryGUID,
                                       @RequestBody(required = false)
                                                     MetadataCorrelationProperties requestBody)
    {
        return restAPI.removeGlossary(serverName, userId, glossaryGUID, requestBody);
    }


    /**
     * Classify the glossary to indicate that it can be used as a taxonomy.
     * This means each term is attached to one, and only one category and the categories are organized as a hierarchy
     * with a single root category.
     *
     * Taxonomies are used as a way of organizing assets and other related metadata.  The terms in the taxonomy
     * are linked to the assets etc and as such they are logically categorized by the linked category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/is-taxonomy")

    public VoidResponse setGlossaryAsTaxonomy(@PathVariable String                            serverName,
                                              @PathVariable String                            userId,
                                              @PathVariable String                            glossaryGUID,
                                              @RequestBody  TaxonomyClassificationRequestBody requestBody)
    {
        return restAPI.setGlossaryAsTaxonomy(serverName, userId, glossaryGUID, requestBody);
    }


    /**
     * Remove the taxonomy designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/is-taxonomy/remove")

    public VoidResponse clearGlossaryAsTaxonomy(@PathVariable String                        serverName,
                                                @PathVariable String                        userId,
                                                @PathVariable String                        glossaryGUID,
                                                @RequestBody(required = false)
                                                              MetadataCorrelationProperties requestBody)
    {
        return restAPI.clearGlossaryAsTaxonomy(serverName, userId, glossaryGUID, requestBody);
    }


    /**
     * Classify a glossary to declare that it has no two GlossaryTerm definitions with
     * the same name.  This means there is only one definition for each term.  Typically the terms are also of a similar
     * level of granularity and are limited to a specific scope of use.
     *
     * Canonical vocabularies are used to semantically classify assets in an unambiguous way.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param requestBody description of the situations where this glossary is relevant.
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/is-canonical")

    public VoidResponse setGlossaryAsCanonical(@PathVariable String                                       serverName,
                                               @PathVariable String                                       userId,
                                               @PathVariable String                                       glossaryGUID,
                                               @RequestBody  CanonicalVocabularyClassificationRequestBody requestBody)
    {
        return restAPI.setGlossaryAsCanonical(serverName, userId, glossaryGUID, requestBody);
    }


    /**
     * Remove the canonical designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/is-canonical/remove")

    public VoidResponse clearGlossaryAsCanonical(@PathVariable String                        serverName,
                                                 @PathVariable String                        userId,
                                                 @PathVariable String                        glossaryGUID,
                                                 @RequestBody(required = false)
                                                               MetadataCorrelationProperties requestBody)
    {
        return restAPI.clearGlossaryAsCanonical(serverName, userId, glossaryGUID, requestBody);
    }


    /**
     * Retrieve the list of glossary metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/by-search-string")

    public GlossaryElementsResponse findGlossaries(@PathVariable String                  serverName,
                                                   @PathVariable String                  userId,
                                                   @RequestParam int                     startFrom,
                                                   @RequestParam int                     pageSize,
                                                   @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findGlossaries(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of glossary metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/by-name")

    public GlossaryElementsResponse   getGlossariesByName(@PathVariable String          serverName,
                                                          @PathVariable String          userId,
                                                          @RequestParam int             startFrom,
                                                          @RequestParam int             pageSize,
                                                          @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getGlossariesByName(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of glossaries created by this caller.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody asset manager identifiers
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/by-asset-manager")

    public GlossaryElementsResponse   getGlossariesForAssetManager(@PathVariable String                             serverName,
                                                                   @PathVariable String                             userId,
                                                                   @RequestParam int                                startFrom,
                                                                   @RequestParam int                                pageSize,
                                                                   @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getGlossariesForAssetManager(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the glossary metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the requested metadata element
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/retrieve")

    public GlossaryElementResponse getGlossaryByGUID(@PathVariable String                             serverName,
                                                     @PathVariable String                             userId,
                                                     @PathVariable String                             glossaryGUID,
                                                     @RequestBody(required = false)
                                                                   AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getGlossaryByGUID(serverName, userId, glossaryGUID, requestBody);
    }



    /* =====================================================================================================================
     * A glossary may host one or more glossary categories depending on its capability
     */

    /**
     * Create a new metadata element to represent a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary where the category is located
     * @param requestBody properties about the glossary category to store
     *
     * @return unique identifier of the new glossary category or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/categories")

    public GUIDResponse createGlossaryCategory(@PathVariable String                      serverName,
                                               @PathVariable String                      userId,
                                               @PathVariable String                      glossaryGUID,
                                               @RequestBody  GlossaryCategoryRequestBody requestBody)
    {
        return restAPI.createGlossaryCategory(serverName, userId, glossaryGUID, requestBody);
    }


    /**
     * Create a new metadata element to represent a glossary category using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new glossary category or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/from-template/{templateGUID}")

    public GUIDResponse createGlossaryCategoryFromTemplate(@PathVariable String               serverName,
                                                           @PathVariable String               userId,
                                                           @PathVariable String               templateGUID,
                                                           @RequestBody  TemplateRequestBody  requestBody)
    {
        return restAPI.createGlossaryCategoryFromTemplate(serverName, userId, templateGUID, requestBody);
    }


    /**
     * Update the metadata element representing a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the metadata element to update
     * @param requestBody new properties for the metadata element
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryCategoryGUID}")

    public VoidResponse updateGlossaryCategory(@PathVariable String                      serverName,
                                               @PathVariable String                      userId,
                                               @PathVariable String                      glossaryCategoryGUID,
                                               @RequestBody  GlossaryCategoryRequestBody requestBody)
    {
        return restAPI.updateGlossaryCategory(serverName, userId, glossaryCategoryGUID, requestBody);
    }


    /**
     * Create a parent-child relationship between two categories.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryParentCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the subcategory
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryParentCategoryGUID}/subcategories/{glossaryChildCategoryGUID}")

    public VoidResponse setupCategoryParent(@PathVariable String                             serverName,
                                            @PathVariable String                             userId,
                                            @PathVariable String                             glossaryParentCategoryGUID,
                                            @PathVariable String                             glossaryChildCategoryGUID,
                                            @RequestBody(required = false)
                                                          AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.setupCategoryParent(serverName, userId, glossaryParentCategoryGUID, glossaryChildCategoryGUID, requestBody);
    }


    /**
     * Remove a parent-child relationship between two categories.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryParentCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the subcategory
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryParentCategoryGUID}/subcategories/{glossaryChildCategoryGUID}/remove")

    public VoidResponse clearCategoryParent(@PathVariable String                            serverName,
                                            @PathVariable String                            userId,
                                            @PathVariable String                            glossaryParentCategoryGUID,
                                            @PathVariable String                            glossaryChildCategoryGUID,
                                            @RequestBody(required = false)
                                                         AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.clearCategoryParent(serverName, userId, glossaryParentCategoryGUID, glossaryChildCategoryGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the metadata element to remove
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryCategoryGUID}/remove")

    public VoidResponse removeGlossaryCategory(@PathVariable String                        serverName,
                                               @PathVariable String                        userId,
                                               @PathVariable String                        glossaryCategoryGUID,
                                               @RequestBody(required = false)
                                                             MetadataCorrelationProperties requestBody)
    {
        return restAPI.removeGlossaryCategory(serverName, userId, glossaryCategoryGUID, requestBody);
    }


    /**
     * Retrieve the list of glossary category metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties and correlators
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/by-search-string")

    public GlossaryCategoryElementsResponse findGlossaryCategories(@PathVariable String                  serverName,
                                                                   @PathVariable String                  userId,
                                                                   @RequestParam int                     startFrom,
                                                                   @RequestParam int                     pageSize,
                                                                   @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findGlossaryCategories(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Return the list of categories associated with a glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody asset manager identifiers
     *
     * @return list of metadata elements describing the categories associated with the requested glossary or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/categories/retrieve")

    public GlossaryCategoryElementsResponse getCategoriesForGlossary(@PathVariable String                             serverName,
                                                                     @PathVariable String                             userId,
                                                                     @PathVariable String                             glossaryGUID,
                                                                     @RequestParam int                                startFrom,
                                                                     @RequestParam int                                pageSize,
                                                                     @RequestBody(required = false)
                                                                                   AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getCategoriesForGlossary(serverName, userId, glossaryGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of glossary category metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param requestBody name to search for and correlators
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/by-name")

    public GlossaryCategoryElementsResponse   getGlossaryCategoriesByName(@PathVariable String          serverName,
                                                                          @PathVariable String          userId,
                                                                          @RequestParam int             startFrom,
                                                                          @RequestParam int             pageSize,
                                                                          @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getGlossaryCategoriesByName(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param requestBody asset manager identifiers
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryCategoryGUID}/retrieve")

    public GlossaryCategoryElementResponse getGlossaryCategoryByGUID(@PathVariable String                             serverName,
                                                                     @PathVariable String                             userId,
                                                                     @PathVariable String                             glossaryCategoryGUID,
                                                                     @RequestBody(required = false)
                                                                                   AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getGlossaryCategoryByGUID(serverName, userId, glossaryCategoryGUID, requestBody);
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param requestBody asset manager identifiers
     *
     * @return parent glossary category element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryCategoryGUID}/parent/retrieve")

    public GlossaryCategoryElementResponse getGlossaryCategoryParent(@PathVariable String                             serverName,
                                                                     @PathVariable String                             userId,
                                                                     @PathVariable String                             glossaryCategoryGUID,
                                                                     @RequestBody(required = false)
                                                                                   AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getGlossaryCategoryParent(serverName, userId, glossaryCategoryGUID, requestBody);
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody asset manager identifiers
     *
     * @return list of glossary category elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryCategoryGUID}/subcategories/retrieve")

    public GlossaryCategoryElementsResponse getGlossarySubCategories(@PathVariable String                             serverName,
                                                                     @PathVariable String                             userId,
                                                                     @PathVariable String                             glossaryCategoryGUID,
                                                                     @RequestParam int                                startFrom,
                                                                     @RequestParam int                                pageSize,
                                                                     @RequestBody(required = false)
                                                                                   AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getGlossarySubCategories(serverName, userId, glossaryCategoryGUID, startFrom, pageSize, requestBody);
    }




    /* ===============================================================================
     * A glossary typically contains many glossary terms, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param requestBody properties for the glossary term
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/terms")

    public GUIDResponse createGlossaryTerm(@PathVariable String                  serverName,
                                           @PathVariable String                  userId,
                                           @PathVariable String                  glossaryGUID,
                                           @RequestBody  GlossaryTermRequestBody requestBody)
    {
        return restAPI.createGlossaryTerm(serverName, userId, glossaryGUID, requestBody);
    }


    /**
     * Create a new metadata element to represent a glossary term whose lifecycle is managed through a controlled workflow.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param requestBody properties for the glossary term
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/terms/new-controlled")

    public GUIDResponse createControlledGlossaryTerm(@PathVariable String                            serverName,
                                                     @PathVariable String                            userId,
                                                     @PathVariable String                            glossaryGUID,
                                                     @RequestBody  ControlledGlossaryTermRequestBody requestBody)
    {
        return restAPI.createControlledGlossaryTerm(serverName, userId, glossaryGUID, requestBody);
    }


    /**
     * Create a new metadata element to represent a glossary term using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/from-template/{templateGUID}")

    public GUIDResponse createGlossaryTermFromTemplate(@PathVariable String               serverName,
                                                       @PathVariable String               userId,
                                                       @PathVariable String               templateGUID,
                                                       @RequestBody  TemplateRequestBody  requestBody)
    {
        return restAPI.createGlossaryTermFromTemplate(serverName, userId, templateGUID, requestBody);
    }


    /**
     * Update the metadata element representing a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param requestBody new properties for the glossary term
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}")

    public VoidResponse updateGlossaryTerm(@PathVariable String                  serverName,
                                           @PathVariable String                  userId,
                                           @PathVariable String                  glossaryTermGUID,
                                           @RequestBody  GlossaryTermRequestBody requestBody)
    {
        return restAPI.updateGlossaryTerm(serverName, userId, glossaryTermGUID, requestBody);
    }


    /**
     * Update the status of the metadata element representing a glossary term.  This is only valid on
     * a controlled glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param requestBody status and correlation properties
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/status")

    public VoidResponse updateGlossaryTermStatus(@PathVariable String                        serverName,
                                                 @PathVariable String                        userId,
                                                 @PathVariable String                        glossaryTermGUID,
                                                 @RequestBody  GlossaryTermStatusRequestBody requestBody)
    {
        return restAPI.updateGlossaryTermStatus(serverName, userId, glossaryTermGUID, requestBody);
    }


    /**
     * Link a term to a category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the glossary category
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param requestBody properties for the categorization relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryCategoryGUID}/terms/{glossaryTermGUID}")

    public VoidResponse setupTermCategory(@PathVariable String                    serverName,
                                          @PathVariable String                    userId,
                                          @PathVariable String                    glossaryCategoryGUID,
                                          @PathVariable String                    glossaryTermGUID,
                                          @RequestBody  CategorizationRequestBody requestBody)
    {
        return restAPI.setupTermCategory(serverName, userId, glossaryCategoryGUID, glossaryTermGUID, requestBody);
    }


    /**
     * Unlink a term from a category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the glossary category
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param requestBody asset manager identifiers
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryCategoryGUID}/terms/{glossaryTermGUID}/remove")

    public VoidResponse clearTermCategory(@PathVariable String                             serverName,
                                          @PathVariable String                             userId,
                                          @PathVariable String                             glossaryCategoryGUID,
                                          @PathVariable String                             glossaryTermGUID,
                                          @RequestBody(required = false)
                                                        AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.clearTermCategory(serverName, userId, glossaryCategoryGUID, glossaryTermGUID, requestBody);
    }


    /**
     * Link two terms together using a specialist relationship.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermOneGUID}/relationships/{relationshipTypeName}/terms/{glossaryTermTwoGUID}")

    public VoidResponse setupTermRelationship(@PathVariable String                      serverName,
                                              @PathVariable String                      userId,
                                              @PathVariable String                      glossaryTermOneGUID,
                                              @PathVariable String                      relationshipTypeName,
                                              @PathVariable String                      glossaryTermTwoGUID,
                                              @RequestBody  TermRelationshipRequestBody requestBody)
    {
        return restAPI.setupTermRelationship(serverName, userId, glossaryTermOneGUID, relationshipTypeName, glossaryTermTwoGUID, requestBody);
    }


    /**
     * Update the relationship properties for the two terms.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermOneGUID}/relationships/{relationshipTypeName}/terms/{glossaryTermTwoGUID}/update")

    public VoidResponse updateTermRelationship(@PathVariable String                      serverName,
                                               @PathVariable String                      userId,
                                               @PathVariable String                      glossaryTermOneGUID,
                                               @PathVariable String                      relationshipTypeName,
                                               @PathVariable String                      glossaryTermTwoGUID,
                                               @RequestBody  TermRelationshipRequestBody requestBody)
    {
        return restAPI.updateTermRelationship(serverName, userId, glossaryTermOneGUID, relationshipTypeName, glossaryTermTwoGUID, requestBody);
    }


    /**
     * Remove the relationship between two terms.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param requestBody properties of the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermOneGUID}/relationships/{relationshipTypeName}/terms/{glossaryTermTwoGUID}/remove")

    public VoidResponse clearTermRelationship(@PathVariable String                             serverName,
                                              @PathVariable String                             userId,
                                              @PathVariable String                             glossaryTermOneGUID,
                                              @PathVariable String                             relationshipTypeName,
                                              @PathVariable String                             glossaryTermTwoGUID,
                                              @RequestBody(required = false)
                                                            AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.clearTermRelationship(serverName, userId, glossaryTermOneGUID, relationshipTypeName, glossaryTermTwoGUID, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes an abstract concept.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-abstract-concept")

    public VoidResponse setTermAsAbstractConcept(@PathVariable String                        serverName,
                                                 @PathVariable String                        userId,
                                                 @PathVariable String                        glossaryTermGUID,
                                                 @RequestBody(required = false)
                                                               MetadataCorrelationProperties requestBody)
    {
        return restAPI.setTermAsAbstractConcept(serverName, userId, glossaryTermGUID, requestBody);
    }


    /**
     * Remove the abstract concept designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-abstract-concept/remove")

    public VoidResponse clearTermAsAbstractConcept(@PathVariable String                        serverName,
                                                   @PathVariable String                        userId,
                                                   @PathVariable String                        glossaryTermGUID,
                                                   @RequestBody(required = false)
                                                                 MetadataCorrelationProperties requestBody)
    {
        return restAPI.clearTermAsAbstractConcept(serverName, userId, glossaryTermGUID, requestBody);
    }

    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-data-value")

    public VoidResponse setTermAsDataValue(@PathVariable String                        serverName,
                                           @PathVariable String                        userId,
                                           @PathVariable String                        glossaryTermGUID,
                                           @RequestBody(required = false)
                                                         MetadataCorrelationProperties requestBody)
    {
        return restAPI.setTermAsDataValue(serverName, userId, glossaryTermGUID, requestBody);
    }


    /**
     * Remove the data value designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-data-value/remove")

    public VoidResponse clearTermAsDataValue(@PathVariable String                        serverName,
                                             @PathVariable String                        userId,
                                             @PathVariable String                        glossaryTermGUID,
                                             @RequestBody(required = false)
                                                           MetadataCorrelationProperties requestBody)
    {
        return restAPI.clearTermAsDataValue(serverName, userId, glossaryTermGUID, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody type of activity and correlators
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-activity")

    public VoidResponse setTermAsActivity(@PathVariable String                                serverName,
                                          @PathVariable String                                userId,
                                          @PathVariable String                                glossaryTermGUID,
                                          @RequestBody  ActivityTermClassificationRequestBody requestBody)
    {
        return restAPI.setTermAsActivity(serverName, userId, glossaryTermGUID, requestBody);
    }


    /**
     * Remove the activity designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-activity/remove")

    public VoidResponse clearTermAsActivity(@PathVariable String                        serverName,
                                            @PathVariable String                        userId,
                                            @PathVariable String                        glossaryTermGUID,
                                            @RequestBody(required = false)
                                                          MetadataCorrelationProperties requestBody)
    {
        return restAPI.clearTermAsActivity(serverName, userId, glossaryTermGUID, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a context.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody more details of the context
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-context-definition")

    public VoidResponse setTermAsContext(@PathVariable String                                     serverName,
                                         @PathVariable String                                     userId,
                                         @PathVariable String                                     glossaryTermGUID,
                                         @RequestBody  ContextDefinitionClassificationRequestBody requestBody)
    {
        return restAPI.setTermAsContext(serverName, userId, glossaryTermGUID, requestBody);
    }


    /**
     * Remove the context definition designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-context-definition/remove")

    public VoidResponse clearTermAsContext(@PathVariable String                        serverName,
                                           @PathVariable String                        userId,
                                           @PathVariable String                        glossaryTermGUID,
                                           @RequestBody(required = false)
                                                         MetadataCorrelationProperties requestBody)
    {
        return restAPI.clearTermAsContext(serverName, userId, glossaryTermGUID, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a spine object.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-spine-object")

    public VoidResponse setTermAsSpineObject(@PathVariable String                        serverName,
                                             @PathVariable String                        userId,
                                             @PathVariable String                        glossaryTermGUID,
                                             @RequestBody(required = false)
                                                           MetadataCorrelationProperties requestBody)
    {
        return restAPI.setTermAsSpineObject(serverName, userId, glossaryTermGUID, requestBody);
    }


    /**
     * Remove the spine object designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-spine-object/remove")

    public VoidResponse clearTermAsSpineObject(@PathVariable String                        serverName,
                                               @PathVariable String                        userId,
                                               @PathVariable String                        glossaryTermGUID,
                                               @RequestBody(required = false)
                                                             MetadataCorrelationProperties requestBody)
    {
        return restAPI.clearTermAsSpineObject(serverName, userId, glossaryTermGUID, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a spine attribute.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-spine-attribute")

    public VoidResponse setTermAsSpineAttribute(@PathVariable String                        serverName,
                                                @PathVariable String                        userId,
                                                @PathVariable String                        glossaryTermGUID,
                                                @RequestBody(required = false)
                                                              MetadataCorrelationProperties requestBody)
    {
        return restAPI.setTermAsSpineAttribute(serverName, userId, glossaryTermGUID, requestBody);
    }


    /**
     * Remove the spine attribute designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-spine-attribute/remove")

    public VoidResponse clearTermAsSpineAttribute(@PathVariable String                        serverName,
                                                  @PathVariable String                        userId,
                                                  @PathVariable String                        glossaryTermGUID,
                                                  @RequestBody(required = false)
                                                                MetadataCorrelationProperties requestBody)
    {
        return restAPI.clearTermAsSpineAttribute(serverName, userId, glossaryTermGUID, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes an object identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-object-identifier")

    public VoidResponse setTermAsObjectIdentifier(@PathVariable String                        serverName,
                                                  @PathVariable String                        userId,
                                                  @PathVariable String                        glossaryTermGUID,
                                                  @RequestBody(required = false)
                                                                MetadataCorrelationProperties requestBody)
    {
        return restAPI.setTermAsObjectIdentifier(serverName, userId, glossaryTermGUID, requestBody);
    }


    /**
     * Remove the object identifier designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-object-identifier/remove")

    public VoidResponse clearTermAsObjectIdentifier(@PathVariable String                        serverName,
                                                    @PathVariable String                        userId,
                                                    @PathVariable String                        glossaryTermGUID,
                                                    @RequestBody(required = false)
                                                                  MetadataCorrelationProperties requestBody)
    {
        return restAPI.clearTermAsObjectIdentifier(serverName, userId, glossaryTermGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to remove
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/remove")

    public VoidResponse removeGlossaryTerm(@PathVariable String                        serverName,
                                           @PathVariable String                        userId,
                                           @PathVariable String                        glossaryTermGUID,
                                           @RequestBody(required = false)
                                                         MetadataCorrelationProperties requestBody)
    {
        return restAPI.removeGlossaryTerm(serverName, userId, glossaryTermGUID, requestBody);
    }


    /**
     * Retrieve the list of glossary term metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody asset manager identifiers and search string
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/by-search-string")

    public GlossaryTermElementsResponse findGlossaryTerms(@PathVariable String                  serverName,
                                                          @PathVariable String                  userId,
                                                          @RequestParam int                     startFrom,
                                                          @RequestParam int                     pageSize,
                                                          @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findGlossaryTerms(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of glossary terms associated with a glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody asset manager identifiers
     *
     * @return list of associated metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/terms/retrieve")

    public GlossaryTermElementsResponse getTermsForGlossary(@PathVariable String                             serverName,
                                                            @PathVariable String                             userId,
                                                            @PathVariable String                             glossaryGUID,
                                                            @RequestParam int                                startFrom,
                                                            @RequestParam int                                pageSize,
                                                            @RequestBody(required = false)
                                                                          AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getTermsForGlossary(serverName, userId, glossaryGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of glossary terms associated with a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the glossary category of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody asset manager identifiers
     *
     * @return list of associated metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryCategoryGUID}/terms/retrieve")

    public GlossaryTermElementsResponse getTermsForGlossaryCategory(@PathVariable String                             serverName,
                                                                    @PathVariable String                             userId,
                                                                    @PathVariable String                             glossaryCategoryGUID,
                                                                    @RequestParam int                                startFrom,
                                                                    @RequestParam int                                pageSize,
                                                                    @RequestBody(required = false)
                                                                                  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getTermsForGlossaryCategory(serverName, userId, glossaryCategoryGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of glossary term metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody asset manager identifiers and name
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/by-name")

    public GlossaryTermElementsResponse   getGlossaryTermsByName(@PathVariable String          serverName,
                                                                 @PathVariable String          userId,
                                                                 @RequestParam int             startFrom,
                                                                 @RequestParam int             pageSize,
                                                                 @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getGlossaryTermsByName(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the glossary term metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the requested metadata element
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/retrieve")

    public GlossaryTermElementResponse getGlossaryTermByGUID(@PathVariable String                             serverName,
                                                             @PathVariable String                             userId,
                                                             @PathVariable String                             glossaryTermGUID,
                                                             @RequestBody(required = false)
                                                                           AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getGlossaryTermByGUID(serverName, userId, glossaryTermGUID, requestBody);
    }



    /* =========================================================================================
     * Support for linkage to external glossary resources.  These glossary resources are not
     * stored as metadata - they could be web pages, ontologies or some other format.
     * It is possible that the external glossary resource may have been generated by the metadata
     * representation or vice versa.
     */


    /**
     * Create a link to an external glossary resource.  This is associated with a glossary to show that they have equivalent content.
     * It is possible that this resource was generated from the glossary content or was the source for it.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param requestBody properties of the link
     *
     * @return unique identifier of the external reference or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/external-links")

    public GUIDResponse createExternalGlossaryLink(@PathVariable String                          serverName,
                                                   @PathVariable String                          userId,
                                                   @RequestBody  ExternalGlossaryLinkRequestBody requestBody)
    {
        return restAPI.createExternalGlossaryLink(serverName, userId, requestBody);
    }


    /**
     * Update the properties of a reference to an external glossary resource.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the external reference
     * @param requestBody properties of the link
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/external-links/{externalLinkGUID}")

    public VoidResponse updateExternalGlossaryLink(@PathVariable String                          serverName,
                                                   @PathVariable String                          userId,
                                                   @PathVariable String                          externalLinkGUID,
                                                   @RequestBody  ExternalGlossaryLinkRequestBody requestBody)
    {
        return restAPI.updateExternalGlossaryLink(serverName, userId, externalLinkGUID, requestBody);
    }


    /**
     * Remove information about a link to an external glossary resource (and the relationships that attached it to the glossaries).
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the external reference
     * @param requestBody asset manager identifiers
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/external-links/{externalLinkGUID}/remove")

    public VoidResponse removeExternalGlossaryLink(@PathVariable String                             serverName,
                                                   @PathVariable String                             userId,
                                                   @PathVariable String                             externalLinkGUID,
                                                   @RequestBody(required = false)
                                                                 AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.removeExternalGlossaryLink(serverName, userId, externalLinkGUID, requestBody);
    }


    /**
     * Connect a glossary to a reference to an external glossary resource.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryGUID unique identifier of the metadata element to attach
     * @param requestBody asset manager identifiers
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/external-links/{externalLinkGUID}")

    public VoidResponse attachExternalLinkToGlossary(@PathVariable String                             serverName,
                                                     @PathVariable String                             userId,
                                                     @PathVariable String                             glossaryGUID,
                                                     @PathVariable String                             externalLinkGUID,
                                                     @RequestBody(required = false)
                                                                   AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.attachExternalLinkToGlossary(serverName, userId, glossaryGUID, externalLinkGUID, requestBody);
    }


    /**
     * Disconnect a glossary from a reference to an external glossary resource.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param requestBody asset manager identifiers
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/external-links/{externalLinkGUID}/remove")

    public VoidResponse detachExternalLinkFromGlossary(@PathVariable String                             serverName,
                                                       @PathVariable String                             userId,
                                                       @PathVariable String                             externalLinkGUID,
                                                       @PathVariable String                             glossaryGUID,
                                                       @RequestBody(required = false)
                                                                     AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.detachExternalLinkFromGlossary(serverName, userId, externalLinkGUID, glossaryGUID, requestBody);
    }


    /**
     * Retrieve the list of links to external glossary resources attached to a glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element for the glossary of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of attached links to external glossary resources or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/external-links/retrieve")

    public ExternalGlossaryLinkElementsResponse getExternalLinksForGlossary(@PathVariable String serverName,
                                                                            @PathVariable String userId,
                                                                            @PathVariable String glossaryGUID,
                                                                            @RequestParam int    startFrom,
                                                                            @RequestParam int    pageSize)
    {
        return restAPI.getExternalLinksForGlossary(serverName, userId, glossaryGUID, startFrom, pageSize);
    }


    /**
     * Return the glossaries connected to an external glossary source.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the metadata element for the external glossary link of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody asset manager identifiers
     *
     * @return list of glossaries or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/external-links/{externalLinkGUID}/retrieve")

    public GlossaryElementsResponse getGlossariesForExternalLink(@PathVariable String serverName,
                                                                 @PathVariable String userId,
                                                                 @PathVariable String externalLinkGUID,
                                                                 @RequestParam int    startFrom,
                                                                 @RequestParam int    pageSize,
                                                                 @RequestBody(required = false)
                                                                               AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getGlossariesForExternalLink(serverName, userId, externalLinkGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Create a link to an external glossary category resource.  This is associated with a category to show that they have equivalent content.
     * It is possible that this resource was generated from the glossary content or was the source for it.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryCategoryGUID unique identifier for the the glossary category
     * @param requestBody properties of the link
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryCategoryGUID}/external-links/{externalLinkGUID}")

    public VoidResponse attachExternalCategoryLink(@PathVariable String                                 serverName,
                                                   @PathVariable String                                 userId,
                                                   @PathVariable String                                 glossaryCategoryGUID,
                                                   @PathVariable String                                 externalLinkGUID,
                                                   @RequestBody  ExternalGlossaryElementLinkRequestBody requestBody)
    {
        return restAPI.attachExternalCategoryLink(serverName, userId, glossaryCategoryGUID, externalLinkGUID, requestBody);
    }


    /**
     * Remove the link to an external glossary category resource.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryCategoryGUID unique identifier for the the glossary category
     * @param requestBody asset manager identifiers
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryCategoryGUID}/external-links/{externalLinkGUID}/remove")

    public VoidResponse detachExternalCategoryLink(@PathVariable String                             serverName,
                                                   @PathVariable String                             userId,
                                                   @PathVariable String                             externalLinkGUID,
                                                   @PathVariable String                             glossaryCategoryGUID,
                                                   @RequestBody(required = false)
                                                                 AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.detachExternalCategoryLink(serverName, userId, externalLinkGUID, glossaryCategoryGUID, requestBody);
    }


    /**
     * Create a link to an external glossary term resource.  This is associated with a term to show that they have equivalent content.
     * It is possible that this resource was generated from the glossary content or was the source for it.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryTermGUID unique identifier for the the glossary category
     * @param requestBody properties of the link
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/external-links/{externalLinkGUID}")

    public VoidResponse attachExternalTermLink(@PathVariable String                                 serverName,
                                               @PathVariable String                                 userId,
                                               @PathVariable String                                 externalLinkGUID,
                                               @PathVariable String                                 glossaryTermGUID,
                                               @RequestBody  ExternalGlossaryElementLinkRequestBody requestBody)
    {
        return restAPI.attachExternalTermLink(serverName, userId, externalLinkGUID, glossaryTermGUID, requestBody);
    }


    /**
     * Remove the link to an external glossary term resource.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryTermGUID unique identifier for the the glossary category
     * @param requestBody asset manager identifiers
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/external-links/{externalLinkGUID}/remove")

    public VoidResponse detachExternalTermLink(@PathVariable String                             serverName,
                                               @PathVariable String                             userId,
                                               @PathVariable String                             externalLinkGUID,
                                               @PathVariable String                             glossaryTermGUID,
                                               @RequestBody(required = false)
                                                             AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.detachExternalTermLink(serverName, userId, externalLinkGUID, glossaryTermGUID, requestBody);
    }
}

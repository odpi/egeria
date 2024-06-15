/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.glossarymanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermElementResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.ArchiveRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.ClassificationRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.ControlledGlossaryTermRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTemplateRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTermActivityTypeListResponse;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTermRelationshipStatusListResponse;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTermStatusListResponse;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTermStatusRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.ReferenceableUpdateRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.RelationshipRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.TemplateRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.server.GlossaryManagerRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The GlossaryManagerResource provides the Spring API endpoints of the Glossary Manager Open Metadata View Service (OMVS).
 * This interface provides a service for Egeria UIs.
 */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/glossary-manager")

@Tag(name="API: Glossary Manager OMVS",
     description="The Glossary Manager OMVS enables the caller to create glossary terms and organize them into categories as part of a controlled workflow process.  It supports the editing glossary and multiple states.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omvs/glossary-manager/overview/"))

public class GlossaryManagerResource
{

    private final GlossaryManagerRESTServices restAPI = new GlossaryManagerRESTServices();


    /**
     * Default constructor
     */
    public GlossaryManagerResource()
    {
    }


    /* ========================================================
     * The glossary is the root object for a glossary.  All the glossary's categories and terms are anchored
     * to it so that if the glossary is deleted, all the categories and terms within it are also deleted.
     */


    /**
     * Create a new metadata element to represent the root of a glossary.  All categories and terms are linked
     * to a single glossary.  They are owned by this glossary and if the glossary is deleted, any linked terms and
     * categories are deleted as well.
     *
     * @param serverName name of the server to route the request to.
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries")

    public GUIDResponse createGlossary(@PathVariable String                   serverName,
                                       @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.createGlossary(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent a glossary using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new glossary.
     * All categories and terms are linked to a single glossary.  They are owned by this glossary and if the
     * glossary is deleted, any linked terms and categories are deleted as well.
     *
     * @param serverName name of the server to route the request to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/from-template/{templateGUID}")

    public GUIDResponse createGlossaryFromTemplate(@PathVariable String              serverName,
                                                   @PathVariable String              templateGUID,
                                                   @RequestParam (required = false, defaultValue = "true")
                                                                 boolean             deepCopy,
                                                   @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createGlossaryFromTemplate(serverName, templateGUID, deepCopy, requestBody);
    }


    /**
     * Update the metadata element representing a glossary.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody new properties for this element
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/update")

    public VoidResponse updateGlossary(@PathVariable String                         serverName,
                                       @PathVariable String                         glossaryGUID,
                                       @RequestParam (required = false, defaultValue = "false")
                                                     boolean                        isMergeUpdate,
                                       @RequestParam (required = false, defaultValue = "false")
                                                     boolean                        forLineage,
                                       @RequestParam (required = false, defaultValue = "false")
                                                     boolean                        forDuplicateProcessing,
                                       @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.updateGlossary(serverName, glossaryGUID, isMergeUpdate, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the metadata element representing a glossary.  This will delete the glossary and all categories
     * and terms.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/remove")

    public VoidResponse removeGlossary(@PathVariable String                         serverName,
                                       @PathVariable String                         glossaryGUID,
                                       @RequestParam (required = false, defaultValue = "false")
                                                     boolean                        forLineage,
                                       @RequestParam (required = false, defaultValue = "false")
                                                     boolean                        forDuplicateProcessing,
                                       @RequestBody(required = false)
                                                     ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.removeGlossary(serverName, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary to indicate that it is an editing glossary - this means it is
     * a collection of glossary updates that will be merged into its source glossary.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/is-editing-glossary")

    public VoidResponse setGlossaryAsEditingGlossary(@PathVariable String                    serverName,
                                                     @PathVariable String                    glossaryGUID,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                   forLineage,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                   forDuplicateProcessing,
                                                     @RequestBody  ClassificationRequestBody requestBody)
    {
        return restAPI.setGlossaryAsEditingGlossary(serverName, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the editing glossary designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/is-editing-glossary/remove")

    public VoidResponse clearGlossaryAsEditingGlossary(@PathVariable String                    serverName,
                                                       @PathVariable String                    glossaryGUID,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                                     boolean                   forLineage,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                                     boolean                   forDuplicateProcessing,
                                                       @RequestBody(required = false)
                                                                     ClassificationRequestBody requestBody)
    {
        return restAPI.clearGlossaryAsEditingGlossary(serverName, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary to indicate that it is a staging glossary - this means it is
     * a collection of glossary updates that will be transferred into another glossary.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/is-staging-glossary")

    public VoidResponse setGlossaryAsStagingGlossary(@PathVariable String                    serverName,
                                                     @PathVariable String                    glossaryGUID,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean                   forLineage,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean                   forDuplicateProcessing,
                                                     @RequestBody  ClassificationRequestBody requestBody)
    {
        return restAPI.setGlossaryAsStagingGlossary(serverName, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the staging glossary designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/is-staging-glossary/remove")

    public VoidResponse clearGlossaryAsStagingGlossary(@PathVariable String                    serverName,
                                                       @PathVariable String                    glossaryGUID,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                                     boolean                   forLineage,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                                     boolean                   forDuplicateProcessing,
                                                       @RequestBody(required = false)
                                                                     ClassificationRequestBody requestBody)
    {
        return restAPI.clearGlossaryAsStagingGlossary(serverName, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary to indicate that it can be used as a taxonomy.
     * This means each term is attached to one, and only one category and the categories are organized as a hierarchy
     * with a single root category.
     * Taxonomies are used as a way of organizing assets and other related metadata.  The terms in the taxonomy
     * are linked to the assets etc. and as such they are logically categorized by the linked category.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/is-taxonomy")

    public VoidResponse setGlossaryAsTaxonomy(@PathVariable String                    serverName,
                                              @PathVariable String                    glossaryGUID,
                                              @RequestParam (required = false, defaultValue = "false")
                                                            boolean                   forLineage,
                                              @RequestParam (required = false, defaultValue = "false")
                                                            boolean                   forDuplicateProcessing,
                                              @RequestBody  ClassificationRequestBody requestBody)
    {
        return restAPI.setGlossaryAsTaxonomy(serverName, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the taxonomy designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/is-taxonomy/remove")

    public VoidResponse clearGlossaryAsTaxonomy(@PathVariable String                    serverName,
                                                @PathVariable String                    glossaryGUID,
                                                @RequestParam (required = false, defaultValue = "false")
                                                              boolean                   forLineage,
                                                @RequestParam (required = false, defaultValue = "false")
                                                              boolean                   forDuplicateProcessing,
                                                @RequestBody(required = false)
                                                              ClassificationRequestBody requestBody)
    {
        return restAPI.clearGlossaryAsTaxonomy(serverName, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify a glossary to declare that it has no two GlossaryTerm definitions with
     * the same name.  This means there is only one definition for each term.  Typically, the terms are also of a similar
     * level of granularity and are limited to a specific scope of use.
     * Canonical vocabularies are used to semantically classify assets in an unambiguous way.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody description of the situations where this glossary is relevant.
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/is-canonical-vocabulary")

    public VoidResponse setGlossaryAsCanonical(@PathVariable String                    serverName,
                                               @PathVariable String                    glossaryGUID,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                   forLineage,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                   forDuplicateProcessing,
                                               @RequestBody  ClassificationRequestBody requestBody)
    {
        return restAPI.setGlossaryAsCanonical(serverName, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the canonical vocabulary designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/is-canonical-vocabulary/remove")

    public VoidResponse clearGlossaryAsCanonical(@PathVariable String                    serverName,
                                                 @PathVariable String                    glossaryGUID,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                               boolean                   forLineage,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                               boolean                   forDuplicateProcessing,
                                                 @RequestBody(required = false)
                                                               ClassificationRequestBody requestBody)
    {
        return restAPI.clearGlossaryAsCanonical(serverName, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /* =====================================================================================================================
     * A glossary may host one or more glossary categories depending on its capability
     */

    /**
     * Create a new metadata element to represent a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the glossary where the category is located
     * @param isRootCategory is this category a root category?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties about the glossary category to store
     *
     * @return unique identifier of the new glossary category or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/categories")

    public GUIDResponse createGlossaryCategory(@PathVariable String                         serverName,
                                               @PathVariable String                         glossaryGUID,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                        isRootCategory,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                        forLineage,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                        forDuplicateProcessing,
                                               @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.createGlossaryCategory(serverName, glossaryGUID, isRootCategory, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Create a new metadata element to represent a glossary category using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the glossary where the category is located
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new glossary category or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/categories/from-template/{templateGUID}")

    public GUIDResponse createGlossaryCategoryFromTemplate(@PathVariable String               serverName,
                                                           @PathVariable String               glossaryGUID,
                                                           @PathVariable String               templateGUID,
                                                           @RequestBody  TemplateRequestBody  requestBody)
    {
        return restAPI.createGlossaryCategoryFromTemplate(serverName, glossaryGUID, templateGUID, requestBody);
    }


    /**
     * Update the metadata element representing a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryCategoryGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody new properties for the metadata element
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryCategoryGUID}/update")

    public VoidResponse updateGlossaryCategory(@PathVariable String                         serverName,
                                               @PathVariable String                         glossaryCategoryGUID,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                        isMergeUpdate,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                        forLineage,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                        forDuplicateProcessing,
                                               @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.updateGlossaryCategory(serverName, glossaryCategoryGUID, isMergeUpdate, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Create a parent-child relationship between two categories.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryParentCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the subcategory
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryParentCategoryGUID}/subcategories/{glossaryChildCategoryGUID}")

    public VoidResponse setupCategoryParent(@PathVariable String                       serverName,
                                            @PathVariable String                       glossaryParentCategoryGUID,
                                            @PathVariable String                       glossaryChildCategoryGUID,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean                      forLineage,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean                      forDuplicateProcessing,
                                            @RequestBody(required = false)
                                                          RelationshipRequestBody requestBody)
    {
        return restAPI.setupCategoryParent(serverName, glossaryParentCategoryGUID, glossaryChildCategoryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove a parent-child relationship between two categories.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryParentCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the subcategory
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryParentCategoryGUID}/subcategories/{glossaryChildCategoryGUID}/remove")

    public VoidResponse clearCategoryParent(@PathVariable String                        serverName,
                                            @PathVariable String                        glossaryParentCategoryGUID,
                                            @PathVariable String                        glossaryChildCategoryGUID,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean                       forLineage,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean                       forDuplicateProcessing,
                                            @RequestBody(required = false)
                                                          EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.clearCategoryParent(serverName, glossaryParentCategoryGUID, glossaryChildCategoryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the metadata element representing a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryCategoryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryCategoryGUID}/remove")

    public VoidResponse removeGlossaryCategory(@PathVariable String                        serverName,
                                               @PathVariable String                        glossaryCategoryGUID,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                      forLineage,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                      forDuplicateProcessing,
                                               @RequestBody(required = false)
                                                             ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.removeGlossaryCategory(serverName, glossaryCategoryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /* ===============================================================================
     * A glossary typically contains many glossary terms, linked with relationships.
     */

    /**
     * Return the list of glossary term status enum values.
     *
     * @param serverName name of the server to route the request to
     * @return list of enum values
     */
    @GetMapping(path = "/glossaries/terms/status-list")

    public GlossaryTermStatusListResponse getGlossaryTermStatuses(@PathVariable String serverName)
    {
        return restAPI.getGlossaryTermStatuses(serverName);
    }


    /**
     * Return the list of glossary term relationship status enum values.
     *
     * @param serverName name of the server to route the request to
     * @return list of enum values
     */
    @GetMapping(path = "/glossaries/terms/relationships/status-list")

    public GlossaryTermRelationshipStatusListResponse getGlossaryTermRelationshipStatuses(@PathVariable String serverName)
    {
        return restAPI.getGlossaryTermRelationshipStatuses(serverName);
    }


    /**
     * Return the list of glossary term relationship status enum values.
     *
     * @param serverName name of the server to route the request to
     * @return list of enum values
     */
    @GetMapping(path = "/glossaries/terms/activity-types")

    public GlossaryTermActivityTypeListResponse getGlossaryTermActivityTypes(@PathVariable String serverName)
    {
        return restAPI.getGlossaryTermActivityTypes(serverName);
    }


    /**
     * Create a new metadata element to represent a glossary term whose lifecycle is managed through a controlled workflow.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the glossary where the new term is to be located
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the glossary term
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/terms/new-controlled")

    public GUIDResponse createControlledGlossaryTerm(@PathVariable String                            serverName,
                                                     @PathVariable String                            glossaryGUID,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                           forLineage,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                           forDuplicateProcessing,
                                                     @RequestBody  ControlledGlossaryTermRequestBody requestBody)
    {
        return restAPI.createControlledGlossaryTerm(serverName, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Create a new metadata element to represent a glossary term using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param templateGUID unique identifier of the metadata element to copy
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     * @param templateSubstitute is this element a template substitute (used as the "other end" of a new/updated relationship)
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/terms/from-template/{templateGUID}")

    public GUIDResponse createGlossaryTermFromTemplate(@PathVariable String                      serverName,
                                                       @PathVariable String                      glossaryGUID,
                                                       @PathVariable String                      templateGUID,
                                                       @RequestParam (required = false, defaultValue = "true")
                                                                     boolean                     deepCopy,
                                                       @RequestParam (required = false, defaultValue = "true")
                                                                     boolean                     templateSubstitute,
                                                       @RequestBody  GlossaryTemplateRequestBody requestBody)
    {
        return restAPI.createGlossaryTermFromTemplate(serverName, glossaryGUID, templateGUID, deepCopy, templateSubstitute, requestBody);
    }


    /**
     * Update the metadata element representing a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody new properties for the glossary term
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/update")

    public VoidResponse updateGlossaryTerm(@PathVariable String                         serverName,
                                           @PathVariable String                         glossaryTermGUID,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                        isMergeUpdate,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                        forLineage,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                        forDuplicateProcessing,
                                           @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.updateGlossaryTerm(serverName, glossaryTermGUID, isMergeUpdate, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Update the status of the metadata element representing a glossary term.  This is only valid on
     * a controlled glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody status and correlation properties
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/status")

    public VoidResponse updateGlossaryTermStatus(@PathVariable String                        serverName,
                                                 @PathVariable String                        glossaryTermGUID,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                               boolean                      forLineage,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                               boolean                      forDuplicateProcessing,
                                                 @RequestBody  GlossaryTermStatusRequestBody requestBody)
    {
        return restAPI.updateGlossaryTermStatus(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Update the glossary term using the properties and classifications from the parentGUID stored in the request body.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param isMergeClassifications should the classification be merged or replace the target entity?
     * @param isMergeProperties should the properties be merged with the existing ones or replace them
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody status and correlation properties
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/update/from-template")

    public VoidResponse updateGlossaryTermFromTemplate(@PathVariable String                         serverName,
                                                       @PathVariable String                         glossaryTermGUID,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                                     boolean                        isMergeClassifications,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                                     boolean                        isMergeProperties,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                                     boolean                        forLineage,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                                     boolean                        forDuplicateProcessing,
                                                       @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.updateGlossaryTermFromTemplate(serverName, glossaryTermGUID, isMergeClassifications, isMergeProperties, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Move the glossary term from one glossary to another.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody status and correlation properties
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/move")

    public VoidResponse moveGlossaryTerm(@PathVariable String                         serverName,
                                         @PathVariable String                         glossaryTermGUID,
                                         @RequestParam (required = false, defaultValue = "false")
                                         boolean                        forLineage,
                                         @RequestParam (required = false, defaultValue = "false")
                                         boolean                        forDuplicateProcessing,
                                         @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.moveGlossaryTerm(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Link a term to a category.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryCategoryGUID unique identifier of the glossary category
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the categorization relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryCategoryGUID}/terms/{glossaryTermGUID}")

    public VoidResponse setupTermCategory(@PathVariable String                  serverName,
                                          @PathVariable String                  glossaryCategoryGUID,
                                          @PathVariable String                  glossaryTermGUID,
                                          @RequestParam (required = false, defaultValue = "false")
                                                        boolean                 forLineage,
                                          @RequestParam (required = false, defaultValue = "false")
                                                        boolean                 forDuplicateProcessing,
                                          @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupTermCategory(serverName, glossaryCategoryGUID, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Unlink a term from a category.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryCategoryGUID unique identifier of the glossary category
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/categories/{glossaryCategoryGUID}/terms/{glossaryTermGUID}/remove")

    public VoidResponse clearTermCategory(@PathVariable String                        serverName,
                                          @PathVariable String                        glossaryCategoryGUID,
                                          @PathVariable String                        glossaryTermGUID,
                                          @RequestParam (required = false, defaultValue = "false")
                                                        boolean                       forLineage,
                                          @RequestParam (required = false, defaultValue = "false")
                                                        boolean                       forDuplicateProcessing,
                                          @RequestBody(required = false)
                                                        EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.clearTermCategory(serverName, glossaryCategoryGUID, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }

    /**
     * Return the list of term-to-term relationship names.
     *
     * @param serverName name of the server instance to connect to
     * @return list of type names that are subtypes of asset or
     * throws InvalidParameterException full path or userId is null or
     * throws PropertyServerException problem accessing property server or
     * throws UserNotAuthorizedException security access problem.
     */
    @GetMapping(path = "/glossaries/terms/relationships/type-names")

    public NameListResponse getTermRelationshipTypeNames(@PathVariable String serverName)
    {
        return restAPI.getTermRelationshipTypeNames(serverName);
    }


    /**
     * Link two terms together using a specialist relationship.
     *
     * @param serverName name of the server to route the request to
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermOneGUID}/relationships/{relationshipTypeName}/terms/{glossaryTermTwoGUID}")

    public VoidResponse setupTermRelationship(@PathVariable String                  serverName,
                                              @PathVariable String                  glossaryTermOneGUID,
                                              @PathVariable String                  relationshipTypeName,
                                              @PathVariable String                  glossaryTermTwoGUID,
                                              @RequestParam (required = false, defaultValue = "false")
                                                            boolean                 forLineage,
                                              @RequestParam (required = false, defaultValue = "false")
                                                            boolean                  forDuplicateProcessing,
                                              @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupTermRelationship(serverName, glossaryTermOneGUID, relationshipTypeName, glossaryTermTwoGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Update the relationship properties for the two terms.
     *
     * @param serverName name of the server to route the request to
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermOneGUID}/relationships/{relationshipTypeName}/terms/{glossaryTermTwoGUID}/update")

    public VoidResponse updateTermRelationship(@PathVariable String                  serverName,
                                               @PathVariable String                  glossaryTermOneGUID,
                                               @PathVariable String                  relationshipTypeName,
                                               @PathVariable String                  glossaryTermTwoGUID,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                 forLineage,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                 forDuplicateProcessing,
                                               @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.updateTermRelationship(serverName, glossaryTermOneGUID, relationshipTypeName, glossaryTermTwoGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the relationship between two terms.
     *
     * @param serverName name of the server to route the request to
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties of the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermOneGUID}/relationships/{relationshipTypeName}/terms/{glossaryTermTwoGUID}/remove")

    public VoidResponse clearTermRelationship(@PathVariable String                        serverName,
                                              @PathVariable String                        glossaryTermOneGUID,
                                              @PathVariable String                        relationshipTypeName,
                                              @PathVariable String                        glossaryTermTwoGUID,
                                              @RequestParam (required = false, defaultValue = "false")
                                                            boolean                       forLineage,
                                              @RequestParam (required = false, defaultValue = "false")
                                                            boolean                       forDuplicateProcessing,
                                              @RequestBody(required = false)
                                                            EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.clearTermRelationship(serverName, glossaryTermOneGUID, relationshipTypeName, glossaryTermTwoGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes an abstract concept.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-abstract-concept")

    public VoidResponse setTermAsAbstractConcept(@PathVariable String                       serverName,
                                                 @PathVariable String                       glossaryTermGUID,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                               boolean                      forLineage,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                               boolean                      forDuplicateProcessing,
                                                 @RequestBody(required = false)
                                                               ClassificationRequestBody    requestBody)
    {
        return restAPI.setTermAsAbstractConcept(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the abstract concept designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-abstract-concept/remove")

    public VoidResponse clearTermAsAbstractConcept(@PathVariable String                    serverName,
                                                   @PathVariable String                    glossaryTermGUID,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                                 boolean                   forLineage,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                                 boolean                   forDuplicateProcessing,
                                                   @RequestBody(required = false)
                                                                 ClassificationRequestBody requestBody)
    {
        return restAPI.clearTermAsAbstractConcept(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a data field and supply
     * properties that describe the characteristics of the data values found within.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-data-field")

    public VoidResponse setTermAsDataField(@PathVariable String                       serverName,
                                           @PathVariable String                       glossaryTermGUID,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                      forLineage,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                      forDuplicateProcessing,
                                           @RequestBody(required = false)
                                                         ClassificationRequestBody    requestBody)
    {
        return restAPI.setTermAsDataField(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the data field designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-data-field/remove")

    public VoidResponse clearTermAsDataField(@PathVariable String                    serverName,
                                             @PathVariable String                    glossaryTermGUID,
                                             @RequestParam (required = false, defaultValue = "false")
                                                           boolean                   forLineage,
                                             @RequestParam (required = false, defaultValue = "false")
                                                           boolean                   forDuplicateProcessing,
                                             @RequestBody(required = false)
                                                           ClassificationRequestBody requestBody)
    {
        return restAPI.clearTermAsDataField(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-data-value")

    public VoidResponse setTermAsDataValue(@PathVariable String                       serverName,
                                           @PathVariable String                       glossaryTermGUID,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                      forLineage,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                      forDuplicateProcessing,
                                           @RequestBody(required = false)
                                                         ClassificationRequestBody    requestBody)
    {
        return restAPI.setTermAsDataValue(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the data value designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-data-value/remove")

    public VoidResponse clearTermAsDataValue(@PathVariable String                    serverName,
                                             @PathVariable String                    glossaryTermGUID,
                                             @RequestParam (required = false, defaultValue = "false")
                                                           boolean                   forLineage,
                                             @RequestParam (required = false, defaultValue = "false")
                                                           boolean                   forDuplicateProcessing,
                                             @RequestBody(required = false)
                                                           ClassificationRequestBody requestBody)
    {
        return restAPI.clearTermAsDataValue(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody type of activity and correlators
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-activity")

    public VoidResponse setTermAsActivity(@PathVariable String                    serverName,
                                          @PathVariable String                    glossaryTermGUID,
                                          @RequestParam (required = false, defaultValue = "false")
                                                        boolean                   forLineage,
                                          @RequestParam (required = false, defaultValue = "false")
                                                        boolean                   forDuplicateProcessing,
                                          @RequestBody  ClassificationRequestBody requestBody)
    {
        return restAPI.setTermAsActivity(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the activity designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-activity/remove")

    public VoidResponse clearTermAsActivity(@PathVariable String                    serverName,
                                            @PathVariable String                    glossaryTermGUID,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean                   forLineage,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean                   forDuplicateProcessing,
                                            @RequestBody(required = false)
                                                          ClassificationRequestBody requestBody)
    {
        return restAPI.clearTermAsActivity(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a context.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody more details of the context
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-context-definition")

    public VoidResponse setTermAsContext(@PathVariable String                    serverName,
                                         @PathVariable String                    glossaryTermGUID,
                                         @RequestParam (required = false, defaultValue = "false")
                                                       boolean                   forLineage,
                                         @RequestParam (required = false, defaultValue = "false")
                                                       boolean                   forDuplicateProcessing,
                                         @RequestBody  ClassificationRequestBody requestBody)
    {
        return restAPI.setTermAsContext(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the context definition designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-context-definition/remove")

    public VoidResponse clearTermAsContext(@PathVariable String                   serverName,
                                           @PathVariable String                   glossaryTermGUID,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                  forLineage,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                  forDuplicateProcessing,
                                           @RequestBody(required = false)
                                                         ClassificationRequestBody requestBody)
    {
        return restAPI.clearTermAsContext(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a spine object.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-spine-object")

    public VoidResponse setTermAsSpineObject(@PathVariable String                   serverName,
                                             @PathVariable String                   glossaryTermGUID,
                                             @RequestParam (required = false, defaultValue = "false")
                                                           boolean                  forLineage,
                                             @RequestParam (required = false, defaultValue = "false")
                                                           boolean                  forDuplicateProcessing,
                                             @RequestBody(required = false)
                                                           ClassificationRequestBody requestBody)
    {
        return restAPI.setTermAsSpineObject(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the spine object designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-spine-object/remove")

    public VoidResponse clearTermAsSpineObject(@PathVariable String                        serverName,
                                               @PathVariable String                        glossaryTermGUID,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                       forLineage,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                       forDuplicateProcessing,
                                               @RequestBody(required = false)
                                                             ClassificationRequestBody     requestBody)
    {
        return restAPI.clearTermAsSpineObject(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a spine attribute.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-spine-attribute")

    public VoidResponse setTermAsSpineAttribute(@PathVariable String                        serverName,
                                                @PathVariable String                        glossaryTermGUID,
                                                @RequestParam (required = false, defaultValue = "false")
                                                              boolean                      forLineage,
                                                @RequestParam (required = false, defaultValue = "false")
                                                              boolean                      forDuplicateProcessing,
                                                @RequestBody(required = false)
                                                              ClassificationRequestBody    requestBody)
    {
        return restAPI.setTermAsSpineAttribute(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the spine attribute designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-spine-attribute/remove")

    public VoidResponse clearTermAsSpineAttribute(@PathVariable String                        serverName,
                                                  @PathVariable String                        glossaryTermGUID,
                                                  @RequestParam (required = false, defaultValue = "false")
                                                                boolean                       forLineage,
                                                  @RequestParam (required = false, defaultValue = "false")
                                                                boolean                       forDuplicateProcessing,
                                                  @RequestBody(required = false)
                                                                ClassificationRequestBody     requestBody)
    {
        return restAPI.clearTermAsSpineAttribute(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes an object identifier.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-object-identifier")

    public VoidResponse setTermAsObjectIdentifier(@PathVariable String                   serverName,
                                                  @PathVariable String                   glossaryTermGUID,
                                                  @RequestParam (required = false, defaultValue = "false")
                                                               boolean                  forLineage,
                                                  @RequestParam (required = false, defaultValue = "false")
                                                               boolean                  forDuplicateProcessing,
                                                  @RequestBody(required = false)
                                                               ClassificationRequestBody requestBody)
    {
        return restAPI.setTermAsObjectIdentifier(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the object identifier designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-object-identifier/remove")

    public VoidResponse clearTermAsObjectIdentifier(@PathVariable String                        serverName,
                                                    @PathVariable String                        glossaryTermGUID,
                                                    @RequestParam (required = false, defaultValue = "false")
                                                                  boolean                      forLineage,
                                                    @RequestParam (required = false, defaultValue = "false")
                                                                  boolean                      forDuplicateProcessing,
                                                    @RequestBody(required = false)
                                                                  ClassificationRequestBody            requestBody)
    {
        return restAPI.clearTermAsObjectIdentifier(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Undo the last update to the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/undo")

    public GlossaryTermElementResponse undoGlossaryTermUpdate(@PathVariable String                        serverName,
                                                              @PathVariable String                        glossaryTermGUID,
                                                              @RequestParam (required = false, defaultValue = "false")
                                                                            boolean                       forLineage,
                                                              @RequestParam (required = false, defaultValue = "false")
                                                                            boolean                       forDuplicateProcessing,
                                                              @RequestBody(required = false)
                                                                            EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.undoGlossaryTermUpdate(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Archive the metadata element representing a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to archive
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/archive")

    public VoidResponse archiveGlossaryTerm(@PathVariable String             serverName,
                                            @PathVariable String             glossaryTermGUID,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean            forDuplicateProcessing,
                                            @RequestBody(required = false)
                                                          ArchiveRequestBody requestBody)
    {
        return restAPI.archiveGlossaryTerm(serverName, glossaryTermGUID, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the metadata element representing a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/remove")

    public VoidResponse removeGlossaryTerm(@PathVariable String                         serverName,
                                           @PathVariable String                         glossaryTermGUID,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                        forLineage,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                        forDuplicateProcessing,
                                           @RequestBody(required = false)
                                                         ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.removeGlossaryTerm(serverName, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }
}

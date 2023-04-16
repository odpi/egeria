/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.glossaryworkflow.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ArchiveRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ClassificationRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ControlledGlossaryTermRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTemplateRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermActivityTypeListResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermRelationshipStatusListResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermStatusListResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermStatusRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ReferenceableUpdateRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.RelationshipRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.TemplateRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.UpdateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.viewservices.glossaryworkflow.server.GlossaryWorkflowRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The GlossaryWorkflowResource provides the Spring API endpoints of the Glossary Workflow Open Metadata View Service (OMVS).
 * This interface provides an interfaces for Egeria UIs.
 */

@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/glossary-workflow/users/{userId}")

@Tag(name="Glossary Workflow OMVS", description=".", externalDocs=@ExternalDocumentation(description="Glossary Workflow Open Metadata View Service (OMVS)",url="https://egeria-project.org/services/omvs/glossary-workflow/overview/"))

public class GlossaryWorkflowResource
{

    private final GlossaryWorkflowRESTServices restAPI = new GlossaryWorkflowRESTServices();


    /**
     * Default constructor
     */
    public GlossaryWorkflowResource()
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
     * @param userId calling user
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries")

    public GUIDResponse createGlossary(@PathVariable String                   serverName,
                                       @PathVariable String                   userId,
                                       @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.createGlossary(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a glossary using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new glossary.
     * All categories and terms are linked to a single glossary.  They are owned by this glossary and if the
     * glossary is deleted, any linked terms and categories are deleted as well.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                                   @PathVariable String              userId,
                                                   @PathVariable String              templateGUID,
                                                   @RequestParam (required = false, defaultValue = "true")
                                                                 boolean             deepCopy,
                                                   @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createGlossaryFromTemplate(serverName, userId, templateGUID, deepCopy, requestBody);
    }


    /**
     * Update the metadata element representing a glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                       @PathVariable String                         userId,
                                       @PathVariable String                         glossaryGUID,
                                       @RequestParam (required = false, defaultValue = "false")
                                                     boolean                        isMergeUpdate,
                                       @RequestParam (required = false, defaultValue = "false")
                                                     boolean                        forLineage,
                                       @RequestParam (required = false, defaultValue = "false")
                                                     boolean                        forDuplicateProcessing,
                                       @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.updateGlossary(serverName, userId, glossaryGUID, isMergeUpdate, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the metadata element representing a glossary.  This will delete the glossary and all categories
     * and terms.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                       @PathVariable String                         userId,
                                       @PathVariable String                         glossaryGUID,
                                       @RequestParam (required = false, defaultValue = "false")
                                                     boolean                        forLineage,
                                       @RequestParam (required = false, defaultValue = "false")
                                                     boolean                        forDuplicateProcessing,
                                       @RequestBody(required = false)
                                                     ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.removeGlossary(serverName, userId, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary to indicate that it is an editing glossary - this means it is
     * a temporary collection of glossary updates that will be merged into another glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                                     @PathVariable String                    userId,
                                                     @PathVariable String                    glossaryGUID,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                   forLineage,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                   forDuplicateProcessing,
                                                     @RequestBody  ClassificationRequestBody requestBody)
    {
        return restAPI.setGlossaryAsEditingGlossary(serverName, userId, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the editing glossary designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                                       @PathVariable String                    userId,
                                                       @PathVariable String                    glossaryGUID,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                                     boolean                   forLineage,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                                     boolean                   forDuplicateProcessing,
                                                       @RequestBody(required = false)
                                                                     ClassificationRequestBody requestBody)
    {
        return restAPI.clearGlossaryAsEditingGlossary(serverName, userId, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary to indicate that it can be used as a taxonomy.
     * This means each term is attached to one, and only one category and the categories are organized as a hierarchy
     * with a single root category.
     * Taxonomies are used as a way of organizing assets and other related metadata.  The terms in the taxonomy
     * are linked to the assets etc. and as such they are logically categorized by the linked category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                              @PathVariable String                    userId,
                                              @PathVariable String                    glossaryGUID,
                                              @RequestParam (required = false, defaultValue = "false")
                                                            boolean                   forLineage,
                                              @RequestParam (required = false, defaultValue = "false")
                                                            boolean                   forDuplicateProcessing,
                                              @RequestBody  ClassificationRequestBody requestBody)
    {
        return restAPI.setGlossaryAsTaxonomy(serverName, userId, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the taxonomy designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                                @PathVariable String                    userId,
                                                @PathVariable String                    glossaryGUID,
                                                @RequestParam (required = false, defaultValue = "false")
                                                              boolean                   forLineage,
                                                @RequestParam (required = false, defaultValue = "false")
                                                              boolean                   forDuplicateProcessing,
                                                @RequestBody(required = false)
                                                              ClassificationRequestBody requestBody)
    {
        return restAPI.clearGlossaryAsTaxonomy(serverName, userId, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify a glossary to declare that it has no two GlossaryTerm definitions with
     * the same name.  This means there is only one definition for each term.  Typically, the terms are also of a similar
     * level of granularity and are limited to a specific scope of use.
     * Canonical vocabularies are used to semantically classify assets in an unambiguous way.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
    @PostMapping(path = "/glossaries/{glossaryGUID}/is-canonical")

    public VoidResponse setGlossaryAsCanonical(@PathVariable String                    serverName,
                                               @PathVariable String                    userId,
                                               @PathVariable String                    glossaryGUID,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                   forLineage,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                   forDuplicateProcessing,
                                               @RequestBody  ClassificationRequestBody requestBody)
    {
        return restAPI.setGlossaryAsCanonical(serverName, userId, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the canonical designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
    @PostMapping(path = "/glossaries/{glossaryGUID}/is-canonical/remove")

    public VoidResponse clearGlossaryAsCanonical(@PathVariable String                    serverName,
                                                 @PathVariable String                    userId,
                                                 @PathVariable String                    glossaryGUID,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                               boolean                   forLineage,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                               boolean                   forDuplicateProcessing,
                                                 @RequestBody(required = false)
                                                               ClassificationRequestBody requestBody)
    {
        return restAPI.clearGlossaryAsCanonical(serverName, userId, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
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
                                               @PathVariable String                         userId,
                                               @PathVariable String                         glossaryGUID,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                        forLineage,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                        forDuplicateProcessing,
                                               @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.createGlossaryCategory(serverName, userId, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Create a new metadata element to represent a glossary category using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                                           @PathVariable String               userId,
                                                           @PathVariable String               glossaryGUID,
                                                           @PathVariable String               templateGUID,
                                                           @RequestBody  TemplateRequestBody  requestBody)
    {
        return restAPI.createGlossaryCategoryFromTemplate(serverName, userId, glossaryGUID, templateGUID, requestBody);
    }


    /**
     * Update the metadata element representing a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                               @PathVariable String                         userId,
                                               @PathVariable String                         glossaryCategoryGUID,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                        isMergeUpdate,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                        forLineage,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                        forDuplicateProcessing,
                                               @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.updateGlossaryCategory(serverName, userId, glossaryCategoryGUID, isMergeUpdate, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Create a parent-child relationship between two categories.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                            @PathVariable String                       userId,
                                            @PathVariable String                       glossaryParentCategoryGUID,
                                            @PathVariable String                       glossaryChildCategoryGUID,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean                      forLineage,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean                      forDuplicateProcessing,
                                            @RequestBody(required = false)
                                                          RelationshipRequestBody requestBody)
    {
        return restAPI.setupCategoryParent(serverName, userId, glossaryParentCategoryGUID, glossaryChildCategoryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove a parent-child relationship between two categories.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                            @PathVariable String                        userId,
                                            @PathVariable String                        glossaryParentCategoryGUID,
                                            @PathVariable String                        glossaryChildCategoryGUID,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean                       forLineage,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean                       forDuplicateProcessing,
                                            @RequestBody(required = false)
                                                          EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.clearCategoryParent(serverName, userId, glossaryParentCategoryGUID, glossaryChildCategoryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the metadata element representing a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                               @PathVariable String                        userId,
                                               @PathVariable String                        glossaryCategoryGUID,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                      forLineage,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                      forDuplicateProcessing,
                                               @RequestBody(required = false)
                                                             ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.removeGlossaryCategory(serverName, userId, glossaryCategoryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /* ===============================================================================
     * A glossary typically contains many glossary terms, linked with relationships.
     */

    /**
     * Return the list of glossary term status enum values.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @return list of enum values
     */
    @GetMapping(path = "/glossaries/terms/status-list")

    public GlossaryTermStatusListResponse getGlossaryTermStatuses(@PathVariable String serverName,
                                                                  @PathVariable String userId)
    {
        return restAPI.getGlossaryTermStatuses(serverName, userId);
    }


    /**
     * Return the list of glossary term relationship status enum values.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @return list of enum values
     */
    @GetMapping(path = "/glossaries/terms/relationships/status-list")

    public GlossaryTermRelationshipStatusListResponse getGlossaryTermRelationshipStatuses(@PathVariable String serverName,
                                                                                          @PathVariable String userId)
    {
        return restAPI.getGlossaryTermRelationshipStatuses(serverName, userId);
    }


    /**
     * Return the list of glossary term relationship status enum values.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @return list of enum values
     */
    @GetMapping(path = "/glossaries/terms/activity-types")

    public GlossaryTermActivityTypeListResponse getGlossaryTermActivityTypes(@PathVariable String serverName,
                                                                             @PathVariable String userId)
    {
        return restAPI.getGlossaryTermActivityTypes(serverName, userId);
    }


    /**
     * Create a new metadata element to represent a glossary term whose lifecycle is managed through a controlled workflow.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                                     @PathVariable String                            userId,
                                                     @PathVariable String                            glossaryGUID,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                           forLineage,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                           forDuplicateProcessing,
                                                     @RequestBody  ControlledGlossaryTermRequestBody requestBody)
    {
        return restAPI.createControlledGlossaryTerm(serverName, userId, glossaryGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Create a new metadata element to represent a glossary term using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/terms/from-template/{templateGUID}")

    public GUIDResponse createGlossaryTermFromTemplate(@PathVariable String               serverName,
                                                       @PathVariable String               userId,
                                                       @PathVariable String               glossaryGUID,
                                                       @PathVariable String               templateGUID,
                                                       @RequestBody  GlossaryTemplateRequestBody requestBody)
    {
        return restAPI.createGlossaryTermFromTemplate(serverName, userId, glossaryGUID, templateGUID, requestBody);
    }


    /**
     * Create a new metadata element to represent a glossary term in an editing glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param editingGlossaryGUID unique identifier of the glossary where the term is located
     * @param glossaryTermGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/editing/{editingGlossaryGUID}/terms/{glossaryTermGUID}/add")

    public GUIDResponse addGlossaryTermToEditingGlossary(@PathVariable String                        serverName,
                                                         @PathVariable String                        userId,
                                                         @PathVariable String                        editingGlossaryGUID,
                                                         @PathVariable String                        glossaryTermGUID,
                                                         @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.addGlossaryTermToEditingGlossary(serverName, userId, editingGlossaryGUID, glossaryTermGUID, requestBody);
    }


    /**
     * Update the metadata element representing a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                           @PathVariable String                         userId,
                                           @PathVariable String                         glossaryTermGUID,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                        isMergeUpdate,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                        forLineage,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                        forDuplicateProcessing,
                                           @RequestBody  ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.updateGlossaryTerm(serverName, userId, glossaryTermGUID, isMergeUpdate, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Update the status of the metadata element representing a glossary term.  This is only valid on
     * a controlled glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                                 @PathVariable String                        userId,
                                                 @PathVariable String                        glossaryTermGUID,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                               boolean                      forLineage,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                               boolean                      forDuplicateProcessing,
                                                 @RequestBody  GlossaryTermStatusRequestBody requestBody)
    {
        return restAPI.updateGlossaryTermStatus(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Link a term to a category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                          @PathVariable String                  userId,
                                          @PathVariable String                  glossaryCategoryGUID,
                                          @PathVariable String                  glossaryTermGUID,
                                          @RequestParam (required = false, defaultValue = "false")
                                                        boolean                 forLineage,
                                          @RequestParam (required = false, defaultValue = "false")
                                                        boolean                 forDuplicateProcessing,
                                          @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupTermCategory(serverName, userId, glossaryCategoryGUID, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Unlink a term from a category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                          @PathVariable String                        userId,
                                          @PathVariable String                        glossaryCategoryGUID,
                                          @PathVariable String                        glossaryTermGUID,
                                          @RequestParam (required = false, defaultValue = "false")
                                                        boolean                       forLineage,
                                          @RequestParam (required = false, defaultValue = "false")
                                                        boolean                       forDuplicateProcessing,
                                          @RequestBody(required = false)
                                                        EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.clearTermCategory(serverName, userId, glossaryCategoryGUID, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Link two terms together using a specialist relationship.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                              @PathVariable String                  userId,
                                              @PathVariable String                  glossaryTermOneGUID,
                                              @PathVariable String                  relationshipTypeName,
                                              @PathVariable String                  glossaryTermTwoGUID,
                                              @RequestParam (required = false, defaultValue = "false")
                                                            boolean                 forLineage,
                                              @RequestParam (required = false, defaultValue = "false")
                                                            boolean                  forDuplicateProcessing,
                                              @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupTermRelationship(serverName, userId, glossaryTermOneGUID, relationshipTypeName, glossaryTermTwoGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Update the relationship properties for the two terms.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                               @PathVariable String                  userId,
                                               @PathVariable String                  glossaryTermOneGUID,
                                               @PathVariable String                  relationshipTypeName,
                                               @PathVariable String                  glossaryTermTwoGUID,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                 forLineage,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                 forDuplicateProcessing,
                                               @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.updateTermRelationship(serverName, userId, glossaryTermOneGUID, relationshipTypeName, glossaryTermTwoGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the relationship between two terms.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                              @PathVariable String                        userId,
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
        return restAPI.clearTermRelationship(serverName, userId, glossaryTermOneGUID, relationshipTypeName, glossaryTermTwoGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes an abstract concept.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                                 @PathVariable String                       userId,
                                                 @PathVariable String                       glossaryTermGUID,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                               boolean                      forLineage,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                               boolean                      forDuplicateProcessing,
                                                 @RequestBody(required = false)
                                                               ClassificationRequestBody    requestBody)
    {
        return restAPI.setTermAsAbstractConcept(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the abstract concept designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                                   @PathVariable String                    userId,
                                                   @PathVariable String                    glossaryTermGUID,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                                 boolean                   forLineage,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                                 boolean                   forDuplicateProcessing,
                                                   @RequestBody(required = false)
                                                                 ClassificationRequestBody requestBody)
    {
        return restAPI.clearTermAsAbstractConcept(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a data field and supply
     * properties that describe the characteristics of the data values found within.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                           @PathVariable String                       userId,
                                           @PathVariable String                       glossaryTermGUID,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                      forLineage,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                      forDuplicateProcessing,
                                           @RequestBody(required = false)
                                                         ClassificationRequestBody    requestBody)
    {
        return restAPI.setTermAsDataField(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the data field designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                             @PathVariable String                    userId,
                                             @PathVariable String                    glossaryTermGUID,
                                             @RequestParam (required = false, defaultValue = "false")
                                                           boolean                   forLineage,
                                             @RequestParam (required = false, defaultValue = "false")
                                                           boolean                   forDuplicateProcessing,
                                             @RequestBody(required = false)
                                                           ClassificationRequestBody requestBody)
    {
        return restAPI.clearTermAsDataField(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                           @PathVariable String                       userId,
                                           @PathVariable String                       glossaryTermGUID,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                      forLineage,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                      forDuplicateProcessing,
                                           @RequestBody(required = false)
                                                         ClassificationRequestBody    requestBody)
    {
        return restAPI.setTermAsDataValue(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the data value designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                             @PathVariable String                    userId,
                                             @PathVariable String                    glossaryTermGUID,
                                             @RequestParam (required = false, defaultValue = "false")
                                                           boolean                   forLineage,
                                             @RequestParam (required = false, defaultValue = "false")
                                                           boolean                   forDuplicateProcessing,
                                             @RequestBody(required = false)
                                                           ClassificationRequestBody requestBody)
    {
        return restAPI.clearTermAsDataValue(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                          @PathVariable String                    userId,
                                          @PathVariable String                    glossaryTermGUID,
                                          @RequestParam (required = false, defaultValue = "false")
                                                        boolean                   forLineage,
                                          @RequestParam (required = false, defaultValue = "false")
                                                        boolean                   forDuplicateProcessing,
                                          @RequestBody  ClassificationRequestBody requestBody)
    {
        return restAPI.setTermAsActivity(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the activity designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                            @PathVariable String                    userId,
                                            @PathVariable String                    glossaryTermGUID,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean                   forLineage,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean                   forDuplicateProcessing,
                                            @RequestBody(required = false)
                                                          ClassificationRequestBody requestBody)
    {
        return restAPI.clearTermAsActivity(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a context.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                         @PathVariable String                    userId,
                                         @PathVariable String                    glossaryTermGUID,
                                         @RequestParam (required = false, defaultValue = "false")
                                                       boolean                   forLineage,
                                         @RequestParam (required = false, defaultValue = "false")
                                                       boolean                   forDuplicateProcessing,
                                         @RequestBody  ClassificationRequestBody requestBody)
    {
        return restAPI.setTermAsContext(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the context definition designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                           @PathVariable String                   userId,
                                           @PathVariable String                   glossaryTermGUID,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                  forLineage,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                  forDuplicateProcessing,
                                           @RequestBody(required = false)
                                                         ClassificationRequestBody requestBody)
    {
        return restAPI.clearTermAsContext(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a spine object.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                             @PathVariable String                   userId,
                                             @PathVariable String                   glossaryTermGUID,
                                             @RequestParam (required = false, defaultValue = "false")
                                                           boolean                  forLineage,
                                             @RequestParam (required = false, defaultValue = "false")
                                                           boolean                  forDuplicateProcessing,
                                             @RequestBody(required = false)
                                                           ClassificationRequestBody requestBody)
    {
        return restAPI.setTermAsSpineObject(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the spine object designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                               @PathVariable String                        userId,
                                               @PathVariable String                        glossaryTermGUID,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                       forLineage,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                       forDuplicateProcessing,
                                               @RequestBody(required = false)
                                                             ClassificationRequestBody     requestBody)
    {
        return restAPI.clearTermAsSpineObject(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a spine attribute.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                                @PathVariable String                        userId,
                                                @PathVariable String                        glossaryTermGUID,
                                                @RequestParam (required = false, defaultValue = "false")
                                                              boolean                      forLineage,
                                                @RequestParam (required = false, defaultValue = "false")
                                                              boolean                      forDuplicateProcessing,
                                                @RequestBody(required = false)
                                                              ClassificationRequestBody    requestBody)
    {
        return restAPI.setTermAsSpineAttribute(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the spine attribute designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                                  @PathVariable String                        userId,
                                                  @PathVariable String                        glossaryTermGUID,
                                                  @RequestParam (required = false, defaultValue = "false")
                                                                boolean                       forLineage,
                                                  @RequestParam (required = false, defaultValue = "false")
                                                                boolean                       forDuplicateProcessing,
                                                  @RequestBody(required = false)
                                                                ClassificationRequestBody     requestBody)
    {
        return restAPI.clearTermAsSpineAttribute(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes an object identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                                  @PathVariable String                   userId,
                                                  @PathVariable String                   glossaryTermGUID,
                                                  @RequestParam (required = false, defaultValue = "false")
                                                               boolean                  forLineage,
                                                  @RequestParam (required = false, defaultValue = "false")
                                                               boolean                  forDuplicateProcessing,
                                                  @RequestBody(required = false)
                                                               ClassificationRequestBody requestBody)
    {
        return restAPI.setTermAsObjectIdentifier(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the object identifier designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                                    @PathVariable String                        userId,
                                                    @PathVariable String                        glossaryTermGUID,
                                                    @RequestParam (required = false, defaultValue = "false")
                                                                  boolean                      forLineage,
                                                    @RequestParam (required = false, defaultValue = "false")
                                                                  boolean                      forDuplicateProcessing,
                                                    @RequestBody(required = false)
                                                                  ClassificationRequestBody            requestBody)
    {
        return restAPI.clearTermAsObjectIdentifier(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Undo the last update to the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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

    public GlossaryTermElementResponse undoGlossaryTermUpdate(@PathVariable String             serverName,
                                                              @PathVariable String             userId,
                                                              @PathVariable String             glossaryTermGUID,
                                                              @RequestParam (required = false, defaultValue = "false")
                                                                            boolean           forLineage,
                                                              @RequestParam (required = false, defaultValue = "false")
                                                                            boolean           forDuplicateProcessing,
                                                              @RequestBody(required = false)
                                                                            UpdateRequestBody requestBody)
    {
        return restAPI.undoGlossaryTermUpdate(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Archive the metadata element representing a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                            @PathVariable String             userId,
                                            @PathVariable String             glossaryTermGUID,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean            forDuplicateProcessing,
                                            @RequestBody(required = false)
                                                          ArchiveRequestBody requestBody)
    {
        return restAPI.archiveGlossaryTerm(serverName, userId, glossaryTermGUID, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the metadata element representing a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
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
                                           @PathVariable String                         userId,
                                           @PathVariable String                         glossaryTermGUID,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                        forLineage,
                                           @RequestParam (required = false, defaultValue = "false")
                                                         boolean                        forDuplicateProcessing,
                                           @RequestBody(required = false)
                                                         ReferenceableUpdateRequestBody requestBody)
    {
        return restAPI.removeGlossaryTerm(serverName, userId, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }
}

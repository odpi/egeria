/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.glossarymanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTermActivityTypeListResponse;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTermRelationshipStatusListResponse;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTermStatusListResponse;
import org.odpi.openmetadata.viewservices.glossarymanager.server.GlossaryManagerRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    public GUIDResponse createGlossary(@PathVariable String                serverName,
                                       @RequestBody  NewElementRequestBody requestBody)
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
                                                   @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createGlossaryFromTemplate(serverName, templateGUID, requestBody);
    }


    /**
     * Update the metadata element representing a glossary.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to update
     * @param requestBody new properties for this element
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/update")

    public VoidResponse updateGlossary(@PathVariable String                   serverName,
                                       @PathVariable String                   glossaryGUID,
                                       @RequestBody  UpdateElementRequestBody requestBody)
    {
        return restAPI.updateGlossary(serverName, glossaryGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a glossary.  This will delete the glossary and all categories
     * and terms.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/delete")

    public VoidResponse deleteGlossary(@PathVariable String                         serverName,
                                       @PathVariable String                         glossaryGUID,
                                       @RequestBody(required = false)
                                           DeleteRequestBody requestBody)
    {
        return restAPI.deleteGlossary(serverName, glossaryGUID,  requestBody);
    }


    /**
     * Classify the glossary to indicate that it is an editing glossary - this means it is
     * a collection of glossary updates that will be merged into its source glossary.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to remove
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
                                                     @RequestBody NewClassificationRequestBody requestBody)
    {
        return restAPI.setGlossaryAsEditingGlossary(serverName, glossaryGUID, requestBody);
    }


    /**
     * Remove the editing glossary designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/is-editing-glossary/delete")

    public VoidResponse clearGlossaryAsEditingGlossary(@PathVariable String                    serverName,
                                                       @PathVariable String                    glossaryGUID,
                                                       @RequestBody(required = false)
                                                                     MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearGlossaryAsEditingGlossary(serverName, glossaryGUID, requestBody);
    }


    /**
     * Classify the glossary to indicate that it is a staging glossary - this means it is
     * a collection of glossary updates that will be transferred into another glossary.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to remove
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
                                                     @RequestBody NewClassificationRequestBody requestBody)
    {
        return restAPI.setGlossaryAsStagingGlossary(serverName, glossaryGUID, requestBody);
    }


    /**
     * Remove the staging glossary designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/is-staging-glossary/delete")

    public VoidResponse clearGlossaryAsStagingGlossary(@PathVariable String                    serverName,
                                                       @PathVariable String                    glossaryGUID,
                                                       @RequestBody(required = false)
                                                                     MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearGlossaryAsStagingGlossary(serverName, glossaryGUID, requestBody);
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
                                              @RequestBody NewClassificationRequestBody requestBody)
    {
        return restAPI.setGlossaryAsTaxonomy(serverName, glossaryGUID, requestBody);
    }


    /**
     * Remove the taxonomy designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to remove
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
                                                @RequestBody(required = false)
                                                              MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearGlossaryAsTaxonomy(serverName, glossaryGUID, requestBody);
    }


    /**
     * Classify a glossary to declare that it has no two GlossaryTerm definitions with
     * the same name.  This means there is only one definition for each term.  Typically, the terms are also of a similar
     * level of granularity and are limited to a specific scope of use.
     * Canonical vocabularies are used to semantically classify assets in an unambiguous way.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to remove
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
                                               @RequestBody NewClassificationRequestBody requestBody)
    {
        return restAPI.setGlossaryAsCanonical(serverName, glossaryGUID, requestBody);
    }


    /**
     * Remove the canonical vocabulary designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to remove
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
                                                 @RequestBody(required = false)
                                                               MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearGlossaryAsCanonical(serverName, glossaryGUID, requestBody);
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
     * Create a new metadata element to represent a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms")

    public GUIDResponse createGlossaryTerm(@PathVariable String                  serverName,
                                           @RequestBody  NewElementRequestBody requestBody)
    {
        return restAPI.createGlossaryTerm(serverName,  requestBody);
    }



    /**
     * Create a new metadata element to represent a glossary term using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/from-template/{templateGUID}")

    public GUIDResponse createGlossaryTermFromTemplate(@PathVariable String                      serverName,
                                                       @PathVariable String                      templateGUID,
                                                       @RequestBody  GlossaryTemplateRequestBody requestBody)
    {
        return restAPI.createGlossaryTermFromTemplate(serverName, templateGUID,  requestBody);
    }


    /**
     * Update the metadata element representing a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param requestBody new properties for the glossary term
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/update")

    public VoidResponse updateGlossaryTerm(@PathVariable String                   serverName,
                                           @PathVariable String                   glossaryTermGUID,
                                           @RequestBody  UpdateElementRequestBody requestBody)
    {
        return restAPI.updateGlossaryTerm(serverName, glossaryTermGUID, requestBody);
    }


    /**
     * Update the glossary term using the properties and classifications from the parentGUID stored in the request body.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param templateGUID template to use
     * @param requestBody status and correlation properties
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/update/from-template/{templateGUID}")

    public VoidResponse updateGlossaryTermFromTemplate(@PathVariable String                        serverName,
                                                       @PathVariable String                        glossaryTermGUID,
                                                       @PathVariable String                        templateGUID,
                                                       @RequestBody UpdateWithTemplateRequestBody requestBody)
    {
        return restAPI.updateGlossaryTermFromTemplate(serverName, glossaryTermGUID, templateGUID, requestBody);
    }


    /**
     * Move the glossary term from one glossary to another.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param glossaryGUID unique identifier of the destination glossary
     * @param requestBody status and correlation properties
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/move-to/{glossaryGUID}")

    public VoidResponse moveGlossaryTerm(@PathVariable String                   serverName,
                                         @PathVariable String                   glossaryTermGUID,
                                         @PathVariable String                   glossaryGUID,
                                         @RequestBody DeleteRequestBody requestBody)
    {
        return restAPI.moveGlossaryTerm(serverName, glossaryTermGUID, glossaryGUID, requestBody);
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
                                              @RequestBody(required = false) NewRelationshipRequestBody requestBody)
    {
        return restAPI.setupTermRelationship(serverName, glossaryTermOneGUID, relationshipTypeName, glossaryTermTwoGUID, requestBody);
    }


    /**
     * Update the relationship properties for the two terms.
     *
     * @param serverName name of the server to route the request to
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

    public VoidResponse updateTermRelationship(@PathVariable String                  serverName,
                                               @PathVariable String                  glossaryTermOneGUID,
                                               @PathVariable String                  relationshipTypeName,
                                               @PathVariable String                  glossaryTermTwoGUID,
                                               @RequestBody  UpdateRelationshipRequestBody requestBody)
    {
        return restAPI.updateTermRelationship(serverName, glossaryTermOneGUID, relationshipTypeName, glossaryTermTwoGUID, requestBody);
    }


    /**
     * Remove the relationship between two terms.
     *
     * @param serverName name of the server to route the request to
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

    public VoidResponse clearTermRelationship(@PathVariable String                        serverName,
                                              @PathVariable String                        glossaryTermOneGUID,
                                              @PathVariable String                        relationshipTypeName,
                                              @PathVariable String                        glossaryTermTwoGUID,
                                              @RequestBody(required = false)
                                                  DeleteRequestBody requestBody)
    {
        return restAPI.clearTermRelationship(serverName, glossaryTermOneGUID, relationshipTypeName, glossaryTermTwoGUID, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes an abstract concept.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
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
                                                 @RequestBody(required = false)
                                                     NewClassificationRequestBody requestBody)
    {
        return restAPI.setTermAsAbstractConcept(serverName, glossaryTermGUID, requestBody);
    }


    /**
     * Remove the abstract concept designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-abstract-concept/delete")

    public VoidResponse clearTermAsAbstractConcept(@PathVariable String                    serverName,
                                                   @PathVariable String                    glossaryTermGUID,
                                                   @RequestBody(required = false)
                                                                 MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearTermAsAbstractConcept(serverName, glossaryTermGUID, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
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

                                           @RequestBody(required = false)
                                               NewClassificationRequestBody requestBody)
    {
        return restAPI.setTermAsDataValue(serverName, glossaryTermGUID, requestBody);
    }


    /**
     * Remove the data value designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
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
                                             @RequestBody(required = false)
                                                           MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearTermAsDataValue(serverName, glossaryTermGUID, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
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
                                          @RequestBody NewClassificationRequestBody requestBody)
    {
        return restAPI.setTermAsActivity(serverName, glossaryTermGUID, requestBody);
    }


    /**
     * Remove the activity designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
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
                                            @RequestBody(required = false)
                                                          MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearTermAsActivity(serverName, glossaryTermGUID, requestBody);
    }


    /**
     * Classify the glossary term to indicate that it describes a context.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
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
                                         @RequestBody NewClassificationRequestBody requestBody)
    {
        return restAPI.setTermAsContext(serverName, glossaryTermGUID, requestBody);
    }


    /**
     * Remove the context definition designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/is-context-definition/delete")

    public VoidResponse clearTermAsContext(@PathVariable String                   serverName,
                                           @PathVariable String                   glossaryTermGUID,
                                           @RequestBody(required = false)
                                                         MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearTermAsContext(serverName, glossaryTermGUID,requestBody);
    }


    /**
     * Remove the metadata element representing a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the metadata element to remove
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/delete")

    public VoidResponse deleteGlossaryTerm(@PathVariable String                         serverName,
                                           @PathVariable String                         glossaryTermGUID,
                                           @RequestBody(required = false)
                                               DeleteRequestBody requestBody)
    {
        return restAPI.deleteGlossaryTerm(serverName, glossaryTermGUID, requestBody);
    }
}

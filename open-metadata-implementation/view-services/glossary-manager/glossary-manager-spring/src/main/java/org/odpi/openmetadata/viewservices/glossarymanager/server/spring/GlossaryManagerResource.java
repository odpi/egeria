/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.glossarymanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: Glossary Manager",
     description="Create and maintain glossary terms and organize them into glossaries.  This work may be part of a controlled workflow process allowing for review and approval cycles.",
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
     * Classify the glossary to indicate that it can be used as a taxonomy.
     * This means each term is attached to one, and only one folder and the folders are organized as a hierarchy
     * with a single root folder.
     * Taxonomies are used as a way of organizing assets and other related metadata.  The terms in the taxonomy
     * are linked to the assets etc. and as such they are logically categorized by the linked folder.
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
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setGlossaryAsTaxonomy",
            description="Classify the glossary to indicate that it can be used as a taxonomy." +
                    " This means each term is attached to one, and only one folder and the folders are organized as a hierarchy" +
                    " with a single root folder." +
                    " Taxonomies are used as a way of organizing assets and other related metadata.  The terms in the taxonomy" +
                    " are linked to the assets etc. and as such they are logically categorized by the linked folder.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/glossary"))

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
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearGlossaryAsTaxonomy",
            description="Remove the taxonomy designation from the glossary.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/glossary"))
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
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setGlossaryAsCanonical",
            description="Classify a glossary to declare that it has no two GlossaryTerm definitions with" +
                    " the same name.  This means there is only one definition for each term.  Typically, the terms are also of a similar" +
                    " level of granularity and are limited to a specific scope of use." +
                    " Canonical vocabularies are used to semantically classify assets in an unambiguous way.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/glossary"))

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
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearGlossaryAsCanonical",
            description="Remove the canonical vocabulary designation from the glossary.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/glossary"))

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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

    public GUIDResponse createGlossaryTermFromTemplate(@PathVariable String              serverName,
                                                       @PathVariable String              templateGUID,
                                                       @RequestBody  TemplateRequestBody requestBody)
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
     * @return  boolean or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    public BooleanResponse updateGlossaryTerm(@PathVariable String                   serverName,
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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse moveGlossaryTerm(@PathVariable String                   serverName,
                                         @PathVariable String                   glossaryTermGUID,
                                         @PathVariable String                   glossaryGUID,
                                         @RequestBody(required = false) DeleteRelationshipRequestBody requestBody)
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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse clearTermRelationship(@PathVariable String                        serverName,
                                              @PathVariable String                        glossaryTermOneGUID,
                                              @PathVariable String                        relationshipTypeName,
                                              @PathVariable String                        glossaryTermTwoGUID,
                                              @RequestBody(required = false)
                                                  DeleteRelationshipRequestBody requestBody)
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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse deleteGlossaryTerm(@PathVariable String                         serverName,
                                           @PathVariable String                         glossaryTermGUID,
                                           @RequestBody(required = false)
                                               DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteGlossaryTerm(serverName, glossaryTermGUID, requestBody);
    }



    /**
     * Retrieve the list of glossary term metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody query
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findGlossaryTerms",
            description="Retrieve the list of glossary term metadata elements that contain the search string.  The search string is located in the request body and is interpreted as a plain string.  The request parameters, startsWith, endsWith and ignoreCase can be used to allow a fuzzy search.  The request body also supports the specification of a glossaryGUID to restrict the search to within a single glossary.",
            externalDocs=@ExternalDocumentation(description="Glossary term metadata element",
                    url="https://egeria-project.org/types/3/0330-Terms/"))

    public OpenMetadataRootElementsResponse findGlossaryTerms(@PathVariable String                  serverName,
                                                              @RequestBody(required = false)  SearchStringRequestBody requestBody)
    {
        return restAPI.findGlossaryTerms(serverName, requestBody);
    }


    /**
     * Retrieve the list of glossary term metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody asset manager identifiers and name
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    public OpenMetadataRootElementsResponse   getGlossaryTermsByName(@PathVariable String             serverName,
                                                                     @RequestBody(required = false)  FilterRequestBody requestBody)
    {
        return restAPI.getGlossaryTermsByName(serverName, requestBody);
    }


    /**
     * Retrieve the glossary term metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the requested metadata element
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    public OpenMetadataRootElementResponse getGlossaryTermByGUID(@PathVariable String                             serverName,
                                                                 @PathVariable String                             glossaryTermGUID,
                                                                 @RequestBody(required = false) GetRequestBody requestBody)
    {
        return restAPI.getGlossaryTermByGUID(serverName, glossaryTermGUID, requestBody);
    }
}

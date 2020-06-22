/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse2;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaRelationshipRESTServices;
import org.springframework.web.bind.annotation.*;



/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides relationship authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/subject-area")
@Tag(name = "Subject Area OMAS", description = "The Subject Area OMAS supports subject matter experts who are documenting their knowledge about a particular subject. This includes glossary terms, reference data, validation rules.", externalDocs = @ExternalDocumentation(description = "Subject Area Open Metadata Access Service (OMAS)", url = "https://egeria.odpi.org/open-metadata-implementation/access-services/subject-area/"))
public class SubjectAreaRelationshipRESTResource {
    private SubjectAreaRelationshipRESTServices restAPI = new SubjectAreaRelationshipRESTServices();

    /**
     * Default constructor
     */
    public SubjectAreaRelationshipRESTResource() {

    }

    /**
     * Create a TermHASARelationship is the relationship between a spine object and a spine attribute.
     * Note that this method does not error if the relationship ends are not spine objects or spine attributes.
     * This allows the user to create terms then make them spine objects and spine attributes at a later stage.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @return response, when successful contains the created TermHASARelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/has-as")
    public SubjectAreaOMASAPIResponse2<Hasa> createTermHASARelationship(@PathVariable String serverName,
                                                                        @PathVariable String userId,

                                                                        @RequestBody Hasa termHASARelationship) {
        return restAPI.createTermHASARelationship(serverName, userId, termHASARelationship);
    }

    /**
     * Get a Term HAS A relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the HAS A relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/has-as/{guid}")
    public SubjectAreaOMASAPIResponse2<Hasa> getTermHASARelationship(@PathVariable String serverName,
                                                                     @PathVariable String userId,
                                                                     @PathVariable String guid) {
        return restAPI.getTermHASARelationship(serverName, userId, guid);
    }

    /**
     * Update a TermHASARelationship is the relationship between a spine object and a spine attribute.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param guid                 unique identifier of the Line
     * @param termHASARelationship the HASA relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created TermHASARelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/has-as/{guid}")
    public SubjectAreaOMASAPIResponse2<Hasa> updateTermHASARelationship(@PathVariable String serverName,
                                                                        @PathVariable String userId,
                                                                        @PathVariable String guid,
                                                                        @RequestBody Hasa termHASARelationship,
                                                                        @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace
    ) {
        return restAPI.updateTermHASARelationship(serverName, userId, guid, termHASARelationship, isReplace);
    }

    /**
     * Delete a Term HAS A relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the HAS A relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/has-as/{guid}")
    public SubjectAreaOMASAPIResponse2<Hasa> deleteTermHASARelationship(@PathVariable String serverName,
                                                                        @PathVariable String userId,
                                                                        @PathVariable String guid,
                                                                        @RequestParam(value = "isPurge", required = false, defaultValue = "false") Boolean isPurge
    ) {
        return restAPI.deleteTermHASARelationship(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a has a relationship.
     * <p>
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/has-as/{guid}")
    public SubjectAreaOMASAPIResponse2<Hasa> restoreTermHASARelationship(@PathVariable String serverName,
                                                                         @PathVariable String userId,
                                                                         @PathVariable String guid) {
        return restAPI.restoreTermHASARelationship(serverName, userId, guid);
    }

    /**
     * Create a RelatedTerm. A Related Term is a link between two similar Terms.
     *
     * @param serverName                          serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                              userId under which the request is performed
     * @param relatedTermRelationshipRelationship the RelatedTerm relationship
     * @return response, when successful contains the created Related Term relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/related-terms")
    public SubjectAreaOMASAPIResponse2<RelatedTerm> createRelatedTerm(@PathVariable String serverName,
                                                                      @PathVariable String userId,
                                                                      @RequestBody RelatedTerm relatedTermRelationshipRelationship) {
        return restAPI.createRelatedTerm(serverName, userId, relatedTermRelationshipRelationship);
    }

    /**
     * Get a related Term relationship.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the related term relationship to get
     * @return response which when successful contains the related term relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/related-terms/{guid}")
    public SubjectAreaOMASAPIResponse2<RelatedTerm> getRelatedTerm(@PathVariable String serverName,
                                                                   @PathVariable String userId,
                                                                   @PathVariable String guid) {
        return restAPI.getRelatedTerm(serverName, userId, guid);
    }

    /**
     * Update a Related Term relationship. A Related Term is a link between two similar Terms.
     * <p>
     *
     * @param serverName              serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                  userId under which the request is performed
     * @param guid                    unique identifier of the Line
     * @param relatedTermRelationship the related term  relationship
     * @param isReplace               flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created RelatedTerm
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/related-terms/{guid}")
    public SubjectAreaOMASAPIResponse2<RelatedTerm> updateRelatedTerm(@PathVariable String serverName,
                                                                      @PathVariable String userId,
                                                                      @PathVariable String guid,
                                                                      @RequestBody RelatedTerm relatedTermRelationship,
                                                                      @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace
    ) {
        return restAPI.updateRelatedTerm(serverName, userId, guid, relatedTermRelationship, isReplace);
    }

    /**
     * Delete a Related Term relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Related term relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/related-terms/{guid}")
    public SubjectAreaOMASAPIResponse2<RelatedTerm> deleteRelatedTerm(@PathVariable String serverName,
                                                                      @PathVariable String userId,
                                                                      @PathVariable String guid,
                                                                      @RequestParam(value = "isPurge", required = false, defaultValue = "false") Boolean isPurge
    ) {
        return restAPI.deleteRelatedTerm(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a related Term relationship.
     * <p>
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/related-terms/{guid}")
    public SubjectAreaOMASAPIResponse2<RelatedTerm> restoreRelatedTerm(@PathVariable String serverName,
                                                                       @PathVariable String userId,
                                                                       @PathVariable String guid) {
        return restAPI.restoreRelatedTerm(serverName, userId, guid);
    }

    /**
     * Create a synonym relationship, which is a link between glossary terms that have the same meaning.
     *
     * <p>
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param synonym    the Synonym relationship
     * @return response, when successful contains the created synonym relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/synonyms")
    public SubjectAreaOMASAPIResponse2<Synonym> createSynonym(@PathVariable String serverName,
                                                              @PathVariable String userId,
                                                              @RequestBody Synonym synonym) {
        return restAPI.createSynonym(serverName, userId, synonym);
    }

    /**
     * Get a synonym relationship, which is a link between glossary terms that have the same meaning.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the synonym relationship to get
     * @return response which when successful contains the synonym relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/synonyms/{guid}")
    public SubjectAreaOMASAPIResponse2<Synonym> getSynonymRelationship(@PathVariable String serverName,
                                                                       @PathVariable String userId,
                                                                       @PathVariable String guid) {
        return restAPI.getSynonymRelationship(serverName, userId, guid);
    }

    /**
     * Update a Synonym relationship which is a link between glossary terms that have the same meaning
     * <p>
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param guid       unique identifier of the Line
     * @param synonym    the synonym  relationship
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created SynonymRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/synonyms/{guid}")
    public SubjectAreaOMASAPIResponse2<Synonym> updateSynonymRelationship(@PathVariable String serverName,
                                                                          @PathVariable String userId,
                                                                          @PathVariable String guid,
                                                                          @RequestBody Synonym synonym,
                                                                          @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace
    ) {
        return restAPI.updateSynonymRelationship(serverName, userId, guid, synonym, isReplace);
    }

    /**
     * Delete a Synonym relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Synonym relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/synonyms/{guid}")
    public SubjectAreaOMASAPIResponse2<Synonym> deleteSynonymRelationship(@PathVariable String serverName,
                                                                          @PathVariable String userId,
                                                                          @PathVariable String guid,
                                                                          @RequestParam(value = "isPurge", required = false, defaultValue = "false") Boolean isPurge
    ) {
        return restAPI.deleteSynonymRelationship(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a Synonym
     * <p>
     * Restore allows the deleted Synonym to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Synonym to delete
     * @return response which when successful contains the restored Synonym
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/synonyms/{guid}")
    public SubjectAreaOMASAPIResponse2<Synonym> restoreSynonym(@PathVariable String serverName,
                                                               @PathVariable String userId,
                                                               @PathVariable String guid) {
        return restAPI.restoreSynonym(serverName, userId, guid);
    }

    /**
     * Create an antonym relationship, which is a link between glossary terms that have the opposite meaning.
     *
     * <p>
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param antonym    the Antonym relationship
     * @return response, when successful contains the created antonym relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/antonyms")
    public SubjectAreaOMASAPIResponse2<Antonym> createAntonym(@PathVariable String serverName,
                                                              @PathVariable String userId,
                                                              @RequestBody Antonym antonym) {
        return restAPI.createAntonym(serverName, userId, antonym);
    }

    /**
     * Get a antonym relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the related term relationship to get
     * @return response which when successful contains the antonym relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/antonyms/{guid}")
    public SubjectAreaOMASAPIResponse2<Antonym> getAntonymRelationship(@PathVariable String serverName,
                                                                       @PathVariable String userId,
                                                                       @PathVariable String guid) {
        return restAPI.getAntonymRelationship(serverName, userId, guid);
    }

    /**
     * Update a Antonym relationship which is a link between glossary terms that have the opposite meaning
     * <p>
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param guid       unique identifier of the Line
     * @param antonym    the antonym relationship
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created AntonymRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/antonyms/{guid}")
    public SubjectAreaOMASAPIResponse2<Antonym> updateAntonymRelationship(@PathVariable String serverName,
                                                                          @PathVariable String userId,
                                                                          @PathVariable String guid,
                                                                          @RequestBody Antonym antonym,
                                                                          @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace
    ) {
        return restAPI.updateAntonymRelationship(serverName, userId, guid, antonym, isReplace);
    }

    /**
     * Delete a Antonym relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Antonym relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/antonyms/{guid}")
    public SubjectAreaOMASAPIResponse2<Antonym> deleteAntonymRelationship(@PathVariable String serverName,
                                                                          @PathVariable String userId,
                                                                          @PathVariable String guid,
                                                                          @RequestParam(value = "isPurge", required = false, defaultValue = "false") Boolean isPurge
    ) {
        return restAPI.deleteAntonymRelationship(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a Antonym
     * <p>
     * Restore allows the deleted Antonym to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Antonym to delete
     * @return response which when successful contains the restored Antonym
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/antonyms/{guid}")
    public SubjectAreaOMASAPIResponse2<Antonym> restoreAntonym(@PathVariable String serverName,
                                                               @PathVariable String userId,
                                                               @PathVariable String guid) {
        return restAPI.restoreAntonym(serverName, userId, guid);
    }

    /**
     * Create a translation relationship, which is a link between glossary terms to provide different natural language translation of the same concept.
     *
     * <p>
     *
     * @param serverName  serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId      userId under which the request is performed
     * @param translation the Translation relationship
     * @return response, when successful contains the created translation relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/translations")
    public SubjectAreaOMASAPIResponse2<Translation> createTranslation(@PathVariable String serverName,
                                                                      @PathVariable String userId,
                                                                      @RequestBody Translation translation) {
        return restAPI.createTranslation(serverName, userId, translation);
    }

    /**
     * Get a translation relationshiptranslation relationship, which is a link between glossary terms to provide different natural language translation of the same concept.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the translation relationship to get
     * @return response which when successful contains the translation relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/translations/{guid}")
    public SubjectAreaOMASAPIResponse2<Translation> getTranslationRelationship(@PathVariable String serverName,
                                                                               @PathVariable String userId,
                                                                               @PathVariable String guid) {
        return restAPI.getTranslationRelationship(serverName, userId, guid);
    }

    /**
     * Update a Translation relationship translation relationship, which is a link between glossary terms to provide different natural language translation of the same concept.
     * <p>
     *
     * @param serverName  serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId      userId under which the request is performed
     * @param guid        unique identifier of the Line
     * @param translation the translation relationship
     * @param isReplace   flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created TranslationRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/translations/{guid}")
    public SubjectAreaOMASAPIResponse2<Translation> updateTranslationRelationship(@PathVariable String serverName,
                                                                                  @PathVariable String userId,
                                                                                  @PathVariable String guid,
                                                                                  @RequestBody Translation translation,
                                                                                  @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace
    ) {
        return restAPI.updateTranslationRelationship(serverName, userId, guid, translation, isReplace);
    }

    /**
     * Delete a Translation relationshiptranslation relationship, which is a link between glossary terms to provide different natural language translation of the same concept.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Translation relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/translations/{guid}")
    public SubjectAreaOMASAPIResponse2<Translation> deleteTranslationRelationship(@PathVariable String serverName,
                                                                                  @PathVariable String userId,
                                                                                  @PathVariable String guid,
                                                                                  @RequestParam(value = "isPurge", required = false, defaultValue = "false") Boolean isPurge
    ) {
        return restAPI.deleteTranslationRelationship(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a Translation
     * <p>
     * Restore allows the deleted Translation to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Translation to delete
     * @return response which when successful contains the restored Translation
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/translations/{guid}")
    public SubjectAreaOMASAPIResponse2<Translation> restoreTranslation(@PathVariable String serverName,
                                                                       @PathVariable String userId,
                                                                       @PathVariable String guid) {
        return restAPI.restoreTranslation(serverName, userId, guid);
    }

    /**
     * Create a usedInContext relationship, which is a link between glossary terms, where one describes the context where the other one is valid to use.
     *
     * <p>
     *
     * @param serverName    serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId        userId under which the request is performed
     * @param usedInContext the UsedInContext relationship
     * @return response, when successful contains the created usedInContext relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/used-in-contexts")
    public SubjectAreaOMASAPIResponse2<UsedInContext> createusedInContext(@PathVariable String serverName,
                                                                          @PathVariable String userId,
                                                                          @RequestBody UsedInContext usedInContext) {
        return restAPI.createUsedInContext(serverName, userId, usedInContext);
    }

    /**
     * Get a usedInContext relationship,  which is a link between glossary terms, where one describes the context where the other one is valid to use.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the usedInContext relationship to get
     * @return response which when successful contains the usedInContext relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/used-in-contexts/{guid}")
    public SubjectAreaOMASAPIResponse2<UsedInContext> getUsedInContextRelationship(@PathVariable String serverName,
                                                                                   @PathVariable String userId,
                                                                                   @PathVariable String guid) {
        return restAPI.getUsedInContextRelationship(serverName, userId, guid);
    }

    /**
     * Update a UsedInContext relationship which is a link between glossary terms, where one describes the context where the other one is valid to use.
     * <p>
     *
     * @param serverName    serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId        userId under which the request is performed
     * @param guid          unique identifier of the Line
     * @param usedInContext the used in context relationship
     * @param isReplace     flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created UsedInContextRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/used-in-contexts/{guid}")
    public SubjectAreaOMASAPIResponse2<UsedInContext> updateUsedInContextRelationship(@PathVariable String serverName,
                                                                                      @PathVariable String userId,
                                                                                      @PathVariable String guid,
                                                                                      @RequestBody UsedInContext usedInContext,
                                                                                      @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace
    ) {
        return restAPI.updateUsedInContextRelationship(serverName, userId, guid, usedInContext, isReplace);
    }

    /**
     * Delete a UsedInContext relationship which is a link between glossary terms, where one describes the context where the other one is valid to use.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the UsedInContext relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/used-in-contexts/{guid}")
    public SubjectAreaOMASAPIResponse2<UsedInContext> deleteUsedInContextRelationship(@PathVariable String serverName,
                                                                                      @PathVariable String userId,
                                                                                      @PathVariable String guid,
                                                                                      @RequestParam(value = "isPurge", required = false, defaultValue = "false") Boolean isPurge
    ) {
        return restAPI.deleteUsedInContextRelationship(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a UsedInContext
     * <p>
     * Restore allows the deleted UsedInContext to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the UsedInContext to delete
     * @return response which when successful contains the restored UsedInContext
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/used-in-contexts/{guid}")
    public SubjectAreaOMASAPIResponse2<UsedInContext> restoreUsedInContext(@PathVariable String serverName,
                                                                           @PathVariable String userId,
                                                                           @PathVariable String guid) {
        return restAPI.restoreUsedInContext(serverName, userId, guid);
    }

    /**
     * Create a preferredTerm relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     *
     * <p>
     *
     * @param serverName    serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId        userId under which the request is performed
     * @param preferredTerm the preferred term relationship
     * @return response, when successful contains the created preferredTerm relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/preferred-terms")
    public SubjectAreaOMASAPIResponse2<PreferredTerm> createPreferredTerm(@PathVariable String serverName,
                                                                          @PathVariable String userId,
                                                                          @RequestBody PreferredTerm preferredTerm) {
        return restAPI.createPreferredTerm(serverName, userId, preferredTerm);
    }

    /**
     * Get a preferredTerm relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the preferredTerm relationship to get
     * @return response which when successful contains the preferredTerm relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/preferred-terms/{guid}")
    public SubjectAreaOMASAPIResponse2<PreferredTerm> getPreferredTermRelationship(@PathVariable String serverName,
                                                                                   @PathVariable String userId,
                                                                                   @PathVariable String guid) {
        return restAPI.getPreferredTermRelationship(serverName, userId, guid);
    }

    /**
     * Update a PreferredTerm relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     * <p>
     *
     * @param serverName    serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId        userId under which the request is performed
     * @param guid          unique identifier of the Line
     * @param preferredTerm the preferred term relationship
     * @param isReplace     flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created PreferredTermRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> FunctionNotSupportedException        Function not supported
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/preferred-terms/{guid}")
    public SubjectAreaOMASAPIResponse2<PreferredTerm> updatePreferredTermRelationship(@PathVariable String serverName,
                                                                                      @PathVariable String userId,
                                                                                      @PathVariable String guid,
                                                                                      @RequestBody PreferredTerm preferredTerm,
                                                                                      @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace
    ) {
        return restAPI.updatePreferredTermRelationship(serverName, userId, guid, preferredTerm, isReplace);
    }

    /**
     * Delete a PreferredTerm relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the PreferredTerm relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/preferred-terms/{guid}")
    public SubjectAreaOMASAPIResponse2<PreferredTerm> deletePreferredTermRelationship(@PathVariable String serverName,
                                                                                      @PathVariable String userId,
                                                                                      @PathVariable String guid,
                                                                                      @RequestParam(value = "isPurge", required = false, defaultValue = "false") Boolean isPurge
    ) {
        return restAPI.deletePreferredTermRelationship(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a PreferredTerm
     * <p>
     * Restore allows the deleted PreferredTerm to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the PreferredTerm to delete
     * @return response which when successful contains the restored PreferredTerm
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/preferred-terms/{guid}")
    public SubjectAreaOMASAPIResponse2<PreferredTerm> restorePreferredTerm(@PathVariable String serverName,
                                                                           @PathVariable String userId,
                                                                           @PathVariable String guid) {
        return restAPI.restorePreferredTerm(serverName, userId, guid);
    }


    /**
     * Create a validValue relationship, which is a link between glossary terms that have the same meaning.
     *
     * <p>
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param validValue the ValidValue relationship
     * @return response, when successful contains the created validValue relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/valid-values")
    public SubjectAreaOMASAPIResponse2<ValidValue> createValidValue(@PathVariable String serverName,
                                                                    @PathVariable String userId,
                                                                    @RequestBody ValidValue validValue) {
        return restAPI.createValidValue(serverName, userId, validValue);
    }

    /**
     * Get a validValue relationship, which is a link between glossary terms that have the same meaning.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the validValue relationship to get
     * @return response which when successful contains the validValue relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/valid-values/{guid}")
    public SubjectAreaOMASAPIResponse2<ValidValue> getValidValueRelationship(@PathVariable String serverName,
                                                                             @PathVariable String userId,
                                                                             @PathVariable String guid) {
        return restAPI.getValidValueRelationship(serverName, userId, guid);
    }

    /**
     * Update a ValidValue relationship which is a link between glossary terms that have the same meaning
     * <p>
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param guid       unique identifier of the Line
     * @param validValue the valid value relationship
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created ValidValueRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/valid-values/{guid}")
    public SubjectAreaOMASAPIResponse2<ValidValue> updateValidValueRelationship(@PathVariable String serverName,
                                                                                @PathVariable String userId,
                                                                                @PathVariable String guid,
                                                                                @RequestBody ValidValue validValue,
                                                                                @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace
    ) {
        return restAPI.updateValidValueRelationship(serverName, userId, guid, validValue, isReplace);
    }


    /**
     * Delete a ValidValue relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ValidValue relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/valid-values/{guid}")
    public SubjectAreaOMASAPIResponse2<ValidValue> deleteValidValueRelationship(@PathVariable String serverName,
                                                                                @PathVariable String userId,
                                                                                @PathVariable String guid,
                                                                                @RequestParam(value = "isPurge", required = false, defaultValue = "false") Boolean isPurge
    ) {
        return restAPI.deleteValidValueRelationship(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a ValidValue
     * <p>
     * Restore allows the deleted ValidValue to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ValidValue to delete
     * @return response which when successful contains the restored ValidValue
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/valid-values/{guid}")
    public SubjectAreaOMASAPIResponse2<ValidValue> restoreValidValue(@PathVariable String serverName,
                                                                     @PathVariable String userId,
                                                                     @PathVariable String guid) {
        return restAPI.restoreValidValue(serverName, userId, guid);
    }

    /**
     * Create a replacementTerm relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     *
     * <p>
     *
     * @param serverName      serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId          userId under which the request is performed
     * @param replacementTerm the ReplacementTerm relationship
     * @return response, when successful contains the created replacementTerm relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/replacement-terms")
    public SubjectAreaOMASAPIResponse2<ReplacementTerm> createReplacementTerm(@PathVariable String serverName,
                                                                              @PathVariable String userId,
                                                                              @RequestBody ReplacementTerm replacementTerm) {
        return restAPI.createReplacementTerm(serverName, userId, replacementTerm);
    }

    /**
     * Get a replacementTerm relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the replacementTerm relationship to get
     * @return response which when successful contains the replacementTerm relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/replacement-terms/{guid}")
    public SubjectAreaOMASAPIResponse2<ReplacementTerm> getReplacementTermRelationship(@PathVariable String serverName,
                                                                                       @PathVariable String userId,
                                                                                       @PathVariable String guid) {
        return restAPI.getReplacementTerm(serverName, userId, guid);
    }

    /**
     * Update a ReplacementTerm relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     * @param serverName      serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId          userId under which the request is performed
     * @param guid            unique identifier of the Line
     * @param replacementTerm the replacement term relationship
     * @param isReplace       flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created ReplacementTermRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/replacement-terms/{guid}")
    public SubjectAreaOMASAPIResponse2<ReplacementTerm> updateReplacementTermRelationship(@PathVariable String serverName,
                                                                                          @PathVariable String userId,
                                                                                          @PathVariable String guid,
                                                                                          @RequestBody ReplacementTerm replacementTerm,
                                                                                          @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace
    ) {
        return restAPI.updateReplacementTerm(serverName, userId, guid, replacementTerm, isReplace);
    }

    /**
     * Delete a ReplacementTerm relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ReplacementTerm relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/replacement-terms/{guid}")
    public SubjectAreaOMASAPIResponse2<ReplacementTerm> deleteReplacementTermRelationship(@PathVariable String serverName,
                                                                                          @PathVariable String userId,
                                                                                          @PathVariable String guid,
                                                                                          @RequestParam(value = "isPurge", required = false, defaultValue = "false") Boolean isPurge
    ) {
        return restAPI.deleteReplacementTerm(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a ReplacementTerm
     * <p>
     * Restore allows the deleted ReplacementTerm to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ReplacementTerm to delete
     * @return response which when successful contains the restored ReplacementTerm
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/replacement-terms/{guid}")
    public SubjectAreaOMASAPIResponse2<ReplacementTerm> restoreReplacementTerm(@PathVariable String serverName,
                                                                               @PathVariable String userId,
                                                                               @PathVariable String guid) {
        return restAPI.restoreReplacementTerm(serverName, userId, guid);
    }

    /**
     * Create a termTYPEDBYRelationship relationship, which is a link between a spine attribute and its type.
     *
     * <p>
     *
     * @param serverName              serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                  userId under which the request is performed
     * @param termTYPEDBYRelationship the TermTYPEDBYRelationship relationship
     * @return response, when successful contains the created termTYPEDBYRelationship relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/typed-bys")
    public SubjectAreaOMASAPIResponse2<TypedBy> createtermTYPEDBYRelationship(@PathVariable String serverName,
                                                                              @PathVariable String userId,
                                                                              @RequestBody TypedBy termTYPEDBYRelationship) {
        return restAPI.createTermTYPEDBYRelationship(serverName, userId, termTYPEDBYRelationship);
    }

    /**
     * Get a termTYPEDBYRelationship relationship, which is a link between a spine attribute and its type.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the termTYPEDBYRelationship relationship to get
     * @return response which when successful contains the termTYPEDBYRelationship relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/typed-bys/{guid}")
    public SubjectAreaOMASAPIResponse2<TypedBy> getTYPEDBYRelationshipRelationship(@PathVariable String serverName,
                                                                                   @PathVariable String userId,
                                                                                   @PathVariable String guid) {
        return restAPI.getTermTYPEDBYRelationship(serverName, userId, guid);
    }

    /**
     * Update a TermTYPEDBYRelationship relationship, which is a link between a spine attribute and its type.
     * <p>
     *
     * @param serverName              serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                  userId under which the request is performed
     * @param guid                    unique identifier of the Line
     * @param termTYPEDBYRelationship the typed by relationship
     * @param isReplace               flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created TermTYPEDBYRelationshipRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/typed-bys/{guid}")
    public SubjectAreaOMASAPIResponse2<TypedBy> updateTermTYPEDBYRelationship(@PathVariable String serverName,
                                                                              @PathVariable String userId,
                                                                              @PathVariable String guid,
                                                                              @RequestBody TypedBy termTYPEDBYRelationship,
                                                                              @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace
    ) {
        return restAPI.updateTermTYPEDBYRelationship(serverName, userId, guid, termTYPEDBYRelationship, isReplace);
    }

    /**
     * Delete a TermTYPEDBYRelationship relationship, which is a link between a spine attribute and its type.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the TermTYPEDBYRelationship relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/typed-bys/{guid}")
    public SubjectAreaOMASAPIResponse2<TypedBy> deleteTypedByRelationship(@PathVariable String serverName,
                                                                          @PathVariable String userId,
                                                                          @PathVariable String guid,
                                                                          @RequestParam(value = "isPurge", required = false, defaultValue = "false") Boolean isPurge
    ) {
        return restAPI.deleteTermTYPEDBYRelationship(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a Term TYPED BY relationship
     * <p>
     * Restore allows the deleted TermTYPEDBY to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the TermTYPEDBY to delete
     * @return response which when successful contains the restored TermTYPEDBY
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/typed-bys/{guid}")
    public SubjectAreaOMASAPIResponse2<TypedBy> restoreTermTYPEDBYRelationship(@PathVariable String serverName,
                                                                               @PathVariable String userId,
                                                                               @PathVariable String guid) {
        return restAPI.restoreTermTYPEDBYRelationship(serverName, userId, guid);
    }

    /**
     * Create a iSARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     *
     * <p>
     *
     * @param serverName      serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId          userId under which the request is performed
     * @param iSARelationship the ISARelationship relationship
     * @return response, when successful contains the created iSARelationship relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/is-as")
    public SubjectAreaOMASAPIResponse2<Isa> createiSARelationship(@PathVariable String serverName,
                                                                  @PathVariable String userId,
                                                                  @RequestBody Isa iSARelationship) {
        return restAPI.createISARelationship(serverName, userId, iSARelationship);
    }

    /**
     * Get a iSARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the iSARelationship relationship to get
     * @return response which when successful contains the iSARelationship relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/is-as/{guid}")
    public SubjectAreaOMASAPIResponse2<Isa> getISARelationship(@PathVariable String serverName,
                                                               @PathVariable String userId,
                                                               @PathVariable String guid) {
        return restAPI.getISARelationship(serverName, userId, guid);
    }

    /**
     * Update a ISARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param guid       unique identifier of the Line
     * @param isa        the is-a relationship
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created ISARelationshipRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/is-as/{guid}")
    public SubjectAreaOMASAPIResponse2<Isa> updateISARelationship(@PathVariable String serverName,
                                                                  @PathVariable String userId,
                                                                  @PathVariable String guid,
                                                                  @RequestBody Isa isa,
                                                                  @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace) {
        return restAPI.updateISARelationship(serverName, userId, guid, isa, isReplace);
    }

    /**
     * Delete a ISARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ISARelationship relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/is-as/{guid}")
    public SubjectAreaOMASAPIResponse2<Isa> deleteTermISARelationship(@PathVariable String serverName,
                                                                      @PathVariable String userId,
                                                                      @PathVariable String guid,
                                                                      @RequestParam(value = "isPurge", required = false, defaultValue = "false") Boolean isPurge) {
        return restAPI.deleteISARelationship(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a Isa Relationship
     * <p>
     * Restore allows the deleted Isa Relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Isa Relationship to delete
     * @return response which when successful contains the restored IsaRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/is-as/{guid}")
    public SubjectAreaOMASAPIResponse2<Isa> restoreIsaRelationship(@PathVariable String serverName,
                                                                   @PathVariable String userId,
                                                                   @PathVariable String guid) {
        return restAPI.restoreISARelationship(serverName, userId, guid);
    }


    /**
     * Create a termISATypeOFRelationship relationship, which is an inheritance relationship between two spine objects.
     *
     * <p>
     *
     * @param serverName                serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                    userId under which the request is performed
     * @param termISATypeOFRelationship the TermISATypeOFRelationship relationship
     * @return response, when successful contains the created termISATypeOFRelationship relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/is-a-type-ofs")
    public SubjectAreaOMASAPIResponse2<IsaTypeOf> createtermISATypeOFRelationship(@PathVariable String serverName,
                                                                                  @PathVariable String userId,
                                                                                  @RequestBody IsaTypeOf termISATypeOFRelationship) {
        return restAPI.createTermISATypeOFRelationship(serverName, userId, termISATypeOFRelationship);
    }

    /**
     * Get a termISATypeOFRelationship relationship, which is an inheritance relationship between two spine objects.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the termISATypeOFRelationship relationship to get
     * @return response which when successful contains the termISATypeOFRelationship relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/is-a-type-ofs/{guid}")
    public SubjectAreaOMASAPIResponse2<IsaTypeOf> getTermISATypeOFRelationship(@PathVariable String serverName,
                                                                               @PathVariable String userId,
                                                                               @PathVariable String guid) {
        return restAPI.getTermISATypeOFRelationship(serverName, userId, guid);
    }

    /**
     * Update a ISARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param guid       unique identifier of the Line
     * @param isatypeof  the is-a-type-of relationship
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created ISARelationshipRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/is-a-type-ofs/{guid}")
    public SubjectAreaOMASAPIResponse2<IsaTypeOf> updateISARelationship(@PathVariable String serverName,
                                                                        @PathVariable String userId,
                                                                        @PathVariable String guid,
                                                                        @RequestBody IsaTypeOf isatypeof,
                                                                        @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace) {
        return restAPI.updateTermISATypeOFRelationship(serverName, userId, guid, isatypeof, isReplace);
    }


    /**
     * Delete a TermISATypeOFRelationship relationship, which is an inheritance relationship between two spine objects.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the TermISATypeOFRelationship relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/is-a-type-ofs/{guid}")
    public SubjectAreaOMASAPIResponse2<IsaTypeOf> deleteTermIsaTypeOfRelationship(@PathVariable String serverName,
                                                                                  @PathVariable String userId,
                                                                                  @PathVariable String guid,
                                                                                  @RequestParam(value = "isPurge", required = false, defaultValue = "false") Boolean isPurge) {
        return restAPI.deleteIsATypeOfRelationship(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a Is a Type Of Relationship
     * <p>
     * Restore allows the deleted Is a Type Of Relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Term Is a Type Of Relationship to delete
     * @return response which when successful contains the restored TermIsaTypeOfRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */


    @PostMapping(path = "/users/{userId}/relationships/is-a-type-ofs/{guid}")
    public SubjectAreaOMASAPIResponse2<IsaTypeOf> restoreTermIsaTypeOfRelationship(@PathVariable String serverName,
                                                                                   @PathVariable String userId,
                                                                                   @PathVariable String guid) {
        return restAPI.restoreTermISATypeOFRelationship(serverName, userId, guid);
    }


    /**
     * Create a termCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * <p>
     *
     * @param serverName                     serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                         userId under which the request is performed
     * @param termCategorizationRelationship the TermCategorizationRelationship relationship
     * @return response, when successful contains the created termCategorizationRelationship relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/term-categorizations")
    public SubjectAreaOMASAPIResponse2<Categorization> createTermCategorization(@PathVariable String serverName,
                                                                                @PathVariable String userId,
                                                                                @RequestBody Categorization termCategorizationRelationship) {
        return restAPI.createTermCategorizationRelationship(serverName, userId, termCategorizationRelationship);
    }

    /**
     * Get a termCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the termCategorizationRelationship relationship to get
     * @return response which when successful contains the termCategorizationRelationship relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/term-categorizations/{guid}")
    public SubjectAreaOMASAPIResponse2<Categorization> getTermCategorizationRelationship(@PathVariable String serverName,
                                                                                         @PathVariable String userId,
                                                                                         @PathVariable String guid) {
        return restAPI.getTermCategorizationRelationship(serverName, userId, guid);
    }

    /**
     * Update a termCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * <p>
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param guid       unique identifier of the Line
     * @param isatypeof  the is-a-type-of relationship
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created termCategorization Relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/term-categorizations/{guid}")
    public SubjectAreaOMASAPIResponse2<Categorization> updateTermCategorizationRelationship(@PathVariable String serverName,
                                                                                            @PathVariable String userId,
                                                                                            @PathVariable String guid,
                                                                                            @RequestBody Categorization isatypeof,
                                                                                            @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace) {
        return restAPI.updateTermCategorizationRelationship(serverName, userId, guid, isatypeof, isReplace);
    }


    /**
     * Delete a TermCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the TermCategorizationRelationship relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/term-categorizations/{guid}")
    public SubjectAreaOMASAPIResponse2<Categorization> deleteTermCategorizationRelationship(@PathVariable String serverName,
                                                                                            @PathVariable String userId,
                                                                                            @PathVariable String guid,
                                                                                            @RequestParam(value = "isPurge", required = false, defaultValue = "false") Boolean isPurge) {
        return restAPI.deleteTermCategorizationRelationship(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a TermCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * <p>
     * Restore allows the deleted TermCategorization Relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Term Is a Type Of Relationship to delete
     * @return response which when successful contains the restored TermCategorization
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/term-categorizations/{guid}")
    public SubjectAreaOMASAPIResponse2<Categorization> restoreTermCategorizationRelationship(@PathVariable String serverName,
                                                                                             @PathVariable String userId,
                                                                                             @PathVariable String guid) {
        return restAPI.restoreTermCategorizationRelationship(serverName, userId, guid);
    }

    /**
     * Create a termAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     * Terms created using the Subject Area OMAS cannot be created without a glossary and there can only be one glossary associated with a
     * Term. This method is to allow glossaries to be associated with Terms that have not been created via the Subject Area OMAS or to recreate
     * the TermAnchor relationship if it has been purged.
     * <p>
     *
     * @param serverName             serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                 userId under which the request is performed
     * @param termAnchorRelationship the TermAnchorRelationship relationship
     * @return response, when successful contains the created termAnchorRelationship relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/term-anchor")
    public SubjectAreaOMASAPIResponse2<TermAnchor> createTermAnchor(@PathVariable String serverName,
                                                                    @PathVariable String userId,
                                                                    @RequestBody TermAnchor termAnchorRelationship) {
        return restAPI.createTermAnchorRelationship(serverName, userId, termAnchorRelationship);
    }

    /**
     * Get a termAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the termAnchor Relationship to get
     * @return response which when successful contains the termAnchorRelationship relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/term-anchor/{guid}")
    public SubjectAreaOMASAPIResponse2<TermAnchor> getTermAnchorRelationship(@PathVariable String serverName,
                                                                             @PathVariable String userId,
                                                                             @PathVariable String guid) {
        return restAPI.getTermAnchorRelationship(serverName, userId, guid);

    }


    /**
     * Delete a TermAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the TermAnchorRelationship relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/term-anchor/{guid}")
    public SubjectAreaOMASAPIResponse2<TermAnchor> deleteTermAnchorRelationship(@PathVariable String serverName,
                                                                                @PathVariable String userId,
                                                                                @PathVariable String guid,
                                                                                @RequestParam(value = "isPurge", required = false, defaultValue = "false") Boolean isPurge) {
        return restAPI.deleteTermAnchorRelationship(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a TermAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     * <p>
     * Restore allows the deleted TermAnchor Relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Term Anchor Relationship to delete
     * @return response which when successful contains the restored TermAnchor
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/term-anchor/{guid}")
    public SubjectAreaOMASAPIResponse2<TermAnchor> restoreTermAnchorRelationship(@PathVariable String serverName,
                                                                                 @PathVariable String userId,
                                                                                 @PathVariable String guid) {
        return restAPI.restoreTermAnchorRelationship(serverName, userId, guid);
    }

    /**
     * Create a categoryAnchor Relationship. A relationship between a Glossary and a Category. This relationship allows terms to be owned by a Glossary.
     * Categories created using the Subject Area OMAS cannot be created without a glossary and there can only be one glossary associated with a
     * Category. This method is to allow glossaries to be associated with Categories that have not been created via the Subject Area OMAS or to recreate
     * the CategoryAnchor relationship if it has been purged.
     * <p>
     *
     * @param serverName                 serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                     userId under which the request is performed
     * @param categoryAnchorRelationship the CategoryAnchorRelationship relationship
     * @return response, when successful contains the created categoryAnchorRelationship relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/category-anchor")
    public SubjectAreaOMASAPIResponse2<CategoryAnchor> createCategoryAnchor(@PathVariable String serverName,
                                                                            @PathVariable String userId,
                                                                            @RequestBody CategoryAnchor categoryAnchorRelationship) {
        return restAPI.createCategoryAnchorRelationship(serverName, userId, categoryAnchorRelationship);
    }

    /**
     * Get a categoryAnchor Relationship. A relationship between a Glossary and a Category. This relationship allows terms to be owned by a Glossary.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the categoryAnchor Relationship to get
     * @return response which when successful contains the categoryAnchorRelationship relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/category-anchor/{guid}")
    public SubjectAreaOMASAPIResponse2<CategoryAnchor> getCategoryAnchorRelationship(@PathVariable String serverName,
                                                                                     @PathVariable String userId,
                                                                                     @PathVariable String guid) {
        return restAPI.getCategoryAnchorRelationship(serverName, userId, guid);
    }

    /**
     * Delete a CategoryAnchor Relationship. A relationship between a Glossary and a Category. This relationship allows terms to be owned by a Glossary.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the CategoryAnchorRelationship relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/category-anchor/{guid}")
    public SubjectAreaOMASAPIResponse2<CategoryAnchor> deleteCategoryAnchorRelationship(@PathVariable String serverName,
                                                                                        @PathVariable String userId,
                                                                                        @PathVariable String guid,
                                                                                        @RequestParam(value = "isPurge", required = false, defaultValue = "false") Boolean isPurge
    ) {
        return restAPI.deleteCategoryAnchorRelationship(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a CategoryAnchor Relationship. A relationship between a Glossary and a Category. This relationship allows terms to be owned by a Glossary.
     * <p>
     * Restore allows the deleted CategoryAnchor Relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Category Anchor Relationship to delete
     * @return response which when successful contains the restored CategoryAnchor
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/category-anchor/{guid}")
    public SubjectAreaOMASAPIResponse2<CategoryAnchor> restoreCategoryAnchorRelationship(@PathVariable String serverName,
                                                                                         @PathVariable String userId,
                                                                                         @PathVariable String guid) {
        return restAPI.restoreCategoryAnchorRelationship(serverName, userId, guid);
    }

    /**
     * Create a project scope relationship, which is a link between the project content and the project.
     *
     * <p>
     *
     * @param serverName   serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId       userId under which the request is performed
     * @param projectScope the Project scope relationship
     * @return response, when successful contains the created project scope relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/project-scopes")
    public SubjectAreaOMASAPIResponse2<ProjectScope> createProjectScopeRelationship(@PathVariable String serverName,
                                                                                    @PathVariable String userId,
                                                                                    @RequestBody ProjectScope projectScope) {
        return restAPI.createProjectScopeRelationship(serverName, userId, projectScope);
    }

    /**
     * Get a project scope relationship, which is a link between the project content and the project.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the project scope relationship to get
     * @return response which when successful contains the project scope relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/project-scopes/{guid}")
    public SubjectAreaOMASAPIResponse2<ProjectScope> getProjectScopeRelationship(@PathVariable String serverName,
                                                                                 @PathVariable String userId,
                                                                                 @PathVariable String guid) {
        return restAPI.getProjectScopeRelationship(serverName, userId, guid);
    }

    /**
     * Update a Project scope relationship which is a link between the project content and the project.
     * <p>
     *
     * @param serverName   serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId       userId under which the request is performed
     * @param guid         unique identifier of the Line
     * @param projectScope the projectScope relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created ProjectScopeRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/project-scopes/{guid}")
    public SubjectAreaOMASAPIResponse2<ProjectScope> updateProjectScopeRelationship(@PathVariable String serverName,
                                                                                    @PathVariable String userId,
                                                                                    @PathVariable String guid,
                                                                                    @RequestBody ProjectScope projectScope,
                                                                                    @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace) {
        return restAPI.updateProjectScopeRelationship(serverName, userId, guid, projectScope, isReplace);
    }

    /**
     * Delete a Project scope relationship which is a link between the project content and the project.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Project scope relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/project-scopes/{guid}")
    public SubjectAreaOMASAPIResponse2<ProjectScope> deleteProjectScopeRelationship(@PathVariable String serverName,
                                                                                    @PathVariable String userId,
                                                                                    @PathVariable String guid,
                                                                                    @RequestParam(value = "isPurge", required = false, defaultValue = "false") Boolean isPurge) {
        return restAPI.deleteProjectScopeRelationship(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a ProjectScope, which is a link between the project content and the project.
     * <p>
     * Restore allows the deleted ProjectScopeRelationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the ProjectScopeRelationship to delete
     * @return response which when successful contains the restored ProjectScopeRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/project-scopes/{guid}")
    public SubjectAreaOMASAPIResponse2<ProjectScope> restoreProjectScoperRelationship(@PathVariable String serverName,
                                                                                      @PathVariable String userId,
                                                                                      @PathVariable String guid) {
        return restAPI.restoreProjectScopeRelationship(serverName, userId, guid);
    }

    /**
     * Get a SemanticAssignment relationship,  Links a glossary term to another element such as an asset or schema element to define its meaning.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the SemanticAssignment relationship to get
     * @return response which when successful contains the SemanticAssignment relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/semantic-assignments/{guid}")
    public SubjectAreaOMASAPIResponse2<SemanticAssignment> getSemanticAssignmentRelationship(@PathVariable String serverName,
                                                                                             @PathVariable String userId,
                                                                                             @PathVariable String guid) {
        return restAPI.getSemanticAssignmentRelationship(serverName, userId, guid);
    }
}

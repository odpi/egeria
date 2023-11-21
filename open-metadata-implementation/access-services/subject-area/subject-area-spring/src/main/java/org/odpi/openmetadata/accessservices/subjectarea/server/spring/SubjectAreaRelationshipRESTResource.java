/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaRelationshipRESTServices;
import org.springframework.web.bind.annotation.*;



/**
 * The SubjectAreaRESTServicesInstance provides the server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides relationship authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/subject-area")
@Tag(name = "Metadata Access Server: Subject Area OMAS", description = "The Subject Area OMAS supports subject matter experts who are documenting their knowledge about a particular subject. This includes glossary terms, reference data, validation rules.",
     externalDocs = @ExternalDocumentation(description = "Further Information",
                                           url = "https://egeria-project.org/services/omas/subject-area/overview/"))
public class SubjectAreaRelationshipRESTResource {
    private final SubjectAreaRelationshipRESTServices restAPI = new SubjectAreaRelationshipRESTServices();

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
     * @param termHasARelationship the HASA relationship
     * @return response, when successful contains the created TermHASARelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/has-as")
    public SubjectAreaOMASAPIResponse<HasA> createTermHasARelationship(@PathVariable String serverName,
                                                                       @PathVariable String userId,

                                                                       @RequestBody HasA termHasARelationship) {
        return restAPI.createTermHasARelationship(serverName, userId, termHasARelationship);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/has-as/{guid}")
    public SubjectAreaOMASAPIResponse<HasA> getTermHasARelationship(@PathVariable String serverName,
                                                                    @PathVariable String userId,
                                                                    @PathVariable String guid) {
        return restAPI.getTermHasARelationship(serverName, userId, guid);
    }

    /**
     * Update a TermHasARelationship is the relationship between a spine object and a spine attribute.
     * <p>
     *
     * @param serverName           serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param guid                 unique identifier of the relationship
     * @param termHasARelationship the HASA relationship
     * @param isReplace            flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created TermHasARelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/has-as/{guid}")
    public SubjectAreaOMASAPIResponse<HasA> updateTermHasARelationship(@PathVariable String serverName,
                                                                       @PathVariable String userId,
                                                                       @PathVariable String guid,
                                                                       @RequestBody HasA termHasARelationship,
                                                                       @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace
    ) {
        return restAPI.updateTermHasARelationship(serverName, userId, guid, termHasARelationship, isReplace);
    }

    /**
     * Delete a Term HAS A relationship
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the HAS A relationship to delete
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/has-as/{guid}")
    public SubjectAreaOMASAPIResponse<HasA> deleteTermHasARelationship(@PathVariable String serverName,
                                                                       @PathVariable String userId,
                                                                       @PathVariable String guid
                                                                      ) {
        return restAPI.deleteTermHasARelationship(serverName, userId, guid);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/has-as/{guid}")
    public SubjectAreaOMASAPIResponse<HasA> restoreTermHasARelationship(@PathVariable String serverName,
                                                                        @PathVariable String userId,
                                                                        @PathVariable String guid) {
        return restAPI.restoreTermHasARelationship(serverName, userId, guid);
    }

    /**
     * Create a RelatedTerm. A Related Term is a link between two similar Terms.
     *
     * @param serverName                          serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                              userId under which the request is performed
     * @param relatedTermRelationship the RelatedTerm relationship
     * @return response, when successful contains the created Related Term relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/related-terms")
    public SubjectAreaOMASAPIResponse<RelatedTerm> createRelatedTerm(@PathVariable String serverName,
                                                                     @PathVariable String userId,
                                                                     @RequestBody RelatedTerm relatedTermRelationship) {
        return restAPI.createRelatedTerm(serverName, userId, relatedTermRelationship);
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/related-terms/{guid}")
    public SubjectAreaOMASAPIResponse<RelatedTerm> getRelatedTerm(@PathVariable String serverName,
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
     * @param guid                    unique identifier of the relationship
     * @param relatedTermRelationship the related term  relationship
     * @param isReplace               flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created RelatedTerm
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/related-terms/{guid}")
    public SubjectAreaOMASAPIResponse<RelatedTerm> updateRelatedTerm(@PathVariable String serverName,
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
     *
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/related-terms/{guid}")
    public SubjectAreaOMASAPIResponse<RelatedTerm> deleteRelatedTerm(@PathVariable String serverName,
                                                                     @PathVariable String userId,
                                                                     @PathVariable String guid
                                                                    
    ) {
        return restAPI.deleteRelatedTerm(serverName, userId, guid);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/related-terms/{guid}")
    public SubjectAreaOMASAPIResponse<RelatedTerm> restoreRelatedTerm(@PathVariable String serverName,
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/synonyms")
    public SubjectAreaOMASAPIResponse<Synonym> createSynonym(@PathVariable String serverName,
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/synonyms/{guid}")
    public SubjectAreaOMASAPIResponse<Synonym> getSynonymRelationship(@PathVariable String serverName,
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
     * @param guid       unique identifier of the relationship
     * @param synonym    the synonym  relationship
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created SynonymRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/synonyms/{guid}")
    public SubjectAreaOMASAPIResponse<Synonym> updateSynonymRelationship(@PathVariable String serverName,
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
     *
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/synonyms/{guid}")
    public SubjectAreaOMASAPIResponse<Synonym> deleteSynonymRelationship(@PathVariable String serverName,
                                                                         @PathVariable String userId,
                                                                         @PathVariable String guid
                                                                        
    ) {
        return restAPI.deleteSynonymRelationship(serverName, userId, guid);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/synonyms/{guid}")
    public SubjectAreaOMASAPIResponse<Synonym> restoreSynonym(@PathVariable String serverName,
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/antonyms")
    public SubjectAreaOMASAPIResponse<Antonym> createAntonym(@PathVariable String serverName,
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/antonyms/{guid}")
    public SubjectAreaOMASAPIResponse<Antonym> getAntonymRelationship(@PathVariable String serverName,
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
     * @param guid       unique identifier of the relationship
     * @param antonym    the antonym relationship
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created AntonymRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/antonyms/{guid}")
    public SubjectAreaOMASAPIResponse<Antonym> updateAntonymRelationship(@PathVariable String serverName,
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
     *
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/antonyms/{guid}")
    public SubjectAreaOMASAPIResponse<Antonym> deleteAntonymRelationship(@PathVariable String serverName,
                                                                         @PathVariable String userId,
                                                                         @PathVariable String guid
                                                                        
    ) {
        return restAPI.deleteAntonymRelationship(serverName, userId, guid);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/antonyms/{guid}")
    public SubjectAreaOMASAPIResponse<Antonym> restoreAntonym(@PathVariable String serverName,
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/translations")
    public SubjectAreaOMASAPIResponse<Translation> createTranslation(@PathVariable String serverName,
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/translations/{guid}")
    public SubjectAreaOMASAPIResponse<Translation> getTranslationRelationship(@PathVariable String serverName,
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
     * @param guid        unique identifier of the relationship
     * @param translation the translation relationship
     * @param isReplace   flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created TranslationRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/translations/{guid}")
    public SubjectAreaOMASAPIResponse<Translation> updateTranslationRelationship(@PathVariable String serverName,
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
     *
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/translations/{guid}")
    public SubjectAreaOMASAPIResponse<Translation> deleteTranslationRelationship(@PathVariable String serverName,
                                                                                 @PathVariable String userId,
                                                                                 @PathVariable String guid
                                                                                
    ) {
        return restAPI.deleteTranslationRelationship(serverName, userId, guid);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/translations/{guid}")
    public SubjectAreaOMASAPIResponse<Translation> restoreTranslation(@PathVariable String serverName,
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/used-in-contexts")
    public SubjectAreaOMASAPIResponse<UsedInContext> createusedInContext(@PathVariable String serverName,
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/used-in-contexts/{guid}")
    public SubjectAreaOMASAPIResponse<UsedInContext> getUsedInContextRelationship(@PathVariable String serverName,
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
     * @param guid          unique identifier of the relationship
     * @param usedInContext the used in context relationship
     * @param isReplace     flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created UsedInContextRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/used-in-contexts/{guid}")
    public SubjectAreaOMASAPIResponse<UsedInContext> updateUsedInContextRelationship(@PathVariable String serverName,
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
     *
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/used-in-contexts/{guid}")
    public SubjectAreaOMASAPIResponse<UsedInContext> deleteUsedInContextRelationship(@PathVariable String serverName,
                                                                                     @PathVariable String userId,
                                                                                     @PathVariable String guid
                                                                                    
    ) {
        return restAPI.deleteUsedInContextRelationship(serverName, userId, guid);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/used-in-contexts/{guid}")
    public SubjectAreaOMASAPIResponse<UsedInContext> restoreUsedInContext(@PathVariable String serverName,
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/preferred-terms")
    public SubjectAreaOMASAPIResponse<PreferredTerm> createPreferredTerm(@PathVariable String serverName,
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/preferred-terms/{guid}")
    public SubjectAreaOMASAPIResponse<PreferredTerm> getPreferredTermRelationship(@PathVariable String serverName,
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
     * @param guid          unique identifier of the relationship
     * @param preferredTerm the preferred term relationship
     * @param isReplace     flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created PreferredTermRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/preferred-terms/{guid}")
    public SubjectAreaOMASAPIResponse<PreferredTerm> updatePreferredTermRelationship(@PathVariable String serverName,
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
     *
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/preferred-terms/{guid}")
    public SubjectAreaOMASAPIResponse<PreferredTerm> deletePreferredTermRelationship(@PathVariable String serverName,
                                                                                     @PathVariable String userId,
                                                                                     @PathVariable String guid
                                                                                    
    ) {
        return restAPI.deletePreferredTermRelationship(serverName, userId, guid);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/preferred-terms/{guid}")
    public SubjectAreaOMASAPIResponse<PreferredTerm> restorePreferredTerm(@PathVariable String serverName,
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/valid-values")
    public SubjectAreaOMASAPIResponse<ValidValue> createValidValue(@PathVariable String serverName,
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/valid-values/{guid}")
    public SubjectAreaOMASAPIResponse<ValidValue> getValidValueRelationship(@PathVariable String serverName,
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
     * @param guid       unique identifier of the relationship
     * @param validValue the valid value relationship
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created ValidValueRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/valid-values/{guid}")
    public SubjectAreaOMASAPIResponse<ValidValue> updateValidValueRelationship(@PathVariable String serverName,
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
     *
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/valid-values/{guid}")
    public SubjectAreaOMASAPIResponse<ValidValue> deleteValidValueRelationship(@PathVariable String serverName,
                                                                               @PathVariable String userId,
                                                                               @PathVariable String guid
                                                                              
    ) {
        return restAPI.deleteValidValueRelationship(serverName, userId, guid);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/valid-values/{guid}")
    public SubjectAreaOMASAPIResponse<ValidValue> restoreValidValue(@PathVariable String serverName,
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/replacement-terms")
    public SubjectAreaOMASAPIResponse<ReplacementTerm> createReplacementTerm(@PathVariable String serverName,
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/replacement-terms/{guid}")
    public SubjectAreaOMASAPIResponse<ReplacementTerm> getReplacementTermRelationship(@PathVariable String serverName,
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
     * @param guid            unique identifier of the relationship
     * @param replacementTerm the replacement term relationship
     * @param isReplace       flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created ReplacementTermRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/replacement-terms/{guid}")
    public SubjectAreaOMASAPIResponse<ReplacementTerm> updateReplacementTermRelationship(@PathVariable String serverName,
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
     *
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/replacement-terms/{guid}")
    public SubjectAreaOMASAPIResponse<ReplacementTerm> deleteReplacementTermRelationship(@PathVariable String serverName,
                                                                                         @PathVariable String userId,
                                                                                         @PathVariable String guid
                                                                                        
    ) {
        return restAPI.deleteReplacementTerm(serverName, userId, guid);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/replacement-terms/{guid}")
    public SubjectAreaOMASAPIResponse<ReplacementTerm> restoreReplacementTerm(@PathVariable String serverName,
                                                                              @PathVariable String userId,
                                                                              @PathVariable String guid) {
        return restAPI.restoreReplacementTerm(serverName, userId, guid);
    }

    /**
     * Create a termTypedByRelationship, which is a link between a spine attribute and its type.
     *
     * <p>
     *
     * @param serverName              serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                  userId under which the request is performed
     * @param termTypedByRelationship the TermTypedByRelationship
     * @return response, when successful contains the created termTypedByRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/typed-bys")
    public SubjectAreaOMASAPIResponse<TypedBy> createtermTypedByRelationship(@PathVariable String serverName,
                                                                             @PathVariable String userId,
                                                                             @RequestBody TypedBy termTypedByRelationship) {
        return restAPI.createTermTypedByRelationship(serverName, userId, termTypedByRelationship);
    }

    /**
     * Get a termTypedByRelationship, which is a link between a spine attribute and its type.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the termTypedByRelationship to get
     * @return response which when successful contains the termTypedByRelationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/typed-bys/{guid}")
    public SubjectAreaOMASAPIResponse<TypedBy> getTypedByRelationship(@PathVariable String serverName,
                                                                                  @PathVariable String userId,
                                                                                  @PathVariable String guid) {
        return restAPI.getTermTypedByRelationship(serverName, userId, guid);
    }

    /**
     * Update a TermTypedByRelationship, which is a link between a spine attribute and its type.
     * <p>
     *
     * @param serverName              serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                  userId under which the request is performed
     * @param guid                    unique identifier of the relationship
     * @param termTypedByRelationship the typed by relationship
     * @param isReplace               flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created TermTypedByRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/typed-bys/{guid}")
    public SubjectAreaOMASAPIResponse<TypedBy> updateTermTypedByRelationship(@PathVariable String serverName,
                                                                             @PathVariable String userId,
                                                                             @PathVariable String guid,
                                                                             @RequestBody TypedBy termTypedByRelationship,
                                                                             @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace
    ) {
        return restAPI.updateTermTypedByRelationship(serverName, userId, guid, termTypedByRelationship, isReplace);
    }

    /**
     * Delete a TermTypedByRelationship, which is a link between a spine attribute and its type.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the TermTypedByRelationship to delete
     *
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/typed-bys/{guid}")
    public SubjectAreaOMASAPIResponse<TypedBy> deleteTypedByRelationship(@PathVariable String serverName,
                                                                         @PathVariable String userId,
                                                                         @PathVariable String guid
                                                                        
    ) {
        return restAPI.deleteTermTypedByRelationship(serverName, userId, guid);
    }

    /**
     * Restore a Term TYPED BY relationship
     * <p>
     * Restore allows the deleted TermTypedBy to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the TermTypedBy to delete
     * @return response which when successful contains the restored TermTypedBy
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/typed-bys/{guid}")
    public SubjectAreaOMASAPIResponse<TypedBy> restoreTermTypedByRelationship(@PathVariable String serverName,
                                                                              @PathVariable String userId,
                                                                              @PathVariable String guid) {
        return restAPI.restoreTermTypedByRelationship(serverName, userId, guid);
    }

    /**
     * Create a iSARelationship, which is a link between a more general glossary term and a more specific definition.
     *
     * <p>
     *
     * @param serverName      serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId          userId under which the request is performed
     * @param iSARelationship the IsARelationship
     * @return response, when successful contains the created iSARelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/is-as")
    public SubjectAreaOMASAPIResponse<IsA> createiSARelationship(@PathVariable String serverName,
                                                                 @PathVariable String userId,
                                                                 @RequestBody IsA iSARelationship) {
        return restAPI.createIsARelationship(serverName, userId, iSARelationship);
    }

    /**
     * Get a iSARelationship, which is a link between a more general glossary term and a more specific definition.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the iSARelationship to get
     * @return response which when successful contains the iSARelationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/is-as/{guid}")
    public SubjectAreaOMASAPIResponse<IsA> getIsARelationship(@PathVariable String serverName,
                                                              @PathVariable String userId,
                                                              @PathVariable String guid) {
        return restAPI.getIsARelationship(serverName, userId, guid);
    }

    /**
     * Update a IsARelationship, which is a link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param guid       unique identifier of the relationship
     * @param isa        the is-a relationship
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created IsARelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/is-as/{guid}")
    public SubjectAreaOMASAPIResponse<IsA> updateIsARelationship(@PathVariable String serverName,
                                                                 @PathVariable String userId,
                                                                 @PathVariable String guid,
                                                                 @RequestBody IsA isa,
                                                                 @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace) {
        return restAPI.updateIsARelationship(serverName, userId, guid, isa, isReplace);
    }

    /**
     * Delete a IsARelationship, which is a link between a more general glossary term and a more specific definition.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the IsARelationship to delete
     *
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/is-as/{guid}")
    public SubjectAreaOMASAPIResponse<IsA> deleteTermIsARelationship(@PathVariable String serverName,
                                                                     @PathVariable String userId,
                                                                     @PathVariable String guid
                                                                    ) {
        return restAPI.deleteIsARelationship(serverName, userId, guid);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/is-as/{guid}")
    public SubjectAreaOMASAPIResponse<IsA> restoreIsaRelationship(@PathVariable String serverName,
                                                                  @PathVariable String userId,
                                                                  @PathVariable String guid) {
        return restAPI.restoreIsARelationship(serverName, userId, guid);
    }

    /**
     * Create a termIsATypeOfRelationship, which is an inheritance relationship between two spine objects.
     *
     * <p>
     * @deprecated IsATypeOfRelationship it is deprecated; move your instances to use IsATypeOf instead.
     * @param serverName                serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                    userId under which the request is performed
     * @param termIsATypeOfRelationship the TermIsATypeOfRelationship
     * @return response, when successful contains the created termIsATypeOfRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/is-a-type-of-deprecateds")
    public SubjectAreaOMASAPIResponse<IsATypeOfDeprecated> createTermIsATypeOfRelationship(@PathVariable String serverName,
                                                                                           @PathVariable String userId,
                                                                                           @RequestBody IsATypeOfDeprecated termIsATypeOfRelationship) {
        return restAPI.createIsATypeOfDeprecated(serverName, userId, termIsATypeOfRelationship);
    }

    /**
     * Get a termIsATypeOfRelationship, which is an inheritance relationship between two spine objects.
     * @deprecated IsATypeOfRelationship it is deprecated; move your instances to use IsATypeOf instead.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the termIsATypeOfRelationship to get
     * @return response which when successful contains the termIsATypeOfRelationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/is-a-type-of-deprecateds/{guid}")
    public SubjectAreaOMASAPIResponse<IsATypeOfDeprecated> getTermIsATypeOfRelationship(@PathVariable String serverName,
                                                                                        @PathVariable String userId,
                                                                                        @PathVariable String guid) {
        return restAPI.getIsATypeOfDeprecated(serverName, userId, guid);
    }

    /**
     * Update a termIsATypeOfRelationship, which is an inheritance relationship between two spine objects.
     * <p>
     * @deprecated IsATypeOfRelationship it is deprecated; move your instances to use IsATypeOf instead. 
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param guid       unique identifier of the relationship
     * @param isatypeof  the is-a-type-of-deprecated relationship
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created IsARelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/is-a-type-of-deprecateds/{guid}")
    public SubjectAreaOMASAPIResponse<IsATypeOfDeprecated> updateIsARelationship(@PathVariable String serverName,
                                                                                 @PathVariable String userId,
                                                                                 @PathVariable String guid,
                                                                                 @RequestBody IsATypeOfDeprecated isatypeof,
                                                                                 @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace) {
        return restAPI.updateIsATypeOfDeprecated(serverName, userId, guid, isatypeof, isReplace);
    }


    /**
     * Delete a TermIsATypeOf Relationship, which is an inheritance relationship between two spine objects.
     * @deprecated IsATypeOfRelationship it is deprecated; move your instances to use IsATypeOf instead.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the TermIsATypeOfRelationship to delete
     *
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/is-a-type-of-deprecateds/{guid}")
    public SubjectAreaOMASAPIResponse<IsATypeOfDeprecated> deleteTermIsaTypeOfRelationship(@PathVariable String serverName,
                                                                                           @PathVariable String userId,
                                                                                           @PathVariable String guid
                                                                                          ) {
        return restAPI.deleteIsATypeOfDeprecatedRelationship(serverName, userId, guid);
    }

    /**
     * Restore a TermIsATypeOf Relationship, which is an inheritance relationship between two spine objects.
     * @deprecated IsATypeOfRelationship it is deprecated; move your instances to use IsATypeOf instead. 
     * <p>
     * Restore allows the deleted Is a Type Of Relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @deprecated 
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Term Is a Type Of Relationship to delete
     * @return response which when successful contains the restored TermIsaTypeOfRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */


    @PostMapping(path = "/users/{userId}/relationships/is-a-type-of-deprecateds/{guid}")
    public SubjectAreaOMASAPIResponse<IsATypeOfDeprecated> restoreTermIsaTypeOfRelationship(@PathVariable String serverName,
                                                                                            @PathVariable String userId,
                                                                                            @PathVariable String guid) {
        return restAPI.restoreIsATypeOfDeprecated(serverName, userId, guid);
    }

    /**
     * Create a IsATypeOf relationship, which is an inheritance relationship between two spine objects.
     * <p>
     *
     * @param serverName                serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                    userId under which the request is performed
     * @param IsATypeOf               the IsATypeOf
     * @return response, when successful contains the created IsATypeOf
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
    @PostMapping(path = "/users/{userId}/relationships/is-a-type-ofs")
    public SubjectAreaOMASAPIResponse<IsATypeOf> createIsATypeOf(@PathVariable String serverName,
                                                                         @PathVariable String userId,
                                                                         @RequestBody IsATypeOf IsATypeOf) {
        return restAPI.createIsATypeOf(serverName, userId, IsATypeOf);
    }
    

    /**
     * Get a IsATypeOf, which is an inheritance relationship between two spine objects.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the IsATypeOf to get
     * @return response which when successful contains the IsATypeOf with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/is-a-type-ofs/{guid}")
    public SubjectAreaOMASAPIResponse<IsATypeOf> getIsATypeOf(@PathVariable String serverName,
                                                                      @PathVariable String userId,
                                                                      @PathVariable String guid) {
        return restAPI.getIsATypeOf(serverName, userId, guid);
    }

    /**
     * Update a IsATypeOf, which is an inheritance relationship between two spine objects.
     * <p>
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param guid       unique identifier of the relationship
     * @param IsATypeOf  the is-a-type-ofs relationship
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created IsATypeOf
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/is-a-type-ofs/{guid}")
    public SubjectAreaOMASAPIResponse<IsATypeOf> updateIsATypeOf(@PathVariable String serverName,
                                                                         @PathVariable String userId,
                                                                         @PathVariable String guid,
                                                                         @RequestBody IsATypeOf IsATypeOf,
                                                                         @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace) {
        return restAPI.updateIsATypeOf(serverName, userId, guid, IsATypeOf, isReplace);
    }


    /**
     * Delete a IsATypeOf, which is an inheritance relationship between two spine objects.
     *  
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the IsATypeOf relationship to delete
     *
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/is-a-type-ofs/{guid}")
    public SubjectAreaOMASAPIResponse<IsATypeOf> deleteTermIsATypeOf(@PathVariable String serverName,
                                                                             @PathVariable String userId,
                                                                             @PathVariable String guid
                                                                            ) {
        return restAPI.deleteIIsATypeOf(serverName, userId, guid);
    }

    /**
     * Restore a 'object inheritances' Relationship
     *
     * <p>
     * Restore allows the deleted 'object inheritances' Relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Term Is a Type Of Relationship to delete
     * @return response which when successful contains the restoredIsATypeOf
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */


    @PostMapping(path = "/users/{userId}/relationships/is-a-type-ofs/{guid}")
    public SubjectAreaOMASAPIResponse<IsATypeOf> restoreTermIsATypeOf(@PathVariable String serverName,
                                                                              @PathVariable String userId,
                                                                              @PathVariable String guid) {
        return restAPI.restoreIsATypeOf(serverName, userId, guid);
    }

    /**
     * Create a termCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * <p>
     *
     * @param serverName                     serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                         userId under which the request is performed
     * @param termCategorizationRelationship the TermCategorizationRelationship
     * @return response, when successful contains the created termCategorizationRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/term-categorizations")
    public SubjectAreaOMASAPIResponse<Categorization> createTermCategorization(@PathVariable String serverName,
                                                                               @PathVariable String userId,
                                                                               @RequestBody Categorization termCategorizationRelationship) {
        return restAPI.createTermCategorizationRelationship(serverName, userId, termCategorizationRelationship);
    }

    /**
     * Get a termCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the termCategorizationRelationship to get
     * @return response which when successful contains the termCategorizationRelationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/term-categorizations/{guid}")
    public SubjectAreaOMASAPIResponse<Categorization> getTermCategorizationRelationship(@PathVariable String serverName,
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
     * @param guid       unique identifier of the relationship
     * @param isatypeof  the is-a-type-of relationship
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created termCategorization Relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/term-categorizations/{guid}")
    public SubjectAreaOMASAPIResponse<Categorization> updateTermCategorizationRelationship(@PathVariable String serverName,
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
     * @param guid       guid of the TermCategorizationRelationship to delete
     *
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/term-categorizations/{guid}")
    public SubjectAreaOMASAPIResponse<Categorization> deleteTermCategorizationRelationship(@PathVariable String serverName,
                                                                                           @PathVariable String userId,
                                                                                           @PathVariable String guid
                                                                                          ) {
        return restAPI.deleteTermCategorizationRelationship(serverName, userId, guid);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/term-categorizations/{guid}")
    public SubjectAreaOMASAPIResponse<Categorization> restoreTermCategorizationRelationship(@PathVariable String serverName,
                                                                                            @PathVariable String userId,
                                                                                            @PathVariable String guid) {
        return restAPI.restoreTermCategorizationRelationship(serverName, userId, guid);
    }

    // No modification of TermAnchor exists, because this is an anchoring relationship


    /**
     * Get a termAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the termAnchor Relationship to get
     * @return response which when successful contains the termAnchorRelationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/term-anchor/{guid}")
    public SubjectAreaOMASAPIResponse<TermAnchor> getTermAnchorRelationship(@PathVariable String serverName,
                                                                            @PathVariable String userId,
                                                                            @PathVariable String guid) {
        return restAPI.getTermAnchorRelationship(serverName, userId, guid);

    }

    // No modification of CategoryAnchor exists, because this is an anchoring relationship


    /**
     * Get a categoryAnchor Relationship. A relationship between a Glossary and a Category. This relationship allows terms to be owned by a Glossary.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the categoryAnchor Relationship to get
     * @return response which when successful contains the categoryAnchorRelationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/category-anchor/{guid}")
    public SubjectAreaOMASAPIResponse<CategoryAnchor> getCategoryAnchor(@PathVariable String serverName,
                                                                        @PathVariable String userId,
                                                                        @PathVariable String guid) {
        return restAPI.getCategoryAnchorRelationship(serverName, userId, guid);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/project-scopes")
    public SubjectAreaOMASAPIResponse<ProjectScope> createProjectScopeRelationship(@PathVariable String serverName,
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/project-scopes/{guid}")
    public SubjectAreaOMASAPIResponse<ProjectScope> getProjectScopeRelationship(@PathVariable String serverName,
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
     * @param guid         unique identifier of the relationship
     * @param projectScope the projectScope relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created ProjectScopeRelationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/project-scopes/{guid}")
    public SubjectAreaOMASAPIResponse<ProjectScope> updateProjectScopeRelationship(@PathVariable String serverName,
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
     *
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/project-scopes/{guid}")
    public SubjectAreaOMASAPIResponse<ProjectScope> deleteProjectScopeRelationship(@PathVariable String serverName,
                                                                                   @PathVariable String userId,
                                                                                   @PathVariable String guid
                                                                                  ) {
        return restAPI.deleteProjectScopeRelationship(serverName, userId, guid);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/project-scopes/{guid}")
    public SubjectAreaOMASAPIResponse<ProjectScope> restoreProjectScopeRelationship(@PathVariable String serverName,
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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/semantic-assignments/{guid}")
    public SubjectAreaOMASAPIResponse<SemanticAssignment> getSemanticAssignmentRelationship(@PathVariable String serverName,
                                                                                            @PathVariable String userId,
                                                                                            @PathVariable String guid) {
        return restAPI.getSemanticAssignmentRelationship(serverName, userId, guid);
    }

    /**
     * Create a CategoryHierarchyLink Relationship. A relationship between two categories used to create nested categories.
     *
     * @param serverName            serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                userId under which the request is performed
     * @param categoryHierarchyLink the CategoryHierarchyLink relationship
     * @return response, when successful contains the created categoryHierarchyLink relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/category-hierarchy-link")
    public SubjectAreaOMASAPIResponse<CategoryHierarchyLink> createCategoryHierarchyLink(@PathVariable String serverName,
                                                                                         @PathVariable String userId,
                                                                                         @RequestBody CategoryHierarchyLink categoryHierarchyLink) {
        return restAPI.createCategoryHierarchyLink(serverName, userId, categoryHierarchyLink);
    }

    /**
     * Get a CategoryHierarchyLink Relationship. A relationship between two categories used to create nested categories.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the CategoryHierarchyLink Relationship to get
     * @return response which when successful contains the CategoryHierarchyLink relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/relationships/category-hierarchy-link/{guid}")
    public SubjectAreaOMASAPIResponse<CategoryHierarchyLink> getCategoryHierarchyLink(@PathVariable String serverName,
                                                                                      @PathVariable String userId,
                                                                                      @PathVariable String guid) {
        return restAPI.getCategoryHierarchyLink(serverName, userId, guid);
    }

    /**
     * Update a CategoryHierarchyLink Relationship. A relationship between two categories used to create nested categories.
     *
     * @param serverName            serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId                userId under which the request is performed
     * @param guid                  guid of the CategoryHierarchyLink relationship
     * @param categoryHierarchyLink the CategoryHierarchyLink relationship
     * @param isReplace             flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the updated categoryHierarchyLink
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/relationships/category-hierarchy-link/{guid}")
    public SubjectAreaOMASAPIResponse<CategoryHierarchyLink> updateCategoryHierarchyLink(@PathVariable String serverName,
                                                                                         @PathVariable String userId,
                                                                                         @PathVariable String guid,
                                                                                         @RequestBody CategoryHierarchyLink categoryHierarchyLink,
                                                                                         @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace
    ) {
        return restAPI.updateCategoryHierarchyLink(serverName, userId, guid, categoryHierarchyLink, isReplace);
    }

    /**
     * Delete a CategoryHierarchyLink Relationship. A relationship between two categories used to create nested categories.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the CategoryHierarchyLink relationship to delete
     *
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/relationships/category-hierarchy-link/{guid}")
    public SubjectAreaOMASAPIResponse<CategoryHierarchyLink> deleteCategoryHierarchyLink(@PathVariable String serverName,
                                                                                         @PathVariable String userId,
                                                                                         @PathVariable String guid
                                                                                        
    ) {
        return restAPI.deleteCategoryHierarchyLink(serverName, userId, guid);
    }

    /**
     * Restore a CategoryHierarchyLink Relationship. A relationship between two categories used to create nested categories.
     * <p>
     * Restore allows the deleted CategoryHierarchyLink Relationship to be made active again. Restore allows deletes to be undone.
     * Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the CategoryHierarchyLink Relationship to delete
     * @return response which when successful contains the restored CategoryHierarchyLink
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/relationships/category-hierarchy-link/{guid}")
    public SubjectAreaOMASAPIResponse<CategoryHierarchyLink> restoreCategoryHierarchyLink(@PathVariable String serverName,
                                                                                          @PathVariable String userId,
                                                                                          @PathVariable String guid) {
        return restAPI.restoreCategoryHierarchyLink(serverName, userId, guid);
    }
}

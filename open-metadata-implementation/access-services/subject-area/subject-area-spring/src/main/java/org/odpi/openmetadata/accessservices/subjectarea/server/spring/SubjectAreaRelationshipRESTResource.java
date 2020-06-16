/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaRESTServicesInstance;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaRelationshipRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides relationship authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/subject-area")

@Tag(name="Subject Area OMAS", description="The Subject Area OMAS supports subject matter experts who are documenting their knowledge about a particular subject. This includes glossary terms, reference data, validation rules.", externalDocs=@ExternalDocumentation(description="Subject Area Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/subject-area/"))

public class SubjectAreaRelationshipRESTResource extends SubjectAreaRESTServicesInstance
{
    private SubjectAreaRelationshipRESTServices restAPI = new SubjectAreaRelationshipRESTServices();
    /**
     * Default constructor
     */
    public SubjectAreaRelationshipRESTResource() {

    }

    /**
     * Create a Hasa is the relationship between a spine object and a spine attribute.
     * Note that this method does not error if the relationship ends are not spine objects or spine attributes.
     * This allows the user to create terms then make them spine objects and spine attributes at a later stage.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @return response, when successful contains the created Hasa
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
    @PostMapping( path = "/users/{userId}/relationships/has-as")
    public SubjectAreaOMASAPIResponse createTermHASARelationship(@PathVariable String serverName, @PathVariable String userId, @RequestBody Hasa termHASARelationship)
    {
        return restAPI.createTermHASARelationship(serverName, userId,termHASARelationship);
    }
    /**
     * Get a Term HAS A relationship
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the HAS A relationship to get
     * @return response which when successful contains the term has a relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/relationships/has-as/{guid}")
    public SubjectAreaOMASAPIResponse getTermHASARelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid)  {
        return restAPI.getTermHASARelationship(serverName, userId,guid);
    }
    /**
     * Update a Hasa is the relationship between a spine object and a spine attribute.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created Hasa
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
    @PutMapping( path = "/users/{userId}/relationships/has-as")
    public SubjectAreaOMASAPIResponse updateTermHASARelationship(@PathVariable String serverName, @PathVariable String userId, @RequestBody Hasa termHASARelationship, @RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateTermHASARelationship(serverName, userId,termHASARelationship,isReplace);
    }

    /**
     * Delete a Term HAS A relationship
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the HAS A relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/users/{userId}/relationships/has-as/{guid}")
    public SubjectAreaOMASAPIResponse deleteTermHASARelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteTermHASARelationship(serverName, userId,guid,isPurge);
    }
    /**
     * Restore a has a relationship.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
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
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @PostMapping( path = "/users/{userId}/relationships/has-as/{guid}")
    public SubjectAreaOMASAPIResponse restoreTermHASARelationship(@PathVariable String serverName, @PathVariable  String userId, @PathVariable  String guid)
    {
        return restAPI.restoreTermHASARelationship(serverName, userId,guid);
    }
    /**
     * Create a RelatedTerm. A Related Term is a link between two similar Terms.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
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
    @PostMapping( path = "/users/{userId}/relationships/related-terms")
    public SubjectAreaOMASAPIResponse createRelatedTerm(@PathVariable String serverName, @PathVariable String userId, @RequestBody RelatedTerm relatedTermRelationshipRelationship)
    {
        return restAPI.createRelatedTerm(serverName, userId, relatedTermRelationshipRelationship);
    }
    /**
     * Get a related Term relationship.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the related term relationship to get
     * @return response which when successful contains the related term relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/relationships/related-terms/{guid}")
    public SubjectAreaOMASAPIResponse getRelatedTerm(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid)  {
        return restAPI.getRelatedTerm(serverName, userId,guid);
    }
    /**
     * Update a Related Term relationship. A Related Term is a link between two similar Terms.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param relatedTermRelationship the related term  relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    @PutMapping( path = "/users/{userId}/relationships/related-terms")
    public SubjectAreaOMASAPIResponse updateRelatedTerm(@PathVariable String serverName, @PathVariable String userId, @RequestBody RelatedTerm relatedTermRelationship,@RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateRelatedTerm(serverName, userId,relatedTermRelationship,isReplace);
    }
    /**
     * Delete a Related Term relationship
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Related term relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/users/{userId}/relationships/related-terms/{guid}")
    public SubjectAreaOMASAPIResponse deleteRelatedTerm(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteRelatedTerm(serverName, userId,guid,isPurge);
    }
    /**
     * Restore a related Term relationship.
     *
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
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
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @PostMapping( path = "/users/{userId}/relationships/related-terms/{guid}")
    public SubjectAreaOMASAPIResponse restoreRelatedTerm(@PathVariable String serverName, @PathVariable  String userId, @PathVariable  String guid)
    {
        return restAPI.restoreRelatedTerm(serverName, userId,guid);
    }
    /**
     *  Create a synonym relationship, which is a link between glossary terms that have the same meaning.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param synonym the Synonym relationship
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
    @PostMapping( path = "/users/{userId}/relationships/synonyms")
    public SubjectAreaOMASAPIResponse createSynonym(@PathVariable String serverName, @PathVariable String userId, @RequestBody Synonym synonym)
    {
        return restAPI.createSynonym(serverName, userId,synonym);
    }
    /**
     * Get a synonym relationship, which is a link between glossary terms that have the same meaning.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the synonym relationship to get
     * @return response which when successful contains the synonym relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/relationships/synonyms/{guid}")
    public SubjectAreaOMASAPIResponse getSynonymRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid)  {
        return restAPI.getSynonymRelationship(serverName, userId,guid);
    }
    /**
     * Update a Synonym relationship which is a link between glossary terms that have the same meaning
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param synonym      the synonym  relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    @PutMapping( path = "/users/{userId}/relationships/synonyms")
    public SubjectAreaOMASAPIResponse updateSynonymRelationship(@PathVariable String serverName, @PathVariable String userId, @RequestBody Synonym synonym,@RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateSynonymRelationship(serverName, userId,synonym,isReplace);
    }
    /**
     * Delete a Synonym relationship
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Synonym relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/users/{userId}/relationships/synonyms/{guid}")
    public SubjectAreaOMASAPIResponse deleteSynonymRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteSynonymRelationship(serverName, userId,guid,isPurge);
    }
    /**
     * Restore a Synonym
     *
     * Restore allows the deleted Synonym to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
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
    @PostMapping( path = "/users/{userId}/relationships/synonyms/{guid}")
    public SubjectAreaOMASAPIResponse restoreSynonym( @PathVariable String serverName,  @PathVariable String userId, @PathVariable String guid)
    {
        return restAPI.restoreSynonym(serverName,userId,guid);
    }

    /**
     *  Create an antonym relationship, which is a link between glossary terms that have the opposite meaning.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param antonym the Antonym relationship
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
    @PostMapping( path = "/users/{userId}/relationships/antonyms")
    public SubjectAreaOMASAPIResponse createAntonym(@PathVariable String serverName, @PathVariable String userId, @RequestBody Antonym antonym)
    {
        return restAPI.createAntonym(serverName, userId,antonym);
    }
    /**
     * Get a antonym relationship
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the related term relationship to get
     * @return response which when successful contains the antonym relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/relationships/antonyms/{guid}")
    public SubjectAreaOMASAPIResponse getAntonymRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid)  {
        return restAPI.getAntonymRelationship(serverName, userId,guid);
    }
    /**
     * Update a Antonym relationship which is a link between glossary terms that have the opposite meaning
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param antonym      the antonym relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    @PutMapping( path = "/users/{userId}/relationships/antonyms")
    public SubjectAreaOMASAPIResponse updateAntonymRelationship(@PathVariable String serverName, @PathVariable String userId, @RequestBody Antonym antonym,@RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateAntonymRelationship(serverName, userId,antonym,isReplace);
    }
    /**
     * Delete a Antonym relationship
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Antonym relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/users/{userId}/relationships/antonyms/{guid}")
    public SubjectAreaOMASAPIResponse deleteAntonymRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteAntonymRelationship(serverName, userId,guid,isPurge);
    }
    /**
     * Restore a Antonym
     *
     * Restore allows the deleted Antonym to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
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
    @PostMapping( path = "/users/{userId}/relationships/antonyms/{guid}")
    public SubjectAreaOMASAPIResponse restoreAntonym( @PathVariable String serverName,  @PathVariable String userId, @PathVariable String guid)
    {
        return restAPI.restoreAntonym(serverName,userId,guid);
    }
    /**
     *  Create a translation relationship, which is a link between glossary terms to provide different natural language translation of the same concept.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
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
    @PostMapping( path = "/users/{userId}/relationships/translations")
    public SubjectAreaOMASAPIResponse createTranslation(@PathVariable String serverName, @PathVariable String userId, @RequestBody Translation  translation)
    {
        return restAPI.createTranslation(serverName, userId,translation);
    }

    /**
     * Get a translation relationshiptranslation relationship, which is a link between glossary terms to provide different natural language translation of the same concept.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the translation relationship to get
     * @return response which when successful contains the translation relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/relationships/translations/{guid}")
    public SubjectAreaOMASAPIResponse getTranslationRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid)  {
        return restAPI.getTranslationRelationship(serverName, userId,guid);
    }

    /**
     * Update a Translation relationship translation relationship, which is a link between glossary terms to provide different natural language translation of the same concept.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param translation     the translation relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    @PutMapping( path = "/users/{userId}/relationships/translations")
    public SubjectAreaOMASAPIResponse updateTranslationRelationship(@PathVariable String serverName, @PathVariable String userId, @RequestBody Translation translation,@RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateTranslationRelationship(serverName, userId,translation,isReplace);
    }

    /**
     * Delete a Translation relationshiptranslation relationship, which is a link between glossary terms to provide different natural language translation of the same concept.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Translation relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/users/{userId}/relationships/translations/{guid}")
    public SubjectAreaOMASAPIResponse deleteTranslationRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteTranslationRelationship(serverName, userId,guid,isPurge);
    }
    /**
     * Restore a Translation
     *
     * Restore allows the deleted Translation to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
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
    @PostMapping( path = "/users/{userId}/relationships/translations/{guid}")
    public SubjectAreaOMASAPIResponse restoreTranslation( @PathVariable String serverName,  @PathVariable String userId, @PathVariable String guid)
    {
        return restAPI.restoreTranslation(serverName,userId,guid);
    }
    /**
     *  Create a usedInContext relationship, which is a link between glossary terms, where one describes the context where the other one is valid to use.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
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
    @PostMapping( path = "/users/{userId}/relationships/used-in-contexts")
    public SubjectAreaOMASAPIResponse createusedInContext(@PathVariable String serverName, @PathVariable String userId, @RequestBody UsedInContext  usedInContext)
    {
        return restAPI.createUsedInContext(serverName, userId,usedInContext);
    }
    /**
     * Get a usedInContext relationship,  which is a link between glossary terms, where one describes the context where the other one is valid to use.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the usedInContext relationship to get
     * @return response which when successful contains the usedInContext relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/relationships/used-in-contexts/{guid}")
    public SubjectAreaOMASAPIResponse getUsedInContextRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid)  {
        return restAPI.getUsedInContextRelationship(serverName, userId,guid);
    }
    /**
     * Update a UsedInContext relationship which is a link between glossary terms, where one describes the context where the other one is valid to use.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param usedInContext the used in context relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    @PutMapping( path = "/users/{userId}/relationships/used-in-contexts")
    public SubjectAreaOMASAPIResponse updateUsedInContextRelationship(@PathVariable String serverName, @PathVariable String userId, @RequestBody UsedInContext usedInContext,@RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateUsedInContextRelationship(serverName, userId,usedInContext,isReplace);
    }

    /**
     * Delete a UsedInContext relationship which is a link between glossary terms, where one describes the context where the other one is valid to use.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the UsedInContext relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/users/{userId}/relationships/used-in-contexts/{guid}")
    public SubjectAreaOMASAPIResponse deleteUsedInContextRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteUsedInContextRelationship(serverName, userId,guid,isPurge);
    }
    /**
     * Restore a UsedInContext
     *
     * Restore allows the deleted UsedInContext to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
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
    @PostMapping( path = "/users/{userId}/relationships/used-in-contexts/{guid}")
    public SubjectAreaOMASAPIResponse restoreUsedInContext( @PathVariable String serverName,  @PathVariable String userId, @PathVariable String guid)
    {
        return restAPI.restoreUsedInContext(serverName,userId,guid);
    }

    /**
     *  Create a preferredTerm relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
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
    @PostMapping( path = "/users/{userId}/relationships/preferred-terms")
    public SubjectAreaOMASAPIResponse createpreferredTerm(@PathVariable String serverName, @PathVariable String userId, @RequestBody PreferredTerm  preferredTerm)
    {
        return restAPI.createPreferredTerm(serverName, userId,preferredTerm);
    }

    /**
     * Get a preferredTerm relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the preferredTerm relationship to get
     * @return response which when successful contains the preferredTerm relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/relationships/preferred-terms/{guid}")
    public SubjectAreaOMASAPIResponse getPreferredTermRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid)  {
        return restAPI.getPreferredTermRelationship(serverName, userId,guid);
    }

    /**
     * Update a PreferredTerm relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param preferredTerm  the preferred term relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    @PutMapping( path = "/users/{userId}/relationships/preferred-terms")
    public SubjectAreaOMASAPIResponse updatePreferredTermRelationship(@PathVariable String serverName, @PathVariable String userId, @RequestBody PreferredTerm preferredTerm,@RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updatePreferredTermRelationship(serverName, userId,preferredTerm,isReplace);
    }

    /**
     * Delete a PreferredTerm relationship, which is a link between glossary terms, it is a Link to an alternative term that the organization prefers is used.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the PreferredTerm relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/users/{userId}/relationships/preferred-terms/{guid}")
    public SubjectAreaOMASAPIResponse deletePreferredTermRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deletePreferredTermRelationship(serverName, userId,guid,isPurge);
    }
    /**
     * Restore a PreferredTerm
     *
     * Restore allows the deleted PreferredTerm to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
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
    @PostMapping( path = "/users/{userId}/relationships/preferred-terms/{guid}")
    public SubjectAreaOMASAPIResponse restorePreferredTerm( @PathVariable String serverName,  @PathVariable String userId, @PathVariable String guid)
    {
        return restAPI.restorePreferredTerm(serverName,userId,guid);
    }


    /**
     *  Create a validValue relationship, which is a link between glossary terms that have the same meaning.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
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
    @PostMapping( path = "/users/{userId}/relationships/valid-values")
    public SubjectAreaOMASAPIResponse createvalidValue(@PathVariable String serverName, @PathVariable String userId, @RequestBody ValidValue  validValue)
    {
        return restAPI.createValidValue(serverName, userId,validValue);
    }

    /**
     * Get a validValue relationship, which is a link between glossary terms that have the same meaning.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the validValue relationship to get
     * @return response which when successful contains the validValue relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/relationships/valid-values/{guid}")
    public SubjectAreaOMASAPIResponse getValidValueRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid)  {
        return restAPI.getValidValueRelationship(serverName, userId,guid);
    }

    /**
     * Update a ValidValue relationship which is a link between glossary terms that have the same meaning
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param validValue the valid value relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    @PutMapping( path = "/users/{userId}/relationships/valid-values")
    public SubjectAreaOMASAPIResponse updateValidValueRelationship(@PathVariable String serverName, @PathVariable String userId, @RequestBody ValidValue validValue,@RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateValidValueRelationship(serverName, userId,validValue,isReplace);
    }


    /**
     * Delete a ValidValue relationship
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ValidValue relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/users/{userId}/relationships/valid-values/{guid}")
    public SubjectAreaOMASAPIResponse deleteValidValueRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteValidValueRelationship(serverName, userId,guid,isPurge);
    }
    /**
     * Restore a ValidValue
     *
     * Restore allows the deleted ValidValue to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
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
    @PostMapping( path = "/users/{userId}/relationships/valid-values/{guid}")
    public SubjectAreaOMASAPIResponse restoreValidValue( @PathVariable String serverName,  @PathVariable String userId, @PathVariable String guid)
    {
        return restAPI.restoreValidValue(serverName,userId,guid);
    }

    /**
     *  Create a replacementTerm relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
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
    @PostMapping( path = "/users/{userId}/relationships/replacement-terms")
    public SubjectAreaOMASAPIResponse createreplacementTerm(@PathVariable String serverName, @PathVariable String userId, @RequestBody ReplacementTerm  replacementTerm)
    {
        return restAPI.createReplacementTerm(serverName, userId,replacementTerm);
    }

    /**
     * Get a replacementTerm relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the replacementTerm relationship to get
     * @return response which when successful contains the replacementTerm relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/relationships/replacement-terms/{guid}")
    public SubjectAreaOMASAPIResponse getReplacementTermRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid)  {
        return restAPI.getReplacementTerm(serverName, userId,guid);
    }

    /**
     * Update a ReplacementTerm relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param replacementTerm  the replacement term relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    @PutMapping( path = "/users/{userId}/relationships/replacement-terms")
    public SubjectAreaOMASAPIResponse updateReplacementTermRelationship(@PathVariable String serverName, @PathVariable String userId, @RequestBody ReplacementTerm replacementTerm,@RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateReplacementTerm
                (serverName, userId,replacementTerm,isReplace);
    }

    /**
     * Delete a ReplacementTerm relationship, which is a link to a glossary term that is replacing an obsolete glossary term.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the ReplacementTerm relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/users/{userId}/relationships/replacement-terms/{guid}")
    public SubjectAreaOMASAPIResponse deleteReplacementTermRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteReplacementTerm(serverName, userId,guid,isPurge);
    }
    /**
     * Restore a ReplacementTerm
     *
     * Restore allows the deleted ReplacementTerm to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
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
    @PostMapping( path = "/users/{userId}/relationships/replacement-terms/{guid}")
    public SubjectAreaOMASAPIResponse restoreReplacementTerm( @PathVariable String serverName,  @PathVariable String userId, @PathVariable String guid)
    {
        return restAPI.restoreReplacementTerm(serverName,userId,guid);
    }

    /**
     *  Create a termTYPEDBYRelationship relationship, which is a link between a spine attribute and its type.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termTYPEDBYRelationship the TypedBy relationship
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
    @PostMapping( path = "/users/{userId}/relationships/typed-bys")
    public SubjectAreaOMASAPIResponse createtermTYPEDBYRelationship(@PathVariable String serverName, @PathVariable String userId, @RequestBody TypedBy termTYPEDBYRelationship)
    {
        return restAPI.createTermTYPEDBYRelationship(serverName, userId,termTYPEDBYRelationship);
    }

    /**
     * Get a termTYPEDBYRelationship relationship, which is a link between a spine attribute and its type.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the termTYPEDBYRelationship relationship to get
     * @return response which when successful contains the termTYPEDBYRelationship relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/relationships/typed-bys/{guid}")
    public SubjectAreaOMASAPIResponse getTYPEDBYRelationshipRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid)  {
        return restAPI.getTermTYPEDBYRelationship(serverName, userId,guid);
    }

    /**
     * Update a TypedBy relationship, which is a link between a spine attribute and its type.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termTYPEDBYRelationship the typed by relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    @PutMapping( path = "/users/{userId}/relationships/typed-bys")
    public SubjectAreaOMASAPIResponse updateTermTYPEDBYRelationship(@PathVariable String serverName, @PathVariable String userId, @RequestBody TypedBy termTYPEDBYRelationship, @RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateTermTYPEDBYRelationship(serverName, userId,termTYPEDBYRelationship,isReplace);
    }

    /**
     * Delete a TypedBy relationship, which is a link between a spine attribute and its type.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TypedBy relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/users/{userId}/relationships/typed-bys/{guid}")
    public SubjectAreaOMASAPIResponse deleteTypedByRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteTermTYPEDBYRelationship(serverName, userId,guid,isPurge);
    }
    /**
     * Restore a Term TYPED BY relationship
     *
     * Restore allows the deleted TermTYPEDBY to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
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
    @PostMapping( path = "/users/{userId}/relationships/typed-bys/{guid}")
    public SubjectAreaOMASAPIResponse restoreTermTYPEDBYRelationship( @PathVariable String serverName,  @PathVariable String userId, @PathVariable String guid)
    {
        return restAPI.restoreTermTYPEDBYRelationship(serverName,userId,guid);
    }

    /**
     *  Create a iSARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param iSARelationship the Isa relationship
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
    @PostMapping( path = "/users/{userId}/relationships/is-as")
    public SubjectAreaOMASAPIResponse createiSARelationship(@PathVariable String serverName, @PathVariable String userId, @RequestBody Isa iSARelationship)
    {
        return restAPI.createISARelationship(serverName, userId,iSARelationship);
    }

    /**
     * Get a iSARelationship relationship, which is a link between a more general glossary term and a more specific definition.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the iSARelationship relationship to get
     * @return response which when successful contains the iSARelationship relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/relationships/is-as/{guid}")
    public SubjectAreaOMASAPIResponse getISARelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid)  {
        return restAPI.getISARelationship(serverName, userId,guid);
    }

    /**
     * Update a Isa relationship, which is a link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param isa                the is-a relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    @PutMapping( path = "/users/{userId}/relationships/is-as")
    public SubjectAreaOMASAPIResponse updateISARelationship(@PathVariable String serverName, @PathVariable String userId, @RequestBody Isa isa, @RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateISARelationship(serverName, userId,isa,isReplace);
    }
    /**
     * Delete a Isa relationship, which is a link between a more general glossary term and a more specific definition.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Isa relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/users/{userId}/relationships/is-as/{guid}")
    public SubjectAreaOMASAPIResponse deleteTermISARelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteISARelationship(serverName, userId,guid,isPurge);
    }
    /**
     * Restore a Isa Relationship
     *
     * Restore allows the deleted Isa Relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
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
    @PostMapping( path = "/users/{userId}/relationships/is-as/{guid}")
    public SubjectAreaOMASAPIResponse restoreIsaRelationship( @PathVariable String serverName,  @PathVariable String userId, @PathVariable String guid)
    {
        return restAPI.restoreISARelationship(serverName,userId,guid);
    }


    /**
     *  Create a termISATypeOFRelationship relationship, which is an inheritance relationship between two spine objects.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param termISATypeOFRelationship the IsaTypeOf relationship
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
    @PostMapping( path = "/users/{userId}/relationships/is-a-type-ofs")
    public SubjectAreaOMASAPIResponse createtermISATypeOFRelationship(@PathVariable String serverName, @PathVariable String userId, @RequestBody IsaTypeOf termISATypeOFRelationship)
    {
        return restAPI.createTermISATypeOFRelationship(serverName, userId,termISATypeOFRelationship);
    }

    /**
     * Get a termISATypeOFRelationship relationship, which is an inheritance relationship between two spine objects.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the termISATypeOFRelationship relationship to get
     * @return response which when successful contains the termISATypeOFRelationship relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/relationships/is-a-type-ofs/{guid}")
    public SubjectAreaOMASAPIResponse getTermISATypeOFRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid)  {
        return restAPI.getTermISATypeOFRelationship(serverName, userId,guid);
    }

    /**
     * Update a Isa relationship, which is a link between a more general glossary term and a more specific definition.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param isatypeof   the is-a-type-of relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    @PutMapping( path = "/users/{userId}/relationships/is-a-type-ofs")
    public SubjectAreaOMASAPIResponse updateISARelationship(@PathVariable String serverName, @PathVariable String userId, @RequestBody IsaTypeOf isatypeof, @RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateTermISATypeOFRelationship(serverName, userId,isatypeof,isReplace);
    }


    /**
     * Delete a IsaTypeOf relationship, which is an inheritance relationship between two spine objects.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the IsaTypeOf relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/users/{userId}/relationships/is-a-type-ofs/{guid}")
    public SubjectAreaOMASAPIResponse deleteTermIsaTypeOfRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteIsATypeOfRelationship(serverName, userId,guid,isPurge);
    }
    /**
     * Restore a Is a Type Of Relationship
     *
     * Restore allows the deleted Is a Type Of Relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
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
    
    
    
    
    @PostMapping( path = "/users/{userId}/relationships/is-a-type-ofs/{guid}")
    public SubjectAreaOMASAPIResponse restoreTermIsaTypeOfRelationship( @PathVariable String serverName,  @PathVariable String userId, @PathVariable String guid)
    {
        return restAPI.restoreTermISATypeOFRelationship(serverName,userId,guid);
    }


    /**
     *  Create a termCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
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
    @PostMapping( path = "/users/{userId}/relationships/term-categorizations")
    public SubjectAreaOMASAPIResponse createTermCategorization(@PathVariable String serverName, @PathVariable String userId, @RequestBody Categorization termCategorizationRelationship)
    {
        return  restAPI.createTermCategorizationRelationship(serverName, userId, termCategorizationRelationship);
    }

    /**
     * Get a termCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the termCategorizationRelationship relationship to get
     * @return response which when successful contains the termCategorizationRelationship relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/relationships/term-categorizations/{guid}")
    public SubjectAreaOMASAPIResponse getTermCategorizationRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid)  {
        return restAPI.getTermCategorizationRelationship(serverName, userId,guid);
    }

    /**
     * Update a termCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param isatypeof   the is-a-type-of relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    @PutMapping( path = "/users/{userId}/relationships/term-categorizations")
    public SubjectAreaOMASAPIResponse updateTermCategorizationRelationship(@PathVariable String serverName, @PathVariable String userId, @RequestBody Categorization isatypeof, @RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateTermCategorizationRelationship(serverName, userId,isatypeof,isReplace);
    }


    /**
     * Delete a TermCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermCategorizationRelationship relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/users/{userId}/relationships/term-categorizations/{guid}")
    public SubjectAreaOMASAPIResponse deleteTermCategorizationRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteTermCategorizationRelationship(serverName, userId,guid,isPurge);
    }
    /**
     * Restore a TermCategorization Relationship. A relationship between a Category and a Term. This relationship allows terms to be categorized.
     *
     * Restore allows the deleted TermCategorization Relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
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
    @PostMapping( path = "/users/{userId}/relationships/term-categorizations/{guid}")
    public SubjectAreaOMASAPIResponse restoreTermCategorizationRelationship( @PathVariable String serverName,  @PathVariable String userId, @PathVariable String guid)
    {
        return restAPI.restoreTermCategorizationRelationship(serverName,userId,guid);
    }

    /**
     *  Create a termAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     * Terms created using the Subject Area OMAS cannot be created without a glossary and there can only be one glossary associated with a
     * Term. This method is to allow glossaries to be associated with Terms that have not been created via the Subject Area OMAS or to recreate
     * the TermAnchor relationship if it has been purged.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
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
    @PostMapping( path = "/users/{userId}/relationships/term-anchor")
    public SubjectAreaOMASAPIResponse createTermAnchor(@PathVariable String serverName, @PathVariable String userId, @RequestBody TermAnchor termAnchorRelationship)
    {
        return restAPI.createTermAnchorRelationship(serverName, userId, termAnchorRelationship);
    }

    /**
     * Get a termAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the termAnchor Relationship to get
     * @return response which when successful contains the termAnchorRelationship relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/relationships/term-anchor/{guid}")
    public SubjectAreaOMASAPIResponse getTermAnchorRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid)  {
        return restAPI.getTermAnchorRelationship(serverName, userId, guid);

    }


    /**
     * Delete a TermAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the TermAnchorRelationship relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/users/{userId}/relationships/term-anchor/{guid}")
    public SubjectAreaOMASAPIResponse deleteTermAnchorRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid, @RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        return restAPI.deleteTermAnchorRelationship(serverName, userId, guid,isPurge);
    }
    /**
     * Restore a TermAnchor Relationship. A relationship between a Glossary and a Term. This relationship allows terms to be owned by a Glossary.
     *
     * Restore allows the deleted TermAnchor Relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
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
    @PostMapping( path = "/users/{userId}/relationships/term-anchor/{guid}")
    public SubjectAreaOMASAPIResponse restoreTermAnchorRelationship( @PathVariable String serverName,  @PathVariable String userId, @PathVariable String guid)
    {
        return restAPI.restoreTermAnchorRelationship(serverName, userId, guid);
    }
    /**
     * Create a categoryAnchor Relationship. A relationship between a Glossary and a Category. This relationship allows terms to be owned by a Glossary.
     * Categories created using the Subject Area OMAS cannot be created without a glossary and there can only be one glossary associated with a
     * Category. This method is to allow glossaries to be associated with Categories that have not been created via the Subject Area OMAS or to recreate
     * the CategoryAnchor relationship if it has been purged.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
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
    @PostMapping( path = "/users/{userId}/relationships/category-anchor")
    public SubjectAreaOMASAPIResponse createCategoryAnchor(@PathVariable String serverName, @PathVariable String userId, @RequestBody CategoryAnchor categoryAnchorRelationship)
    {
        return restAPI.createCategoryAnchorRelationship(serverName, userId, categoryAnchorRelationship);
    }

    /**
     * Get a categoryAnchor Relationship. A relationship between a Glossary and a Category. This relationship allows terms to be owned by a Glossary.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the categoryAnchor Relationship to get
     * @return response which when successful contains the categoryAnchorRelationship relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/relationships/category-anchor/{guid}")
    public SubjectAreaOMASAPIResponse getCategoryAnchorRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid)  {
        return restAPI.getCategoryAnchorRelationship(serverName, userId, guid);
    }

    /**
     * Delete a CategoryAnchor Relationship. A relationship between a Glossary and a Category. This relationship allows terms to be owned by a Glossary.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the CategoryAnchorRelationship relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/users/{userId}/relationships/category-anchor/{guid}")
    public SubjectAreaOMASAPIResponse deleteCategoryAnchorRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid, @RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        return restAPI.deleteCategoryAnchorRelationship(serverName, userId, guid,isPurge);
    }
    /**
     * Restore a CategoryAnchor Relationship. A relationship between a Glossary and a Category. This relationship allows terms to be owned by a Glossary.
     *
     * Restore allows the deleted CategoryAnchor Relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
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
    @PostMapping( path = "/users/{userId}/relationships/category-anchor/{guid}")
    public SubjectAreaOMASAPIResponse restoreCategoryAnchorRelationship( @PathVariable String serverName,  @PathVariable String userId, @PathVariable String guid)
    {
        return restAPI.restoreCategoryAnchorRelationship(serverName, userId,  guid);
    }

    /**
     *  Create a project scope relationship, which is a link between the project content and the project.
     *
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
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
    @PostMapping( path = "/users/{userId}/relationships/project-scopes")
    public SubjectAreaOMASAPIResponse createProjectScopeRelationship(@PathVariable String serverName, @PathVariable String userId, @RequestBody ProjectScope projectScope)
    {
        return restAPI.createProjectScopeRelationship(serverName, userId,projectScope);
    }
    /**
     * Get a project scope relationship, which is a link between the project content and the project.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the project scope relationship to get
     * @return response which when successful contains the project scope relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/relationships/project-scopes/{guid}")
    public SubjectAreaOMASAPIResponse getProjectScopeRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid)  {
        return restAPI.getProjectScopeRelationship(serverName, userId,guid);
    }
    /**
     * Update a Project scope relationship which is a link between the project content and the project.
     * <p>
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId               userId under which the request is performed
     * @param projectScope     the projectScope relationship
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
    @PutMapping( path = "/users/{userId}/relationships/project-scopes")
    public SubjectAreaOMASAPIResponse updateProjectScopeRelationship(@PathVariable String serverName, @PathVariable String userId, @RequestBody ProjectScope projectScope, @RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateProjectScopeRelationship(serverName, userId,projectScope,isReplace);
    }
    /**
     * Delete a Project scope relationship which is a link between the project content and the project.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Project scope relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/users/{userId}/relationships/project-scopes/{guid}")
    public SubjectAreaOMASAPIResponse deleteProjectScopeRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteProjectScopeRelationship(serverName, userId,guid,isPurge);
    }
    /**
     * Restore a ProjectScope, which is a link between the project content and the project.
     *
     * Restore allows the deleted ProjectScopeRelationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
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
    @PostMapping( path = "/users/{userId}/relationships/project-scopes/{guid}")
    public SubjectAreaOMASAPIResponse restoreProjectScoperRelationship( @PathVariable String serverName,  @PathVariable String userId, @PathVariable String guid) {
        return restAPI.restoreProjectScopeRelationship(serverName, userId, guid);
    }
    /**
     * Get a SemanticAssignment relationship,  Links a glossary term to another element such as an asset or schema element to define its meaning.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the SemanticAssignment relationship to get
     * @return response which when successful contains the SemanticAssignment relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/relationships/semantic-assignments/{guid}")
    public SubjectAreaOMASAPIResponse getSemanticAssignmentRelationship(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid)  {
        return restAPI.getSemanticAssignmentRelationship(serverName, userId,guid);
    }
}

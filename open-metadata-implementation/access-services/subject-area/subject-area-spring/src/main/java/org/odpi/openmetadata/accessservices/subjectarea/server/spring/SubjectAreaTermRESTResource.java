/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.spring;


import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Antonym;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.RelatedTermRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Synonym;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermHASARelationship;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaTermRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The SubjectAreaRESTServices provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectAreaDefinition Open Metadata
 * Assess Service (OMAS).  This interface provides term authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("/open-metadata/access-services/subject-area")
public class SubjectAreaTermRESTResource extends SubjectAreaRESTServices{
    private SubjectAreaTermRESTServices restAPI = new SubjectAreaTermRESTServices();
    /**
     * Default constructor
     */
    public SubjectAreaTermRESTResource() {
        //SubjectAreaRESTServices registers this omas.
    }

    /**
     * Create a Term
     *
     * The qualifiedName can be specified and will be honoured. If it is specified then the caller may wish to ensure that it is
     * unique. If this qualifiedName is not specified then one will be generated as GlossaryTerm concatinated with the guid.
     *
     * Failure to create the Terms classifications, link to its glossary or its icon, results in the create failing and the term being deleted
     *
     * @param userId userId
     * @param suppliedTerm term to create
     * @return response, when successful contains the created term.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> StatusNotSupportedException          A status value is not supported</li>
     * </ul>
     */
    @RequestMapping(method = RequestMethod.POST, path = "/users/{userId}/terms")
    public SubjectAreaOMASAPIResponse createTerm(@PathVariable String userId, @RequestBody Term suppliedTerm) {
        return restAPI.createTerm(userId,suppliedTerm);
    }

    /**
     * Get a Term
     * @param userId userId under which the request is performed
     * @param guid   guid of the term to get
     * @return a response which when successful contains the term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * </ul>
     */
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/terms/{guid}")
    public  SubjectAreaOMASAPIResponse getTermByGuid(@PathVariable String userId, @PathVariable String guid) {
       return restAPI.getTermByGuid(userId,guid);
    }

    /**
     * Update a Term
     * <p>
     * Status is not updated using this call.
     *
     * @param userId           userId under which the request is performed
     * @param guid             guid of the term to update
     * @param suppliedTerm     term to be updated
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/users/{userId}/terms/{guid}")
    public SubjectAreaOMASAPIResponse updateTerm(@PathVariable String userId,@PathVariable String guid, Term suppliedTerm, @RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateTerm(userId,guid,suppliedTerm,isReplace);
    }
    /**
     * Delete a Term instance
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the term instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the term will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param userId  userId under which the request is performed
     * @param guid    guid of the term to be deleted.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return a response which when successful contains a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the term was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the term was not purged</li>
     * </ul>
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/users/{userId}/terms/{guid}")
    public  SubjectAreaOMASAPIResponse deleteTerm(@PathVariable String userId,@PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge)  {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteTerm(userId,guid,isPurge);
    }
    /**
     * Create a TermHASARelationship is the relationship between a spine object and a spine attribute.
     * <p>
     *
     * @param userid               userid under which the request is performed
     * @param termGuid             guid of the term on which the relationship should be created
     * @param termHASARelationship the HASA relationship
     * @return response, when successful contains the created term HASA Relationship
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
    @RequestMapping(method = RequestMethod.POST, path = "/users/{userId}/terms/{termGuid}/hasa")
    public SubjectAreaOMASAPIResponse createTermHASARelationship(@PathVariable String userid,@PathVariable String termGuid,@RequestBody TermHASARelationship termHASARelationship)
    {
        return restAPI.createTermHASARelationship(userid,termGuid,termHASARelationship);
    }
    /**
     * Get a Term HAS A relationship
     *
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
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/relationships/hasa/{guid}")
    public SubjectAreaOMASAPIResponse getTermHASARelationship(@PathVariable String userId,@PathVariable String guid)  {
        return restAPI.getTermHASARelationship(userId,guid);
    }

    /**
     * Delete a Term HAS A relationship
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the HAS A relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the term has a relationship with the requested guid
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
    @RequestMapping(method = RequestMethod.DELETE, path = "/users/{userId}/relationships/hasa/{guid}")
    public SubjectAreaOMASAPIResponse deleteTermHASARelationship(@PathVariable String userId, @PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteTermHASARelationship(userId,guid,isPurge);
    }
    /**
     * Create a RelatedTermRelationship. A Related Term is a link between two similar Terms.
     * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
     * The first entity proxy has seeAlso as the proxy name for entity type GlossaryTerm.
     * The second entity proxy has seeAlso as the proxy name for entity type GlossaryTerm.
     *
     * Each entity proxy also stores the entities guid.
     *
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termGuid             guid of the term on which the relationship should be created
     * @param relatedTermRelationshipRelationship the RelatedTermRelationship relationship
     * @return response, when successful contains the created related term relationship
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
    @RequestMapping(method = RequestMethod.POST, path = "/users/{userId}/terms/{termGuid}/relatedterm")
    public SubjectAreaOMASAPIResponse createRelatedTerm(@PathVariable String userId,@PathVariable String termGuid,@RequestBody RelatedTermRelationship relatedTermRelationshipRelationship)
    {
        return restAPI.createRelatedTerm(userId,termGuid, relatedTermRelationshipRelationship);
    }
    /**
     * Get a related Term relationship
     *
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
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/relationships/relatedterm/{guid}")
    public SubjectAreaOMASAPIResponse getRelatedTermRelationship(@PathVariable String userId,@PathVariable String guid)  {
          return restAPI.getRelatedTermRelationship(userId,guid);
    }
    /**
     * Delete a Related Term relationship
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Related term relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the related term relationship with the requested guid
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
    @RequestMapping(method = RequestMethod.DELETE, path = "/users/{userId}/relationships/relatedterm/{guid}")
    public SubjectAreaOMASAPIResponse deleteRelatedTermRelationship(@PathVariable String userId, @PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI. deleteRelatedTermRelationship(userId,guid,isPurge);
    }
    /**
     *  Create a synonym relationship, which is a link between glossary terms that have the same meaning.
     *
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termGuid             guid of the term on which the relationship should be created
     * @param synonym the Synonym relationship
     * @return response, when successful contains the created synonym relationship
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
    @RequestMapping(method = RequestMethod.POST, path = "/users/{userId}/terms/{termGuid}/synonym")
    public SubjectAreaOMASAPIResponse createSynonym(@PathVariable String userId,@PathVariable String termGuid,@RequestBody Synonym synonym)
    {
        return restAPI.createSynonym(userId,termGuid,synonym);
    }
    /**
     * Get a synonym relationship
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the related term relationship to get
     * @return response which when successful contains the synonym relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    //       /users/Fred/relationships/synonym/ca51c8c4-8469-4e3a-adac-0720888c5c4c"

    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/relationships/synonym/{guid}")
    public SubjectAreaOMASAPIResponse getSynonymRelationship(@PathVariable String userId,@PathVariable String guid)  {
        return restAPI.getSynonymRelationship(userId,guid);
    }
    /**
     * Delete a Synonym relationship
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Synonym relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the Synonym relationship with the requested guid
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
    @RequestMapping(method = RequestMethod.DELETE, path = "/users/{userId}/relationships/synonym/{guid}")
    public SubjectAreaOMASAPIResponse deleteSynonymRelationship(@PathVariable String userId, @PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteSynonymRelationship(userId,guid,isPurge);
    }
    /**
     *  Create an antonym relationship, which is a link between glossary terms that have the opposite meaning.
     *
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termGuid             guid of the term on which the relationship should be created
     * @param antonym the Antonym relationship
     * @return response, when successful contains the created antonym relationship
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
    @RequestMapping(method = RequestMethod.POST, path = "/users/{userId}/terms/{termGuid}/antonym")
    public SubjectAreaOMASAPIResponse createAntonym(@PathVariable String userId,@PathVariable String termGuid, @RequestBody Antonym antonym)
    {
        return restAPI.createAntonym(userId,termGuid,antonym);
    }
    /**
     * Get a antonym relationship
     *
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
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/relationships/antonym/{guid}")
    public SubjectAreaOMASAPIResponse getAntonymRelationship(@PathVariable String userId,@PathVariable String guid)  {
        return restAPI.getAntonymRelationship(userId,guid);
    }
    /**
     * Delete a Antonym relationship
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Antonym relationship to delete
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return response which when successful contains the Antonym relationship with the requested guid
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
    @RequestMapping(method = RequestMethod.DELETE, path = "/users/{userId}/relationships/antonym/{guid}")
    public SubjectAreaOMASAPIResponse deleteAntonymRelationship(@PathVariable String userId, @PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge) {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteAntonymRelationship(userId,guid,isPurge);
    }
}
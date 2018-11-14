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
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaRelationshipRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaTermRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The SubjectAreaRESTServices provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectAreaDefinition Open Metadata
 * Assess Service (OMAS).  This interface provides relationship authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("/open-metadata/access-services/subject-area")
public class SubjectAreaRelationshipRESTResource extends SubjectAreaRESTServices{
    private SubjectAreaRelationshipRESTServices restAPI = new SubjectAreaRelationshipRESTServices();
    /**
     * Default constructor
     */
    public SubjectAreaRelationshipRESTResource() {
        //SubjectAreaRESTServices registers this omas.
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
     * Update a TermHASARelationship is the relationship between a spine object and a spine attribute.
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param termHASARelationship the HASA relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created TermHASARelationship
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
    @RequestMapping(method = RequestMethod.PUT, path = "/users/{userId}/relationships/hasa}")
    public SubjectAreaOMASAPIResponse updateTermHASARelationship(@PathVariable String userId, TermHASARelationship termHASARelationship,@RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateTermHASARelationship(userId,termHASARelationship,isReplace);
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
     * Update a RelatedTermRelationship. A Related Term is a link between two similar Terms.
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param relatedTermRelationship the related term relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created TermHASARelationship
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
    @RequestMapping(method = RequestMethod.PUT, path = "/users/{userId}/relationships/relatedterm}")
    public SubjectAreaOMASAPIResponse updateTermHASARelationship(@PathVariable String userId, RelatedTermRelationship relatedTermRelationship,@RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateRelatedTermRelationship(userId,relatedTermRelationship,isReplace);
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
     * Update a SynonymRelationship, which is a link between glossary terms that have the same meaning.
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param synonym the related term relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created TermHASARelationship
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
    @RequestMapping(method = RequestMethod.PUT, path = "/users/{userId}/relationships/synonym}")
    public SubjectAreaOMASAPIResponse updateTermHASARelationship(@PathVariable String userId, Synonym synonym,@RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateSynonymRelationship(userId,synonym,isReplace);
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
     * Update a AntonymRelationship, which is a link between glossary terms that have the same meaning.
     * <p>
     *
     * @param userId               userId under which the request is performed
     * @param antonym the related term relationship
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response, when successful contains the created TermHASARelationship
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
    @RequestMapping(method = RequestMethod.PUT, path = "/users/{userId}/relationships/antonym}")
    public SubjectAreaOMASAPIResponse updateTermHASARelationship(@PathVariable String userId, Antonym antonym,@RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateAntonymRelationship(userId,antonym,isReplace);
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
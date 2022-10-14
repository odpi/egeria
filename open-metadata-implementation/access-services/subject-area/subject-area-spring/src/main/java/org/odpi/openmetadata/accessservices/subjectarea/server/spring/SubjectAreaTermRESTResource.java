/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaTermRESTServices;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * The SubjectAreaRESTServicesInstance provides the server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides term authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/subject-area")
@Tag(name = "Subject Area OMAS", description = "The Subject Area OMAS supports subject matter experts who are documenting their knowledge about a particular subject. This includes glossary terms, reference data, validation rules.",
     externalDocs = @ExternalDocumentation(description = "Subject Area Open Metadata Access Service (OMAS)",
                                           url = "https://egeria-project.org/services/omas/subject-area/overview/"))
public class SubjectAreaTermRESTResource {
    private final SubjectAreaTermRESTServices restAPI = new SubjectAreaTermRESTServices();

    /**
     * Default constructor
     */
    public SubjectAreaTermRESTResource() {

    }

    /**
     * Create a Term
     * <p>
     * The name needs to be specified - as this is the main identifier for the term. The name should be unique for canonical glossaries. This API does not police the uniqueness in this case.
     * <p>
     * The qualifiedName can be specified and will be honoured. If it is specified then the caller may wish to ensure that it is
     * unique. If this qualifiedName is not specified then one will be generated as GlossaryTerm concatenated with the guid.
     * <p>
     * Failure to create the Terms classifications, link to its glossary or its icon, results in the create failing and the term being deleted
     *
     * @param serverName   serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId       userId
     * @param suppliedTerm term to create
     * @return response, when successful contains the created term.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata repository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> ClassificationException              Error processing a classification
     * <li> StatusNotSupportedException          A status value is not supported
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/terms")
    public SubjectAreaOMASAPIResponse<Term> createTerm(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @RequestBody Term suppliedTerm) {
        return restAPI.createTerm(serverName, userId, suppliedTerm);
    }

    /**
     * Get a Term
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the term to get
     * @return response which when successful contains the term with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/terms/{guid}")
    public SubjectAreaOMASAPIResponse<Term> getTermByGuid(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @PathVariable String guid) {
        return restAPI.getTermByGuid(serverName, userId, guid);
    }

    /**
     * Find Term
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param searchCriteria     String expression matching Term property values (this does not include the GlossarySummary content). When not specified, all terms are returned.
     * @param exactValue a boolean, which when set means that only exact matches will be returned, otherwise matches that start with the search criteria will be returned.
     * @param ignoreCase a boolean, which when set means that case will be ignored, if not set that case will be respected
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time.
     * @param startingFrom             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of Terms meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a find was issued but the repository does not implement find functionality in some way.</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/terms")
    public SubjectAreaOMASAPIResponse<Term> findTerm(@PathVariable String serverName, @PathVariable String userId,
                                                     @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
                                                     @RequestParam(value = "exactValue", required = false, defaultValue = "false") Boolean exactValue,
                                                     @RequestParam(value = "ignoreCase", required = false, defaultValue = "true") Boolean ignoreCase,
                                                     @RequestParam(value = "asOfTime", required = false) Date asOfTime,
                                                     @RequestParam(value = "startingFrom", required = false, defaultValue = "0") Integer startingFrom,
                                                     @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                     @RequestParam(value = "sequencingOrder", required = false) String sequencingOrder,
                                                     @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty
    ) {
        return restAPI.findTerm(serverName, userId, searchCriteria, exactValue, ignoreCase, asOfTime, startingFrom, pageSize, sequencingOrder, sequencingProperty);
    }

    /**
     * Get Term relationships
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param guid               guid of the term to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param startingFrom             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return a response which when successful contains the term relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */


    @GetMapping(path = "/users/{userId}/terms/{guid}/relationships")
    public SubjectAreaOMASAPIResponse<Relationship> getTermRelationships(@PathVariable String serverName, @PathVariable String userId,
                                                                         @PathVariable String guid,
                                                                         @RequestParam(value = "asOfTime", required = false) Date asOfTime,
                                                                         @RequestParam(value = "startingFrom", required = false, defaultValue = "0") Integer startingFrom,
                                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                         @RequestParam(value = "sequencingOrder", required = false) SequencingOrder sequencingOrder,
                                                                         @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty

                                                                        ) {
        return restAPI.getTermRelationships(serverName, userId, guid, asOfTime, startingFrom, pageSize, sequencingOrder, sequencingProperty);
    }

    /**
     * Update a Term
     * <p>
     * Status is not updated using this call.
     *
     * @param serverName   serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId       userId under which the request is performed
     * @param guid         guid of the term to update
     * @param suppliedTerm term to be updated
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated. The GovernanceClassification content is always replaced.
     * @return a response which when successful contains the updated term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/terms/{guid}")
    public SubjectAreaOMASAPIResponse<Term> updateTerm(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String guid,
                                                       @RequestBody Term suppliedTerm,
                                                       @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace
    ) {
        return restAPI.updateTerm(serverName, userId, guid, suppliedTerm, isReplace);
    }

    /**
     * Delete a Term instance
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional.
     * <p>
     * A soft delete means that the term instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the term will not exist after the operation.
     * when not successful the following Exception responses can occur
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param guid       guid of the term to be deleted.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the term was not deleted.</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/terms/{guid}")
    public SubjectAreaOMASAPIResponse<Term> deleteTerm(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String guid
                                                      ) {
        return restAPI.deleteTerm(serverName, userId, guid);
    }

    /**
     * Restore a Term
     * <p>
     * Restore allows the deleted Term to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the term to delete
     * @return response which when successful contains the restored term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/terms/{guid}")
    public SubjectAreaOMASAPIResponse<Term> restoreTerm(@PathVariable String serverName,
                                                        @PathVariable String userId,
                                                        @PathVariable String guid) {
        return restAPI.restoreTerm(serverName, userId, guid);
    }

    /**
     * Get the Categories categorizing this Term. The server has a maximum page size defined, the number of Categories returned is limited by that maximum page size.
     *
     * @param serverName   serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId       unique identifier for requesting user, under which the request is performed
     * @param guid         guid of the category to get terms
     * @param startingFrom the starting element number for this set of results.  This is used when retrieving elements
     * @param pageSize     the maximum number of elements that can be returned on this request.
     * @return A list of categories categorizing this Term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/terms/{guid}/categories")
    public SubjectAreaOMASAPIResponse<Category> getTermCategories(@PathVariable String serverName,
                                                                  @PathVariable String userId,
                                                                  @PathVariable String guid,
                                                                  @RequestParam(value = "startingFrom", required = false, defaultValue = "0") Integer startingFrom,
                                                                  @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        return restAPI.getTermCategories(serverName, userId, guid, startingFrom, pageSize);
    }
}

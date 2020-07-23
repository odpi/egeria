/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.subjectarea;


import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.terms.SubjectAreaTerm;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.SecureController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectAreaDefinition Open Metadata
 * Assess Service (OMAS).  This interface provides term authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("/api/subject-area/terms")
public class SubjectAreaTermController  extends SecureController
{
    private static String className = SubjectAreaTermController.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger(className);
    private final SubjectAreaTerm subjectAreaTerm;
    private final String user = "demo";

    /**
     * Default constructor
     * @param subjectArea main client object for the Subject Area OMAS
     */
    public SubjectAreaTermController(SubjectArea subjectArea) {
        this.subjectAreaTerm = subjectArea.getSubjectAreaTerm();
    }

    /**
     /**
     * Create a Term
     * <p>
     * The name needs to be specified - as this is the main identifier for the term. The name should be unique for canonical glossaries. This API does not police the uniqueness in this case.
     * <p>
     * The qualifiedName can be specified and will be honoured. If it is specified then the caller may wish to ensure that it is
     * unique. If this qualifiedName is not specified then one will be generated as GlossaryTerm concatinated with the the guid.
     * <p>
     * Failure to create the Terms classifications, link to its glossary or its icon, results in the create failing and the term being deleted

     * @param suppliedTerm Term to create
     * @param request Servlet request
     * @return response, when successful contains the created term.
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping()
    public SubjectAreaOMASAPIResponse<Term> createTerm(@RequestBody Term suppliedTerm, HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
        try {
            Term term = this.subjectAreaTerm.term().create(userId,suppliedTerm);
            response.addResult(term);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;
    }


    /**
     * Get a term.
     * @param guid guid of the term to get
     * @param request Servlet request
     * @return response which when successful contains the term with the requested guid
     *  when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping( path = "/{guid}")
    public  SubjectAreaOMASAPIResponse<Term> getTerm(@PathVariable String guid,HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
        try {
            Term term = this.subjectAreaTerm.term().getByGUID(userId,guid);
            response.addResult(term);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;
    }
    /**
     * Find Term
     *
     * @param searchCriteria String expression matching Term property values.
     * @param asOfTime the terms returned as they were at this time. null indicates at the current time.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is no limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @param request Servlet request
     * @return A list of terms meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping( path = "/")
    public  SubjectAreaOMASAPIResponse<Term> findTerm(
                                                @RequestParam(value = "searchCriteria", required=false) String searchCriteria,
                                                @RequestParam(value = "asOfTime", required=false) Date asOfTime,
                                                @RequestParam(value = "offset", required = false, defaultValue = PAGE_OFFSET_DEFAULT_VALUE) Integer offset,
                                                @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE_DEFAULT_VALUE) Integer pageSize,
                                                @RequestParam(value = "sequencingOrder", required=false) SequencingOrder sequencingOrder,
                                                @RequestParam(value = "SequencingProperty", required=false) String sequencingProperty,
                                                HttpServletRequest request
    )  {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
        try {
            FindRequest findRequest = new FindRequest();
            findRequest.setSearchCriteria(searchCriteria);
            findRequest.setAsOfTime(asOfTime);
            findRequest.setOffset(offset);
            findRequest.setPageSize(pageSize);
            findRequest.setSequencingOrder(sequencingOrder);
            findRequest.setSequencingProperty(sequencingProperty);

            List<Term> terms = this.subjectAreaTerm.term().find(userId, findRequest);
            response.addAllResults(terms);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;
    }
    /**
     * Get Term relationships
     *
     * @param guid   guid of the term to get
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is not limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @param request Servlet request
     * @return a response which when successful contains the term relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    @GetMapping( path = "/{guid}/relationships")
    public  SubjectAreaOMASAPIResponse<Line> getTermRelationships(
                                                            @PathVariable String guid,
                                                            @RequestParam(value = "asOfTime", required=false) Date asOfTime,
                                                            @RequestParam(value = "offset", required = false, defaultValue = PAGE_OFFSET_DEFAULT_VALUE) Integer offset,
                                                            @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE_DEFAULT_VALUE) Integer pageSize,
                                                            @RequestParam(value = "sequencingOrder", required=false) SequencingOrder sequencingOrder,
                                                            @RequestParam(value = "SequencingProperty", required=false) String sequencingProperty,
                                                            HttpServletRequest request
    
    ) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Line> response = new SubjectAreaOMASAPIResponse<>();
        try {
            FindRequest findRequest = new FindRequest();
            findRequest.setAsOfTime(asOfTime);
            findRequest.setOffset(offset);
            findRequest.setPageSize(pageSize);
            findRequest.setSequencingOrder(sequencingOrder);
            findRequest.setSequencingProperty(sequencingProperty);

            List<Line> lines = this.subjectAreaTerm.term().getRelationships(userId,guid, findRequest);
            response.addAllResults(lines);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;

    }

    /**
     * Update a Term
     * <p>
     * If the caller has chosen to incorporate the term name in their Term Terms or Categories qualified name, renaming the term will cause those
     * qualified names to mismatch the Term name.
     * If the caller has chosen to incorporate the term qualifiedName in their Term Terms or Categories qualified name, changing the qualified name of the term will cause those
     * qualified names to mismatch the Term name.
     * Status is not updated using this call.
     *
     * @param guid             guid of the term to update
     * @param term         term to update
     * @param isReplace flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @param request Servlet request
     * @return a response which when successful contains the updated term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping( path = "/{guid}")
    public  SubjectAreaOMASAPIResponse<Term> updateTerm(
                                                      @PathVariable String guid,
                                                      @RequestBody Term term,
                                                      @RequestParam(value = "isReplace", required=false, defaultValue = "false") Boolean isReplace,
                                                      HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
        try {
            Term updatedTerm;
            if (isReplace) {
                updatedTerm = this.subjectAreaTerm.term().replace(userId, guid, term);
            } else {
                updatedTerm = this.subjectAreaTerm.term().update(userId, guid, term);
            }
            response.addResult(updatedTerm);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;
    }

    /**
     * Delete a Term instance
     * <p>
     * The deletion of a term is only allowed if there is no term content (i.e. no terms or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the term instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the term will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param guid    guid of the term to be deleted.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @param request Servlet request
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping( path = "/{guid}")
    public  SubjectAreaOMASAPIResponse<Term> deleteTerm(@PathVariable String guid,
                                                        @RequestParam(value = "isPurge", required=false, defaultValue = "false") Boolean isPurge,
                                                        HttpServletRequest request)  {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
        try {
            if (isPurge) {
                this.subjectAreaTerm.term().purge(userId,guid);
            } else {
               this.subjectAreaTerm.term().delete(userId, guid);
            }

        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;
    }
    /**
     * Restore a Term
     *
     * Restore allows the deleted Term to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param guid       guid of the term to restore
     * @param request Servlet request
     * @return response which when successful contains the restored term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping( path = "/{guid}")
    public SubjectAreaOMASAPIResponse<Term> restoreTerm(@PathVariable String guid, HttpServletRequest request)
    {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
        try {
            Term term = this.subjectAreaTerm.term().restore(userId,guid);
            response.addResult(term);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;
    }
}

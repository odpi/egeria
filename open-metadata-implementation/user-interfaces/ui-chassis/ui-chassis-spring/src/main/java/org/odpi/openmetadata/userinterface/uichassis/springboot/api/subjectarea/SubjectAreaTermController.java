/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.subjectarea;


import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaTerm;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.utils.DetectUtils;
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
    private final SubjectArea subjectArea;
    private static String className = SubjectAreaTermController.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger(className);
    private final SubjectAreaTerm subjectAreaTerm;
    private final String user = "demo";

    /**
     * Default constructor
     * @param subjectArea main client object for the Subject Area OMAS
     */
    public SubjectAreaTermController(SubjectArea subjectArea) {

        this.subjectArea = subjectArea;
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
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    @PostMapping()
    public SubjectAreaOMASAPIResponse createTerm(@RequestBody Term suppliedTerm, HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse response=null;
        try {
            Term term = this.subjectAreaTerm.createTerm(userId,suppliedTerm);
            TermResponse termResponse = new TermResponse();
            termResponse.setTerm(term);
            response = termResponse;
        } catch (SubjectAreaCheckedException e) {
            response = DetectUtils.getResponseFromException(e);
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
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException  not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> FunctionNotSupportedException   Function not supported</li>
     * </ul>
     */
    @GetMapping( path = "/{guid}")
    public  SubjectAreaOMASAPIResponse getTerm(@PathVariable String guid,HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse response=null;
        try {
            Term term = this.subjectAreaTerm.getTermByGuid(userId,guid);
            TermResponse termResponse = new TermResponse();
            termResponse.setTerm(term);
            response = termResponse;
        } catch (SubjectAreaCheckedException e) {
            response = DetectUtils.getResponseFromException(e);
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
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a find was issued but the repository does not implement find functionality in some way.</li>
     * </ul>
     */
    @GetMapping( path = "/")
    public  SubjectAreaOMASAPIResponse findTerm(
                                                @RequestParam(value = "searchCriteria", required=false) String searchCriteria,
                                                @RequestParam(value = "asOfTime", required=false) Date asOfTime,
                                                @RequestParam(value = "offset", required=false) Integer offset,
                                                @RequestParam(value = "pageSize", required=false) Integer pageSize,
                                                @RequestParam(value = "sequencingOrder", required=false) SequencingOrder sequencingOrder,
                                                @RequestParam(value = "SequencingProperty", required=false) String sequencingProperty,
                                                HttpServletRequest request
    )  {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse response;
        try {
            if (offset ==null) {
                offset=0;
            }
            if (pageSize==null) {
                pageSize=0;
            }
            List<Term> terms = this.subjectAreaTerm.findTerm(userId,searchCriteria,asOfTime,offset,pageSize,sequencingOrder,sequencingProperty);
            TermsResponse termsResponse = new TermsResponse();
            termsResponse.setTerms(terms);
            response = termsResponse;
        } catch (SubjectAreaCheckedException e) {
            response = DetectUtils.getResponseFromException(e);
        }
        return  response;
    }
    /*
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
    public  SubjectAreaOMASAPIResponse getTermRelationships(
                                                            @PathVariable String guid,
                                                            @RequestParam(value = "asOfTime", required=false) Date asOfTime,
                                                            @RequestParam(value = "offset", required=false) Integer offset,
                                                            @RequestParam(value = "pageSize", required=false) Integer pageSize,
                                                            @RequestParam(value = "sequencingOrder", required=false) SequencingOrder sequencingOrder,
                                                            @RequestParam(value = "SequencingProperty", required=false) String sequencingProperty,
                                                            HttpServletRequest request
    
    ) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse response;
        try {
            if (offset ==null) {
                offset=0;
            }
            if (pageSize==null) {
                pageSize=0;
            }
            List<Line> lines = this.subjectAreaTerm.getTermRelationships(userId,guid,asOfTime,offset,pageSize,sequencingOrder,sequencingProperty);
            LinesResponse linesResponse = new LinesResponse();
            linesResponse.setLines(lines);
            response = linesResponse;
        } catch (SubjectAreaCheckedException e) {
            response = DetectUtils.getResponseFromException(e);
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
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    @PutMapping( path = "/{guid}")
    public  SubjectAreaOMASAPIResponse updateTerm(
                                                      @PathVariable String guid,
                                                      @RequestBody Term term,
                                                      @RequestParam(value = "isReplace", required=false) Boolean isReplace,
                                                      HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse response=null;
        try {
            Term updatedTerm;
            if (isReplace == null){
                isReplace = false;
            }
            if (isReplace) {
                updatedTerm = this.subjectAreaTerm.replaceTerm(userId, guid, term);
            } else {
                updatedTerm = this.subjectAreaTerm.updateTerm(userId, guid, term);
            }
            TermResponse termResponse = new TermResponse();
            termResponse.setTerm(updatedTerm);
            response = termResponse;
        } catch (SubjectAreaCheckedException e) {
            response = DetectUtils.getResponseFromException(e);
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
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the term was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the term was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/{guid}")
    public  SubjectAreaOMASAPIResponse deleteTerm(@PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge, HttpServletRequest request)  {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse response=null;
        try {
            if (isPurge) {
                this.subjectAreaTerm.purgeTerm(userId,guid);
                response = new VoidResponse();
            } else {
                Term term = this.subjectAreaTerm.deleteTerm(userId,guid);
                TermResponse termResponse = new TermResponse();
                termResponse.setTerm(term);
                response = termResponse;
            }

        } catch (SubjectAreaCheckedException e) {
            response = DetectUtils.getResponseFromException(e);
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
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping( path = "/{guid}")
    public SubjectAreaOMASAPIResponse restoreTerm(@PathVariable String guid, HttpServletRequest request)
    {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse response=null;
        try {
            Term term = this.subjectAreaTerm.restoreTerm(userId,guid);
            TermResponse termResponse = new TermResponse();
            termResponse.setTerm(term);
            response = termResponse;
        } catch (SubjectAreaCheckedException e) {
            response = DetectUtils.getResponseFromException(e);
        }
        return  response;
    }
}

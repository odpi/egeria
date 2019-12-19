/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria term. */
package org.odpi.openmetadata.viewservices.subjectarea.server;


import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.userinterface.security.springboot.securitycontrollers.SecureController;
import org.odpi.openmetadata.viewservices.subjectarea.ffdc.handlers.ErrorHandler;
import org.odpi.openmetadata.viewservices.subjectarea.services.SubjectAreaViewTermRESTServices;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.viewervices.subject-area.server  implementation of the Subject Area Open Metadata
 * View Service (OMVS) for terms.  This interface provides glossary authoring interfaces for subject area experts.
 */

@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/subject-area/terms")

@DependsOn("securityConfig")
public class SubjectAreaViewTermRESTResource extends SecureController {

    private SubjectAreaViewTermRESTServices restAPI = new SubjectAreaViewTermRESTServices();
    private static String serviceName = ViewServiceDescription.SUBJECT_AREA.getViewServiceName();


    /**
     * Default constructor
     */
    public SubjectAreaViewTermRESTResource() {
    }


    /**
     * Create a Term. There are specializations of terms that can also be created using this operation.
     * To create a specialization, you should specify a nodeType other than Term in the supplied term.
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     * <li>Taxonomy to create a Taxonomy </li>
     * <li>CanonicalTerm to create a canonical term </li>
     * <li>TaxonomyAndCanonicalTerm to create a term that is both a taxonomy and a canonical glosary </li>
     * <li>Term to create a term that is not a taxonomy or a canonical term</li>
     * </ul>
     *
     * @param serverName   name of the local server.
     * @param suppliedTerm Term to create.
     * @param request      http request that we can get the userId from
     * @return response, when successful contains the created term.
     * when not successful the following Exception responses can occur
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
    public SubjectAreaOMASAPIResponse createTerm(@PathVariable String serverName,
                                                 @RequestBody Term suppliedTerm,
                                                 HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse subjectAreaOMASAPIResponse = null;
        if (userId == null) {
            subjectAreaOMASAPIResponse = ErrorHandler.handleNullUser(
                    "createTerm", serverName, serviceName);
        } else {
            subjectAreaOMASAPIResponse = restAPI.createTerm(userId, serverName, suppliedTerm);
        }

        return subjectAreaOMASAPIResponse;
    }

    /**
     * Get a term.
     *
     * @param serverName local server name
     * @param guid       guid of the term to get
     * @param request    servlet request
     * @return response which when successful contains the term with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException  not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> FunctionNotSupportedException   Function not supported</li>
     * </ul>
     */
    @GetMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse getTerm(@PathVariable String serverName, @PathVariable String guid, HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse subjectAreaOMASAPIResponse = null;
        if (userId == null) {
            subjectAreaOMASAPIResponse = ErrorHandler.handleNullUser(
                    "getTerm", serverName, serviceName);
        } else {
            subjectAreaOMASAPIResponse = restAPI.getTerm(userId, serverName, guid);
        }
        return subjectAreaOMASAPIResponse;
    }

    /**
     * Find Term
     *
     * @param searchCriteria     String expression matching Term property values .
     * @param asOfTime           the terms returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is no limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @param request            servlet request
     * @return A list of terms meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    @GetMapping()
    public SubjectAreaOMASAPIResponse findTerm(
            @PathVariable String serverName,
            @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
            @RequestParam(value = "asOfTime", required = false) Date asOfTime,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sequencingOrder", required = false) SequencingOrder sequencingOrder,
            @RequestParam(value = "SequencingProperty", required = false) String sequencingProperty,
            HttpServletRequest request
    ) {

        String userId = getUser(request);
        SubjectAreaOMASAPIResponse subjectAreaOMASAPIResponse = null;
        if (userId == null) {
            subjectAreaOMASAPIResponse = ErrorHandler.handleNullUser(
                    "findTerm", serverName, serviceName);
        } else {
            subjectAreaOMASAPIResponse = restAPI.findTerm(serverName, userId, asOfTime, searchCriteria, offset, pageSize, sequencingOrder, sequencingProperty);
        }
        return subjectAreaOMASAPIResponse;

    }

    /**
     * Get Term relationships
     *
     * @param serverName         local server name
     * @param guid               guid of the term to get
     * @param guid               guid of the term to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is not limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @param request            servlet request
     * @return a response which when successful contains the term relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    @GetMapping(path = "/{guid}/relationships")
    public SubjectAreaOMASAPIResponse getTermRelationships(
            @PathVariable String serverName,
            @PathVariable String guid,
            @RequestParam(value = "asOfTime", required = false) Date asOfTime,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sequencingOrder", required = false) SequencingOrder sequencingOrder,
            @RequestParam(value = "SequencingProperty", required = false) String sequencingProperty,
            HttpServletRequest request

    ) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse subjectAreaOMASAPIResponse = null;
        if (userId == null) {
            subjectAreaOMASAPIResponse = ErrorHandler.handleNullUser(
                    "getTermRelationships", serverName, serviceName);
        } else {
            subjectAreaOMASAPIResponse = restAPI.getTermRelationships(serverName,
                    userId,
                    guid,
                    asOfTime,
                    offset,
                    pageSize,
                    sequencingOrder,
                    sequencingProperty);
        }
        return subjectAreaOMASAPIResponse;

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
     * @param serverName   local server name
     * @param guid         guid of the term to get
     * @param guid         guid of the term to update
     * @param suppliedTerm term to update
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @param request      servlet request
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
    public SubjectAreaOMASAPIResponse updateTerm(
            @PathVariable String serverName,
            @PathVariable String guid,
            @RequestBody Term suppliedTerm,
            @RequestParam(value = "isReplace", required = false) Boolean isReplace,
            HttpServletRequest request) {

        String userId = getUser(request);
        SubjectAreaOMASAPIResponse subjectAreaOMASAPIResponse = null;
        if (userId == null) {
            subjectAreaOMASAPIResponse = ErrorHandler.handleNullUser(
                    "updateTerm", serverName, serviceName);
        } else {


            subjectAreaOMASAPIResponse = restAPI.updateTerm(userId, serverName, guid, suppliedTerm, isReplace);
        }

        return subjectAreaOMASAPIResponse;

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
     * @param serverName local server name
     * @param guid       guid of the term to get
     * @param guid       guid of the term to be deleted.
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @param request    servlet request
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
    @DeleteMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse deleteTerm(@PathVariable String serverName,
                                                 @PathVariable String guid,
                                                 @RequestParam(value = "isPurge", required = false) Boolean isPurge,
                                                 HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse subjectAreaOMASAPIResponse = null;
        if (userId == null) {
            subjectAreaOMASAPIResponse = ErrorHandler.handleNullUser(
                    "deleteTerm", serverName, serviceName);
        } else {
            subjectAreaOMASAPIResponse = restAPI.deleteTerm(userId, serverName, guid, isPurge);
        }

        return subjectAreaOMASAPIResponse;

    }

    /**
     * Restore a Term
     * <p>
     * Restore allows the deleted Term to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName local server name
     * @param guid       guid of the term to get
     * @param guid       guid of the term to restore
     * @param request    servlet request
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
    @PostMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse restoreTerm(@PathVariable String serverName,
                                                  @PathVariable String guid,
                                                  HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse subjectAreaOMASAPIResponse = null;
        if (userId == null) {
            subjectAreaOMASAPIResponse = ErrorHandler.handleNullUser(
                    "restoreTerm", serverName, serviceName);
        } else {
            subjectAreaOMASAPIResponse = restAPI.restoreTerm(userId, serverName, guid);
        }

        return subjectAreaOMASAPIResponse;
    }
}

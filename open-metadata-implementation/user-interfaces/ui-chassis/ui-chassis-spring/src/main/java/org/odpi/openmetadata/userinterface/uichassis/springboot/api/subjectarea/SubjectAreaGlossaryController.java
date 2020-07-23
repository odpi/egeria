/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.subjectarea;


import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.glossaries.SubjectAreaGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.SecureController;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectAreaDefinition Open Metadata
 * Assess Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("/api/subject-area/glossaries")
@DependsOn("securityConfig")
public class SubjectAreaGlossaryController extends SecureController {
    private static String className = SubjectAreaGlossaryController.class.getName();
    private final SubjectAreaGlossary subjectAreaGlossary;

    /**
     * Default constructor
     * @param subjectArea main client object for the Subject Area OMAS
     */
    public SubjectAreaGlossaryController(SubjectArea subjectArea) {
        this.subjectAreaGlossary = subjectArea.getSubjectAreaGlossary();
    }

    /**
     * Create a Glossary. There are specializations of glossaries that can also be created using this operation.
     * To create a specialization, you should specify a nodeType other than Glossary in the supplied glossary.
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     *     <li>Taxonomy to create a Taxonomy </li>
     *     <li>CanonicalGlossary to create a canonical glossary </li>
     *     <li>TaxonomyAndCanonicalGlossary to create a glossary that is both a taxonomy and a canonical glosary </li>
     *     <li>Glossary to create a glossary that is not a taxonomy or a canonical glossary</li>
     * </ul>
     * @param suppliedGlossary Glossary to create
     * @param request servlet request
     * @return response, when successful contains the created glossary.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping
    public SubjectAreaOMASAPIResponse<Glossary> createGlossary(@RequestBody Glossary suppliedGlossary, HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();
        try {
            Glossary glossary = this.subjectAreaGlossary.glossary().create(userId, suppliedGlossary);
            response.addResult(glossary);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;
    }

    /**
     * Get a glossary.
     * @param guid guid of the glossary to get
     * @param request servlet request
     * @return response which when successful contains the glossary with the requested guid
     *  when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/{guid}")
    public  SubjectAreaOMASAPIResponse<Glossary> getGlossary(@PathVariable String guid,HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();
        try {
            Glossary glossary = this.subjectAreaGlossary.glossary().getByGUID(userId,guid);
            response.addResult(glossary);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;
    }
    /**
     * Find Glossary
     *
     * @param searchCriteria String expression matching Glossary property values .
     * @param asOfTime the glossaries returned as they were at this time. null indicates at the current time.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is no limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @param request servlet request
     * @return A list of glossaries meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping
    public  SubjectAreaOMASAPIResponse<Glossary> findGlossary(
                                                @RequestParam(value = "searchCriteria", required=false) String searchCriteria,
                                                @RequestParam(value = "asOfTime", required=false) Date asOfTime,
                                                @RequestParam(value = "offset", required = false, defaultValue = PAGE_OFFSET_DEFAULT_VALUE) Integer offset,
                                                @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE_DEFAULT_VALUE) Integer pageSize,
                                                @RequestParam(value = "sequencingOrder", required=false) SequencingOrder sequencingOrder,
                                                @RequestParam(value = "SequencingProperty", required=false) String sequencingProperty,
                                                HttpServletRequest request
    )  {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();
        try {
            FindRequest findRequest = new FindRequest();
            findRequest.setSearchCriteria(searchCriteria);
            findRequest.setAsOfTime(asOfTime);
            findRequest.setOffset(offset);
            findRequest.setPageSize(pageSize);
            findRequest.setSequencingOrder(sequencingOrder);
            findRequest.setSequencingProperty(sequencingProperty);

            List<Glossary> glossaries = this.subjectAreaGlossary.glossary().find(userId, findRequest);
            response.addAllResults(glossaries);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;
    }
    /**
     * Get Glossary relationships
     *
     * @param guid   guid of the glossary to get
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is not limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @param request servlet request
     * @return a response which when successful contains the glossary relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/{guid}/relationships")
    public  SubjectAreaOMASAPIResponse<Line> getGlossaryRelationships(
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

            List<Line> lines = this.subjectAreaGlossary.glossary().getRelationships(userId,guid, findRequest);
            response.addAllResults(lines);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;

    }

    /**
     * Update a Glossary
     * <p>
     * If the caller has chosen to incorporate the glossary name in their Glossary Terms or Categories qualified name, renaming the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * If the caller has chosen to incorporate the glossary qualifiedName in their Glossary Terms or Categories qualified name, changing the qualified name of the glossary will cause those
     * qualified names to mismatch the Glossary name.
     * Status is not updated using this call.
     *
     * @param guid             guid of the glossary to update
     * @param glossary         glossary to update
     * @param isReplace flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @param request servlet request
     * @return a response which when successful contains the updated glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/{guid}")
    public  SubjectAreaOMASAPIResponse<Glossary> updateGlossary(
                                                      @PathVariable String guid,
                                                      @RequestBody Glossary glossary,
                                                      @RequestParam(value = "isReplace", required=false, defaultValue = "false") Boolean isReplace,
                                                      HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();
        try {
            Glossary updatedGlossary;
            if (isReplace) {
                updatedGlossary = this.subjectAreaGlossary.glossary().replace(userId, guid, glossary);
            } else {
                updatedGlossary = this.subjectAreaGlossary.glossary().update(userId, guid, glossary);
            }
            response.addResult(updatedGlossary);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;
    }

    /**
     * Delete a Glossary instance
     * <p>
     * The deletion of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the glossary instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the glossary will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param guid    guid of the glossary to be deleted.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @param request servlet request
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping(path = "/{guid}")
    public  SubjectAreaOMASAPIResponse<Glossary> deleteGlossary(@PathVariable String guid,
                                                                @RequestParam(value = "isPurge", required=false, defaultValue = "false") Boolean isPurge,
                                                                HttpServletRequest request)  {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();
        try {
            if (isPurge) {
                subjectAreaGlossary.glossary().purge(userId, guid);
            } else {
                subjectAreaGlossary.glossary().delete(userId, guid);
            }

        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;
    }
    /**
     * Restore a Glossary
     *
     * Restore allows the deleted Glossary to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param guid       guid of the glossary to restore
     * @param request servlet request
     * @return response which when successful contains the restored glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse<Glossary> restoreGlossary(@PathVariable String guid,HttpServletRequest request)
    {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Glossary> response = new SubjectAreaOMASAPIResponse<>();
        try {
            Glossary glossary = this.subjectAreaGlossary.glossary().restore(userId,guid);
            response.addResult(glossary);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;
    }
}
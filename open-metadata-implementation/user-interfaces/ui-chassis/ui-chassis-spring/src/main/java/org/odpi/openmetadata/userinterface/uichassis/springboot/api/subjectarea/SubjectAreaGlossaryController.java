/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.subjectarea;


import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.utils.DetectUtils;

import org.odpi.openmetadata.userinterface.uichassis.springboot.api.SecureController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class SubjectAreaGlossaryController extends SecureController
{
    private final SubjectArea subjectArea;
    private static String className = SubjectAreaGlossaryController.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger(className);
    private final SubjectAreaGlossary subjectAreaGlossary;

    /**
     * Default constructor
     * @param subjectArea main client object for the Subject Area OMAS
     */
    public SubjectAreaGlossaryController(SubjectArea subjectArea) {

        this.subjectArea = subjectArea;
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
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    @PostMapping()
    public SubjectAreaOMASAPIResponse createGlossary(@RequestBody Glossary suppliedGlossary, HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse response=null;
        try {
            Glossary glossary = this.subjectAreaGlossary.createGlossary(userId, suppliedGlossary);
            GlossaryResponse glossaryResponse = new GlossaryResponse();
            glossaryResponse.setGlossary(glossary);
            response = glossaryResponse;
        } catch (SubjectAreaCheckedException e) {
            response = DetectUtils.getResponseFromException(e);
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
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException  not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> FunctionNotSupportedException   Function not supported</li>
     * </ul>
     */
    @GetMapping( path = "/{guid}")
    public  SubjectAreaOMASAPIResponse getGlossary(@PathVariable String guid,HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse response=null;
        try {
            Glossary glossary = this.subjectAreaGlossary.getGlossaryByGuid(userId,guid);
            GlossaryResponse glossaryResponse = new GlossaryResponse();
            glossaryResponse.setGlossary(glossary);
            response = glossaryResponse;
        } catch (SubjectAreaCheckedException e) {
            response = DetectUtils.getResponseFromException(e);
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
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    @GetMapping()
    public  SubjectAreaOMASAPIResponse findGlossary(
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

            if (offset == null) {
                offset = new Integer(0);
            }
            if (pageSize == null) {
               pageSize = new Integer(0);
            }
            List<Glossary> glossaries = this.subjectAreaGlossary.findGlossary(userId, searchCriteria, asOfTime, offset,pageSize, sequencingOrder, sequencingProperty);
            GlossariesResponse glossariesResponse = new GlossariesResponse();
            glossariesResponse.setGlossaries(glossaries);
            response = glossariesResponse;
        } catch (SubjectAreaCheckedException e) {
            response = DetectUtils.getResponseFromException(e);
        }
        return  response;
    }
    /*
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
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    @GetMapping( path = "/{guid}/relationships")
    public  SubjectAreaOMASAPIResponse getGlossaryRelationships(
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
            List<Line> lines = this.subjectAreaGlossary.getGlossaryRelationships(userId,guid,asOfTime,offset,pageSize,sequencingOrder,sequencingProperty);
            LinesResponse linesResponse = new LinesResponse();
            linesResponse.setLines(lines);
            response = linesResponse;
        } catch (SubjectAreaCheckedException e) {
            response = DetectUtils.getResponseFromException(e);
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
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    @PutMapping( path = "/{guid}")
    public  SubjectAreaOMASAPIResponse updateGlossary(
                                                      @PathVariable String guid,
                                                      @RequestBody Glossary glossary,
                                                      @RequestParam(value = "isReplace", required=false) Boolean isReplace,
                                                      HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse response=null;
        try {
            Glossary updatedGlossary;
            if (isReplace == null){
                isReplace = false;
            }
            if (isReplace) {
                updatedGlossary = this.subjectAreaGlossary.replaceGlossary(userId, guid, glossary);
            } else {
                updatedGlossary = this.subjectAreaGlossary.updateGlossary(userId, guid, glossary);
            }
            GlossaryResponse glossaryResponse = new GlossaryResponse();
            glossaryResponse.setGlossary(updatedGlossary);
            response = glossaryResponse;
        } catch (SubjectAreaCheckedException e) {
            response = DetectUtils.getResponseFromException(e);
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
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the glossary was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the glossary was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/{guid}")
    public  SubjectAreaOMASAPIResponse deleteGlossary(@PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge, HttpServletRequest request)  {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse response=null;
        try {
            if (isPurge) {
                this.subjectAreaGlossary.purgeGlossary(userId, guid);
                response = new VoidResponse();
            } else {
                Glossary glossary = this.subjectAreaGlossary.deleteGlossary(userId, guid);
                GlossaryResponse glossaryResponse = new GlossaryResponse();
                glossaryResponse.setGlossary(glossary);
                response = glossaryResponse;
            }

        } catch (SubjectAreaCheckedException e) {
            response = DetectUtils.getResponseFromException(e);
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
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping( path = "/{guid}")
    public SubjectAreaOMASAPIResponse restoreGlossary(@PathVariable String guid,HttpServletRequest request)
    {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse response=null;
        try {
            Glossary glossary = this.subjectAreaGlossary.restoreGlossary(userId,guid);
            GlossaryResponse glossaryResponse = new GlossaryResponse();
            glossaryResponse.setGlossary(glossary);
            response = glossaryResponse;
        } catch (SubjectAreaCheckedException e) {
            response = DetectUtils.getResponseFromException(e);
        }
        return  response;
    }
}

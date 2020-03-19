/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria term. */
package org.odpi.openmetadata.viewservices.glossaryauthor.server;


import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.viewservices.glossaryauthor.services.GlossaryAuthorViewTermRESTServices;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * The GlossaryAuthorRESTServicesInstance provides the org.odpi.openmetadata.viewervices.glossaryauthor.server implementation of the Glossary Author Open Metadata
 * View Service (OMVS) for terms.  This interface provides term authoring interfaces for subject area experts.
 */

@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/glossary-author/users/{userId}/terms")

public class GlossaryAuthorViewTermRESTResource {

    private GlossaryAuthorViewTermRESTServices restAPI = new GlossaryAuthorViewTermRESTServices();


    /**
     * Default constructor
     */
    public GlossaryAuthorViewTermRESTResource() {
    }


    /**
     * Create a Term.
     * <p>
     * Terms with the same name can be confusing. Best practise is to create terms that have unique names.
     * This Create call does not police that Term names are unique. So it is possible to create terms with the same name as each other.
     *
     * @param serverName   name of the local server.
     * @param suppliedTerm Term to create.
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
    public SubjectAreaOMASAPIResponse createTerm(@PathVariable String serverName, @PathVariable String userId,
                                                 @RequestBody Term suppliedTerm) {
        return restAPI.createTerm(serverName, userId, suppliedTerm);

    }

    /**
     * Get a term.
     *
     * @param serverName local UI server name
     * @param guid       guid of the term to get
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
    public SubjectAreaOMASAPIResponse getTerm(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid) {
        return restAPI.getTerm(serverName, userId, guid);

    }

    /**
     * Find Term
     *
     * @param serverName         local UI server name
     * @param searchCriteria     String expression matching Term property values .
     * @param asOfTime           the terms returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is no limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
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
            @PathVariable String serverName, @PathVariable String userId,
            @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
            @RequestParam(value = "asOfTime", required = false) Date asOfTime,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sequencingOrder", required = false) SequencingOrder sequencingOrder,
            @RequestParam(value = "SequencingProperty", required = false) String sequencingProperty
                                              ) {
        return restAPI.findTerm(serverName, userId, asOfTime, searchCriteria, offset, pageSize, sequencingOrder, sequencingProperty);
    }

    /**
     * Get Term relationships
     *
     * @param serverName         local UI server name
     * @param guid               guid of the term to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is not limit to the page size
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
    @GetMapping(path = "/{guid}/relationships")
    public SubjectAreaOMASAPIResponse getTermRelationships(
            @PathVariable String serverName, @PathVariable String userId,
            @PathVariable String guid,
            @RequestParam(value = "asOfTime", required = false) Date asOfTime,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sequencingOrder", required = false) SequencingOrder sequencingOrder,
            @RequestParam(value = "SequencingProperty", required = false) String sequencingProperty

                                                          ) {
        return restAPI.getTermRelationships(serverName,
                                            userId,
                                            guid,
                                            asOfTime,
                                            offset,
                                            pageSize,
                                            sequencingOrder,
                                            sequencingProperty);

    }

    /**
     * Update a Term
     * <p>
     * Status is not updated using this call.
     *
     * @param serverName   local UI server name
     * @param guid         guid of the term to update
     * @param suppliedTerm term to update
     * @param isReplace    flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    @PutMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse updateTerm(
            @PathVariable String serverName, @PathVariable String userId,
            @PathVariable String guid,
            @RequestBody Term suppliedTerm,
            @RequestParam(value = "isReplace", required = false) Boolean isReplace) {

        return restAPI.updateTerm(serverName, userId, guid, suppliedTerm, isReplace);


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
     * @param serverName local UI server name
     * @param guid       guid of the term to be deleted.
     * @param isPurge    true indicates a hard delete, false is a soft delete.
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
    public SubjectAreaOMASAPIResponse deleteTerm(@PathVariable String serverName, @PathVariable String userId,
                                                 @PathVariable String guid,
                                                 @RequestParam(value = "isPurge", required = false) Boolean isPurge,
                                                 HttpServletRequest request) {
        return restAPI.deleteTerm(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a Term
     * <p>
     * Restore allows the deleted Term to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName local UI server name
     * @param guid       guid of the term to restore
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
    public SubjectAreaOMASAPIResponse restoreTerm(@PathVariable String serverName, @PathVariable String userId,
                                                  @PathVariable String guid,
                                                  HttpServletRequest request) {
        return restAPI.restoreTerm(serverName, userId, guid);

    }
}

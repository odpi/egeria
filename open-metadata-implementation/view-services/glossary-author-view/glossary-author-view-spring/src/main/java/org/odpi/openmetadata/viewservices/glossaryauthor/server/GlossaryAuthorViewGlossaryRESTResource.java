/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.server;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.viewservices.glossaryauthor.services.GlossaryAuthorViewGlossaryRESTServices;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

/**
 * The GlossaryAuthorRESTServicesInstance provides the org.odpi.openmetadata.viewservices.glossaryauthor.server  implementation of the Glossary Author Open Metadata
 * View Service (OMVS) for glossaries.  This interface provides glossary authoring interfaces for subject area experts.
 */

@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/glossary-author/users/{userId}/glossaries")

@Tag(name="Glossary Author OMVS", description="Develop a definition of a subject area by authoring glossaries, including terms for use by a user interface.", externalDocs=@ExternalDocumentation(description="Glossary Author View Services (OMVS)",url="https://egeria.odpi.org/open-metadata-implementation/view-services/glossary-author-view/"))

public class GlossaryAuthorViewGlossaryRESTResource {

    private GlossaryAuthorViewGlossaryRESTServices restAPI = new GlossaryAuthorViewGlossaryRESTServices();


    /**
     * Default constructor
     */
    public GlossaryAuthorViewGlossaryRESTResource() {
    }

    /**
     * Create a Glossary. There are specializations of glossaries that can also be created using this operation.
     * To create a specialization, you should specify a nodeType other than Glossary in the supplied glossary.
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     * <li>Taxonomy to create a Taxonomy </li>
     * <li>CanonicalGlossary to create a canonical glossary </li>
     * <li>TaxonomyAndCanonicalGlossary to create a glossary that is both a taxonomy and a canonical glosary </li>
     * <li>Glossary to create a glossary that is not a taxonomy or a canonical glossary</li>
     * </ul>
     *
     * @param serverName       name of the local server.
     * @param userId           userid
     * @param suppliedGlossary Glossary to create.
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
    public SubjectAreaOMASAPIResponse createGlossary(@PathVariable String serverName,
                                                     @PathVariable String userId,
                                                     @RequestBody Glossary suppliedGlossary) {
        return restAPI.createGlossary(serverName, userId, suppliedGlossary);

    }

    /**
     * Get a glossary.
     *
     * @param serverName local UI server name
     * @param userId     userid
     * @param guid       guid of the glossary to get
     * @return response which when successful contains the glossary with the requested guid
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
    public SubjectAreaOMASAPIResponse getGlossary(@PathVariable String serverName,
                                                  @PathVariable String userId,
                                                  @PathVariable String guid) {
        return restAPI.getGlossary(serverName, userId, guid);

    }

    /**
     * Find Glossary
     *
     * @param serverName         local UI server name
     * @param userId             userid
     * @param searchCriteria     String expression matching Glossary property values .
     * @param asOfTime           the glossaries returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is no limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
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
    public SubjectAreaOMASAPIResponse findGlossary(
            @PathVariable String serverName,
            @PathVariable String userId,
            @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
            @RequestParam(value = "asOfTime", required = false) Date asOfTime,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sequencingOrder", required = false) SequencingOrder sequencingOrder,
            @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty
                                                  ) {
        return restAPI.findGlossary(serverName, userId, asOfTime, searchCriteria, offset, pageSize, sequencingOrder, sequencingProperty);
    }

    /**
     * Get Glossary relationships
     *
     * @param serverName         local UI server name
     * @param userId             userid
     * @param guid               guid of the glossary to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is not limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return a response which when successful contains the glossary relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    @GetMapping(path = "/{guid}/relationships")
    public SubjectAreaOMASAPIResponse getGlossaryRelationships(
            @PathVariable String serverName,
            @PathVariable String userId,
            @PathVariable String guid,
            @RequestParam(value = "asOfTime", required = false) Date asOfTime,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sequencingOrder", required = false) SequencingOrder sequencingOrder,
            @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty

                                                              ) {
        return restAPI.getGlossaryRelationships(serverName,
                                                userId,
                                                guid,
                                                asOfTime,
                                                offset,
                                                pageSize,
                                                sequencingOrder,
                                                sequencingProperty);

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
     * @param serverName       local UI server name
     * @param userId           userid
     * @param guid             guid of the glossary to update
     * @param suppliedGlossary glossary to update
     * @param isReplace        flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    @PutMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse updateGlossary(
            @PathVariable String serverName, @PathVariable String userId,
            @PathVariable String guid,
            @RequestBody Glossary suppliedGlossary,
            @RequestParam(value = "isReplace", required = false) Boolean isReplace) {

        return restAPI.updateGlossary(serverName, userId, guid, suppliedGlossary, isReplace);


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
     * @param serverName local UI server name
     * @param userId     userid
     * @param guid       guid of the glossary to be deleted.
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the glossary was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the glossary was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse deleteGlossary(@PathVariable String serverName,
                                                     @PathVariable String userId,
                                                     @PathVariable String guid,
                                                     @RequestParam(value = "isPurge", required = false) Boolean isPurge
                                                     ) {
        return restAPI.deleteGlossary(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a Glossary
     * <p>
     * Restore allows the deleted Glossary to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName local UI server name
     * @param userId     userid
     * @param guid       guid of the glossary to restore
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
    @PostMapping(path = "/{guid}")
    public SubjectAreaOMASAPIResponse restoreGlossary(@PathVariable String serverName,
                                                      @PathVariable String userId,
                                                      @PathVariable String guid) {
        return restAPI.restoreGlossary(serverName, userId, guid);

    }
}

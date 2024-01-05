/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaGlossaryRESTServices;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/subject-area")
@Tag(name = "Metadata Access Server: Subject Area OMAS", description = "The Subject Area OMAS supports subject matter experts who are documenting their knowledge about a particular subject. This includes glossary terms, reference data, validation rules.",
     externalDocs = @ExternalDocumentation(description = "Further Information",
                                           url = "https://egeria-project.org/services/omas/subject-area/overview/"))
public class SubjectAreaGlossaryRESTResource {
    private final SubjectAreaGlossaryRESTServices restAPI = new SubjectAreaGlossaryRESTServices();

    /**
     * Default constructor
     */
    public SubjectAreaGlossaryRESTResource() {

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
     *
     * @param serverName       serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId           unique identifier for requesting user, under which the request is performed
     * @param suppliedGlossary Glossary to create
     * @return response, when successful contains the created glossary.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/glossaries")
    public SubjectAreaOMASAPIResponse<Glossary> createGlossary(@PathVariable String serverName,
                                                               @PathVariable String userId,
                                                               @RequestBody Glossary suppliedGlossary) {
        return restAPI.createGlossary(serverName, userId, suppliedGlossary);
    }

    /**
     * Get a glossary.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param guid       guid of the glossary to get
     * @return response which when successful contains the glossary with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/glossaries/{guid}")
    public SubjectAreaOMASAPIResponse<Glossary> getGlossary(@PathVariable String serverName,
                                                            @PathVariable String userId,
                                                            @PathVariable String guid) {
        return restAPI.getGlossaryByGuid(serverName, userId, guid);
    }

    /**
     * Find Glossary
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param searchCriteria     String expression matching Glossary property values. If not specified then all glossaries are returned.
     * @param exactValue a boolean, which when set means that only exact matches will be returned, otherwise matches that start with the search criteria will be returned.
     * @param ignoreCase a boolean, which when set means that case will be ignored, if not set that case will be respected
     * @param asOfTime           the glossaries returned as they were at this time. null indicates at the current time.
     * @param startingFrom       the starting element number for this set of results.  This is used when retrieving elements
     * @param pageSize           the maximum number of elements that can be returned on this request.
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of glossaries meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/glossaries")
    public SubjectAreaOMASAPIResponse<Glossary> findGlossary(@PathVariable String serverName, @PathVariable String userId,
                                                             @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
                                                             @RequestParam(value = "exactValue", required = false, defaultValue = "false") Boolean exactValue,
                                                             @RequestParam(value = "ignoreCase", required = false, defaultValue = "true") Boolean ignoreCase,
                                                             @RequestParam(value = "asOfTime", required = false) Date asOfTime,
                                                             @RequestParam(value = "startingFrom", required = false, defaultValue = "0") Integer startingFrom,
                                                             @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                             @RequestParam(value = "sequencingOrder", required = false) String sequencingOrder,
                                                             @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty
    ) {
        return restAPI.findGlossary(serverName, userId, searchCriteria, exactValue, ignoreCase, asOfTime, startingFrom, pageSize, sequencingOrder, sequencingProperty);
    }

    /**
     * Get Glossary relationships
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param guid               guid of the glossary to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param startingFrom       the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return a response which when successful contains the glossary relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/glossaries/{guid}/relationships")
    public SubjectAreaOMASAPIResponse<Relationship> getGlossaryRelationships(@PathVariable String serverName, @PathVariable String userId,
                                                                             @PathVariable String guid,
                                                                             @RequestParam(value = "asOfTime", required = false) Date asOfTime,
                                                                             @RequestParam(value = "startingFrom", required = false, defaultValue = "0") Integer startingFrom,
                                                                             @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                             @RequestParam(value = "sequencingOrder", required = false) SequencingOrder sequencingOrder,
                                                                             @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty
                                                                            ) {
        return restAPI.getGlossaryRelationships(serverName, userId, guid, asOfTime, startingFrom, pageSize, sequencingOrder, sequencingProperty);
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
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the glossary to update
     * @param glossary   glossary to update
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/glossaries/{guid}")
    public SubjectAreaOMASAPIResponse<Glossary> updateGlossary(@PathVariable String serverName,
                                                               @PathVariable String userId,
                                                               @PathVariable String guid,
                                                               @RequestBody Glossary glossary,
                                                               @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace) {
        return restAPI.updateGlossary(serverName, userId, guid, glossary, isReplace);
    }

    /**
     * Delete a Glossary instance
     * <p>
     * The deletion of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional.
     * <p>
     * A soft delete means that the glossary instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the glossary will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the glossary to be deleted.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * <li> EntityNotDeletedException            a soft delete was issued but the glossary was not deleted.</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/glossaries/{guid}")
    public SubjectAreaOMASAPIResponse<Glossary> deleteGlossary(@PathVariable String serverName,
                                                               @PathVariable String userId,
                                                               @PathVariable String guid){
        return restAPI.deleteGlossary(serverName, userId, guid);
    }

    /**
     * Restore a Glossary
     * <p>
     * Restore allows the deleted Glossary to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the glossary to restore
     * @return response which when successful contains the restored glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/glossaries/{guid}")
    public SubjectAreaOMASAPIResponse<Glossary> restoreGlossary(@PathVariable String serverName,
                                                                @PathVariable String userId,
                                                                @PathVariable String guid) {
        return restAPI.restoreGlossary(serverName, userId, guid);
    }

    /**
     * Get terms that are owned by this glossary. The server has a maximum page size defined, the number of terms returned is limited by that maximum page size.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the category to get terms
     * @param asOfTime   the terms returned as they were at this time. null indicates at the current time.
     * @param searchCriteria String expression matching child Term property values.
     * @param exactValue a boolean, which when set means that only exact matches will be returned, otherwise matches that start with the search criteria will be returned.
     * @param ignoreCase a boolean, which when set means that case will be ignored, if not set that case will be respected
     * @param startingFrom the starting element number for this set of results. This is used when retrieving elements
     * @param pageSize Return the maximum number of elements that can be returned on this request.
     * @return A list of terms owned by the glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     * */
    @GetMapping(path = "/users/{userId}/glossaries/{guid}/terms")
    public SubjectAreaOMASAPIResponse<Term> getGlossaryTerms(@PathVariable String serverName,
                                                             @PathVariable String userId,
                                                             @PathVariable String guid,
                                                             @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
                                                             @RequestParam(value = "exactValue", required = false, defaultValue = "false") Boolean exactValue,
                                                             @RequestParam(value = "ignoreCase", required = false, defaultValue = "true") Boolean ignoreCase,
                                                             @RequestParam(value = "asOfTime", required = false) Date asOfTime,
                                                             @RequestParam(value = "startingFrom", required = false, defaultValue = "0") Integer startingFrom,
                                                             @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                             @RequestParam(value = "sequencingOrder", required = false) String sequencingOrder,
                                                             @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty) {
        return restAPI.getGlossaryTerms(serverName, userId, guid, searchCriteria, exactValue, ignoreCase,asOfTime, startingFrom, pageSize, sequencingOrder, sequencingProperty);
    }

    /**
     * Get the Categories owned by this glossary. The server has a maximum page size defined, the number of Categories returned is limited by that maximum page size.
     *
     * @param serverName   serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId       unique identifier for requesting user, under which the request is performed
     * @param guid         guid of the glossary to get terms
     * @param searchCriteria String expression matching child Category property values.
     * @param exactValue a boolean, which when set means that only exact matches will be returned, otherwise matches that start with the search criteria will be returned.
     * @param ignoreCase a boolean, which when set means that case will be ignored, if not set that case will be respected
     * @param asOfTime     the categories returned as they were at this time. null indicates at the current time.
     * @param startingFrom the starting element number for this set of results.  This is used when retrieving elements
     * @param pageSize     the maximum number of elements that can be returned on this request.
     * @param onlyTop      when only the top categories (those categories without parents) are returned.
     * @return A list of categories owned by the glossary
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     * */
    @GetMapping(path = "/users/{userId}/glossaries/{guid}/categories")
    public SubjectAreaOMASAPIResponse<Category> getGlossaryCategories(@PathVariable String serverName,
                                                                      @PathVariable String userId,
                                                                      @PathVariable String guid,
                                                                      @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
                                                                      @RequestParam(value = "exactValue", required = false, defaultValue = "false") Boolean exactValue,
                                                                      @RequestParam(value = "ignoreCase", required = false, defaultValue = "true") Boolean ignoreCase,
                                                                      @RequestParam(value = "asOfTime", required = false) Date asOfTime,
                                                                      @RequestParam(value = "onlyTop", required = false, defaultValue = "true") Boolean onlyTop,
                                                                      @RequestParam(value = "startingFrom", required = false, defaultValue = "0") Integer startingFrom,
                                                                      @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                      @RequestParam(value = "sequencingOrder", required = false) String sequencingOrder,
                                                                      @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty
                                                                      ) {
        return restAPI.getGlossaryCategories(serverName, userId, guid, searchCriteria, exactValue, ignoreCase, asOfTime, onlyTop, startingFrom, pageSize, sequencingOrder, sequencingProperty);
    }
}
/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaCategoryRESTServices;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides category authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/subject-area")
@Tag(name = "Subject Area OMAS", description = "The Subject Area OMAS supports subject matter experts who are documenting their knowledge about a particular subject. This includes glossary terms, reference data, validation rules.", externalDocs = @ExternalDocumentation(description = "Subject Area Open Metadata Access Service (OMAS)", url = "https://egeria.odpi.org/open-metadata-implementation/access-services/subject-area/"))
public class SubjectAreaCategoryRESTResource {
    private final SubjectAreaCategoryRESTServices restAPI = new SubjectAreaCategoryRESTServices();

    /**
     * Default constructor
     */
    public SubjectAreaCategoryRESTResource() {

    }

    /**
     * Create a Category. There is specialization of a Category that can also be created using this operation.
     * To create this specialization, you should specify a nodeType other than Category in the supplied category.
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     *     <li>SubjectAreaDefinition to create a Category that represents a subject area </li>
     *     <li>Category to create a category that is not a subject area</li>
     * </ul>
     * <p>
     * The qualifiedName can be specified and will be honoured. If it is specified then the caller may wish to ensure that it is
     * unique. If this qualifiedName is not specified then one will be generated as GlossaryCategory concatinated with the the guid.
     *
     * <p>
     * Failure to create the Categories classifications, link to its glossary or its icon, results in the create failing and the category being deleted
     *
     * @param serverName       serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId           unique identifier for requesting user, under which the request is performed
     * @param suppliedCategory category to create
     * @return response, when successful contains the created category.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/categories")
    public SubjectAreaOMASAPIResponse<Category> createCategory(@PathVariable String serverName,
                                                               @PathVariable String userId,
                                                               @RequestBody Category suppliedCategory) {
        return restAPI.createCategory(serverName, userId, suppliedCategory);
    }

    /**
     * Get a Category
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the category to get
     * @return response, when successful contains the category associated with the requested guid.
     * n not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/categories/{guid}")
    public SubjectAreaOMASAPIResponse<Category> getCategoryByGuid(@PathVariable String serverName,
                                                                  @PathVariable String userId,
                                                                  @PathVariable String guid) {
        return restAPI.getCategory(serverName, userId, guid);
    }

    /**
     * Find Category
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param searchCriteria     String expression matching Category property values (this does not include the GlossarySummary content).
     * @param exactValue a boolean, which when set means that only exact matches will be returned, otherwise matches that start with the search criteria will be returned.
     * @param ignoreCase a boolean, which when set means that case will be ignored, if not set that case will be respected
     * @param asOfTime           the categories returned as they were at this time. null indicates at the current time.
     * @param startingFrom       the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of Categories meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/categories")
    public SubjectAreaOMASAPIResponse<Category> findCategory(@PathVariable String serverName, @PathVariable String userId,
                                                             @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
                                                             @RequestParam(value = "exactValue", required = false, defaultValue = "false") Boolean exactValue,
                                                             @RequestParam(value = "ignoreCase", required = false, defaultValue = "true") Boolean ignoreCase,
                                                             @RequestParam(value = "asOfTime", required = false) Date asOfTime,
                                                             @RequestParam(value = "startingFrom", required = false, defaultValue = "0") Integer startingFrom,
                                                             @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                             @RequestParam(value = "sequencingOrder", required = false) String sequencingOrder,
                                                             @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty
    ) {
        return restAPI.findCategory(serverName, userId, searchCriteria, exactValue, ignoreCase,asOfTime, startingFrom, pageSize, sequencingOrder, sequencingProperty);
    }

    /**
     * Get Category relationships
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the category to get
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param startingFrom  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return a response which when successful contains the category relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/categories/{guid}/relationships")
    public SubjectAreaOMASAPIResponse<Relationship> getCategoryRelationships(@PathVariable String serverName, @PathVariable String userId,
                                                                             @PathVariable String guid,
                                                                             @RequestParam(value = "asOfTime", required = false) Date asOfTime,
                                                                             @RequestParam(value = "startingFrom", required = false) Integer startingFrom,
                                                                             @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                             @RequestParam(value = "sequencingOrder", required = false) SequencingOrder sequencingOrder,
                                                                             @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty
                                                                            ) {
        return restAPI.getCategoryRelationships(serverName, userId, guid, asOfTime, startingFrom, pageSize, sequencingOrder, sequencingProperty);
    }

    /**
     * Update a Category
     * <p>
     * Status is not updated using this call.
     *
     * @param serverName       serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId           userId under which the request is performed
     * @param guid             guid of the category to update
     * @param suppliedCategory category to be updated
     * @param isReplace        flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated category
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/categories/{guid}")
    public SubjectAreaOMASAPIResponse<Category> updateCategory(@PathVariable String serverName,
                                                               @PathVariable String userId,
                                                               @PathVariable String guid,
                                                               @RequestBody Category suppliedCategory,
                                                               @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace) {
        return restAPI.updateCategory(serverName, userId, guid, suppliedCategory, isReplace);
    }

    /**
     * Delete a Category or SubjectAreaDefinition instance
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional.
     * <p>
     * A soft delete means that the category instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the category will not exist after the operation.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param guid       guid of the category to be deleted.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * <li> EntityNotDeletedException            a soft delete was issued but the category was not deleted.</li>
     * <li> EntityNotPurgedException             a hard delete was issued but the category was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/categories/{guid}")
    public SubjectAreaOMASAPIResponse<Category> deleteCategory(@PathVariable String serverName,
                                                               @PathVariable String userId,
                                                               @PathVariable String guid
                                                              ) {
        return restAPI.deleteCategory(serverName, userId, guid);
    }

    /**
     * Restore a Category
     * <p>
     * Restore allows the deleted Category to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the category to restore
     * @return response which when successful contains the restored category
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/categories/{guid}")
    public SubjectAreaOMASAPIResponse<Category> restoreCategory(@PathVariable String serverName,
                                                                @PathVariable String userId,
                                                                @PathVariable String guid) {
        return restAPI.restoreCategory(serverName, userId, guid);
    }

    /**
     * Get the terms that are categorized by this Category
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the category to get terms
     * @param searchCriteria String expression to match the categorized Term property values.
     * @param exactValue a boolean, which when set means that only exact matches will be returned, otherwise matches that start with the search criteria will be returned.
     * @param ignoreCase a boolean, which when set means that case will be ignored, if not set that case will be respected
     * @param startingFrom the starting element number for this set of results.  This is used when retrieving elements
     * @param pageSize     the maximum number of elements that can be returned on this request.
     * @return A list of terms is categorized by this Category
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/categories/{guid}/terms")
    public SubjectAreaOMASAPIResponse<Term> getCategorizedTerms(@PathVariable String serverName,
                                                             @PathVariable String userId,
                                                             @PathVariable String guid,
                                                             @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
                                                             @RequestParam(value = "exactValue", required = false, defaultValue = "false") Boolean exactValue,
                                                             @RequestParam(value = "ignoreCase", required = false, defaultValue = "true") Boolean ignoreCase,
                                                             @RequestParam(value = "startingFrom", required = false, defaultValue = "0") Integer startingFrom,
                                                             @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return restAPI.getCategorizedTerms(serverName, userId, guid, searchCriteria, exactValue, ignoreCase,startingFrom, pageSize);
    }

    /**
     * Get this Category's child Categories. The server has a maximum page size defined, the number of Categories returned is limited by that maximum page size.
     *
     * @param serverName   serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId       unique identifier for requesting user, under which the request is performed
     * @param guid         guid of the parent category
     * @param searchCriteria String expression matching child Category property values.
     * @param exactValue a boolean, which when set means that only exact matches will be returned, otherwise matches that start with the search criteria will be returned.
     * @param ignoreCase a boolean, which when set means that case will be ignored, if not set that case will be respected
     * @param startingFrom the starting element number for this set of results.  This is used when retrieving elements
     * @param pageSize     the maximum number of elements that can be returned on this request.
     * @return A list of child categories filtered by the search criteria if one is supplied.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     **/
    @GetMapping(path = "/users/{userId}/categories/{guid}/categories")
    public SubjectAreaOMASAPIResponse<Category> getCategoryChildren(@PathVariable String serverName,
                                                                      @PathVariable String userId,
                                                                      @PathVariable String guid,
                                                                      @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
                                                                      @RequestParam(value = "exactValue", required = false, defaultValue = "false") Boolean exactValue,
                                                                      @RequestParam(value = "ignoreCase", required = false, defaultValue = "true") Boolean ignoreCase,
                                                                      @RequestParam(value = "startingFrom", required = false, defaultValue = "0") Integer startingFrom,
                                                                      @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        return restAPI.getCategoryChildren(serverName, userId, guid, searchCriteria, exactValue, ignoreCase,startingFrom, pageSize);
    }
}
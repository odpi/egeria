/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaCategoryRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaRESTServicesInstance;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides category authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/subject-area")

@Tag(name="Subject Area OMAS", description="The Subject Area OMAS supports subject matter experts who are documenting their knowledge about a particular subject. This includes glossary terms, reference data, validation rules.", externalDocs=@ExternalDocumentation(description="Subject Area Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/subject-area/"))

public class SubjectAreaCategoryRESTResource extends SubjectAreaRESTServicesInstance
{
    private SubjectAreaCategoryRESTServices restAPI = new SubjectAreaCategoryRESTServices();
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
     *
     * The qualifiedName can be specified and will be honoured. If it is specified then the caller may wish to ensure that it is
     * unique. If this qualifiedName is not specified then one will be generated as GlossaryCategory concatinated with the the guid.
     *
     * <p>
     * Failure to create the Categories classifications, link to its glossary or its icon, results in the create failing and the category being deleted
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param suppliedCategory category to create
     * @return response, when successful contains the created category.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification</li>
     * <li> StatusNotSupportedException          A status value is not supported</li>
     * </ul>
     */
    @PostMapping( path = "/users/{userId}/categories")
    public SubjectAreaOMASAPIResponse createCategory(@PathVariable String serverName,@PathVariable String userId, @RequestBody Category suppliedCategory) {
        return restAPI.createCategory(serverName,userId,suppliedCategory);
    }

    /**
     * Get a Category
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the category to get

     * @return response, when successful contains the category associated with the requested guid.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/categories/{guid}")
    public  SubjectAreaOMASAPIResponse getCategoryByGuid(@PathVariable String serverName,@PathVariable String userId, @PathVariable String guid) {
       return restAPI.getCategory(serverName,userId,guid);
    }

    /**
     * Find Category
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param searchCriteria String expression matching Category property values (this does not include the GlossarySummary content).
     * @param asOfTime the categories returned as they were at this time. null indicates at the current time.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is no limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of Categories meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a find was issued but the repository does not implement find functionality in some way.</li>
     * </ul>
     */
    @GetMapping( path = "/users/{userId}/categories")
    public  SubjectAreaOMASAPIResponse findTerm(@PathVariable String serverName, @PathVariable String userId,
                                                @RequestParam(value = "searchCriteria", required=false) String searchCriteria,
                                                @RequestParam(value = "asOfTime", required=false) Date asOfTime,
                                                @RequestParam(value = "offset", required=false) Integer offset,
                                                @RequestParam(value = "pageSize", required=false) Integer pageSize,
                                                @RequestParam(value = "sequencingOrder", required=false) SequencingOrder sequencingOrder,
                                                @RequestParam(value = "SequencingProperty", required=false) String sequencingProperty
    )  {
        return restAPI.findCategory(serverName,userId,searchCriteria,asOfTime,offset,pageSize,sequencingOrder,sequencingProperty);
    }
    /*
     * Get Category relationships
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the category to get
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is not limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return a response which when successful contains the category relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */


    @GetMapping( path = "/users/{userId}/categories/{guid}/relationships")
    public  SubjectAreaOMASAPIResponse getCategoryRelationships(@PathVariable String serverName, @PathVariable String userId,
                                                            @PathVariable String guid,
                                                            @RequestParam(value = "asOfTime", required=false) Date asOfTime,
                                                            @RequestParam(value = "offset", required=false) Integer offset,
                                                            @RequestParam(value = "pageSize", required=false) Integer pageSize,
                                                            @RequestParam(value = "sequencingOrder", required=false) SequencingOrder sequencingOrder,
                                                            @RequestParam(value = "SequencingProperty", required=false) String sequencingProperty
    ) {
        return restAPI.getCategoryRelationships(serverName, userId,guid,asOfTime,offset,pageSize,sequencingOrder,sequencingProperty);
    }

    /**
     * Update a Category
     * <p>
     * Status is not updated using this call.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId           userId under which the request is performed
     * @param guid             guid of the category to update
     * @param suppliedCategory     category to be updated
     * @param isReplace flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated category
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    @PutMapping( path = "/users/{userId}/categories/{guid}")
    public SubjectAreaOMASAPIResponse updateCategory(@PathVariable String serverName, @PathVariable String userId, @PathVariable String guid, @RequestBody Category suppliedCategory, @RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateCategory(serverName, userId,guid,suppliedCategory,isReplace);
    }
    /**
     * Delete a Category or SubjectAreaDefinition instance
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the category instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the category will not exist after the operation.
     * when not successful the following Exception responses can occur
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId  userId under which the request is performed
     * @param guid    guid of the category to be deleted.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the category was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the category was not purged</li>
     * </ul>
     */
    @DeleteMapping( path = "/users/{userId}/categories/{guid}")
    public  SubjectAreaOMASAPIResponse deleteCategory(@PathVariable String serverName, @PathVariable String userId,@PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge)  {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteCategory(serverName, userId,guid,isPurge);
    }
    /**
     * Restore a Category
     *
     * Restore allows the deleted Category to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the category to restore
     * @return response which when successful contains the restored category
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping( path = "/users/{userId}/categories/{guid}")
    public SubjectAreaOMASAPIResponse restoreCategory( @PathVariable String serverName,  @PathVariable String userId, @PathVariable String guid)
    {
        return restAPI.restoreCategory(serverName,userId,guid);
    }
}

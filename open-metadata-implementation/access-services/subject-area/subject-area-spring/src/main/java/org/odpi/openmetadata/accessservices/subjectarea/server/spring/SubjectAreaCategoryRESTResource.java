/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.server.spring;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaCategoryRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The SubjectAreaRESTServices provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Assess Service (OMAS).  This interface provides category authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("/open-metadata/access-services/subject-area")
public class SubjectAreaCategoryRESTResource extends SubjectAreaRESTServices{
    private SubjectAreaCategoryRESTServices restAPI = new SubjectAreaCategoryRESTServices();
    /**
     * Default constructor
     */
    public SubjectAreaCategoryRESTResource() {
        //SubjectAreaRESTServices registers this omas.
    }

    /**
     * Create a Category. There is specialization of a Category that can also be created using this operation.
     * To create this specialization, you should specify a nodeType other than Category in the supplied category.
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     *     <li>SubjectArea to create a Category that represents a subject area </li>
     *     <li>Category to create a category that is not a subject area</li>
     * </ul>
     *
     * The qualifiedName can be specified and will be honoured. If it is specified then the caller may wish to ensure that it is
     * unique. If this qualifiedName is not specified then one will be generated as GlossaryCategory concatinated with the the guid.
     *
     * <p>
     * Failure to create the Categories classifications, link to its glossary or its icon, results in the create failing and the category being deleted
     *
     * @param userid unique identifier for requesting user, under which the request is performed
     * @param suppliedCategory category to create
     * @return response, when successful contains the created category.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> StatusNotSupportedException          A status value is not supported</li>
     * </ul>
     */
    @RequestMapping(method = RequestMethod.POST, path = "/users/{userid}/categories")
    public SubjectAreaOMASAPIResponse createGlossary(@PathVariable String userid, @RequestBody Category suppliedCategory) {
        return restAPI.createCategory(userid,suppliedCategory);
    }


    /**
     * Get a Category
     * @param userid unique identifier for requesting user, under which the request is performed
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
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userid}/categories/{guid}")
    public  SubjectAreaOMASAPIResponse getCategoryByGuid(@PathVariable String userid, @PathVariable String guid) {
       return restAPI.getCategory(userid,guid);
    }

    /**
     * Update a Category
     * <p>
     * Status is not updated using this call.
     *
     * @param userid          unique identifier for requesting user, under which the request is performed
     * @param guid             guid of the category to update
     * @param suppliedCategory     category to be updated
     * @param isReplace flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/users/{userid}/categories/{guid}")
    public SubjectAreaOMASAPIResponse updateCategory(@PathVariable String userid,@PathVariable String guid, Category suppliedCategory, @RequestParam(value = "isReplace", required=false) Boolean isReplace) {
        return restAPI.updateCategory(userid,guid,suppliedCategory,isReplace);
    }
    /**
     * Delete a Category instance
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the category instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the category will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param userid  unique identifier for requesting user, under which the request is performed
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
    @RequestMapping(method = RequestMethod.DELETE, path = "/users/{userid}/categories/{guid}")
    public  SubjectAreaOMASAPIResponse deleteCategory(@PathVariable String userid,@PathVariable String guid,@RequestParam(value = "isPurge", required=false) Boolean isPurge)  {
        if (isPurge == null) {
            // default to soft delete if isPurge is not specified.
            isPurge = false;
        }
        return restAPI.deleteCategory(userid,guid,isPurge);
    }
}

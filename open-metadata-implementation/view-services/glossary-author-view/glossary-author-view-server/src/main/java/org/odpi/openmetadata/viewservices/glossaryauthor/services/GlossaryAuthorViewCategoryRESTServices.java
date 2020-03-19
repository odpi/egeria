/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.glossaryauthor.services;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.viewservices.glossaryauthor.handlers.CategoryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * The GlossaryAuthorViewCategoryRESTServices provides the org.odpi.openmetadata.viewservices.glossaryauthor.services implementation of the Glossary Author Open Metadata
 * View Service (OMVS). This interface provides view category authoring interfaces for subject area experts to author categories.
 */

public class GlossaryAuthorViewCategoryRESTServices extends BaseGlossaryAuthorView {

    private static String className = GlossaryAuthorViewCategoryRESTServices.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger(className);

    /**
     * Default constructor
     */
    public GlossaryAuthorViewCategoryRESTServices() {

    }

    /**
     * Create a Category
     *
     * @param serverName name of the local view server.
     * @param userId  userId under which the request is performed
     * @param suppliedCategory Category to create
     * @return the created category.
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse createCategory(String serverName, String userId, Category suppliedCategory) {
        final String methodName = "createCategory";

        RESTCallToken              token    = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog                   auditLog = null;

        // should not be called without a supplied category - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CategoryHandler handler = instanceHandler.getCategoryHandler(serverName, userId, methodName);
            Category createdCategory = handler.createCategory(userId,
                    suppliedCategory);
            response = new CategoryResponse(createdCategory);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a category.
     *
     * @param serverName name of the local view server.
     * @param userId     user identifier
     * @param guid       guid of the category to get
     * @return response which when successful contains the category with the requested guid
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

    public SubjectAreaOMASAPIResponse getCategory(String serverName, String userId, String guid) {
        final String methodName = "getCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CategoryHandler handler = instanceHandler.getCategoryHandler(serverName, userId, methodName);
            Category obtainedCategory = handler.getCategoryByGuid(userId,
                    guid);
            response = new CategoryResponse(obtainedCategory);
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Find Category
     *
     * @param serverName         name of the local view server.
     * @param userId             user identifier
     * @param searchCriteria     String expression matching Category property values .
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
    public SubjectAreaOMASAPIResponse findCategory(
            String serverName,
            String userId,
            Date asOfTime,
            String searchCriteria,
            Integer offset,
            Integer pageSize,
            SequencingOrder sequencingOrder,
            String sequencingProperty
    ) {
        final String methodName = "findCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CategoryHandler handler = instanceHandler.getCategoryHandler(serverName, userId, methodName);
            if (offset == null) {
                offset = new Integer(0);
            }
            if (pageSize == null) {
                pageSize = new Integer(0);
            }
            List<Category> categories = handler.findCategory(
                    userId,
                    searchCriteria,
                    asOfTime,
                    offset,
                    pageSize,
                    sequencingOrder,
                    sequencingProperty);
            CategoriesResponse categoriesResponse = new CategoriesResponse();
            categoriesResponse.setCategories(categories);
            response = categoriesResponse;
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Get Category relationships
     *
     * @param serverName         name of the local view server.
     * @param userId             user identifier
     * @param guid               guid of the category to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is not limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
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
    public SubjectAreaOMASAPIResponse getCategoryRelationships(
            String serverName,
            String userId,
            String guid,
            Date asOfTime,
            Integer offset,
            Integer pageSize,
            SequencingOrder sequencingOrder,
            String sequencingProperty


    ) {
        final String methodName = "getCategoryRelationships";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CategoryHandler handler = instanceHandler.getCategoryHandler(serverName, userId, methodName);
            List<Line> lines =  handler.getCategoryRelationships(userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
            LinesResponse linesResponse = new LinesResponse();
            linesResponse.setLines(lines);
            response = linesResponse;
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a Category
     * <p>
     * Status is not updated using this call.
     *
     * @param serverName         name of the local view server.
     * @param userId             user identifier
     * @param guid       guid of the category to update
     * @param category   category to update
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated category
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse updateCategory(
            String serverName,
            String userId,
            String guid,
            Category category,
            Boolean isReplace
    ) {
        final String methodName = "updateCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied category - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CategoryHandler handler = instanceHandler.getCategoryHandler(serverName, userId, methodName);
            Category updatedCategory;
            if (isReplace == null) {
                isReplace = false;
            }
            if (isReplace) {
                updatedCategory = handler.replaceCategory(userId, guid, category);
            } else {
                updatedCategory = handler.updateCategory(userId, guid, category);
            }
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setCategory(updatedCategory);
            response = categoryResponse;
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a Category instance
     * <p>
     * The deletion of a category is only allowed if there is no category content (i.e. no categories or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the category instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the category will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param serverName         name of the local view server.
     * @param userId             user identifier
     * @param guid       guid of the category to be deleted.
     * @param isPurge    true indicates a hard delete, false is a soft delete.
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
    public SubjectAreaOMASAPIResponse deleteCategory(
            String serverName,
            String userId,
            String guid,
            Boolean isPurge
    ) {

        final String methodName = "deleteCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied category - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CategoryHandler handler = instanceHandler.getCategoryHandler(serverName, userId, methodName);
            if (isPurge == null) {
                // default to soft delete if isPurge is not specified.
                isPurge = false;
            }

            if (isPurge) {
                handler.purgeCategory(userId, guid);
                response = new VoidResponse();
            } else {
                Category category = handler.deleteCategory(userId, guid);
                CategoryResponse categoryResponse = new CategoryResponse();
                categoryResponse.setCategory(category);
                response = categoryResponse;
            }
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore a Category
     * <p>
     * Restore allows the deleted Category to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName         name of the local view server.
     * @param userId             user identifier
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
    public SubjectAreaOMASAPIResponse restoreCategory(
            String serverName,
            String userId,
            String guid) {
        final String methodName = "restoreCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse response = null;
        AuditLog auditLog = null;

        // should not be called without a supplied category - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CategoryHandler handler = instanceHandler.getCategoryHandler(serverName, userId, methodName);
            Category category = handler.restoreCategory(userId, guid);
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setCategory(category);
            response = categoryResponse;
        }  catch (Throwable error) {
            response = getResponseForError(error, auditLog, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.glossaryauthor.services;

import org.odpi.openmetadata.accessservices.subjectarea.client.configs.SubjectAreaConfigClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClients;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.categories.SubjectAreaCategoryClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.Config;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;

import java.util.Date;
import java.util.List;

/**
 * The GlossaryAuthorViewCategoryRESTServices provides the org.odpi.openmetadata.viewservices.glossaryauthor.services implementation of the Glossary Author Open Metadata
 * View Service (OMVS). This interface provides view category authoring interfaces for subject area experts to author categories.
 */

public class GlossaryAuthorViewCategoryRESTServices extends BaseGlossaryAuthorView {
    private static String className = GlossaryAuthorViewCategoryRESTServices.class.getName();

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
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Category> createCategory(String serverName, String userId, Category suppliedCategory) {
        final String methodName = "createCategory";

        RESTCallToken              token    = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog                   auditLog = null;

        // should not be called without a supplied category - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            Category createdCategory = clients.categories().create(userId, suppliedCategory);
            response.addResult(createdCategory);
        }  catch (Exception exception) {
            response =  getResponseForException(exception, auditLog, className, methodName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Category> getCategory(String serverName, String userId, String guid) {
        final String methodName = "getCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            Category obtainedCategory = clients.categories().getByGUID(userId, guid);
            response.addResult(obtainedCategory);
        }  catch (Exception exception) {
            response =  getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Find Category
     *
     * @param serverName         name of the local view server.
     * @param userId             user identifier
     * @param searchCriteria     String expression matching Category property values.
     * @param asOfTime           the glossaries returned as they were at this time. null indicates at the current time.
     * @param startingFrom             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
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
    public SubjectAreaOMASAPIResponse<Category> findCategory(
            String serverName,
            String userId,
            Date asOfTime,
            String searchCriteria,
            Integer startingFrom,
            Integer pageSize,
            SequencingOrder sequencingOrder,
            String sequencingProperty
    ) {
        final String methodName = "findCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        try {
            if (startingFrom == null) {
                startingFrom = 0;
            }
            if (pageSize == null) {
                pageSize = invalidParameterHandler.getMaxPagingSize();
            }
            invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            FindRequest findRequest = new FindRequest();
            findRequest.setSearchCriteria(searchCriteria);
            findRequest.setAsOfTime(asOfTime);
            findRequest.setStartingFrom(startingFrom);
            findRequest.setPageSize(pageSize);
            findRequest.setSequencingOrder(sequencingOrder);
            findRequest.setSequencingProperty(sequencingProperty);
            SubjectAreaConfigClient client = instanceHandler.getSubjectAreaConfigClient(serverName, userId, methodName);
            Config subjectAreaConfig = client.getConfig(userId);
            List<Category> categories = clients.categories().find(userId, findRequest, subjectAreaConfig.getMaxPageSize());
            response.addAllResults(categories);
        }  catch (Exception exception) {
            response =  getResponseForException(exception, auditLog, className, methodName);
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
     * @param startingFrom          the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return a response which when successful contains the category relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Relationship> getCategoryRelationships(
            String serverName,
            String userId,
            String guid,
            Date asOfTime,
            Integer startingFrom,
            Integer pageSize,
            SequencingOrder sequencingOrder,
            String sequencingProperty


                                                                            ) {
        final String methodName = "getCategoryRelationships";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Relationship> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            if (pageSize == null) {
                pageSize = invalidParameterHandler.getMaxPagingSize();
            }
            if (pageSize == null) {
                pageSize = invalidParameterHandler.getMaxPagingSize();
            }
            invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            FindRequest findRequest = new FindRequest();
            findRequest.setAsOfTime(asOfTime);
            findRequest.setStartingFrom(startingFrom);
            findRequest.setPageSize(pageSize);
            findRequest.setSequencingOrder(sequencingOrder);
            findRequest.setSequencingProperty(sequencingProperty);

            List<Relationship> relationships =  clients.categories().getRelationships(userId, guid, findRequest);
            response.addAllResults(relationships);
        }  catch (Exception exception) {
            response =  getResponseForException(exception, auditLog, className, methodName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Category> updateCategory(
            String serverName,
            String userId,
            String guid,
            Category category,
            boolean isReplace
    ) {
        final String methodName = "updateCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied category - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            Category updatedCategory;
            if (isReplace) {
                updatedCategory = clients.categories().replace(userId, guid, category);
            } else {
                updatedCategory = clients.categories().update(userId, guid, category);
            }
            response.addResult(updatedCategory);
        }  catch (Exception exception) {
            response =  getResponseForException(exception, auditLog, className, methodName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Category> deleteCategory(
            String serverName,
            String userId,
            String guid,
            boolean isPurge
    ) {

        final String methodName = "deleteCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied category - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);

            if (isPurge) {
                clients.categories().purge(userId, guid);
            } else {
                clients.categories().delete(userId, guid);
            }
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Category> restoreCategory(
            String serverName,
            String userId,
            String guid) {
        final String methodName = "restoreCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied category - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            Category category = clients.categories().restore(userId, guid);
            response.addResult(category);
        }  catch (Exception exception) {
            response =  getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
    /**
     * Get this Category's child Categories. The server has a maximum page size defined, the number of Categories returned is limited by that maximum page size.
     *
     * @param serverName   serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId       unique identifier for requesting user, under which the request is performed
     * @param guid         guid of the parent category
     * @param searchCriteria String expression matching child Category property values.
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
    public SubjectAreaOMASAPIResponse<Category> getCategoryChildren(String serverName, String userId, String guid, String searchCriteria, Integer startingFrom, Integer pageSize) {

        final String methodName = "getCategoryChildren";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        FindRequest findRequest = new FindRequest();
        if (startingFrom == null) {
            startingFrom = 0;
        }
        if (pageSize == null) {
            pageSize = invalidParameterHandler.getMaxPagingSize();
        }

        try {
            invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);
            findRequest.setPageSize(pageSize);
            findRequest.setStartingFrom(startingFrom);
            findRequest.setSearchCriteria(searchCriteria);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            List<Category> categories = ((SubjectAreaCategoryClient) clients.categories()).getCategoryChildren(userId, guid, findRequest);
            response.addAllResults(categories);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Get the terms that are categorized by this Category
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the category to get terms
     * @param searchCriteria String expression to match the categorized Term property values.
     * @param startingFrom the starting element number for this set of results.  This is used when retrieving elements
     * @param pageSize     the maximum number of elements that can be returned on this request.
     * @return A list of terms is categorized by this Category
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     **/
    public SubjectAreaOMASAPIResponse<Term> getCategorizedTerms(String serverName, String userId, String guid,String searchCriteria, Integer startingFrom, Integer pageSize) {
            final String methodName = "getCategorizedTerms";

            RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
            SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
            AuditLog auditLog = null;
            FindRequest findRequest = new FindRequest();
            if (startingFrom == null) {
                startingFrom = 0;
            }
            if (pageSize == null) {
                pageSize = invalidParameterHandler.getMaxPagingSize();
            }
            try {
                invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);
                findRequest.setPageSize(pageSize);
                findRequest.setStartingFrom(startingFrom);
                findRequest.setSearchCriteria(searchCriteria);
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
                List<Term> terms = ((SubjectAreaCategoryClient)clients.categories()).getTerms(userId, guid, findRequest);
                response.addAllResults(terms);
            } catch (Exception exception) {
                response = getResponseForException(exception, auditLog, className, methodName);
            }
            restCallLogger.logRESTCallReturn(token, response.toString());
            return response;
        }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria term. */
package org.odpi.openmetadata.viewservices.glossaryauthor.services;

import org.odpi.openmetadata.accessservices.subjectarea.client.configs.SubjectAreaConfigClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClients;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.terms.SubjectAreaTermClient;
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
 * The GlossaryAuthorViewTermRESTServices provides the org.odpi.openmetadata.viewservices.glossaryauthor.services implementation of the Glossary Author Open Metadata
 * View Service (OMVS). This interface provides view term authoring interfaces for subject area experts to author terms.
 */

public class GlossaryAuthorViewTermRESTServices extends BaseGlossaryAuthorView {
    private static String className = GlossaryAuthorViewTermRESTServices.class.getName();

    /**
     * Default constructor
     */
    public GlossaryAuthorViewTermRESTServices() {

    }

    /**
     * Create a Term
     *
     * @param serverName name of the local view server.
     * @param userId  userId under which the request is performed
     * @param suppliedTerm Term to create
     * @return the created term.
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Term> createTerm(String serverName, String userId, Term suppliedTerm) {
        final String methodName = "createTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied term - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            Term createdTerm = clients.terms().create(userId, suppliedTerm);
            response.addResult(createdTerm);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a term. The server has a maximum page size defined, the number of categories (a field of Term) returned is limited by that maximum page size.
     *
     * @param serverName name of the local view server.
     * @param userId     user identifier
     * @param guid       guid of the term to get
     * @return response which when successful contains the term with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Term> getTerm(String serverName, String userId, String guid) {
        final String methodName = "getTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            Term obtainedTerm = clients.terms().getByGUID(userId, guid);
            response.addResult(obtainedTerm);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Find Term
     *
     * @param serverName         name of the local view server.
     * @param userId             user identifier
     * @param searchCriteria     String expression matching Term property values .
     * @param exactValue a boolean, which when set means that only exact matches will be returned, otherwise matches that start with the search criteria will be returned.
     * @param ignoreCase a boolean, which when set means that case will be ignored, if not set that case will be respected
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
    public SubjectAreaOMASAPIResponse<Term> findTerm(
            String serverName,
            String userId,
            Date asOfTime,
            String searchCriteria,
            boolean exactValue,
            boolean ignoreCase,
            Integer startingFrom,
            Integer pageSize,
            SequencingOrder sequencingOrder,
            String sequencingProperty
    ) {
        final String methodName = "findTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            if (pageSize == null) {
                pageSize = invalidParameterHandler.getMaxPagingSize();
            }
            if (startingFrom == null) {
                startingFrom = 0;
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
            List<Term> terms = clients.terms().find(userId, findRequest, exactValue, ignoreCase, subjectAreaConfig.getMaxPageSize());
            response.addAllResults(terms);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Get Term relationships
     *
     * @param serverName         name of the local view server.
     * @param userId             user identifier
     * @param guid               guid of the term to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param startingFrom          the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return a response which when successful contains the term relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Relationship> getTermRelationships(
            String serverName,
            String userId,
            String guid,
            Date asOfTime,
            Integer startingFrom,
            Integer pageSize,
            SequencingOrder sequencingOrder,
            String sequencingProperty


                                                                        ) {
        final String methodName = "getTermRelationships";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Relationship> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            if (pageSize == null) {
                pageSize = invalidParameterHandler.getMaxPagingSize();
            }
            if (startingFrom == null) {
                startingFrom = 0;
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

            List<Relationship> relationships =  clients.terms().getRelationships(userId, guid, findRequest);
            response.addAllResults(relationships);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a Term
     * <p>
     * Status is not updated using this call.
     *
     * @param serverName         name of the local view server.
     * @param userId             user identifier
     * @param guid               guid of the term to update
     * @param term               term to update
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Term> updateTerm(
            String serverName,
            String userId,
            String guid,
            Term term,
            boolean isReplace
    ) {
        final String methodName = "updateTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied term - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            Term updatedTerm;

            if (isReplace) {
                updatedTerm = clients.terms().replace(userId, guid, term);
            } else {
                updatedTerm = clients.terms().update(userId, guid, term);
            }
            response.addResult(updatedTerm);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a Term instance
     * <p>
     * The deletion of a term is only allowed if there is no term content (i.e. no terms or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional.
     * <p>
     * A soft delete means that the term instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the term will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param serverName         name of the local view server.
     * @param userId             user identifier
     * @param guid       guid of the term to be deleted.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Term> deleteTerm(
            String serverName,
            String userId,
            String guid
    ) {

        final String methodName = "deleteTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied term - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            clients.terms().delete(userId, guid);

        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore a Term
     * <p>
     * Restore allows the deleted Term to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName         name of the local view server.
     * @param userId             user identifier
     * @param guid       guid of the term to restore
     * @return response which when successful contains the restored term
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Term> restoreTerm(
            String serverName,
            String userId,
            String guid) {
        final String methodName = "restoreTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied term - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            Term term = clients.terms().restore(userId, guid);
            response.addResult(term);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    public SubjectAreaOMASAPIResponse<Category> getCategories(String serverName, String userId, String guid, Integer startingFrom, Integer pageSize) {
        final String methodName = "getCategories";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        FindRequest findRequest = new FindRequest();
        if (pageSize == null) {
            pageSize = invalidParameterHandler.getMaxPagingSize();
        }
        if (startingFrom == null) {
            startingFrom = 0;
        }
        try {
            invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);
            findRequest.setPageSize(pageSize);
            findRequest.setStartingFrom(startingFrom);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            List<Category> categories = ((SubjectAreaTermClient) clients.terms()).getCategories(userId, guid, findRequest);
            response.addAllResults(categories);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}

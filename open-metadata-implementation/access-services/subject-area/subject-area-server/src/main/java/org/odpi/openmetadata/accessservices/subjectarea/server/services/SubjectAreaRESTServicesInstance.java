/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaAuditCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.handlers.SubjectAreaRelationshipHandler;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.IRelationshipMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * SubjectAreaRESTServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class SubjectAreaRESTServicesInstance {
    private static final String className = SubjectAreaRelationshipHandler.class.getName();
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaRESTServicesInstance.class);

    // The OMRSAPIHelper allows the junits to mock out the omrs layer
    protected static SubjectAreaInstanceHandler instanceHandler = new SubjectAreaInstanceHandler();

    /**
     * Default constructor
     */
    public SubjectAreaRESTServicesInstance() {
    }

    /**
     * Create a relationship (relationship), which is a link between two Nodes.
     * <p>
     *
     * @param <R> {@link Relationship} type of object for response
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param restAPIName name of the rest API
     * @param userId     userId under which the request is performed
     * @param clazz       mapper Class
     * @param relationship       relationship to create
     * @return response, when successful contains the created relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function is not supported.
     * </ul>
     */
    protected <R extends Relationship> SubjectAreaOMASAPIResponse<R> createRelationship(String serverName,
                                                                                        String restAPIName,
                                                                                        String userId,
                                                                                        Class<? extends IRelationshipMapper<R>> clazz,
                                                                                        R relationship)
    {

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + restAPIName + ",userId=" + userId + ",className=" + className);
        }
        SubjectAreaOMASAPIResponse<R> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            SubjectAreaRelationshipHandler handler = instanceHandler.getSubjectAreaRelationshipHandler(userId, serverName, restAPIName);
            response = handler.createRelationship(restAPIName, userId, clazz, relationship);

        } catch (OCFCheckedExceptionBase e) {
            response.setExceptionInfo(e, className);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + restAPIName + ",userId=" + userId + ",className=" + className + ", response =" + response);
        }
        return response;
    }

    /**
     * Get a relationship (relationship)
     *
     * @param <L> {@link Relationship} type of object for response
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param restAPIName name of the rest API
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param clazz       mapper Class
     * @param guid       guid of the relationship to get
     * @return response which when successful contains the relationship with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    protected <L extends Relationship> SubjectAreaOMASAPIResponse<L> getRelationship(String serverName,
                                                                                     String restAPIName,
                                                                                     String userId,
                                                                                     Class<? extends IRelationshipMapper<L>> clazz,
                                                                                     String guid)
    {

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + restAPIName + ",userId=" + userId + ",className=" + className);
        }
        SubjectAreaOMASAPIResponse<L> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            SubjectAreaRelationshipHandler handler = instanceHandler.getSubjectAreaRelationshipHandler(userId, serverName, restAPIName);
            response = handler.getRelationship(restAPIName, userId, clazz, guid);

        } catch (OCFCheckedExceptionBase e) {
            response.setExceptionInfo(e, className);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + restAPIName + ",userId=" + userId + ",className=" + className + ", response =" + response);
        }
        return response;
    }

    /**
     * Update a relationship.
     * <p>
     *
     * @param <L> {@link Relationship} type of object for response
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param restAPIName rest api name
     * @param userId     userId under which the request is performed
     * @param guid        unique identifier of the relationship
     * @param clazz       mapper Class
     * @param relationship       the relationship to update
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response,              when successful contains the updated relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    protected <L extends Relationship> SubjectAreaOMASAPIResponse<L> updateRelationship(String serverName,
                                                                                        String restAPIName,
                                                                                        String userId,
                                                                                        String guid,
                                                                                        Class<? extends IRelationshipMapper<L>> clazz,
                                                                                        L relationship,
                                                                                        boolean isReplace)
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + restAPIName + ",userId=" + userId + ",className=" + className);
        }
        SubjectAreaOMASAPIResponse<L> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            SubjectAreaRelationshipHandler handler = instanceHandler.getSubjectAreaRelationshipHandler(userId, serverName, restAPIName);
            response = handler.updateRelationship(restAPIName, userId, guid, clazz, relationship, isReplace);

        } catch (OCFCheckedExceptionBase e) {
            response.setExceptionInfo(e, className);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + restAPIName + ",userId=" + userId + ",className=" + className + ", response =" + response);
        }
        return response;
    }

    /**
     * Delete a relationship (relationship)
     *
     * @param <L> {@link Relationship} type of object for response
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param restAPIName rest API name
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param clazz       mapper Class
     * @param guid       guid of the HAS A relationship to delete
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return response for a soft delete, the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    public <L extends Relationship> SubjectAreaOMASAPIResponse<L> deleteRelationship(String serverName,
                                                                                     String restAPIName,
                                                                                     String userId,
                                                                                     Class<? extends IRelationshipMapper<L>> clazz,
                                                                                     String guid,
                                                                                     Boolean isPurge)
    {

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + restAPIName + ",userId=" + userId + ",className=" + className);
        }
        SubjectAreaOMASAPIResponse<L> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            SubjectAreaRelationshipHandler handler = instanceHandler.getSubjectAreaRelationshipHandler(userId, serverName, restAPIName);
            response = handler.deleteRelationship(restAPIName, userId, clazz, guid, isPurge);

        } catch (OCFCheckedExceptionBase e) {
            response.setExceptionInfo(e, className);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + restAPIName + ",userId=" + userId + ",className=" + className + ", response =" + response);
        }
        return response;
    }

    /**
     * Restore a relationship (relationship).
     * <p>
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param <L> {@link Relationship} type of object for response
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param restAPIName name of the rest API
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param clazz       mapper Class
     * @param guid       guid of the relationship to restore
     * @return response which when successful contains the restored relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the relationship was not purged</li>
     * </ul>
     */
    protected <L extends Relationship> SubjectAreaOMASAPIResponse<L> restoreRelationship(String serverName,
                                                                                         String restAPIName,
                                                                                         String userId,
                                                                                         Class<? extends IRelationshipMapper<L>> clazz,
                                                                                         String guid)
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + restAPIName + ",userId=" + userId + ",className=" + className);
        }
        SubjectAreaOMASAPIResponse<L> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            SubjectAreaRelationshipHandler handler = instanceHandler.getSubjectAreaRelationshipHandler(userId, serverName, restAPIName);
            response = handler.restoreRelationship(restAPIName, userId, clazz, guid);

        } catch (OCFCheckedExceptionBase e) {
            response.setExceptionInfo(e, className);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + restAPIName + ",userId=" + userId + ",className=" + className + ", response =" + response);
        }
        return response;
    }

    protected FindRequest getFindRequest(String searchCriteria, Date asOfTime, Integer startingFrom, Integer pageSize, String sequencingOrderName, String sequencingProperty, Integer handlerMaxPageSize) {
        FindRequest findRequest = new FindRequest();
        SequencingOrder sequencingOrder = SequencingOrder.ANY;
        for (SequencingOrder possibleSequence: SequencingOrder.values()) {
            if (possibleSequence.name().equals(sequencingOrderName)) {
                sequencingOrder=possibleSequence;
            }

        }
        findRequest.setSearchCriteria(searchCriteria);
        findRequest.setAsOfTime(asOfTime);
        findRequest.setStartingFrom(startingFrom);
        if (pageSize == null){
            findRequest.setPageSize(handlerMaxPageSize);
        } else {
            findRequest.setPageSize(pageSize);
        }
        findRequest.setSequencingOrder(sequencingOrder);
        findRequest.setSequencingProperty(sequencingProperty);
        return findRequest;
    }
    /**
     * Get the appropriate response from the supplied Exception
     *
     * @param exception      - supplied exception
     * @param auditLog   - auditlog (may be null if unable to initialize)
     * @param className  - calling class's Name
     * @param restAPIName - calling method's name
     * @return response corresponding to the exception.
     */
    protected <T> SubjectAreaOMASAPIResponse<T> getResponseForException(Exception exception, AuditLog auditLog, String className, String restAPIName) {
        SubjectAreaOMASAPIResponse<T> response = new SubjectAreaOMASAPIResponse<>();
        if (exception instanceof OCFCheckedExceptionBase) {
            response.setExceptionInfo((OCFCheckedExceptionBase) exception, className);
        } else {
            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition();
            messageDefinition.setMessageParameters(exception.getMessage());
            SubjectAreaCheckedException checkedException = new SubjectAreaCheckedException(messageDefinition, className, restAPIName, exception);
            response.setExceptionInfo(checkedException, className);
            if (auditLog != null) {
                auditLog.logException(restAPIName,
                                      SubjectAreaAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(exception.getClass().getName(), restAPIName, exception.getMessage()),
                                      exception);
            }
        }
        return response;
    }
}
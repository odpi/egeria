/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaAuditCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.handlers.SubjectAreaRelationshipHandler;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.ILineMapper;
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
     * Create a Line (relationship), which is a link between two Nodes.
     * <p>
     *
     * @param <L> {@link Line} type of object for response
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param restAPIName name of the rest API
     * @param userId     userId under which the request is performed
     * @param clazz       mapper Class
     * @param line       line to create
     * @return response, when successful contains the created line
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
    protected <L extends Line> SubjectAreaOMASAPIResponse<L> createLine(String serverName,
                                                                        String restAPIName,
                                                                        String userId,
                                                                        Class<? extends ILineMapper<L>> clazz,
                                                                        L line)
    {

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + restAPIName + ",userId=" + userId + ",className=" + className);
        }
        SubjectAreaOMASAPIResponse<L> response = new SubjectAreaOMASAPIResponse<>();
                AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            SubjectAreaRelationshipHandler handler = instanceHandler.getSubjectAreaRelationshipHandler(userId, serverName, restAPIName);
            response = handler.createLine(restAPIName, userId, clazz, line);

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
     * Get a Line (relationship)
     *
     * @param <L> {@link Line} type of object for response
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
    protected <L extends Line> SubjectAreaOMASAPIResponse<L> getLine(String serverName,
                                                                     String restAPIName,
                                                                     String userId,
                                                                     Class<? extends ILineMapper<L>> clazz,
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
            response = handler.getLine(restAPIName, userId, clazz, guid);

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
     * @param <L> {@link Line} type of object for response
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param restAPIName rest api name
     * @param userId     userId under which the request is performed
     * @param guid        unique identifier of the Line
     * @param clazz       mapper Class
     * @param line       the relationship to update
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return response,              when successful contains the updated Line
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
    protected <L extends Line> SubjectAreaOMASAPIResponse<L> updateLine(String serverName,
                                                                        String restAPIName,
                                                                        String userId,
                                                                        String guid,
                                                                        Class<? extends ILineMapper<L>> clazz,
                                                                        L line,
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
            response = handler.updateLine(restAPIName, userId, guid, clazz, line, isReplace);

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
     * Delete a Line (relationship)
     *
     * @param <L> {@link Line} type of object for response
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
    public <L extends Line> SubjectAreaOMASAPIResponse<L> deleteLine(String serverName,
                                                                     String restAPIName,
                                                                     String userId,
                                                                     Class<? extends ILineMapper<L>> clazz,
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
            response = handler.deleteLine(restAPIName, userId, clazz, guid, isPurge);

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
     * Restore a Line (relationship).
     * <p>
     * Restore allows the deleted relationship to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param <L> {@link Line} type of object for response
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
    protected <L extends Line> SubjectAreaOMASAPIResponse<L> restoreLine(String serverName,
                                                                         String restAPIName,
                                                                         String userId,
                                                                         Class<? extends ILineMapper<L>> clazz,
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
            response = handler.restoreLine(restAPIName, userId, clazz, guid);

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
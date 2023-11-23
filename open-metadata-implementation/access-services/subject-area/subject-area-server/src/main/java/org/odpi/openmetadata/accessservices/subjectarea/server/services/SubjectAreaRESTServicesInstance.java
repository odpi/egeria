/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaAuditCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.handlers.SubjectAreaRelationshipHandler;
import org.odpi.openmetadata.accessservices.subjectarea.handlers.SubjectAreaTermHandler;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.RelationshipType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.IRelationshipMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.TermHasARelationshipMapper;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
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

    // The OMRSAPIHelper allows the junits to mock out the omrs layer
    protected static SubjectAreaInstanceHandler instanceHandler = new SubjectAreaInstanceHandler();
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(SubjectAreaRESTServicesInstance.class),
                                                                            instanceHandler.getServiceName());

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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<R> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            SubjectAreaRelationshipHandler handler = instanceHandler.getSubjectAreaRelationshipHandler(userId, serverName, restAPIName);
            response = handler.createRelationship(restAPIName, userId, clazz, relationship);
            if (response.results().size() >0) {
                // if required attempt to create spine objects orientated classifications on the ends
                // typed by should have spine attribute and spine object
                // hasa  should have spine attribute and spine object
                // isatypeof should have spine object and spine object
                // isatypeofdeprecated is deprecated
                // isa is not to do with spine objects.
                 Relationship createdRelationship = response.results().get(0);
                  final String relationshipName = createdRelationship.getName();
                  SubjectAreaTermHandler termHandler = instanceHandler.getSubjectAreaTermHandler(userId, serverName, restAPIName);
                  String end1Guid = createdRelationship.getEnd1().getNodeGuid();
                  String end2Guid = createdRelationship.getEnd2().getNodeGuid();

                  if (relationshipName.equals(RelationshipType.HasA.name()) || relationshipName.equals(RelationshipType.TypedBy.name())) {
                      SubjectAreaOMASAPIResponse<Term> end1TermResponse = termHandler.getTermByGuid(userId, end1Guid);
                      SubjectAreaOMASAPIResponse<Term> end2TermResponse  = termHandler.getTermByGuid(userId, end2Guid);
                      Term end1Term = end1TermResponse.results().get(0);
                      Term end2Term = end2TermResponse.results().get(0);

                      if (!end1Term.isSpineObject()) {
                          end1Term.setSpineObject(true);
                          // ignore the response -as the repository may not set spine objects
                          termHandler.updateTerm(userId,end1Guid, end1Term, handler, false);
                      }
                      if (!end2Term.isSpineAttribute()) {
                          end2Term.setSpineAttribute(true);
                          // ignore the response -as the repository may not set spine attributes
                          termHandler.updateTerm(userId,end2Guid, end2Term, handler,false);
                      }
                  } else  if (relationshipName.equals(RelationshipType.IsATypeOf.name())) {
                      SubjectAreaOMASAPIResponse<Term> end1TermResponse = termHandler.getTermByGuid(userId, end1Guid);
                      SubjectAreaOMASAPIResponse<Term> end2TermResponse  = termHandler.getTermByGuid(userId, end2Guid);
                      Term end1Term = end1TermResponse.results().get(0);
                      Term end2Term = end2TermResponse.results().get(0);
                      if (!end1Term.isSpineObject()) {
                          end1Term.setSpineObject(true);
                          // ignore the response -as the repository may not set spine objects
                          termHandler.updateTerm(userId,end1Guid, end1Term, handler, false);
                      }
                      if (!end2Term.isSpineObject()) {
                          // ignore the response -as the repository may not set spine objects
                          end2Term.setSpineObject(true);
                          termHandler.updateTerm(userId, end2Guid, end2Term, handler,false);
                      }
                  }

            }
        } catch (OCFCheckedExceptionBase e) {
            response.setExceptionInfo(e, className);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
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
        restCallLogger.logRESTCallReturn(token, response.toString());
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
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
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
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a relationship (relationship)
     *
     * @param <L> {@link Relationship} type of object for response
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param restAPIName rest API name
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param clazz      mapper Class
     * @param guid       guid of the HAS A relationship to delete
     * @return response for a soft delete, the response contains the deleted relationship
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata repository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the relationship was not deleted.</li>
     * </ul>
     */
    public <L extends Relationship> SubjectAreaOMASAPIResponse<L> deleteRelationship(String serverName,
                                                                                     String restAPIName,
                                                                                     String userId,
                                                                                     Class<? extends IRelationshipMapper<L>> clazz,
                                                                                     String guid)
    {

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
        SubjectAreaOMASAPIResponse<L> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, restAPIName);
            SubjectAreaRelationshipHandler handler = instanceHandler.getSubjectAreaRelationshipHandler(userId, serverName, restAPIName);
            response = handler.deleteRelationship(restAPIName, userId, clazz, guid);

        } catch (OCFCheckedExceptionBase e) {
            response.setExceptionInfo(e, className);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, restAPIName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
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
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, restAPIName);
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
        restCallLogger.logRESTCallReturn(token, response.toString());
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
     * @param auditLog   - ffdc (may be null if unable to initialize)
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
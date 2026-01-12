/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.timekeeper.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ContextEventHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.*;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The TimeKeeperRESTServices provides the server-side implementation of the Time Keeper Open Metadata
 * View Service (OMVS).  This interface provides access to context events.
 */
public class TimeKeeperRESTServices extends TokenController
{
    private static final TimeKeeperInstanceHandler instanceHandler = new TimeKeeperInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(TimeKeeperRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public TimeKeeperRESTServices()
    {
    }
    

    /**
     * Create a contextEvent.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the contextEvent.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createContextEvent(String                serverName,
                                           String                urlMarker,
                                           NewElementRequestBody requestBody)
    {
        final String methodName = "createContextEvent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ContextEventHandler handler = instanceHandler.getContextEventHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof ContextEventProperties contextEventProperties)
                {
                    response.setGUID(handler.createContextEvent(userId,
                                                                requestBody,
                                                                requestBody.getInitialClassifications(),
                                                                contextEventProperties,
                                                                requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ContextEventProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a new metadata element to represent a contextEvent using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createContextEventFromTemplate(String              serverName,
                                                       String              urlMarker,
                                                       TemplateRequestBody requestBody)
    {
        final String methodName = "createContextEventFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ContextEventHandler handler = instanceHandler.getContextEventHandler(userId, serverName, urlMarker, methodName);

                response.setGUID(handler.createContextEventFromTemplate(userId,
                                                                        requestBody,
                                                                        requestBody.getTemplateGUID(),
                                                                        requestBody.getReplacementProperties(),
                                                                        requestBody.getPlaceholderPropertyValues(),
                                                                        requestBody.getParentRelationshipProperties()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the properties of a contextEvent.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param contextEventGUID unique identifier of the contextEvent (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateContextEvent(String                   serverName,
                                              String                   urlMarker,
                                              String                   contextEventGUID,
                                              UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateContextEvent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ContextEventHandler handler = instanceHandler.getContextEventHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof ContextEventProperties contextEventProperties)
                {
                    response.setFlag(handler.updateContextEvent(userId,
                                                                contextEventGUID,
                                                                requestBody,
                                                                contextEventProperties));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setFlag(handler.updateContextEvent(userId,
                                                                contextEventGUID,
                                                                requestBody,
                                                                null));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ContextEventProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Connect two context events to show that one is dependent on another.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param parentContextEventGUID    unique identifier of the  parent context event.
     * @param childContextEventGUID     unique identifier of the child context event.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkDependentContextEvents(String                     serverName,
                                                   String                     urlMarker,
                                                   String                     parentContextEventGUID,
                                                   String                     childContextEventGUID,
                                                   NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkDependentContextEvents";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ContextEventHandler handler = instanceHandler.getContextEventHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DependentContextEventProperties properties)
                {
                    handler.linkDependentContextEvents(userId,
                                                       parentContextEventGUID,
                                                       childContextEventGUID,
                                                       requestBody,
                                                       properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkDependentContextEvents(userId,
                                                       parentContextEventGUID,
                                                       childContextEventGUID,
                                                       requestBody,
                                                       null);
                }
                else
                {
                    /*
                     * Wrong type of properties ...
                     */
                    restExceptionHandler.handleInvalidPropertiesObject(DependentContextEventProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkDependentContextEvents(userId,
                                                   parentContextEventGUID,
                                                   childContextEventGUID,
                                                   null,
                                                   null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach two dependent context events from one another.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param parentContextEventGUID    unique identifier of the  parent context event.
     * @param childContextEventGUID     unique identifier of the child context event.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachDependentContextEvents(String                        serverName,
                                                     String                        urlMarker,
                                                     String                        parentContextEventGUID,
                                                     String                        childContextEventGUID,
                                                     DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachDependentContextEvents";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ContextEventHandler handler = instanceHandler.getContextEventHandler(userId, serverName, urlMarker, methodName);

            handler.detachDependentContextEvents(userId,
                                                 parentContextEventGUID,
                                                 childContextEventGUID,
                                                 requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Connect two context events to show that one is related to the other.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param contextEventOneGUID    unique identifier of the  context event at end 1
     * @param contextEventTwoGUID     unique identifier of the  context event at end 2
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkRelatedContextEvents(String                  serverName,
                                                 String                  urlMarker,
                                                 String                  contextEventOneGUID,
                                                 String                  contextEventTwoGUID,
                                                 NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkRelatedContextEvents";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ContextEventHandler handler = instanceHandler.getContextEventHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof RelatedContextEventProperties properties)
                {
                    handler.linkRelatedContextEvents(userId,
                                                     contextEventOneGUID,
                                                     contextEventTwoGUID,
                                                     requestBody,
                                                     properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkRelatedContextEvents(userId,
                                                     contextEventOneGUID,
                                                     contextEventTwoGUID,
                                                     requestBody,
                                                     null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(RelatedContextEventProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkRelatedContextEvents(userId,
                                                 contextEventOneGUID,
                                                 contextEventTwoGUID,
                                                 null,
                                                 null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach two context events that are related to one another.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param contextEventOneGUID    unique identifier of the  context event at end 1
     * @param contextEventTwoGUID     unique identifier of the  context event at end 2
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachRelatedContextEvents(String                        serverName,
                                                   String                        urlMarker,
                                                   String                        contextEventOneGUID,
                                                   String                        contextEventTwoGUID,
                                                   DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachRelatedContextEvents";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ContextEventHandler handler = instanceHandler.getContextEventHandler(userId, serverName, urlMarker, methodName);

            handler.detachRelatedContextEvents(userId,
                                               contextEventOneGUID,
                                               contextEventTwoGUID,
                                               requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Connect a context event to an element that provides evidence that this context event is real.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param contextEventGUID    unique identifier of the context event
     * @param evidenceGUID     unique identifier of the element representing the evidence
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkContextEventEvidence(String                  serverName,
                                                 String                  urlMarker,
                                                 String                  contextEventGUID,
                                                 String                  evidenceGUID,
                                                 NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkContextEventEvidence";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ContextEventHandler handler = instanceHandler.getContextEventHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ContextEventEvidenceProperties properties)
                {
                    handler.linkContextEventEvidence(userId,
                                                     contextEventGUID,
                                                     evidenceGUID,
                                                     requestBody,
                                                     properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkContextEventEvidence(userId,
                                                     contextEventGUID,
                                                     evidenceGUID,
                                                     requestBody,
                                                     null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ContextEventEvidenceProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkContextEventEvidence(userId,
                                                 contextEventGUID,
                                                 evidenceGUID,
                                                 null,
                                                 null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach a context event from an element that provides evidence that this context event is real.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param contextEventGUID    unique identifier of the context event
     * @param evidenceGUID     unique identifier of the element representing the evidence
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachContextEventEvidence(String                        serverName,
                                                   String                        urlMarker,
                                                   String                        contextEventGUID,
                                                   String                        evidenceGUID,
                                                   DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachContextEventEvidence";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ContextEventHandler handler = instanceHandler.getContextEventHandler(userId, serverName, urlMarker, methodName);

            handler.detachContextEventEvidence(userId,
                                               contextEventGUID,
                                               evidenceGUID,
                                               requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Connect a context event to an element that is impacted by this event.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param contextEventGUID    unique identifier of the context event
     * @param impactedElementGUID     unique identifier of the element that is impacted by the event
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkContextEventImpact(String                  serverName,
                                               String                  urlMarker,
                                               String                  contextEventGUID,
                                               String                  impactedElementGUID,
                                               NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkContextEventImpact";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ContextEventHandler handler = instanceHandler.getContextEventHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ContextEventImpactProperties properties)
                {
                    handler.linkContextEventImpact(userId,
                                                     contextEventGUID,
                                                     impactedElementGUID,
                                                     requestBody,
                                                     properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkContextEventImpact(userId,
                                                     contextEventGUID,
                                                     impactedElementGUID,
                                                     requestBody,
                                                     null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ContextEventImpactProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkContextEventImpact(userId,
                                                 contextEventGUID,
                                                 impactedElementGUID,
                                                 null,
                                                 null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach a context event from an element that is impacted by the event.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param contextEventGUID    unique identifier of the context event
     * @param impactedElementGUID     unique identifier of the element that is impacted by the event
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachContextEventImpact(String                        serverName,
                                                 String                        urlMarker,
                                                 String                        contextEventGUID,
                                                 String                        impactedElementGUID,
                                                 DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachContextEventImpact";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ContextEventHandler handler = instanceHandler.getContextEventHandler(userId, serverName, urlMarker, methodName);

            handler.detachContextEventImpact(userId,
                                             contextEventGUID,
                                             impactedElementGUID,
                                             requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Connect a context event to an element whose data is impacted by this event.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param timelineAffectedElementGUID     unique identifier of the element whose data is impacted by the context event
     * @param contextEventGUID    unique identifier of the context event
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkContextEventTimelineEffect(String                     serverName,
                                                       String                     urlMarker,
                                                       String                     timelineAffectedElementGUID,
                                                       String                     contextEventGUID,
                                                       NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkContextEventTimelineEffect";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ContextEventHandler handler = instanceHandler.getContextEventHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ContextEventForTimelineEffectsProperties properties)
                {
                    handler.linkContextEventTimelineEffect(userId,
                                                    timelineAffectedElementGUID,
                                                    contextEventGUID,
                                                    requestBody,
                                                    properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkContextEventTimelineEffect(userId,
                                                    timelineAffectedElementGUID,
                                                    contextEventGUID,
                                                    requestBody,
                                                    null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ContextEventForTimelineEffectsProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkContextEventTimelineEffect(userId,
                                                timelineAffectedElementGUID,
                                                contextEventGUID,
                                                null,
                                                null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach a context event from an element whose data is impacted by the event.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param timelineAffectedElementGUID     unique identifier of the element whose data is impacted by the context event
     * @param contextEventGUID    unique identifier of the context event
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachContextEventTimelineEffect(String                        serverName,
                                                    String                        urlMarker,
                                                    String                        timelineAffectedElementGUID,
                                                    String                        contextEventGUID,
                                                    DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachContextEventTimelineEffect";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ContextEventHandler handler = instanceHandler.getContextEventHandler(userId, serverName, urlMarker, methodName);

            handler.detachContextEventTimelineEffect(userId,
                                                timelineAffectedElementGUID,
                                                contextEventGUID,
                                                requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Delete a contextEvent.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param contextEventGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteContextEvent(String                   serverName,
                                           String                   urlMarker,
                                           String                   contextEventGUID,
                                           DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteContextEvent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ContextEventHandler handler = instanceHandler.getContextEventHandler(userId, serverName, urlMarker, methodName);

            handler.deleteContextEvent(userId, contextEventGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of contextEvent metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getContextEventsByName(String            serverName,
                                                                   String            urlMarker,
                                                                   FilterRequestBody requestBody)
    {
        final String methodName = "getContextEventsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ContextEventHandler handler = instanceHandler.getContextEventHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getContextEventsByName(userId, requestBody.getFilter(), requestBody));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of contextEvent metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param contextEventGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getContextEventByGUID(String             serverName,
                                                                 String             urlMarker,
                                                                 String             contextEventGUID,
                                                                 GetRequestBody requestBody)
    {
        final String methodName = "getContextEventByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ContextEventHandler handler = instanceHandler.getContextEventHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getContextEventByGUID(userId, contextEventGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of contextEvent metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findContextEvents(String                  serverName,
                                                              String                  urlMarker,
                                                              SearchStringRequestBody requestBody)
    {
        final String methodName = "findContextEvents";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ContextEventHandler handler = instanceHandler.getContextEventHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findContextEvents(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findContextEvents(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}

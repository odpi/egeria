/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.externallinks.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ExternalIdHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ExternalReferenceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.CitedDocumentLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.MediaReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The ExternalLinksRESTServices provides the server-side implementation of the External Links Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class ExternalLinksRESTServices extends TokenController
{
    private static final ExternalLinksInstanceHandler instanceHandler = new ExternalLinksInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ExternalLinksRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public ExternalLinksRESTServices()
    {
    }


    /**
     * Create an external reference.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the external reference.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createExternalReference(String                serverName,
                                                String                urlMarker,
                                                NewElementRequestBody requestBody)
    {
        final String methodName = "createExternalReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof ExternalReferenceProperties externalReferenceProperties)
                {
                    response.setGUID(handler.createExternalReference(userId,
                                                                     requestBody,
                                                                     requestBody.getInitialClassifications(),
                                                                     externalReferenceProperties,
                                                                     requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ExternalReferenceProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Create a new metadata element to represent an external reference using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createExternalReferenceFromTemplate(String              serverName,
                                                            String              urlMarker,
                                                            TemplateRequestBody requestBody)
    {
        final String methodName = "createExternalReferenceFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

                response.setGUID(handler.createExternalReferenceFromTemplate(userId,
                                                                             requestBody,
                                                                             requestBody.getTemplateGUID(),
                                                                             requestBody.getReplacementProperties(),
                                                                             requestBody.getReplacementClassifications(),
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the properties of an external reference.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param externalReferenceGUID unique identifier of the external reference (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateExternalReference(String                   serverName,
                                                   String                   urlMarker,
                                                   String                   externalReferenceGUID,
                                                   UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateExternalReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof ExternalReferenceProperties externalReferenceProperties)
                {
                    response.setFlag(handler.updateExternalReference(userId,
                                                                     externalReferenceGUID,
                                                                     requestBody,
                                                                     externalReferenceProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ExternalReferenceProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Attach an external reference to an element.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param elementGUID          unique identifier of the element
     * @param externalReferenceGUID          unique identifier of the external reference
     * @param requestBody  description of the relationship.
     *
     * @return unique identifier of the new relationship or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse linkExternalReference(String                     serverName,
                                              String                     urlMarker,
                                              String                     elementGUID,
                                              String                     externalReferenceGUID,
                                              NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkExternalReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ExternalReferenceLinkProperties externalReferenceLinkProperties)
                {
                    response.setGUID(handler.linkExternalReference(userId,
                                                  elementGUID,
                                                  externalReferenceGUID,
                                                  requestBody,
                                                  externalReferenceLinkProperties));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.linkExternalReference(userId,
                                                  elementGUID,
                                                  externalReferenceGUID,
                                                  requestBody,
                                                  null));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ExternalReferenceLinkProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setGUID(handler.linkExternalReference(userId,
                                              elementGUID,
                                              externalReferenceGUID,
                                              null,
                                              null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the properties of a external reference link relationship.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param externalReferenceLinkRelationshipGUID unique identifier of the relationship
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse updateExternalReferenceLink(String                        serverName,
                                                    String                        urlMarker,
                                                    String                        externalReferenceLinkRelationshipGUID,
                                                    UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "updateExternalReferenceLink";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ExternalReferenceLinkProperties properties)
                {
                    handler.updateExternalReferenceLink(userId, externalReferenceLinkRelationshipGUID, requestBody, properties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ExternalReferenceLinkProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove a external reference link relationship.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param externalReferenceLinkRelationshipGUID unique identifier of the relationship
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachExternalReference(String                        serverName,
                                                String                        urlMarker,
                                                String                        externalReferenceLinkRelationshipGUID,
                                                DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachExternalReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

            handler.detachExternalReference(userId, externalReferenceLinkRelationshipGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Attach an external media reference to an element.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param elementGUID          unique identifier of the element
     * @param externalReferenceGUID          unique identifier of the external reference
     * @param requestBody  description of the relationship.
     *
     * @return unique identifier of the new relationship or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse linkMediaReference(String                     serverName,
                                           String                     urlMarker,
                                           String                     elementGUID,
                                           String                     externalReferenceGUID,
                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkMediaReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof MediaReferenceProperties mediaReferenceProperties)
                {
                    response.setGUID(handler.linkMediaReference(userId,
                                               elementGUID,
                                               externalReferenceGUID,
                                               requestBody,
                                               mediaReferenceProperties));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.linkMediaReference(userId,
                                               elementGUID,
                                               externalReferenceGUID,
                                               requestBody,
                                               null));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(MediaReferenceProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setGUID(handler.linkMediaReference(userId,
                                           elementGUID,
                                           externalReferenceGUID,
                                           null,
                                           null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the properties of a media reference relationship.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param mediaReferenceRelationshipGUID unique identifier of the relationship
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse updateMediaReference(String                        serverName,
                                             String                        urlMarker,
                                             String                        mediaReferenceRelationshipGUID,
                                             UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "updateMediaReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof MediaReferenceProperties properties)
                {
                    handler.updateMediaReference(userId, mediaReferenceRelationshipGUID, requestBody, properties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(MediaReferenceProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove a media reference relationship.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param mediaReferenceRelationshipGUID unique identifier of the relationship
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachMediaReference(String                        serverName,
                                             String                        urlMarker,
                                             String                        mediaReferenceRelationshipGUID,
                                             DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachMediaReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

            handler.detachMediaReference(userId, mediaReferenceRelationshipGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }




    /**
     * Attach an element to its external document reference.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param elementGUID          unique identifier of the element
     * @param externalReferenceGUID          unique identifier of the external reference
     * @param requestBody  description of the relationship.
     *
     * @return unique identifier of the new relationship or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse linkCitedDocumentReference(String                     serverName,
                                                   String                     urlMarker,
                                                   String                     elementGUID,
                                                   String                     externalReferenceGUID,
                                                   NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkCitedDocumentReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CitedDocumentLinkProperties citedDocumentLinkProperties)
                {
                    response.setGUID(handler.linkCitedDocumentReference(userId,
                                                       elementGUID,
                                                       externalReferenceGUID,
                                                       requestBody,
                                                       citedDocumentLinkProperties));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.linkCitedDocumentReference(userId,
                                                       elementGUID,
                                                       externalReferenceGUID,
                                                       requestBody,
                                                       null));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CitedDocumentLinkProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setGUID(handler.linkCitedDocumentReference(userId,
                                                   elementGUID,
                                                   externalReferenceGUID,
                                                   null,
                                                   null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the properties of a cited document link relationship.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param citedDocumentLinkRelationshipGUID unique identifier of the relationship
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse updateCitedDocumentReference(String                        serverName,
                                                     String                        urlMarker,
                                                     String                        citedDocumentLinkRelationshipGUID,
                                                     UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "updateCitedDocumentReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CitedDocumentLinkProperties properties)
                {
                    handler.updateCitedDocumentReference(userId, citedDocumentLinkRelationshipGUID, requestBody, properties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CitedDocumentLinkProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove a cited document link relationship.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param citedDocumentLinkRelationshipGUID unique identifier of the relationship
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachCitedDocumentReference(String                        serverName,
                                                     String                        urlMarker,
                                                     String                        citedDocumentLinkRelationshipGUID,
                                                     DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachCitedDocumentReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

            handler.detachCitedDocumentReference(userId, citedDocumentLinkRelationshipGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Delete an external reference.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param externalReferenceGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteExternalReference(String                   serverName,
                                                String                   urlMarker,
                                                String                   externalReferenceGUID,
                                                DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteExternalReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

            handler.deleteExternalReference(userId, externalReferenceGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the list of external reference metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getExternalReferencesByName(String            serverName,
                                                                        String            urlMarker,
                                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getExternalReferencesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getExternalReferencesByName(userId,
                                                                         requestBody.getFilter(),
                                                                         requestBody));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the list of external reference metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param externalReferenceGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getExternalReferenceByGUID(String         serverName,
                                                                      String         urlMarker,
                                                                      String         externalReferenceGUID,
                                                                      GetRequestBody requestBody)
    {
        final String methodName = "getExternalReferenceByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getExternalReferenceByGUID(userId, externalReferenceGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the list of external reference metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findExternalReferences(String                  serverName,
                                                                   String                  urlMarker,
                                                                   SearchStringRequestBody requestBody)
    {
        final String methodName = "findExternalReferences";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findExternalReferences(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findExternalReferences(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /* =====================================================================================================================
     * Work with external identifiers
     */

    /**
     * Add the description of a specific external identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse addExternalIdentifier(String                   serverName,
                                              String                   urlMarker,
                                              String                   elementGUID,
                                              NewExternalIdRequestBody requestBody)
    {
        final String methodName = "addExternalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                ExternalIdHandler handler = instanceHandler.getExternalIdHandler(userId, serverName, urlMarker, methodName);

                NewElementOptions newElementOptions = new NewElementOptions(requestBody);

                newElementOptions.setIsOwnAnchor(false);
                newElementOptions.setAnchorGUID(elementGUID);
                newElementOptions.setParentGUID(elementGUID);
                newElementOptions.setParentAtEnd1(true);
                newElementOptions.setParentRelationshipTypeName(OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName);

                handler.createExternalId(userId,
                                         newElementOptions,
                                         requestBody.getInitialClassifications(),
                                         requestBody.getProperties(),
                                         requestBody.getParentRelationshipProperties());
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Update the description of a specific external identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param externalIdGUID unique identifier (GUID) of the externalId in the open metadata ecosystem
     * @param requestBody unique identifier of this element in the external system plus additional mapping properties
     *
     * @return boolean or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public BooleanResponse updateExternalIdentifier(String                   serverName,
                                                    String                   urlMarker,
                                                    String                   externalIdGUID,
                                                    UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateExternalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getProperties() instanceof ExternalIdProperties externalIdProperties))
            {
                ExternalIdHandler handler = instanceHandler.getExternalIdHandler(userId, serverName, urlMarker, methodName);

                response.setFlag(handler.updateExternalId(userId,
                                                          externalIdGUID,
                                                          requestBody,
                                                          externalIdProperties));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Remove an external identifier from an existing open metadata element.  The open metadata element is not
     * affected.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param externalIdGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse deleteExternalIdentifier(String                   serverName,
                                                 String                   urlMarker,
                                                 String                   externalIdGUID,
                                                 DeleteElementRequestBody requestBody)
    {
        final String methodName = "removeExternalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalIdHandler handler = instanceHandler.getExternalIdHandler(userId, serverName, urlMarker, methodName);

            handler.deleteExternalId(userId, externalIdGUID, requestBody);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param externalIdLinkGUID unique identifier (GUID) of this element in open metadata
     * @param requestBody details of the external identifier and its scope
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse confirmSynchronization(String                        serverName,
                                               String                        urlMarker,
                                               String                        externalIdLinkGUID,
                                               UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "confirmSynchronization";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalIdHandler handler = instanceHandler.getExternalIdHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof ExternalIdLinkProperties externalIdLinkProperties)
                {
                    handler.confirmSynchronization(userId,
                                                   externalIdLinkGUID,
                                                   requestBody,
                                                   externalIdLinkProperties);
                }
                else if (requestBody.getProperties() != null)
                {
                    handler.confirmSynchronization(userId,
                                                   externalIdLinkGUID,
                                                   requestBody,
                                                   (ExternalIdLinkProperties) null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ExternalIdLinkProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Retrieve the list of external id metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getExternalIdsByName(String            serverName,
                                                                 String            urlMarker,
                                                                 FilterRequestBody requestBody)
    {
        final String methodName = "getExternalIdsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalIdHandler handler = instanceHandler.getExternalIdHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getExternalIdsByName(userId,
                                                                  requestBody.getFilter(),
                                                                  requestBody));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the list of external id metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param externalIdGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getExternalIdByGUID(String         serverName,
                                                               String         urlMarker,
                                                               String         externalIdGUID,
                                                               GetRequestBody requestBody)
    {
        final String methodName = "getExternalIdByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalIdHandler handler = instanceHandler.getExternalIdHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getExternalIdByGUID(userId, externalIdGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the list of external id metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findExternalIds(String                  serverName,
                                                            String                  urlMarker,
                                                            SearchStringRequestBody requestBody)
    {
        final String methodName = "findExternalIds";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalIdHandler handler = instanceHandler.getExternalIdHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findExternalIds(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findExternalIds(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /*
     * =====================================================================================================================
     * External identifier relationships
     */

    /**
     * Attach an existing external identifier to the element that it identifies.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element
     * @param externalIdGUID unique identifier of the external identifier
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkExternalIdToElement(String                     serverName,
                                                String                     urlMarker,
                                                String                     elementGUID,
                                                String                     externalIdGUID,
                                                NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkExternalIdToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalIdHandler handler = instanceHandler.getExternalIdHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                handler.linkExternalIdToElement(userId, elementGUID, externalIdGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof ExternalIdLinkProperties properties)
            {
                handler.linkExternalIdToElement(userId, elementGUID, externalIdGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkExternalIdToElement(userId, elementGUID, externalIdGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(ExternalIdLinkProperties.class.getName(), methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Detach an external identifier from the element that it identifies.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element
     * @param externalIdGUID unique identifier of the external identifier
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachExternalIdFromElement(String                        serverName,
                                                    String                        urlMarker,
                                                    String                        elementGUID,
                                                    String                        externalIdGUID,
                                                    DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachExternalIdFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalIdHandler handler = instanceHandler.getExternalIdHandler(userId, serverName, urlMarker, methodName);

            handler.detachExternalIdFromElement(userId, elementGUID, externalIdGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }

    /**
     * Detach an element from its external document reference.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param elementGUID          unique identifier of the element
     * @param externalReferenceGUID          unique identifier of the external reference
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     *
     * This is a multi-link relationship, so this request removes every cited document link relationship
     * between the two elements.  Use the request that takes the relationship's own unique identifier to
     * remove just one of them.
     */
    public VoidResponse detachCitedDocumentReference(String                        serverName,
                                                     String                        urlMarker,
                                                     String                        elementGUID,
                                                     String                        externalReferenceGUID,
                                                     DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachCitedDocumentReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

            handler.detachCitedDocumentReference(userId, elementGUID, externalReferenceGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Detach an external reference from an element.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param elementGUID          unique identifier of the element
     * @param externalReferenceGUID          unique identifier of the external reference
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     *
     * This is a multi-link relationship, so this request removes every external reference link relationship
     * between the two elements.  Use the request that takes the relationship's own unique identifier to
     * remove just one of them.
     */
    public VoidResponse detachExternalReference(String                        serverName,
                                                String                        urlMarker,
                                                String                        elementGUID,
                                                String                        externalReferenceGUID,
                                                DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachExternalReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

            handler.detachExternalReference(userId, elementGUID, externalReferenceGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Detach an external media reference from an element.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param elementGUID          unique identifier of the element
     * @param externalReferenceGUID          unique identifier of the external reference
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     *
     * This is a multi-link relationship, so this request removes every media reference relationship
     * between the two elements.  Use the request that takes the relationship's own unique identifier to
     * remove just one of them.
     */
    public VoidResponse detachMediaReference(String                        serverName,
                                             String                        urlMarker,
                                             String                        elementGUID,
                                             String                        externalReferenceGUID,
                                             DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachMediaReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

            handler.detachMediaReference(userId, elementGUID, externalReferenceGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }
}

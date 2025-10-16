/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.externallinks.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ExternalReferenceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.CitedDocumentLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.MediaReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworkservices.omf.rest.ExternalIdEffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.MetadataCorrelationHeadersResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.UpdateMetadataCorrelatorsRequestBody;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.Date;


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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createExternalReference(String                serverName,
                                                String                urlMarker,
                                                NewElementRequestBody requestBody)
    {
        final String methodName = "createExternalReference";

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createExternalReferenceFromTemplate(String              serverName,
                                                            String              urlMarker,
                                                            TemplateRequestBody requestBody)
    {
        final String methodName = "createExternalReferenceFromTemplate";

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
                ExternalReferenceHandler handler = instanceHandler.getExternalReferenceHandler(userId, serverName, urlMarker, methodName);

                response.setGUID(handler.createExternalReferenceFromTemplate(userId,
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
     * Update the properties of an external reference.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param externalReferenceGUID unique identifier of the external reference (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateExternalReference(String                   serverName,
                                                String                   urlMarker,
                                                String                   externalReferenceGUID,
                                                UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateExternalReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
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
                    handler.updateExternalReference(userId,
                                                    externalReferenceGUID,
                                                    requestBody,
                                                    externalReferenceProperties);
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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkExternalReference(String                     serverName,
                                              String                     urlMarker,
                                              String                     elementGUID,
                                              String                     externalReferenceGUID,
                                              NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkExternalReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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
                if (requestBody.getProperties() instanceof ExternalReferenceLinkProperties externalReferenceLinkProperties)
                {
                    handler.linkExternalReference(userId,
                                                  elementGUID,
                                                  externalReferenceGUID,
                                                  requestBody,
                                                  externalReferenceLinkProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkExternalReference(userId,
                                                  elementGUID,
                                                  externalReferenceGUID,
                                                  requestBody,
                                                  null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ExternalReferenceLinkProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkExternalReference(userId,
                                              elementGUID,
                                              externalReferenceGUID,
                                              metadataSourceOptions,
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachExternalReference(String            serverName,
                                                String            urlMarker,
                                                String            elementGUID,
                                                String            externalReferenceGUID,
                                                DeleteRequestBody requestBody)
    {
        final String methodName = "detachExternalReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkMediaReference(String                     serverName,
                                           String                     urlMarker,
                                           String                     elementGUID,
                                           String                     externalReferenceGUID,
                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkMediaReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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
                if (requestBody.getProperties() instanceof MediaReferenceProperties mediaReferenceProperties)
                {
                    handler.linkMediaReference(userId,
                                               elementGUID,
                                               externalReferenceGUID,
                                               requestBody,
                                               mediaReferenceProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkMediaReference(userId,
                                               elementGUID,
                                               externalReferenceGUID,
                                               requestBody,
                                               null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(MediaReferenceProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkMediaReference(userId,
                                           elementGUID,
                                           externalReferenceGUID,
                                           metadataSourceOptions,
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachMediaReference(String            serverName,
                                             String            urlMarker,
                                             String            elementGUID,
                                             String            externalReferenceGUID,
                                             DeleteRequestBody requestBody)
    {
        final String methodName = "detachMediaReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkCitedDocumentReference(String                  serverName,
                                                   String                  urlMarker,
                                                   String                  elementGUID,
                                                   String                  externalReferenceGUID,
                                                   NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkCitedDocumentReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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
                if (requestBody.getProperties() instanceof CitedDocumentLinkProperties citedDocumentLinkProperties)
                {
                    handler.linkCitedDocumentReference(userId,
                                                       elementGUID,
                                                       externalReferenceGUID,
                                                       requestBody,
                                                       citedDocumentLinkProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkCitedDocumentReference(userId,
                                                       elementGUID,
                                                       externalReferenceGUID,
                                                       requestBody,
                                                       null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CitedDocumentLinkProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkCitedDocumentReference(userId,
                                                   elementGUID,
                                                   externalReferenceGUID,
                                                   metadataSourceOptions,
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachCitedDocumentReference(String                   serverName,
                                                     String                   urlMarker,
                                                     String                   elementGUID,
                                                     String                   externalReferenceGUID,
                                                     DeleteRequestBody requestBody)
    {
        final String methodName = "detachCitedDocumentReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteExternalReference(String                   serverName,
                                                String                   urlMarker,
                                                String                   externalReferenceGUID,
                                                DeleteRequestBody requestBody)
    {
        final String methodName = "deleteExternalReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getExternalReferencesByName(String            serverName,
                                                                        String            urlMarker,
                                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getExternalReferencesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getExternalReferenceByGUID(String             serverName,
                                                                      String             urlMarker,
                                                                      String             externalReferenceGUID,
                                                                      GetRequestBody requestBody)
    {
        final String methodName = "getExternalReferenceByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findExternalReferences(String            serverName,
                                                                   String              urlMarker,
                                                                   SearchStringRequestBody requestBody)
    {
        final String methodName = "findExternalReferences";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse addExternalIdentifier(String                               serverName,
                                              String                urlMarker,
                                              String                               openMetadataElementGUID,
                                              String                               openMetadataElementTypeName,
                                              boolean                              forLineage,
                                              boolean                              forDuplicateProcessing,
                                              UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        final String methodName                      = "addExternalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getMetadataCorrelationProperties() != null))
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, urlMarker, methodName);

                handler.addExternalIdentifier(userId,
                                              requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                              requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                              requestBody.getMetadataCorrelationProperties().getExternalScopeTypeName(),
                                              openMetadataElementGUID,
                                              openMetadataElementTypeName,
                                              requestBody.getMetadataCorrelationProperties(),
                                              requestBody.getEffectiveFrom(),
                                              requestBody.getEffectiveTo(),
                                              forLineage,
                                              forDuplicateProcessing,
                                              requestBody.getEffectiveTime());
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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the description of a specific external identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse updateExternalIdentifier(String                               serverName,
                                                 String                urlMarker,
                                                 String                               openMetadataElementGUID,
                                                 String                               openMetadataElementTypeName,
                                                 boolean                              forLineage,
                                                 boolean                              forDuplicateProcessing,
                                                 UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        final String methodName                      = "updateExternalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getMetadataCorrelationProperties() != null))
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, urlMarker, methodName);

                handler.updateExternalIdentifier(userId,
                                                 requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                 requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                 requestBody.getMetadataCorrelationProperties().getExternalScopeTypeName(),
                                                 openMetadataElementGUID,
                                                 openMetadataElementTypeName,
                                                 requestBody.getMetadataCorrelationProperties(),
                                                 requestBody.getEffectiveFrom(),
                                                 requestBody.getEffectiveTo(),
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 requestBody.getEffectiveTime());
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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * That the external identifier matches the open metadata GUID.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public BooleanResponse validateExternalIdentifier(String                               serverName,
                                                      String                urlMarker,
                                                      String                               openMetadataElementGUID,
                                                      String                               openMetadataElementTypeName,
                                                      boolean                              forLineage,
                                                      boolean                              forDuplicateProcessing,
                                                      UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        final String methodName = "validateExternalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getMetadataCorrelationProperties() != null))
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, urlMarker, methodName);

                if ((requestBody.getMetadataCorrelationProperties().getIdentifier() != null) &&
                        (requestBody.getMetadataCorrelationProperties().getExternalScopeGUID() != null) &&
                        (requestBody.getMetadataCorrelationProperties().getExternalScopeName() != null))
                {
                    response.setFlag(handler.validateExternalIdentifier(userId,
                                                                        requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                                        requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                                        openMetadataElementGUID,
                                                                        openMetadataElementTypeName,
                                                                        requestBody.getMetadataCorrelationProperties().getIdentifier(),
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        requestBody.getEffectiveTime()));
                }
                else
                {
                    response.setFlag(true);
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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove an external identifier from an existing open metadata element.  The open metadata element is not
     * affected.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse removeExternalIdentifier(String                               serverName,
                                                 String                urlMarker,
                                                 String                               openMetadataElementGUID,
                                                 String                               openMetadataElementTypeName,
                                                 boolean                              forLineage,
                                                 boolean                              forDuplicateProcessing,
                                                 UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        final String methodName = "removeExternalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getMetadataCorrelationProperties() != null))
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, urlMarker, methodName);

                handler.removeExternalIdentifier(userId,
                                                 requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                 requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                 openMetadataElementGUID,
                                                 openMetadataElementTypeName,
                                                 requestBody.getMetadataCorrelationProperties().getIdentifier(),
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 requestBody.getEffectiveTime());
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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the scope associated with a collection of external identifiers.  All associated external identifiers are removed too.
     * The linked open metadata elements are not affected.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param externalScopeGUID unique identifier (GUID) of the scope element in the open metadata ecosystem
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse removeExternalScope(String                   serverName,
                                            String                urlMarker,
                                            String                   externalScopeGUID,
                                            boolean                  forLineage,
                                            boolean                  forDuplicateProcessing,
                                            EffectiveTimeRequestBody requestBody)
    {
        final String methodName                      = "removeExternalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, urlMarker, methodName);

                handler.removeExternalScope(userId,
                                            externalScopeGUID,
                                            forLineage,
                                            forDuplicateProcessing,
                                            requestBody.getEffectiveTime());
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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param requestBody details of the external identifier and its scope
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse confirmSynchronization(String                        serverName,
                                               String                urlMarker,
                                               String                        openMetadataElementGUID,
                                               String                        openMetadataElementTypeName,
                                               MetadataCorrelationProperties requestBody)
    {
        final String methodName = "confirmSynchronization";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, urlMarker, methodName);

                handler.confirmSynchronization(userId,
                                               openMetadataElementGUID,
                                               openMetadataElementTypeName,
                                               requestBody.getIdentifier(),
                                               requestBody.getExternalScopeGUID(),
                                               requestBody.getExternalScopeName());
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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the external identifiers for a particular metadata element.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody details of the external identifier and its scope
     *
     * @return list of correlation header elements, null if null or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public MetadataCorrelationHeadersResponse getExternalIdentifiers(String                        serverName,
                                                                     String                urlMarker,
                                                                     String                        openMetadataElementGUID,
                                                                     String                        openMetadataElementTypeName,
                                                                     int                           startFrom,
                                                                     int                           pageSize,
                                                                     boolean                       forLineage,
                                                                     boolean                       forDuplicateProcessing,
                                                                     ExternalIdEffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getExternalIdentifiers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataCorrelationHeadersResponse response = new MetadataCorrelationHeadersResponse();
        AuditLog                           auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, urlMarker, methodName);

                response.setElementList(handler.getExternalIdentifiers(userId,
                                                                       requestBody.getExternalScopeGUID(),
                                                                       requestBody.getExternalScopeName(),
                                                                       openMetadataElementGUID,
                                                                       openMetadataElementTypeName,
                                                                       startFrom,
                                                                       pageSize,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       requestBody.getEffectiveTime()));
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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the unique identifier of the external asset manager from its qualified name.
     * Typically, the qualified name comes from the integration connector configuration.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody details of the external identifier
     *
     * @return list of linked elements, null if null or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public ElementHeadersResponse getElementsForExternalIdentifier(String                               serverName,
                                                                   String                urlMarker,
                                                                   int                                  startFrom,
                                                                   int                                  pageSize,
                                                                   boolean                              forLineage,
                                                                   boolean                              forDuplicateProcessing,
                                                                   UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        final String methodName = "getElementsForExternalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ElementHeadersResponse response = new ElementHeadersResponse();
        AuditLog               auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getMetadataCorrelationProperties() != null))
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, urlMarker, methodName);

                response.setElementHeaders(handler.getElementsForExternalIdentifier(userId,
                                                                                    requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                                                    requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                                                    requestBody.getMetadataCorrelationProperties().getIdentifier(),
                                                                                    startFrom,
                                                                                    pageSize,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    requestBody.getEffectiveTime()));
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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.externalreferences.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ExternalReferenceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.CitedDocumentLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.MediaReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The ExternalReferencesRESTServices provides the server-side implementation of the External References Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class ExternalReferencesRESTServices extends TokenController
{
    private static final ExternalReferencesInstanceHandler instanceHandler = new ExternalReferencesInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ExternalReferencesRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public ExternalReferencesRESTServices()
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
}

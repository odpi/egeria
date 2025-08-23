/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.locationarena.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.LocationHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ITInfrastructureProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.AdjacentLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.KnownLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.LocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.NestedLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The LocationArenaRESTServices provides the server-side implementation of the Location Arena Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class LocationArenaRESTServices extends TokenController
{
    private static final LocationArenaInstanceHandler instanceHandler = new LocationArenaInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(LocationArenaRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public LocationArenaRESTServices()
    {
    }



    /**
     * Create a location.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the location.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createLocation(String                serverName,
                                       NewElementRequestBody requestBody)
    {
        final String methodName = "createLocation";

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
                LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof LocationProperties locationProperties)
                {
                    response.setGUID(handler.createLocation(userId,
                                                            requestBody,
                                                            requestBody.getInitialClassifications(),
                                                            locationProperties,
                                                            requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LocationProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a location using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createLocationFromTemplate(String              serverName,
                                                   TemplateRequestBody requestBody)
    {
        final String methodName = "createLocationFromTemplate";

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
                LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

                response.setGUID(handler.createLocationFromTemplate(userId,
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
     * Update the properties of a location.
     *
     * @param serverName         name of called server.
     * @param locationGUID unique identifier of the location (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateLocation(String                   serverName,
                                       String                   locationGUID,
                                       UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateLocation";

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
                LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof LocationProperties locationProperties)
                {
                    handler.updateLocation(userId,
                                           locationGUID,
                                           requestBody,
                                           locationProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LocationProperties.class.getName(), methodName);
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
     * Attach a location to one of its peers.
     *
     * @param serverName         name of called server
     * @param locationOneGUID          unique identifier of the first location
     * @param locationTwoGUID          unique identifier of the second location
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkPeerLocation(String                     serverName,
                                         String                     locationOneGUID,
                                         String                     locationTwoGUID,
                                         NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkPeerLocations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AdjacentLocationProperties adjacentLocationProperties)
                {
                    handler.linkPeerLocations(userId,
                                              locationOneGUID,
                                              locationTwoGUID,
                                              requestBody,
                                              adjacentLocationProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkPeerLocations(userId,
                                              locationOneGUID,
                                              locationTwoGUID,
                                              requestBody,
                                              null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(KnownLocationProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkPeerLocations(userId,
                                          locationOneGUID,
                                          locationTwoGUID,
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
     * Detach a location from one of its peers.
     *
     * @param serverName         name of called server
     * @param locationOneGUID          unique identifier of the first location
     * @param locationTwoGUID          unique identifier of the second location
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachPeerLocations(String            serverName,
                                            String            locationOneGUID,
                                            String            locationTwoGUID,
                                            DeleteRequestBody requestBody)
    {
        final String methodName = "detachPeerLocations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            handler.detachPeerLocations(userId, locationOneGUID, locationTwoGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach a super location to a nested location.
     *
     * @param serverName         name of called server
     * @param locationGUID          unique identifier of the super location
     * @param nestedLocationGUID            unique identifier of the nested location
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkNestedLocation(String                     serverName,
                                           String                     locationGUID,
                                           String                     nestedLocationGUID,
                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkNestedLocation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof NestedLocationProperties nestedLocationProperties)
                {
                    handler.linkNestedLocation(userId,
                                               locationGUID,
                                               nestedLocationGUID,
                                               requestBody,
                                               nestedLocationProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkNestedLocation(userId,
                                               locationGUID,
                                               nestedLocationGUID,
                                               requestBody,
                                               null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(KnownLocationProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkNestedLocation(userId,
                                           locationGUID,
                                           nestedLocationGUID,
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
     * Detach a super location from a nested location.
     *
     * @param serverName         name of called server
     * @param locationGUID          unique identifier of the super location
     * @param nestedLocationGUID            unique identifier of the nested location
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachNestedLocation(String            serverName,
                                             String            locationGUID,
                                             String            nestedLocationGUID,
                                             DeleteRequestBody requestBody)
    {
        final String methodName = "detachNestedLocation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            handler.detachNestedLocation(userId, locationGUID, nestedLocationGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach an element to its location.
     *
     * @param serverName         name of called server
     * @param elementGUID       unique identifier of the asset
     * @param locationGUID            unique identifier of the location
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkKnownLocation(String                     serverName,
                                          String                     elementGUID,
                                          String                     locationGUID,
                                          NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkKnownLocation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof KnownLocationProperties knownLocationProperties)
                {
                    handler.linkKnownLocation(userId,
                                               elementGUID,
                                               locationGUID,
                                               requestBody,
                                               knownLocationProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkKnownLocation(userId,
                                              elementGUID,
                                              locationGUID,
                                              requestBody,
                                              null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ITInfrastructureProfileProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkKnownLocation(userId,
                                           elementGUID,
                                           locationGUID,
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
     * Detach an element from its location.
     *
     * @param serverName         name of called server
     * @param elementGUID       unique identifier of the element
     * @param locationGUID            unique identifier of the location
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachKnownLocation(String            serverName,
                                            String            elementGUID,
                                            String            locationGUID,
                                            DeleteRequestBody requestBody)
    {
        final String methodName = "detachKnownLocation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            handler.detachKnownLocation(userId, elementGUID, locationGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Delete a location.
     *
     * @param serverName         name of called server
     * @param locationGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteLocation(String            serverName,
                                       String            locationGUID,
                                       DeleteRequestBody requestBody)
    {
        final String methodName = "deleteLocation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            handler.deleteLocation(userId, locationGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of location metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getLocationsByName(String            serverName,
                                                               FilterRequestBody requestBody)
    {
        final String methodName = "getLocationsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getLocationsByName(userId, requestBody.getFilter(), requestBody));
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
     * Retrieve the list of location metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param locationGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getLocationByGUID(String             serverName,
                                                             String             locationGUID,
                                                             GetRequestBody requestBody)
    {
        final String methodName = "getLocationByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            response.setElement(handler.getLocationByGUID(userId, locationGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of location metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findLocations(String                  serverName,
                                                          SearchStringRequestBody requestBody)
    {
        final String methodName = "findLocations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findLocations(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findLocations(userId, null, null));
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

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.locationarena.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.LocationHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.AdjacentLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.KnownLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.LocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.NestedLocationProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.FixedLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.CyberLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.SecureLocationProperties;



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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createLocation(String                serverName,
                                       NewElementRequestBody requestBody)
    {
        final String methodName = "createLocation";

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createLocationFromTemplate(String              serverName,
                                                   TemplateRequestBody requestBody)
    {
        final String methodName = "createLocationFromTemplate";

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
                LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

                response.setGUID(handler.createLocationFromTemplate(userId,
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
     * Update the properties of a location.
     *
     * @param serverName         name of called server.
     * @param locationGUID unique identifier of the location (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateLocation(String                   serverName,
                                          String                   locationGUID,
                                          UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateLocation";

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
                LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof LocationProperties locationProperties)
                {
                    response.setFlag(handler.updateLocation(userId, locationGUID, requestBody, locationProperties));
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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkPeerLocation(String                     serverName,
                                         String                     locationOneGUID,
                                         String                     locationTwoGUID,
                                         NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkPeerLocations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                    restExceptionHandler.handleInvalidPropertiesObject(AdjacentLocationProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkPeerLocations(userId,
                                          locationOneGUID,
                                          locationTwoGUID,
                                          null,
                                          null);
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
     * Detach a location from one of its peers.
     *
     * @param serverName         name of called server
     * @param locationOneGUID          unique identifier of the first location
     * @param locationTwoGUID          unique identifier of the second location
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachPeerLocations(String                        serverName,
                                            String                        locationOneGUID,
                                            String                        locationTwoGUID,
                                            DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachPeerLocations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkNestedLocation(String                     serverName,
                                           String                     locationGUID,
                                           String                     nestedLocationGUID,
                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkNestedLocation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                    restExceptionHandler.handleInvalidPropertiesObject(NestedLocationProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkNestedLocation(userId,
                                           locationGUID,
                                           nestedLocationGUID,
                                           null,
                                           null);
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
     * Detach a super location from a nested location.
     *
     * @param serverName         name of called server
     * @param locationGUID          unique identifier of the super location
     * @param nestedLocationGUID            unique identifier of the nested location
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachNestedLocation(String                        serverName,
                                             String                        locationGUID,
                                             String                        nestedLocationGUID,
                                             DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachNestedLocation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkKnownLocation(String                     serverName,
                                          String                     elementGUID,
                                          String                     locationGUID,
                                          NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkKnownLocation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                    restExceptionHandler.handleInvalidPropertiesObject(KnownLocationProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkKnownLocation(userId,
                                           elementGUID,
                                           locationGUID,
                                           null,
                                           null);
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
     * Detach an element from its location.
     *
     * @param serverName         name of called server
     * @param elementGUID       unique identifier of the element
     * @param locationGUID            unique identifier of the location
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachKnownLocation(String                        serverName,
                                            String                        elementGUID,
                                            String                        locationGUID,
                                            DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachKnownLocation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteLocation(String                   serverName,
                                       String                   locationGUID,
                                       DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteLocation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getLocationsByName(String            serverName,
                                                               FilterRequestBody requestBody)
    {
        final String methodName = "getLocationsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getLocationByGUID(String             serverName,
                                                             String             locationGUID,
                                                             GetRequestBody requestBody)
    {
        final String methodName = "getLocationByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findLocations(String                  serverName,
                                                          SearchStringRequestBody requestBody)
    {
        final String methodName = "findLocations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /*
     * =====================================================================================================================
     * Location classifications
     */

    /**
     * Classify a location to say that it is a fixed physical location.
     *
     * @param serverName name of the server to route the request to
     * @param locationGUID unique identifier of the location
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setLocationAsFixedLocation(String                       serverName,
                                                   String                       locationGUID,
                                                   NewClassificationRequestBody requestBody)
    {
        final String methodName = "setLocationAsFixedLocation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.setLocationAsFixedLocation(userId, locationGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof FixedLocationProperties properties)
            {
                handler.setLocationAsFixedLocation(userId, locationGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setLocationAsFixedLocation(userId, locationGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(FixedLocationProperties.class.getName(), methodName);
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
     * Remove the fixed location designation from a location.
     *
     * @param serverName name of the server to route the request to
     * @param locationGUID unique identifier of the location
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearLocationAsFixedLocation(String                          serverName,
                                                     String                          locationGUID,
                                                     DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearLocationAsFixedLocation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            handler.clearLocationAsFixedLocation(userId, locationGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify a location to say that it is a cyber location reached over a network.
     *
     * @param serverName name of the server to route the request to
     * @param locationGUID unique identifier of the location
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setLocationAsCyberLocation(String                       serverName,
                                                   String                       locationGUID,
                                                   NewClassificationRequestBody requestBody)
    {
        final String methodName = "setLocationAsCyberLocation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.setLocationAsCyberLocation(userId, locationGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof CyberLocationProperties properties)
            {
                handler.setLocationAsCyberLocation(userId, locationGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setLocationAsCyberLocation(userId, locationGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(CyberLocationProperties.class.getName(), methodName);
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
     * Remove the cyber location designation from a location.
     *
     * @param serverName name of the server to route the request to
     * @param locationGUID unique identifier of the location
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearLocationAsCyberLocation(String                          serverName,
                                                     String                          locationGUID,
                                                     DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearLocationAsCyberLocation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            handler.clearLocationAsCyberLocation(userId, locationGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify a location to say that access to it is restricted.
     *
     * @param serverName name of the server to route the request to
     * @param locationGUID unique identifier of the location
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setLocationAsSecureLocation(String                       serverName,
                                                    String                       locationGUID,
                                                    NewClassificationRequestBody requestBody)
    {
        final String methodName = "setLocationAsSecureLocation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.setLocationAsSecureLocation(userId, locationGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof SecureLocationProperties properties)
            {
                handler.setLocationAsSecureLocation(userId, locationGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setLocationAsSecureLocation(userId, locationGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(SecureLocationProperties.class.getName(), methodName);
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
     * Remove the secure location designation from a location.
     *
     * @param serverName name of the server to route the request to
     * @param locationGUID unique identifier of the location
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearLocationAsSecureLocation(String                          serverName,
                                                      String                          locationGUID,
                                                      DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearLocationAsSecureLocation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            handler.clearLocationAsSecureLocation(userId, locationGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }
}

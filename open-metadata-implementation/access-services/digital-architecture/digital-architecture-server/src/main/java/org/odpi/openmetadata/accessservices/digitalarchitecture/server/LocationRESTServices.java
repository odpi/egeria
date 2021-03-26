/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.server;

import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.*;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.*;
import org.odpi.openmetadata.accessservices.digitalarchitecture.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.generichandlers.LocationHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * LocationRESTServices provides the API operations to create and maintain location information.
 */
public class LocationRESTServices
{
    private static DigitalArchitectureInstanceHandler   instanceHandler     = new DigitalArchitectureInstanceHandler();
    private static RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(LocationRESTServices.class),
                                                                      instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public LocationRESTServices()
    {
    }


    /*
     * ==============================================
     * Manage Locations
     * ==============================================
     */


    /**
     * Create a new metadata element to represent a location. Classifications can be added later to define the
     * type of location.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param locationProperties properties to store
     * @return unique identifier of the new metadata element
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public GUIDResponse createLocation(String             serverName,
                                       String             userId,
                                       LocationProperties locationProperties)
    {
        final String methodName = "createLocation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (locationProperties != null)
            {
                LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

                response.setGUID(handler.createLocation(userId,
                                                        locationProperties.getQualifiedName(),
                                                        locationProperties.getDisplayName(),
                                                        locationProperties.getDescription(),
                                                        locationProperties.getAdditionalProperties(),
                                                        locationProperties.getTypeName(),
                                                        locationProperties.getExtendedProperties(),
                                                        methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a new metadata element to represent a location using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new location.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param templateGUID       unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @return unique identifier of the new metadata element
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public GUIDResponse createLocationFromTemplate(String             serverName,
                                                   String             userId,
                                                   String             templateGUID,
                                                   TemplateProperties templateProperties)
    {
        final String methodName = "createLocationFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (templateProperties != null)
            {
                LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

                response.setGUID(handler.createLocationFromTemplate(userId,
                                                                    templateGUID,
                                                                    templateProperties.getQualifiedName(),
                                                                    templateProperties.getDisplayName(),
                                                                    templateProperties.getDescription(),
                                                                    methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the metadata element representing a location.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param locationGUID       unique identifier of the metadata element to update
     * @param locationProperties new properties for this element
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse updateLocation(String             serverName,
                                       String             userId,
                                       String             locationGUID,
                                       LocationProperties locationProperties)
    {
        final String methodName = "updateLocation";
        final String guidParameter = "locationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (locationProperties != null)
            {
                LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

                handler.updateLocation(userId,
                                       locationGUID,
                                       guidParameter,
                                       locationProperties.getQualifiedName(),
                                       locationProperties.getDisplayName(),
                                       locationProperties.getDescription(),
                                       locationProperties.getAdditionalProperties(),
                                       locationProperties.getTypeName(),
                                       locationProperties.getExtendedProperties(),
                                       methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Classify the location to indicate that it represents a fixed physical location.
     *
     * @param serverName name of calling server
     * @param userId        calling user
     * @param locationGUID  unique identifier of the metadata element to classify
     * @param requestBody   properties of the location
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse setLocationAsFixedPhysical(String                   serverName,
                                                   String                   userId,
                                                   String                   locationGUID,
                                                   FixedLocationRequestBody requestBody)
    {
        final String methodName = "setLocationAsFixedPhysical";
        final String locationGUIDParameter = "locationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

                handler.addFixedLocationClassification(userId,
                                                       locationGUID,
                                                       locationGUIDParameter,
                                                       requestBody.getCoordinates(),
                                                       requestBody.getMapProjection(),
                                                       requestBody.getPostalAddress(),
                                                       requestBody.getTimeZone(),
                                                       methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the fixed physical location designation from the location.
     *
     * @param serverName name of calling server
     * @param userId       calling user
     * @param locationGUID unique identifier of the metadata element to unclassify
     * @param requestBody null request body
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse clearLocationAsFixedPhysical(String          serverName,
                                                     String          userId,
                                                     String          locationGUID,
                                                     NullRequestBody requestBody)
    {
        final String methodName = "clearLocationAsFixedPhysical";
        final String locationGUIDParameter = "locationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            handler.removeFixedLocationClassification(userId,
                                                      locationGUID,
                                                      locationGUIDParameter,
                                                      methodName);

        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Classify the location to indicate that it represents a secure location.
     *
     * @param serverName name of calling server
     * @param userId       calling user
     * @param locationGUID unique identifier of the metadata element to classify
     * @param requestBody   properties of the location
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse setLocationAsSecure(String                    serverName,
                                            String                    userId,
                                            String                    locationGUID,
                                            SecureLocationRequestBody requestBody)

    {
        final String methodName = "setLocationAsSecure";
        final String locationGUIDParameter = "locationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

                handler.addSecureLocationClassification(userId,
                                                        locationGUID,
                                                        locationGUIDParameter,
                                                        requestBody.getDescription(),
                                                        requestBody.getLevel(),
                                                        methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the secure location designation from the location.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param locationGUID unique identifier of the metadata element to unclassify
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse clearLocationAsSecure(String          serverName,
                                              String          userId,
                                              String          locationGUID,
                                              NullRequestBody requestBody)
    {
        final String methodName = "clearLocationAsSecure";
        final String locationGUIDParameter = "locationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            handler.removeSecureLocationClassification(userId,
                                                       locationGUID,
                                                       locationGUIDParameter,
                                                       methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Classify the location to indicate that it represents a digital/cyber location.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param locationGUID unique identifier of the metadata element to classify
     * @param requestBody position of the location
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse setLocationAsDigital(String                     serverName,
                                             String                     userId,
                                             String                     locationGUID,
                                             DigitalLocationRequestBody requestBody)
    {
        final String methodName = "setLocationAsDigital";
        final String locationGUIDParameter = "locationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

                handler.addCyberLocationClassification(userId,
                                                       locationGUID,
                                                       locationGUIDParameter,
                                                       requestBody.getNetworkAddress(),
                                                       methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the digital/cyber location designation from the location.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param locationGUID unique identifier of the metadata element to unclassify
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse clearLocationAsDigital(String          serverName,
                                               String          userId,
                                               String          locationGUID,
                                               NullRequestBody requestBody)
    {
        final String methodName = "clearLocationAsDigital";
        final String locationGUIDParameter = "locationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            handler.removeCyberLocationClassification(userId,
                                                      locationGUID,
                                                      locationGUIDParameter,
                                                      methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the metadata element representing a location.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param locationGUID unique identifier of the metadata element to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeLocation(String          serverName,
                                       String          userId,
                                       String          locationGUID,
                                       NullRequestBody requestBody)
    {
        final String   methodName = "removeLocation";
        final String   guidParameter = "locationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            handler.removeLocation(userId, locationGUID, guidParameter, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a parent-child relationship between two locations.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param parentLocationGUID unique identifier of the location that is the broader location
     * @param childLocationGUID unique identifier of the location that is the smaller, nested location
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse setupNestedLocation(String          serverName,
                                            String          userId,
                                            String          parentLocationGUID,
                                            String          childLocationGUID,
                                            NullRequestBody requestBody)
    {
        final String methodName = "setupNestedLocation";
        final String parentLocationGUIDParameter = "parentLocationGUID";
        final String childLocationGUIDParameter = "childLocationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            handler.setupNestedLocation(userId,
                                        parentLocationGUID,
                                        parentLocationGUIDParameter,
                                        childLocationGUID,
                                        childLocationGUIDParameter,
                                        methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove a parent-child relationship between two locations.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param parentLocationGUID unique identifier of the location that is the broader location
     * @param childLocationGUID unique identifier of the location that is the smaller, nested location
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse clearNestedLocation(String          serverName,
                                            String          userId,
                                            String          parentLocationGUID,
                                            String          childLocationGUID,
                                            NullRequestBody requestBody)
    {
        final String methodName = "clearNestedLocation";
        final String parentLocationGUIDParameter = "parentLocationGUID";
        final String childLocationGUIDParameter = "childLocationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            handler.clearNestedLocation(userId,
                                        parentLocationGUID,
                                        parentLocationGUIDParameter,
                                        childLocationGUID,
                                        childLocationGUIDParameter,
                                        methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a peer-to-peer relationship between two locations.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param locationOneGUID unique identifier of the first location
     * @param locationTwoGUID unique identifier of the second location
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse setupAdjacentLocation(String          serverName,
                                              String          userId,
                                              String          locationOneGUID,
                                              String          locationTwoGUID,
                                              NullRequestBody requestBody)
    {
        final String methodName = "setupAdjacentLocation";
        final String locationOneGUIDParameter = "locationOneGUID";
        final String locationTwoGUIDParameter = "locationTwoGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            handler.setupPeerLocations(userId,
                                        locationOneGUID,
                                        locationOneGUIDParameter,
                                        locationTwoGUID,
                                        locationTwoGUIDParameter,
                                        methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove a peer-to-peer relationship between two locations.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param locationOneGUID unique identifier of the first location
     * @param locationTwoGUID unique identifier of the second location
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse clearAdjacentLocation(String          serverName,
                                              String          userId,
                                              String          locationOneGUID,
                                              String          locationTwoGUID,
                                              NullRequestBody requestBody)
    {
        final String methodName = "clearAdjacentLocation";
        final String locationOneGUIDParameter = "locationOneGUID";
        final String locationTwoGUIDParameter = "locationTwoGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            handler.clearPeerLocations(userId,
                                       locationOneGUID,
                                       locationOneGUIDParameter,
                                       locationTwoGUID,
                                       locationTwoGUIDParameter,
                                       methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Retrieve the list of location metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public LocationsResponse findLocations(String                  serverName,
                                           String                  userId,
                                           SearchStringRequestBody requestBody,
                                           int                     startFrom,
                                           int                     pageSize)
    {
        final String methodName = "findLocations";
        final String parameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LocationsResponse response = new LocationsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

                List<LocationElement> locations = handler.findBeans(userId,
                                                                    requestBody.getSearchString(),
                                                                    parameterName,
                                                                    OpenMetadataAPIMapper.LOCATION_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                                                    startFrom,
                                                                    pageSize,
                                                                    methodName);
                response.setElementList(locations);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of location metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public LocationsResponse getLocationsByName(String          serverName,
                                                String          userId,
                                                NameRequestBody requestBody,
                                                int             startFrom,
                                                int             pageSize)
    {
        final String methodName = "getLocationsByName";
        final String parameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LocationsResponse response = new LocationsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

                List<String> specificMatchPropertyNames = new ArrayList<>();
                specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
                specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);

                List<LocationElement> locations = handler.getBeansByValue(userId,
                                                                          requestBody.getName(),
                                                                          parameterName,
                                                                          OpenMetadataAPIMapper.LOCATION_TYPE_GUID,
                                                                          OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                                                          specificMatchPropertyNames,
                                                                          true,
                                                                          null,
                                                                          null,
                                                                          startFrom,
                                                                          pageSize,
                                                                          methodName);
                response.setElementList(locations);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the location metadata element with the supplied unique identifier.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param locationGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public LocationResponse getLocationByGUID(String serverName,
                                              String userId,
                                              String locationGUID)
    {
        final String methodName = "getLocationByGUID";
        final String locationGUIDParameter = "locationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LocationResponse response = new LocationResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            LocationElement location = handler.getBeanFromRepository(userId,
                                                                     locationGUID,
                                                                     locationGUIDParameter,
                                                                     OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                                                     methodName);
            response.setElement(location);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}

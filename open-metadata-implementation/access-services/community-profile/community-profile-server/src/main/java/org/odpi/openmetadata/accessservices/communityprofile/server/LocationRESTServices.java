/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;

import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.LocationElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.AdjacentLocationProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.AssetLocationProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.LocationProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.NestedLocationProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ProfileLocationProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.DigitalLocationProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.FixedLocationProperties;
import org.odpi.openmetadata.accessservices.communityprofile.rest.ClassificationRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.rest.LocationListResponse;
import org.odpi.openmetadata.accessservices.communityprofile.rest.LocationResponse;
import org.odpi.openmetadata.accessservices.communityprofile.properties.SecureLocationProperties;
import org.odpi.openmetadata.accessservices.communityprofile.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.rest.RelationshipRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.LocationHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * LocationRESTServices provides the API operations to create and maintain location information.
 */
public class LocationRESTServices
{
    private static final CommunityProfileInstanceHandler   instanceHandler     = new CommunityProfileInstanceHandler();
    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(LocationRESTServices.class),
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
     * @param requestBody properties to store
     * @return unique identifier of the new metadata element
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public GUIDResponse createLocation(String                   serverName,
                                       String                   userId,
                                       ReferenceableRequestBody requestBody)
    {
        final String methodName = "createLocation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof LocationProperties)
                {
                    LocationProperties locationProperties = (LocationProperties)requestBody.getProperties();

                    response.setGUID(handler.createLocation(userId,
                                                            requestBody.getExternalSourceGUID(),
                                                            requestBody.getExternalSourceName(),
                                                            locationProperties.getQualifiedName(),
                                                            locationProperties.getIdentifier(),
                                                            locationProperties.getDisplayName(),
                                                            locationProperties.getDescription(),
                                                            locationProperties.getAdditionalProperties(),
                                                            locationProperties.getTypeName(),
                                                            locationProperties.getExtendedProperties(),
                                                            locationProperties.getEffectiveFrom(),
                                                            locationProperties.getEffectiveTo(),
                                                            new Date(),
                                                            methodName));
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
     * @param requestBody properties that override the template
     * @return unique identifier of the new metadata element
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public GUIDResponse createLocationFromTemplate(String              serverName,
                                                   String              userId,
                                                   String              templateGUID,
                                                   TemplateRequestBody requestBody)
    {
        final String methodName = "createLocationFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

                response.setGUID(handler.createLocationFromTemplate(userId,
                                                                    requestBody.getExternalSourceGUID(),
                                                                    requestBody.getExternalSourceName(),
                                                                    templateGUID,
                                                                    requestBody.getQualifiedName(),
                                                                    requestBody.getIdentifier(),
                                                                    requestBody.getDisplayName(),
                                                                    requestBody.getDescription(),
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
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param locationGUID       unique identifier of the metadata element to update
     * @param requestBody new properties for this element
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse updateLocation(String                   serverName,
                                       String                   userId,
                                       String                   locationGUID,
                                       boolean                  isMergeUpdate,
                                       ReferenceableRequestBody requestBody)
    {
        final String methodName = "updateLocation";
        final String guidParameter = "locationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LocationProperties)
                {
                    LocationProperties locationProperties = (LocationProperties) requestBody.getProperties();

                    handler.updateLocation(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           locationGUID,
                                           guidParameter,
                                           locationProperties.getQualifiedName(),
                                           locationProperties.getIdentifier(),
                                           locationProperties.getDisplayName(),
                                           locationProperties.getDescription(),
                                           locationProperties.getAdditionalProperties(),
                                           locationProperties.getTypeName(),
                                           locationProperties.getExtendedProperties(),
                                           isMergeUpdate,
                                           locationProperties.getEffectiveFrom(),
                                           locationProperties.getEffectiveTo(),
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
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
    public VoidResponse setLocationAsFixedPhysical(String                    serverName,
                                                   String                    userId,
                                                   String                    locationGUID,
                                                   ClassificationRequestBody requestBody)
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

                if (requestBody.getProperties() instanceof FixedLocationProperties)
                {
                    FixedLocationProperties properties = (FixedLocationProperties) requestBody.getProperties();

                    handler.addFixedLocationClassification(userId,
                                                           requestBody.getExternalSourceGUID(),
                                                           requestBody.getExternalSourceName(),
                                                           locationGUID,
                                                           locationGUIDParameter,
                                                           properties.getCoordinates(),
                                                           properties.getMapProjection(),
                                                           properties.getPostalAddress(),
                                                           properties.getTimeZone(),
                                                           properties.getEffectiveFrom(),
                                                           properties.getEffectiveTo(),
                                                           false,
                                                           false,
                                                           new Date(),
                                                           methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(FixedLocationProperties.class.getName(), methodName);
                }
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
     * @param requestBody  request body
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse clearLocationAsFixedPhysical(String                    serverName,
                                                     String                    userId,
                                                     String                    locationGUID,
                                                     ExternalSourceRequestBody requestBody)
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

            if (requestBody == null)
            {
                handler.removeFixedLocationClassification(userId,
                                                          null,
                                                          null,
                                                          locationGUID,
                                                          locationGUIDParameter,
                                                          false,
                                                          false,
                                                          new Date(),
                                                          methodName);
            }
            else
            {
                handler.removeFixedLocationClassification(userId,
                                                          requestBody.getExternalSourceGUID(),
                                                          requestBody.getExternalSourceName(),
                                                          locationGUID,
                                                          locationGUIDParameter,
                                                          false,
                                                          false,
                                                          new Date(),
                                                          methodName);
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
                                            ClassificationRequestBody requestBody)

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

                if (requestBody.getProperties() instanceof  SecureLocationProperties)
                {
                    SecureLocationProperties properties = (SecureLocationProperties) requestBody.getProperties();
                    handler.addSecureLocationClassification(userId,
                                                            requestBody.getExternalSourceGUID(),
                                                            requestBody.getExternalSourceName(),
                                                            locationGUID,
                                                            locationGUIDParameter,
                                                            properties.getDescription(),
                                                            properties.getLevel(),
                                                            properties.getEffectiveFrom(),
                                                            properties.getEffectiveTo(),
                                                            false,
                                                            false,
                                                            new Date(),
                                                            methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SecureLocationProperties.class.getName(), methodName);
                }
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
     * @param requestBody  request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse clearLocationAsSecure(String                    serverName,
                                              String                    userId,
                                              String                    locationGUID,
                                              ExternalSourceRequestBody requestBody)
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

            if (requestBody == null)
            {
                handler.removeSecureLocationClassification(userId,
                                                           null,
                                                           null,
                                                           locationGUID,
                                                           locationGUIDParameter,
                                                           false,
                                                           false,
                                                           new Date(),
                                                           methodName);
            }
            else
            {
                handler.removeSecureLocationClassification(userId,
                                                           requestBody.getExternalSourceGUID(),
                                                           requestBody.getExternalSourceName(),
                                                           locationGUID,
                                                           locationGUIDParameter,
                                                           false,
                                                           false,
                                                           new Date(),
                                                           methodName);
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
    public VoidResponse setLocationAsDigital(String                    serverName,
                                             String                    userId,
                                             String                    locationGUID,
                                             ClassificationRequestBody requestBody)
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

                if (requestBody.getProperties() instanceof DigitalLocationProperties)
                {
                    DigitalLocationProperties properties = (DigitalLocationProperties)requestBody.getProperties();

                    handler.addCyberLocationClassification(userId,
                                                           requestBody.getExternalSourceGUID(),
                                                           requestBody.getExternalSourceName(),
                                                           locationGUID,
                                                           locationGUIDParameter,
                                                           properties.getNetworkAddress(),
                                                           properties.getEffectiveFrom(),
                                                           properties.getEffectiveTo(),
                                                           false,
                                                           false,
                                                           new Date(),
                                                           methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DigitalLocationProperties.class.getName(), methodName);
                }
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
    public VoidResponse clearLocationAsDigital(String                    serverName,
                                               String                    userId,
                                               String                    locationGUID,
                                               ExternalSourceRequestBody requestBody)
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

            if (requestBody == null)
            {
                handler.removeCyberLocationClassification(userId,
                                                          null,
                                                          null,
                                                          locationGUID,
                                                          locationGUIDParameter,
                                                          false,
                                                          false,
                                                          new Date(),
                                                          methodName);
            }
            else
            {
                handler.removeCyberLocationClassification(userId,
                                                          requestBody.getExternalSourceGUID(),
                                                          requestBody.getExternalSourceName(),
                                                          locationGUID,
                                                          locationGUIDParameter,
                                                          false,
                                                          false,
                                                          new Date(),
                                                          methodName);
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
    public VoidResponse removeLocation(String                    serverName,
                                       String                    userId,
                                       String                    locationGUID,
                                       ExternalSourceRequestBody requestBody)
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

            if (requestBody == null)
            {
                handler.removeLocation(userId,
                                       null,
                                       null,
                                       locationGUID,
                                       guidParameter,
                                       false,
                                       false,
                                       new Date(),
                                       methodName);
            }
            else
            {
                handler.removeLocation(userId,
                                       requestBody.getExternalSourceGUID(),
                                       requestBody.getExternalSourceName(),
                                       locationGUID,
                                       guidParameter,
                                       false,
                                       false,
                                       new Date(),
                                       methodName);
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
    public VoidResponse setupNestedLocation(String                  serverName,
                                            String                  userId,
                                            String                  parentLocationGUID,
                                            String                  childLocationGUID,
                                            RelationshipRequestBody requestBody)
    {
        final String methodName                  = "setupNestedLocation";
        final String parentLocationGUIDParameter = "parentLocationGUID";
        final String childLocationGUIDParameter  = "childLocationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof NestedLocationProperties)
                {
                    handler.setupNestedLocation(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                parentLocationGUID,
                                                parentLocationGUIDParameter,
                                                childLocationGUID,
                                                childLocationGUIDParameter,
                                                requestBody.getProperties().getEffectiveFrom(),
                                                requestBody.getProperties().getEffectiveTo(),
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setupNestedLocation(userId,
                                                null,
                                                null,
                                                parentLocationGUID,
                                                parentLocationGUIDParameter,
                                                childLocationGUID,
                                                childLocationGUIDParameter,
                                                null,
                                                null,
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NestedLocationProperties.class.getName(), methodName);
                }
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
    public VoidResponse clearNestedLocation(String                    serverName,
                                            String                    userId,
                                            String                    parentLocationGUID,
                                            String                    childLocationGUID,
                                            ExternalSourceRequestBody requestBody)
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

            if (requestBody == null)
            {
                handler.clearNestedLocation(userId,
                                            null,
                                            null,
                                            parentLocationGUID,
                                            parentLocationGUIDParameter,
                                            childLocationGUID,
                                            childLocationGUIDParameter,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
            }
            else
            {
                handler.clearNestedLocation(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            parentLocationGUID,
                                            parentLocationGUIDParameter,
                                            childLocationGUID,
                                            childLocationGUIDParameter,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
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
    public VoidResponse setupAdjacentLocation(String                  serverName,
                                              String                  userId,
                                              String                  locationOneGUID,
                                              String                  locationTwoGUID,
                                              RelationshipRequestBody requestBody)
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

            if (requestBody != null)
            {
                if (requestBody.getProperties() == null)
                {
                    handler.setupPeerLocations(userId,
                                               null,
                                               null,
                                               locationOneGUID,
                                               locationOneGUIDParameter,
                                               locationTwoGUID,
                                               locationTwoGUIDParameter,
                                               null,
                                               null,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
                }
                else if (requestBody.getProperties() instanceof AdjacentLocationProperties)
                {
                    AdjacentLocationProperties properties = (AdjacentLocationProperties)requestBody.getProperties();

                    handler.setupPeerLocations(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               locationOneGUID,
                                               locationOneGUIDParameter,
                                               locationTwoGUID,
                                               locationTwoGUIDParameter,
                                               properties.getEffectiveFrom(),
                                               properties.getEffectiveTo(),
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AdjacentLocationProperties.class.getName(), methodName);
                }
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
    public VoidResponse clearAdjacentLocation(String                    serverName,
                                              String                    userId,
                                              String                    locationOneGUID,
                                              String                    locationTwoGUID,
                                              ExternalSourceRequestBody requestBody)
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

            if (requestBody == null)
            {
                handler.clearPeerLocations(userId,
                                           null,
                                           null,
                                           locationOneGUID,
                                           locationOneGUIDParameter,
                                           locationTwoGUID,
                                           locationTwoGUIDParameter,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
            }
            else
            {
                handler.clearPeerLocations(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           locationOneGUID,
                                           locationOneGUIDParameter,
                                           locationTwoGUID,
                                           locationTwoGUIDParameter,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
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
     * Create a profile location relationship between an actor profile and a location.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param actorProfileGUID unique identifier of the actor profile
     * @param locationGUID unique identifier of the location
     * @param requestBody profile location request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse setupProfileLocation(String                  serverName,
                                             String                  userId,
                                             String                  actorProfileGUID,
                                             String                  locationGUID,
                                             RelationshipRequestBody requestBody)
    {
        final String methodName = "setupProfileLocation";
        final String actorProfileGUIDParameter = "actorProfileGUID";
        final String locationGUIDParameter = "locationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ProfileLocationProperties)
                {
                    ProfileLocationProperties properties = (ProfileLocationProperties)requestBody.getProperties();

                    handler.setupProfileLocation(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 actorProfileGUID,
                                                 actorProfileGUIDParameter,
                                                 locationGUID,
                                                 locationGUIDParameter,
                                                 properties.getAssociationType(),
                                                 properties.getEffectiveFrom(),
                                                 properties.getEffectiveTo(),
                                                 false,
                                                 false,
                                                 new Date(),
                                                 methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setupProfileLocation(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 actorProfileGUID,
                                                 actorProfileGUIDParameter,
                                                 locationGUID,
                                                 locationGUIDParameter,
                                                 null,
                                                 null,
                                                 null,
                                                 false,
                                                 false,
                                                 new Date(),
                                                 methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ProfileLocationProperties.class.getName(), methodName);
                }
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
     * Remove a profile location relationship between an actor profile and a location.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param actorProfileGUID unique identifier of the actor profile
     * @param locationGUID unique identifier of the location
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse clearProfileLocation(String                    serverName,
                                             String                    userId,
                                             String                    actorProfileGUID,
                                             String                    locationGUID,
                                             ExternalSourceRequestBody requestBody)
    {
        final String methodName = "clearProfileLocation";
        final String actorProfileGUIDParameter = "actorProfileGUID";
        final String locationGUIDParameter = "locationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.clearProfileLocation(userId,
                                             null,
                                             null,
                                             actorProfileGUID,
                                             actorProfileGUIDParameter,
                                             locationGUID,
                                             locationGUIDParameter,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
            }
            else
            {
                handler.clearProfileLocation(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             actorProfileGUID,
                                             actorProfileGUIDParameter,
                                             locationGUID,
                                             locationGUIDParameter,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
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
     * Create an asset location relationship between an asset and a location.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param locationGUID unique identifier of the location
     * @param requestBody profile location request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse setupAssetLocation(String                  serverName,
                                           String                  userId,
                                           String                  assetGUID,
                                           String                  locationGUID,
                                           RelationshipRequestBody requestBody)
    {
        final String methodName = "setupAssetLocation";
        final String assetGUIDParameter = "assetGUID";
        final String locationGUIDParameter = "locationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AssetLocationProperties)
                {
                    AssetLocationProperties properties = (AssetLocationProperties)requestBody.getProperties();

                    handler.setupAssetLocation(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               locationGUID,
                                               locationGUIDParameter,
                                               assetGUID,
                                               assetGUIDParameter,
                                               properties.getEffectiveFrom(),
                                               properties.getEffectiveTo(),
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setupAssetLocation(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               locationGUID,
                                               locationGUIDParameter,
                                               assetGUID,
                                               assetGUIDParameter,
                                               null,
                                               null,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AssetLocationProperties.class.getName(), methodName);
                }
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
     * Remove an asset location relationship between an asset and a location.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param assetGUID unique identifier of the actor profile
     * @param locationGUID unique identifier of the  location
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse clearAssetLocation(String                    serverName,
                                           String                    userId,
                                           String                    assetGUID,
                                           String                    locationGUID,
                                           ExternalSourceRequestBody requestBody)
    {
        final String methodName = "clearAssetLocation";
        final String assetGUIDParameter = "assetGUID";
        final String locationTwoGUIDParameter = "locationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.clearAssetLocation(userId,
                                           null,
                                           null,
                                           locationGUID,
                                           locationTwoGUIDParameter,
                                           assetGUID,
                                           assetGUIDParameter,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
            }
            else
            {
                handler.clearAssetLocation(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           locationGUID,
                                           locationTwoGUIDParameter,
                                           assetGUID,
                                           assetGUIDParameter,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
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
    public LocationListResponse findLocations(String                  serverName,
                                              String                  userId,
                                              SearchStringRequestBody requestBody,
                                              int                     startFrom,
                                              int                     pageSize)
    {
        final String methodName = "findLocations";
        final String parameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LocationListResponse response = new LocationListResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

                List<LocationElement> locations = handler.findLocations(userId,
                                                                        requestBody.getSearchString(),
                                                                        parameterName,
                                                                        startFrom,
                                                                        pageSize,
                                                                        false,
                                                                        false,
                                                                        new Date(),
                                                                        methodName);
                response.setElements(locations);
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
     * Retrieve the list of location metadata elements with a matching qualified name, identifier or display name.
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
    public LocationListResponse getLocationsByName(String          serverName,
                                                   String          userId,
                                                   NameRequestBody requestBody,
                                                   int             startFrom,
                                                   int             pageSize)
    {
        final String methodName = "getLocationsByName";
        final String parameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LocationListResponse response = new LocationListResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

                List<LocationElement> locations = handler.getLocationsByName(userId,
                                                                             requestBody.getName(),
                                                                             parameterName,
                                                                             startFrom,
                                                                             pageSize,
                                                                             false,
                                                                             false,
                                                                             new Date(),
                                                                             methodName);
                response.setElements(locations);
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
     * Retrieve the list of adjacent location metadata elements linked to locationGUID.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param locationGUID locationGUID to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public LocationListResponse getAdjacentLocations(String serverName,
                                                     String userId,
                                                     String locationGUID,
                                                     int    startFrom,
                                                     int    pageSize)
    {
        final String methodName = "getAdjacentLocations";
        final String parameterName = "locationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LocationListResponse response = new LocationListResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            List<LocationElement> locations = handler.getAdjacentLocations(userId,
                                                                           locationGUID,
                                                                           parameterName,
                                                                           OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                                                           startFrom,
                                                                           pageSize,
                                                                           false,
                                                                           false,
                                                                           new Date(),
                                                                           methodName);
            response.setElements(locations);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Retrieve the list of nested location metadata elements linked to locationGUID.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param locationGUID locationGUID to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public LocationListResponse getNestedLocations(String serverName,
                                                   String userId,
                                                   String locationGUID,
                                                   int    startFrom,
                                                   int    pageSize)
    {
        final String methodName = "getNestedLocations";
        final String parameterName = "locationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LocationListResponse response = new LocationListResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            List<LocationElement> locations = handler.getNestedLocations(userId,
                                                                         locationGUID,
                                                                         parameterName,
                                                                         OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                                                         startFrom,
                                                                         pageSize,
                                                                         false,
                                                                         false,
                                                                         new Date(),
                                                                         methodName);
            response.setElements(locations);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Retrieve the list of location metadata elements that has the location identifier with locationGUID nested inside it.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param locationGUID locationGUID to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public LocationListResponse getGroupingLocations(String serverName,
                                                     String userId,
                                                     String locationGUID,
                                                     int    startFrom,
                                                     int    pageSize)
    {
        final String methodName = "getGroupingLocations";
        final String parameterName = "locationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LocationListResponse response = new LocationListResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            List<LocationElement> locations = handler.getGroupingLocations(userId,
                                                                           locationGUID,
                                                                           parameterName,
                                                                           OpenMetadataAPIMapper.LOCATION_TYPE_NAME,
                                                                           startFrom,
                                                                           pageSize,
                                                                           false,
                                                                           false,
                                                                           new Date(),
                                                                           methodName);
            response.setElements(locations);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of location metadata elements linked to the requested profile.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param actorProfileGUID profile to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public LocationListResponse getLocationsByProfile(String serverName,
                                                      String userId,
                                                      String actorProfileGUID,
                                                      int    startFrom,
                                                      int    pageSize)
    {
        final String methodName = "getLocationsByProfile";
        final String parameterName = "actorProfileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LocationListResponse response = new LocationListResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            List<LocationElement> locations = handler.getProfileLocations(userId,
                                                                          actorProfileGUID,
                                                                          parameterName,
                                                                          OpenMetadataAPIMapper.ACTOR_PROFILE_TYPE_NAME,
                                                                          startFrom,
                                                                          pageSize,
                                                                          false,
                                                                          false,
                                                                          new Date(),
                                                                          methodName);
            response.setElements(locations);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of location metadata elements linked to assetGUID.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param assetGUID asset to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public LocationListResponse getKnownLocationsForAsset(String serverName,
                                                          String userId,
                                                          String assetGUID,
                                                          int    startFrom,
                                                          int    pageSize)
    {
        final String methodName = "getKnownLocationsForAsset";
        final String parameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LocationListResponse response = new LocationListResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            List<LocationElement> locations = handler.getAssetLocations(userId,
                                                                        assetGUID,
                                                                        parameterName,
                                                                        OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                        startFrom,
                                                                        pageSize,
                                                                        false,
                                                                        false,
                                                                        new Date(),
                                                                        methodName);
            response.setElements(locations);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of location metadata.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public LocationListResponse getLocations(String          serverName,
                                             String          userId,
                                             int             startFrom,
                                             int             pageSize)
    {
        final String methodName = "getLocations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LocationListResponse response = new LocationListResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LocationHandler<LocationElement> handler = instanceHandler.getLocationHandler(userId, serverName, methodName);

            List<LocationElement> locations = handler.getLocations(userId,
                                                                   startFrom,
                                                                   pageSize,
                                                                   false,
                                                                   false,
                                                                   new Date(),
                                                                   methodName);
            response.setElements(locations);
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
                                                                     false,
                                                                     false,
                                                                     new Date(),
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

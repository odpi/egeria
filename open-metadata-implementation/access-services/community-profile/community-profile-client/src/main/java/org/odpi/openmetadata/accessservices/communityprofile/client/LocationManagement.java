/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.client;

import org.odpi.openmetadata.accessservices.communityprofile.api.LocationManagementInterface;
import org.odpi.openmetadata.accessservices.communityprofile.client.rest.CommunityProfileRESTClient;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.LocationElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.AdjacentLocationProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.AssetLocationProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.LocationProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.NestedLocationProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ProfileLocationProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.TemplateProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.DigitalLocationProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.FixedLocationProperties;
import org.odpi.openmetadata.accessservices.communityprofile.rest.LocationListResponse;
import org.odpi.openmetadata.accessservices.communityprofile.rest.LocationResponse;
import org.odpi.openmetadata.accessservices.communityprofile.properties.SecureLocationProperties;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * LocationManager provides the API operations to create and maintain location definitions.
 *
 */
public class LocationManagement extends CommunityProfileBaseClient implements LocationManagementInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     *
     * @throws InvalidParameterException bad input parameters
     */
    public LocationManagement(String serverName,
                              String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public LocationManagement(String   serverName,
                              String   serverPlatformURLRoot,
                              AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException bad input parameters
     */
    public LocationManagement(String serverName,
                              String serverPlatformURLRoot,
                              String userId,
                              String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException bad input parameters
     */
    public LocationManagement(String   serverName,
                              String   serverPlatformURLRoot,
                              String   userId,
                              String   password,
                              AuditLog auditLog) throws  InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient pre-initialized REST client
     * @param maxPageSize pre-initialized parameter limit
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public LocationManagement(String                     serverName,
                              String                     serverPlatformURLRoot,
                              CommunityProfileRESTClient restClient,
                              int                        maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
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
     * @param userId             calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param locationProperties properties to store
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createLocation(String             userId,
                                 String             externalSourceGUID,
                                 String             externalSourceName,
                                 LocationProperties locationProperties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "createLocation";
        final String propertiesParameter = "locationProperties";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations";

        return super.createReferenceable(userId, externalSourceGUID, externalSourceName, locationProperties, propertiesParameter, urlTemplate, methodName);
    }


    /**
     * Create a new metadata element to represent a location using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new location.
     *
     * @param userId             calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param templateGUID       unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createLocationFromTemplate(String             userId,
                                             String             externalSourceGUID,
                                             String             externalSourceName,
                                             String             templateGUID,
                                             TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "createLocationFromTemplate";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/from-template/{2}";

        return super.createReferenceableFromTemplate(userId, externalSourceGUID, externalSourceName, templateGUID, templateProperties, urlTemplate, methodName);
    }


    /**
     * Update the metadata element representing a location.
     *
     * @param userId             calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param locationGUID       unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param locationProperties new properties for this element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateLocation(String             userId,
                               String             externalSourceGUID,
                               String             externalSourceName,
                               String             locationGUID,
                               boolean            isMergeUpdate,
                               LocationProperties locationProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "updateLocation";
        final String guidParameter = "locationGUID";
        final String propertiesParameter = "locationProperties";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/{2}/update?isMergeUpdate={3}";

        super.updateReferenceable(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  locationGUID,
                                  guidParameter,
                                  isMergeUpdate,
                                  locationProperties,
                                  propertiesParameter,
                                  urlTemplate,
                                  methodName);
    }


    /**
     * Classify the location to indicate that it represents a fixed physical location.
     *
     * @param userId        calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param locationGUID  unique identifier of the metadata element to classify
     * @param properties    time zone and position of the location
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setLocationAsFixedPhysical(String                  userId,
                                           String                  externalSourceGUID,
                                           String                  externalSourceName,
                                           String                  locationGUID,
                                           FixedLocationProperties properties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "setLocationAsFixedPhysical";
        final String locationGUIDParameter = "locationGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/{2}/classify-as-fixed-location";

        super.setReferenceableClassification(userId, externalSourceGUID, externalSourceName, locationGUID, locationGUIDParameter, properties, urlTemplate, methodName);
    }


    /**
     * Remove the fixed physical location designation from the location.
     *
     * @param userId       calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param locationGUID unique identifier of the metadata element to unclassify
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearLocationAsFixedPhysical(String userId,
                                             String externalSourceGUID,
                                             String externalSourceName,
                                             String locationGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "clearLocationAsFixedPhysical";
        final String locationGUIDParameter = "locationGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/classify-as-fixed-location/delete";

        super.removeReferenceableClassification(userId, externalSourceGUID, externalSourceName, locationGUID, locationGUIDParameter, urlTemplate, methodName);
    }


    /**
     * Classify the location to indicate that it represents a secure location.
     *
     * @param userId       calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param locationGUID unique identifier of the metadata element to classify
     * @param properties  properties of security at the site
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setLocationAsSecure(String                   userId,
                                    String                   externalSourceGUID,
                                    String                   externalSourceName,
                                    String                   locationGUID,
                                    SecureLocationProperties properties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException

    {
        final String methodName = "setLocationAsSecure";
        final String locationGUIDParameter = "locationGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/classify-as-secure-location";

        super.setReferenceableClassification(userId, externalSourceGUID, externalSourceName, locationGUID, locationGUIDParameter, properties, urlTemplate, methodName);
    }


    /**
     * Remove the secure location designation from the location.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param locationGUID unique identifier of the metadata element to unclassify
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearLocationAsSecure(String userId,
                                      String externalSourceGUID,
                                      String externalSourceName,
                                      String locationGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "clearLocationAsSecure";
        final String locationGUIDParameter = "locationGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/classify-as-secure-location/delete";

        super.removeReferenceableClassification(userId, externalSourceGUID, externalSourceName, locationGUID, locationGUIDParameter, urlTemplate, methodName);
    }


    /**
     * Classify the location to indicate that it represents a digital/cyber location.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param locationGUID unique identifier of the metadata element to classify
     * @param properties network address of the location
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setLocationAsDigital(String                    userId,
                                     String                    externalSourceGUID,
                                     String                    externalSourceName,
                                     String                    locationGUID,
                                     DigitalLocationProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "setLocationAsDigital";
        final String locationGUIDParameter = "locationGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/classify-as-digital-location";

        super.setReferenceableClassification(userId, externalSourceGUID, externalSourceName, locationGUID, locationGUIDParameter, properties, urlTemplate, methodName);
    }


    /**
     * Remove the digital/cyber location designation from the location.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param locationGUID unique identifier of the metadata element to unclassify
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearLocationAsDigital(String userId,
                                       String externalSourceGUID,
                                       String externalSourceName,
                                       String locationGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName = "clearLocationAsDigital";
        final String locationGUIDParameter = "locationGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/classify-as-digital-location/delete";

        super.removeReferenceableClassification(userId, externalSourceGUID, externalSourceName, locationGUID, locationGUIDParameter, urlTemplate, methodName);
    }


    /**
     * Remove the metadata element representing a location.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param locationGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeLocation(String userId,
                               String externalSourceGUID,
                               String externalSourceName,
                               String locationGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String methodName = "removeLocation";
        final String guidParameter = "locationGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/{2}/delete";

        super.removeReferenceable(userId, externalSourceGUID, externalSourceName, locationGUID, guidParameter, urlTemplate, methodName);
    }


    /**
     * Create a parent-child relationship between two locations.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param parentLocationGUID unique identifier of the location that is the broader location
     * @param childLocationGUID unique identifier of the location that is the smaller, nested location
     * @param properties relationship properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupNestedLocation(String                   userId,
                                    String                   externalSourceGUID,
                                    String                   externalSourceName,
                                    String                   parentLocationGUID,
                                    String                   childLocationGUID,
                                    NestedLocationProperties properties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "setupNestedLocation";
        final String parentLocationGUIDParameter = "parentLocationGUID";
        final String childLocationGUIDParameter = "childLocationGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/{2}/has-nested-location/{3}";

        super.setupRelationship(userId, externalSourceGUID, externalSourceName, parentLocationGUID, parentLocationGUIDParameter, properties, childLocationGUID, childLocationGUIDParameter, urlTemplate, methodName);
    }


    /**
     * Remove a parent-child relationship between two locations.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param parentLocationGUID unique identifier of the location that is the broader location
     * @param childLocationGUID unique identifier of the location that is the smaller, nested location
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearNestedLocation(String userId,
                                    String externalSourceGUID,
                                    String externalSourceName,
                                    String parentLocationGUID,
                                    String childLocationGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName = "clearNestedLocation";
        final String parentLocationGUIDParameter = "parentLocationGUID";
        final String childLocationGUIDParameter = "childLocationGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/{2}/has-nested-location/{3}/delete";

        super.clearRelationship(userId, externalSourceGUID, externalSourceName, parentLocationGUID, parentLocationGUIDParameter, childLocationGUID, childLocationGUIDParameter, urlTemplate, methodName);
    }


    /**
     * Create a peer-to-peer relationship between two locations.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param locationOneGUID unique identifier of the first location
     * @param locationTwoGUID unique identifier of the second location
     * @param properties relationship properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupAdjacentLocation(String                     userId,
                                      String                     externalSourceGUID,
                                      String                     externalSourceName,
                                      String                     locationOneGUID,
                                      String                     locationTwoGUID,
                                      AdjacentLocationProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "setupAdjacentLocation";
        final String locationOneGUIDParameter = "locationOneGUID";
        final String locationTwoGUIDParameter = "locationTwoGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/{2}/linked-to-peer-location/{3}";

        super.setupRelationship(userId, externalSourceGUID, externalSourceName, locationOneGUID, locationOneGUIDParameter, properties, locationTwoGUID, locationTwoGUIDParameter, urlTemplate, methodName);
    }


    /**
     * Remove a peer-to-peer relationship between two locations.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param locationOneGUID unique identifier of the first location
     * @param locationTwoGUID unique identifier of the second location
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearAdjacentLocation(String userId,
                                      String externalSourceGUID,
                                      String externalSourceName,
                                      String locationOneGUID,
                                      String locationTwoGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName = "clearAdjacentLocation";
        final String locationOneGUIDParameter = "locationOneGUID";
        final String locationTwoGUIDParameter = "locationTwoGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/{2}/linked-to-peer-location/{3}/delete";

        super.clearRelationship(userId, externalSourceGUID, externalSourceName, locationOneGUID, locationOneGUIDParameter, locationTwoGUID, locationTwoGUIDParameter, urlTemplate, methodName);
    }


    /**
     * Create a profile location relationship between an actor profile and a location.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param actorProfileGUID unique identifier of the actor profile
     * @param locationGUID unique identifier of the location
     * @param properties type of association with the location
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupProfileLocation(String                    userId,
                                     String                    externalSourceGUID,
                                     String                    externalSourceName,
                                     String                    actorProfileGUID,
                                     String                    locationGUID,
                                     ProfileLocationProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName            = "setupProfileLocation";
        final String actorGUIDParameter    = "actorProfileGUID";
        final String locationGUIDParameter = "locationGUID";
        final String urlTemplate           = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/{2}/linked-to-actor-profiles/{3}";

        super.setupRelationship(userId,
                                externalSourceGUID,
                                externalSourceName,
                                locationGUID,
                                locationGUIDParameter,
                                properties,
                                actorProfileGUID,
                                actorGUIDParameter,
                                urlTemplate,
                                methodName);
    }


    /**
     * Remove a profile location relationship between an actor profile and a location.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param actorProfileGUID unique identifier of the actor profile
     * @param locationGUID unique identifier of the  location
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearProfileLocation(String userId,
                                     String externalSourceGUID,
                                     String externalSourceName,
                                     String actorProfileGUID,
                                     String locationGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "clearProfileLocation";
        final String actorGUIDParameter = "actorProfileGUID";
        final String locationGUIDParameter = "locationGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/{2}/linked-to-actor-profiles/{3}/delete";

        super.clearRelationship(userId, externalSourceGUID, externalSourceName, locationGUID, locationGUIDParameter, actorProfileGUID, actorGUIDParameter, urlTemplate, methodName);
    }


    /**
     * Create an asset location relationship between an asset and a location.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param assetGUID unique identifier of the asset
     * @param locationGUID unique identifier of the location
     * @param properties type of association with the location
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupAssetLocation(String                    userId,
                                   String                    externalSourceGUID,
                                   String                    externalSourceName,
                                   String                    assetGUID,
                                   String                    locationGUID,
                                   AssetLocationProperties properties) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName            = "setupAssetLocation";
        final String actorGUIDParameter    = "assetGUID";
        final String locationGUIDParameter = "locationGUID";
        final String urlTemplate           = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/{2}/linked-to-assets/{3}";

        super.setupRelationship(userId,
                                externalSourceGUID,
                                externalSourceName,
                                locationGUID,
                                locationGUIDParameter,
                                properties,
                                assetGUID,
                                actorGUIDParameter,
                                urlTemplate,
                                methodName);
    }


    /**
     * Remove an asset location relationship between an asset and a location.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param assetGUID unique identifier of the asset
     * @param locationGUID unique identifier of the  location
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearAssetLocation(String userId,
                                   String externalSourceGUID,
                                   String externalSourceName,
                                   String assetGUID,
                                   String locationGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "clearAssetLocation";
        final String actorGUIDParameter = "assetGUID";
        final String locationGUIDParameter = "locationGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/{2}/linked-to-assets/{3}/delete";

        super.clearRelationship(userId, externalSourceGUID, externalSourceName, locationGUID, locationGUIDParameter, assetGUID, actorGUIDParameter, urlTemplate, methodName);
    }


    /**
     * Retrieve the list of location metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<LocationElement> findLocations(String userId,
                                               String searchString,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "findLocations";
        final String parameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, parameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/by-search-string" +
                                             "?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(parameterName);

        LocationListResponse restResult = restClient.callLocationsPostRESTCall(methodName,
                                                                               urlTemplate,
                                                                               requestBody,
                                                                               serverName,
                                                                               userId,
                                                                               startFrom,
                                                                               validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of location metadata elements with a matching qualified name, identifier or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<LocationElement> getLocationsByName(String userId,
                                                    String name,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "getLocationsByName";
        final String nameParameter = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameter);

        LocationListResponse restResult = restClient.callLocationsPostRESTCall(methodName,
                                                                               urlTemplate,
                                                                               requestBody,
                                                                               serverName,
                                                                               userId,
                                                                               startFrom,
                                                                               validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of adjacent location metadata elements linked to locationGUID.
     *
     * @param userId calling user
     * @param locationGUID locationGUID to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<LocationElement> getAdjacentLocations(String userId,
                                                      String locationGUID,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "getAdjacentLocations";

        final String guidParameter = "actorProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(locationGUID, guidParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/{2}/has-peer-locations?startFrom={3}&pageSize={4}";

        LocationListResponse restResult = restClient.callLocationsGetRESTCall(methodName,
                                                                              urlTemplate,
                                                                              serverName,
                                                                              userId,
                                                                              locationGUID,
                                                                              startFrom,
                                                                              validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of nested location metadata elements linked to locationGUID.
     *
     * @param userId calling user
     * @param locationGUID locationGUID to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<LocationElement> getNestedLocations(String userId,
                                                    String locationGUID,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "getNestedLocations";
        final String guidParameter = "locationGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(locationGUID, guidParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/{2}/has-nested-locations?startFrom={3}&pageSize={4}";

        LocationListResponse restResult = restClient.callLocationsGetRESTCall(methodName,
                                                                              urlTemplate,
                                                                              serverName,
                                                                              userId,
                                                                              locationGUID,
                                                                              startFrom,
                                                                              validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of location metadata elements that has the location identifier with locationGUID nested inside it.
     *
     * @param userId calling user
     * @param locationGUID locationGUID to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<LocationElement> getGroupingLocations(String userId,
                                                      String locationGUID,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "getNestedLocations";
        final String guidParameter = "locationGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(locationGUID, guidParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/{2}/has-grouping-locations?startFrom={3}&pageSize={4}";

        LocationListResponse restResult = restClient.callLocationsGetRESTCall(methodName,
                                                                              urlTemplate,
                                                                              serverName,
                                                                              userId,
                                                                              locationGUID,
                                                                              startFrom,
                                                                              validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of location metadata elements with linked to the actorProfileGUID.
     *
     * @param userId calling user
     * @param actorProfileGUID actorProfileGUID to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<LocationElement> getLocationsByProfile(String userId,
                                                       String actorProfileGUID,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "getLocationsByProfile";
        final String guidParameter = "actorProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actorProfileGUID, guidParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/by-actor-profile/{2}?startFrom={3}&pageSize={4}";

        LocationListResponse restResult = restClient.callLocationsGetRESTCall(methodName,
                                                                              urlTemplate,
                                                                              serverName,
                                                                              userId,
                                                                              actorProfileGUID,
                                                                              startFrom,
                                                                              validatedPageSize);

        return restResult.getElements();
    }



    /**
     * Retrieve the list of location metadata elements linked to assetGUID.
     *
     * @param userId calling user
     * @param assetGUID assetGUID to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<LocationElement> getKnownLocationsForAsset(String userId,
                                                           String assetGUID,
                                                           int    startFrom,
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "getKnownLocationsForAsset";
        final String guidParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/by-asset/{2}?startFrom={3}&pageSize={4}";

        LocationListResponse restResult = restClient.callLocationsGetRESTCall(methodName,
                                                                              urlTemplate,
                                                                              serverName,
                                                                              userId,
                                                                              assetGUID,
                                                                              startFrom,
                                                                              validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of location metadata elements.
     *
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<LocationElement> getLocations(String userId,
                                              int    startFrom,
                                              int    pageSize) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "getLocations";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations?startFrom={2}&pageSize={3}";


        LocationListResponse restResult = restClient.callLocationsGetRESTCall(methodName,
                                                                              urlTemplate,
                                                                              serverName,
                                                                              userId,
                                                                              startFrom,
                                                                              validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the location metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param locationGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public LocationElement getLocationByGUID(String userId,
                                             String locationGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String   methodName = "getLocationByGUID";
        final String   locationGUIDParameter = "locationGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(locationGUID, locationGUIDParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/community-profile/users/{1}/locations/{2}";

        LocationResponse restResult = restClient.callLocationGetRESTCall(methodName,
                                                                         urlTemplate,
                                                                         serverName,
                                                                         userId,
                                                                         locationGUID);
        return restResult.getElement();
    }
}

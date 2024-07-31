/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.api;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.LocationElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.AdjacentLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.AssetLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.DigitalLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.FixedLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.LocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.NestedLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.ProfileLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.SecureLocationProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * LocationManagementInterface defines the client side interface for the Community Profile OMAS that is relevant for managing definitions of locations.
 */
public interface LocationManagementInterface
{
    /**
     * Create a new metadata element to represent a location. Classifications can be added later to define the
     * type of location.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param locationProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createLocation(String             userId,
                          String             externalSourceGUID,
                          String             externalSourceName,
                          LocationProperties locationProperties) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Create a new metadata element to represent a location using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new location.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createLocationFromTemplate(String             userId,
                                      String             externalSourceGUID,
                                      String             externalSourceName,
                                      String             templateGUID,
                                      TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Update the metadata element representing a location.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param locationGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param locationProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateLocation(String             userId,
                        String             externalSourceGUID,
                        String             externalSourceName,
                        String             locationGUID,
                        boolean            isMergeUpdate,
                        LocationProperties locationProperties) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Classify the location to indicate that it represents a fixed physical location.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param locationGUID unique identifier of the metadata element to classify
     * @param properties time zone and position of the location
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setLocationAsFixedPhysical(String                  userId,
                                    String                  externalSourceGUID,
                                    String                  externalSourceName,
                                    String                  locationGUID,
                                    FixedLocationProperties properties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Remove the fixed physical location designation from the location.
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
    void clearLocationAsFixedPhysical(String userId,
                                      String externalSourceGUID,
                                      String externalSourceName,
                                      String locationGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Classify the location to indicate that it represents a secure location.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param locationGUID unique identifier of the metadata element to classify
     * @param properties properties of security at the site
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setLocationAsSecure(String                   userId,
                             String                   externalSourceGUID,
                             String                   externalSourceName,
                             String                   locationGUID,
                             SecureLocationProperties properties) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


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
    void clearLocationAsSecure(String userId,
                               String externalSourceGUID,
                               String externalSourceName,
                               String locationGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException;


    /**
     * Classify the location to indicate that it represents a digital/cyber location.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param locationGUID unique identifier of the metadata element to classify
     * @param properties address of the location
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setLocationAsDigital(String                    userId,
                              String                    locationGUID,
                              String                    externalSourceGUID,
                              String                    externalSourceName,
                              DigitalLocationProperties properties) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;


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
    void clearLocationAsDigital(String userId,
                                String externalSourceGUID,
                                String externalSourceName,
                                String locationGUID) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;


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
    void setupNestedLocation(String                   userId,
                             String                   externalSourceGUID,
                             String                   externalSourceName,
                             String                   parentLocationGUID,
                             String                   childLocationGUID,
                             NestedLocationProperties properties) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


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
    void clearNestedLocation(String userId,
                             String externalSourceGUID,
                             String externalSourceName,
                             String parentLocationGUID,
                             String childLocationGUID) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException;


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
    void setupAdjacentLocation(String                     userId,
                               String                     externalSourceGUID,
                               String                     externalSourceName,
                               String                     locationOneGUID,
                               String                     locationTwoGUID,
                               AdjacentLocationProperties properties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException;


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
    void clearAdjacentLocation(String userId,
                               String externalSourceGUID,
                               String externalSourceName,
                               String locationOneGUID,
                               String locationTwoGUID) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException;


    /**
     * Create a profile location relationship between an actor profile and a location.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param actorProfileGUID unique identifier of the actor profile
     * @param locationGUID unique identifier of the  location
     * @param properties type of association with the location
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupProfileLocation(String                    userId,
                              String                    externalSourceGUID,
                              String                    externalSourceName,
                              String                    actorProfileGUID,
                              String                    locationGUID,
                              ProfileLocationProperties properties) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;


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
    void clearProfileLocation(String userId,
                              String externalSourceGUID,
                              String externalSourceName,
                              String actorProfileGUID,
                              String locationGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException;



    /**
     * Create an asset location relationship between an asset and a location.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param assetGUID unique identifier of the actor profile
     * @param locationGUID unique identifier of the  location
     * @param properties type of association with the location
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupAssetLocation(String                  userId,
                            String                  externalSourceGUID,
                            String                  externalSourceName,
                            String                  assetGUID,
                            String                  locationGUID,
                            AssetLocationProperties properties) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


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
    void clearAssetLocation(String userId,
                            String externalSourceGUID,
                            String externalSourceName,
                            String assetGUID,
                            String locationGUID) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException;


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
    void removeLocation(String userId,
                        String externalSourceGUID,
                        String externalSourceName,
                        String locationGUID) throws InvalidParameterException,
                                                    UserNotAuthorizedException,
                                                    PropertyServerException;



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
    List<LocationElement> findLocations(String userId,
                                        String searchString,
                                        int    startFrom,
                                        int    pageSize) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


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
    List<LocationElement> getLocationsByName(String userId,
                                             String name,
                                             int    startFrom,
                                             int    pageSize) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


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
    List<LocationElement> getAdjacentLocations(String userId,
                                               String locationGUID,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


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
    List<LocationElement> getNestedLocations(String userId,
                                             String locationGUID,
                                             int    startFrom,
                                             int    pageSize) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


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
    List<LocationElement> getGroupingLocations(String userId,
                                               String locationGUID,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


    /**
     * Retrieve the list of location metadata elements linked to actorProfileGUID.
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
    List<LocationElement> getLocationsByProfile(String userId,
                                                String actorProfileGUID,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


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
    List<LocationElement> getKnownLocationsForAsset(String userId,
                                                    String assetGUID,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


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
    List<LocationElement> getLocations(String userId,
                                       int    startFrom,
                                       int    pageSize) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;


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
    LocationElement getLocationByGUID(String userId,
                                      String locationGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;
}

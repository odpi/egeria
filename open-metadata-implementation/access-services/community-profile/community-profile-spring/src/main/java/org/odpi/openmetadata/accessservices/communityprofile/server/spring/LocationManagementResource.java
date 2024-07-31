/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.communityprofile.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.accessservices.communityprofile.server.LocationRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * LocationResource provides the API operations to create and maintain locations.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/community-profile/users/{userId}")

@Tag(name="Metadata Access Server: Community Profile OMAS", description="The Community Profile OMAS provides APIs and events for tools and applications that are managing information about people and the way they work together.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/community-profile/overview/"))

public class LocationManagementResource
{
    private final LocationRESTServices restAPI = new LocationRESTServices();


    /**
     * Default constructor
     */
    public LocationManagementResource()
    {
    }


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
    @PostMapping(path = "/locations")

    public GUIDResponse createLocation(@PathVariable String                   serverName,
                                       @PathVariable String                   userId,
                                       @RequestBody  ReferenceableRequestBody locationProperties)
    {
        return restAPI.createLocation(serverName, userId, locationProperties);
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
    @PostMapping(path = "/locations/from-template/{templateGUID}")

    public GUIDResponse createLocationFromTemplate(@PathVariable String              serverName,
                                                   @PathVariable String              userId,
                                                   @PathVariable String              templateGUID,
                                                   @RequestBody TemplateRequestBody templateProperties)
    {
        return restAPI.createLocationFromTemplate(serverName, userId, templateGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a location.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param locationGUID       unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param locationProperties new properties for this element
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/locations/{locationGUID}/update")

    public VoidResponse updateLocation(@PathVariable String                   serverName,
                                       @PathVariable String                   userId,
                                       @PathVariable String                   locationGUID,
                                       @RequestParam boolean                  isMergeUpdate,
                                       @RequestBody  ReferenceableRequestBody locationProperties)
    {
        return restAPI.updateLocation(serverName, userId, locationGUID, isMergeUpdate, locationProperties);
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
    @PostMapping(path = "/locations/{locationGUID}/classify-as-fixed-location")

    public VoidResponse setLocationAsFixedPhysical(@PathVariable String                    serverName,
                                                   @PathVariable String                    userId,
                                                   @PathVariable String                    locationGUID,
                                                   @RequestBody  ClassificationRequestBody requestBody)
    {
        return restAPI.setLocationAsFixedPhysical(serverName, userId, locationGUID, requestBody);
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
    @PostMapping(path = "/locations/{locationGUID}/classify-as-fixed-location/delete")

    public VoidResponse clearLocationAsFixedPhysical(@PathVariable String                    serverName,
                                                     @PathVariable String                    userId,
                                                     @PathVariable String                    locationGUID,
                                                     @RequestBody (required = false)
                                                                   ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearLocationAsFixedPhysical(serverName, userId, locationGUID, requestBody);
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
    @PostMapping(path = "/locations/{locationGUID}/classify-as-secure-location")

    public VoidResponse setLocationAsSecure(@PathVariable String                    serverName,
                                            @PathVariable String                    userId,
                                            @PathVariable String                    locationGUID,
                                            @RequestBody  ClassificationRequestBody requestBody)

    {
        return restAPI.setLocationAsSecure(serverName, userId, locationGUID, requestBody);
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
    @PostMapping(path = "/locations/{locationGUID}/classify-as-secure-location/delete")

    public VoidResponse clearLocationAsSecure(@PathVariable String                    serverName,
                                              @PathVariable String                    userId,
                                              @PathVariable String                    locationGUID,
                                              @RequestBody (required = false)
                                                            ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearLocationAsSecure(serverName, userId, locationGUID, requestBody);
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
    @PostMapping(path = "/locations/{locationGUID}/classify-as-digital-location")

    public VoidResponse setLocationAsDigital(@PathVariable String                    serverName,
                                             @PathVariable String                    userId,
                                             @PathVariable String                    locationGUID,
                                             @RequestBody  ClassificationRequestBody requestBody)
    {
        return restAPI.setLocationAsDigital(serverName, userId, locationGUID, requestBody);
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
    @PostMapping(path = "/locations/{locationGUID}/classify-as-digital-location/delete")

    public VoidResponse clearLocationAsDigital(@PathVariable String                    serverName,
                                               @PathVariable String                    userId,
                                               @PathVariable String                    locationGUID,
                                               @RequestBody (required = false)
                                                             ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearLocationAsDigital(serverName, userId, locationGUID, requestBody);
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
    @PostMapping(path = "/locations/{locationGUID}/delete")

    public VoidResponse removeLocation(@PathVariable String                    serverName,
                                       @PathVariable String                    userId,
                                       @PathVariable String                    locationGUID,
                                       @RequestBody (required = false)
                                                     ExternalSourceRequestBody requestBody)
    {
        return restAPI.removeLocation(serverName, userId, locationGUID, requestBody);
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
    @PostMapping(path = "/locations/{parentLocationGUID}/has-nested-location/{childLocationGUID}")

    public VoidResponse setupNestedLocation(@PathVariable String                  serverName,
                                            @PathVariable String                  userId,
                                            @PathVariable String                  parentLocationGUID,
                                            @PathVariable String                  childLocationGUID,
                                            @RequestBody (required = false)
                                                          RelationshipRequestBody requestBody)
    {
        return restAPI.setupNestedLocation(serverName, userId, parentLocationGUID, childLocationGUID, requestBody);
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
    @PostMapping(path = "/locations/{parentLocationGUID}/has-nested-location/{childLocationGUID}/delete")

    public VoidResponse clearNestedLocation(@PathVariable String                    serverName,
                                            @PathVariable String                    userId,
                                            @PathVariable String                    parentLocationGUID,
                                            @PathVariable String                    childLocationGUID,
                                            @RequestBody (required = false)
                                                          ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearNestedLocation(serverName, userId, parentLocationGUID, childLocationGUID, requestBody);
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
    @PostMapping(path = "/locations/{locationOneGUID}/linked-to-peer-location/{locationTwoGUID}")

    public VoidResponse setupAdjacentLocation(@PathVariable String                  serverName,
                                              @PathVariable String                  userId,
                                              @PathVariable String                  locationOneGUID,
                                              @PathVariable String                  locationTwoGUID,
                                              @RequestBody (required = false)
                                                            RelationshipRequestBody requestBody)
    {
        return restAPI.setupAdjacentLocation(serverName, userId, locationOneGUID, locationTwoGUID, requestBody);
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
    @PostMapping(path = "/locations/{locationOneGUID}/linked-to-peer-location/{locationTwoGUID}/delete")

    public VoidResponse clearAdjacentLocation(@PathVariable String                    serverName,
                                              @PathVariable String                    userId,
                                              @PathVariable String                    locationOneGUID,
                                              @PathVariable String                    locationTwoGUID,
                                              @RequestBody (required = false)
                                                            ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearAdjacentLocation(serverName, userId, locationOneGUID, locationTwoGUID, requestBody);
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
    @PostMapping(path = "/locations/{locationGUID}/linked-to-actor-profiles/{actorProfileGUID}")

    public VoidResponse setupProfileLocation(@PathVariable String                  serverName,
                                             @PathVariable String                  userId,
                                             @PathVariable String                  actorProfileGUID,
                                             @PathVariable String                  locationGUID,
                                             @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupProfileLocation(serverName, userId, actorProfileGUID, locationGUID, requestBody);
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
    @PostMapping(path = "/locations/{locationGUID}/linked-to-actor-profiles/{actorProfileGUID}/delete")

    public VoidResponse clearProfileLocation(@PathVariable String                    serverName,
                                             @PathVariable String                    userId,
                                             @PathVariable String                    actorProfileGUID,
                                             @PathVariable String                    locationGUID,
                                             @RequestBody(required = false)
                                                           ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearProfileLocation(serverName, userId, actorProfileGUID, locationGUID, requestBody);
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
    @PostMapping(path = "/locations/{locationGUID}/linked-to-assets/{assetGUID}")

    public VoidResponse setupAssetLocation(@PathVariable String                  serverName,
                                           @PathVariable String                  userId,
                                           @PathVariable String                  assetGUID,
                                           @PathVariable String                  locationGUID,
                                           @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupAssetLocation(serverName, userId, assetGUID, locationGUID, requestBody);
    }


    /**
     * Remove an asset location relationship between an asset and a location.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param locationGUID unique identifier of the location
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/locations/{locationGUID}/linked-to-assets/{assetGUID}/delete")

    public VoidResponse clearAssetLocation(@PathVariable String                    serverName,
                                           @PathVariable String                    userId,
                                           @PathVariable String                    assetGUID,
                                           @PathVariable String                    locationGUID,
                                           @RequestBody(required = false)
                                                         ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearAssetLocation(serverName, userId, assetGUID, locationGUID, requestBody);
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
    @PostMapping(path = "/locations/by-search-string")

    public LocationsResponse findLocations(@PathVariable String                  serverName,
                                           @PathVariable String                  userId,
                                           @RequestParam int                     startFrom,
                                           @RequestParam int                     pageSize,
                                           @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findLocations(serverName, userId, requestBody, startFrom, pageSize);
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
    @PostMapping(path = "/locations/by-name")

    public LocationsResponse getLocationsByName(@PathVariable String          serverName,
                                                @PathVariable String          userId,
                                                @RequestParam int             startFrom,
                                                @RequestParam int             pageSize,
                                                @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getLocationsByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of adjacent location metadata elements linked to locationGUID.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param locationGUID location to start from
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/locations/{locationGUID}/has-peer-locations")

    public LocationsResponse getAdjacentLocations(@PathVariable String serverName,
                                                  @PathVariable String userId,
                                                  @PathVariable String locationGUID,
                                                  @RequestParam int    startFrom,
                                                  @RequestParam int    pageSize)
    {
        return restAPI.getAdjacentLocations(serverName, userId, locationGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of nested location metadata elements linked to locationGUID.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param locationGUID location to start from
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/locations/{locationGUID}/has-nested-locations")

    public LocationsResponse getNestedLocations(@PathVariable String serverName,
                                                @PathVariable String userId,
                                                @PathVariable String locationGUID,
                                                @RequestParam int    startFrom,
                                                @RequestParam int    pageSize)
    {
        return restAPI.getNestedLocations(serverName, userId, locationGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of location metadata elements that has the location identifier with locationGUID nested inside it.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param locationGUID location to start from
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/locations/{locationGUID}/has-grouping-locations")

    public LocationsResponse getGroupingLocations(@PathVariable String serverName,
                                                  @PathVariable String userId,
                                                  @PathVariable String locationGUID,
                                                  @RequestParam int    startFrom,
                                                  @RequestParam int    pageSize)
    {
        return restAPI.getGroupingLocations(serverName, userId, locationGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of location metadata elements linked to the requested profile.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param actorProfileGUID name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/locations/by-actor-profile/{actorProfileGUID}")

    public LocationsResponse getLocationsByProfile(@PathVariable String serverName,
                                                   @PathVariable String userId,
                                                   @PathVariable String actorProfileGUID,
                                                   @RequestParam int    startFrom,
                                                   @RequestParam int    pageSize)
    {
        return restAPI.getLocationsByProfile(serverName, userId, actorProfileGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of location metadata elements linked to the requested asset.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param assetGUID name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/locations/by-asset/{assetGUID}")

    public LocationsResponse getKnownLocationsForAsset(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String assetGUID,
                                                       @RequestParam int    startFrom,
                                                       @RequestParam int    pageSize)
    {
        return restAPI.getKnownLocationsForAsset(serverName, userId, assetGUID, startFrom, pageSize);
    }



    /**
     * Retrieve the list of location metadata elements.
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
    @GetMapping(path = "/locations")

    public LocationsResponse getLocations(@PathVariable String          serverName,
                                          @PathVariable String          userId,
                                          @RequestParam int             startFrom,
                                          @RequestParam int             pageSize)
    {
        return restAPI.getLocations(serverName, userId, startFrom, pageSize);
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
    @GetMapping(path = "/locations/{locationGUID}")

    public LocationResponse getLocationByGUID(@PathVariable String serverName,
                                              @PathVariable String userId,
                                              @PathVariable String locationGUID)
    {
        return restAPI.getLocationByGUID(serverName, userId, locationGUID);
    }
}

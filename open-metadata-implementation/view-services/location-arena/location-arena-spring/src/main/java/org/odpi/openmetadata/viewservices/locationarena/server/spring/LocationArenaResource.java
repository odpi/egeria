/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.locationarena.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.locationarena.server.LocationArenaRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The LocationArenaResource provides part of the server-side implementation of the Location Arena OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/location-arena")

@Tag(name="API: Location Arena OMVS", description="The Location Arena OMVS provides APIs for managing context events.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/location-arena/overview/"))

public class LocationArenaResource
{
    private final LocationArenaRESTServices restAPI = new LocationArenaRESTServices();

    /**
     * Default constructor
     */
    public LocationArenaResource()
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
    @PostMapping(path = "/locations")

    @Operation(summary="createLocation",
            description="Create a location.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/location"))

    public GUIDResponse createLocation(@PathVariable String                               serverName,
                                       @RequestBody (required = false)
                                       NewElementRequestBody requestBody)
    {
        return restAPI.createLocation(serverName, requestBody);
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
    @PostMapping(path = "/locations/from-template")
    @Operation(summary="createLocationFromTemplate",
            description="Create a new metadata element to represent a location using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/location"))

    public GUIDResponse createLocationFromTemplate(@PathVariable
                                                   String              serverName,
                                                   @RequestBody (required = false)
                                                   TemplateRequestBody requestBody)
    {
        return restAPI.createLocationFromTemplate(serverName, requestBody);
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
    @PostMapping(path = "/locations/{locationGUID}/update")
    @Operation(summary="updateLocation",
            description="Update the properties of a location.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/location"))

    public VoidResponse updateLocation(@PathVariable
                                       String                                  serverName,
                                       @PathVariable
                                       String                                  locationGUID,
                                       @RequestBody (required = false)
                                       UpdateElementRequestBody requestBody)
    {
        return restAPI.updateLocation(serverName, locationGUID, requestBody);
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
    @PostMapping(path = "/locations/{locationOneGUID}/adjacent-locations/{locationTwoGUID}/attach")
    @Operation(summary="linkPeerLocation",
            description="Attach a location to one of its peers.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/location"))

    public VoidResponse linkPeerLocation(@PathVariable
                                         String                     serverName,
                                         @PathVariable
                                         String locationOneGUID,
                                         @PathVariable
                                         String locationTwoGUID,
                                         @RequestBody (required = false)
                                         NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkPeerLocation(serverName, locationOneGUID, locationTwoGUID, requestBody);
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
    @PostMapping(path = "/locations/{locationOneGUID}/adjacent-locations/{locationTwoGUID}/detach")
    @Operation(summary="detachPeerLocations",
            description="Detach a location from one of its peers.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/location"))

    public VoidResponse detachPeerLocations(@PathVariable
                                            String                    serverName,
                                            @PathVariable
                                            String locationOneGUID,
                                            @PathVariable
                                            String locationTwoGUID,
                                            @RequestBody (required = false)
                                            DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachPeerLocations(serverName, locationOneGUID, locationTwoGUID, requestBody);
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
    @PostMapping(path = "/locations/{locationGUID}/nested-locations/{nestedLocationGUID}/attach")
    @Operation(summary="linkNestedLocation",
            description="Attach a super location to a nested location.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/location"))

    public VoidResponse linkNestedLocation(@PathVariable
                                           String                     serverName,
                                           @PathVariable
                                           String locationGUID,
                                           @PathVariable
                                           String nestedLocationGUID,
                                           @RequestBody (required = false)
                                           NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkNestedLocation(serverName, locationGUID, nestedLocationGUID, requestBody);
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
    @PostMapping(path = "/locations/{locationGUID}/nested-locations/{nestedLocationGUID}/detach")
    @Operation(summary="detachNestedLocation",
            description="Detach a super location from a nested location.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/location"))

    public VoidResponse detachNestedLocation(@PathVariable
                                             String                    serverName,
                                             @PathVariable
                                             String locationGUID,
                                             @PathVariable
                                             String nestedLocationGUID,
                                             @RequestBody (required = false)
                                             DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachNestedLocation(serverName, locationGUID, nestedLocationGUID, requestBody);
    }


    /**
     * Attach an element to its location.
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
    @PostMapping(path = "/elements/{elementGUID}/known-locations/{locationGUID}/attach")
    @Operation(summary="linkKnownLocation",
            description="Attach an element to its location.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/location"))

    public VoidResponse linkKnownLocation(@PathVariable
                                          String                     serverName,
                                          @PathVariable
                                          String elementGUID,
                                          @PathVariable
                                          String locationGUID,
                                          @RequestBody (required = false)
                                          NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkKnownLocation(serverName, elementGUID, locationGUID, requestBody);
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
    @PostMapping(path = "/elements/{elementGUID}/known-locations/{locationGUID}/detach")
    @Operation(summary="detachKnownLocation",
            description="Detach an element from its location.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/location"))

    public VoidResponse detachKnownLocation(@PathVariable
                                            String                    serverName,
                                            @PathVariable
                                            String elementGUID,
                                            @PathVariable
                                            String locationGUID,
                                            @RequestBody (required = false)
                                            DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachKnownLocation(serverName, elementGUID, locationGUID, requestBody);
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
    @PostMapping(path = "/locations/{locationGUID}/delete")
    @Operation(summary="deleteLocation",
            description="Delete a location.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/location"))

    public VoidResponse deleteLocation(@PathVariable
                                       String                    serverName,
                                       @PathVariable
                                       String                    locationGUID,
                                       @RequestBody (required = false)
                                       DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteLocation(serverName, locationGUID, requestBody);
    }


    /**
     * Returns the list of locations with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/locations/by-name")
    @Operation(summary="getLocationsByName",
            description="Returns the list of locations with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/location"))

    public OpenMetadataRootElementsResponse getLocationsByName(@PathVariable
                                                               String            serverName,
                                                               @RequestBody (required = false)
                                                               FilterRequestBody requestBody)
    {
        return restAPI.getLocationsByName(serverName, requestBody);
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
    @PostMapping(path = "/locations/by-search-string")
    @Operation(summary="findLocations",
            description="Retrieve the list of location metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/location"))

    public OpenMetadataRootElementsResponse findLocations(@PathVariable
                                                          String                  serverName,
                                                          @RequestBody (required = false)
                                                          SearchStringRequestBody requestBody)
    {
        return restAPI.findLocations(serverName, requestBody);
    }


    /**
     * Return the properties of a specific location.
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
    @PostMapping(path = "/locations/{locationGUID}/retrieve")
    @Operation(summary="getLocationByGUID",
            description="Return the properties of a specific location.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/location"))

    public OpenMetadataRootElementResponse getLocationByGUID(@PathVariable
                                                             String             serverName,
                                                             @PathVariable
                                                             String             locationGUID,
                                                             @RequestBody (required = false)
                                                             GetRequestBody requestBody)
    {
        return restAPI.getLocationByGUID(serverName, locationGUID, requestBody);
    }

}

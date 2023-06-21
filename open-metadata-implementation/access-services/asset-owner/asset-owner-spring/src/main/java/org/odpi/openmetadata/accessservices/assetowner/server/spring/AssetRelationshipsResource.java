/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetowner.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.accessservices.assetowner.rest.RelatedElementListResponse;
import org.odpi.openmetadata.accessservices.assetowner.rest.RelationshipRequestBody;
import org.odpi.openmetadata.accessservices.assetowner.server.RelatedElementRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * The AssetRelationshipsResource provides a Spring based server-side REST API
 * that supports the RelatedElementsManagementInterface.   It delegates each request to the
 * RelatedElementRESTServices.  This provides the server-side implementation of the Community Profile Open Metadata
 * Assess Service (OMAS) which is used to manage information about people, roles and organizations.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-owner/users/{userId}")

@Tag(name="Asset Owner OMAS",
     description="The Asset Owner OMAS provides APIs and notifications for tools and applications supporting the work of " +
                         "Asset Owners in protecting and enhancing their assets.",
     externalDocs=@ExternalDocumentation(description="Asset Owner Open Metadata Access Service (OMAS)",
                                         url="https://egeria-project.org/services/omas/asset-owner/overview/"))

public class AssetRelationshipsResource
{
    private final RelatedElementRESTServices restAPI = new RelatedElementRESTServices();

    /**
     * Default constructor
     */
    public AssetRelationshipsResource()
    {
    }


    /**
     * Create a "MoreInformation" relationship between an element that is descriptive and one that is providing the detail.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element that is descriptive
     * @param detailGUID         unique identifier of the element that provides the detail
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/related-elements/{elementGUID}/more-information/{detailGUID}")

    public VoidResponse setupMoreInformation(@PathVariable String                  serverName,
                                             @PathVariable String                  userId,
                                             @PathVariable String                  elementGUID,
                                             @PathVariable String                  detailGUID,
                                             @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupMoreInformation(serverName, userId, elementGUID, detailGUID, requestBody);
    }


    /**
     * Remove a "MoreInformation" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element that is descriptive
     * @param detailGUID         unique identifier of the element that provides the detail
     * @param requestBody external source identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/related-elements/{elementGUID}/more-information/{detailGUID}/delete")

    public VoidResponse clearMoreInformation(@PathVariable String                    serverName,
                                             @PathVariable String                    userId,
                                             @PathVariable String                    elementGUID,
                                             @PathVariable String                    detailGUID,
                                             @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearMoreInformation(serverName, userId, elementGUID, detailGUID, requestBody);
    }


    /**
     * Retrieve the detail elements linked via a "MoreInformation" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element that is descriptive
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/related-elements/more-information/by-descriptive-element/{elementGUID}")

    public  RelatedElementListResponse getMoreInformation(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @PathVariable String elementGUID,
                                                          @RequestParam int    startFrom,
                                                          @RequestParam int    pageSize)
    {
        return restAPI.getMoreInformation(serverName, userId, elementGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the descriptive elements linked via a "MoreInformation" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param detailGUID         unique identifier of the element that provides the detail
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/related-elements/more-information/by-detail-element/{detailGUID}")

    public  RelatedElementListResponse getDescriptiveElements(@PathVariable String serverName,
                                                              @PathVariable String userId,
                                                              @PathVariable String detailGUID,
                                                              @RequestParam int    startFrom,
                                                              @RequestParam int    pageSize)
    {
        return restAPI.getDescriptiveElements(serverName, userId, detailGUID, startFrom, pageSize);
    }


    /**
     * Create a "Stakeholder" relationship between an element and its stakeholder.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element
     * @param stakeholderGUID    unique identifier of the stakeholder
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/related-elements/{elementGUID}/stakeholders/{stakeholderGUID}")

    public VoidResponse setupStakeholder(@PathVariable String                  serverName,
                                         @PathVariable String                  userId,
                                         @PathVariable String                  elementGUID,
                                         @PathVariable String                  stakeholderGUID,
                                         @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupStakeholder(serverName, userId, elementGUID, stakeholderGUID, requestBody);
    }


    /**
     * Remove a "Stakeholder" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element
     * @param stakeholderGUID    unique identifier of the stakeholder
     * @param requestBody external source identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/related-elements/{elementGUID}/stakeholders/{stakeholderGUID}/delete")

    public VoidResponse clearStakeholder(@PathVariable String                    serverName,
                                         @PathVariable String                    userId,
                                         @PathVariable String                    elementGUID,
                                         @PathVariable String                    stakeholderGUID,
                                         @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearStakeholder(serverName, userId, elementGUID, stakeholderGUID, requestBody);
    }


    /**
     * Retrieve the stakeholder elements linked via the "Stakeholder"  relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/related-elements/stakeholders/by-commissioned-element/{elementGUID}")

    public  RelatedElementListResponse getStakeholders(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String elementGUID,
                                                       @RequestParam int   startFrom,
                                                       @RequestParam int   pageSize)
    {
        return restAPI.getStakeholders(serverName, userId, elementGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the elements commissioned by a stakeholder, linked via the "Stakeholder"  relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param stakeholderGUID unique identifier of the stakeholder
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/related-elements/stakeholders/by-stakeholder/{stakeholderGUID}")

    public  RelatedElementListResponse getStakeholderCommissionedElements(@PathVariable String serverName,
                                                                          @PathVariable String userId,
                                                                          @PathVariable String stakeholderGUID,
                                                                          @RequestParam int   startFrom,
                                                                          @RequestParam int   pageSize)
    {
        return restAPI.getStakeholderCommissionedElements(serverName, userId, stakeholderGUID, startFrom, pageSize);
    }


    /**
     * Create a "ResourceList" relationship between a consuming element and an element that represents resources.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param resourceGUID unique identifier of the resource
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/related-elements/{elementGUID}/resource-list/{resourceGUID}")

    public VoidResponse setupResource(@PathVariable String                  serverName,
                                      @PathVariable String                  userId,
                                      @PathVariable String                  elementGUID,
                                      @PathVariable String                  resourceGUID,
                                      @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupResource(serverName, userId, elementGUID, resourceGUID, requestBody);
    }


    /**
     * Remove a "ResourceList" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param resourceGUID unique identifier of the resource
     * @param requestBody external source identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/related-elements/{elementGUID}/resource-list/{resourceGUID}/delete")

    public VoidResponse clearResource(@PathVariable String                    serverName,
                                      @PathVariable String                    userId,
                                      @PathVariable String                    elementGUID,
                                      @PathVariable String                    resourceGUID,
                                      @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearResource(serverName, userId, elementGUID, resourceGUID, requestBody);
    }


    /**
     * Retrieve the list of resources assigned to an element via the "ResourceList" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/related-elements/resource-list/by-assignee/{elementGUID}")

    public  RelatedElementListResponse getResourceList(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String elementGUID,
                                                       @RequestParam int    startFrom,
                                                       @RequestParam int    pageSize)
    {
        return restAPI.getResourceList(serverName, userId, elementGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of elements assigned to a resource via the "ResourceList" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param resourceGUID unique identifier of the resource
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/related-elements/resource-list/by-resource/{resourceGUID}")

    public RelatedElementListResponse getSupportedByResource(@PathVariable String serverName,
                                                             @PathVariable String userId,
                                                             @PathVariable String resourceGUID,
                                                             @RequestParam int   startFrom,
                                                             @RequestParam int   pageSize)
    {
        return restAPI.getSupportedByResource(serverName, userId, resourceGUID, startFrom, pageSize);
    }
}

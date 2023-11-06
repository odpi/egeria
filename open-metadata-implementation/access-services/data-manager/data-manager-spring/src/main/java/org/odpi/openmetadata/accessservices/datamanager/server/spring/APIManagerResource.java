/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.datamanager.properties.APIParameterListType;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.accessservices.datamanager.server.APIManagerRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;


/**
 * APIManagerResource is the server-side implementation of the Data Manager OMAS's
 * support for APIs.  It matches the APIManagerClient.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-manager/users/{userId}")

@Tag(name="Metadata Access Server: Data Manager OMAS",
     description="The Data Manager OMAS provides APIs for tools and applications wishing to manage metadata relating to data managers " +
                         "such as database servers, event brokers, content managers and file systems.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/data-manager/overview/"))

public class APIManagerResource
{
    private final APIManagerRESTServices restAPI = new APIManagerRESTServices();

    /**
     * Default constructor
     */
    public APIManagerResource()
    {
    }


    /**
     * Create a new metadata element to represent a api.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiManagerIsHome should the API be marked as owned by the event broker so others can not update?
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis")

    public GUIDResponse createAPI(@PathVariable String         serverName,
                                  @PathVariable String         userId,
                                  @RequestParam boolean        apiManagerIsHome,
                                  @RequestBody  APIRequestBody requestBody)
    {
        return restAPI.createAPI(serverName, userId, apiManagerIsHome, null, requestBody);
    }


    /**
     * Create a new metadata element to represent a api.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param endpointGUID unique identifier of the endpoint where this API is located
     * @param apiManagerIsHome should the API be marked as owned by the event broker so others can not update?
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/for-endpoint/{endpointGUID}")

    public GUIDResponse createAPI(@PathVariable String         serverName,
                                  @PathVariable String         userId,
                                  @PathVariable String         endpointGUID,
                                  @RequestParam boolean        apiManagerIsHome,
                                  @RequestBody  APIRequestBody requestBody)
    {
        return restAPI.createAPI(serverName, userId, apiManagerIsHome, endpointGUID, requestBody);
    }


    /**
     * Create a new metadata element to represent a API using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param apiManagerIsHome should the API be marked as owned by the event broker so others can not update?
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/from-template/{templateGUID}")

    public GUIDResponse createAPIFromTemplate(@PathVariable String              serverName,
                                              @PathVariable String              userId,
                                              @PathVariable String              templateGUID,
                                              @RequestParam boolean             apiManagerIsHome,
                                              @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createAPIFromTemplate(serverName, userId, null, templateGUID, apiManagerIsHome, requestBody);
    }


    /**
     * Create a new metadata element to represent a API using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param endpointGUID unique identifier of the endpoint where this API is located
     * @param templateGUID unique identifier of the metadata element to copy
     * @param apiManagerIsHome should the API be marked as owned by the event broker so others can not update?
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/for-endpoint/{endpointGUID}/from-template/{templateGUID}")

    public GUIDResponse createAPIFromTemplate(@PathVariable String              serverName,
                                              @PathVariable String              userId,
                                              @PathVariable String              endpointGUID,
                                              @PathVariable String              templateGUID,
                                              @RequestParam boolean             apiManagerIsHome,
                                              @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createAPIFromTemplate(serverName, userId, endpointGUID, templateGUID, apiManagerIsHome, requestBody);
    }


    /**
     * Update the metadata element representing a api.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with the existing properties of overlay them?
     * @param requestBody new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/{apiGUID}")

    public VoidResponse updateAPI(@PathVariable String         serverName,
                                  @PathVariable String         userId,
                                  @PathVariable String         apiGUID,
                                  @RequestParam boolean        isMergeUpdate,
                                  @RequestBody  APIRequestBody requestBody)
    {
        return restAPI.updateAPI(serverName, userId, apiGUID, isMergeUpdate, requestBody);
    }


    /**
     * Update the zones for the API asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiGUID unique identifier of the metadata element to publish
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/{apiGUID}/publish")

    public VoidResponse publishAPI(@PathVariable                  String          serverName,
                                   @PathVariable                  String          userId,
                                   @PathVariable                  String          apiGUID,
                                   @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.publishAPI(serverName, userId, apiGUID, nullRequestBody);
    }


    /**
     * Update the zones for the API asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the API is first created).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiGUID unique identifier of the metadata element to withdraw
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/{apiGUID}/withdraw")

    public VoidResponse withdrawAPI(@PathVariable                  String          serverName,
                                    @PathVariable                  String          userId,
                                    @PathVariable                  String          apiGUID,
                                    @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.withdrawAPI(serverName, userId, apiGUID, nullRequestBody);
    }


    /**
     * Remove the metadata element representing a api.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique endpointGUID of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/{apiGUID}/{qualifiedName}/delete")

    public VoidResponse removeAPI(@PathVariable String                    serverName,
                                  @PathVariable String                    userId,
                                  @PathVariable String                    apiGUID,
                                  @PathVariable String                    qualifiedName,
                                  @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.removeAPI(serverName, userId, apiGUID, qualifiedName, requestBody);
    }


    /**
     * Retrieve the list of API metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/by-search-string")

    public APIsResponse findAPIs(@PathVariable String                  serverName,
                                 @PathVariable String                  userId,
                                 @RequestBody  SearchStringRequestBody requestBody,
                                 @RequestParam int                     startFrom,
                                 @RequestParam int                     pageSize)
    {
        return restAPI.findAPIs(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of API metadata elements with a matching qualified or display endpointGUID.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/by-name")

    public APIsResponse   getAPIsByName(@PathVariable String          serverName,
                                        @PathVariable String          userId,
                                        @RequestBody  NameRequestBody requestBody,
                                        @RequestParam int             startFrom,
                                        @RequestParam int             pageSize)
    {
        return restAPI.getAPIsByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of apis created by this caller.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the event broker
     * @param apiManagerName unique endpointGUID of software server capability representing the event broker
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/apis/api-managers/{apiManagerGUID}/{apiManagerName}")

    public APIsResponse   getAPIsForAPIManager(@PathVariable String serverName,
                                               @PathVariable String userId,
                                               @PathVariable String apiManagerGUID,
                                               @PathVariable String apiManagerName,
                                               @RequestParam int    startFrom,
                                               @RequestParam int    pageSize)
    {
        return restAPI.getAPIsForAPIManager(serverName, userId, apiManagerGUID, apiManagerName, startFrom, pageSize);
    }


    /**
     * Retrieve the list of API metadata elements that are linked to the requested endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param endpointGUID endpointGUID to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/apis/by-endpoint/{endpointGUID}")

    public APIsResponse getAPIsByEndpoint(@PathVariable String serverName,
                                          @PathVariable String userId,
                                          @PathVariable String endpointGUID,
                                          @RequestParam int    startFrom,
                                          @RequestParam int    pageSize)
    {
        return restAPI.getAPIsByEndpoint(serverName, userId, endpointGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the API metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/apis/{apiGUID}")

    public APIResponse getAPIByGUID(@PathVariable String serverName,
                                    @PathVariable String userId,
                                    @PathVariable String apiGUID)
    {
        return restAPI.getAPIByGUID(serverName, userId, apiGUID);
    }


    /* ============================================================================
     * A api may host one or more API operations depending on its capability
     */

    /**
     * Create a new metadata element to represent a API operation.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiGUID unique identifier of the topic where the schema is located
     * @param requestBody properties about the API operation
     *
     * @return unique identifier of the new API operation or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/{apiGUID}/api-operations")

    public GUIDResponse createAPIOperation(@PathVariable String                  serverName,
                                           @PathVariable String                  userId,
                                           @PathVariable String                  apiGUID,
                                           @RequestBody  APIOperationRequestBody requestBody)
    {
        return restAPI.createAPIOperation(serverName, userId, apiGUID, requestBody);
    }


    /**
     * Create a new metadata element to represent a API operation using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param apiGUID unique identifier of the topic where the schema is located
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new API operation or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/{apiGUID}/api-operations/from-template/{templateGUID}")

    public GUIDResponse createAPIOperationFromTemplate(@PathVariable String              serverName,
                                                       @PathVariable String              userId,
                                                       @PathVariable String              templateGUID,
                                                       @PathVariable String              apiGUID,
                                                       @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createAPIOperationFromTemplate(serverName, userId, templateGUID, apiGUID, requestBody);
    }


    /**
     * Update the metadata element representing a API operation.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiOperationGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/api-operations/{apiOperationGUID}")

    public VoidResponse updateAPIOperation(@PathVariable String                  serverName,
                                           @PathVariable String                  userId,
                                           @PathVariable String                  apiOperationGUID,
                                           @RequestParam boolean                 isMergeUpdate,
                                           @RequestBody  APIOperationRequestBody requestBody)
    {
        return restAPI.updateAPIOperation(serverName, userId, apiOperationGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the metadata element representing an API operation.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiOperationGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique endpointGUID of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/api-operations/{apiOperationGUID}/{qualifiedName}/delete")

    public VoidResponse removeAPIOperation(@PathVariable String                    serverName,
                                           @PathVariable String                    userId,
                                           @PathVariable String                    apiOperationGUID,
                                           @PathVariable String                    qualifiedName,
                                           @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.removeAPIOperation(serverName, userId, apiOperationGUID, qualifiedName, requestBody);
    }


    /**
     * Retrieve the list of API operation metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/api-operations/by-search-string")

    public APIOperationsResponse findAPIOperations(@PathVariable String                  serverName,
                                                   @PathVariable String                  userId,
                                                   @RequestBody  SearchStringRequestBody requestBody,
                                                   @RequestParam int                     startFrom,
                                                   @RequestParam int                     pageSize)
    {
        return restAPI.findAPIOperations(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Return the list of operation associated with an API.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiGUID unique identifier of the topic to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the schemas associated with the requested topic or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/apis/{apiGUID}/api-operations")

    public APIOperationsResponse getAPIOperationsForAPI(@PathVariable String serverName,
                                                        @PathVariable String userId,
                                                        @PathVariable String apiGUID,
                                                        @RequestParam int    startFrom,
                                                        @RequestParam int    pageSize)
    {
        return restAPI.getAPIOperationsForAPI(serverName, userId, apiGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of API operation metadata elements with a matching qualified or display endpointGUID.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/api-operations/by-name")

    public APIOperationsResponse getAPIOperationsByName(@PathVariable String          serverName,
                                                        @PathVariable String          userId,
                                                        @RequestBody  NameRequestBody requestBody,
                                                        @RequestParam int             startFrom,
                                                        @RequestParam int             pageSize)
    {
        return restAPI.getAPIOperationsByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the API operation metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiOperationGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/apis/api-operations/{apiOperationGUID}")

    public APIOperationResponse getAPIOperationByGUID(@PathVariable String serverName,
                                                      @PathVariable String userId,
                                                      @PathVariable String apiOperationGUID)
    {
        return restAPI.getAPIOperationByGUID(serverName, userId, apiOperationGUID);
    }
    

    /*
     * An API Operation may support a header, a request and a response parameter list depending on its capability
     */

    /**
     * Create a new metadata element to represent a API parameter list.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiOperationGUID unique identifier of the topic where the schema is located
     * @param parameterListType is this a header, request or response
     * @param requestBody properties about the API parameter list
     *
     * @return unique identifier of the new API parameter list or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/api-operations/{apiOperationGUID}/api-parameter-lists/{parameterListType}")

    public GUIDResponse createAPIParameterList(@PathVariable String                      serverName,
                                               @PathVariable String                      userId,
                                               @PathVariable String                      apiOperationGUID,
                                               @PathVariable APIParameterListType        parameterListType,
                                               @RequestBody  APIParameterListRequestBody requestBody)
    {
        return restAPI.createAPIParameterList(serverName, userId, apiOperationGUID, parameterListType, requestBody);
    }


    /**
     * Create a new metadata element to represent a API parameter list using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param apiOperationGUID unique identifier of the topic where the schema is located
     * @param parameterListType is this a header, request or response
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new API parameter list or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/api-operations/{apiOperationGUID}/api-parameter-lists/{parameterListType}/from-template/{templateGUID}")

    public GUIDResponse createAPIParameterListFromTemplate(@PathVariable String               serverName,
                                                           @PathVariable String               userId,
                                                           @PathVariable String               templateGUID,
                                                           @PathVariable String               apiOperationGUID,
                                                           @PathVariable APIParameterListType parameterListType,
                                                           @RequestBody  TemplateRequestBody  requestBody)
    {
        return restAPI.createAPIParameterListFromTemplate(serverName, userId, templateGUID, apiOperationGUID, parameterListType, requestBody);
    }


    /**
     * Update the metadata element representing a API parameter list.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiParameterListGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/api-operations/api-parameter-lists/{apiParameterListGUID}")

    public VoidResponse updateAPIParameterList(@PathVariable String                      serverName,
                                               @PathVariable String                      userId,
                                               @PathVariable String                      apiParameterListGUID,
                                               @RequestParam boolean                     isMergeUpdate,
                                               @RequestBody  APIParameterListRequestBody requestBody)
    {
        return restAPI.updateAPIParameterList(serverName, userId, apiParameterListGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the metadata element representing a API parameter list.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiParameterListGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique endpointGUID of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/api-operations/api-parameter-lists/{apiParameterListGUID}/{qualifiedName}/delete")

    public VoidResponse removeAPIParameterList(@PathVariable String                    serverName,
                                               @PathVariable String                    userId,
                                               @PathVariable String                    apiParameterListGUID,
                                               @PathVariable String                    qualifiedName,
                                               @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.removeAPIParameterList(serverName, userId, apiParameterListGUID, qualifiedName, requestBody);
    }


    /**
     * Retrieve the list of API parameter list metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/api-operations/api-parameter-lists/by-search-string")

    public APIParameterListsResponse findAPIParameterLists(@PathVariable String                  serverName,
                                                           @PathVariable String                  userId,
                                                           @RequestBody  SearchStringRequestBody requestBody,
                                                           @RequestParam int                     startFrom,
                                                           @RequestParam int                     pageSize)
    {
        return restAPI.findAPIParameterLists(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Return the list of schemas associated with a topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiOperationGUID unique identifier of the topic to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the schemas associated with the requested topic or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/apis/api-operations/{apiOperationGUID}/api-parameter-lists")

    public APIParameterListsResponse getAPIParameterListsForOperation(@PathVariable String serverName,
                                                                      @PathVariable String userId,
                                                                      @PathVariable String apiOperationGUID,
                                                                      @RequestParam int    startFrom,
                                                                      @RequestParam int    pageSize)
    {
        return restAPI.getAPIParameterListsForOperation(serverName, userId, apiOperationGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of API parameter list metadata elements with a matching qualified or display endpointGUID.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/apis/api-operations/api-parameter-lists/by-name")

    public APIParameterListsResponse getAPIParameterListsByName(@PathVariable String          serverName,
                                                                @PathVariable String          userId,
                                                                @RequestBody  NameRequestBody requestBody,
                                                                @RequestParam int             startFrom,
                                                                @RequestParam int             pageSize)
    {
        return restAPI.getAPIParameterListsByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the API parameter list metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param apiParameterListGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/apis/api-operations/api-parameter-lists/{apiParameterListGUID}")

    public APIParameterListResponse getAPIParameterListByGUID(@PathVariable String serverName,
                                                              @PathVariable String userId,
                                                              @PathVariable String apiParameterListGUID)
    {
        return restAPI.getAPIParameterListByGUID(serverName, userId, apiParameterListGUID);
    }
}

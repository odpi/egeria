/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ProcessContainmentType;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ProcessStatus;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.accessservices.assetmanager.server.LineageExchangeRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * LineageExchangeResource is the server-side implementation of the Asset Manager OMAS's
 * support for processes, ports and lineage mapping.  It matches the LineageExchangeClient.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-manager/users/{userId}")

@Tag(name="Asset Manager OMAS",
     description="The Asset Manager OMAS provides APIs and events for managing metadata exchange with third party asset managers, such as data catalogs.",
     externalDocs=@ExternalDocumentation(description="Asset Manager Open Metadata Access Service (OMAS)",
                                         url="https://egeria.odpi.org/open-metadata-implementation/access-services/asset-manager/"))

public class LineageExchangeResource
{
    private LineageExchangeRESTServices restAPI = new LineageExchangeRESTServices();


    /**
     * Default constructor
     */
    public LineageExchangeResource()
    {
    }
    

    /* =====================================================================================================================
     * A process describes a well defined series of steps that gets something done.
     */

    /**
     * Create a new metadata element to represent a process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param initialStatus status value for the new process (default = ACTIVE)
     * @param requestBody properties about the process to store
     *
     * @return unique identifier of the new process or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes")

    public GUIDResponse createProcess(@PathVariable String             serverName,
                                      @PathVariable String             userId,
                                      @RequestParam boolean            assetManagerIsHome,
                                      @RequestParam ProcessStatus      initialStatus,
                                      @RequestBody  ProcessRequestBody requestBody)
    {
        return restAPI.createProcess(serverName, userId, assetManagerIsHome, initialStatus, requestBody);
    }


    /**
     * Create a new metadata element to represent a process using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new process or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes/from-template/{templateGUID}")

    public GUIDResponse createProcessFromTemplate(@PathVariable String              serverName,
                                                  @PathVariable String              userId,
                                                  @PathVariable String              templateGUID,
                                                  @RequestParam boolean             assetManagerIsHome,
                                                  @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createProcessFromTemplate(serverName, userId, assetManagerIsHome, templateGUID, requestBody);
    }


    /**
     * Update the metadata element representing a process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes/{processGUID}")

    public VoidResponse updateProcess(@PathVariable String             serverName,
                                      @PathVariable String             userId,
                                      @PathVariable String             processGUID,
                                      @RequestParam boolean            isMergeUpdate,
                                      @RequestBody  ProcessRequestBody requestBody)
    {
        return restAPI.updateProcess(serverName, userId, processGUID, isMergeUpdate, requestBody);
    }


    /**
     * Update the status of the metadata element representing a process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the process to update
     * @param requestBody new status for the process
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes/{processGUID}/status")

    public VoidResponse updateProcessStatus(@PathVariable String                   serverName,
                                            @PathVariable String                   userId,
                                            @PathVariable String                   processGUID,
                                            @RequestBody  ProcessStatusRequestBody requestBody)
    {
        return restAPI.updateProcessStatus(serverName, userId, processGUID, requestBody);
    }


    /**
     * Create a parent-child relationship between two processes.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param parentProcessGUID unique identifier of the process in the external process manager that is to be the parent process
     * @param childProcessGUID unique identifier of the process in the external process manager that is to be the nested sub-process
     * @param containmentType describes the ownership of the sub-process
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes/parent/{parentProcessGUID}/child/{childProcessGUID}")

    public VoidResponse setupProcessParent(@PathVariable String                             serverName,
                                           @PathVariable String                             userId,
                                           @PathVariable String                             parentProcessGUID,
                                           @PathVariable String                             childProcessGUID,
                                           @RequestParam boolean                            assetManagerIsHome,
                                           @RequestParam ProcessContainmentType             containmentType,
                                           @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.setupProcessParent(serverName, userId, parentProcessGUID, childProcessGUID, assetManagerIsHome, containmentType, requestBody);
    }


    /**
     * Remove a parent-child relationship between two processes.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param parentProcessGUID unique identifier of the process in the external process manager that is to be the parent process
     * @param childProcessGUID unique identifier of the process in the external process manager that is to be the nested sub-process
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes/parent/{parentProcessGUID}/child/{childProcessGUID}/remove")
    
    public VoidResponse clearProcessParent(@PathVariable String                             serverName,
                                           @PathVariable String                             userId,
                                           @PathVariable String                             parentProcessGUID,
                                           @PathVariable String                             childProcessGUID,
                                           @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.clearProcessParent(serverName, userId, parentProcessGUID, childProcessGUID, requestBody);
    }


    /**
     * Update the zones for the process so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Asset Manager OMAS).
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to publish
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes/{processGUID}/publish")

    public VoidResponse publishProcess(@PathVariable String                             serverName,
                                       @PathVariable String                             userId,
                                       @PathVariable String                             processGUID,
                                       @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.publishProcess(serverName, userId, processGUID, requestBody);
    }


    /**
     * Update the zones for the process so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Asset Manager OMAS.  This is the setting when the process is first created).
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to withdraw
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes/{processGUID}/withdraw")

    public VoidResponse withdrawProcess(@PathVariable String                             serverName,
                                        @PathVariable String                             userId,
                                        @PathVariable String                             processGUID,
                                        @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.withdrawProcess(serverName, userId, processGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to remove
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes/{processGUID}/remove")

    public VoidResponse removeProcess(@PathVariable String                        serverName,
                                      @PathVariable String                        userId,
                                      @PathVariable String                        processGUID,
                                      @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.removeProcess(serverName, userId, processGUID, requestBody);
    }


    /**
     * Retrieve the list of process metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes/by-search-string")

    public ProcessElementsResponse findProcesses(@PathVariable String                  serverName,
                                                 @PathVariable String                  userId,
                                                 @RequestParam int                     startFrom,
                                                 @RequestParam int                     pageSize,
                                                 @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findProcesses(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Return the list of processes associated with the process manager.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return list of metadata elements describing the processes associated with the requested asset manager or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes/by-asset-manager")

    public ProcessElementsResponse getProcessesForAssetManager(@PathVariable String                             serverName,
                                                               @PathVariable String                             userId,
                                                               @RequestParam int                                startFrom,
                                                               @RequestParam int                                pageSize,
                                                               @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getProcessesForAssetManager(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of process metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes/by-name")

    public ProcessElementsResponse getProcessesByName(@PathVariable String          serverName,
                                                      @PathVariable String          userId,
                                                      @RequestParam int             startFrom,
                                                      @RequestParam int             pageSize,
                                                      @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getProcessesByName(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes/{processGUID}/retrieve")

    public ProcessElementResponse getProcessByGUID(@PathVariable String                             serverName,
                                                   @PathVariable String                             userId,
                                                   @PathVariable String                             processGUID,
                                                   @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getProcessByGUID(serverName, userId, processGUID, requestBody);
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return parent process element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes/{processGUID}/parent/retrieve")

    public ProcessElementResponse getProcessParent(@PathVariable String                             serverName,
                                                   @PathVariable String                             userId,
                                                   @PathVariable String                             processGUID,
                                                   @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getProcessParent(serverName, userId, processGUID, requestBody);
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return list of process elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes/{processGUID}/children/retrieve")

    public ProcessElementsResponse getSubProcesses(@PathVariable String                             serverName,
                                                   @PathVariable String                             userId,
                                                   @PathVariable String                             processGUID,
                                                   @RequestParam int                                startFrom,
                                                   @RequestParam int                                pageSize,
                                                   @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getSubProcesses(serverName, userId, processGUID, startFrom, pageSize, requestBody);
    }


    /* ===============================================================================
     * A process typically contains ports that show the flow of data and control to and from it.
     */

    /**
     * Create a new metadata element to represent a port.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the process manager can update this port
     * @param processGUID unique identifier of the process where the port is located
     * @param requestBody properties for the port
     *
     * @return unique identifier of the new metadata element for the port or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes/{processGUID}/ports")

    public GUIDResponse createPort(@PathVariable String          serverName,
                                   @PathVariable String          userId,
                                   @PathVariable String          processGUID,
                                   @RequestParam boolean         assetManagerIsHome,
                                   @RequestBody  PortRequestBody requestBody)
    {
        return restAPI.createPort(serverName, userId, assetManagerIsHome, processGUID, requestBody);
    }


    /**
     * Update the properties of the metadata element representing a port.  This call replaces
     * all existing properties with the supplied properties.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param portGUID unique identifier of the port to update
     * @param requestBody new properties for the port
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/ports/{portGUID}/update")

    public VoidResponse updatePort(@PathVariable String          serverName,
                                   @PathVariable String          userId,
                                   @PathVariable String          portGUID,
                                   @RequestBody  PortRequestBody requestBody)
    {
        return restAPI.updatePort(serverName, userId, portGUID, requestBody);
    }


    /**
     * Link a port to a process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param processGUID unique identifier of the process
     * @param portGUID unique identifier of the port
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes/{processGUID}/ports/{portGUID}")

    public VoidResponse setupProcessPort(@PathVariable String                             serverName,
                                         @PathVariable String                             userId,
                                         @PathVariable String                             processGUID,
                                         @PathVariable String                             portGUID,
                                         @RequestParam boolean                            assetManagerIsHome,
                                         @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.setupProcessPort(serverName, userId, assetManagerIsHome, processGUID, portGUID, requestBody);
    }


    /**
     * Unlink a port from a process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the process
     * @param portGUID unique identifier of the port
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes/{processGUID}/ports/{portGUID}/remove")

    public VoidResponse clearProcessPort(@PathVariable String                             serverName,
                                         @PathVariable String                             userId,
                                         @PathVariable String                             processGUID,
                                         @PathVariable String                             portGUID,
                                         @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.clearProcessPort(serverName, userId, processGUID, portGUID, requestBody);
    }


    /**
     * Link two ports together to show that portTwo is an implementation of portOne. (That is, portOne delegates to
     * portTwo.)
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param portOneGUID unique identifier of the port at end 1
     * @param portTwoGUID unique identifier of the port at end 2
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/ports/{portOneGUID}/port-delegations/{portTwoGUID}")

    public VoidResponse setupPortDelegation(@PathVariable String                             serverName,
                                            @PathVariable String                             userId,
                                            @PathVariable String                             portOneGUID,
                                            @PathVariable String                             portTwoGUID,
                                            @RequestParam boolean                            assetManagerIsHome,
                                            @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.setupPortDelegation(serverName, userId, assetManagerIsHome, portOneGUID, portTwoGUID, requestBody);
    }


    /**
     * Remove the port delegation relationship between two ports.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param portOneGUID unique identifier of the port at end 1
     * @param portTwoGUID unique identifier of the port at end 2
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/ports/{portOneGUID}/port-delegations/{portTwoGUID}/remove")

    public VoidResponse clearPortDelegation(@PathVariable String                             serverName,
                                            @PathVariable String                             userId,
                                            @PathVariable String                             portOneGUID,
                                            @PathVariable String                             portTwoGUID,
                                            @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.clearPortDelegation(serverName, userId, portOneGUID, portTwoGUID, requestBody);
    }


    /**
     * Link a schema type to a port to show the structure of data it accepts.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param portGUID unique identifier of the port
     * @param schemaTypeGUID unique identifier of the schemaType
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/ports/{portGUID}/schema-type/{schemaTypeGUID}")

    public VoidResponse setupPortSchemaType(@PathVariable String                             serverName,
                                            @PathVariable String                             userId,
                                            @RequestParam boolean                            assetManagerIsHome,
                                            @PathVariable String                             portGUID,
                                            @PathVariable String                             schemaTypeGUID,
                                            @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.setupPortSchemaType(serverName, userId, assetManagerIsHome, portGUID, schemaTypeGUID, requestBody);
    }


    /**
     * Remove the schema type from a port.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param portGUID unique identifier of the port
     * @param schemaTypeGUID unique identifier of the schemaType
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/ports/{portGUID}/schema-type/{schemaTypeGUID}/remove")

    public VoidResponse clearPortSchemaType(@PathVariable String                             serverName,
                                            @PathVariable String                             userId,
                                            @PathVariable String                             portGUID,
                                            @PathVariable String                             schemaTypeGUID,
                                            @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.clearPortSchemaType(serverName, userId, portGUID, schemaTypeGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a port.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param portGUID unique identifier of the metadata element to remove
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/ports/{portGUID}/remove")

    public VoidResponse removePort(@PathVariable String                        serverName,
                                   @PathVariable String                        userId,
                                   @PathVariable String                        portGUID,
                                   @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.removePort(serverName, userId, portGUID, requestBody);
    }


    /**
     * Retrieve the list of port metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/ports/by-search-string")

    public PortElementsResponse findPorts(@PathVariable String                  serverName,
                                          @PathVariable String                  userId,
                                          @RequestParam int                     startFrom,
                                          @RequestParam int                     pageSize,
                                          @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findPorts(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of ports associated with a process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the process of @RequestParam interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return list of associated metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/processes/{processGUID}/ports/retrieve")

    public PortElementsResponse getPortsForProcess(@PathVariable String                             serverName,
                                                   @PathVariable String                             userId,
                                                   @PathVariable String                             processGUID,
                                                   @RequestParam int                                startFrom,
                                                   @RequestParam int                                pageSize,
                                                   @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getPortsForProcess(serverName, userId, processGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of ports that delegate to this port.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param portGUID unique identifier of the starting port
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return list of associated metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/ports/{portGUID}/used-by/retrieve")

    public PortElementsResponse getPortUse(@PathVariable String                             serverName,
                                           @PathVariable String                             userId,
                                           @PathVariable String                             portGUID,
                                           @RequestParam int                                startFrom,
                                           @RequestParam int                                pageSize,
                                           @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getPortUse(serverName, userId, portGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the port that this port delegates to.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param portGUID unique identifier of the starting port alias
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/ports/{portGUID}/port-delegations/retrieve")

    public PortElementResponse getPortDelegation(@PathVariable String                             serverName,
                                                 @PathVariable String                             userId,
                                                 @PathVariable String                             portGUID,
                                                 @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getPortDelegation(serverName, userId, portGUID, requestBody);
    }


    /**
     * Retrieve the list of port metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/ports/by-name")

    public PortElementsResponse getPortsByName(@PathVariable String          serverName,
                                               @PathVariable String          userId,
                                               @RequestParam int             startFrom,
                                               @RequestParam int             pageSize,
                                               @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getPortsByName(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the port metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param portGUID unique identifier of the requested metadata element
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/ports/{portGUID}/retrieve")

    public PortElementResponse getPortByGUID(@PathVariable String                             serverName,
                                             @PathVariable String                             userId,
                                             @PathVariable String                             portGUID,
                                             @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getPortByGUID(serverName, userId, portGUID, requestBody);
    }


    /* ===============================================================================
     * General linkage and classifications
     */


    /**
     * Classify a port, process or process as "BusinessSignificant" (this may effect the way that lineage is displayed).
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/is-business-significant")

    public VoidResponse setBusinessSignificant(@PathVariable String                        serverName,
                                               @PathVariable String                        userId,
                                               @PathVariable String                        elementGUID,
                                               @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.setBusinessSignificant(serverName, userId, elementGUID, requestBody);
    }


    /**
     * Remove the "BusinessSignificant" designation from the element.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/is-business-significant/remove")

    public VoidResponse clearBusinessSignificant(@PathVariable String                        serverName,
                                                 @PathVariable String                        userId,
                                                 @PathVariable String                        elementGUID,
                                                 @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.clearBusinessSignificant(serverName, userId, elementGUID, requestBody);
    }


    /**
     * Link two elements together to show that data flows from one to the other.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param requestBody properties of the relationship
     *
     * @return unique identifier of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-flows/suppliers/{dataSupplierGUID}/consumers/{dataConsumerGUID}")

    public GUIDResponse setupDataFlow(@PathVariable String              serverName,
                                      @PathVariable String              userId,
                                      @PathVariable String              dataSupplierGUID,
                                      @PathVariable String              dataConsumerGUID,
                                      @RequestParam boolean             assetManagerIsHome,
                                      @RequestBody  DataFlowRequestBody requestBody)
    {
        return restAPI.setupDataFlow(serverName, userId, dataSupplierGUID, dataConsumerGUID, assetManagerIsHome, requestBody);
    }


    /**
     * Retrieve the data flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one data flow relationships between these two elements since it is used to disambiguate
     * the request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param requestBody optional name to search for
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-flows/suppliers/{dataSupplierGUID}/consumers/{dataConsumerGUID}/retrieve")

    public DataFlowElementResponse getDataFlow(@PathVariable String          serverName,
                                               @PathVariable String          userId,
                                               @PathVariable String          dataSupplierGUID,
                                               @PathVariable String          dataConsumerGUID,
                                               @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getDataFlow(serverName, userId, dataSupplierGUID, dataConsumerGUID, requestBody);
    }


    /**
     * Update relationship between two elements that shows that data flows from one to the other.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param requestBody properties of the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-flows/{dataFlowGUID}/update")

    public VoidResponse updateDataFlow(@PathVariable String              serverName,
                                       @PathVariable String              userId,
                                       @PathVariable String              dataFlowGUID,
                                       @RequestBody  DataFlowRequestBody requestBody)
    {
        return restAPI.updateDataFlow(serverName, userId, dataFlowGUID, requestBody);
    }


    /**
     * Remove the data flow relationship between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-flows/{dataFlowGUID}/remove")

    public VoidResponse clearDataFlow(@PathVariable String                             serverName,
                                      @PathVariable String                             userId,
                                      @PathVariable String                             dataFlowGUID,
                                      @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.clearDataFlow(serverName, userId, dataFlowGUID, requestBody);
    }


    /**
     * Retrieve the data flow relationships linked from an specific element to the downstream consumers.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-flows/suppliers/{dataSupplierGUID}/consumers/retrieve")

    public DataFlowElementsResponse getDataFlowConsumers(@PathVariable String                             serverName,
                                                         @PathVariable String                             userId,
                                                         @PathVariable String                             dataSupplierGUID,
                                                         @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getDataFlowConsumers(serverName, userId, dataSupplierGUID, requestBody);
    }


    /**
     * Retrieve the data flow relationships linked from an specific element to the upstream suppliers.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-flows/consumers/{dataConsumerGUID}/suppliers/retrieve")

    public DataFlowElementsResponse getDataFlowSuppliers(@PathVariable String                             serverName,
                                                         @PathVariable String                             userId,
                                                         @PathVariable String                             dataConsumerGUID,
                                                         @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getDataFlowSuppliers(serverName, userId, dataConsumerGUID, requestBody);
    }


    /**
     * Link two elements to show that when one completes the next is started.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param requestBody properties of the relationship
     *
     * @return unique identifier for the control flow relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/control-flows/current-steps/{currentStepGUID}/next-steps/{nextStepGUID}")

    public GUIDResponse setupControlFlow(@PathVariable String                 serverName,
                                         @PathVariable String                 userId,
                                         @PathVariable String                 currentStepGUID,
                                         @PathVariable String                 nextStepGUID,
                                         @RequestParam boolean                assetManagerIsHome,
                                         @RequestBody  ControlFlowRequestBody requestBody)
    {
        return restAPI.setupControlFlow(serverName, userId, currentStepGUID, nextStepGUID, assetManagerIsHome, requestBody);
    }


    /**
     * Retrieve the control flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one control flow relationships between these two elements since it is used to disambiguate
     * the request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param requestBody unique identifier for this relationship
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/control-flows/current-steps/{currentStepGUID}/next-steps/{nextStepGUID}/retrieve")

    public ControlFlowElementResponse getControlFlow(@PathVariable String          serverName,
                                                     @PathVariable String          userId,
                                                     @PathVariable String          currentStepGUID,
                                                     @PathVariable String          nextStepGUID,
                                                     @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getControlFlow(serverName, userId, currentStepGUID, nextStepGUID, requestBody);
    }


    /**
     * Update the relationship between two elements that shows that when one completes the next is started.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param requestBody properties of the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/control-flows/{controlFlowGUID}/update")

    public VoidResponse updateControlFlow(@PathVariable String                 serverName,
                                          @PathVariable String                 userId,
                                          @PathVariable String                 controlFlowGUID,
                                          @RequestBody  ControlFlowRequestBody requestBody)
    {
        return restAPI.updateControlFlow(serverName, userId, controlFlowGUID, requestBody);
    }


    /**
     * Remove the control flow relationship between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/control-flows/{controlFlowGUID}/remove")

    public VoidResponse clearControlFlow(@PathVariable String                             serverName,
                                         @PathVariable String                             userId,
                                         @PathVariable String                             controlFlowGUID,
                                         @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.clearControlFlow(serverName, userId, controlFlowGUID, requestBody);
    }


    /**
     * Retrieve the control relationships linked from an specific element to the possible next elements in the process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param currentStepGUID unique identifier of the current step
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/control-flows/current-steps/{currentStepGUID}/next-steps/retrieve")

    public ControlFlowElementsResponse getControlFlowNextSteps(@PathVariable String                             serverName,
                                                               @PathVariable String                             userId,
                                                               @PathVariable String                             currentStepGUID,
                                                               @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getControlFlowNextSteps(serverName, userId, currentStepGUID, requestBody);
    }


    /**
     * Retrieve the control relationships linked from an specific element to the possible previous elements in the process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param currentStepGUID unique identifier of the previous step
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/control-flows/current-steps/{currentStepGUID}/previous-steps/retrieve")

    public ControlFlowElementsResponse getControlFlowPreviousSteps(@PathVariable String                             serverName,
                                                                   @PathVariable String                             userId,
                                                                   @PathVariable String                             currentStepGUID,
                                                                   @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getControlFlowPreviousSteps(serverName, userId, currentStepGUID, requestBody);
    }


    /**
     * Link two elements together to show a request-response call between them.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param callerGUID unique identifier of the element that is making the call
     * @param calledGUID unique identifier of the element that is processing the call
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param requestBody properties of the relationship
     *
     * @return unique identifier of the new relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/process-calls/callers/{callerGUID}/called/{calledGUID}")

    public GUIDResponse setupProcessCall(@PathVariable String                 serverName,
                                         @PathVariable String                 userId,
                                         @PathVariable String                 callerGUID,
                                         @PathVariable String                 calledGUID,
                                         @RequestParam boolean                assetManagerIsHome,
                                         @RequestBody  ProcessCallRequestBody requestBody)
    {
        return restAPI.setupProcessCall(serverName, userId, callerGUID, calledGUID, assetManagerIsHome, requestBody);
    }


    /**
     * Retrieve the process call relationship between two elements.  The qualifiedName is optional unless there
     * is more than one process call relationships between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param callerGUID unique identifier of the element that is making the call
     * @param calledGUID unique identifier of the element that is processing the call
     * @param requestBody unique identifier for this relationship
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/process-calls/callers/{callerGUID}/called/{calledGUID}/retrieve")

    public ProcessCallElementResponse getProcessCall(@PathVariable String          serverName,
                                                     @PathVariable String          userId,
                                                     @PathVariable String          callerGUID,
                                                     @PathVariable String          calledGUID,
                                                     @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getProcessCall(serverName, userId, callerGUID, calledGUID, requestBody);
    }


    /**
     * Update the relationship between two elements that shows a request-response call between them.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processCallGUID unique identifier of the process call relationship
     * @param requestBody properties of the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/process-calls/{processCallGUID}/update")

    public VoidResponse updateProcessCall(@PathVariable String                 serverName,
                                          @PathVariable String                 userId,
                                          @PathVariable String                 processCallGUID,
                                          @RequestBody  ProcessCallRequestBody requestBody)
    {
        return restAPI.updateProcessCall(serverName, userId, processCallGUID, requestBody);
    }


    /**
     * Remove the process call relationship.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processCallGUID unique identifier of the process call relationship
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/process-calls/{processCallGUID}/remove")

    public VoidResponse clearProcessCall(@PathVariable String                             serverName,
                                         @PathVariable String                             userId,
                                         @PathVariable String                             processCallGUID,
                                         @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.clearProcessCall(serverName, userId, processCallGUID, requestBody);
    }


    /**
     * Retrieve the process call relationships linked from an specific element to the elements it calls.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param callerGUID unique identifier of the element that is making the call
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/process-calls/callers/{callerGUID}/called/retrieve")

    public ProcessCallElementsResponse getProcessCalled(@PathVariable String                             serverName,
                                                        @PathVariable String                             userId,
                                                        @PathVariable String                             callerGUID,
                                                        @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    { 
        return restAPI.getProcessCalled(serverName, userId, callerGUID, requestBody);
    }


    /**
     * Retrieve the process call relationships linked from an specific element to its callers.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param calledGUID unique identifier of the element that is processing the call
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/process-calls/called/{calledGUID}/callers/retrieve")

    public ProcessCallElementsResponse getProcessCallers(@PathVariable String                             serverName,
                                                         @PathVariable String                             userId,
                                                         @PathVariable String                             calledGUID,
                                                         @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getProcessCallers(serverName, userId, calledGUID, requestBody);
    }


    /**
     * Link to elements together to show that they are part of the lineage of the data that is moving
     * between the processes.  Typically the lineage relationships stitch together processes and data assets
     * supported by different technologies.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/lineage-mappings/sources/{sourceElementGUID}/destinations/{destinationElementGUID}")

    public VoidResponse setupLineageMapping(@PathVariable String                             serverName,
                                            @PathVariable String                             userId,
                                            @PathVariable String                             sourceElementGUID,
                                            @PathVariable String                             destinationElementGUID,
                                            @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.setupLineageMapping(serverName, userId, sourceElementGUID, destinationElementGUID, requestBody);
    }


    /**
     * Remove the lineage mapping between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/lineage-mappings/sources/{sourceElementGUID}/destinations/{destinationElementGUID}/remove")

    public VoidResponse clearLineageMapping(@PathVariable String                             serverName,
                                            @PathVariable String                             userId,
                                            @PathVariable String                             sourceElementGUID,
                                            @PathVariable String                             destinationElementGUID,
                                            @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.clearLineageMapping(serverName, userId, sourceElementGUID, destinationElementGUID, requestBody);
    }


    /**
     * Retrieve the lineage mapping relationships linked from an specific source element to its destinations.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/lineage-mappings/sources/{sourceElementGUID}/destinations/retrieve")

    public LineageMappingElementsResponse getDestinationLineageMappings(@PathVariable String                             serverName,
                                                                        @PathVariable String                             userId,
                                                                        @PathVariable String                             sourceElementGUID,
                                                                        @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getDestinationLineageMappings(serverName, userId, sourceElementGUID, requestBody);
    }


    /**
     * Retrieve the lineage mapping relationships linked from an specific destination element to its sources.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param destinationElementGUID unique identifier of the destination
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/lineage-mappings/destinations/{destinationElementGUID}/sources/retrieve")

    public LineageMappingElementsResponse getSourceLineageMappings(@PathVariable String                             serverName,
                                                                   @PathVariable String                             userId,
                                                                   @PathVariable String                             destinationElementGUID,
                                                                   @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getSourceLineageMappings(serverName, userId, destinationElementGUID, requestBody);
    }
}

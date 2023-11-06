/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.frameworkservices.gaf.server.OpenGovernanceRESTServices;
import org.springframework.web.bind.annotation.*;

/**
 * GovernanceEngineOMASResource supports the REST APIs for running Governance Action Service
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/framework-services/{serviceURLMarker}/open-governance-service/users/{userId}")

@Tag(name="Framework Services: Open Governance Service",
        description="Provides support for common governance services used across the OMASs.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/gaf-metadata-management/"))

public class OpenGovernanceResource
{
    private final OpenGovernanceRESTServices restAPI = new OpenGovernanceRESTServices();


    /**
     * Update the status of a specific action target. By default, these values are derived from
     * the values for the governance action service.  However, if the governance action service has to process name
     * target elements, then setting the status on each individual target will show the progress of the
     * governance action service.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody relationship properties
     *
     * @return void or
     *
     *  InvalidParameterException the action target GUID is not recognized
     *  UserNotAuthorizedException the governance action service is not authorized to update the action target properties
     *  PropertyServerException there is a problem connecting to the metadata store
     */
    @PostMapping(path = "/governance-actions/action-targets/update")

    public VoidResponse updateActionTargetStatus(@PathVariable String                        serverName,
                                                 @PathVariable String                        userId,
                                                 @RequestBody  ActionTargetStatusRequestBody requestBody)
    {
        return restAPI.updateActionTargetStatus(serverName, userId, requestBody);
    }


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param governanceActionGUID unique identifier of the governance action to update
     * @param requestBody completion status enum value, optional guard strings for triggering subsequent action(s) plus
     *                    a list of additional elements to add to the action targets for the next phase
     *
     * @return void or
     *
     *  InvalidParameterException the completion status is null
     *  UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     *  PropertyServerException there is a problem connecting to the metadata store
     */
    @PostMapping(path = "/governance-actions/{governanceActionGUID}/completion-status")

    public VoidResponse recordCompletionStatus(@PathVariable String                      serverName,
                                               @PathVariable String                      userId,
                                               @PathVariable String                      governanceActionGUID,
                                               @RequestBody  CompletionStatusRequestBody requestBody)
    {
        return restAPI.recordCompletionStatus(serverName, userId, governanceActionGUID, requestBody);
    }


    /**
     * Create a governance action in the metadata store which will trigger the governance action service
     * associated with the supplied request type.  The governance action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestBody properties for the governance action and to pass to the governance action service
     *
     * @return unique identifier of the governance action or
     *
     *  InvalidParameterException null qualified name
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/governance-engines/{governanceEngineName}/governance-actions/initiate")

    public GUIDResponse initiateGovernanceAction(@PathVariable String                      serverName,
                                                 @PathVariable String                      userId,
                                                 @PathVariable String                      governanceEngineName,
                                                 @RequestBody  GovernanceActionRequestBody requestBody)
    {
        return restAPI.initiateGovernanceAction(serverName, userId, governanceEngineName, requestBody);
    }


    /**
     * Using the named governance action process as a template, initiate a chain of governance actions.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody properties to initiate the new instance of the process
     *
     * @return unique identifier of the first governance action of the process or
     *
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/governance-action-processes/initiate")

    public GUIDResponse initiateGovernanceActionProcess(@PathVariable String                             serverName,
                                                        @PathVariable String                             userId,
                                                        @RequestBody  GovernanceActionProcessRequestBody requestBody)
    {
        return restAPI.initiateGovernanceActionProcess(serverName, userId, requestBody);
    }


    /**
     * Create a simple relationship between two elements. If the relationship already exists,
     * the properties are updated.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody parameters for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/related-elements/link-as-peer-duplicate")

    public VoidResponse linkElementsAsPeerDuplicates(@PathVariable String                    serverName,
                                                     @PathVariable String                    userId,
                                                     @RequestBody  PeerDuplicatesRequestBody requestBody)
    {
        return restAPI.linkElementsAsDuplicates(serverName, userId, requestBody);
    }


    /**
     * Create a simple relationship between two elements. If the relationship already exists,
     * the properties are updated.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody parameters for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/related-elements/link-as-consolidated-duplicate")

    public VoidResponse linkConsolidatedDuplicate(@PathVariable String                            serverName,
                                                  @PathVariable String                            userId,
                                                  @RequestBody  ConsolidatedDuplicatesRequestBody requestBody)
    {
        return restAPI.linkConsolidatedDuplicate(serverName, userId, requestBody);
    }
}

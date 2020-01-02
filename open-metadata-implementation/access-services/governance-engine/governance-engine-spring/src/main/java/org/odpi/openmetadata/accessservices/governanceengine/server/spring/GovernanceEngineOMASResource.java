/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.spring;

import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAssetAPIResponse;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAssetListAPIResponse;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.SoftwareServerCapabilityRequestBody;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.SoftwareServerCapabilityResponse;
import org.odpi.openmetadata.accessservices.governanceengine.server.GovernanceEngineRESTServices;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-engine/users/{userId}")
public class GovernanceEngineOMASResource {

    private GovernanceEngineRESTServices restAPI = new GovernanceEngineRESTServices();

    /**
     * Returns the list of governed asset
     * <p>
     * These include the tag associations but not the definitions of those tags
     *
     * @param userId - String - userId of user making request.
     * @param type   - the type of the entities that are returned
     * @return GovernedAssetComponentList or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping( path = "/assets", produces = MediaType.APPLICATION_JSON_VALUE)
    public GovernedAssetListAPIResponse getGovernedAssets(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @RequestParam(value = "type", required = false) List<String> type) {
        return restAPI.getGovernedAssets(serverName, userId, type);
    }

    /**
     * Returns a single governed asset
     * <p>
     * These include the tag associations but not the definitions of those tags
     *
     * @param userId    - String - userId of user making request.
     * @param assetGuid - Guid of the asset component to retrieve
     * @return GovernedAsset or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping( path = "/assets/{assetGuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GovernedAssetAPIResponse getGovernedAsset(@PathVariable String serverName,
                                                     @PathVariable String userId,
                                                     @PathVariable String assetGuid) {
        return restAPI.getGovernedAsset(serverName, userId, assetGuid);
    }

    /**
     * Create a Software Server Capability entity
     *
     * @param serverName  - String - the name of server instance to call
     * @param userId      - String - userId of user making request.
     * @param requestBody - SoftwareServerCapabilityRequestBody
     * @return the Software Server entity created
     */
    @PostMapping(path = "/software-server-capabilities")
    public SoftwareServerCapabilityResponse createSoftwareServerCapability(@PathVariable("serverName") String serverName,
                                                                           @PathVariable("userId") String userId,
                                                                           @RequestBody SoftwareServerCapabilityRequestBody requestBody) {
        return restAPI.createSoftwareServer(serverName, userId, requestBody);
    }


    /**
     * Fetch the Software Server Capability entity by global identifier
     *
     * @param serverName name of server instance to call
     * @param userId     the name of the calling user
     * @param guid       guid of the software server
     * @return unique identifier of the created process
     */
    @GetMapping(path = "/software-server-capabilities/{guid}")
    public SoftwareServerCapabilityResponse getSoftwareServerCapabilityByGUID(@PathVariable String serverName,
                                                                              @PathVariable String userId,
                                                                              @PathVariable String guid) {
        return restAPI.getSoftwareServerByGUID(serverName, userId, guid);
    }

}

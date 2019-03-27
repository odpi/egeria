/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.spring;

import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAssetAPIResponse;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAssetListAPIResponse;
import org.odpi.openmetadata.accessservices.governanceengine.server.GovernanceEngineRESTServices;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
     * @param userId         - String - userId of user making request.
     * @param classification - this may be the qualifiedName or displayName of the connection.
     * @param type           - the type of the entities that are returned
     * @return GovernedAssetComponentList or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets", produces = MediaType.APPLICATION_JSON_VALUE)
    GovernedAssetListAPIResponse getGovernedAssets(@PathVariable String serverName,
                                                   @PathVariable String userId,
                                                   @RequestParam(value = "classification", required = false) List<String> classification,
                                                   @RequestParam(value = "type", required = false) List<String> type) {
        return restAPI.getGovernedAssets(serverName, userId, classification, type);
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
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GovernedAssetAPIResponse getGovernedAsset(@PathVariable String serverName,
                                                     @PathVariable String userId,
                                                     @PathVariable String assetGuid) {
        return restAPI.getGovernedAsset(serverName, userId, assetGuid);
    }


}

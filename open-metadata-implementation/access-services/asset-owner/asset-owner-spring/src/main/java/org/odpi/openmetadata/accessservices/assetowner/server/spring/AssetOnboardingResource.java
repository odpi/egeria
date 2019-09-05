/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.server.spring;


import org.odpi.openmetadata.accessservices.assetowner.rest.NewCSVFileAssetRequestBody;
import org.odpi.openmetadata.accessservices.assetowner.server.AssetOnboardingRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.springframework.web.bind.annotation.*;

/**
 * AssetOnboardingResource supports the server-side capture of REST calls to add new asset definitions.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-owner/users/{userId}")
public class AssetOnboardingResource
{
    private AssetOnboardingRESTServices  restAPI = new AssetOnboardingRESTServices();

    /**
     * Default constructor
     */
    public AssetOnboardingResource()
    {
    }


    /**
     * Add a simple asset description linked to a connection object for a CSV file.
     *
     * @param serverName name of calling server
     * @param userId calling user (assumed to be the owner)
     * @param requestBody parameters for the new asset
     *
     * @return unique identifier (guid) of the asset or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/csv-files")

    public GUIDResponse addCSVFileToCatalog(@PathVariable String                  serverName,
                                            @PathVariable String                  userId,
                                            @RequestBody NewCSVFileAssetRequestBody requestBody)
    {
        return restAPI.addCSVFileToCatalog(serverName, userId, requestBody);
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server.spring;

import org.odpi.openmetadata.accessservices.assetlineage.model.rest.responses.AssetResponse;
import org.odpi.openmetadata.accessservices.assetlineage.service.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.service.AssetLineageRestServices;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-lineage/users/{userId}/")
public class AssetLineageOMASResource {

    private final AssetLineageRestServices restAPI = new AssetLineageRestServices();
    private AssetContext assetContext = new AssetContext();

    public AssetLineageOMASResource() {

    }

    /**
     * Return the full context of an asset/glossary term based on its identifier.
     * The response contains the list of the connections assigned to the asset.
     *
     * @param serverName       unique identifier for requested server.
     * @param userId           the unique identifier for the user
     * @param assetId          the global unique identifier of the asset
     * @return list of properties used to narrow the search
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/asset-context/{assetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetResponse getAssetContext(@PathVariable("serverName") String serverName,
                                         @PathVariable("userId") String userId,
                                         @PathVariable("assetId") String assetId) {
        return assetContext.buildAssetContext(serverName, userId, assetId);
    }

}

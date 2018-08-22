/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.server.spring;

import org.odpi.openmetadata.accessservice.assetcatalog.service.OMASCatalogRESTServices;
import org.odpi.openmetadata.repositoryservices.rest.properties.TypeDefGalleryResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.TypeDefResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The AssetCatalogGeneralResource provides the server-side implementation of the Asset Catalog Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/open-metadata/access-services/asset-catalog/users/{userId}/")
public class AssetCatalogGeneralResource {

    private OMASCatalogRESTServices restAPI = new OMASCatalogRESTServices();

    /**
     * @param userId the unique identifier for the user
     * @return a list with available typedefs
     */
    @RequestMapping(method = RequestMethod.GET, path = "/get-typedefs")
    public TypeDefGalleryResponse getTypeDefs(@PathVariable String userId) {
        return restAPI.getAllTypes(userId);
    }

    /**
     * @param userId the unique identifier for the user
     * @param guid   the unique identifier for the type
     * @return the typedef with the given id
     */
    @RequestMapping(method = RequestMethod.GET, path = "/get-type/{guid}")
    public TypeDefResponse getTypeById(@PathVariable String userId, @PathVariable String guid) {
        return restAPI.getTypeDefByGUID(userId, guid);
    }

}

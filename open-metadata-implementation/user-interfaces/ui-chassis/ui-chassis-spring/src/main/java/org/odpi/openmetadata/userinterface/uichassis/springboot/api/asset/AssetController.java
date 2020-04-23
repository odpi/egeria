/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.asset;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetElements;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Type;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.service.AssetCatalogOMASService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    @Autowired
    AssetCatalogOMASService assetCatalogOMASService;

    /**
     * @param searchCriteria the query parameter with the search phrase
     * @return list of assets
     */
    @GetMapping( path = "/search")
    public List<AssetElements> searchAssets(@RequestParam("q") String searchCriteria, @RequestParam("types") List<String> types)
            throws PropertyServerException, InvalidParameterException {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        SearchParameters searchParameters = new SearchParameters();
        if(CollectionUtils.isNotEmpty(types) ) {
            searchParameters.setEntityTypes(types);
        }
        return assetCatalogOMASService.searchAssets(user, searchCriteria, searchParameters);
    }

    /**
     *
     * @return the supported types from AssetCatalog OMAS
     * @throws PropertyServerException if a configuration on the backend
     * @throws InvalidParameterException if parameter validation fails
     */
    @GetMapping( path = "/types")
    public List<Type> getSupportedTypes() throws PropertyServerException, InvalidParameterException {
        String user = SecurityContextHolder.getContext().getAuthentication().getName( );
        return assetCatalogOMASService.getSupportedTypes(user);
    }

    /**
     *
     * @param guid of the Entity to be retrieved
     * @return the entity details
     * @throws PropertyServerException if a configuration on the backend
     * @throws InvalidParameterException if parameter validation fails
     */
    @GetMapping( value = "/{guid}")
    public AssetDescription getAsset(@PathVariable("guid") String guid)
            throws PropertyServerException, InvalidParameterException {
        String user = SecurityContextHolder.getContext().getAuthentication().getName( );
        return assetCatalogOMASService.getAssetDetails(user, guid, "none");
    }

    /**
     *
     * @param guid of the Entity to be retrieved
     * @return the entity context
     * @throws PropertyServerException if a configuration on the backend
     * @throws InvalidParameterException if parameter validation fails
     */
    @GetMapping( value = "/{guid}/context")
    public AssetElements getAssetContext(@PathVariable("guid") String guid)
            throws PropertyServerException, InvalidParameterException {
        String user = SecurityContextHolder.getContext().getAuthentication().getName( );
        return assetCatalogOMASService.getAssetContext(user, guid, "none");
    }


}
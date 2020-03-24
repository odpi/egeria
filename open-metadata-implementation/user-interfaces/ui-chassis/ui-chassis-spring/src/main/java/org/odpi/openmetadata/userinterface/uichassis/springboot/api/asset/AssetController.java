/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.asset;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetElement;
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
import java.util.Map;

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

    @GetMapping( path = "/types")
    public List<Type> getTypes() throws PropertyServerException, InvalidParameterException {
        String user = SecurityContextHolder.getContext().getAuthentication().getName( );
        return assetCatalogOMASService.getSupportedTypes(user);
    }

    @GetMapping( value = "/{guid}")
    public List<AssetDescription> getAsset(@PathVariable("guid") String guid)
            throws PropertyServerException, InvalidParameterException {
        String user = SecurityContextHolder.getContext().getAuthentication().getName( );
        return assetCatalogOMASService.getAssetDetails(user, guid, "none");
    }

    @GetMapping( value = "/{guid}/context")
    public List<AssetElements> getAssetContext(@PathVariable("guid") String guid)
            throws PropertyServerException, InvalidParameterException {
        String user = SecurityContextHolder.getContext().getAuthentication().getName( );
        return assetCatalogOMASService.getAssetContext(user, guid, "none");
    }


}
/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.asset;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetElements;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Type;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.service.AssetCatalogOMASService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AssetSearchController {

    @Autowired
    AssetCatalogOMASService assetCatalogOMASService;

    /**
     * @param searchCriteria the query parameter with the search phrase
     * @return list of assets
     */
    @GetMapping( path = "/assets/search")
    public List<AssetElements> searchAssets(@RequestParam("q") String searchCriteria, @RequestParam("types") List<String> types) throws PropertyServerException, InvalidParameterException {
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


}
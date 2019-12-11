/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.search;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetElements;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.service.AssetCatalogOMASService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AssetSearchController {

    @Autowired
    AssetCatalogOMASService assetCatalogOMASService;

    /**
     * @param searchCriteria the query parameter with the search phrase
     * @return list of assets
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/search")
    public List<AssetElements> searchAssets(@RequestParam("q") String searchCriteria, @RequestParam("types") List<String> types) throws PropertyServerException, InvalidParameterException {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        SearchParameters searchParameters = new SearchParameters();
        if(CollectionUtils.isNotEmpty(types) ) {
            searchParameters.setEntityTypes(types);
        }
        return assetCatalogOMASService.searchAssets(user, searchCriteria, searchParameters);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/types")
    public Map<String, String> getTypes()  {
        Map<String, String> types = new HashMap();
        types.put("Process", "d8f33bd7-afa9-4a11-a8c7-07dcec83c050");
        types.put("RelationalTable", "ce7e72b8-396a-4013-8688-f9d973067425");
        types.put("RelationalColumn", "aa8d5470-6dbc-4648-9e2f-045e5df9d2f9");
        types.put("GlossaryTerm", "0db3e6ec-f5ef-4d75-ae38-b7ee6fd6ec0a");
        types.put("Database", "0921c83f-b2db-4086-a52c-0d10e52ca078");//TODO types are temporary hardcoded until service to return types is created in asset catalog
        return types;
    }


}
/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.search;

import org.odpi.openmetadata.accessservices.assetcatalog.model.Term;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.userinterface.accessservices.service.AssetCatalogOMASService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assets/search")
public class AssetSearchController {

    @Autowired
    AssetCatalogOMASService assetCatalogOMASService;

    /**
     * @param searchCriteria the query parameter with the search phrase
     * @return list of assets
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Term> searchAssets(@RequestParam("searchCriteria") String searchCriteria) throws PropertyServerException, InvalidParameterException {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        return assetCatalogOMASService.searchAssets(user, searchCriteria, new SearchParameters());
    }

}
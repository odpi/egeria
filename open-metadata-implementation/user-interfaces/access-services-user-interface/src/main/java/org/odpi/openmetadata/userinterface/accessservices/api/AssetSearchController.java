/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.api;

import org.odpi.openmetadata.accessservices.assetcatalog.exception.InvalidParameterException;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.PropertyServerException;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.userinterface.accessservices.service.AssetCatalogOMASService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assets/search")
public class AssetSearchController {

    @Autowired
    AssetCatalogOMASService omasService;

    @RequestMapping(method = RequestMethod.GET)
    public List<AssetDescription>  searchAssets(@RequestParam("q") String q) {
        try {
            return omasService.searchAssets(q);
        } catch (PropertyServerException | InvalidParameterException e) {
            handleExceprion(e);
            return null;
        }
    }

    private void handleExceprion(Exception e){
        if(e instanceof InvalidParameterException){
            throw new IllegalArgumentException(e.getMessage());
        }
        throw new RuntimeException("Unknown exception! " + e.getMessage());
    }
}

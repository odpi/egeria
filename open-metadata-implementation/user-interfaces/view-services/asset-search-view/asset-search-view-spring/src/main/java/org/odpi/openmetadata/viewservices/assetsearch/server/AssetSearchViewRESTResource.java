/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetsearch.server;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetElements;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Type;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.userinterface.common.ffdc.DependantServerNotAvailableException;
import org.odpi.openmetadata.userinterface.security.springboot.securitycontrollers.SecureController;
import org.odpi.openmetadata.viewservices.assetsearch.services.AssetSearchViewRESTServices;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/asset-search")
@DependsOn("securityConfig")
public class AssetSearchViewRESTResource extends SecureController {
    private AssetSearchViewRESTServices restAPI = new AssetSearchViewRESTServices();
    private static String serviceName = ViewServiceDescription.ASSET_SEARCH.getViewServiceName();


    /**
     * @param serverName local UI server name
     * @param searchCriteria the query parameter with the search phrase
     * @return list of asset elements
     * @throws PropertyServerException property server exception
     * @throws InvalidParameterException invalid or null parameter
     */
    @RequestMapping(method = RequestMethod.GET, path = "/search")
    public List<AssetElements>  searchAssets(@PathVariable("serverName") String serverName,
                                   @RequestParam("q") String searchCriteria, @RequestParam("types") List<String> types,
                                   HttpServletRequest request) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException, DependantServerNotAvailableException {
        String userId = getUser(request);
        List<AssetElements>  assetElementList = null;
        if (userId == null) {
            //TODO sort out how to do the error processing properly. Git issue #2015 raised
            throw new org.odpi.openmetadata.userinterface.security.springboot.exceptions.UserNotAuthorizedException("User not authorised");
        } else {
            SearchParameters searchParameters = new SearchParameters();
            if(CollectionUtils.isNotEmpty(types) ) {
                searchParameters.setEntityTypes(types);
            }
            assetElementList = restAPI.searchAssets(serverName, userId, searchCriteria, searchParameters);
        }
        return assetElementList;
    }
//    @RequestMapping(method = RequestMethod.GET, path = "/types")
//    public Map<String, String> getTypes()  {
//        Map<String, String> types = new HashMap();
//        types.put("Process", "d8f33bd7-afa9-4a11-a8c7-07dcec83c050");
//        types.put("RelationalTable", "ce7e72b8-396a-4013-8688-f9d973067425");
//        types.put("RelationalColumn", "aa8d5470-6dbc-4648-9e2f-045e5df9d2f9");
//        types.put("GlossaryTerm", "0db3e6ec-f5ef-4d75-ae38-b7ee6fd6ec0a");
//        types.put("Database", "0921c83f-b2db-4086-a52c-0d10e52ca078");
//        //TODO types are temporary hardcoded until service to return types is created in asset catalog
//        return types;
//    }


    @RequestMapping(method = RequestMethod.GET, path = "/types")
    public List<Type> getTypes(@PathVariable("serverName") String serverName, HttpServletRequest request) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException, DependantServerNotAvailableException {
        String userId = getUser(request);
        return restAPI.getSupportedTypes(serverName,userId);
    }

}
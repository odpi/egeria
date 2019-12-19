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
    @GetMapping(path = "/search")
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


    @GetMapping(path = "/types")
    public List<Type> getTypes(@PathVariable("serverName") String serverName, HttpServletRequest request) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException, DependantServerNotAvailableException {
        String userId = getUser(request);
        return restAPI.getSupportedTypes(serverName,userId);
    }

}
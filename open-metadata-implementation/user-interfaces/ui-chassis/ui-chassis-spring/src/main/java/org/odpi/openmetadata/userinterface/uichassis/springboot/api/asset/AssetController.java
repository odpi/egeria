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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.userinterface.uichassis.springboot.service.AssetCatalogOMASService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    @Autowired
    AssetCatalogOMASService assetCatalogOMASService;

    /**
     *
     * @param searchCriteria the query parameter with the search phrase
     * @param types OM types list to search for
     * @param sequencingProperty name of the property based on which to sort the result
     * @param sequencingOrder PROPERTY_ASCENDING or PROPERTY_DESCENDING
     * @param caseSensitive set case sensitive flag
     * @param exactMatch set exact match flag
     * @param from the offset for the results
     * @param pageSize the number of results per page
     * @return list of assets
     * @throws PropertyServerException if a configuration on the backend
     * @throws InvalidParameterException if parameter validation fails
     */
    @GetMapping( path = "/search")
    public List<AssetElements> searchAssets(@RequestParam("q") String searchCriteria,
                                            @RequestParam("types") List<String> types,
                                            @RequestParam(name = "sequencingProperty", defaultValue = "displayName")
                                                        String sequencingProperty,
                                            @RequestParam(name = "sequencingOrder", defaultValue = "PROPERTY_ASCENDING")
                                                        SequencingOrder sequencingOrder,
                                            @RequestParam(defaultValue="false")  boolean caseSensitive,
                                            @RequestParam(defaultValue="false") boolean exactMatch,
                                            @RequestParam(defaultValue="0") Integer from,
                                            @RequestParam(defaultValue="10") Integer pageSize)
            throws PropertyServerException, InvalidParameterException {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        SearchParameters searchParameters = new SearchParameters();
        if(CollectionUtils.isNotEmpty(types) ) {
            searchParameters.setEntityTypes(types);
        }
        searchParameters.setPageSize(pageSize);
        searchParameters.setFrom(from);
        searchParameters.setSequencingProperty(sequencingProperty);
        searchParameters.setSequencingOrder(sequencingOrder);
        searchParameters.setCaseInsensitive(!caseSensitive);
        searchParameters.setExactMatch(exactMatch);
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
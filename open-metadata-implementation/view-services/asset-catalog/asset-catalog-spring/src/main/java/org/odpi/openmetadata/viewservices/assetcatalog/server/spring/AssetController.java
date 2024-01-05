/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.search.SequencingOrder;
import org.odpi.openmetadata.viewservices.assetcatalog.beans.AssetCatalogBean;
import org.odpi.openmetadata.viewservices.assetcatalog.beans.Elements;
import org.odpi.openmetadata.viewservices.assetcatalog.beans.Type;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.AssetCatalogResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.AssetCatalogSupportedTypes;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.AssetListResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.AssetResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.server.AssetCatalogRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/asset-catalog/old/assets")

@Tag(name="API: Asset Catalog OMVS",
     description="Search for assets, retrieve their properties, lineage and related glossary information.",
     externalDocs=@ExternalDocumentation(description="Further Information", url="https://egeria-project.org/services/omvs/asset-catalog/overview/"))

/**
 * Original REST Resource
 */
@Deprecated
public class AssetController
{
    private final RESTExceptionHandler exceptionHandler = new RESTExceptionHandler();

    private final AssetCatalogRESTServices restAPI = new AssetCatalogRESTServices();

    /**
     * Return a list of assets matching the search criteria without the full context
     *
     * @param serverName name of the server to route the request to
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
     * @throws UserNotAuthorizedException security access problem
     * @throws InvalidParameterException if parameter validation fails
     */
    @GetMapping( path = "/search")
    public List<Elements> searchAssets(@PathVariable String serverName,
                                       @RequestParam("q") String searchCriteria,
                                       @RequestParam("types") List<String> types,
                                       @RequestParam(name = "sequencingProperty", defaultValue = "displayName") String sequencingProperty,
                                       @RequestParam(name = "sequencingOrder", defaultValue = "PROPERTY_ASCENDING") SequencingOrder sequencingOrder,
                                       @RequestParam(defaultValue="false")  boolean caseSensitive,
                                       @RequestParam(defaultValue="false") boolean exactMatch,
                                       @RequestParam(defaultValue="0") Integer from,
                                       @RequestParam(defaultValue="10") Integer pageSize) throws PropertyServerException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 InvalidParameterException
    {
        final String methodName = "searchAssets";

        AssetListResponse restResult = restAPI.searchAssets(serverName,
                                                            searchCriteria,
                                                            types,
                                                            sequencingProperty,
                                                            sequencingOrder,
                                                            caseSensitive,
                                                            exactMatch,
                                                            from,
                                                            pageSize);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult.getElementsList();
    }


    /**
     * Return a list of assets matching the type name without the full context
     * The list includes also subtypes
     *
     * @param serverName name of the server to route the request to
     * @param typeName the assets type name to search for
     * @return list of assets by type name
     * @throws PropertyServerException if a configuration on the backend
     * @throws UserNotAuthorizedException security access problem
     * @throws InvalidParameterException if parameter validation fails
     */
    @GetMapping( path = "/search-by-type-name/{typeName}")
    public List<Elements> searchAssetsByTypeName(@PathVariable String serverName,
                                                 @PathVariable("typeName") String typeName) throws PropertyServerException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   InvalidParameterException
    {
        final String methodName = "searchAssetsByTypeName";

        AssetListResponse restResult = restAPI.searchAssetsByTypeName(serverName, typeName);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult.getElementsList();
    }

    /**
     * Return a list of assets matching the type GUID without the full context
     * The list includes also subtypes
     *
     * @param serverName name of the server to route the request to
     * @param typeGUID the assets type GUID to search for
     * @return list of assets by type GUID
     * @throws PropertyServerException if a configuration on the backend
     * @throws UserNotAuthorizedException security access problem
     * @throws InvalidParameterException if parameter validation fails
     */
    @GetMapping( path = "/search-by-type-guid/{typeGUID}")
    public List<Elements> searchAssetsByTypeGUID(@PathVariable String serverName,
                                                 @PathVariable("typeGUID") String typeGUID) throws PropertyServerException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   InvalidParameterException
    {
        final String methodName = "searchAssetsByTypeGUID";

        AssetListResponse restResult = restAPI.searchAssetsByTypeGUID(serverName, typeGUID);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult.getElementsList();
    }

    /**
     * Returns the list with supported types for search, including the subtypes supported
     *
     * @param serverName name of the server to route the request to
     * @return the supported types from Asset Consumer OMAS
     * @throws PropertyServerException if a configuration on the backend
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException security access problem
     */
    @GetMapping( path = "/types")
    public List<Type> getSupportedTypes(@PathVariable String serverName) throws PropertyServerException,
                                                                                UserNotAuthorizedException,
                                                                                InvalidParameterException
    {
        final String methodName = "getSupportedTypes";

        AssetCatalogSupportedTypes restResult = restAPI.getSupportedTypes(serverName);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult.getTypes();
    }


    /**
     * Fetch asset's header, classification and properties.
     *
     * @param serverName name of the server to route the request to
     * @param guid of the Entity to be retrieved
     * @return the entity details
     * @throws PropertyServerException if a configuration on the backend
     * @throws UserNotAuthorizedException security access problem
     * @throws InvalidParameterException if parameter validation fails
     */
    @GetMapping( value = "/{guid}")
    public AssetCatalogBean getAsset(@PathVariable String serverName,
                                     @PathVariable("guid") String guid) throws PropertyServerException,
                                                                               UserNotAuthorizedException,
                                                                               InvalidParameterException
    {
        final String methodName = "getAsset";

        AssetCatalogResponse restResult = restAPI.getAsset(serverName, guid);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult.getAssetCatalogBean();
    }


    /**
     * Return the full context of an asset/glossary term based on its identifier.
     * The response contains the list of the connections assigned to the asset.
     *
     * @param serverName name of the server to route the request to
     * @param guid of the Entity to be retrieved
     * @return the entity context
     * @throws PropertyServerException if a configuration on the backend
     * @throws UserNotAuthorizedException security access problem
     * @throws InvalidParameterException if parameter validation fails
     */
    @GetMapping( value = "/{guid}/context")
    public Elements getAssetContext(@PathVariable String serverName,
                                    @PathVariable("guid") String guid) throws PropertyServerException,
                                                                              UserNotAuthorizedException,
                                                                              InvalidParameterException
    {
        final String methodName = "getAssetContext";

        AssetResponse restResult = restAPI.getAssetContext(serverName, guid);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult.getAsset();
    }
}
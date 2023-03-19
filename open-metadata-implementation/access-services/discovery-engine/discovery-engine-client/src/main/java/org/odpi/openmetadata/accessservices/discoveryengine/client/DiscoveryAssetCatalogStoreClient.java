/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.client;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryAssetCatalogStore;

import java.util.List;

/**
 * DiscoveryAssetCatalogStoreClient is the open metadata default implementation of the Open Discovery Framework (ODF)
 * DiscoveryAssetCatalogStore.  It uses the open metadata services through the Discovery Engine OMAS to provide access
 * to the requested assets.
 */
public class DiscoveryAssetCatalogStoreClient extends DiscoveryAssetCatalogStore
{
    private DiscoveryEngineClient discoveryEngineClient;    /* Initialized in constructor */

    /**
     * Create a discovery asset catalog store that call call the discovery engine client.
     *
     * @param userId calling user
     * @param discoveryEngineClient client for the Discovery Engine OMAS
     * @param maxPageSize maximum number of results to return.
     */
    public DiscoveryAssetCatalogStoreClient(String                userId,
                                            DiscoveryEngineClient discoveryEngineClient,
                                            int                   maxPageSize)
    {
        super(userId, maxPageSize);

        this.discoveryEngineClient = discoveryEngineClient;
    }


    /**
     * Return the next set of assets to process.
     *
     * @param startFrom starting point of the query
     * @param pageSize maximum number of results to return
     * @return list of unique identifiers for located assets
     * @throws InvalidParameterException one of the parameters is not recognized
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    @Override
    public List<String> getAssets(int  startFrom,
                                  int  pageSize) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        return discoveryEngineClient.getAssets(userId, startFrom, pageSize);
    }


    /**
     * Return the assets with the same qualified name.  If all is well there should be only one
     * returned.
     *
     * @param name the qualified name to query on
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @return list of unique identifiers for matching assets
     * @throws InvalidParameterException one of the parameters is not recognized
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    @Override
    public List<String>  getAssetsByQualifiedName(String   name,
                                                  int      startFrom,
                                                  int      pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return discoveryEngineClient.getAssetsByQualifiedName(userId, name, startFrom, pageSize);
    }


    /**
     * Return the list of matching assets that have the supplied name as either the
     * qualified name or display name.  This is an exact match retrieval.
     *
     * @param name name to query for
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @return list of unique identifiers for matching assets
     * @throws InvalidParameterException one of the parameters is not recognized
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    @Override
    public List<String>  getAssetsByName(String   name,
                                         int      startFrom,
                                         int      pageSize) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        return discoveryEngineClient.getAssetsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Return the list of assets that have the search string somewhere in their properties.
     * The search string may be a regular expression.
     *
     * @param searchString value to search for
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @return list of unique identifiers for matching assets
     * @throws InvalidParameterException one of the parameters is not recognized
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    @Override
    public List<String> findAssets(String   searchString,
                                   int      startFrom,
                                   int      pageSize) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        return discoveryEngineClient.findAssets(userId, searchString, startFrom, pageSize);
    }


    /**
     * Return the list of assets that have the same endpoint address.
     *
     * @param networkAddress address to query on
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @return list of unique identifiers for matching assets
     * @throws InvalidParameterException one of the parameters is not recognized
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    @Override
    public  List<String> getAssetsByEndpoint(String   networkAddress,
                                             int      startFrom,
                                             int      pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return discoveryEngineClient.findAssetsByEndpoint(userId, networkAddress, startFrom, pageSize);
    }
}

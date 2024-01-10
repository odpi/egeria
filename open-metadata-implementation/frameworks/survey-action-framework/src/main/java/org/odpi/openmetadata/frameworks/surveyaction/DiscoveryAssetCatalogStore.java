/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.surveyaction;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * DiscoveryAssetCatalogStore provides access to the asset catalog to enable it to step through the
 * assets available in the asset catalog.
 */
public abstract class DiscoveryAssetCatalogStore
{
    protected String  userId;
    protected int     maxPageSize;

    /**
     * Constructor sets the max page size which is used by the caller to ensure they
     * do not ask for too many assets at once.
     *
     * @param userId calling user
     * @param maxPageSize maximum number of assets that can be returned on a single request.
     */
    public DiscoveryAssetCatalogStore(String  userId,
                                      int maxPageSize)
    {
        this.userId = userId;
        this.maxPageSize = maxPageSize;
    }


    /**
     * Return the maximum number of assets that can be returned by getAssets().
     *
     * @return integer
     */
    public int getMaxPageSize()
    {
        return maxPageSize;
    }


    /**
     * Return the next set of assets to process.
     *
     * @param startFrom starting point of the query
     * @param pageSize maximum number of results to return
     * @return list of unique identifiers for matching assets
     * @throws InvalidParameterException one of the parameters is not recognized
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    public abstract List<String>  getAssets(int  startFrom,
                                            int  pageSize) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


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
    public abstract List<String>  getAssetsByQualifiedName(String   name,
                                                           int      startFrom,
                                                           int      pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


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
    public abstract List<String>  getAssetsByName(String   name,
                                                  int      startFrom,
                                                  int      pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


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
    public abstract List<String> findAssets(String   searchString,
                                            int      startFrom,
                                            int      pageSize) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


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
    public abstract List<String> getAssetsByEndpoint(String   networkAddress,
                                                     int      startFrom,
                                                     int      pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;
}

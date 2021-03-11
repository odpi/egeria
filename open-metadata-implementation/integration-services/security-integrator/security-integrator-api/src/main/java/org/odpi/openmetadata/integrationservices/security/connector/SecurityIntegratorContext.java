/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.security.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.DataAssetExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.DataAssetElement;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;


/**
 * SecurityIntegratorContext provides a wrapper around the Community Profile OMAS client.
 * It provides the simplified interface to open metadata needed by the SecurityIntegratorConnector.
 */
public class SecurityIntegratorContext
{
    private DataAssetExchangeClient dataAssetExchangeClient;

    String                   userId;
    String                   assetManagerGUID;
    String                   assetManagerName;
    String                   connectorName;
    AuditLog                 auditLog;


    /**
     * Create a new client to exchange data asset content with open metadata.
     *
     * @param dataAssetExchangeClient client for exchange requests
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param auditLog logging destination
     */
    public SecurityIntegratorContext(DataAssetExchangeClient dataAssetExchangeClient,
                                     String userId,
                                     String assetManagerGUID,
                                     String assetManagerName,
                                     String connectorName,
                                     AuditLog auditLog)
    {
        this.dataAssetExchangeClient = dataAssetExchangeClient;

        this.userId                   = userId;
        this.assetManagerGUID         = assetManagerGUID;
        this.assetManagerName         = assetManagerName;
        this.connectorName            = connectorName;
        this.auditLog                 = auditLog;
    }


    /**
     * Retrieve the list of asset metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement> findAssets(String searchString,
                                             int    startFrom,
                                             int    pageSize) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        return dataAssetExchangeClient.findDataAssets(userId, assetManagerGUID, assetManagerName, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of asset metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement>   getAssetsByName(String name,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        return dataAssetExchangeClient.getDataAssetsByName(userId, assetManagerGUID, assetManagerName, name, startFrom, pageSize);
    }


    /**
     * Step through the assets visible to this caller.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement>   scanAssets(int startFrom,
                                               int pageSize) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        return dataAssetExchangeClient.scanDataAssets(userId, assetManagerGUID, assetManagerName, startFrom, pageSize);
    }


    /**
     * Retrieve the asset metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataAssetElement getAssetByGUID(String userId,
                                           String assetManagerGUID,
                                           String assetManagerName,
                                           String openMetadataGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return dataAssetExchangeClient.getDataAssetByGUID(userId, assetManagerGUID, assetManagerName, openMetadataGUID);
    }
}

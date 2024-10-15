/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.context;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;

public class ConnectedAssetContext
{
    private final String                userId;
    private final OpenIntegrationClient openIntegrationClient;

    /**
     * Constructor sets up the key parameters for accessing the asset store.
     *
     * @param userId calling user
     * @param openIntegrationClient client to make the calls
     */
    public ConnectedAssetContext(String                userId,
                                 OpenIntegrationClient openIntegrationClient)
    {
        this.userId = userId;
        this.openIntegrationClient = openIntegrationClient;
    }


    /**
     * Returns the unique identifier corresponding to the supplied connection.
     *
     * @param connection   the connection object that contains the properties needed to create the connection.
     *
     * @return guid
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     */
    public String saveConnection(Connection connection) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException,
                                                               ConnectionCheckedException,
                                                               ConnectorCheckedException
    {
        return openIntegrationClient.saveConnection(userId, connection);
    }


    /**
     * Returns the unique identifier corresponding to the supplied connection.
     *
     * @param assetGUID the unique identifier of an asset to attach the connection to
     * @param connection   the connection object that contains the properties needed to create the connection.
     *
     * @return guid
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     */
    public String saveConnection(String     assetGUID,
                                 Connection connection) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException,
                                                               ConnectionCheckedException,
                                                               ConnectorCheckedException
    {
        return openIntegrationClient.saveConnection(userId, assetGUID, connection);
    }


    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @param assetGUID the unique identifier of an asset to attach the connection to
     * @return a comprehensive collection of properties about the asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AssetUniverse getAssetProperties(String assetGUID) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        return openIntegrationClient.getAssetProperties(userId, assetGUID);
    }


    /**
     * Return the connector to the requested asset.
     *
     * @param assetGUID the unique identifier of an asset to attach the connection to
     * @param auditLog    optional logging destination
     * @return Open Connector Framework (OCF) connector
     * @throws InvalidParameterException the asset guid is not recognized or the userId is null
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection needed to
     *                                    create the connector.
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    public  Connector  getConnectorToAsset(String   assetGUID,
                                           AuditLog auditLog) throws InvalidParameterException,
                                                                     ConnectionCheckedException,
                                                                     ConnectorCheckedException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        return openIntegrationClient.getConnectorToAsset(userId, assetGUID, auditLog);
    }
}

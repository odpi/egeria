/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;


/**
 * ConnectorFactoryService is the service for creating connectors to the data and services of a digital resource (represented by an asset).
 */
public class ConnectorFactoryService
{
    private final ConnectedAssetClient        connectedAssetClient;
    private final String                      userId;

    /**
     * Create a new client to exchange data asset content with open metadata.
     *
     * @param connectedAssetClient client for exchange requests
     * @param userId integration daemon's userId
     */
    ConnectorFactoryService(ConnectedAssetClient     connectedAssetClient,
                            String                   userId)
    {
        this.connectedAssetClient        = connectedAssetClient;
        this.userId                      = userId;
    }


    /*
     * ===============================================
     * ConnectorFactoryInterface
     * ===============================================
     */


    /**
     * Returns the connector corresponding to the supplied connection name.
     *
     * @param connectionName   this may be the qualifiedName or displayName of the connection.
     *
     * @return   connector instance - or null if there is no connection
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public Connector getConnectorByName(String connectionName) throws InvalidParameterException,
                                                                      ConnectionCheckedException,
                                                                      ConnectorCheckedException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        return connectedAssetClient.getConnectorByName(userId, connectionName);
    }


    /**
     * Returns the connector corresponding to the supplied asset GUID.
     *
     * @param assetGUID   the unique id for the asset within the metadata repository.
     * @param auditLog    optional logging destination
     *
     * @return    connector instance - or null if there is no connection
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public Connector getConnectorForAsset(String   assetGUID,
                                          AuditLog auditLog) throws InvalidParameterException,
                                                                    ConnectionCheckedException,
                                                                    ConnectorCheckedException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return connectedAssetClient.getConnectorForAsset(userId, assetGUID, auditLog);
    }


    /**
     * Returns the connector corresponding to the supplied connection GUID.
     *
     * @param connectionGUID   the unique id for the connection within the metadata repository.
     *
     * @return  connector instance - or null if there is no connection
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public Connector getConnectorByGUID(String connectionGUID) throws InvalidParameterException,
                                                                      ConnectionCheckedException,
                                                                      ConnectorCheckedException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        return connectedAssetClient.getConnectorByGUID(userId, connectionGUID);
    }


    /**
     * Returns the connector corresponding to the supplied connection.
     *
     * @param connection   the connection object that contains the properties needed to create the connection.
     *
     * @return  connector instance
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     */
    public Connector  getConnectorByConnection(Connection connection) throws InvalidParameterException,
                                                                             ConnectionCheckedException,
                                                                             ConnectorCheckedException
    {
        return connectedAssetClient.getConnectorByConnection(userId, connection);
    }
}

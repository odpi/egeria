/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery;

import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.discovery.ffdc.ODFErrorCode;

/**
 * DiscoveryAssetStore defines the interface to a connector broker backed by a metadata store that returns
 * information about the Asset that a Discovery Engine is to analyze.  The userId that made the discovery
 * request is the default user for the asset store.  This userId may be over-ridden by the discovery engine.
 */
public abstract class DiscoveryAssetStore
{
    protected String        assetGUID;
    protected String        userId;
    protected Connection    assetConnection = null;



    /**
     * Constructor sets up the key parameters for accessing the asset store.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the annotations should be attached to
     */
    public DiscoveryAssetStore(String assetGUID,
                               String userId)
    {
        this.assetGUID = assetGUID;
        this.userId = userId;
    }


    /**
     * Return the connector for a connection.
     *
     * @param connection connection properties to create the connector
     * @return connector to the asset
     * @throws InvalidParameterException the connection object is not valid
     * @throws PropertyServerException the connector is not operating correctly
     */
    private   Connector  getConnectorFromConnection(Connection   connection) throws InvalidParameterException,
                                                                                    PropertyServerException
    {
        final String    methodName = "getConnectorFromConnection";

        ConnectorBroker  connectorBroker = new ConnectorBroker();

        try
        {
            return connectorBroker.getConnector(connection);
        }
        catch (ConnectionCheckedException  error)
        {
            ODFErrorCode errorCode = ODFErrorCode.INVALID_ASSET_CONNECTION;
            String       errorMessage = errorCode.getErrorMessageId()
                                      + errorCode.getFormattedErrorMessage(assetGUID, error.getErrorMessage(), connection.toString());

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                error,
                                                "connection");
        }
        catch (ConnectorCheckedException  error)
        {
            ODFErrorCode errorCode    = ODFErrorCode.INVALID_ASSET_CONNECTOR;
            String       errorMessage = errorCode.getErrorMessageId()
                                      + errorCode.getFormattedErrorMessage(assetGUID, error.getErrorMessage(), connection.toString());

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Return the connection information for the asset.  This is used to create the connector.  The connector
     * is an Open Connector Framework (OCF) connector that provides access to the asset's data and metadata properties.
     *
     * @return Connection bean
     * @throws InvalidParameterException the asset guid is not recognized
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    protected abstract Connection  getConnectionForAsset() throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Return the connector to the requested asset.
     *
     * @return Open Connector Framework (OCF) connector
     * @throws InvalidParameterException the asset guid is not recognized
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection needed to
     *                                    create the connector.
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    public Connector  getConnectorToAsset() throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException
    {
        if (assetConnection == null)
        {
            assetConnection = getConnectionForAsset();
        }

        return getConnectorFromConnection(assetConnection);
    }
}

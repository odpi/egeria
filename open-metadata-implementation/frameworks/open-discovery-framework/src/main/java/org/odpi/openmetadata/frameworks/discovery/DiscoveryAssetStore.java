/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery;

import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
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
     * Returns the connector corresponding to the supplied connection.
     *
     * @param userId       userId of user making request.
     * @param connection   the connection object that contains the properties needed to create the connection.
     *
     * @return Connector   connector instance
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     */
    protected abstract Connector getConnectorByConnection(String     userId,
                                                          Connection connection) throws InvalidParameterException,
                                                                                        ConnectionCheckedException,
                                                                                        ConnectorCheckedException;


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
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @param userId         userId of user making request.
     * @param assetGUID      unique identifier for asset.
     *
     * @return a comprehensive collection of properties about the asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected abstract AssetUniverse getAssetProperties(String userId,
                                                        String assetGUID) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException;


    /**
     * Return the connector to the requested asset.
     *
     * @return Open Connector Framework (OCF) connector
     * @throws InvalidParameterException the asset guid is not recognized or the userId is null
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection needed to
     *                                    create the connector.
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    public Connector  getConnectorToAsset() throws InvalidParameterException,
                                                   ConnectionCheckedException,
                                                   ConnectorCheckedException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException
    {
        if (assetConnection == null)
        {
            assetConnection = getConnectionForAsset();
        }

        return this.getConnectorByConnection(userId, assetConnection);
    }
}

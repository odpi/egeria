/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.client;

import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryAssetStore;


/**
 * DiscoveryAssetStoreClient provides the client-side library for the Open Discovery Framework (ODF)'s
 * Discovery Asset Store that provides a Discovery service with access to the connector for the
 * asset to be discovered.  From the connector, the Discovery service is able to extract the known properties
 * about the asset and access its data.
 * <br><br>
 * An instance of this client is created for each discovery service instance that runs.  This is
 * why the REST client is passed in on the constructor (since creating a new RestTemplate object is
 * very expensive).
 */
public class DiscoveryAssetStoreClient extends DiscoveryAssetStore
{
    private final DiscoveryEngineClient discoveryEngineClient;    /* Initialized in constructor */

    /**
     * Constructor sets up the key parameters for accessing the asset store.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the annotations should be attached to
     * @param discoveryEngineClient client for calling REST APIs
     */
    public DiscoveryAssetStoreClient(String                assetGUID,
                                     String                userId,
                                     DiscoveryEngineClient discoveryEngineClient)
    {
        super(assetGUID, userId);

        this.discoveryEngineClient = discoveryEngineClient;
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
    @Override
    protected  Connection  getConnectionForAsset() throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        return discoveryEngineClient.getConnectionForAsset(userId, assetGUID);
    }


    /**
     * Returns the connector corresponding to the supplied connection.
     *
     * @param connection   the connection object that contains the properties needed to create the connection.
     *
     * @return Connector  instance
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     */
    @Override
    public Connector getConnectorByConnection(Connection connection) throws InvalidParameterException,
                                                                            ConnectionCheckedException,
                                                                            ConnectorCheckedException
    {
        return discoveryEngineClient.getConnectorForConnection(userId, connection);
    }


    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @return a comprehensive collection of properties about the asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public AssetUniverse getAssetProperties() throws InvalidParameterException,
                                                     PropertyServerException,
                                                     UserNotAuthorizedException
    {
        return discoveryEngineClient.getAssetProperties(userId, assetGUID);
    }


    /**
     * Log an audit message about this asset.
     *
     * @param discoveryService name of discovery service
     * @param message message to log
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void logAssetAuditMessage(String   discoveryService,
                                     String   message) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        discoveryEngineClient.logAssetAuditMessage(userId, assetGUID, discoveryService, message);
    }
}

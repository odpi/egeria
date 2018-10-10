/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;


import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectedAssetProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;

/**
 * <p>
 * The Connector is the interface for all connector instances.   Connectors are client-side interfaces to assets
 * such as data stores, data sets, APIs, analytical functions.  They handle the communication with the server that
 * hosts the assets, along with the communication with the metadata server to serve up metadata (properties) about
 * the assets.
 * </p>
 * <p>
 * Each connector implementation is paired with a connector provider.  The connector provider is the factory for
 * connector instances.
 * </p>
 * <p>
 * The Connector interface defines that a connector instance should be able to return a unique
 * identifier, a connection object and a metadata object called ConnectedAssetProperties.
 * </p>
 * <p>
 * Each specific implementation of a connector then extends the Connector interface to add the methods to work with the
 * particular type of asset it supports.  For example, a JDBC connector would add the standard JDBC SQL interface, the
 * OMRS Connectors add the metadata repository management APIs...
 * </p>
 * <p>
 * The initialize() method is called by the Connector Provider to set up the connector instance Id and the
 * Connection properties for the connector as part of its construction process.
 * </p>
 * <p>
 * ConnectedAssetProperties provides descriptive properties about the asset that the connector is accessing.
 * It is supplied to the connector later during its initialization through the initializeConnectedAssetProperties() method.
 * See AssetConsumer OMAS for an example of this.
 * </p>
 * <p>
 * Both the connector and the connector provider have base classes (ConnectorBase and
 * ConnectorProviderBase respectively) that implement all of the standard methods.  The connector developer extends
 * these classes to add the specific methods to manage the asset and configure the base classes.
 * </p>
 */
public abstract class Connector
{
    /**
     * Call made by the ConnectorProvider to initialize the Connector with the base services.
     *
     * @param connectorInstanceId   unique id for the connector instance   useful for messages etc
     * @param connection   POJO for the configuration used to create the connector.
     */
    public abstract void initialize(String               connectorInstanceId,
                                    ConnectionProperties connection);


    /**
     * Returns the unique connector instance id that assigned to the connector instance when it was created.
     * It is useful for error and audit messages.
     *
     * @return guid for the connector instance
     */
    public abstract String getConnectorInstanceId();


    /**
     * Returns the connection object that was used to create the connector instance.  Its contents are never refreshed
     * during the lifetime of a connector instance even if the connection information is updated or removed from
     * the originating metadata repository.
     *
     * @return connection properties object
     */
    public abstract ConnectionProperties getConnection();


    /**
     * Set up the connected asset properties object.  This provides the known metadata properties stored in one or more
     * metadata repositories.  The implementation of the connected asset properties object is free to determine when
     * the properties are populated.  It may be as lazy as whenever getConnectedAssetProperties() is called.
     *
     * @param connectedAssetProperties   properties of the connected asset
     */
    public abstract void initializeConnectedAssetProperties(ConnectedAssetProperties connectedAssetProperties);


    /**
     * Returns the properties that contain the metadata for the asset.  The asset metadata is retrieved from the
     * metadata repository and cached in the ConnectedAssetProperties object each time the getConnectedAssetProperties
     * method is requested by the caller.   Once the ConnectedAssetProperties object has the metadata cached, it can be
     * used to access the asset property values many times without a return to the metadata repository.
     * The cache of metadata can be refreshed simply by calling this getConnectedAssetProperties() method again.
     *
     * @param userId userId of requesting user
     * @return ConnectedAssetProperties   connected asset properties
     * @throws PropertyServerException indicates a problem retrieving properties from a metadata repository
     * @throws UserNotAuthorizedException indicates that the user is not authorized to access the asset properties.
     */
    public abstract ConnectedAssetProperties getConnectedAssetProperties(String userId) throws PropertyServerException,
                                                                                               UserNotAuthorizedException;


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public abstract void start() throws ConnectorCheckedException;



    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public abstract void disconnect() throws ConnectorCheckedException;
}
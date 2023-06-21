/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectedAssetProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The ConnectorBase is an implementation of the Connector interface.
 * <br><br>
 * Connectors are client-side interfaces to assets such as data stores, data sets, APIs, analytical functions.
 * They handle the communication with the server that hosts the assets, along with the communication with the
 * metadata server to serve up metadata about the assets, and support for an audit log for the caller to log its
 * activity.
 * <br><br>
 * Each connector implementation is paired with a connector provider.  The connector provider is the factory for
 * connector instances.
 * <br><br>
 * The Connector interface defines that a connector instance should be able to return a unique
 * identifier, a connection object and a metadata properties object for its connected asset.
 * These are supplied to the connector during its initialization.
 * <br><br>
 * The ConnectorBase base class implements all the methods required by the Connector interface.
 * Each specific implementation of a connector then extends this interface to add the methods to work with the
 * particular type of asset it supports.  For example, a JDBC connector would add the standard JDBC SQL interface, the
 * OMRS Connectors add the metadata repository management APIs...
 */
public abstract class ConnectorBase extends Connector implements SecureConnectorExtension
{
    protected String                   connectorInstanceId      = null;
    protected ConnectionProperties     connectionProperties     = null;
    protected Connection               connectionBean           = null;
    protected ConnectedAssetProperties           connectedAssetProperties = null;
    protected Map<String, SecretsStoreConnector> secretsStoreConnectorMap = new HashMap<>();

    private volatile boolean           isActive                 = false;

    private static final Logger   log = LoggerFactory.getLogger(ConnectorBase.class);

    private        final int      hashCode = UUID.randomUUID().hashCode();

    /**
     * Typical Constructor: Connectors should always have a constructor requiring no parameters and perform
     * initialization in the initialize method.
     */
    public  ConnectorBase()
    {
        /*
         * Nothing to do real initialization happens in the "initialize()" method.
         * This pattern is used to make it possible for ConnectorBrokerBase to support the dynamic loading and
         * instantiation of arbitrary connector instances without needing to know the specifics of their constructor
         * methods
         */
        log.debug("New Connector Requested");
    }


    /**
     * Call made by the ConnectorProvider to initialize the Connector with the base services.
     *
     * @param connectorInstanceId   unique id for the connector instance   useful for messages etc
     * @param connectionProperties   POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String               connectorInstanceId,
                           ConnectionProperties connectionProperties)
    {
        this.connectorInstanceId = connectorInstanceId;
        this.connectionProperties = connectionProperties;

        /*
         * Set up the secured properties and the connection bean
         */
        ProtectedConnection  protectedConnection = new ProtectedConnection(connectionProperties);
        this.connectionBean = protectedConnection.getConnectionBean();

        log.debug("New Connector initialized: " + connectorInstanceId + ", " + connectionProperties.getQualifiedName() + "," + connectionProperties.getDisplayName());
    }


    /**
     * Returns the unique connector instance id that assigned to the connector instance when it was created.
     * It is useful for error and audit messages.
     *
     * @return guid for the connector instance
     */
    @Override
    public String getConnectorInstanceId()
    {
        return connectorInstanceId;
    }


    /**
     * Returns the connection object that was used to create the connector instance.  Its contents are never refreshed
     * during the lifetime of the connector instance, even if the connection information is updated or removed
     * from the originating metadata repository.
     *
     * @return connection properties object
     */
    @Override
    public ConnectionProperties getConnection()
    {
        return connectionProperties;
    }


    /**
     * Set up the connected asset properties object.  This provides the known metadata properties stored in one or more
     * metadata repositories.  The properties are populated whenever getConnectedAssetProperties() is called.
     *
     * @param connectedAssetProperties   properties of the connected asset
     */
    @Override
    public void initializeConnectedAssetProperties(ConnectedAssetProperties connectedAssetProperties)
    {
        this.connectedAssetProperties = connectedAssetProperties;
    }


    /**
     * Set up information about an embedded connector that this connector can use to support secure access to its resources.
     *
     * @param displayName name of the secrets store
     * @param secretsStoreConnector an embedded secrets store connector
     */
    @Override
    public void initializeSecretsStoreConnector(String                displayName,
                                                SecretsStoreConnector secretsStoreConnector)
    {
        if (displayName != null)
        {
            secretsStoreConnectorMap.put(displayName, secretsStoreConnector);
        }
        else
        {
            secretsStoreConnectorMap.put(String.valueOf(secretsStoreConnectorMap.size()), secretsStoreConnector);
        }
    }


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
    @Override
    public ConnectedAssetProperties getConnectedAssetProperties(String userId) throws PropertyServerException, UserNotAuthorizedException
    {
        log.debug("ConnectedAssetProperties requested: " + connectorInstanceId + ", " + connectionProperties.getQualifiedName() + "," + connectionProperties.getDisplayName());

        if (connectedAssetProperties != null)
        {
            connectedAssetProperties.refresh();
        }

        return connectedAssetProperties;
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public synchronized void start() throws ConnectorCheckedException
    {
        isActive = true;
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  synchronized void disconnect() throws ConnectorCheckedException
    {
        isActive = false;
    }


    /**
     * Return a flag indicating whether the connector is active.  This means it has been started and not yet
     * disconnected.
     *
     * @return isActive flag
     */
    public synchronized boolean isActive()
    {
        return isActive;
    }


    /**
     * Provide a common implementation of hashCode for all OCF Connector objects.  The UUID is unique and
     * is randomly assigned and so its hashCode is as good as anything to describe the hash code of the connector
     * object.
     *
     * @return random UUID as hashcode
     */
    @Override
    public int hashCode()
    {
        return hashCode;
    }


    /**
     * Provide a common implementation of equals for all OCF Connector Provider objects.  The UUID is unique and
     * is randomly assigned and so its hashCode is as good as anything to evaluate the equality of the connector
     * provider object.
     *
     * @param object   object to test
     * @return boolean flag
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }

        ConnectorBase that = (ConnectorBase) object;

        return (hashCode == that.hashCode);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ConnectorBase{" +
                "connectorInstanceId='" + connectorInstanceId + '\'' +
                ", connectionProperties=" + connectionProperties +
                ", connectedAssetProperties=" + connectedAssetProperties +
                ", isActive=" + isActive +
                ", hashCode=" + hashCode +
                '}';
    }

    /**
     * ProtectedConnection provides a subclass to Connection in order to extract protected values from the
     * connection in order to supply them to the Connector implementation.
     */
    protected static class ProtectedConnection extends ConnectionProperties
    {
        /**
         * Copy/clone connector.
         *
         * @param templateConnection connection to copy
         */
        ProtectedConnection(ConnectionProperties templateConnection)
        {
            super(templateConnection);
        }

        /**
         * Return a copy of the ConnectionBean.
         *
         * @return Connection bean
         */
        @Override
        protected Connection getConnectionBean()
        {
            return super.getConnectionBean();
        }
    }
}
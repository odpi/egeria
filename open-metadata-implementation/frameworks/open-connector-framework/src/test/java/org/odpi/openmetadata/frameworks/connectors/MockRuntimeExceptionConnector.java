/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectedAssetProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The ConnectorBase is an implementation of the Connector interface.
 *
 * Connectors are client-side interfaces to assets such as data stores, data sets, APIs, analytical functions.
 * They handle the communication with the server that hosts the assets, along with the communication with the
 * metadata server to serve up metadata about the assets, and support for an audit log for the caller to log its
 * activity.
 *
 * Each connector implementation is paired with a connector provider.  The connector provider is the factory for
 * connector instances.
 *
 * The Connector interface defines that a connector instance should be able to return a unique
 * identifier, a connection object and a metadata properties object for its connected asset.
 * These are supplied to the connector during its initialization.
 *
 * The ConnectorBase base class implements all the methods required by the Connector interface.
 * Each specific implementation of a connector then extends this interface to add the methods to work with the
 * particular type of asset it supports.  For example, a JDBC connector would add the standard JDBC SQL interface, the
 * OMRS Connectors add the metadata repository management APIs...
 */
public abstract class MockRuntimeExceptionConnector extends ConnectorBase
{
    protected String                   connectorInstanceId      = null;
    protected ConnectionProperties     connectionProperties     = null;
    protected Connection               connectionBean           = null;
    protected ConnectedAssetProperties connectedAssetProperties = null;
    protected boolean                  isActive                 = false;

    /*
     * Secured properties are protected properties from the connection.  They are retrieved as a protected
     * variable to allow subclasses of ConnectorBase to access them.
     */
    protected Map<String, String> securedProperties = null;

    private static final int      hashCode = UUID.randomUUID().hashCode();
    private static final Logger   log = LoggerFactory.getLogger(MockRuntimeExceptionConnector.class);

    /**
     * Typical Constructor Connectors should always have a constructor requiring no parameters and perform
     * initialization in the initialize method.
     */
    public MockRuntimeExceptionConnector()
    {
        super();
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
        throw new OCFRuntimeException(OCFErrorCode.NO_MORE_ELEMENTS.getMessageDefinition("IteratorName"),
                                      this.getClass().getName(),
                                      "getCachedList");
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
     * Returns the properties that contain the metadata for the asset.  The asset metadata is retrieved from the
     * metadata repository and cached in the ConnectedAssetProperties object each time the getConnectedAssetProperties
     * method is requested by the caller.   Once the ConnectedAssetProperties object has the metadata cached, it can be
     * used to access the asset property values many times without a return to the metadata repository.
     * The cache of metadata can be refreshed simply by calling this getConnectedAssetProperties() method again.
     *
     * @return ConnectedAssetProperties   connected asset properties
     * @throws PropertyServerException indicates a problem retrieving properties from a metadata repository
     * @throws UserNotAuthorizedException authorization error
     */
    public ConnectedAssetProperties getConnectedAssetProperties() throws PropertyServerException, UserNotAuthorizedException
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
    public void start() throws ConnectorCheckedException
    {
        isActive = true;
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        isActive = false;
    }


    /**
     * Return a flag indicating whether the connector is active.  This means it has been started and not yet
     * disconnected.
     *
     * @return isActive flag
     */
    @Override
    public boolean isActive()
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

        MockRuntimeExceptionConnector that = (MockRuntimeExceptionConnector) object;

        if (connectorInstanceId != null ? !connectorInstanceId.equals(that.connectorInstanceId) : that.connectorInstanceId != null)
        {
            return false;
        }
        if (connectionProperties != null ? !connectionProperties.equals(that.connectionProperties) : that.connectionProperties != null)
        {
            return false;
        }
        return connectedAssetProperties != null ? connectedAssetProperties.equals(that.connectedAssetProperties) : that.connectedAssetProperties == null;
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
                '}';
    }


    /**
     * ProtectedConnection provides a subclass to Connection in order to extract protected values from the
     * connection in order to supply them to the Connector implementation.
     */
    private class ProtectedConnection extends ConnectionProperties
    {
        private static final long     serialVersionUID = 1L;

        private ProtectedConnection(ConnectionProperties templateConnection)
        {
            super(templateConnection);
        }

        /**
         * Return a copy of the secured properties.  Null means no secured properties are available.
         * This method is protected so only OCF (or subclasses) can access them.  When Connector is passed to calling
         * OMAS, the secured properties are not available.
         *
         * @return secured properties   typically user credentials for the connection
         */
        @Override
        protected Map<String, String> getSecuredProperties()
        {
            Map<String, String>  securedProperties = super.getConnectionBean().getSecuredProperties();
            if (securedProperties == null)
            {
                return null;
            }
            else if (securedProperties.isEmpty())
            {
                return null;
            }
            else
            {
                return new HashMap<>(securedProperties);
            }
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
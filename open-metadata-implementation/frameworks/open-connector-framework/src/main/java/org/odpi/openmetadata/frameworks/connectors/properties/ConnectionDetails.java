/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;

import java.util.Map;
import java.util.Objects;

/**
 * ConnectionDetails is an object that contains the properties needed to create and initialise a connector to access a
 * specific data assets.
 * <br><br>
 * The properties for ConnectionDetails are defined in model 0201.  They include the following options for connector name:
 * <ul>
 *     <li>
 *         guid - Globally unique identifier for the connection.
 *     </li>
 *     <li>
 *         url - URL of the connection definition in the metadata repository.
 *         This URL can be stored as a property in another entity to create an explicit link to this connection.
 *     </li>
 *     <li>
 *         qualifiedName - The official (unique) name for the connection.
 *         This is often defined by the IT systems management organization and should be used (when available) on
 *         audit logs and error messages.  The qualifiedName is defined in the 0010 model as part of Referenceable.
 *     </li>
 *     <li>
 *         displayName - A consumable name for the connection.   Often a shortened form of the qualifiedName for use
 *         on user interfaces and messages.  The displayName should be only be used for audit logs and error messages
 *         if the qualifiedName is not set.
 *     </li>
 * </ul>
 *  Either the guid, qualifiedName or displayName can be used to specify the name for a connection.
 *  Other properties for the connection include:
 *
 *  <ul>
 *      <li>
 *          type - information about the TypeDef for Connection
 *      </li>
 *      <li>
 *          description - A full description of the connection covering details of the assets it connects to
 *          along with usage and versioning information.
 *      </li>
 *      <li>
 *          additionalProperties - Any additional properties associated with the connection.
 *      </li>
 *      <li>
 *          configurationProperties - Any specific configuration properties for the underlying technology.
 *      </li>
 *      <li>
 *          securedProperties - Protected properties for secure log on by connector to back end server.  These
 *          are protected properties that can only be retrieved by privileged connector code.
 *      </li>
 *      <li>
 *          connectorType - Properties that describe the connector type for the connector.
 *      </li>
 *      <li>
 *          endpoint - Properties that describe the server endpoint where the connector will retrieve the assets.
 *      </li>
 *  </ul>
 */
public class ConnectionDetails extends AssetReferenceable
{
    protected Connection    connectionBean;


    /**
     * Bean constructor
     *
     * @param connectionBean bean containing the properties
     */
    public ConnectionDetails(Connection  connectionBean)
    {
        super(connectionBean);

        if (connectionBean == null)
        {
            this.connectionBean = new Connection();
        }
        else
        {
            this.connectionBean = connectionBean;
        }
    }



    /**
     * Copy/clone Constructor to return a copy of a connection object that is connected to an asset.
     *
     * @param templateConnection template object to copy.
     */
    public ConnectionDetails(ConnectionDetails templateConnection)
    {
        super(templateConnection);

        if (templateConnection == null)
        {
            this.connectionBean = new Connection();
        }
        else if (templateConnection instanceof VirtualConnectionDetails virtualConnectionDetails)
        {
            this.connectionBean = new VirtualConnection(virtualConnectionDetails.getConnectionBean());
        }
        else
        {
            this.connectionBean = new Connection(templateConnection.getConnectionBean());
        }
    }


    /**
     * Copy/clone Constructor to return a copy of a connection object but with a replacement connector type.
     *
     * @param templateConnection template object to copy.
     * @param connectorType connector type to replace in the connection
     */
    public ConnectionDetails(ConnectionDetails templateConnection,
                             ConnectorType     connectorType)
    {
        super(templateConnection);

        if (templateConnection == null)
        {
            this.connectionBean = new Connection();
        }
        else if (templateConnection instanceof VirtualConnectionDetails virtualConnectionDetails)
        {
            this.connectionBean = new VirtualConnection(virtualConnectionDetails.getConnectionBean());
        }
        else
        {
            this.connectionBean = new Connection(templateConnection.getConnectionBean());
        }

        this.connectionBean.setConnectorType(connectorType);
    }


    /**
     * Copy/clone Constructor to return a copy of a connection object but with a replacement network address
     * found in the attached Endpoint.
     *
     * @param templateConnection template object to copy.
     * @param networkAddress network address to replace in the connection's endpoint
     */
    public ConnectionDetails(ConnectionDetails templateConnection,
                             String            networkAddress)
    {
        super(templateConnection);

        if (templateConnection == null)
        {
            this.connectionBean = new Connection();
        }
        else if (templateConnection instanceof VirtualConnectionDetails virtualConnectionDetails)
        {
            this.connectionBean = new VirtualConnection(virtualConnectionDetails.getConnectionBean());
        }
        else
        {
            this.connectionBean = new Connection(templateConnection.getConnectionBean());
        }

        Endpoint endpoint = this.getEndpoint().getEndpointBean();

        if (endpoint == null)
        {
            endpoint = new Endpoint();
        }
        endpoint.setAddress(networkAddress);

        this.connectionBean.setEndpoint(endpoint);
    }


    /**
     * Return the bean with all the properties.
     *
     * @return connection bean
     */
    protected Connection getConnectionBean()
    {
        return connectionBean;
    }


    /**
     * Returns the stored display name property for the connection.
     * Null means no displayName is available.
     *
     * @return displayName
     */
    public String getDisplayName() { return connectionBean.getDisplayName(); }


    /**
     * Returns a formatted string with the connection name.  It is used in formatting error messages for the
     * exceptions thrown by consuming components.  It is extremely cautious because most of the exceptions
     * are reporting a malformed connection object so who knows what else is wrong with it.
     * <br><br>
     * Within the connection are 2 possible properties that could
     * contain the connection name:
     *   ** qualifiedName - this is a uniqueName and should be there
     *   ** displayName - shorter simpler name but may not be unique - so may not identify the connection in error
     * <br><br>
     * This method inspects these properties and builds up a string to represent the connection name
     *
     * @return connection name
     */
    public String  getConnectionName()
    {
        String   connectionName = "<Unknown>"; /* if all properties are blank */
        String   qualifiedName = connectionBean.getQualifiedName();
        String   displayName = connectionBean.getDisplayName();

        /*
         * The qualifiedName is preferred because it is unique.
         */
        if (qualifiedName != null && (!qualifiedName.isEmpty()))
        {
            /*
             * Use qualified name.
             */
            connectionName = qualifiedName;
        }
        else if (displayName != null && (!displayName.isEmpty()))
        {
            /*
             * The qualifiedName is not set but the displayName is available so use it.
             */
            connectionName = displayName;
        }

        return connectionName;
    }


    /**
     * Returns the stored description property for the connection.
     * If no description is provided then null is returned.
     *
     * @return description
     */
    public String getDescription()
    {
        return connectionBean.getDescription();
    }


    /**
     * Returns a copy of the properties for this connection's connector type.
     * A null means there is no connection type.
     *
     * @return connector type for the connection
     */
    public ConnectorTypeDetails getConnectorType()
    {
        ConnectorType  connectorType = connectionBean.getConnectorType();

        if (connectorType == null)
        {
            return null;
        }
        else
        {
            return new ConnectorTypeDetails(connectorType);
        }
    }


    /**
     * Return id of the calling user.
     *
     * @return string
     */
    public String getUserId()
    {
        return connectionBean.getUserId();
    }


    /**
     * Return an encrypted password.  The caller is responsible for decrypting it.
     *
     * @return string
     */
    public String getEncryptedPassword()
    {
        return connectionBean.getEncryptedPassword();
    }


    /**
     * Return an unencrypted password.
     *
     * @return string
     */
    public String getClearPassword()
    {
        return connectionBean.getClearPassword();
    }


    /**
     * Returns a copy of the properties for this connection's endpoint.
     * Null means no endpoint information available.
     *
     * @return endpoint for the connection
     */
    public EndpointDetails getEndpoint()
    {
        Endpoint   endpoint = connectionBean.getEndpoint();

        if (endpoint == null)
        {
            return null;
        }
        else
        {
            return new EndpointDetails(endpoint);
        }
    }


    /**
     * Return a copy of the configuration properties.  Null means no properties are available.
     *
     * @return configuration properties for the underlying technology
     */
    public Map<String, Object> getConfigurationProperties()
    {
        return connectionBean.getConfigurationProperties();
    }


    /**
     * Return a copy of the secured properties.  Null means no secured properties are available.
     * This method is protected so only OCF (or subclasses) can access them.  When Connector is passed to calling
     * OMAS, the secured properties are not available.
     *
     * @return secured properties typically user credentials for the connection
     */
    public Map<String, String> getSecuredProperties()
    {
        return connectionBean.getSecuredProperties();
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return connectionBean.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ConnectionDetails that = (ConnectionDetails) objectToCompare;
        return Objects.equals(connectionBean, that.connectionBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), connectionBean);
    }
}
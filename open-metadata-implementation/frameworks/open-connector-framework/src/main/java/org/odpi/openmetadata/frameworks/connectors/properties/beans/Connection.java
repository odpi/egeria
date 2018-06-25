/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The connection is an object that contains the properties needed to create and initialise a connector to access a
 * specific data assets.
 *
 * The properties for a connection are defined in model 0201.  They include the following options for connector name:
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
 *
 *  Either the guid, qualifiedName or displayName can be used to specify the name for a connection.
 *
 *  Other properties for the connection include:
 *  <ul>
 *      <li>
 *          type - information about the TypeDef for Connection
 *      </li>
 *      <li>
 *          description - A full description of the connection covering details of the assets it connects to
 *          along with usage and version information.
 *      </li>
 *      <li>
 *          additionalProperties - Any additional properties associated with the connection.
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
 *
 * The connection class is simply used to cache the properties for an connection.
 * It is used by other classes to exchange this information between a metadata repository and a consumer.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = VirtualConnection.class, name = "VirtualConnection")
        })
public class Connection extends Referenceable
{
    /*
     * Attributes of a connector
     */
    protected String              displayName       = null;
    protected String              description       = null;
    protected ConnectorType       connectorType     = null;
    protected Endpoint            endpoint          = null;
    protected Map<String, Object> securedProperties = null;


    /**
     * Default constructor sets the Connection properties to null.
     */
    public Connection()
    {
        super();
    }


    /**
     * Copy/clone Constructor to return a copy of a connection object.
     *
     * @param templateConnection Connection to copy
     */
    public Connection(Connection templateConnection)
    {
        super(templateConnection);

        if (templateConnection != null)
        {
            displayName = templateConnection.getDisplayName();
            description = templateConnection.getDescription();
            connectorType = templateConnection.getConnectorType();
            endpoint = templateConnection.getEndpoint();
            securedProperties = templateConnection.getSecuredProperties();
        }
    }


    /**
     * Returns the stored display name property for the connection.
     * Null means no displayName is available.
     *
     * @return displayName
     */
    public String getDisplayName() { return displayName; }


    /**
     * Set up the display name for UIs and reports.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Set up description of the element.
     *
     * @param description String
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Returns the stored description property for the connection.
     * If no description is provided then null is returned.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the connector type properties for this Connection.
     *
     * @param connectorType ConnectorType properties object
     */
    public void setConnectorType(ConnectorType connectorType)
    {
        this.connectorType = connectorType;
    }


    /**
     * Returns a copy of the properties for this connection's connector type.
     * A null means there is no connection type.
     *
     * @return connector type for the connection
     */
    public ConnectorType getConnectorType()
    {
        if (connectorType == null)
        {
            return null;
        }
        else
        {
            return new ConnectorType(connectorType);
        }
    }


    /**
     * Set up the endpoint properties for this Connection.
     *
     * @param endpoint Endpoint properties object
     */
    public void setEndpoint(Endpoint endpoint)
    {
        this.endpoint = endpoint;
    }


    /**
     * Returns a copy of the properties for this connection's endpoint.
     * Null means no endpoint information available.
     *
     * @return endpoint for the connection
     */
    public Endpoint getEndpoint()
    {
        if (endpoint == null)
        {
            return null;
        }
        else
        {
            return new Endpoint(endpoint);
        }
    }


    /**
     * Set up the secured properties for this Connection.
     *
     * @param securedProperties properties that contain secret information such as log on information.
     */
    public void setSecuredProperties(Map<String,Object> securedProperties)
    {
        this.securedProperties = securedProperties;
    }


    /**
     * Return a copy of the secured properties.  Null means no secured properties are available.
     *
     * @return secured properties typically user credentials for the connection
     */
    public Map<String,Object> getSecuredProperties()
    {
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
     * Standard toString method. Note SecuredProperties are not displayed.  This is deliberate because
     * there is no knowing where the string will be printed.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Connection{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", connectorType=" + connectorType +
                ", endpoint=" + endpoint +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
                '}';
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
        if (!(objectToCompare instanceof Connection))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        Connection that = (Connection) objectToCompare;
        return Objects.equals(getDisplayName(), that.getDisplayName()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getConnectorType(), that.getConnectorType()) &&
                Objects.equals(getEndpoint(), that.getEndpoint()) &&
                Objects.equals(getSecuredProperties(), that.getSecuredProperties());
    }
}

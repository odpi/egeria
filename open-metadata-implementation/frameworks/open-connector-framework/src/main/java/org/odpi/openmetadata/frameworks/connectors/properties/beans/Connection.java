/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
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
 *          securedProperties - Protected properties for secure log on by connector to back end server.  These
 *          are protected properties that can only be retrieved by privileged connector code.
 *      </li>
 *      <li>
 *          userId - name or URI or connecting user.
 *      </li>
 *      <li>
 *          encryptedPassword - password for the userId - needs decrypting by connector before use.
 *      </li>
 *      <li>
 *          clearPassword - password for userId - ready to use.
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
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = VirtualConnection.class, name = "VirtualConnection")
        })
public class Connection extends Referenceable
{
    /*
     * Attributes of a connector
     */
    protected String              displayName         = null;
    protected String              description         = null;
    protected ConnectorType       connectorType       = null;
    protected Endpoint            endpoint            = null;
    protected String              userId              = null;
    protected String              encryptedPassword   = null;
    protected String              clearPassword       = null;
    protected Map<String, Object> securedProperties   = null;


    /**
     * Return the standard type for a connection type.
     *
     * @return ElementType object
     */
    public static ElementType getConnectionType()
    {
        final String        elementTypeId                   = "114e9f8f-5ff3-4c32-bd37-a7eb42712253";
        final String        elementTypeName                 = "Connection";
        final long          elementTypeVersion              = 1;
        final String        elementTypeDescription          = "A set of properties to identify and configure a connector instance.";
        final String        elementAccessServiceURL         = null;
        final ElementOrigin elementOrigin                   = ElementOrigin.LOCAL_COHORT;
        final String        elementHomeMetadataCollectionId = null;

        ElementType elementType = new ElementType();

        elementType.setElementTypeId(elementTypeId);
        elementType.setElementTypeName(elementTypeName);
        elementType.setElementTypeVersion(elementTypeVersion);
        elementType.setElementTypeDescription(elementTypeDescription);
        elementType.setElementSourceServer(elementAccessServiceURL);
        elementType.setElementOrigin(elementOrigin);
        elementType.setElementHomeMetadataCollectionId(elementHomeMetadataCollectionId);

        return elementType;
    }


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
            userId = templateConnection.getUserId();
            clearPassword = templateConnection.getClearPassword();
            encryptedPassword = templateConnection.getEncryptedPassword();
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
     * Return id of the calling user.
     *
     * @return string
     */
    public String getUserId()
    {
        return userId;
    }


    /**
     * Set up the id of the calling user.
     *
     * @param userId string
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }


    /**
     * Return an encrypted password.  The caller is responsible for decrypting it.
     *
     * @return string
     */
    public String getEncryptedPassword()
    {
        return encryptedPassword;
    }


    /**
     * Set up an encrypted password.
     *
     * @param encryptedPassword string
     */
    public void setEncryptedPassword(String encryptedPassword)
    {
        this.encryptedPassword = encryptedPassword;
    }


    /**
     * Return an unencrypted password.
     *
     * @return string
     */
    public String getClearPassword()
    {
        return clearPassword;
    }


    /**
     * Set up an unencrypted password.
     *
     * @param clearPassword string
     */
    public void setClearPassword(String clearPassword)
    {
        this.clearPassword = clearPassword;
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
                ", userId='" + userId + '\'' +
                ", encryptedPassword='" + encryptedPassword + '\'' +
                ", clearPassword='" + clearPassword + '\'' +
                ", securedProperties=" + securedProperties +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", extendedProperties=" + getExtendedProperties() +
                ", meanings=" + getMeanings() +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", URL='" + getURL() + '\'' +
                ", classifications=" + getClassifications() +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
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
                Objects.equals(getUserId(), that.getUserId()) &&
                Objects.equals(getEncryptedPassword(), that.getEncryptedPassword()) &&
                Objects.equals(getClearPassword(), that.getClearPassword()) &&
                Objects.equals(getSecuredProperties(), that.getSecuredProperties());
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getDescription(), getConnectorType(), getEndpoint(),
                            getUserId(), getEncryptedPassword(), getClearPassword(), getSecuredProperties());
    }
}

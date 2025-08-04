/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefCategory;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The connection is an object that contains the properties needed to create and initialise a connector to access a
 * specific data assets.
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
 *          configurationProperties - properties for configuring the connector.
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
 * The connection class is simply used to cache the properties for a connection.
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
    private ConnectorType       connectorType           = null;
    private Endpoint            endpoint                = null;
    private String              userId                  = null;
    private String              encryptedPassword       = null;
    private String              clearPassword           = null;
    private Map<String, Object> configurationProperties = null;
    private Map<String, String> securedProperties       = null;


    /**
     * Return the standard type for a connection type.
     *
     * @return ElementType object
     */
    public static ElementType getConnectionType()
    {
        final long          elementTypeVersion              = 1;

        ElementType elementType = new ElementType();

        elementType.setTypeId(OpenMetadataType.CONNECTION.typeGUID);
        elementType.setTypeName(OpenMetadataType.CONNECTION.typeName);
        elementType.setTypeVersion(elementTypeVersion);
        elementType.setTypeDescription(OpenMetadataType.CONNECTION.description);
        elementType.setTypeCategory(OpenMetadataTypeDefCategory.ENTITY_DEF);

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
     * @param template Connection to copy
     */
    public Connection(Connection template)
    {
        super(template);

        if (template != null)
        {
            userId = template.getUserId();
            clearPassword = template.getClearPassword();
            encryptedPassword = template.getEncryptedPassword();
            connectorType = template.getConnectorType();
            endpoint = template.getEndpoint();
            configurationProperties = template.getConfigurationProperties();
            securedProperties = template.getSecuredProperties();
        }
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
        return connectorType;
    }


    /**
     * Return the userId to use on this connection.
     *
     * @return string
     */
    public String getUserId()
    {
        return userId;
    }


    /**
     * Set up the userId to use on this connection.
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
        return endpoint;
    }


    /**
     * Set up the configuration properties for this Connection.
     *
     * @param configurationProperties properties that contain additional configuration information for the connector.
     */
    public void setConfigurationProperties(Map<String, Object> configurationProperties)
    {
        this.configurationProperties = configurationProperties;
    }


    /**
     * Return a copy of the configuration properties.  Null means no configuration properties are available.
     *
     * @return configuration properties typically controlling the behaviour for the connector
     */
    public Map<String, Object> getConfigurationProperties()
    {
        return configurationProperties;
    }


    /**
     * Set up the secured properties for this Connection.
     *
     * @param securedProperties properties that contain secret information such as log on information.
     */
    public void setSecuredProperties(Map<String, String> securedProperties)
    {
        this.securedProperties = securedProperties;
    }


    /**
     * Return a copy of the secured properties.  Null means no secured properties are available.
     *
     * @return secured properties typically user credentials for the connection
     */
    public Map<String, String> getSecuredProperties()
    {
        return securedProperties;
    }


    /**
     * Standard toString method. Note SecuredProperties and other credential type properties are not displayed.
     * This is deliberate because there is no knowing where the string will be printed.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Connection{" +
                ", connectorType=" + connectorType +
                ", endpoint=" + endpoint +
                ", userId='" + userId + '\'' +
                ", encryptedPassword='" + encryptedPassword + '\'' +
                ", clearPassword='" + clearPassword + '\'' +
                ", configurationProperties=" + configurationProperties +
                ", securedProperties=" + securedProperties +
                "} " + super.toString();
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
        return Objects.equals(getConnectorType(), that.getConnectorType()) &&
                Objects.equals(getEndpoint(), that.getEndpoint()) &&
                Objects.equals(getUserId(), that.getUserId()) &&
                Objects.equals(getEncryptedPassword(), that.getEncryptedPassword()) &&
                Objects.equals(getClearPassword(), that.getClearPassword()) &&
                Objects.equals(getConfigurationProperties(), that.getConfigurationProperties()) &&
                Objects.equals(getSecuredProperties(), that.getSecuredProperties());
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getConnectorType(), getEndpoint(),
                            getUserId(), getEncryptedPassword(), getClearPassword(), getSecuredProperties(),
                            getConfigurationProperties());
    }
}

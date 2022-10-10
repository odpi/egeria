/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.metadatasecurity.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Connection is a set of properties that describes an open metadata asset.  It is designed to convey the important properties
 * needed to make a security decision.
 */
public class Connection extends Referenceable
{
    private static final long   serialVersionUID = 1L;

    private String              displayName             = null;
    private String              description             = null;
    private String              networkAddress          = null;
    private String              userId                  = null;
    private String              encryptedPassword       = null;
    private String              clearPassword           = null;
    private Map<String, Object> configurationProperties = null;
    private Map<String, Object> securedProperties       = null;

    /**
     * Default constructor
     */
    public Connection()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template template values for asset summary
     */
    public Connection(Connection template)
    {
        super(template);

        if (template != null)
        {
            displayName             = template.getDisplayName();
            description             = template.getDescription();
            networkAddress          = template.getNetworkAddress();
            userId                  = template.getUserId();
            clearPassword           = template.getClearPassword();
            encryptedPassword       = template.getEncryptedPassword();
            configurationProperties = template.getConfigurationProperties();
            securedProperties       = template.getSecuredProperties();
        }
    }


    /**
     * Returns the stored display name property for the asset.
     * If no display name is available then null is returned.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the stored display name property for the asset.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Returns the stored description property for the asset.
     * If no description is provided then null is returned.
     *
     * @return description String text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the stored description property associated with the asset.
     *
     * @param description String text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the endpoint that this connection points to.
     *
     * @return string - typically url
     */
    public String getNetworkAddress()
    {
        return networkAddress;
    }


    /**
     * Set up the endpoint that this connection points to.
     *
     * @param networkAddress string - typically url
     */
    public void setNetworkAddress(String networkAddress)
    {
        this.networkAddress = networkAddress;
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
     * Set up the configuration properties for this Connection.
     *
     * @param configurationProperties properties that contain additional configuration information for the connector.
     */
    public void setConfigurationProperties(Map<String,Object> configurationProperties)
    {
        this.configurationProperties = configurationProperties;
    }


    /**
     * Return a copy of the configuration properties.  Null means no secured properties are available.
     *
     * @return secured properties typically user credentials for the connection
     */
    public Map<String,Object> getConfigurationProperties()
    {
        if (configurationProperties == null)
        {
            return null;
        }
        else if (configurationProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(configurationProperties);
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Connection{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", networkAddress='" + networkAddress + '\'' +
                ", userId='" + userId + '\'' +
                ", encryptedPassword='" + encryptedPassword + '\'' +
                ", clearPassword='" + clearPassword + '\'' +
                ", configurationProperties=" + configurationProperties +
                ", securedProperties=" + securedProperties +
                ", typeGUID='" + getTypeGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", GUID='" + getGUID() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", extendedProperties=" + getExtendedProperties() +
                ", securityLabels=" + getSecurityLabels() +
                ", securityProperties=" + getSecurityProperties() +
                ", confidentiality=" + getConfidentiality() +
                ", confidence=" + getConfidence() +
                ", criticality=" + getCriticality() +
                ", retention=" + getRetention() +
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
        return Objects.equals(networkAddress, that.networkAddress) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(encryptedPassword, that.encryptedPassword) &&
                Objects.equals(clearPassword, that.clearPassword) &&
                Objects.equals(configurationProperties, that.configurationProperties) &&
                Objects.equals(securedProperties, that.securedProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), networkAddress, userId, encryptedPassword, clearPassword, configurationProperties, securedProperties);
    }
}

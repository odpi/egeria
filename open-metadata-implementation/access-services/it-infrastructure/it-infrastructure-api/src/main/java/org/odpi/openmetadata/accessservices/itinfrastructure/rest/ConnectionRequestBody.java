/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ConnectionProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ConnectionRequestBody carries the parameters for describing a connection.
 * The external source identifiers are used if the connection was created using the external source identifiers.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConnectionRequestBody extends ConnectionProperties
{
    private String externalSourceGUID         = null;
    private String externalSourceName         = null;


    /**
     * Default constructor
     */
    public ConnectionRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConnectionRequestBody(ConnectionRequestBody template)
    {
        super(template);

        if (template != null)
        {
            externalSourceGUID = template.getExternalSourceGUID();
            externalSourceName = template.getExternalSourceName();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConnectionRequestBody(ConnectionProperties template)
    {
        super(template);
    }


    /**
     * Return the unique identifier of the software server capability entity that represented the external source - null for local.
     *
     * @return string guid
     */
    public String getExternalSourceGUID()
    {
        return externalSourceGUID;
    }


    /**
     * Set up the unique identifier of the software server capability entity that represented the external source - null for local.
     *
     * @param externalSourceGUID string guid
     */
    public void setExternalSourceGUID(String externalSourceGUID)
    {
        this.externalSourceGUID = externalSourceGUID;
    }


    /**
     * Return the unique name of the software server capability entity that represented the external source.
     *
     * @return string name
     */
    public String getExternalSourceName()
    {
        return externalSourceName;
    }


    /**
     * Set up the unique name of the software server capability entity that represented the external source.
     *
     * @param externalSourceName string name
     */
    public void setExternalSourceName(String externalSourceName)
    {
        this.externalSourceName = externalSourceName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ConnectionRequestBody{" +
                       "externalSourceGUID='" + externalSourceGUID + '\'' +
                       ", externalSourceName='" + externalSourceName + '\'' +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", userId='" + getUserId() + '\'' +
                       ", encryptedPassword='" + getEncryptedPassword() + '\'' +
                       ", clearPassword='" + getClearPassword() + '\'' +
                       ", configurationProperties=" + getConfigurationProperties() +
                       ", securedProperties=" + getSecuredProperties() +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        ConnectionRequestBody that = (ConnectionRequestBody) objectToCompare;
        return Objects.equals(externalSourceGUID, that.externalSourceGUID) &&
                Objects.equals(externalSourceName, that.externalSourceName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), externalSourceGUID, externalSourceName);
    }
}

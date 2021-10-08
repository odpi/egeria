/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securitymanager.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.securitymanager.properties.DataFileProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataFileRequestBody carries the parameters for creating a new file asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataFileRequestBody extends DataFileProperties
{
    private static final long    serialVersionUID = 1L;

    private String externalSourceGUID         = null;
    private String externalSourceName         = null;
    private String connectorProviderClassName = null;


    /**
     * Default constructor
     */
    public DataFileRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataFileRequestBody(DataFileRequestBody template)
    {
        super(template);

        if (template != null)
        {
            externalSourceGUID         = template.getExternalSourceGUID();
            externalSourceName         = template.getExternalSourceName();
            connectorProviderClassName = template.getConnectorProviderClassName();
        }
    }


    /**
     * Copy/clone constructor for main properties
     *
     * @param template object to copy
     */
    public DataFileRequestBody(DataFileProperties template)
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
     * Return the fully qualified class name of the connector provider for this type of file.  If null is
     * passed, the server uses the default file connector.
     *
     * @return string name
     */
    public String getConnectorProviderClassName()
    {
        return connectorProviderClassName;
    }


    /**
     * Set up the fully qualified class name of the connector provider for this type of file.  If null is
     * passed, the server uses the default file connector.
     *
     * @param connectorProviderClassName string name
     */
    public void setConnectorProviderClassName(String connectorProviderClassName)
    {
        this.connectorProviderClassName = connectorProviderClassName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DataFileRequestBody{" +
                "externalSourceGUID='" + externalSourceGUID + '\'' +
                ", externalSourceName='" + externalSourceName + '\'' +
                ", connectorClassName='" + connectorProviderClassName + '\'' +
                ", fileType='" + getFileType() + '\'' +
                ", createTime=" + getCreateTime() +
                ", modifiedTime=" + getModifiedTime() +
                ", encodingType='" + getEncodingType() + '\'' +
                ", encodingLanguage='" + getEncodingLanguage() + '\'' +
                ", encodingDescription='" + getEncodingDescription() + '\'' +
                ", encodingProperties=" + getEncodingProperties() +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
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
        DataFileRequestBody that = (DataFileRequestBody) objectToCompare;
        return Objects.equals(externalSourceGUID, that.externalSourceGUID) &&
                Objects.equals(externalSourceName, that.externalSourceName) &&
                Objects.equals(connectorProviderClassName, that.connectorProviderClassName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), externalSourceGUID, externalSourceName, connectorProviderClassName);
    }
}
/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalSchemaTypeProperties carries the unique identifier and properties of a reusable schema type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class ExternalSchemaTypeProperties extends SimpleSchemaTypeProperties
{
    private static final long     serialVersionUID = 1L;

    private String               externalSchemaTypeGUID = null;
    private SchemaTypeProperties externalSchemaType     = null;

    /**
     * Default constructor
     */
    public ExternalSchemaTypeProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public ExternalSchemaTypeProperties(ExternalSchemaTypeProperties template)
    {
        super(template);

        if (template != null)
        {
            externalSchemaTypeGUID = template.getExternalSchemaTypeGUID();
            externalSchemaType = template.getExternalSchemaType();
        }
    }


    /**
     * Return the unique identifier of the external schema type.
     *
     * @return string guid
     */
    public String getExternalSchemaTypeGUID()
    {
        return externalSchemaTypeGUID;
    }


    /**
     * Set up the unique identifier of the external schema type.
     *
     * @param externalSchemaTypeGUID string guid
     */
    public void setExternalSchemaTypeGUID(String externalSchemaTypeGUID)
    {
        this.externalSchemaTypeGUID = externalSchemaTypeGUID;
    }


    /**
     * Return the schema type that is reusable amongst assets.
     *
     * @return bean describing external schema
     */
    public SchemaTypeProperties getExternalSchemaType()
    {
        return externalSchemaType;
    }


    /**
     * Set up the schema type that is reusable amongst assets.
     *
     * @param externalSchemaType bean describing external schema
     */
    public void setExternalSchemaType(SchemaTypeProperties externalSchemaType)
    {
        this.externalSchemaType = externalSchemaType;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ExternalSchemaTypeProperties{" +
                       "externalSchemaTypeGUID=" + externalSchemaTypeGUID +
                       ", externalSchemaType=" + externalSchemaType +
                       ", dataType='" + getDataType() + '\'' +
                       ", defaultValue='" + getDefaultValue() + '\'' +
                       ", versionNumber='" + getVersionNumber() + '\'' +
                       ", author='" + getAuthor() + '\'' +
                       ", usage='" + getUsage() + '\'' +
                       ", encodingStandard='" + getEncodingStandard() + '\'' +
                       ", namespace='" + getNamespace() + '\'' +
                       ", formula='" + getFormula() + '\'' +
                       ", queries=" + getQueries() +
                       ", isDeprecated=" + getIsDeprecated() +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                        ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties()+
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
        ExternalSchemaTypeProperties that = (ExternalSchemaTypeProperties) objectToCompare;
        return Objects.equals(externalSchemaTypeGUID, that.externalSchemaTypeGUID) &&
                       Objects.equals(externalSchemaType, that.externalSchemaType);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), externalSchemaTypeGUID, externalSchemaType);
    }
}
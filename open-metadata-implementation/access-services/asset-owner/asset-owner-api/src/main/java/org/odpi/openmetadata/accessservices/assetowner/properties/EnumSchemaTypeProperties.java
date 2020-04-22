/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EnumSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EnumSchemaTypeProperties carries the specialized parameters for creating or updating enum schema types.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class EnumSchemaTypeProperties extends SimpleSchemaTypeProperties
{
    private static final long     serialVersionUID = 1L;

    private String  validValueSet = null;

    /**
     * Default constructor
     */
    public EnumSchemaTypeProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public EnumSchemaTypeProperties(EnumSchemaTypeProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone operator.
     *
     * @param objectToFill schema type object
     * @return filled object
     */
    public EnumSchemaType cloneProperties(EnumSchemaType  objectToFill)
    {
        EnumSchemaType clone = objectToFill;

        if (clone == null)
        {
            clone = new EnumSchemaType();
        }

        super.cloneProperties(clone);

        return clone;
    }


    /**
     * Return the unique identifier of the valid value set that describes the enum values for this schema element.
     *
     * @return string guid
     */
    public String getValidValueSet()
    {
        return validValueSet;
    }


    /**
     * Set up the unique identifier of the value set that describes the enum values for this schema element.
     *
     * @param validValueSet string guid
     */
    public void setValidValueSet(String validValueSet)
    {
        this.validValueSet = validValueSet;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "EnumSchemaTypeProperties{" +
                "validValueSet='" + validValueSet + '\'' +
                ", dataType='" + getDataType() + '\'' +
                ", defaultValue='" + getDefaultValue() + '\'' +
                ", versionNumber='" + getVersionNumber() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", usage='" + getUsage() + '\'' +
                ", encodingStandard='" + getEncodingStandard() + '\'' +
                ", namespace='" + getNamespace() + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", deprecated=" + isDeprecated() +
                ", typeName='" + getTypeName() + '\'' +
                ", classifications=" + getClassifications() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", meanings=" + getMeanings() +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }
}
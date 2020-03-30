/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SimpleSchemaType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SimpleSchemaTypeProperties carries the common parameters for creating or updating primitive and enum schema types.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PrimitiveSchemaTypeProperties.class, name = "PrimitiveSchemaTypeProperties"),
        @JsonSubTypes.Type(value = EnumSchemaTypeProperties.class, name = "EnumSchemaTypeProperties"),
})
public class SimpleSchemaTypeProperties extends SchemaTypeProperties
{
    private static final long     serialVersionUID = 1L;

    private String dataType   = null;
    private String defaultValue = null;

    /**
     * Default constructor
     */
    public SimpleSchemaTypeProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param template template object to copy.
     */
    public SimpleSchemaTypeProperties(SimpleSchemaTypeProperties template)
    {
        super(template);

        if (template != null)
        {
            dataType   = template.getDataType();
            defaultValue = template.getDefaultValue();
        }
    }


    /**
     * Copy/clone operator.
     *
     * @param objectToFill schema type object
     * @return filled object
     */
    public SimpleSchemaType cloneProperties(SimpleSchemaType objectToFill)
    {
        SimpleSchemaType clone = objectToFill;

        if (clone == null)
        {
            clone = new SimpleSchemaType();
        }

        clone.setDataType(this.getDataType());
        clone.setDefaultValue(this.getDefaultValue());

        super.cloneProperties(clone);

        return clone;
    }



    /**
     * Return the data type for this element.  Null means unknown data type.
     *
     * @return String data type name
     */
    public String getDataType() { return dataType; }


    /**
     * Set up the data type for this element.  Null means unknown data type.
     *
     * @param dataType data type name
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }


    /**
     * Return the default value for the element.  Null means no default value set up.
     *
     * @return String containing default value
     */
    public String getDefaultValue() { return defaultValue; }


    /**
     * Set up the default value for the element.  Null means no default value set up.
     *
     * @param defaultValue String containing default value
     */
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
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
        SimpleSchemaTypeProperties that = (SimpleSchemaTypeProperties) objectToCompare;
        return Objects.equals(dataType, that.dataType) &&
                Objects.equals(defaultValue, that.defaultValue);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SimpleSchemaTypeProperties{" +
                "dataType='" + dataType + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", versionNumber='" + getVersionNumber() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", usage='" + getUsage() + '\'' +
                ", encodingStandard='" + getEncodingStandard() + '\'' +
                ", namespace='" + getNamespace() + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", classifications=" + getClassifications() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", meanings=" + getMeanings() +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }
}
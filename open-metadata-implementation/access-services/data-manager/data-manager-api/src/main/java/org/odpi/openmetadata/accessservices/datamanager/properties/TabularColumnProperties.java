/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TabularColumnProperties is a class for representing a column within a table type structure.
 * Tabular columns are schema attributes with a simple type attached
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DatabaseColumnProperties.class, name = "DatabaseColumnProperties"),
        })
public class TabularColumnProperties extends SchemaAttributeProperties
{
    private static final long     serialVersionUID = 1L;

    private String dataType           = null;
    private String defaultValue       = null;
    private String fixedValue         = null;
    private String externalTypeGUID   = null;
    private String validValuesSetGUID = null;

    /**
     * Default constructor used by subclasses
     */
    public TabularColumnProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public TabularColumnProperties(TabularColumnProperties template)
    {
        super(template);

        if (template != null)
        {
            dataType           = template.getDataType();
            defaultValue       = template.getDefaultValue();
            fixedValue         = template.getFixedValue();
            externalTypeGUID   = template.getExternalTypeGUID();
            validValuesSetGUID = template.getValidValuesSetGUID();
        }
    }


    /**
     * Return the data type for this element.  Null means unknown data type.
     *
     * @return string data type name
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
     * @return string containing default value
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
     * Return a fixed literal value - an alternative to default value.
     *
     * @return string value
     */
    public String getFixedValue()
    {
        return fixedValue;
    }


    /**
     * If the column contains a fixed literal value, set this value here - an alternative to default value.
     *
     * @param fixedValue string
     */
    public void setFixedValue(String fixedValue)
    {
        this.fixedValue = fixedValue;
    }


    /**
     * Return the unique identifier of this column's type.
     *
     * @return unique identifier (guid) of the external schema type
     */
    public String getExternalTypeGUID()
    {
        return externalTypeGUID;
    }


    /**
     * If the type of this column is represented by an external (standard type) put its value here.  No need to set
     * dataType, FixedType or defaultType
     *
     * @param externalTypeGUID unique identifier (guid) of the external schema type
     */
    public void setExternalTypeGUID(String externalTypeGUID)
    {
        this.externalTypeGUID = externalTypeGUID;
    }


    /**
     * Return the set of valid values for this column.
     *
     * @return unique identifier (guid) of the valid values set
     */
    public String getValidValuesSetGUID()
    {
        return validValuesSetGUID;
    }


    /**
     * If the type is controlled by a fixed set of values, set up the unique identifier of the valid values set
     * that lists the valid values.
     *
     * @param validValuesSetGUID unique identifier (guid) of the valid values set
     */
    public void setValidValuesSetGUID(String validValuesSetGUID)
    {
        this.validValuesSetGUID = validValuesSetGUID;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TabularColumnProperties{" +
                "dataType='" + dataType + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", fixedValue='" + fixedValue + '\'' +
                ", externalTypeGUID='" + externalTypeGUID + '\'' +
                ", validValuesSetGUID='" + validValuesSetGUID + '\'' +
                ", elementPosition=" + getElementPosition() +
                ", minCardinality=" + getMinCardinality() +
                ", maxCardinality=" + getMaxCardinality() +
                ", allowsDuplicateValues=" + getAllowsDuplicateValues() +
                ", orderedValues=" + getOrderedValues() +
                ", sortOrder=" + getSortOrder() +
                ", minimumLength=" + getMinimumLength() +
                ", length=" + getLength() +
                ", significantDigits=" + getPrecision() +
                ", nullable=" + getIsNullable() +
                ", defaultValueOverride='" + getDefaultValueOverride() + '\'' +
                ", nativeJavaClass='" + getNativeJavaClass() + '\'' +
                ", aliases=" + getAliases() +
                ", deprecated=" + getIsDeprecated() +
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
        TabularColumnProperties that = (TabularColumnProperties) objectToCompare;
        return Objects.equals(dataType, that.dataType) &&
                Objects.equals(defaultValue, that.defaultValue) &&
                Objects.equals(fixedValue, that.fixedValue) &&
                Objects.equals(externalTypeGUID, that.externalTypeGUID) &&
                Objects.equals(validValuesSetGUID, that.validValuesSetGUID);
    }

    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), dataType, defaultValue, fixedValue, externalTypeGUID, validValuesSetGUID);
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DataItemSortOrder;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIParameterProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.events.EventSchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaAttributeProperties represents a data field that is part of a complex schema type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = TabularColumnProperties.class, name = "TabularColumnProperties"),
                @JsonSubTypes.Type(value = RelationalTableProperties.class, name = "RelationalTableProperties"),
                @JsonSubTypes.Type(value = EventSchemaAttributeProperties.class, name = "EventSchemaAttributeProperties"),
                @JsonSubTypes.Type(value = APIParameterProperties.class, name = "APIParameterProperties"),
        })
public class SchemaAttributeProperties extends SchemaElementProperties
{
    private boolean           allowsDuplicateValues = false;
    private boolean           orderedValues         = false;
    private String            defaultValueOverride  = null;
    private DataItemSortOrder sortOrder             = null;
    private int               minimumLength         = 0;
    private int               length                = 0;
    private int               precision             = 0;
    private boolean           isNullable            = true;
    private String            nativeClass           = null;
    private List<String>      aliases               = null;

    /**
     * Default constructor
     */
    public SchemaAttributeProperties()
    {
        super();
        super.typeName = OpenMetadataType.SCHEMA_ATTRIBUTE.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template schema attribute to copy.
     */
    public SchemaAttributeProperties(SchemaAttributeProperties template)
    {
        super(template);

        if (template != null)
        {
            allowsDuplicateValues = template.getAllowsDuplicateValues();
            orderedValues         = template.getOrderedValues();
            sortOrder             = template.getSortOrder();
            minimumLength         = template.getMinimumLength();
            length                = template.getLength();
            precision             = template.getPrecision();
            isNullable            = template.getIsNullable();
            defaultValueOverride = template.getDefaultValueOverride();
            nativeClass          = template.getNativeClass();
            aliases              = template.getAliases();
        }
    }


    /**
     * Return whether the same value can be used by more than one instance of this attribute.
     *
     * @return boolean flag
     */
    public boolean getAllowsDuplicateValues()
    {
        return allowsDuplicateValues;
    }


    /**
     * Set up whether the same value can be used by more than one instance of this attribute.
     *
     * @param allowsDuplicateValues boolean flag
     */
    public void setAllowsDuplicateValues(boolean allowsDuplicateValues)
    {
        this.allowsDuplicateValues = allowsDuplicateValues;
    }


    /**
     * Return whether the attribute instances are arranged in an order.
     *
     * @return boolean flag
     */
    public boolean getOrderedValues()
    {
        return orderedValues;
    }


    /**
     * Set up whether the attribute instances are arranged in an order.
     *
     * @param orderedValues boolean flag
     */
    public void setOrderedValues(boolean orderedValues)
    {
        this.orderedValues = orderedValues;
    }


    /**
     * Return the order that the attribute instances are arranged in - if any.
     *
     * @return DataItemSortOrder enum
     */
    public DataItemSortOrder getSortOrder()
    {
        return sortOrder;
    }


    /**
     * Set up the order that the attribute instances are arranged in - if any.
     *
     * @param sortOrder DataItemSortOrder enum
     */
    public void setSortOrder(DataItemSortOrder sortOrder)
    {
        this.sortOrder = sortOrder;
    }


    /**
     * Return the minimum length of the data.
     *
     * @return int
     */
    public int getMinimumLength()
    {
        return minimumLength;
    }


    /**
     * Set up the minimum length of the data.
     *
     * @param minimumLength int
     */
    public void setMinimumLength(int minimumLength)
    {
        this.minimumLength = minimumLength;
    }


    /**
     * Return the length of the data field.
     *
     * @return int
     */
    public int getLength()
    {
        return length;
    }


    /**
     * Set up the length of the data field.
     *
     * @param length int
     */
    public void setLength(int length)
    {
        this.length = length;
    }


    /**
     * Return the number of significant digits to the right of decimal point.
     *
     * @return int
     */
    public int getPrecision()
    {
        return precision;
    }


    /**
     * Set up the number of significant digits to the right of decimal point.
     *
     * @param precision int
     */
    public void setPrecision(int precision)
    {
        this.precision = precision;
    }


    /**
     * Return whether the field is nullable or not.
     *
     * @return boolean
     */
    public boolean getIsNullable()
    {
        return isNullable;
    }


    /**
     * Set up whether the field is nullable or not.
     *
     * @param nullable boolean
     */
    public void setIsNullable(boolean nullable)
    {
        isNullable = nullable;
    }


    /**
     * Return any default value for this attribute that would override the default defined in the
     * schema element for this attribute's type (note only used is type is primitive).
     *
     * @return String default value override
     */
    public String getDefaultValueOverride() { return defaultValueOverride; }


    /**
     * Set up any default value for this attribute that would override the default defined in the
     * schema element for this attribute's type (note only used is type is primitive).
     *
     * @param defaultValueOverride String default value override
     */
    public void setDefaultValueOverride(String defaultValueOverride)
    {
        this.defaultValueOverride = defaultValueOverride;
    }


    /**
     * Return the name of the Java class to use to represent this type.
     *
     * @return fully qualified Java class name
     */
    public String getNativeClass()
    {
        return nativeClass;
    }


    /**
     * Set up the name of the Java class to use to represent this type.
     *
     * @param nativeClass fully qualified Java class name
     */
    public void setNativeClass(String nativeClass)
    {
        this.nativeClass = nativeClass;
    }


    /**
     * Return a list of alternative names for the attribute.
     *
     * @return list of names
     */
    public List<String> getAliases()
    {
        return aliases;
    }


    /**
     * Set up a list of alternative names for the attribute.
     *
     * @param aliases list of names
     */
    public void setAliases(List<String> aliases)
    {
        this.aliases = aliases;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaAttributeProperties{" +
                "allowsDuplicateValues=" + allowsDuplicateValues +
                ", orderedValues=" + orderedValues +
                ", defaultValueOverride='" + defaultValueOverride + '\'' +
                ", sortOrder=" + sortOrder +
                ", minimumLength=" + minimumLength +
                ", length=" + length +
                ", precision=" + precision +
                ", isNullable=" + isNullable +
                ", nativeClass='" + nativeClass + '\'' +
                ", aliases=" + aliases +
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
        SchemaAttributeProperties that = (SchemaAttributeProperties) objectToCompare;
        return allowsDuplicateValues == that.allowsDuplicateValues &&
                       orderedValues == that.orderedValues &&
                       minimumLength == that.minimumLength &&
                       length == that.length &&
                       precision == that.precision &&
                       isNullable == that.isNullable &&
                       Objects.equals(defaultValueOverride, that.defaultValueOverride) &&
                       sortOrder == that.sortOrder &&
                       Objects.equals(nativeClass, that.nativeClass) &&
                       Objects.equals(aliases, that.aliases);
    }


    /**
     * Return a number that represents the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), allowsDuplicateValues, orderedValues, defaultValueOverride, sortOrder, minimumLength, length, precision, isNullable, nativeClass, aliases);
    }
}

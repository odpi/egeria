/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DataItemSortOrder;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIParameterProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.DatabaseTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.events.EventSchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
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
                @JsonSubTypes.Type(value = DatabaseTableProperties.class, name = "DatabaseTableProperties"),
                @JsonSubTypes.Type(value = EventSchemaAttributeProperties.class, name = "EventSchemaAttributeProperties"),
                @JsonSubTypes.Type(value = APIParameterProperties.class, name = "APIParameterProperties"),
        })
public class SchemaAttributeProperties extends SchemaElementProperties
{
    private int               elementPosition       = 0;
    private int               minCardinality        = 0;
    private int               maxCardinality        = 0;
    private boolean           allowsDuplicateValues = false;
    private boolean           orderedValues         = false;
    private String            defaultValueOverride  = null;
    private DataItemSortOrder sortOrder             = null;
    private int               minimumLength         = 0;
    private int               length                = 0;
    private int               precision             = 0;
    private boolean           isNullable            = true;
    private String            nativeJavaClass       = null;
    private List<String>      aliases               = null;

    /*
     * Details of the associated schema type when type is Primitive, Literal, External or Enum.  Other types are
     * created independently and linked to the schema attribute.
     */

    private String dataType           = null;
    private String defaultValue       = null;
    private String fixedValue         = null;
    private String externalTypeGUID   = null;
    private String validValuesSetGUID = null;

    private SchemaTypeProperties schemaType = null;

    /**
     * Default constructor
     */
    public SchemaAttributeProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName);
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
            elementPosition       = template.getElementPosition();
            minCardinality        = template.getMinCardinality();
            maxCardinality        = template.getMaxCardinality();
            allowsDuplicateValues = template.getAllowsDuplicateValues();
            orderedValues         = template.getOrderedValues();
            sortOrder             = template.getSortOrder();
            minimumLength         = template.getMinimumLength();
            length                = template.getLength();
            precision             = template.getPrecision();
            isNullable            = template.getIsNullable();
            defaultValueOverride  = template.getDefaultValueOverride();
            nativeJavaClass       = template.getNativeJavaClass();
            aliases               = template.getAliases();

            dataType           = template.getDataType();
            defaultValue       = template.getDefaultValue();
            fixedValue         = template.getFixedValue();
            externalTypeGUID   = template.getExternalTypeGUID();
            validValuesSetGUID = template.getValidValuesSetGUID();
            schemaType         = template.getSchemaType();
        }
    }


    /**
     * Return the position of this schema attribute in its parent schema.
     *
     * @return int position in schema - 0 means first
     */
    public int getElementPosition() { return elementPosition; }


    /**
     * Set up the position of this schema attribute in its parent schema.
     *
     * @param elementPosition int position in schema - 0 means first
     */
    public void setElementPosition(int elementPosition)
    {
        this.elementPosition = elementPosition;
    }


    /**
     * Return this minimum number of instances allowed for this attribute.
     *
     * @return int
     */
    public int getMinCardinality()
    {
        return minCardinality;
    }


    /**
     * Set up the minimum number of instances allowed for this attribute.
     *
     * @param minCardinality int
     */
    public void setMinCardinality(int minCardinality)
    {
        this.minCardinality = minCardinality;
    }


    /**
     * Return the maximum number of instances allowed for this attribute.
     *
     * @return int (-1 means infinite)
     */
    public int getMaxCardinality()
    {
        return maxCardinality;
    }


    /**
     * Set up the maximum number of instances allowed for this attribute.
     *
     * @param maxCardinality int (-1 means infinite)
     */
    public void setMaxCardinality(int maxCardinality)
    {
        this.maxCardinality = maxCardinality;
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
    public String getNativeJavaClass()
    {
        return nativeJavaClass;
    }


    /**
     * Set up the name of the Java class to use to represent this type.
     *
     * @param nativeJavaClass fully qualified Java class name
     */
    public void setNativeJavaClass(String nativeJavaClass)
    {
        this.nativeJavaClass = nativeJavaClass;
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
     * Description fo the schema Type.
     *
     * @return schema type properties
     */
    public SchemaTypeProperties getSchemaType()
    {
        return schemaType;
    }


    /**
     * Set up the description of the schemaType.
     *
     * @param schemaType schema type
     */
    public void setSchemaType(SchemaTypeProperties schemaType)
    {
        this.schemaType = schemaType;
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
                "elementPosition=" + elementPosition +
                ", minCardinality=" + minCardinality +
                ", maxCardinality=" + maxCardinality +
                ", allowsDuplicateValues=" + allowsDuplicateValues +
                ", orderedValues=" + orderedValues +
                ", defaultValueOverride='" + defaultValueOverride + '\'' +
                ", sortOrder=" + sortOrder +
                ", minimumLength=" + minimumLength +
                ", length=" + length +
                ", precision=" + precision +
                ", isNullable=" + isNullable +
                ", nativeJavaClass='" + nativeJavaClass + '\'' +
                ", aliases=" + aliases +
                ", dataType='" + dataType + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", fixedValue='" + fixedValue + '\'' +
                ", externalTypeGUID='" + externalTypeGUID + '\'' +
                ", validValuesSetGUID='" + validValuesSetGUID + '\'' +
                ", schemaType=" + schemaType +
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
        return elementPosition == that.elementPosition &&
                       minCardinality == that.minCardinality &&
                       maxCardinality == that.maxCardinality &&
                       allowsDuplicateValues == that.allowsDuplicateValues &&
                       orderedValues == that.orderedValues &&
                       minimumLength == that.minimumLength &&
                       length == that.length &&
                       precision == that.precision &&
                       isNullable == that.isNullable &&
                       Objects.equals(defaultValueOverride, that.defaultValueOverride) &&
                       sortOrder == that.sortOrder &&
                       Objects.equals(nativeJavaClass, that.nativeJavaClass) &&
                       Objects.equals(aliases, that.aliases) &&
                       Objects.equals(dataType, that.dataType) &&
                       Objects.equals(defaultValue, that.defaultValue) &&
                       Objects.equals(fixedValue, that.fixedValue) &&
                       Objects.equals(externalTypeGUID, that.externalTypeGUID) &&
                       Objects.equals(validValuesSetGUID, that.validValuesSetGUID)&&
                       Objects.equals(schemaType, that.schemaType);
    }


    /**
     * Return a number that represents the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementPosition, minCardinality, maxCardinality, allowsDuplicateValues, orderedValues,
                            defaultValueOverride, sortOrder, minimumLength, length, precision, isNullable, nativeJavaClass,
                            aliases, dataType, defaultValue, fixedValue, externalTypeGUID, validValuesSetGUID, schemaType);
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DataItemSortOrder;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A description of a data field.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class DataFieldProperties extends ReferenceableProperties
{
    private String            displayName       = null;
    private String            namespace         = null;
    private List<String>      aliases           = null;
    private String            description       = null;
    private boolean           isDeprecated      = false;
    private String            versionIdentifier = null;
    private String            defaultValue      = null;
    private boolean           isNullable        = true;
    private String            dataType          = null;
    private int               minCardinality    = 0;
    private int               maxCardinality    = 0;
    private int               minimumLength     = 0;
    private int               length            = 0;
    private int               precision         = 0;
    private int               significantDigits = 0;
    private boolean           orderedValues     = false;
    private DataItemSortOrder sortOrder         = null;


    /**
     * Default constructor
     */
    public DataFieldProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataFieldProperties(DataFieldProperties template)
    {
        super(template);

        if (template != null)
        {
            displayName       = template.getDisplayName();
            namespace         = template.getNamespace();
            aliases           = template.getAliases();
            description       = template.getDescription();
            isDeprecated      = template.getIsDeprecated();
            versionIdentifier = template.getVersionIdentifier();
            defaultValue      = template.getDefaultValue();
            isNullable        = template.getIsNullable();
            dataType          = template.getDataType();
            minCardinality    = template.getMinCardinality();
            maxCardinality    = template.getMaxCardinality();
            minimumLength     = template.getMinimumLength();
            length            = template.getLength();
            precision         = template.getPrecision();
            significantDigits = template.getSignificantDigits();
            orderedValues     = template.getOrderedValues();
            sortOrder         = template.getSortOrder();
        }
    }


    /**
     * Return the display name of the file
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name of the file.
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the name of the namespace that this type belongs to.
     *
     * @return string name
     */
    public String getNamespace()
    {
        return namespace;
    }


    /**
     * Set up the name of the namespace that this type belongs to.
     *
     * @param namespace string name
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }


    /**
     * Return a list of alternative names for the data field.
     *
     * @return list of names
     */
    public List<String> getAliases()
    {
        return aliases;
    }


    /**
     * Set up a list of alternative names for the data field.
     *
     * @param aliases list of names
     */
    public void setAliases(List<String> aliases)
    {
        this.aliases = aliases;
    }


    /**
     * Return the description of the file.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the file.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Is the schema element deprecated?
     *
     * @return boolean flag
     */
    public boolean getIsDeprecated()
    {
        return isDeprecated;
    }


    /**
     * Set whether the schema element deprecated or not.  Default is false.
     *
     * @param deprecated boolean flag
     */
    public void setIsDeprecated(boolean deprecated)
    {
        isDeprecated = deprecated;
    }


    /**
     * Return the version identifier for this data field.
     *
     * @return string
     */
    public String getVersionIdentifier()
    {
        return versionIdentifier;
    }


    /**
     * Set up the version identifier for this data field.
     *
     * @param versionIdentifier string
     */
    public void setVersionIdentifier(String versionIdentifier)
    {
        this.versionIdentifier = versionIdentifier;
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
     * Return the number of significant digits before the decimal point (zero means it is an integer).
     *
     * @return int
     */
    public int getSignificantDigits()
    {
        return significantDigits;
    }


    /**
     * Set up the number of significant digits before the decimal point (zero means it is an integer).
     *
     * @param significantDigits int
     */
    public void setSignificantDigits(int significantDigits)
    {
        this.significantDigits = significantDigits;
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DataFieldProperties{" +
                "displayName='" + displayName + '\'' +
                ", namespace='" + namespace + '\'' +
                ", aliases=" + aliases +
                ", description='" + description + '\'' +
                ", isDeprecated=" + isDeprecated +
                ", versionIdentifier='" + versionIdentifier + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", isNullable=" + isNullable +
                ", dataType='" + dataType + '\'' +
                ", minCardinality=" + minCardinality +
                ", maxCardinality=" + maxCardinality +
                ", minimumLength=" + minimumLength +
                ", length=" + length +
                ", precision=" + precision +
                ", significantDigits=" + significantDigits +
                ", orderedValues=" + orderedValues +
                ", sortOrder=" + sortOrder +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        DataFieldProperties that = (DataFieldProperties) objectToCompare;
        return isDeprecated == that.isDeprecated && isNullable == that.isNullable && minCardinality == that.minCardinality && maxCardinality == that.maxCardinality && minimumLength == that.minimumLength && length == that.length && precision == that.precision && significantDigits == that.significantDigits && orderedValues == that.orderedValues && Objects.equals(displayName, that.displayName) && Objects.equals(namespace, that.namespace) && Objects.equals(aliases, that.aliases) && Objects.equals(description, that.description) && Objects.equals(versionIdentifier, that.versionIdentifier) && Objects.equals(defaultValue, that.defaultValue) && Objects.equals(dataType, that.dataType) && sortOrder == that.sortOrder;
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), displayName, namespace, aliases, description, isDeprecated, versionIdentifier, defaultValue, isNullable, dataType, minCardinality, maxCardinality, minimumLength, length, precision, significantDigits, orderedValues, sortOrder);
    }
}

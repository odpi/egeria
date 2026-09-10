/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DataItemSortOrder;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AuthoredReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

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
public class DataFieldProperties extends AuthoredReferenceableProperties
{
    private String       namespacePath = null;
    private List<String> aliases       = null;
    private List<String>      namePatterns      = null;
    private String            defaultValue      = null;
    private boolean           isNullable        = true;
    private String            dataType          = null;
    private String            units             = null;
    private int               minimumLength     = 0;
    private int               length            = 0;
    private int               precision         = 0;
    private boolean           orderedValues     = false;
    private DataItemSortOrder sortOrder         = null;
    private boolean           isPartitionKey        = false;
    private int               partitionKeyPosition  = 0;
    private boolean           allowsDuplicateValues = true;


    /**
     * Default constructor
     */
    public DataFieldProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_FIELD.typeName;
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
            namespacePath = template.getNamespacePath();
            aliases       = template.getAliases();
            namePatterns      = template.getNamePatterns();
            defaultValue      = template.getDefaultValue();
            isNullable        = template.getIsNullable();
            dataType          = template.getDataType();
            units             = template.getUnits();
            minimumLength     = template.getMinimumLength();
            length            = template.getLength();
            precision         = template.getPrecision();
            orderedValues     = template.getOrderedValues();
            sortOrder         = template.getSortOrder();
            isPartitionKey        = template.getIsPartitionKey();
            partitionKeyPosition  = template.getPartitionKeyPosition();
            allowsDuplicateValues = template.getAllowsDuplicateValues();
        }
    }


    /**
     * Return the name of the namespace that this type belongs to.
     *
     * @return string name
     */
    public String getNamespacePath()
    {
        return namespacePath;
    }


    /**
     * Set up the name of the namespace that this type belongs to.
     *
     * @param namespacePath string name
     */
    public void setNamespacePath(String namespacePath)
    {
        this.namespacePath = namespacePath;
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
     * Return a list of name patterns to use when generating schemas.  Use space separated capitalized works so the
     * schema generators can easily convert to valid language keywords.
     *
     * @return string
     */
    public List<String> getNamePatterns()
    {
        return namePatterns;
    }


    /**
     * Set up a list of name patterns to use when generating schemas.  Use space separated capitalized works so the
     * schema generators can easily convert to valid language keywords.
     *
     * @param namePatterns string
     */
    public void setNamePatterns(List<String> namePatterns)
    {
        this.namePatterns = namePatterns;
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
     * Return the units for the data field.
     *
     * @return string
     */
    public String getUnits()
    {
        return units;
    }


    /**
     * Set up the units for the data field.
     *
     * @param units string
     */
    public void setUnits(String units)
    {
        this.units = units;
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
     * Return whether the data field is part of the key used to partition the data.
     *
     * @return boolean
     */
    public boolean getIsPartitionKey()
    {
        return isPartitionKey;
    }


    /**
     * Set up whether the data field is part of the key used to partition the data.
     *
     * @param isPartitionKey boolean
     */
    public void setIsPartitionKey(boolean isPartitionKey)
    {
        this.isPartitionKey = isPartitionKey;
    }


    /**
     * Return the position of the data field in the partition key, starting from 1 (0 means not set).
     *
     * @return int
     */
    public int getPartitionKeyPosition()
    {
        return partitionKeyPosition;
    }


    /**
     * Set up the position of the data field in the partition key, starting from 1.
     *
     * @param partitionKeyPosition int
     */
    public void setPartitionKeyPosition(int partitionKeyPosition)
    {
        this.partitionKeyPosition = partitionKeyPosition;
    }


    /**
     * Return whether the values of this data field may be duplicated across the records of the data (true by default);
     * false means the values are unique.
     *
     * @return boolean
     */
    public boolean getAllowsDuplicateValues()
    {
        return allowsDuplicateValues;
    }


    /**
     * Set up whether the values of this data field may be duplicated across the records of the data.
     *
     * @param allowsDuplicateValues boolean
     */
    public void setAllowsDuplicateValues(boolean allowsDuplicateValues)
    {
        this.allowsDuplicateValues = allowsDuplicateValues;
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
                "namespace='" + namespacePath + '\'' +
                ", aliases=" + aliases +
                ", namePatterns=" + namePatterns +
                ", defaultValue='" + defaultValue + '\'' +
                ", isNullable=" + isNullable +
                ", dataType='" + dataType + '\'' +
                ", units='" + units + '\'' +
                ", minimumLength=" + minimumLength +
                ", length=" + length +
                ", precision=" + precision +
                ", orderedValues=" + orderedValues +
                ", sortOrder=" + sortOrder +
                ", isPartitionKey=" + isPartitionKey +
                ", partitionKeyPosition=" + partitionKeyPosition +
                ", allowsDuplicateValues=" + allowsDuplicateValues +
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
        return isNullable == that.isNullable &&
                minimumLength == that.minimumLength && length == that.length && precision == that.precision &&
                orderedValues == that.orderedValues &&
                Objects.equals(namespacePath, that.namespacePath) && Objects.equals(aliases, that.aliases) &&
                Objects.equals(namePatterns, that.namePatterns) &&
                Objects.equals(defaultValue, that.defaultValue) &&
                Objects.equals(dataType, that.dataType) &&
                Objects.equals(units, that.units) &&
                sortOrder == that.sortOrder &&
                isPartitionKey == that.isPartitionKey &&
                partitionKeyPosition == that.partitionKeyPosition &&
                allowsDuplicateValues == that.allowsDuplicateValues;
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), namespacePath, aliases, namePatterns,
                            defaultValue, isNullable, dataType, units,
                            minimumLength, length, precision, orderedValues, sortOrder, isPartitionKey,
                            partitionKeyPosition, allowsDuplicateValues);
    }
}

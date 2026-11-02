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
    private Boolean           isNullable        = null;
    private String            dataType          = null;
    private String            units             = null;
    private Integer           minimumLength     = null;
    private Integer           length            = null;
    private Integer           precision         = null;
    private Boolean           orderedValues     = null;
    private DataItemSortOrder sortOrder         = null;
    private Boolean           isPartitionKey        = null;
    private Integer           partitionKeyPosition  = null;
    private Boolean           allowsDuplicateValues = null;


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
    public Boolean getIsNullable()
    {
        return isNullable;
    }


    /**
     * Set up whether the field is nullable or not.
     *
     * @param nullable boolean
     */
    public void setIsNullable(Boolean nullable)
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
    public Integer getMinimumLength()
    {
        return minimumLength;
    }


    /**
     * Set up the minimum length of the data.
     *
     * @param minimumLength int
     */
    public void setMinimumLength(Integer minimumLength)
    {
        this.minimumLength = minimumLength;
    }


    /**
     * Return the length of the data field.
     *
     * @return int
     */
    public Integer getLength()
    {
        return length;
    }


    /**
     * Set up the length of the data field.
     *
     * @param length int
     */
    public void setLength(Integer length)
    {
        this.length = length;
    }


    /**
     * Return the number of significant digits to the right of decimal point.
     *
     * @return int
     */
    public Integer getPrecision()
    {
        return precision;
    }


    /**
     * Set up the number of significant digits to the right of decimal point.
     *
     * @param precision int
     */
    public void setPrecision(Integer precision)
    {
        this.precision = precision;
    }


    /**
     * Return whether the attribute instances are arranged in an order.
     *
     * @return boolean flag
     */
    public Boolean getOrderedValues()
    {
        return orderedValues;
    }


    /**
     * Set up whether the attribute instances are arranged in an order.
     *
     * @param orderedValues boolean flag
     */
    public void setOrderedValues(Boolean orderedValues)
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
    public Boolean getIsPartitionKey()
    {
        return isPartitionKey;
    }


    /**
     * Set up whether the data field is part of the key used to partition the data.
     *
     * @param isPartitionKey boolean
     */
    public void setIsPartitionKey(Boolean isPartitionKey)
    {
        this.isPartitionKey = isPartitionKey;
    }


    /**
     * Return the position of the data field in the partition key, starting from 1 (0 means not set).
     *
     * @return int
     */
    public Integer getPartitionKeyPosition()
    {
        return partitionKeyPosition;
    }


    /**
     * Set up the position of the data field in the partition key, starting from 1.
     *
     * @param partitionKeyPosition int
     */
    public void setPartitionKeyPosition(Integer partitionKeyPosition)
    {
        this.partitionKeyPosition = partitionKeyPosition;
    }


    /**
     * Return whether the values of this data field may be duplicated across the records of the data (true by default);
     * false means the values are unique.
     *
     * @return boolean
     */
    public Boolean getAllowsDuplicateValues()
    {
        return allowsDuplicateValues;
    }


    /**
     * Set up whether the values of this data field may be duplicated across the records of the data.
     *
     * @param allowsDuplicateValues boolean
     */
    public void setAllowsDuplicateValues(Boolean allowsDuplicateValues)
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
                "namespacePath='" + namespacePath + '\'' +
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
        return Objects.equals(isNullable, that.isNullable) &&
                Objects.equals(minimumLength, that.minimumLength) && Objects.equals(length, that.length) && Objects.equals(precision, that.precision) &&
                Objects.equals(orderedValues, that.orderedValues) &&
                Objects.equals(namespacePath, that.namespacePath) && Objects.equals(aliases, that.aliases) &&
                Objects.equals(namePatterns, that.namePatterns) &&
                Objects.equals(defaultValue, that.defaultValue) &&
                Objects.equals(dataType, that.dataType) &&
                Objects.equals(units, that.units) &&
                sortOrder == that.sortOrder &&
                Objects.equals(isPartitionKey, that.isPartitionKey) &&
                Objects.equals(partitionKeyPosition, that.partitionKeyPosition) &&
                Objects.equals(allowsDuplicateValues, that.allowsDuplicateValues);
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

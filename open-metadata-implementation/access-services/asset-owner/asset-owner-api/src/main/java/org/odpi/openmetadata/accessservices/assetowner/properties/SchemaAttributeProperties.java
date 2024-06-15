/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.SchemaAttributeElement;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DataItemSortOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaAttributeProperties carries the common parameters for creating or updating schema attributes.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SchemaAttributeElement.class, name = "SchemaAttributeElement"),
              })
public class SchemaAttributeProperties extends SchemaElementProperties
{
    private int               elementPosition       = 0;

    /*
     * Details related to the instances of the attribute
     */
    private int               minCardinality        = 0;
    private int               maxCardinality        = -1;
    private boolean           allowsDuplicateValues = false;
    private boolean           orderedValues         = false;
    private String            defaultValueOverride  = null;
    private DataItemSortOrder sortOrder             = null;
    private int               minimumLength         = 0;
    private int               length                = 0;
    private int               precision             = 0;
    private int               significantDigits     = 0;
    private boolean           isNullable            = true;

    /*
     * Two choices on how the type is expressed
     */
    private SchemaTypeProperties attributeType = null;

    /*
     * Attribute relationships are things like foreign keys
     */
    private List<SchemaAttributeRelationshipProperties> attributeRelationships = null;

    /*
     * Implementation details
     */
    private String            nativeJavaClass       = null;
    private List<String>      aliases               = null;


    /**
     * Default constructor
     */
    public SchemaAttributeProperties()
    {
        super();
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
            elementPosition        = template.getElementPosition();
            minCardinality         = template.getMinCardinality();
            maxCardinality         = template.getMaxCardinality();
            allowsDuplicateValues  = template.getAllowsDuplicateValues();
            orderedValues          = template.getOrderedValues();
            sortOrder              = template.getSortOrder();
            minimumLength          = template.getMinimumLength();
            length                 = template.getLength();
            precision              = template.getPrecision();
            significantDigits      = template.getSignificantDigits();
            isNullable             = template.getIsNullable();
            defaultValueOverride   = template.getDefaultValueOverride();
            attributeType          = template.getAttributeType();
            attributeRelationships = template.getAttributeRelationships();
            nativeJavaClass        = template.getNativeJavaClass();
            aliases                = template.getAliases();
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
     * Return the SchemaType that relates to the type of this attribute.
     *
     * @return SchemaType
     */
    public SchemaTypeProperties getAttributeType()
    {
        return attributeType;
    }


    /**
     * Set up the SchemaType that relates to the type of this attribute.
     *
     * @param attributeType SchemaType
     */
    public void setAttributeType(SchemaTypeProperties attributeType)
    {
        this.attributeType = attributeType;
    }


    /**
     * Return any relationships to other schema attributes.
     *
     * @return list of attribute relationships
     */
    public List<SchemaAttributeRelationshipProperties> getAttributeRelationships()
    {
        if (attributeRelationships == null)
        {
            return null;
        }
        else if (attributeRelationships.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(attributeRelationships);
        }
    }


    /**
     * Set up any relationships to other schema attributes.
     *
     * @param attributeRelationships list of attribute relationships
     */
    public void setAttributeRelationships(List<SchemaAttributeRelationshipProperties> attributeRelationships)
    {
        this.attributeRelationships = attributeRelationships;
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
                       ", significantDigits=" + significantDigits +
                       ", isNullable=" + isNullable +
                       ", attributeType=" + attributeType +
                       ", attributeRelationships=" + attributeRelationships +
                       ", nativeJavaClass='" + nativeJavaClass + '\'' +
                       ", aliases=" + aliases +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", isDeprecated=" + getIsDeprecated() +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
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
        if (! (objectToCompare instanceof SchemaAttributeProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return elementPosition == that.elementPosition &&
                       minCardinality == that.minCardinality &&
                       maxCardinality == that.maxCardinality &&
                       allowsDuplicateValues == that.allowsDuplicateValues &&
                       orderedValues == that.orderedValues &&
                       minimumLength == that.minimumLength &&
                       length == that.length &&
                       precision == that.precision &&
                       significantDigits == that.significantDigits &&
                       isNullable == that.isNullable &&
                       Objects.equals(defaultValueOverride, that.defaultValueOverride) &&
                       sortOrder == that.sortOrder &&
                       Objects.equals(attributeType, that.attributeType) &&
                       Objects.equals(attributeRelationships, that.attributeRelationships) &&
                       Objects.equals(nativeJavaClass, that.nativeJavaClass) &&
                       Objects.equals(aliases, that.aliases);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementPosition, minCardinality, maxCardinality, allowsDuplicateValues, orderedValues,
                            defaultValueOverride, sortOrder, minimumLength, length, precision, significantDigits, isNullable, attributeType,
                            attributeRelationships, nativeJavaClass, aliases);
    }
}

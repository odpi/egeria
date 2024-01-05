/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *  SchemaAttribute describes a single attribute within a schema.  The attribute has a name, order in the
 *  schema and cardinality.  Its type is a SchemaType (such as StructSchemaType or PrimitiveSchemaType).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaAttribute extends SchemaElement
{
    private static final long   serialVersionUID      = 1L;

    protected int               elementPosition       = 0;

    /*
     * Details related to the instances of the attribute
     */
    protected String            cardinality           = null;
    protected int               minCardinality        = 0;
    protected int               maxCardinality        = 0;
    protected boolean           allowsDuplicateValues = true;
    protected boolean           orderedValues         = false;
    protected String            defaultValueOverride  = null;
    protected DataItemSortOrder sortOrder             = null;
    protected int               minimumLength         = 0;
    protected int               length                = 0;
    protected int               precision             = 0;
    protected int               significantDigits     = 0;
    protected boolean           isNullable            = true;


    /*
     * Three choices on how the type is expressed
     */
    protected SchemaType                        attributeType          = null;
    protected List<SchemaAttributeRelationship> attributeRelationships = null;

    /*
     * Implementation details
     */
    protected String            nativeJavaClass       = null;
    protected List<String>      aliases               = null;


    /**
     * Default constructor
     */
    public SchemaAttribute()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template schema attribute to copy.
     */
    public SchemaAttribute(SchemaAttribute template)
    {
        super(template);

        if (template != null)
        {
            elementPosition        = template.getElementPosition();
            cardinality            = template.getCardinality();
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
     * Return a clone of this schema element.  This method is needed because schema element
     * is abstract.
     *
     * @return Clone of subclass.
     */
    public SchemaElement cloneSchemaElement()
    {
        return new SchemaAttribute(this);
    }


    /**
     * Return the name of this schema attribute.  This value is deprecated in favour of displayName.
     *
     * @return String attribute name
     */
    @Deprecated
    public String getAttributeName() { return displayName; }


    /**
     * Set up the name of this schema attribute.  This value is deprecated in favour of displayName.
     *
     * @param attributeName String attribute name
     */
    @Deprecated
    public void setAttributeName(String attributeName)
    {
        this.displayName = attributeName;
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
     * Return the category of the schema attribute.
     *
     * @return enum SchemaAttributeCategory
     */
    public SchemaAttributeCategory getCategory()
    {
        SchemaAttributeCategory category;

        if (maxCardinality == 0)
        {
            category = SchemaAttributeCategory.UNKNOWN;
        }
        else if (maxCardinality == 1)
        {
            category = SchemaAttributeCategory.SINGLETON;
        }
        else if (orderedValues)
        {
            category = SchemaAttributeCategory.ARRAY;
        }
        else if (allowsDuplicateValues)
        {
            category = SchemaAttributeCategory.BAG;
        }
        else
        {
            category = SchemaAttributeCategory.SET;
        }

        return category;
    }


    /**
     * Set up the category of the schema attribute.
     *
     * @param category enum SchemaAttributeCategory
     */
    @SuppressWarnings(value = "unused")
    public void setCategory(SchemaAttributeCategory category)
    {
        /*
         * Nothing to do as this value is derived from the max cardinality, orderedValues and allowsDuplicates.
         */
    }


    /**
     * Return the display version of the cardinality defined for this schema attribute.  There is a deprecated value that many mean this value is
     * set explicitly.  Otherwise it is manufactured from the min and max values.
     *
     * @return String cardinality defined for this schema attribute.
     */
    public String getCardinality()
    {
        if (cardinality != null)
        {
            return cardinality;
        }

        String cardinalityDescription;
        if ((minCardinality < 0) && (maxCardinality < 0))
        {
            cardinalityDescription = "*";
        }
        else
        {
            if (minCardinality < 1)
            {
                cardinalityDescription = "0..";
            }
            else
            {
                cardinalityDescription = minCardinality + "..";
            }

            if (maxCardinality < 0)
            {
                cardinalityDescription = cardinalityDescription + "*";
            }
            else
            {
                cardinalityDescription = cardinalityDescription + maxCardinality;
            }
        }

        return cardinalityDescription;
    }


    /**
     * Set up the display version of the cardinality defined for this schema attribute.
     *
     * @param cardinality String cardinality defined for this schema attribute.
     */
    public void setCardinality(String cardinality)
    {
        this.cardinality = cardinality;
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
    public SchemaType getAttributeType()
    {
        if (attributeType == null)
        {
            return null;
        }
        else
        {
            return attributeType.cloneSchemaType();
        }
    }


    /**
     * Set up the SchemaType that relates to the type of this attribute.
     *
     * @param attributeType SchemaType
     */
    public void setAttributeType(SchemaType attributeType)
    {
        this.attributeType = attributeType;
    }


    /**
     * Return any relationships to other schema attributes.
     *
     * @return list of attribute relationships
     */
    public List<SchemaAttributeRelationship> getAttributeRelationships()
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
    public void setAttributeRelationships(List<SchemaAttributeRelationship> attributeRelationships)
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
        return "SchemaAttribute{" +
                       "elementPosition=" + elementPosition +
                       ", cardinality='" + cardinality + '\'' +
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
                       ", isCalculatedValue=" + isCalculatedValue +
                       ", expression='" + expression + '\'' +
                       ", nativeJavaClass='" + nativeJavaClass + '\'' +
                       ", aliases=" + aliases +
                       ", deprecated=" + getIsDeprecated() +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
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
        SchemaAttribute that = (SchemaAttribute) objectToCompare;
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
                       isCalculatedValue == that.isCalculatedValue &&
                       Objects.equals(cardinality, that.cardinality) &&
                       Objects.equals(defaultValueOverride, that.defaultValueOverride) &&
                       sortOrder == that.sortOrder &&
                       Objects.equals(attributeType, that.attributeType) &&
                       Objects.equals(attributeRelationships, that.attributeRelationships) &&
                       Objects.equals(expression, that.expression) &&
                       Objects.equals(nativeJavaClass, that.nativeJavaClass) &&
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
        return Objects.hash(super.hashCode(), elementPosition, cardinality, minCardinality, maxCardinality, allowsDuplicateValues, orderedValues,
                            defaultValueOverride, sortOrder, minimumLength, length, precision, significantDigits, isNullable, attributeType,
                            attributeRelationships, isCalculatedValue, expression, nativeJavaClass, aliases);
    }
}
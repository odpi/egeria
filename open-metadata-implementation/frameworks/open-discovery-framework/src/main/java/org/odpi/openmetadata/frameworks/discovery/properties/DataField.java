/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.DataItemSortOrder;

import java.io.Serial;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * DataField describes a single data field (column, attribute or property) discovered during the analysis of an asset.  It provides an anchor for
 * annotations that are specific to the field.
 */
public class DataField extends PropertyBase
{
    @Serial
    private static final long serialVersionUID = 1L;

    private int                 dataFieldPosition    = 0;
    private String              dataFieldName        = null;
    private String              dataFieldType        = null;
    private String              dataFieldDescription = null;
    private String              dataFieldNamespace   = null;
    private List<String>        dataFieldAliases     = null;
    private DataItemSortOrder   dataFieldSortOrder   = null;
    private int                 minCardinality       = 0;
    private int                 maxCardinality       = 0;
    private boolean             isNullable           = true;
    private int                 minimumLength        = 0;
    private int                 length               = 0;
    private int                 precision            = 0;
    private int                 significantDigits    = 0;
    private String              defaultValue         = null;
    private boolean             isDeprecated         = false;
    private long                version              = 0L;
    private String              versionIdentifier    = null;
    private Map<String, String> additionalProperties = null;

    private int                 dataFieldAnnotations = 0;
    private int                 nestedDataFields     = 0;
    private int                 linkedDataFields     = 0;


    /**
     * Default constructor
     */
    public DataField()
    {
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public DataField(DataField template)
    {
        super(template);

        if (template != null)
        {
            dataFieldPosition = template.getDataFieldPosition();

            dataFieldName = template.getDataFieldName();
            dataFieldType = template.getDataFieldType();
            dataFieldDescription = template.getDataFieldDescription();
            dataFieldNamespace = template.getDataFieldNamespace();
            dataFieldAliases = template.getDataFieldAliases();
            dataFieldSortOrder = template.getDataFieldSortOrder();
            minCardinality = template.getMinCardinality();
            maxCardinality = template.getMaxCardinality();
            isNullable = template.getIsNullable();
            minimumLength = template.getMinimumLength();
            length = template.getLength();
            precision = template.getPrecision();
            significantDigits = template.getSignificantDigits();
            defaultValue = template.getDefaultValue();
            isDeprecated = template.getIsDeprecated();
            version = template.getVersion();
            versionIdentifier = template.getVersionIdentifier();
            additionalProperties = template.getAdditionalProperties();

            dataFieldAnnotations = template.getDataFieldAnnotations();
            nestedDataFields = template.getNestedDataFields();
            linkedDataFields = template.getLinkedDataFields();
        }
    }


    /**
     * Return the position (index) of the data field in the schema.
     *
     * @return integer
     */
    public int getDataFieldPosition()
    {
        return dataFieldPosition;
    }


    /**
     * Set up the position (index) of the data field in the schema.
     *
     * @param dataFieldPosition integer
     */
    public void setDataFieldPosition(int dataFieldPosition)
    {
        this.dataFieldPosition = dataFieldPosition;
    }


    /**
     * Return the name of this data field.
     *
     * @return string name
     */
    public String getDataFieldName()
    {
        return dataFieldName;
    }


    /**
     * Set up the name of this data field.
     *
     * @param dataFieldName string name
     */
    public void setDataFieldName(String dataFieldName)
    {
        this.dataFieldName = dataFieldName;
    }


    /**
     * Return the name of type of this data field.
     *
     * @return string type name
     */
    public String getDataFieldType()
    {
        return dataFieldType;
    }


    /**
     * Set up the name of type of this data field.  This can be used when building the schema.
     *
     * @param dataFieldType string type name
     */
    public void setDataFieldType(String dataFieldType)
    {
        this.dataFieldType = dataFieldType;
    }


    /**
     * Return description of this data field.
     *
     * @return text
     */
    public String getDataFieldDescription()
    {
        return dataFieldDescription;
    }


    /**
     * Set up the description of this data field.
     *
     * @param dataFieldDescription text
     */
    public void setDataFieldDescription(String dataFieldDescription)
    {
        this.dataFieldDescription = dataFieldDescription;
    }


    /**
     * Return the name of the namespace that this type belongs to.
     *
     * @return string name
     */
    public String getDataFieldNamespace()
    {
        return dataFieldNamespace;
    }


    /**
     * Set up the name of the namespace that this type belongs to.
     *
     * @param dataFieldNamespace string name
     */
    public void setDataFieldNamespace(String dataFieldNamespace)
    {
        this.dataFieldNamespace = dataFieldNamespace;
    }


    /**
     * Return a list of alternative names for the data field.
     *
     * @return list of names
     */
    public List<String> getDataFieldAliases()
    {
        return dataFieldAliases;
    }


    /**
     * Set up a list of alternative names for the data field.
     *
     * @param dataFieldAliases list of names
     */
    public void setDataFieldAliases(List<String> dataFieldAliases)
    {
        this.dataFieldAliases = dataFieldAliases;
    }


    /**
     * Return the order that the data field instances are arranged in - if any.
     *
     * @return DataItemSortOrder enum
     */
    public DataItemSortOrder getDataFieldSortOrder()
    {
        return dataFieldSortOrder;
    }


    /**
     * Set up the order that the data field instances are arranged in - if any.
     *
     * @param dataFieldSortOrder DataItemSortOrder enum
     */
    public void setDataFieldSortOrder(DataItemSortOrder dataFieldSortOrder)
    {
        this.dataFieldSortOrder = dataFieldSortOrder;
    }


    /**
     * Return this minimum number of instances allowed for this data field.
     *
     * @return int
     */
    public int getMinCardinality()
    {
        return minCardinality;
    }


    /**
     * Set up the minimum number of instances allowed for this data field.
     *
     * @param minCardinality int
     */
    public void setMinCardinality(int minCardinality)
    {
        this.minCardinality = minCardinality;
    }


    /**
     * Return the maximum number of instances allowed for this data field.
     *
     * @return int (-1 means infinite)
     */
    public int getMaxCardinality()
    {
        return maxCardinality;
    }


    /**
     * Set up the maximum number of instances allowed for this data field.
     *
     * @param maxCardinality int (-1 means infinite)
     */
    public void setMaxCardinality(int maxCardinality)
    {
        this.maxCardinality = maxCardinality;
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
     * Return whether the data field is deprecated or not.  Default is false.
     *
     * @return boolean flag
     */
    public boolean getIsDeprecated()
    {
        return isDeprecated;
    }


    /**
     * Set whether the data field is deprecated or not.  Default is false.
     *
     * @param deprecated boolean flag
     */
    public void setIsDeprecated(boolean deprecated)
    {
        isDeprecated = deprecated;
    }


    /**
     * Return the default value for the data field.  Null means no default value set up.
     *
     * @return String containing default value
     */
    public String getDefaultValue() { return defaultValue; }


    /**
     * Set up the default value for the data field.  Null means no default value set up.
     *
     * @param defaultValue String containing default value
     */
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }


    /**
     * Return the incremental version number.
     *
     * @return long
     */
    public long getVersion()
    {
        return version;
    }


    /**
     * Set up the incremental version number.
     *
     * @param version long
     */
    public void setVersion(long version)
    {
        this.version = version;
    }


    /**
     * Return the descriptive version identifier.
     *
     * @return string
     */
    public String getVersionIdentifier()
    {
        return versionIdentifier;
    }


    /**
     * Set up the descriptive version identifier.
     *
     * @param versionIdentifier string
     */
    public void setVersionIdentifier(String versionIdentifier)
    {
        this.versionIdentifier = versionIdentifier;
    }


    /**
     * Return any additional properties.
     *
     * @return map of property name to property value
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }

        return new HashMap<>(additionalProperties);
    }


    /**
     * Set up any additional properties.
     *
     * @param additionalProperties map of property name to property value
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return the number of the annotations currently attached to this data field.
     *
     * @return integer
     */
    public int getDataFieldAnnotations()
    {
        return dataFieldAnnotations;
    }


    /**
     * Set up the number of the annotations currently attached to this data field.
     *
     * @param dataFieldAnnotations integer
     */
    public void setDataFieldAnnotations(int dataFieldAnnotations)
    {
        this.dataFieldAnnotations = dataFieldAnnotations;
    }


    /**
     * Return the number of nested data fields.
     *
     * @return integer
     */
    public int getNestedDataFields()
    {
        return nestedDataFields;
    }


    /**
     * Set up the number of nested data fields.
     *
     * @param nestedDataFields integer
     */
    public void setNestedDataFields(int nestedDataFields)
    {
        this.nestedDataFields = nestedDataFields;
    }


    /**
     * Return the number of linked data fields.
     *
     * @return integer
     */
    public int getLinkedDataFields()
    {
        return linkedDataFields;
    }


    /**
     * Set up the number of linked data fields.
     *
     * @param linkedDataFields integer
     */
    public void setLinkedDataFields(int linkedDataFields)
    {
        this.linkedDataFields = linkedDataFields;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataField{" +
                       "dataFieldPosition=" + dataFieldPosition +
                       ", dataFieldName='" + dataFieldName + '\'' +
                       ", dataFieldType='" + dataFieldType + '\'' +
                       ", dataFieldDescription='" + dataFieldDescription + '\'' +
                       ", dataFieldNamespace='" + dataFieldNamespace + '\'' +
                       ", dataFieldAliases=" + dataFieldAliases +
                       ", dataFieldSortOrder=" + dataFieldSortOrder +
                       ", minCardinality=" + minCardinality +
                       ", maxCardinality=" + maxCardinality +
                       ", isNullable=" + isNullable +
                       ", minimumLength=" + minimumLength +
                       ", length=" + length +
                       ", precision=" + precision +
                       ", significantDigits=" + significantDigits +
                       ", defaultValue='" + defaultValue + '\'' +
                       ", isDeprecated=" + isDeprecated +
                       ", version=" + version +
                       ", versionIdentifier='" + versionIdentifier + '\'' +
                       ", additionalProperties=" + additionalProperties +
                       ", dataFieldAnnotations=" + dataFieldAnnotations +
                       ", nestedDataFields=" + nestedDataFields +
                       ", headerVersion=" + getHeaderVersion() +
                       ", elementHeader=" + getElementHeader() +
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
        if (! (objectToCompare instanceof DataField dataField))
        {
            return false;
        }
        return dataFieldPosition == dataField.dataFieldPosition &&
                       minCardinality == dataField.minCardinality &&
                       maxCardinality == dataField.maxCardinality &&
                       isNullable == dataField.isNullable &&
                       minimumLength == dataField.minimumLength &&
                       length == dataField.length &&
                       precision == dataField.precision &&
                       significantDigits == dataField.significantDigits &&
                       isDeprecated == dataField.isDeprecated &&
                       version == dataField.version &&
                       dataFieldAnnotations == dataField.dataFieldAnnotations
                       && nestedDataFields == dataField.nestedDataFields &&
                       Objects.equals(dataFieldName, dataField.dataFieldName) &&
                       Objects.equals(dataFieldType, dataField.dataFieldType) &&
                       Objects.equals(dataFieldDescription, dataField.dataFieldDescription) &&
                       Objects.equals(dataFieldNamespace, dataField.dataFieldNamespace) &&
                       Objects.equals(dataFieldAliases, dataField.dataFieldAliases) &&
                       dataFieldSortOrder == dataField.dataFieldSortOrder &&
                       Objects.equals(defaultValue, dataField.defaultValue) &&
                       Objects.equals(versionIdentifier, dataField.versionIdentifier) &&
                       Objects.equals(additionalProperties, dataField.additionalProperties);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(dataFieldPosition, dataFieldName, dataFieldType, dataFieldDescription, dataFieldNamespace, dataFieldAliases,
                            dataFieldSortOrder, minCardinality, maxCardinality, isNullable, minimumLength, length, precision, significantDigits,
                            defaultValue, isDeprecated, version, versionIdentifier, additionalProperties, dataFieldAnnotations, nestedDataFields);
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataTypeDefAttribute stores the properties used to describe a attribute within a Classification,
 * Entity or Relationship.  The attribute may itself be of types Enum, Collection or Primitive Types.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataTypeDefAttribute extends OpenMetadataTypeDefElementHeader
{
    protected String                       attributeName   = null;
    protected OpenMetadataAttributeTypeDef       attributeType       = null;
    protected OpenMetadataTypeDefAttributeStatus attributeStatus     = null;
    protected String                             replacedByAttribute = null;
    protected String                             attributeDescription     = null;
    protected String                           attributeDescriptionGUID = null;
    protected OpenMetadataAttributeCardinality cardinality              = OpenMetadataAttributeCardinality.UNKNOWN;
    protected int                              valuesMinCount           = 0;
    protected int                                valuesMaxCount           = 1;
    protected boolean                            isIndexable              = true;
    protected boolean                            isUnique                 = false;
    protected String                            defaultValue                 = null;
    protected List<ExternalStandardTypeMapping> externalStandardTypeMappings = null;


    /**
     * Default constructor creates an empty OpenMetadataTypeDefAttribute.
     */
    public OpenMetadataTypeDefAttribute()
    {
        super();
    }


    /**
     * Copy/clone constructor initialized with the values from the supplied template.
     *
     * @param template OpenMetadataTypeDefAttribute to copy
     */
    public OpenMetadataTypeDefAttribute(OpenMetadataTypeDefAttribute template)
    {
        super(template);

        if (template != null)
        {
            attributeName = template.getAttributeName();
            attributeType = template.getAttributeType();
            attributeStatus = template.getAttributeStatus();
            replacedByAttribute = template.getReplacedByAttribute();
            attributeDescription = template.getAttributeDescription();
            attributeDescriptionGUID = template.getAttributeDescriptionGUID();
            cardinality = template.getAttributeCardinality();
            valuesMinCount = template.getValuesMinCount();
            valuesMaxCount = template.getValuesMaxCount();
            isUnique = template.isUnique();
            isIndexable = template.isIndexable();
            defaultValue = template.getDefaultValue();
            this.setExternalStandardMappings(template.getExternalStandardMappings());
        }
    }


    /**
     * Return the name of this attribute.
     *
     * @return String name
     */
    public String getAttributeName()
    {
        return attributeName;
    }


    /**
     * Set up the name of this attribute.
     *
     * @param attributeName String name
     */
    public void setAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
    }


    /**
     * Return the name of the type for the value in this attribute.
     *
     * @return OpenMetadataAttributeTypeDef definition of attribute type
     */
    public OpenMetadataAttributeTypeDef getAttributeType() { return attributeType; }


    /**
     * Set up the name of the type for the value in this attribute.
     *
     * @param attributeType OpenMetadataAttributeTypeDef
     */
    public void setAttributeType(OpenMetadataAttributeTypeDef attributeType) {this.attributeType = attributeType; }


    /**
     * Return the status of this attribute.
     *
     * @return status (null means ACTIVE)
     */
    public OpenMetadataTypeDefAttributeStatus getAttributeStatus()
    {
        return attributeStatus;
    }


    /**
     * Set up the status of this attribute.
     *
     * @param attributeStatus status (null means ACTIVE)
     */
    public void setAttributeStatus(OpenMetadataTypeDefAttributeStatus attributeStatus)
    {
        this.attributeStatus = attributeStatus;
    }


    /**
     * If the attribute has been renamed, this contains the name of the new attribute.
     *
     * @return new attribute name
     */
    public String getReplacedByAttribute()
    {
        return replacedByAttribute;
    }


    /**
     * If the attribute has been renamed, this contains the name of the new attribute.
     *
     * @param replacedByAttribute new attribute name
     */
    public void setReplacedByAttribute(String replacedByAttribute)
    {
        this.replacedByAttribute = replacedByAttribute;
    }


    /**
     * Return the short description of the attribute.
     *
     * @return String description
     */
    public String getAttributeDescription()
    {
        return attributeDescription;
    }


    /**
     * Set up the short description of the attribute.
     *
     * @param attributeDescription String description
     */
    public void setAttributeDescription(String attributeDescription)
    {
        this.attributeDescription = attributeDescription;
    }


    /**
     * Return the unique id of the glossary term that describes this attribute (or null if
     * no attribute defined).
     *
     * @return String guid
     */
    public String getAttributeDescriptionGUID()
    {
        return attributeDescriptionGUID;
    }


    /**
     * Set up the unique id of the glossary term that describes this attribute (or null if
     * no attribute defined).
     *
     * @param attributeDescriptionGUID String guid
     */
    public void setAttributeDescriptionGUID(String attributeDescriptionGUID)
    {
        this.attributeDescriptionGUID = attributeDescriptionGUID;
    }


    /**
     * Return the cardinality of this attribute.
     *
     * @return OpenMetadataAttributeCardinality Enum.
     */
    public OpenMetadataAttributeCardinality getAttributeCardinality() { return cardinality; }


    /**
     * Set up the cardinality for this attribute.
     *
     * @param attributeCardinality enum value
     */
    public void setAttributeCardinality(OpenMetadataAttributeCardinality attributeCardinality) {this.cardinality = attributeCardinality; }


    /**
     * Return the minimum number of values for this attribute (relevant for Arrays, Sets and Maps).
     *
     * @return int minimum count
     */
    public int getValuesMinCount() { return valuesMinCount; }


    /**
     * Set up the minimum number of values for this attribute (relevant for Arrays, Sets and Maps).
     *
     * @param valuesMinCount int minimum count
     */
    public void setValuesMinCount(int valuesMinCount) { this.valuesMinCount = valuesMinCount; }


    /**
     * Return the maximum number of values for this attribute (relevant for Arrays, Sets and Maps).
     *
     * @return int maximum count
     */
    public int getValuesMaxCount() { return valuesMaxCount; }


    /**
     * Set up the maximum number of values for this attribute (relevant for Arrays, Sets and Maps).
     *
     * @param valuesMaxCount int maximum count
     */
    public void setValuesMaxCount(int valuesMaxCount) { this.valuesMaxCount = valuesMaxCount; }


    /**
     * Return whether the value for this attribute is unique across the specific instances of Struct, Classification,
     * Entity or Relationship types that this attribute included in.
     *
     * @return boolean isUnique flag
     */
    public boolean isUnique() { return isUnique; }


    /**
     * Set up the isUnique flag.  This indicates whether the value for this attribute is unique
     * across the specific instances of Struct, Classification,
     * Entity or Relationship types that this attribute included in.
     *
     * @param unique boolean isUnique flag
     */
    public void setUnique(boolean unique) { isUnique = unique; }


    /**
     * Return whether this attribute should be included in the metadata collection's search index.
     *
     * @return boolean isIndexable flag
     */
    public boolean isIndexable() { return isIndexable; }


    /**
     * Set up the isIndexable flag.  This indicates whether this attribute should be included in the
     * metadata collection's search index.
     *
     * @param indexable boolean isIndexable flag
     */
    public void setIndexable(boolean indexable) { isIndexable = indexable; }


    /**
     * Return the default value for this attribute.
     *
     * @return String default value
     */
    public String getDefaultValue() { return defaultValue; }


    /**
     * Set up the default value for this attribute.
     *
     * @param defaultValue String
     */
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }


    /**
     * Return the list of mappings to external standards.
     *
     * @return ExternalStandardMappings list
     */
    public List<ExternalStandardTypeMapping> getExternalStandardMappings()
    {
        return externalStandardTypeMappings;
    }


    /**
     * Set up the list of mappings to external standards.
     *
     * @param externalStandardTypeMappings ExternalStandardMappings list
     */
    public void setExternalStandardMappings(List<ExternalStandardTypeMapping> externalStandardTypeMappings)
    {
        this.externalStandardTypeMappings = externalStandardTypeMappings;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataTypeDefAttribute{" +
                "attributeName='" + attributeName + '\'' +
                ", attributeType=" + attributeType +
                ", attributeStatus=" + attributeStatus +
                ", replacedByAttribute='" + replacedByAttribute + '\'' +
                ", attributeDescription='" + attributeDescription + '\'' +
                ", attributeDescriptionGUID='" + attributeDescriptionGUID + '\'' +
                ", cardinality=" + cardinality +
                ", valuesMinCount=" + valuesMinCount +
                ", valuesMaxCount=" + valuesMaxCount +
                ", isIndexable=" + isIndexable +
                ", isUnique=" + isUnique +
                ", defaultValue='" + defaultValue + '\'' +
                ", externalStandardTypeMappings=" + externalStandardTypeMappings + '}';
    }


    /**
     * Verify that supplied object has the same properties.
     *
     * @param objectToCompare object to test
     * @return result
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
        OpenMetadataTypeDefAttribute that = (OpenMetadataTypeDefAttribute) objectToCompare;
        return valuesMinCount == that.valuesMinCount &&
                valuesMaxCount == that.valuesMaxCount &&
                isIndexable == that.isIndexable &&
                isUnique == that.isUnique &&
                Objects.equals(attributeName, that.attributeName) &&
                Objects.equals(attributeType, that.attributeType) &&
                attributeStatus == that.attributeStatus &&
                Objects.equals(replacedByAttribute, that.replacedByAttribute) &&
                Objects.equals(attributeDescription, that.attributeDescription) &&
                Objects.equals(attributeDescriptionGUID, that.attributeDescriptionGUID) &&
                cardinality == that.cardinality &&
                Objects.equals(defaultValue, that.defaultValue) &&
                Objects.equals(externalStandardTypeMappings, that.externalStandardTypeMappings);
    }


    /**
     * Int for hash map
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(attributeName, attributeType, attributeStatus, replacedByAttribute, attributeDescription, attributeDescriptionGUID, cardinality, valuesMinCount, valuesMaxCount, isIndexable, isUnique, defaultValue,
                            externalStandardTypeMappings);
    }
}

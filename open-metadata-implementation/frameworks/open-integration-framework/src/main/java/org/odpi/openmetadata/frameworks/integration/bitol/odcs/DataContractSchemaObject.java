/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a schema object (typically a table, view, topic or file) in an Open Data Contract
 * Standard (ODCS) data contract. The schema object has a list of properties (columns/fields), relationships to
 * other schema objects and data quality checks.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataContractSchemaObject extends DataContractSchemaElement
{
    private String                           logicalType = null;
    private String                           dataGranularityDescription = null;
    private List<DataContractSchemaProperty> properties = null;
    private List<DataContractRelationship>   relationships = null;
    private List<DataContractQualityRule>    quality = null;


    /**
     * Default constructor
     */
    public DataContractSchemaObject()
    {
    }


    /**
     * Return the logical type of the schema object.  The only standard value is object.
     *
     * @return string type name
     */
    public String getLogicalType()
    {
        return logicalType;
    }


    /**
     * Set up the logical type of the schema object.  The only standard value is object.
     *
     * @param logicalType string type name
     */
    public void setLogicalType(String logicalType)
    {
        this.logicalType = logicalType;
    }


    /**
     * Return the description of the granularity of the data, for example the aggregation level.
     *
     * @return string description
     */
    public String getDataGranularityDescription()
    {
        return dataGranularityDescription;
    }


    /**
     * Set up the description of the granularity of the data, for example the aggregation level.
     *
     * @param dataGranularityDescription string description
     */
    public void setDataGranularityDescription(String dataGranularityDescription)
    {
        this.dataGranularityDescription = dataGranularityDescription;
    }


    /**
     * Return the list of properties (columns/fields) of the schema object.
     *
     * @return list of properties
     */
    public List<DataContractSchemaProperty> getProperties()
    {
        return properties;
    }


    /**
     * Set up the list of properties (columns/fields) of the schema object.
     *
     * @param properties list of properties
     */
    public void setProperties(List<DataContractSchemaProperty> properties)
    {
        this.properties = properties;
    }


    /**
     * Return the list of relationships from this schema object to other schema objects.
     *
     * @return list of relationships
     */
    public List<DataContractRelationship> getRelationships()
    {
        return relationships;
    }


    /**
     * Set up the list of relationships from this schema object to other schema objects.
     *
     * @param relationships list of relationships
     */
    public void setRelationships(List<DataContractRelationship> relationships)
    {
        this.relationships = relationships;
    }


    /**
     * Return the list of data quality checks for this schema object.
     *
     * @return list of quality rules
     */
    public List<DataContractQualityRule> getQuality()
    {
        return quality;
    }


    /**
     * Set up the list of data quality checks for this schema object.
     *
     * @param quality list of quality rules
     */
    public void setQuality(List<DataContractQualityRule> quality)
    {
        this.quality = quality;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataContractSchemaObject{" +
                       "logicalType='" + logicalType + '\'' +
                       ", dataGranularityDescription='" + dataGranularityDescription + '\'' +
                       ", properties=" + properties +
                       ", relationships=" + relationships +
                       ", quality=" + quality +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        DataContractSchemaObject that = (DataContractSchemaObject) objectToCompare;
        return Objects.equals(logicalType, that.logicalType) &&
                       Objects.equals(dataGranularityDescription, that.dataGranularityDescription) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(relationships, that.relationships) &&
                       Objects.equals(quality, that.quality);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), logicalType, dataGranularityDescription, properties, relationships, quality);
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a property (typically a column or field) of a schema object in an Open Data Contract
 * Standard (ODCS) data contract. Properties may be nested: a property with logicalType object has its own list
 * of properties, and a property with logicalType array describes its element type in the items property. This
 * class is used for both schema properties and array item properties since the standard gives them the same
 * shape.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataContractSchemaProperty extends DataContractSchemaElement
{
    private String                           logicalType = null;
    private DataContractLogicalTypeOptions   logicalTypeOptions = null;
    private Boolean                          primaryKey = null;
    private Integer                          primaryKeyPosition = null;
    private Boolean                          required = null;
    private Boolean                          unique = null;
    private Boolean                          partitioned = null;
    private Integer                          partitionKeyPosition = null;
    private String                           classification = null;
    private String                           encryptedName = null;
    private List<String>                     transformSourceObjects = null;
    private String                           transformLogic = null;
    private String                           transformDescription = null;
    private List<Object>                     examples = null;
    private Boolean                          criticalDataElement = null;
    private List<DataContractRelationship>   relationships = null;
    private List<DataContractQualityRule>    quality = null;
    private List<DataContractSchemaProperty> properties = null;
    private DataContractSchemaProperty       items = null;
    private List<DataContractEnumValue> enumValues = null;
    private DataContractSchemaMap map = null;
    private String semanticType = null;


    /**
     * Default constructor
     */
    public DataContractSchemaProperty()
    {
    }


    /**
     * Return the logical (platform independent) type of the property.  Standard values are defined in DataContractLogicalType.
     *
     * @return string type name
     */
    public String getLogicalType()
    {
        return logicalType;
    }


    /**
     * Set up the logical (platform independent) type of the property.  Standard values are defined in DataContractLogicalType.
     *
     * @param logicalType string type name
     */
    public void setLogicalType(String logicalType)
    {
        this.logicalType = logicalType;
    }


    /**
     * Return the options that refine the logical type.
     *
     * @return logical type options
     */
    public DataContractLogicalTypeOptions getLogicalTypeOptions()
    {
        return logicalTypeOptions;
    }


    /**
     * Set up the options that refine the logical type.
     *
     * @param logicalTypeOptions logical type options
     */
    public void setLogicalTypeOptions(DataContractLogicalTypeOptions logicalTypeOptions)
    {
        this.logicalTypeOptions = logicalTypeOptions;
    }


    /**
     * Return whether the property is part of the primary key.
     *
     * @return boolean flag or null if not specified
     */
    public Boolean getPrimaryKey()
    {
        return primaryKey;
    }


    /**
     * Set up whether the property is part of the primary key.
     *
     * @param primaryKey boolean flag or null if not specified
     */
    public void setPrimaryKey(Boolean primaryKey)
    {
        this.primaryKey = primaryKey;
    }


    /**
     * Return the position of the property in the primary key (starting from 1; -1 if not part of the key).
     *
     * @return integer value or null if not specified
     */
    public Integer getPrimaryKeyPosition()
    {
        return primaryKeyPosition;
    }


    /**
     * Set up the position of the property in the primary key (starting from 1; -1 if not part of the key).
     *
     * @param primaryKeyPosition integer value or null if not specified
     */
    public void setPrimaryKeyPosition(Integer primaryKeyPosition)
    {
        this.primaryKeyPosition = primaryKeyPosition;
    }


    /**
     * Return whether a value is required for the property (not nullable).
     *
     * @return boolean flag or null if not specified
     */
    public Boolean getRequired()
    {
        return required;
    }


    /**
     * Set up whether a value is required for the property (not nullable).
     *
     * @param required boolean flag or null if not specified
     */
    public void setRequired(Boolean required)
    {
        this.required = required;
    }


    /**
     * Return whether the values of the property are unique.
     *
     * @return boolean flag or null if not specified
     */
    public Boolean getUnique()
    {
        return unique;
    }


    /**
     * Set up whether the values of the property are unique.
     *
     * @param unique boolean flag or null if not specified
     */
    public void setUnique(Boolean unique)
    {
        this.unique = unique;
    }


    /**
     * Return whether the property is a partition key.
     *
     * @return boolean flag or null if not specified
     */
    public Boolean getPartitioned()
    {
        return partitioned;
    }


    /**
     * Set up whether the property is a partition key.
     *
     * @param partitioned boolean flag or null if not specified
     */
    public void setPartitioned(Boolean partitioned)
    {
        this.partitioned = partitioned;
    }


    /**
     * Return the position of the property in the partition key (starting from 1; -1 if not a partition key).
     *
     * @return integer value or null if not specified
     */
    public Integer getPartitionKeyPosition()
    {
        return partitionKeyPosition;
    }


    /**
     * Set up the position of the property in the partition key (starting from 1; -1 if not a partition key).
     *
     * @param partitionKeyPosition integer value or null if not specified
     */
    public void setPartitionKeyPosition(Integer partitionKeyPosition)
    {
        this.partitionKeyPosition = partitionKeyPosition;
    }


    /**
     * Return the data classification of the property, for example public, restricted or confidential.
     *
     * @return string classification
     */
    public String getClassification()
    {
        return classification;
    }


    /**
     * Set up the data classification of the property, for example public, restricted or confidential.
     *
     * @param classification string classification
     */
    public void setClassification(String classification)
    {
        this.classification = classification;
    }


    /**
     * Return the name of the property that holds the encrypted version of this property's values.
     *
     * @return string name
     */
    public String getEncryptedName()
    {
        return encryptedName;
    }


    /**
     * Set up the name of the property that holds the encrypted version of this property's values.
     *
     * @param encryptedName string name
     */
    public void setEncryptedName(String encryptedName)
    {
        this.encryptedName = encryptedName;
    }


    /**
     * Return the list of source objects used in the transformation that produces the property.
     *
     * @return list of object names
     */
    public List<String> getTransformSourceObjects()
    {
        return transformSourceObjects;
    }


    /**
     * Set up the list of source objects used in the transformation that produces the property.
     *
     * @param transformSourceObjects list of object names
     */
    public void setTransformSourceObjects(List<String> transformSourceObjects)
    {
        this.transformSourceObjects = transformSourceObjects;
    }


    /**
     * Return the logic (for example SQL) of the transformation that produces the property.
     *
     * @return string logic
     */
    public String getTransformLogic()
    {
        return transformLogic;
    }


    /**
     * Set up the logic (for example SQL) of the transformation that produces the property.
     *
     * @param transformLogic string logic
     */
    public void setTransformLogic(String transformLogic)
    {
        this.transformLogic = transformLogic;
    }


    /**
     * Return the description in business terms of the transformation that produces the property.
     *
     * @return string description
     */
    public String getTransformDescription()
    {
        return transformDescription;
    }


    /**
     * Set up the description in business terms of the transformation that produces the property.
     *
     * @param transformDescription string description
     */
    public void setTransformDescription(String transformDescription)
    {
        this.transformDescription = transformDescription;
    }


    /**
     * Return the list of example values.
     *
     * @return list of values
     */
    public List<Object> getExamples()
    {
        return examples;
    }


    /**
     * Set up the list of example values.
     *
     * @param examples list of values
     */
    public void setExamples(List<Object> examples)
    {
        this.examples = examples;
    }


    /**
     * Return whether the property is a critical data element.
     *
     * @return boolean flag or null if not specified
     */
    public Boolean getCriticalDataElement()
    {
        return criticalDataElement;
    }


    /**
     * Set up whether the property is a critical data element.
     *
     * @param criticalDataElement boolean flag or null if not specified
     */
    public void setCriticalDataElement(Boolean criticalDataElement)
    {
        this.criticalDataElement = criticalDataElement;
    }


    /**
     * Return the list of relationships from this property to other properties.
     *
     * @return list of relationships
     */
    public List<DataContractRelationship> getRelationships()
    {
        return relationships;
    }


    /**
     * Set up the list of relationships from this property to other properties.
     *
     * @param relationships list of relationships
     */
    public void setRelationships(List<DataContractRelationship> relationships)
    {
        this.relationships = relationships;
    }


    /**
     * Return the list of data quality checks for this property.
     *
     * @return list of quality rules
     */
    public List<DataContractQualityRule> getQuality()
    {
        return quality;
    }


    /**
     * Set up the list of data quality checks for this property.
     *
     * @param quality list of quality rules
     */
    public void setQuality(List<DataContractQualityRule> quality)
    {
        this.quality = quality;
    }


    /**
     * Return the nested properties (when the logical type is object).
     *
     * @return list of nested properties
     */
    public List<DataContractSchemaProperty> getProperties()
    {
        return properties;
    }


    /**
     * Set up the nested properties (when the logical type is object).
     *
     * @param properties list of nested properties
     */
    public void setProperties(List<DataContractSchemaProperty> properties)
    {
        this.properties = properties;
    }


    /**
     * Return the description of the items (when the logical type is array).
     *
     * @return item property description
     */
    public DataContractSchemaProperty getItems()
    {
        return items;
    }


    /**
     * Set up the description of the items (when the logical type is array).
     *
     * @param items item property description
     */
    public void setItems(DataContractSchemaProperty items)
    {
        this.items = items;
    }


    /**
     * Return the enumeration of allowed values for this property (the enum array in the document).
     *
     * @return List<DataContractEnumValue>
     */
    @JsonProperty("enum")
    public List<DataContractEnumValue> getEnumValues()
    {
        return enumValues;
    }


    /**
     * Set up the enumeration of allowed values for this property (the enum array in the document).
     *
     * @param enumValues List<DataContractEnumValue>
     */
    @JsonProperty("enum")
    public void setEnumValues(List<DataContractEnumValue> enumValues)
    {
        this.enumValues = enumValues;
    }


    /**
     * Return the key and value definitions when the logical type is map.
     *
     * @return DataContractSchemaMap
     */
    public DataContractSchemaMap getMap()
    {
        return map;
    }


    /**
     * Set up the key and value definitions when the logical type is map.
     *
     * @param map DataContractSchemaMap
     */
    public void setMap(DataContractSchemaMap map)
    {
        this.map = map;
    }


    /**
     * Return the semantic role of the property: column (the default), measure or dimension.
     *
     * @return String
     */
    public String getSemanticType()
    {
        return semanticType;
    }


    /**
     * Set up the semantic role of the property: column (the default), measure or dimension.
     *
     * @param semanticType String
     */
    public void setSemanticType(String semanticType)
    {
        this.semanticType = semanticType;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataContractSchemaProperty{" +
                       "logicalType='" + logicalType + '\'' +
                       ", logicalTypeOptions=" + logicalTypeOptions +
                       ", primaryKey=" + primaryKey +
                       ", primaryKeyPosition=" + primaryKeyPosition +
                       ", required=" + required +
                       ", unique=" + unique +
                       ", partitioned=" + partitioned +
                       ", partitionKeyPosition=" + partitionKeyPosition +
                       ", classification='" + classification + '\'' +
                       ", encryptedName='" + encryptedName + '\'' +
                       ", transformSourceObjects=" + transformSourceObjects +
                       ", transformLogic='" + transformLogic + '\'' +
                       ", transformDescription='" + transformDescription + '\'' +
                       ", examples=" + examples +
                       ", criticalDataElement=" + criticalDataElement +
                       ", relationships=" + relationships +
                       ", quality=" + quality +
                       ", properties=" + properties +
                       ", items=" + items +
                       ", enumValues=" + enumValues +
                       ", map=" + map +
                       ", semanticType=" + semanticType +
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
        DataContractSchemaProperty that = (DataContractSchemaProperty) objectToCompare;
        return Objects.equals(logicalType, that.logicalType) &&
                       Objects.equals(logicalTypeOptions, that.logicalTypeOptions) &&
                       Objects.equals(primaryKey, that.primaryKey) &&
                       Objects.equals(primaryKeyPosition, that.primaryKeyPosition) &&
                       Objects.equals(required, that.required) &&
                       Objects.equals(unique, that.unique) &&
                       Objects.equals(partitioned, that.partitioned) &&
                       Objects.equals(partitionKeyPosition, that.partitionKeyPosition) &&
                       Objects.equals(classification, that.classification) &&
                       Objects.equals(encryptedName, that.encryptedName) &&
                       Objects.equals(transformSourceObjects, that.transformSourceObjects) &&
                       Objects.equals(transformLogic, that.transformLogic) &&
                       Objects.equals(transformDescription, that.transformDescription) &&
                       Objects.equals(examples, that.examples) &&
                       Objects.equals(criticalDataElement, that.criticalDataElement) &&
                       Objects.equals(relationships, that.relationships) &&
                       Objects.equals(quality, that.quality) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(items, that.items) &&
                       Objects.equals(enumValues, that.enumValues) &&
                       Objects.equals(map, that.map) &&
                       Objects.equals(semanticType, that.semanticType);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), logicalType, logicalTypeOptions, primaryKey, primaryKeyPosition, required, unique, partitioned, partitionKeyPosition, classification, encryptedName, transformSourceObjects, transformLogic, transformDescription, examples, criticalDataElement, relationships, quality, properties, items, enumValues, map, semanticType);
    }
}

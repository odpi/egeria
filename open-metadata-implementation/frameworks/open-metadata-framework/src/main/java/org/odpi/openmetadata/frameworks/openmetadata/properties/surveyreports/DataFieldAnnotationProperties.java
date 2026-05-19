/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataFieldAnnotationProperties is a common base class for annotations that are attached to a data field.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ClassificationAnnotationProperties.class, name = "ClassificationAnnotationProperties"),
                @JsonSubTypes.Type(value = DataClassAnnotationProperties.class, name = "DataClassAnnotationProperties"),
                @JsonSubTypes.Type(value = DataGrainAnnotationProperties.class, name = "DataGrainAnnotationProperties"),
                @JsonSubTypes.Type(value = FingerprintAnnotationProperties.class, name = "FingerprintAnnotationProperties"),
                @JsonSubTypes.Type(value = QualityAnnotationProperties.class, name = "QualityAnnotationProperties"),
                @JsonSubTypes.Type(value = ResourceProfileAnnotationProperties.class, name = "ResourceProfileAnnotationProperties"),
                @JsonSubTypes.Type(value = ResourceProfileLogAnnotationProperties.class, name = "ResourceProfileLogAnnotationProperties"),
                @JsonSubTypes.Type(value = RelationshipAdviceAnnotationProperties.class, name = "RelationshipAdviceAnnotationProperties"),
                @JsonSubTypes.Type(value = RequestForActionProperties.class, name = "RequestForActionProperties"),
                @JsonSubTypes.Type(value = SemanticAnnotationProperties.class, name = "SemanticAnnotationProperties"),
        })
public class DataFieldAnnotationProperties extends AnnotationProperties
{
    private String dataType          = null;
    private long   matchingValues    = 0L;
    private long   nonMatchingValues = 0L;

    /**
     * Default constructor
     */
    public DataFieldAnnotationProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_FIELD_ANNOTATION.typeName;
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public DataFieldAnnotationProperties(DataFieldAnnotationProperties template)
    {
        super(template);

        if (template != null)
        {
            dataType          = template.getDataType();
            matchingValues    = template.getMatchingValues();
            nonMatchingValues = template.getNonMatchingValues();
        }
    }


    /**
     * Return the data type for this element.  Null means an unknown data type.
     *
     * @return String data type name
     */
    public String getDataType() { return dataType; }


    /**
     * Set up the data type for this element.  Null means an unknown data type.
     *
     * @param dataType data type name
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }


    /**
     * Return the count of matching values that match the specification of these data classes.
     *
     * @return long
     */
    public long getMatchingValues()
    {
        return matchingValues;
    }


    /**
     * Set up the count of matching values that match the specification of these data classes.
     *
     * @param matchingValues long
     */
    public void setMatchingValues(long matchingValues)
    {
        this.matchingValues = matchingValues;
    }


    /**
     * Return the count of values that do not match the specification of these data classes.
     *
     * @return long
     */
    public long getNonMatchingValues()
    {
        return nonMatchingValues;
    }


    /**
     * Set up the count of values that do not match the specification of these data classes.
     *
     * @param nonMatchingValues long
     */
    public void setNonMatchingValues(long nonMatchingValues)
    {
        this.nonMatchingValues = nonMatchingValues;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataFieldAnnotationProperties{" +
                "matchingValues=" + matchingValues +
                ", nonMatchingValues=" + nonMatchingValues +
                ", dataType='" + dataType + '\'' +
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
        DataFieldAnnotationProperties that = (DataFieldAnnotationProperties) objectToCompare;
        return matchingValues == that.matchingValues &&
                nonMatchingValues == that.nonMatchingValues &&
                Objects.equals(dataType, that.dataType);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), dataType, matchingValues, nonMatchingValues);
    }
}

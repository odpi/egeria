/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataSourceMeasurementAnnotation describes properties that describe the characteristics of the data source as a whole.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DataSourcePhysicalStatusAnnotation.class, name = "DataSourcePhysicalStatusAnnotation")
        })
public class DataSourceMeasurementAnnotation extends Annotation
{
    private static final long    serialVersionUID = 1L;

    private Map<String, String> dataSourceProperties = null;


    /**
     * Default constructor
     */
    public DataSourceMeasurementAnnotation()
    {
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public DataSourceMeasurementAnnotation(DataSourceMeasurementAnnotation template)
    {
        super(template);

        if (template != null)
        {
            dataSourceProperties = template.getDataSourceProperties();
        }
    }


    /**
     * Return the properties of the data source.
     *
     * @return date time
     */
    public Map<String, String> getDataSourceProperties()
    {
        if (dataSourceProperties == null)
        {
            return null;
        }
        else if (dataSourceProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(dataSourceProperties);
        }
    }


    /**
     * Set up the properties of the data source.
     *
     * @param dataSourceProperties date time
     */
    public void setDataSourceProperties(Map<String, String> dataSourceProperties)
    {
        this.dataSourceProperties = dataSourceProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataSourceMeasurementAnnotation{" +
                "dataSourceProperties=" + dataSourceProperties +
                ", annotationType='" + getAnnotationType() + '\'' +
                ", summary='" + getSummary() + '\'' +
                ", confidenceLevel=" + getConfidenceLevel() +
                ", expression='" + getExpression() + '\'' +
                ", explanation='" + getExplanation() + '\'' +
                ", analysisStep='" + getAnalysisStep() + '\'' +
                ", jsonProperties='" + getJsonProperties() + '\'' +
                ", annotationStatus=" + getAnnotationStatus() +
                ", numAttachedAnnotations=" + getNumAttachedAnnotations() +
                ", reviewDate=" + getReviewDate() +
                ", steward='" + getSteward() + '\'' +
                ", reviewComment='" + getReviewComment() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DataSourceMeasurementAnnotation that = (DataSourceMeasurementAnnotation) objectToCompare;
        return Objects.equals(getDataSourceProperties(), that.getDataSourceProperties());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDataSourceProperties());
    }
}

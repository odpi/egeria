/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

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
                @JsonSubTypes.Type(value = ResourcePhysicalStatusAnnotationProperties.class, name = "DataSourcePhysicalStatusAnnotation")
        })
public class ResourceMeasureAnnotationProperties extends AnnotationProperties
{
    private Map<String, String> resourceProperties = null;


    /**
     * Default constructor
     */
    public ResourceMeasureAnnotationProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName);
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public ResourceMeasureAnnotationProperties(ResourceMeasureAnnotationProperties template)
    {
        super(template);

        if (template != null)
        {
            resourceProperties = template.getResourceProperties();
        }
    }


    /**
     * Return the properties of the data source.
     *
     * @return date time
     */
    public Map<String, String> getResourceProperties()
    {
        return resourceProperties;
    }


    /**
     * Set up the properties of the data source.
     *
     * @param resourceProperties date time
     */
    public void setResourceProperties(Map<String, String> resourceProperties)
    {
        this.resourceProperties = resourceProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ResourceMeasurementAnnotation{" +
                "resourceProperties=" + resourceProperties +
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
        ResourceMeasureAnnotationProperties that = (ResourceMeasureAnnotationProperties) objectToCompare;
        return Objects.equals(getResourceProperties(), that.getResourceProperties());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getResourceProperties());
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SampleDataProperties links an asset containing sample data to an element it described
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SampleDataProperties extends RelationshipBeanProperties
{
    private String samplingMethod = null;



    /**
     * Default constructor
     */
    public SampleDataProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SAMPLE_DATA_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template to copy.
     */
    public SampleDataProperties(SampleDataProperties template)
    {
        super(template);

        if (template != null)
        {
            samplingMethod = template.getSamplingMethod();
        }
    }


    /**
     * Return the name of the method used to sample the full data collection.
     *
     * @return string
     */
    public String getSamplingMethod() { return samplingMethod; }


    /**
     * Set up the name of the method used to sample the full data collection.
     *
     * @param samplingMethod string
     */
    public void setSamplingMethod(String samplingMethod)
    {
        this.samplingMethod = samplingMethod;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SampleDataProperties{" +
                "samplingMethod='" + samplingMethod + '\'' +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        SampleDataProperties that = (SampleDataProperties) objectToCompare;
        return Objects.equals(samplingMethod, that.samplingMethod);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), samplingMethod);
    }
}
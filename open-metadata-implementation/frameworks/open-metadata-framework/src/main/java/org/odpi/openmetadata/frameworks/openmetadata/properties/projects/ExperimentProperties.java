/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.projects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExperimentProperties is used to provide the properties for an Experiment classification.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExperimentProperties extends ClassificationBeanProperties
{
    private String hypothesis = null;


    /**
     * Default constructor
     */
    public ExperimentProperties()
    {
        super();
        super.typeName = OpenMetadataType.EXPERIMENT_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor for an editing glossary classification.
     *
     * @param template template object to copy.
     */
    public ExperimentProperties(ExperimentProperties template)
    {
        super(template);

        if (template != null)
        {
            hypothesis = template.getHypothesis();
        }
    }


    /**
     * Return the description (typically and overview of the revision) of the glossary.
     *
     * @return string description
     */
    public String getHypothesis()
    {
        return hypothesis;
    }


    /**
     * Set up the description (typically and overview of the revision) of the glossary.
     *
     * @param hypothesis string description
     */
    public void setHypothesis(String hypothesis)
    {
        this.hypothesis = hypothesis;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ScopingCollectionProperties{" +
                "description='" + hypothesis + '\'' +
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
        if (! (objectToCompare instanceof ExperimentProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(hypothesis, that.hypothesis);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), hypothesis);
    }
}

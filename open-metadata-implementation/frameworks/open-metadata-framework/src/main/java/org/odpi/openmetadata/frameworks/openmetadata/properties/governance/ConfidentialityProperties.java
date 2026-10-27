/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Defines the properties for a confidentiality classification.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConfidentialityProperties extends GovernedDataClassificationBase
{
    private Integer confidentialityLevel = null;

    /**
     * Default constructor
     */
    public ConfidentialityProperties()
    {
        super();
        super.typeName = OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ConfidentialityProperties(ConfidentialityProperties template)
    {
        super(template);

        if (template != null)
        {
            confidentialityLevel = template.getConfidentialityLevel();
        }
    }


    /**
     * Return the level assigned to this element for this classification.
     *
     * @return int
     */
    public Integer getConfidentialityLevel()
    {
        return confidentialityLevel;
    }


    /**
     * Set up the level assigned to this element for this classification.
     *
     * @param confidentialityLevel int
     */
    public void setConfidentialityLevel(Integer confidentialityLevel)
    {
        this.confidentialityLevel = confidentialityLevel;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ConfidentialityProperties{" +
                "confidentialityLevel=" + confidentialityLevel +
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
        ConfidentialityProperties that = (ConfidentialityProperties) objectToCompare;
        return Objects.equals(confidentialityLevel, that.confidentialityLevel);
    }


    /**
     * Return code value representing the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), confidentialityLevel);
    }
}

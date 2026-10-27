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
 * Defines the properties for an impact classification.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ImpactProperties extends GovernedDataClassificationBase
{
    private Integer severityLevel = null;


    /**
     * Default constructor
     */
    public ImpactProperties()
    {
        super();
        super.typeName = OpenMetadataType.IMPACT_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ImpactProperties(ImpactProperties template)
    {
        super(template);

        if (template != null)
        {
            severityLevel = template.getSeverityLevel();
        }
    }


    /**
     * Return the level assigned to this element for this classification.
     *
     * @return int
     */
    public Integer getSeverityLevel()
    {
        return severityLevel;
    }


    /**
     * Set up the level assigned to this element for this classification.
     *
     * @param severityLevel int
     */
    public void setSeverityLevel(Integer severityLevel)
    {
        this.severityLevel = severityLevel;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ImpactProperties{" +
                "severityLevel=" + severityLevel +
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
        ImpactProperties that = (ImpactProperties) objectToCompare;
        return Objects.equals(severityLevel, that.severityLevel);
    }


    /**
     * Return code value representing the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), severityLevel);
    }
}

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
 * Defines the properties for a criticality classification.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CriticalityProperties extends GovernedDataClassificationBase
{
    private Integer criticalityLevel = null;

    /**
     * Default constructor
     */
    public CriticalityProperties()
    {
        super();
        super.typeName = OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public CriticalityProperties(CriticalityProperties template)
    {
        super(template);

        if (template != null)
        {
            criticalityLevel = template.getCriticalityLevel();
        }
    }


    /**
     * Return the level assigned to this element for this classification.
     *
     * @return int
     */
    public Integer getCriticalityLevel()
    {
        return criticalityLevel;
    }


    /**
     * Set up the level assigned to this element for this classification.
     *
     * @param criticalityLevel int
     */
    public void setCriticalityLevel(Integer criticalityLevel)
    {
        this.criticalityLevel = criticalityLevel;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "CriticalityProperties{" +
                "criticalityLevel=" + criticalityLevel +
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
        CriticalityProperties that = (CriticalityProperties) objectToCompare;
        return Objects.equals(criticalityLevel, that.criticalityLevel);
    }


    /**
     * Return code value representing the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), criticalityLevel);
    }
}

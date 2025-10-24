/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceActionProcessStepProperties provides a structure for carrying the properties for a governance action process step.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionProcessStepProperties extends GovernanceActionTypeProperties
{
    private boolean ignoreMultipleTriggers = false;

    /**
     * Default constructor
     */
    public GovernanceActionProcessStepProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionProcessStepProperties(GovernanceActionProcessStepProperties template)
    {
        super (template);

        if (template != null)
        {
            ignoreMultipleTriggers = template.getIgnoreMultipleTriggers();
        }
    }



    /**
     * Return whether this action type can be triggered more than once in a single step of the governance action process.
     *
     * @return boolean flag
     */
    public boolean getIgnoreMultipleTriggers()
    {
        return ignoreMultipleTriggers;
    }


    /**
     * Set up whether this action type can be triggered more than once in a single step of the governance action process.
     *
     * @param ignoreMultipleTriggers boolean flag
     */
    public void setIgnoreMultipleTriggers(boolean ignoreMultipleTriggers)
    {
        this.ignoreMultipleTriggers = ignoreMultipleTriggers;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "GovernanceActionProcessStepProperties{" +
                "ignoreMultipleTriggers=" + ignoreMultipleTriggers +
                "} " + super.toString();
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        GovernanceActionProcessStepProperties that = (GovernanceActionProcessStepProperties) objectToCompare;
        return ignoreMultipleTriggers == that.ignoreMultipleTriggers ;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), ignoreMultipleTriggers);
    }
}

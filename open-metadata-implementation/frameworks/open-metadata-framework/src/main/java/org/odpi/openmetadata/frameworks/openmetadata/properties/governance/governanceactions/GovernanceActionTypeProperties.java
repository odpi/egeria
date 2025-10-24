/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceActionTypeProperties provides a structure for carrying the properties for a governance action type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = GovernanceActionProcessStepProperties.class, name = "GovernanceActionProcessStepProperties"),
        })
public class GovernanceActionTypeProperties extends GovernanceActionProperties
{
    private int waitTime = 0;


    /**
     * Default constructor
     */
    public GovernanceActionTypeProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionTypeProperties(GovernanceActionTypeProperties template)
    {
        super (template);

        if (template != null)
        {
            waitTime         = template.getWaitTime();
        }
    }


    /**
     * Return the minimum number of minutes to wait before starting the next governance action.
     *
     * @return int (minutes)
     */
    public int getWaitTime()
    {
        return waitTime;
    }


    /**
     * Set up the minimum number of minutes to wait before starting the next governance action.
     *
     * @param waitTime int (minutes)
     */
    public void setWaitTime(int waitTime)
    {
        this.waitTime = waitTime;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "GovernanceActionTypeProperties{" +
                "waitTime=" + waitTime +
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
        GovernanceActionTypeProperties that = (GovernanceActionTypeProperties) objectToCompare;
        return waitTime == that.waitTime;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), waitTime);
    }
}

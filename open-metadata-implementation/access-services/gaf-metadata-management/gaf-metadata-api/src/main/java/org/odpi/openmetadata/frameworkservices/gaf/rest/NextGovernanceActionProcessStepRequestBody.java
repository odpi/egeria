/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NextGovernanceActionProcessStepRequestBody contains the properties for a next action type relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NextGovernanceActionProcessStepRequestBody
{
    private String  guard                  = null;
    private boolean mandatoryGuard         = false;

    /**
     * Default constructor
     */
    public NextGovernanceActionProcessStepRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NextGovernanceActionProcessStepRequestBody(NextGovernanceActionProcessStepRequestBody template)
    {
        if (template != null)
        {
            guard                  = template.getGuard();
            mandatoryGuard         = template.getMandatoryGuard();
        }
    }


    /**
     * Return the triggering guard (or null for any guard).
     *
     * @return string name
     */
    public String getGuard()
    {
        return guard;
    }


    /**
     * Set up the triggering guard (or null for any guard).
     *
     * @param guard string name
     */
    public void setGuard(String guard)
    {
        this.guard = guard;
    }


    /**
     * Return if the guard must be returned from the previous action for any of the next actions to progress.
     *
     * @return boolean flag
     */
    public boolean getMandatoryGuard()
    {
        return mandatoryGuard;
    }


    /**
     * Set up if the guard must be returned from the previous action for any of the next actions to progress.
     *
     * @param mandatoryGuard boolean flag
     */
    public void setMandatoryGuard(boolean mandatoryGuard)
    {
        this.mandatoryGuard = mandatoryGuard;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "NextGovernanceActionProcessStepRequestBody{" +
                "guard='" + guard + '\'' +
                ", mandatoryGuard=" + mandatoryGuard +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        NextGovernanceActionProcessStepRequestBody that = (NextGovernanceActionProcessStepRequestBody) objectToCompare;
        return mandatoryGuard == that.mandatoryGuard && Objects.equals(guard, that.guard);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guard, mandatoryGuard);
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NextGovernanceActionProcessStepElement contains the properties and header for a governance action process step entity plus the
 * properties of a NextGovernanceActionProcessStep relationship retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NextGovernanceActionProcessStepElement
{
    private GovernanceActionProcessStepElement nextProcessStep         = null;
    private String                             nextProcessStepLinkGUID = null;
    private String                             guard                   = null;
    private boolean                            mandatoryGuard          = false;



    /**
     * Default constructor
     */
    public NextGovernanceActionProcessStepElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NextGovernanceActionProcessStepElement(NextGovernanceActionProcessStepElement template)
    {
        if (template != null)
        {
            nextProcessStep         = template.getNextProcessStep();
            nextProcessStepLinkGUID = template.getNextProcessStepLinkGUID();
            guard                   = template.getGuard();
            mandatoryGuard          = template.getMandatoryGuard();
        }
    }


    /**
     * Return details of the next governance action process step
     *
     * @return governance action process step properties
     */
    public GovernanceActionProcessStepElement getNextProcessStep()
    {
        return nextProcessStep;
    }


    /**
     * Set up details of the next governance action process step
     *
     * @param nextProcessStep governance action process step properties
     */
    public void setNextProcessStep(GovernanceActionProcessStepElement nextProcessStep)
    {
        this.nextProcessStep = nextProcessStep;
    }


    /**
     * Return the unique identifier of the relationship.
     *
     * @return string guid
     */
    public String getNextProcessStepLinkGUID()
    {
        return nextProcessStepLinkGUID;
    }


    /**
     * Set up the unique identifier of the relationship.
     *
     * @param nextProcessStepLinkGUID string guid
     */
    public void setNextProcessStepLinkGUID(String nextProcessStepLinkGUID)
    {
        this.nextProcessStepLinkGUID = nextProcessStepLinkGUID;
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
        return "NextGovernanceActionProcessStepElement{" +
                       "nextProcessStep=" + nextProcessStep +
                       ", nextProcessStepLinkGUID='" + nextProcessStepLinkGUID + '\'' +
                       ", guard='" + guard + '\'' +
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
        NextGovernanceActionProcessStepElement that = (NextGovernanceActionProcessStepElement) objectToCompare;
        return mandatoryGuard == that.mandatoryGuard &&
                       Objects.equals(nextProcessStep, that.nextProcessStep) &&
                       Objects.equals(nextProcessStepLinkGUID, that.nextProcessStepLinkGUID) &&
                       Objects.equals(guard, that.guard);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(nextProcessStep, nextProcessStepLinkGUID, guard, mandatoryGuard);
    }
}

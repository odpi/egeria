/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NextGovernanceActionTypeElement contains the properties and header for a governance action type entity plus the
 * properties of a NextGovernanceActionType relationship retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NextGovernanceActionTypeElement implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private GovernanceActionTypeElement nextActionType         = null;
    private String                      nextActionLinkGUID     = null;
    private String                      guard                  = null;
    private boolean                     mandatoryGuard         = false;



    /**
     * Default constructor
     */
    public NextGovernanceActionTypeElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NextGovernanceActionTypeElement(NextGovernanceActionTypeElement template)
    {
        if (template != null)
        {
            nextActionType         = template.getNextActionType();
            nextActionLinkGUID     = template.getNextActionLinkGUID();
            guard                  = template.getGuard();
            mandatoryGuard         = template.getMandatoryGuard();
        }
    }


    /**
     * Return details of the next governance action type
     *
     * @return governance action type properties
     */
    public GovernanceActionTypeElement getNextActionType()
    {
        return nextActionType;
    }


    /**
     * Set up details of the next governance action type
     *
     * @param nextActionType governance action type properties
     */
    public void setNextActionType(GovernanceActionTypeElement nextActionType)
    {
        this.nextActionType = nextActionType;
    }


    /**
     * Return the unique identifier of the relationship.
     *
     * @return string guid
     */
    public String getNextActionLinkGUID()
    {
        return nextActionLinkGUID;
    }


    /**
     * Set up the unique identifier of the relationship.
     *
     * @param nextActionLinkGUID string guid
     */
    public void setNextActionLinkGUID(String nextActionLinkGUID)
    {
        this.nextActionLinkGUID = nextActionLinkGUID;
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
        return "NextGovernanceActionTypeElement{" +
                       "nextActionType=" + nextActionType +
                       ", nextActionLinkGUID='" + nextActionLinkGUID + '\'' +
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
        NextGovernanceActionTypeElement that = (NextGovernanceActionTypeElement) objectToCompare;
        return mandatoryGuard == that.mandatoryGuard &&
                       Objects.equals(nextActionType, that.nextActionType) &&
                       Objects.equals(nextActionLinkGUID, that.nextActionLinkGUID) &&
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
        return Objects.hash(nextActionType, nextActionLinkGUID, guard, mandatoryGuard);
    }
}

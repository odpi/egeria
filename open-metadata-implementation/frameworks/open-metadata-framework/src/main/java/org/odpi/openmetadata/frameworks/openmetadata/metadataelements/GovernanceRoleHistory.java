/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceRoleHistory is the bean used to return a governance officer role definition and its appointees over time.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceRoleHistory extends GovernanceRoleAppointee
{
    private List<GovernanceAppointee> predecessors = null;
    private List<GovernanceAppointee> successors   = null;

    /**
     * Default constructor
     */
    public GovernanceRoleHistory()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceRoleHistory(GovernanceRoleHistory template)
    {
        super(template);

        if (template != null)
        {
            predecessors = template.getPredecessors();
            successors = template.getSuccessors();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceRoleHistory(GovernanceRoleElement template)
    {
        super(template);
    }


    /**
     * Return the list of predecessors to this appointment.
     *
     * @return list of individuals who used to have this role
     */
    public List<GovernanceAppointee> getPredecessors()
    {
        return predecessors;
    }


    /**
     * Set up the list of predecessors to this appointment.
     *
     * @param predecessors list of individuals who used to have this role
     */
    public void setPredecessors(List<GovernanceAppointee> predecessors)
    {
        this.predecessors = predecessors;
    }


    /**
     * Return the list of successors lined up to take over this appointment.
     *
     * @return list of individuals who will have this role in the future
     */
    public List<GovernanceAppointee> getSuccessors()
    {
        return successors;
    }


    /**
     * Set up the list of successors lined up to take over this appointment.
     *
     * @param successors list of individuals who will have this role in the future
     */
    public void setSuccessors(List<GovernanceAppointee> successors)
    {
        this.successors = successors;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceRoleHistory{" +
                       "currentAppointees=" + getCurrentAppointees() +
                       ", predecessors=" + predecessors +
                       ", successors=" + successors +
                       ", elementHeader=" + getElementHeader() +
                       ", role=" + getRole() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceRoleHistory that = (GovernanceRoleHistory) objectToCompare;
        return Objects.equals(predecessors, that.predecessors) &&
                       Objects.equals(successors, that.successors);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), predecessors, successors);
    }
}

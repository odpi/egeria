/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalservice.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PersonRoleHistory is the bean used to return a role definition and its appointees over time.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonRoleHistory extends PersonRoleAppointee
{
    private static final long serialVersionUID = 1L;

    private List<Appointee> predecessors = null;
    private List<Appointee> successors   = null;

    /**
     * Default constructor
     */
    public PersonRoleHistory()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonRoleHistory(PersonRoleHistory template)
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
    public PersonRoleHistory(PersonRoleElement template)
    {
        super(template);
    }


    /**
     * Return the list of predecessors to this appointment.
     *
     * @return list of individuals who used to have this role
     */
    public List<Appointee> getPredecessors()
    {
        return predecessors;
    }


    /**
     * Set up the list of predecessors to this appointment.
     *
     * @param predecessors list of individuals who used to have this role
     */
    public void setPredecessors(List<Appointee> predecessors)
    {
        this.predecessors = predecessors;
    }


    /**
     * Return the list of successors lined up to take over this appointment.
     *
     * @return list of individuals who will have this role in the future
     */
    public List<Appointee> getSuccessors()
    {
        return successors;
    }


    /**
     * Set up the list of successors lined up to take over this appointment.
     *
     * @param successors list of individuals who will have this role in the future
     */
    public void setSuccessors(List<Appointee> successors)
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
        return "PersonRoleHistory{" +
                       "currentAppointees=" + getCurrentAppointees() +
                       ", elementHeader=" + getElementHeader() +
                       ", properties=" + getProperties() +
                       ", predecessors=" + predecessors +
                       ", successors=" + successors +
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
        PersonRoleHistory that = (PersonRoleHistory) objectToCompare;
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

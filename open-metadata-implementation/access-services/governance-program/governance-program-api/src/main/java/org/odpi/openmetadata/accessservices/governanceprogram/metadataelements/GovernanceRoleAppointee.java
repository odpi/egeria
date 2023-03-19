/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.metadataelements;

import com.fasterxml.jackson.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceRoleAppointee is the bean used to return a governance role and current appointee(s).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
                      @JsonSubTypes.Type(value = GovernanceRoleHistory.class, name = "GovernanceRoleHistory")
              })
public class GovernanceRoleAppointee extends GovernanceRoleElement
{
    private static final long serialVersionUID = 1L;

    private List<GovernanceAppointee> currentAppointees = null;


    /**
     * Default constructor
     */
    public GovernanceRoleAppointee()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceRoleAppointee(GovernanceRoleAppointee template)
    {
        super(template);

        if (template != null)
        {
            currentAppointees = template.getCurrentAppointees();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceRoleAppointee(GovernanceRoleElement template)
    {
        super(template);
    }


    /**
     * Return information about the person appointed to the governance role.
     *
     * @return list of GovernanceAppointee objects
     */
    public List<GovernanceAppointee> getCurrentAppointees()
    {
        if (currentAppointees == null)
        {
            return null;
        }
        else if (currentAppointees.isEmpty())
        {
            return null;
        }

        return currentAppointees;
    }


    /**
     * Set up the information about the person appointed to the governance role.
     *
     * @param currentAppointees  list of GovernanceAppointee objects
     */
    public void setCurrentAppointees(List<GovernanceAppointee> currentAppointees)
    {
        this.currentAppointees = currentAppointees;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceRoleAppointee{" +
                       "elementHeader=" + getElementHeader() +
                       ", role=" + getRole() +
                       ", currentAppointees=" + currentAppointees +
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
        GovernanceRoleAppointee that = (GovernanceRoleAppointee) objectToCompare;
        return Objects.equals(currentAppointees, that.currentAppointees);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), currentAppointees);
    }
}

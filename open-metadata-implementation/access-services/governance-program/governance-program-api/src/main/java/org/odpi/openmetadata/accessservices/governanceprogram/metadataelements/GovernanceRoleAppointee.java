/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.metadataelements;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceRoleAppointee is the bean used to return a governance role and current appointee.
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

    private GovernanceAppointee         appointee     = null;


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
            appointee = template.getAppointee();
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
     * @return GovernanceAppointee object
     */
    public GovernanceAppointee getAppointee()
    {
        return appointee;
    }


    /**
     * Set up the information about the person appointed to the governance role.
     *
     * @param appointee  GovernanceAppointee object
     */
    public void setAppointee(GovernanceAppointee appointee)
    {
        this.appointee = appointee;
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
                       ", appointee=" + appointee +
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
        return Objects.equals(appointee, that.appointee);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), appointee);
    }
}

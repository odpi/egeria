/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;


import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PersonRole covers a role that has been defined in an organization.  One of more people
 * can be assigned to a role.  The optional headCount determines the maximum number of people that should be
 * appointed (open metadata does not enforce this level but sends a notification if the headCount
 * level is breached.) It also returns how many people are currently appointed to the role.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonRole extends PersonalRole
{
    private static final long    serialVersionUID = 1L;

    private boolean headCountLimitSet = false;
    private int     headCount         = 0;
    private int     appointmentCount  = 0;

    /**
     * Default constructor
     */
    public PersonRole()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonRole(PersonRole template)
    {
        super(template);
    }


    /**
     * Return the indicator whether the head count limit is set for a person role.
     *
     * @return boolean flag
     */
    public boolean getHeadCountLimitSet()
    {
        return headCountLimitSet;
    }


    /**
     * Set up the indicator whether the head count limit is set for a person role.
     *
     * @param headCountLimitSet boolean flag
     */
    public void setHeadCountLimitSet(boolean headCountLimitSet)
    {
        this.headCountLimitSet = headCountLimitSet;
    }


    /**
     * Return the head count limit (or zero if not set).
     *
     * @return int
     */
    public int getHeadCount()
    {
        return headCount;
    }


    /**
     * Set up the head count limit (or zero if not set).
     *
     * @param headCount int
     */
    public void setHeadCount(int headCount)
    {
        this.headCount = headCount;
    }


    /**
     * Return the count of people currently appointed to the role.
     *
     * @return int
     */
    public int getAppointmentCount()
    {
        return appointmentCount;
    }


    /**
     * Set up the count of people currently appointed to the role.
     *
     * @param appointmentCount int
     */
    public void setAppointmentCount(int appointmentCount)
    {
        this.appointmentCount = appointmentCount;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "PersonRole{" +
                "headCountLimitSet=" + headCountLimitSet +
                ", headCount=" + headCount +
                ", appointmentCount=" + appointmentCount +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", classifications=" + getClassifications() +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
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
        PersonRole that = (PersonRole) objectToCompare;
        return getHeadCountLimitSet() == that.getHeadCountLimitSet() &&
                getHeadCount() == that.getHeadCount() &&
                getAppointmentCount() == that.getAppointmentCount();
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getHeadCountLimitSet(), getHeadCount(), getAppointmentCount());
    }
}

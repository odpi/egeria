/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * GovernanceAppointee describes an individual's appointment as
 * a governance officer or to a specific governance role.  It includes their personal details along with the
 * start and end date of their appointment.  The elementHeader is from the
 * PersonRoleAppointment relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceAppointee implements Serializable, MetadataElement
{
    private static final long          serialVersionUID = 1L;

    private ElementHeader  elementHeader = null;
    private ProfileElement profile   = null;
    private Date           startDate = null;
    private Date           endDate   = null;


    /**
     * Default constructor
     */
    public GovernanceAppointee()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceAppointee(GovernanceAppointee template)
    {
        if (template != null)
        {
            this.elementHeader = template.getElementHeader();
            this.profile = template.getProfile();
            this.startDate = template.getStartDate();
            this.endDate = template.getEndDate();
        }
    }



    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the profile information for the individual.
     *
     * @return personal profile object
     */
    public ProfileElement getProfile()
    {
        if (profile == null)
        {
            return null;
        }
        else
        {
            return profile;
        }
    }


    /**
     * Set up the profile information for the individual.
     *
     * @param profile personal profile object
     */
    public void setProfile(ProfileElement profile)
    {
        this.profile = profile;
    }


    /**
     * Return the start date of the appointment.
     *
     * @return date
     */
    public Date getStartDate()
    {
        return startDate;
    }


    /**
     * Set the start date of the appointment.
     *
     * @param startDate date
     */
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }


    /**
     * Return the end date of the appointment.
     *
     * @return end date - null means open-ended appointment
     */
    public Date getEndDate()
    {
        return endDate;

    }


    /**
     * Set up the start date of the appointment.
     *
     * @param endDate date - null means open-ended appointment
     */
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "GovernanceAppointee{" +
                       "elementHeader=" + elementHeader +
                       ", profile=" + profile +
                       ", startDate=" + startDate +
                       ", endDate=" + endDate +
                       '}';
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
        GovernanceAppointee that = (GovernanceAppointee) objectToCompare;
        return  Objects.equals(getElementHeader(), that.getElementHeader()) &&
                Objects.equals(getProfile(), that.getProfile()) &&
                Objects.equals(getStartDate(), that.getStartDate()) &&
                Objects.equals(getEndDate(), that.getEndDate());
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getProfile(), getElementHeader(), getStartDate(), getEndDate());
    }
}

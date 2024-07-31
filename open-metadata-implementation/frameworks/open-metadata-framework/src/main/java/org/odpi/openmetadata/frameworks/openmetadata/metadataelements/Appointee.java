/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Appointee describes an individual's appointment to a specific role.  It includes their personal details along with the
 * start and end date of their appointment.  The elementHeader is from the PersonRoleAppointment relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Appointee implements MetadataElement
{
    private ElementHeader       elementHeader = null;
    private ActorProfileElement profile       = null;
    private Date                startDate     = null;
    private Date                endDate       = null;
    private boolean             isPublic      = false;


    /**
     * Default constructor
     */
    public Appointee()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public Appointee(Appointee template)
    {
        if (template != null)
        {
            this.elementHeader = template.getElementHeader();
            this.profile = template.getProfile();
            this.startDate = template.getStartDate();
            this.endDate = template.getEndDate();
            isPublic     = template.getIsPublic();
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
     * Return if the contents of this profile be shared with colleagues.
     *
     * @return flag
     */
    public boolean getIsPublic()
    {
        return isPublic;
    }


    /**
     * Set up if the contents of this profile be shared with colleagues.
     *
     * @param isPublic flag
     */
    public void setIsPublic(boolean isPublic)
    {
        this.isPublic = isPublic;
    }


    /**
     * Return the profile information for the individual.
     *
     * @return personal profile object
     */
    public ActorProfileElement getProfile()
    {
       return profile;
    }


    /**
     * Set up the profile information for the individual.
     *
     * @param profile personal profile object
     */
    public void setProfile(ActorProfileElement profile)
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
        return "Appointee{" +
                "elementHeader=" + elementHeader +
                ", profile=" + profile +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", isPublic=" + isPublic +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        Appointee appointee = (Appointee) objectToCompare;
        return isPublic == appointee.isPublic && Objects.equals(elementHeader, appointee.elementHeader) && Objects.equals(profile, appointee.profile) && Objects.equals(startDate, appointee.startDate) && Objects.equals(endDate, appointee.endDate);
    }

    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getProfile(), getElementHeader(), getStartDate(), getEndDate(), getIsPublic());
    }
}

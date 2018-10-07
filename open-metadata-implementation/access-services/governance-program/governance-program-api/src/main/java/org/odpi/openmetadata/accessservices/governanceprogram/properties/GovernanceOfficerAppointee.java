/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceOfficerAppointee describes an individual's appointment as
 * a governance officer.  It includes their personal details along with the
 * start and end date of their appointment.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceOfficerAppointee implements Serializable
{
    private static final long          serialVersionUID = 1L;

    private String          guid      = null;
    private String          type      = null;
    private PersonalProfile profile   = null;
    private Date            startDate = null;
    private Date            endDate   = null;


    /**
     * Default constructor
     */
    public GovernanceOfficerAppointee()
    {
    }


    /**
     * Copy/clone constructor
     */
    public GovernanceOfficerAppointee(GovernanceOfficerAppointee  template)
    {
        if (template != null)
        {
            this.guid = template.getGUID();
            this.type = template.getType();
            this.profile = template.getProfile();
            this.startDate = template.getStartDate();
            this.endDate = template.getEndDate();
        }
    }


    /**
     * Return the unique identifier for this definition.  This value is assigned by the metadata collection
     * when the governance definition is created.
     *
     * @return String guid
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Set up the unique identifier for this definition.  This value is assigned by the metadata collection
     * when the governance definition is created.
     *
     * @param guid String guid
     */
    public void setGUID(String guid)
    {
        this.guid = guid;
    }


    /**
     * Return the name of the specific type of governance definition.
     *
     * @return String type name
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the name of the specific type of the governance definition.
     *
     * @param type String type name
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Return the profile information for the individual.
     *
     * @return personal profile object
     */
    public PersonalProfile getProfile()
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
    public void setProfile(PersonalProfile profile)
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
        if (startDate == null)
        {
            return null;
        }
        else
        {
            return new Date(startDate.getTime());
        }
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
        if (endDate == null)
        {
            return null;
        }
        else
        {
            return new Date(endDate.getTime());
        }
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
        return "GovernanceOfficerAppointee{" +
                "guid='" + guid + '\'' +
                ", type='" + type + '\'' +
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
        GovernanceOfficerAppointee that = (GovernanceOfficerAppointee) objectToCompare;
        return  Objects.equals(getGUID(), that.getGUID()) &&
                Objects.equals(getType(), that.getType()) &&
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
        return Objects.hash(getGUID(), getType(), getProfile(), getStartDate(), getEndDate());
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AppointmentRequestBody provides a structure for appointing a person to a role.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AppointmentRequestBody extends GovernanceProgramOMASAPIRequestBody
{
    private String profileGUID   = null;
    private Date   effectiveDate = null;


    /**
     * Default constructor
     */
    public AppointmentRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AppointmentRequestBody(AppointmentRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.profileGUID = template.getProfileGUID();
            this.effectiveDate = template.getEffectiveDate();
        }
    }


    /**
     * Return the unique employee number for this governance officer.
     *
     * @return String identifier
     */
    public String getProfileGUID()
    {
        return profileGUID;
    }


    /**
     * Set up the unique employee number for this governance officer.
     *
     * @param guid String identifier
     */
    public void setProfileGUID(String guid)
    {
        this.profileGUID = guid;
    }


    /**
     * Return the date that this action is effective.
     *
     * @return date
     */
    public Date getEffectiveDate()
    {
        return effectiveDate;
    }


    /**
     * Set up the date that this action is effective.
     *
     * @param effectiveDate date
     */
    public void setEffectiveDate(Date effectiveDate)
    {
        this.effectiveDate = effectiveDate;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "AppointmentRequestBody{" +
                "profileGUID='" + profileGUID + '\'' +
                ", effectiveDate=" + effectiveDate +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AppointmentRequestBody that = (AppointmentRequestBody) objectToCompare;
        return Objects.equals(profileGUID, that.profileGUID) &&
                Objects.equals(effectiveDate, that.effectiveDate);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(profileGUID, effectiveDate);
    }
}

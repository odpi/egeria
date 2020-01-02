/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomain;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceOfficerValidatorRequestBody provides a request body with the identifiers used to verify that
 * the right object is being deleted.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceOfficerValidatorRequestBody extends GovernanceProgramOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    private GovernanceDomain    governanceDomain    = null;
    private String              appointmentId       = null;


    /**
     * Default constructor
     */
    public GovernanceOfficerValidatorRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceOfficerValidatorRequestBody(GovernanceOfficerValidatorRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.governanceDomain = template.getGovernanceDomain();
            this.appointmentId = template.getAppointmentId();
        }
    }


    /**
     * Return the governance domain over which this governance officer presides.
     *
     * @return governance domain enum value
     */
    public GovernanceDomain getGovernanceDomain()
    {
        return governanceDomain;
    }


    /**
     * Set up the governance domain over which this governance officer presides.
     *
     * @param governanceDomain enum
     */
    public void setGovernanceDomain(GovernanceDomain governanceDomain)
    {
        this.governanceDomain = governanceDomain;
    }


    /**
     * Return the unique identifier for this job role/appointment.
     *
     * @return unique name
     */
    public String getAppointmentId()
    {
        return appointmentId;
    }


    /**
     * Set up the unique identifier for this job role/appointment.
     *
     * @param appointmentId unique name
     */
    public void setAppointmentId(String appointmentId)
    {
        this.appointmentId = appointmentId;
    }


    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceOfficerValidatorRequestBody{" +
                "governanceDomain=" + governanceDomain +
                ", appointmentId='" + appointmentId +
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
        GovernanceOfficerValidatorRequestBody that = (GovernanceOfficerValidatorRequestBody) objectToCompare;
        return  governanceDomain == that.governanceDomain &&
                Objects.equals(appointmentId, that.appointmentId);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), governanceDomain, appointmentId);
    }
}

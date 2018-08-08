/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The GovernanceOfficer describes an individual who has (or will be) appointed to lead one of the
 * Governance Domains supported by the governance program.  Information about the
 * governance officer is stored as an ActorProfile.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceOfficer extends PersonalProfile
{
    private static final long          serialVersionUID = 1L;

    private String              appointmentContext  = null;
    private GovernanceDomain    governanceDomain    = null;


    /**
     * Default Constructor
     */
    public GovernanceOfficer()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public GovernanceOfficer(GovernanceOfficer template)
    {
        super(template);

        if (template != null)
        {
            this.appointmentContext = template.getAppointmentContext();
        }
    }


    /**
     * Return the context in which the governance officer is appointed. This may be an organizational scope,
     * location, or scope of assets.
     *
     * @return string description
     */
    public String getAppointmentContext()
    {
        return appointmentContext;
    }


    /**
     * Set up the context in which the governance officer is appointed. This may be an organizational scope,
     * location, or scope of assets.
     *
     * @param appointmentContext
     */
    public void setAppointmentContext(String appointmentContext)
    {
        this.appointmentContext = appointmentContext;
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
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "GovernanceOfficer{" +
                "appointmentContext='" + appointmentContext + '\'' +
                ", governanceDomain=" + governanceDomain +
                ", GUID='" + getGUID() + '\'' +
                ", type='" + getType() + '\'' +
                ", employeeNumber='" + getEmployeeNumber() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", knownName='" + getKnownName() + '\'' +
                ", jobTitle='" + getJobTitle() + '\'' +
                ", jobRoleDescription='" + getJobRoleDescription() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
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
        if (!(objectToCompare instanceof GovernanceOfficer))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceOfficer that = (GovernanceOfficer) objectToCompare;
        return Objects.equals(getAppointmentContext(), that.getAppointmentContext()) &&
                getGovernanceDomain() == that.getGovernanceDomain();
    }
}

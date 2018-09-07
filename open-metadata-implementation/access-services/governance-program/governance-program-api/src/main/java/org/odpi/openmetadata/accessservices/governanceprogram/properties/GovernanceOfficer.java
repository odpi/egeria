/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The GovernanceOfficer describes the leader of a governance program.  Initially, the r0le is defined and then
 * a person is assigned to the role.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceOfficer extends GovernanceReferenceableHeader
{
    private GovernanceDomain    governanceDomain    = null;

    private String                           appointmentId      = null;
    private String                           appointmentContext = null;
    private GovernanceOfficerAppointee       appointee          = null;
    private List<GovernanceOfficerAppointee> predecessors       = null;
    private List<GovernanceOfficerAppointee> successors         = null;


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
            this.governanceDomain = template.getGovernanceDomain();
            this.appointmentId = template.getAppointmentId();
            this.appointmentContext = template.getAppointmentContext();
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
     * @param appointmentContext string description
     */
    public void setAppointmentContext(String appointmentContext)
    {
        this.appointmentContext = appointmentContext;
    }


    /**
     * Return information about the person appointed to the governance officer role.
     *
     * @return GovernanceOfficerAppointee object
     */
    public GovernanceOfficerAppointee getAppointee()
    {
        return appointee;
    }


    /**
     * Set up the information about the person appointed to the governance officer role.
     *
     * @param appointee  GovernanceOfficerAppointee object
     */
    public void setAppointee(GovernanceOfficerAppointee appointee)
    {
        this.appointee = appointee;
    }


    /**
     * Return the list of predecessors to this appointment.
     *
     * @return list of individuals who used to have this role
     */
    public List<GovernanceOfficerAppointee> getPredecessors()
    {
        return predecessors;
    }


    /**
     * Set up the list of predecessors to this appointment.
     *
     * @param predecessors list of individuals who used to have this role
     */
    public void setPredecessors(List<GovernanceOfficerAppointee> predecessors)
    {
        this.predecessors = predecessors;
    }


    /**
     * Return the list of successors lined up to take over this appointment.
     *
     * @return list of individuals who will have this role in the future
     */
    public List<GovernanceOfficerAppointee> getSuccessors()
    {
        return successors;
    }


    /**
     * Set up the list of successors lined up to take over this appointment.
     *
     * @param successors list of individuals who will have this role in the future
     */
    public void setSuccessors(List<GovernanceOfficerAppointee> successors)
    {
        this.successors = successors;
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
                "governanceDomain=" + governanceDomain +
                ", appointmentId='" + appointmentId + '\'' +
                ", appointmentContext='" + appointmentContext + '\'' +
                ", appointee=" + appointee +
                ", predecessors=" + predecessors +
                ", successors=" + successors +
                ", externalReferences=" + getExternalReferences() +
                ", additionalProperties=" + getAdditionalProperties() +
                ", GUID='" + getGUID() + '\'' +
                ", type='" + getType() + '\'' +
                ", title='" + getTitle() + '\'' +
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
        GovernanceOfficer that = (GovernanceOfficer) objectToCompare;
        return getGovernanceDomain() == that.getGovernanceDomain() &&
                Objects.equals(getAppointmentId(), that.getAppointmentId()) &&
                Objects.equals(getAppointmentContext(), that.getAppointmentContext()) &&
                Objects.equals(getAppointee(), that.getAppointee()) &&
                Objects.equals(predecessors, that.predecessors) &&
                Objects.equals(successors, that.successors);
    }
}

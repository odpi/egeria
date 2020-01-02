/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.ExternalReference;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceOfficerDetailsRequestBody provides the properties used to create or update governance officer entities.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceOfficerDetailsRequestBody extends GovernanceProgramOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    private String                      title                = null;

    private GovernanceDomain            governanceDomain    = null;

    private String                      appointmentId       = null;
    private String                      appointmentContext  = null;

    private Map<String, String>         additionalProperties = null;
    private List<ExternalReference>     externalReferences = null;

    /**
     * Default constructor
     */
    public GovernanceOfficerDetailsRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceOfficerDetailsRequestBody(GovernanceOfficerDetailsRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.title = template.getTitle();
            this.governanceDomain = template.getGovernanceDomain();
            this.appointmentId = template.getAppointmentId();
            this.appointmentContext = template.getAppointmentContext();
            this.externalReferences = template.getExternalReferences();
            this.additionalProperties = template.getAdditionalProperties();
        }
    }

    /**
     * Return the title associated with this governance definition.
     *
     * @return String title
     */
    public String getTitle()
    {
        return title;
    }


    /**
     * Set up the title associated with this governance definition.
     *
     * @param title String title
     */
    public void setTitle(String title)
    {
        this.title = title;
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
     * @param appointmentContext description
     */
    public void setAppointmentContext(String appointmentContext)
    {
        this.appointmentContext = appointmentContext;
    }


    /**
     * Return the list of links to external documentation that are relevant to this governance definition.
     *
     * @return list of external references
     */
    public List<ExternalReference> getExternalReferences()
    {
        if (externalReferences == null)
        {
            return null;
        }
        else if (externalReferences.isEmpty())
        {
            return null;
        }
        else
        {
            return externalReferences;
        }
    }


    /**
     * Set up the list of links to external documentation that are relevant to this governance definition.
     *
     * @param externalReferences list of external references
     */
    public void setExternalReferences(List<ExternalReference> externalReferences)
    {
        this.externalReferences = externalReferences;
    }


    /**
     * Return the map of properties that are not explicitly provided as properties on this bean.
     *
     * @return map from string to object.
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Set up the map of properties that are not explicitly provided as properties on this bean.
     *
     * @param additionalProperties map from string to object.
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceOfficerDetailsRequestBody{" +
                "title='" + title + '\'' +
                ", governanceDomain=" + governanceDomain +
                ", appointmentId='" + appointmentId + '\'' +
                ", appointmentContext='" + appointmentContext + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", externalReferences=" + externalReferences +
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
        GovernanceOfficerDetailsRequestBody that = (GovernanceOfficerDetailsRequestBody) objectToCompare;
        return Objects.equals(title, that.title) &&
                governanceDomain == that.governanceDomain &&
                Objects.equals(appointmentId, that.appointmentId) &&
                Objects.equals(appointmentContext, that.appointmentContext) &&
                Objects.equals(additionalProperties, that.additionalProperties) &&
                Objects.equals(externalReferences, that.externalReferences);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), title, governanceDomain, appointmentId, appointmentContext,
                            additionalProperties, externalReferences);
    }
}

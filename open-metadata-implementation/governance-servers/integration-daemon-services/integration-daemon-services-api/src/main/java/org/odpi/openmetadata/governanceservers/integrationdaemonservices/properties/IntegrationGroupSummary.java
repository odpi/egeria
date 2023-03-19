/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * IntegrationGroupSummary is a summary of the properties known about a specific integration group.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IntegrationGroupSummary implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String                           integrationGroupName        = null;
    private String                           integrationGroupGUID        = null;
    private String                           integrationGroupDescription = null;
    private IntegrationGroupStatus           integrationGroupStatus      = null;
    private List<IntegrationConnectorReport> integrationConnectorReports = null;


    /**
     * Default constructor
     */
    public IntegrationGroupSummary()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IntegrationGroupSummary(IntegrationGroupSummary template)
    {
        if (template != null)
        {
            integrationGroupName        = template.getIntegrationGroupName();
            integrationGroupGUID        = template.getIntegrationGroupGUID();
            integrationGroupDescription = template.getIntegrationGroupDescription();
            integrationGroupStatus      = template.getIntegrationGroupStatus();
            integrationConnectorReports = template.getIntegrationConnectorReports();
        }
    }

    /**
     * Return the name of this integration group.
     *
     * @return string name
     */
    public String getIntegrationGroupName()
    {
        return integrationGroupName;
    }


    /**
     * Set up the name of this integration group.
     *
     * @param integrationGroupName string name
     */
    public void setIntegrationGroupName(String integrationGroupName)
    {
        this.integrationGroupName = integrationGroupName;
    }


    /**
     * Return the integration group's unique identifier.  This is only available if the
     * integration daemon has managed to retrieve its configuration from the metadata server.
     *
     * @return string identifier
     */
    public String getIntegrationGroupGUID()
    {
        return integrationGroupGUID;
    }


    /**
     * Set up the integration group's unique identifier.
     *
     * @param integrationGroupGUID string identifier
     */
    public void setIntegrationGroupGUID(String integrationGroupGUID)
    {
        this.integrationGroupGUID = integrationGroupGUID;
    }


    /**
     * Return the description of the integration group. This is only available if the
     * integration daemon has managed to retrieve its configuration from the metadata server.
     *
     * @return string description
     */
    public String getIntegrationGroupDescription()
    {
        return integrationGroupDescription;
    }

    /**
     * Set up the description of the integration group.
     *
     * @param integrationGroupDescription string description
     */
    public void setIntegrationGroupDescription(String integrationGroupDescription)
    {
        this.integrationGroupDescription = integrationGroupDescription;
    }


    /**
     * Return the status of the integration group.
     *
     * @return status enum
     */
    public IntegrationGroupStatus getIntegrationGroupStatus()
    {
        return integrationGroupStatus;
    }


    /**
     * Set up the status of the integration group.
     *
     * @param integrationGroupStatus status enum
     */
    public void setIntegrationGroupStatus(IntegrationGroupStatus integrationGroupStatus)
    {
        this.integrationGroupStatus = integrationGroupStatus;
    }


    /**
     * Return the status of the connectors running under this integration service.
     *
     * @return Connector status
     */
    public List<IntegrationConnectorReport> getIntegrationConnectorReports()
    {
        return integrationConnectorReports;
    }


    /**
     * Return the status of the connectors running under this integration service.
     *
     * @param integrationConnectorReports Connector status
     */
    public void setIntegrationConnectorReports(List<IntegrationConnectorReport> integrationConnectorReports)
    {
        this.integrationConnectorReports = integrationConnectorReports;
    }

    /**
     * JSON-style toString
     *
     * @return description of the object values
     */
    @Override
    public String toString()
    {
        return "IntegrationGroupSummary{" +
                "integrationGroupName='" + integrationGroupName + '\'' +
                ", integrationGroupGUID='" + integrationGroupGUID + '\'' +
                ", integrationGroupDescription='" + integrationGroupDescription + '\'' +
                ", integrationGroupStatus=" + integrationGroupStatus +
                ", integrationConnectorReports=" + integrationConnectorReports +
                '}';
    }


    /**
     * Compare objects
     *
     * @param objectToCompare object
     * @return boolean
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
        IntegrationGroupSummary that = (IntegrationGroupSummary) objectToCompare;
        return Objects.equals(integrationGroupName, that.integrationGroupName) &&
                       Objects.equals(integrationGroupGUID, that.integrationGroupGUID) &&
                Objects.equals(integrationGroupDescription, that.integrationGroupDescription) &&
                integrationGroupStatus == that.integrationGroupStatus &&
                Objects.equals(integrationConnectorReports, that.integrationConnectorReports);
    }


   /**
     * Simple hash for the object
     *
     * @return int
     */
   @Override
   public int hashCode()
   {
       return Objects.hash(integrationGroupName, integrationGroupGUID, integrationGroupDescription, integrationGroupStatus, integrationConnectorReports);
   }
}

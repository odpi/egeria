/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationConnectorReport;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupStatus;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMAGIntegrationGroupProperties is a summary of the properties known about a specific integration group.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMAGIntegrationGroupProperties
{
    private String                           integrationGroupName        = null;
    private String                           integrationGroupGUID        = null;
    private String                           integrationGroupDescription = null;
    private IntegrationGroupStatus           integrationGroupStatus      = null;


    /**
     * Default constructor
     */
    public OMAGIntegrationGroupProperties()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OMAGIntegrationGroupProperties(OMAGIntegrationGroupProperties template)
    {
        if (template != null)
        {
            integrationGroupName        = template.getIntegrationGroupName();
            integrationGroupGUID        = template.getIntegrationGroupGUID();
            integrationGroupDescription = template.getIntegrationGroupDescription();
            integrationGroupStatus      = template.getIntegrationGroupStatus();
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
     * JSON-style toString
     *
     * @return description of the object values
     */
    @Override
    public String toString()
    {
        return "OMAGIntegrationGroupProperties{" +
                "integrationGroupName='" + integrationGroupName + '\'' +
                ", integrationGroupGUID='" + integrationGroupGUID + '\'' +
                ", integrationGroupDescription='" + integrationGroupDescription + '\'' +
                ", integrationGroupStatus=" + integrationGroupStatus +
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
        OMAGIntegrationGroupProperties that = (OMAGIntegrationGroupProperties) objectToCompare;
        return Objects.equals(integrationGroupName, that.integrationGroupName) &&
                       Objects.equals(integrationGroupGUID, that.integrationGroupGUID) &&
                Objects.equals(integrationGroupDescription, that.integrationGroupDescription) &&
                integrationGroupStatus == that.integrationGroupStatus;
    }


   /**
     * Simple hash for the object
     *
     * @return int
     */
   @Override
   public int hashCode()
   {
       return Objects.hash(integrationGroupName, integrationGroupGUID, integrationGroupDescription, integrationGroupStatus);
   }
}

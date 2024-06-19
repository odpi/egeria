/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationConnectorReport;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationDaemonStatus;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupSummary;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMAGIntegrationDaemonProperties extends OMAGServerProperties
{
    private List<OMAGIntegrationGroupProperties> integrationGroups           = null;
    private List<IntegrationConnectorReport>     integrationConnectorReports = null;

    public OMAGIntegrationDaemonProperties()
    {
    }



    /**
     * Return the summary of each integration group running in the integration daemon.
     *
     * @return list of groups
     */
    public List<OMAGIntegrationGroupProperties> getIntegrationGroups()
    {
        if (integrationGroups == null)
        {
            return null;
        }
        else if (integrationGroups.isEmpty())
        {
            return null;
        }

        return integrationGroups;
    }


    /**
     * Set up the name of each integration group running in the integration daemon.
     *
     * @param integrationGroupNames list of groups
     */
    public void setIntegrationGroups(List<OMAGIntegrationGroupProperties> integrationGroupNames)
    {
        this.integrationGroups = integrationGroupNames;
    }


    /**
     * Return reports of connectors running in the integration daemon.
     *
     * @return list of summaries
     */
    public List<IntegrationConnectorReport> getIntegrationConnectorReports()
    {
        if (integrationConnectorReports == null)
        {
            return null;
        }
        else if (integrationConnectorReports.isEmpty())
        {
            return null;
        }

        return integrationConnectorReports;
    }


    /**
     * Set up reports of connectors running in the integration daemon.
     *
     * @param integrationConnectorReports list of connector
     */
    public void setIntegrationConnectorReports(List<IntegrationConnectorReport> integrationConnectorReports)
    {
        this.integrationConnectorReports = integrationConnectorReports;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OMAGIntegrationDaemonProperties{" +
                "integrationGroups=" + integrationGroups +
                ", integrationConnectorReports=" + integrationConnectorReports +
                "} " + super.toString();
    }

    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        OMAGIntegrationDaemonProperties that = (OMAGIntegrationDaemonProperties) objectToCompare;
        return Objects.equals(integrationGroups, that.integrationGroups) && Objects.equals(integrationConnectorReports, that.integrationConnectorReports);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), integrationGroups, integrationConnectorReports);
    }
}

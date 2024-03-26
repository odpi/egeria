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
 * IntegrationServiceSummary provides a structure to return the status of the connectors associated with an
 * integration service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IntegrationServiceSummary implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private int                              integrationServiceId          = 0;
    private String                           integrationServiceFullName    = null;
    private String                           integrationServiceURLMarker   = null;
    private String                           integrationServiceDescription = null;
    private String                           integrationServiceWiki        = null;
    private List<IntegrationConnectorReport> integrationConnectorReports   = null;


    /**
     * Default constructor for use with Jackson libraries
     */
    public IntegrationServiceSummary()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IntegrationServiceSummary(IntegrationServiceSummary template)
    {
        if (template != null)
        {
            integrationServiceId          = template.getIntegrationServiceId();
            integrationServiceFullName    = template.getIntegrationServiceFullName();
            integrationServiceURLMarker   = template.getIntegrationServiceURLMarker();
            integrationServiceDescription = template.getIntegrationServiceDescription();
            integrationServiceWiki        = template.getIntegrationServiceWiki();
            integrationConnectorReports   = template.getIntegrationConnectorReports();
        }
    }


    /**
     * Return the code number (ordinal) for this integration service.
     *
     * @return int ordinal
     */
    public int getIntegrationServiceId()
    {
        return integrationServiceId;
    }


    /**
     * Set up the code number (ordinal) for this integration service.
     *
     * @param integrationServiceId int ordinal
     */
    public void setIntegrationServiceId(int integrationServiceId)
    {
        this.integrationServiceId = integrationServiceId;
    }


    /**
     * Set up the full name of the integration service.
     *
     * @param integrationServiceFullName String name
     */
    public void setIntegrationServiceFullName(String integrationServiceFullName)
    {
        this.integrationServiceFullName = integrationServiceFullName;
    }



    /**
     * Return the full name of the integration service.
     *
     * @return String name
     */
    public String getIntegrationServiceFullName()
    {
        return integrationServiceFullName;
    }


    /**
     * Return the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @return String default name
     */
    public String getIntegrationServiceURLMarker()
    {
        return integrationServiceURLMarker;
    }


    /**
     * Set up the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @param integrationServiceURLMarker url fragment
     */
    public void setIntegrationServiceURLMarker(String integrationServiceURLMarker)
    {
        this.integrationServiceURLMarker = integrationServiceURLMarker;
    }



    /**
     * Return the short description of the integration service.  The default value is in English but this can be changed.
     *
     * @return String description
     */
    public String getIntegrationServiceDescription()
    {
        return integrationServiceDescription;
    }


    /**
     * Set up the short description of the integration service.
     *
     * @param integrationServiceDescription String description
     */
    public void setIntegrationServiceDescription(String integrationServiceDescription)
    {
        this.integrationServiceDescription = integrationServiceDescription;
    }


    /**
     * Return the wiki page link for the integration service.
     *
     * @return String url
     */
    public String getIntegrationServiceWiki()
    {
        return integrationServiceWiki;
    }


    /**
     * Set up the wiki page link for the integration service.
     *
     * @param integrationServiceWiki String url
     */
    public void setIntegrationServiceWiki(String integrationServiceWiki)
    {
        this.integrationServiceWiki = integrationServiceWiki;
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
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "IntegrationServiceSummary{" +
                "integrationServiceId=" + integrationServiceId +
                ", integrationServiceFullName='" + integrationServiceFullName + '\'' +
                ", integrationServiceURLMarker='" + integrationServiceURLMarker + '\'' +
                ", integrationServiceDescription='" + integrationServiceDescription + '\'' +
                ", integrationServiceWiki='" + integrationServiceWiki + '\'' +
                ", integrationConnectorReports=" + integrationConnectorReports + '\'' +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        IntegrationServiceSummary that = (IntegrationServiceSummary) objectToCompare;
        return integrationServiceId == that.integrationServiceId &&
                Objects.equals(integrationServiceFullName, that.integrationServiceFullName) &&
                Objects.equals(integrationServiceURLMarker, that.integrationServiceURLMarker) &&
                Objects.equals(integrationServiceDescription, that.integrationServiceDescription) &&
                Objects.equals(integrationServiceWiki, that.integrationServiceWiki) &&
                Objects.equals(integrationConnectorReports, that.integrationConnectorReports);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(integrationServiceId, integrationServiceFullName, integrationServiceURLMarker,
                            integrationServiceDescription, integrationServiceWiki, integrationConnectorReports);
    }
}

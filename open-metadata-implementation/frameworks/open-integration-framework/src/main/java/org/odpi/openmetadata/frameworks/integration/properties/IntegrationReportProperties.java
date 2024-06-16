/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * IntegrationReportProperties contains the properties for an integration report.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IntegrationReportProperties
{
    private String              serverName            = null;
    private String              connectorId           = null;
    private String              connectorName         = null;
    private Date                refreshStartDate      = null;
    private Date                refreshCompletionDate = null;
    private List<String>        createdElements       = null;
    private List<String>        updatedElements       = null;
    private List<String>        deletedElements       = null;
    private Map<String, String> additionalProperties  = null;


    /**
     * Default constructor
     */
    public IntegrationReportProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IntegrationReportProperties(IntegrationReportProperties template)
    {
        if (template != null)
        {
            serverName = template.getServerName();
            connectorId = template.getConnectorId();
            connectorName = template.getConnectorName();
            refreshStartDate = template.getRefreshStartDate();
            refreshCompletionDate = template.getRefreshCompletionDate();
            additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the name of the integration daemon where the integration connector was/is running.
     *
     * @return string name
     */
    public String getServerName()
    {
        return serverName;
    }


    /**
     * Set up the name of the integration daemon where the integration connector was/is running.
     *
     * @param serverName string name
     */
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }


    /**
     * Return the unique identifier for the connector deployment.  This is connectorId in the configuration document or the guid of
     * the RegisteredIntegrationConnector relationship.
     *
     * @return string id
     */
    public String getConnectorId()
    {
        return connectorId;
    }


    /**
     * Set up the unique identifier for the connector deployment.  This is connectorId in the configuration document or the guid of
     * the RegisteredIntegrationConnector relationship.
     *
     * @param connectorId string id
     */
    public void setConnectorId(String connectorId)
    {
        this.connectorId = connectorId;
    }


    /**
     * Return the name of the connector deployment.
     *
     * @return string name
     */
    public String getConnectorName()
    {
        return connectorName;
    }


    /**
     * Set up the name of the connector deployment.
     *
     * @param connectorName string name
     */
    public void setConnectorName(String connectorName)
    {
        this.connectorName = connectorName;
    }


    /**
     * Return the start date/time that this report covers.
     *
     * @return date
     */
    public Date getRefreshStartDate()
    {
        return refreshStartDate;
    }


    /**
     * Set up the start date/time that this report covers.
     *
     * @param refreshStartDate date
     */
    public void setRefreshStartDate(Date refreshStartDate)
    {
        this.refreshStartDate = refreshStartDate;
    }


    /**
     * Return the end date/time that this report covers.
     *
     * @return date
     */
    public Date getRefreshCompletionDate()
    {
        return refreshCompletionDate;
    }


    /**
     * Set up the end date/time that this report covers.
     *
     * @param refreshCompletionDate date
     */
    public void setRefreshCompletionDate(Date refreshCompletionDate)
    {
        this.refreshCompletionDate = refreshCompletionDate;
    }


    /**
     * Return the list of guids of elements that have been created.
     *
     * @return list of guids
     */
    public List<String> getCreatedElements()
    {
        return createdElements;
    }


    /**
     * Set up the list of guids of elements that have been created.
     *
     * @param createdElements list of guids
     */
    public void setCreatedElements(List<String> createdElements)
    {
        this.createdElements = createdElements;
    }

    /**
     * Return the identifiers of the elements that have been updated.
     *
     * @return list of guids
     */
    public List<String> getUpdatedElements()
    {
        return updatedElements;
    }

    /**
     * Set up the identifiers of the elements that have been updated.
     *
     * @param updatedElements list of guids
     */
    public void setUpdatedElements(List<String> updatedElements)
    {
        this.updatedElements = updatedElements;
    }


    /**
     * Return the identifiers of the elements that have been deleted.
     *
     * @return list of guids
     */
    public List<String> getDeletedElements()
    {
        return deletedElements;
    }


    /**
     * Set up the identifiers of the elements that have been deleted.
     *
     * @param deletedElements list of guids
     */
    public void setDeletedElements(List<String> deletedElements)
    {
        this.deletedElements = deletedElements;
    }


    /**
     * Return any additional properties that the connector adds to the report.
     *
     * @return name-value pairs
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up any additional properties that the connector adds to the report.
     *
     * @param additionalProperties name-value pairs
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "IntegrationReportProperties{" +
                "serverName='" + serverName + '\'' +
                ", connectorId='" + connectorId + '\'' +
                ", connectorName='" + connectorName + '\'' +
                ", refreshStartDate=" + refreshStartDate +
                ", refreshCompletionDate=" + refreshCompletionDate +
                ", createElements=" + createdElements +
                ", updateElements=" + updatedElements +
                ", deleteElements=" + deletedElements +
                ", additionalProperties=" + additionalProperties +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        IntegrationReportProperties that = (IntegrationReportProperties) objectToCompare;
        return Objects.equals(serverName, that.serverName) &&
                Objects.equals(connectorId, that.connectorId) &&
                Objects.equals(connectorName, that.connectorName) &&
                Objects.equals(refreshStartDate, that.refreshStartDate) &&
                Objects.equals(refreshCompletionDate, that.refreshCompletionDate) &&
                Objects.equals(createdElements, that.createdElements) &&
                Objects.equals(updatedElements, that.updatedElements) &&
                Objects.equals(deletedElements, that.deletedElements) &&
                Objects.equals(additionalProperties, that.additionalProperties);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(serverName, connectorId, connectorName, refreshStartDate, refreshCompletionDate, createdElements, updatedElements,
                            deletedElements, additionalProperties);
    }
}

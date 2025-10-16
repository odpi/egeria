/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ConnectorActivityReportProperties contains the properties for an integration report.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConnectorActivityReportProperties extends ReportProperties
{
    private String              serverName              = null;
    private String              connectorId             = null;
    private String              connectorName           = null;
    private Date                connectorStartDate      = null;
    private Date                refreshStartDate        = null;
    private Date                refreshCompletionDate   = null;
    private Date                connectorDisconnectDate = null;
    private List<String>        createdElements         = null;
    private List<String>        updatedElements         = null;
    private List<String>        deletedElements         = null;


    /**
     * Default constructor
     */
    public ConnectorActivityReportProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.CONNECTOR_ACTIVITY_REPORT.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConnectorActivityReportProperties(ConnectorActivityReportProperties template)
    {
        super(template);

        if (template != null)
        {
            serverName = template.getServerName();
            connectorId = template.getConnectorId();
            connectorName = template.getConnectorName();
            connectorStartDate = template.getConnectorStartDate();
            refreshStartDate = template.getRefreshStartDate();
            refreshCompletionDate = template.getRefreshCompletionDate();
            connectorDisconnectDate = template.getConnectorDisconnectDate();
            createdElements = template.getCreatedElements();
            updatedElements = template.getUpdatedElements();
            deletedElements = template.getDeletedElements();
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
     * Return the date/time that this connector started.
     *
     * @return timestamp
     */
    public Date getConnectorStartDate()
    {
        return connectorStartDate;
    }


    /**
     * Set up the date/time that the connector started.
     *
     * @param connectorStartDate timestamp
     */
    public void setConnectorStartDate(Date connectorStartDate)
    {
        this.connectorStartDate = connectorStartDate;
    }


    /**
     * Return the date/time that the connector last refreshed.
     *
     * @return timestamp
     */
    public Date getRefreshStartDate()
    {
        return refreshStartDate;
    }


    /**
     * Set up the date/time that the connector last refreshed.
     *
     * @param refreshStartDate timestamp
     */
    public void setRefreshStartDate(Date refreshStartDate)
    {
        this.refreshStartDate = refreshStartDate;
    }


    /**
     * Return the date/time that the connector last completed refresh.
     *
     * @return timestamp
     */
    public Date getRefreshCompletionDate()
    {
        return refreshCompletionDate;
    }


    /**
     * Set up the date/time that the connector last complete refresh.
     *
     * @param refreshCompletionDate timestamp
     */
    public void setRefreshCompletionDate(Date refreshCompletionDate)
    {
        this.refreshCompletionDate = refreshCompletionDate;
    }


    /**
     * Return the date/time that the connector disconnected (finished running).
     *
     * @return timestamp
     */
    public Date getConnectorDisconnectDate()
    {
        return connectorDisconnectDate;
    }


    /**
     * Set up the date/time that the connector disconnected (finished running).
     *
     * @param connectorDisconnectDate timestamp
     */
    public void setConnectorDisconnectDate(Date connectorDisconnectDate)
    {
        this.connectorDisconnectDate = connectorDisconnectDate;
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ConnectorActivityReportProperties{" +
                "serverName='" + serverName + '\'' +
                ", connectorId='" + connectorId + '\'' +
                ", connectorName='" + connectorName + '\'' +
                ", connectorStartDate=" + connectorStartDate +
                ", refreshStartDate=" + refreshStartDate +
                ", refreshCompletionDate=" + refreshCompletionDate +
                ", connectorDisconnectDate=" + connectorDisconnectDate +
                ", createdElements=" + createdElements +
                ", updatedElements=" + updatedElements +
                ", deletedElements=" + deletedElements +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ConnectorActivityReportProperties that = (ConnectorActivityReportProperties) objectToCompare;
        return Objects.equals(serverName, that.serverName) &&
                Objects.equals(connectorId, that.connectorId) &&
                Objects.equals(connectorName, that.connectorName) &&
                Objects.equals(connectorStartDate, that.connectorStartDate) &&
                Objects.equals(refreshStartDate, that.refreshStartDate) &&
                Objects.equals(refreshCompletionDate, that.refreshCompletionDate) &&
                Objects.equals(connectorDisconnectDate, that.connectorDisconnectDate) &&
                Objects.equals(createdElements, that.createdElements) &&
                Objects.equals(updatedElements, that.updatedElements) &&
                Objects.equals(deletedElements, that.deletedElements);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), serverName, connectorId, connectorName, connectorStartDate,
                            refreshStartDate, refreshCompletionDate, connectorDisconnectDate,
                            createdElements, updatedElements, deletedElements);
    }
}

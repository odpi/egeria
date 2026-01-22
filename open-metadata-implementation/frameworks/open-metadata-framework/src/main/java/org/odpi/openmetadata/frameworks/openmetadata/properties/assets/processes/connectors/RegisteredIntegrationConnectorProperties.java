/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RegisteredIntegrationConnectorProperties provides a structure for carrying the properties for a RegisteredIntegrationConnector relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RegisteredIntegrationConnectorProperties extends RelationshipBeanProperties
{
    private String                   connectorName               = null;
    private String                   connectorUserId             = null;
    private String                   metadataSourceQualifiedName = null;
    private Date                     startDate                   = null;
    private long                     refreshTimeInterval         = 0L;
    private Date                     connectorShutdownDate       = null;
    private PermittedSynchronization permittedSynchronization         = null;
    private boolean                  generateConnectorActivityReports = true;


    /**
     * Default constructor does nothing.
     */
    public RegisteredIntegrationConnectorProperties()
    {
        super();
        super.typeName = OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RegisteredIntegrationConnectorProperties(RegisteredIntegrationConnectorProperties template)
    {
        super(template);

        if (template != null)
        {
            connectorName                    = template.getConnectorName();
            connectorUserId                  = template.getConnectorUserId();
            metadataSourceQualifiedName      = template.getMetadataSourceQualifiedName();
            startDate                        = template.getStartDate();
            refreshTimeInterval              = template.getRefreshTimeInterval();
            connectorShutdownDate            = template.getConnectorShutdownDate();
            permittedSynchronization         = template.getPermittedSynchronization();
            generateConnectorActivityReports = template.getGenerateConnectorActivityReports();
        }
    }


    /**
     * Return the name of the connector.  This name is used for routing refresh calls to the connector as well
     * as being used for diagnostics.  Ideally, it should be unique amongst the connectors for the integration service.
     *
     * @return String name
     */
    public String getConnectorName()
    {
        return connectorName;
    }


    /**
     * Set up the name of the connector.  This name is used for routing refresh calls to the connector as well
     * as being used for diagnostics.  Ideally it should be unique amongst the connectors for the integration service.
     *
     * @param connectorName String
     */
    public void setConnectorName(String connectorName)
    {
        this.connectorName = connectorName;
    }


    /**
     * Return the user id for the connector - if this is null, the integration daemon's userId is used
     * on requests to the Open Metadata Access Service (OMAS).
     *
     * @return string name
     */
    public String getConnectorUserId()
    {
        return connectorUserId;
    }


    /**
     * Set up the user id for the connector - if this is null, the integration daemon's userId is used
     * on requests to the Open Metadata Access Service (OMAS).
     *
     * @param connectorUserId string name
     */
    public void setConnectorUserId(String connectorUserId)
    {
        this.connectorUserId = connectorUserId;
    }


    /**
     * Return the qualified name of the metadata source for this integration connector.  This is the qualified name
     * of an appropriate software server capability stored in open metadata.  This software server capability
     * is accessed via the partner OMAS.
     *
     * @return string name
     */
    public String getMetadataSourceQualifiedName()
    {
        return metadataSourceQualifiedName;
    }


    /**
     * Set up the qualified name of the metadata source for this integration connector.  This is the qualified name
     * of an appropriate software server capability stored in open metadata.  This software server capability
     * is accessed via the partner OMAS.
     *
     * @param metadataSourceQualifiedName string name
     */
    public void setMetadataSourceQualifiedName(String metadataSourceQualifiedName)
    {
        this.metadataSourceQualifiedName = metadataSourceQualifiedName;
    }


    /**
     * Return the date/time that this connector can start.  Null means that it can start immediately.
     *
     * @return date
     */
    public Date getStartDate()
    {
        return startDate;
    }


    /**
     * Set up the date/time that this connector can start.  Null means that it can start immediately.
     *
     * @param startDate date
     */
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }


    /**
     * Return the number of minutes between each call to the connector to refresh the metadata.  Zero means that refresh
     * is only called at server start up and whenever the refresh REST API request is made to the integration daemon.
     * If the refresh time interval is greater than 0 then additional calls to refresh are added spaced out by the refresh time interval.
     *
     * @return minute count
     */
    public long getRefreshTimeInterval()
    {
        return refreshTimeInterval;
    }


    /**
     * Set up the number of minutes between each call to the connector to refresh the metadata.  Zero means that refresh
     * is only called at server start up and whenever the refresh REST API request is made to the integration daemon.
     * If the refresh time interval is greater than 0 then additional calls to refresh are added spaced out by the refresh time interval.
     *
     * @param refreshTimeInterval minute count
     */
    public void setRefreshTimeInterval(long refreshTimeInterval)
    {
        this.refreshTimeInterval = refreshTimeInterval;
    }


    /**
     * Return the date/time that the integration connector should stop running.
     *
     * @return date
     */
    public Date getConnectorShutdownDate()
    {
        return connectorShutdownDate;
    }


    /**
     * Set up the date/time that the integration connector should stop running.
     *
     * @param connectorShutdownDate date
     */
    public void setConnectorShutdownDate(Date connectorShutdownDate)
    {
        this.connectorShutdownDate = connectorShutdownDate;
    }


    /**
     * Return the permitted direction of metadata flow (see the enum definition for more details).  Any attempt
     * by the connector to send/receive metadata in a direction that is not permitted results in a UserNotAuthorizedException.
     *
     * @return enum
     */
    public PermittedSynchronization getPermittedSynchronization()
    {
        return permittedSynchronization;
    }


    /**
     * Set up the permitted direction of metadata flow (see the enum definition for more details).  Any attempt
     * by the connector to send/receive metadata in a direction that is not permitted results in a UserNotAuthorizedException.
     *
     * @param permittedSynchronization enum
     */
    public void setPermittedSynchronization(PermittedSynchronization permittedSynchronization)
    {
        this.permittedSynchronization = permittedSynchronization;
    }


    /**
     * Return a flag indicating whether the integration connector should create an integration report.
     *
     * @return boolean flag (default = true)
     */
    public boolean getGenerateConnectorActivityReports()
    {
        return generateConnectorActivityReports;
    }


    /**
     * Set up a flag indicating whether the integration connector should create an integration report.
     *
     * @param generateConnectorActivityReports boolean flag
     */
    public void setGenerateConnectorActivityReports(boolean generateConnectorActivityReports)
    {
        this.generateConnectorActivityReports = generateConnectorActivityReports;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "RegisteredIntegrationConnectorProperties{" +
                "connectorName='" + connectorName + '\'' +
                ", connectorUserId='" + connectorUserId + '\'' +
                ", metadataSourceQualifiedName='" + metadataSourceQualifiedName + '\'' +
                ", startDate=" + startDate +
                ", refreshTimeInterval=" + refreshTimeInterval +
                ", connectorShutdownDate=" + connectorShutdownDate +
                ", permittedSynchronization=" + permittedSynchronization +
                ", generateIntegrationReports=" + generateConnectorActivityReports +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        RegisteredIntegrationConnectorProperties that = (RegisteredIntegrationConnectorProperties) objectToCompare;
        return refreshTimeInterval == that.refreshTimeInterval &&
                generateConnectorActivityReports == that.generateConnectorActivityReports &&
                Objects.equals(connectorName, that.connectorName) &&
                Objects.equals(connectorUserId, that.connectorUserId) &&
                Objects.equals(metadataSourceQualifiedName, that.metadataSourceQualifiedName) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(connectorShutdownDate, that.connectorShutdownDate) &&
                permittedSynchronization == that.permittedSynchronization;
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), connectorName, connectorUserId, metadataSourceQualifiedName, startDate, refreshTimeInterval, connectorShutdownDate, permittedSynchronization, generateConnectorActivityReports);
    }
}

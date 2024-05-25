/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.integration.contextmanager.PermittedSynchronization;

import java.io.Serial;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * IntegrationConnectorConfig provides the configuration properties used to create and manage an
 * integration connector that exchanges metadata with a third party technology.
 *
 * The integration connectors can:
 * <ul>
 *     <li>Listen on a blocking call for the third party technology to send a notification.</li>
 *     <li>Register with an external notification service that sends notifications on its own thread.</li>
 *     <li>Register a listener with the OMAS client to act on notifications from the OMAS's Out Topic.</li>
 *     <li>Poll the third party technology each time that the refresh() method is called.</li>
 * </ul>
 *
 * The configuration properties defines the connector to create and how it should be operated.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IntegrationConnectorConfig extends AdminServicesConfigHeader
{
    private String                   connectorId                 = UUID.randomUUID().toString();
    private String                   connectorName               = null;
    private String                   connectorUserId             = null;
    private Connection               connection                  = null;
    private String                   metadataSourceQualifiedName = null;
    private long                     refreshTimeInterval         = 0L;
    private boolean                  usesBlockingCalls           = false;
    private PermittedSynchronization permittedSynchronization    = null;
    private boolean                  generateIntegrationReports  = false;


    /**
     * Default constructor does nothing.
     */
    public IntegrationConnectorConfig()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IntegrationConnectorConfig(IntegrationConnectorConfig template)
    {
        super(template);

        if (template != null)
        {
            connectorId                 = template.getConnectorId();
            connectorName               = template.getConnectorName();
            connectorUserId             = template.getConnectorUserId();
            connection                  = template.getConnection();
            metadataSourceQualifiedName = template.getMetadataSourceQualifiedName();
            refreshTimeInterval         = template.getRefreshTimeInterval();
            usesBlockingCalls           = template.getUsesBlockingCalls();
            permittedSynchronization    = template.getPermittedSynchronization();
            generateIntegrationReports  = template.getGenerateIntegrationReports();
        }
    }


    /**
     * Return the unique identifier of this connector.
     *
     * @return string guid
     */
    public String getConnectorId()
    {
        return connectorId;
    }


    /**
     * Set up the unique identifier of this connector.
     *
     * @param connectorId string guid
     */
    public void setConnectorId(String connectorId)
    {
        this.connectorId = connectorId;
    }


    /**
     * Return the name of the connector.  This name is used for routing refresh calls to the connector as well
     * as being used for diagnostics.  Ideally it should be unique amongst the connectors for the integration service.
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
     * Set up the connection for the integration connector.
     *
     * @return Connection object
     */
    public Connection getConnection()
    {
        return connection;
    }


    /**
     * Set up the connection for the integration connector.
     *
     * @param connection Connection object
     */
    public void setConnection(Connection connection)
    {
        this.connection = connection;
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
     * Return if the connector should be started in its own thread to allow it to block on a listening call.
     *
     * @return boolean flag
     */
    public boolean getUsesBlockingCalls()
    {
        return usesBlockingCalls;
    }


    /**
     * Set up if the connector should be started in its own thread to allow it to block on a listening call.
     *
     * @param usesBlockingCalls boolean flag
     */
    public void setUsesBlockingCalls(boolean usesBlockingCalls)
    {
        this.usesBlockingCalls = usesBlockingCalls;
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
     * @return boolean flag (default = false)
     */
    public boolean getGenerateIntegrationReports()
    {
        return generateIntegrationReports;
    }


    /**
     * Set up a flag indicating whether the integration connector should create an integration report.
     *
     * @param generateIntegrationReports boolean flag
     */
    public void setGenerateIntegrationReports(boolean generateIntegrationReports)
    {
        this.generateIntegrationReports = generateIntegrationReports;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "IntegrationConnectorConfig{" +
                       "connectorId='" + connectorId + '\'' +
                       ", connectorName='" + connectorName + '\'' +
                       ", connectorUserId='" + connectorUserId + '\'' +
                       ", connection=" + connection +
                       ", metadataSourceQualifiedName='" + metadataSourceQualifiedName + '\'' +
                       ", refreshTimeInterval=" + refreshTimeInterval +
                       ", usesBlockingCalls=" + usesBlockingCalls +
                       ", permittedSynchronization=" + permittedSynchronization +
                       ", generateIntegrationReports=" + generateIntegrationReports +
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
        IntegrationConnectorConfig that = (IntegrationConnectorConfig) objectToCompare;
        return getRefreshTimeInterval() == that.getRefreshTimeInterval() &&
                       getUsesBlockingCalls() == that.getUsesBlockingCalls() &&
                       getGenerateIntegrationReports() == that.getGenerateIntegrationReports() &&
                       Objects.equals(getConnectorId(), that.getConnectorId()) &&
                       Objects.equals(getConnectorName(), that.getConnectorName()) &&
                       Objects.equals(getConnectorUserId(), that.getConnectorUserId()) &&
                       Objects.equals(getConnection(), that.getConnection()) &&
                       Objects.equals(getMetadataSourceQualifiedName(), that.getMetadataSourceQualifiedName()) &&
                       getPermittedSynchronization() == that.getPermittedSynchronization();
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(connectorId, connectorName, connectorUserId, connection, metadataSourceQualifiedName, refreshTimeInterval,
                            usesBlockingCalls, permittedSynchronization, generateIntegrationReports);
    }
}

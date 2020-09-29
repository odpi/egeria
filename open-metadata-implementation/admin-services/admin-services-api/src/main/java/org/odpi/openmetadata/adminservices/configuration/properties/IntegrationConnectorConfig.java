/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * IntegrationConnectorConfig provides the configuration properties used to create and manage a
 * connector that exchanges metadata with a third party technology.
 *
 * The connectors can:
 * <ul>
 *     <li>Listen on a blocking call for the third party technology to send a notification.</li>
 *     <li>Register with a notification service that sends notifications on its own thread.</li>
 *     <li>Register a listener with the OMAS client to act on notifications from the OMAS's Out Topic.</li>
 *     <li>Poll the third party technology each time that the refresh() method is called</li>
 * </ul>
 *
 * The configuration properties defines the connector to create and how it should be operated.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IntegrationConnectorConfig extends AdminServicesConfigHeader
{
    private static final long    serialVersionUID = 1L;

    private String     connectorName               = null;
    private Connection connection                  = null;
    private String     metadataSourceQualifiedName = null;
    private long       refreshTimeInterval         = 0L;
    private boolean    usesBlockingCalls           = false;


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
            connectorName               = template.getConnectorName();
            connection                  = template.getConnection();
            metadataSourceQualifiedName = template.getMetadataSourceQualifiedName();
            refreshTimeInterval         = template.getRefreshTimeInterval();
            usesBlockingCalls           = template.isUsesBlockingCalls();
        }
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
     * Return if the connector should be started in its own thread to allow it is block on a listening call.
     *
     * @return boolean flag
     */
    public boolean isUsesBlockingCalls()
    {
        return usesBlockingCalls;
    }


    /**
     * Set up if the connector should be started in its own thread to allow it is block on a listening call.
     *
     * @param usesBlockingCalls boolean flag
     */
    public void setUsesBlockingCalls(boolean usesBlockingCalls)
    {
        this.usesBlockingCalls = usesBlockingCalls;
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
                "connectorName='" + connectorName + '\'' +
                ", connection=" + connection +
                ", metadataSourceQualifiedName='" + metadataSourceQualifiedName + '\'' +
                ", refreshTimeInterval=" + refreshTimeInterval +
                ", usesBlockingCalls=" + usesBlockingCalls +
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
        return refreshTimeInterval == that.refreshTimeInterval &&
                usesBlockingCalls == that.usesBlockingCalls &&
                Objects.equals(metadataSourceQualifiedName, that.metadataSourceQualifiedName) &&
                Objects.equals(connectorName, that.connectorName) &&
                Objects.equals(connection, that.connection);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(connectorName, connection, metadataSourceQualifiedName, refreshTimeInterval, usesBlockingCalls);
    }
}

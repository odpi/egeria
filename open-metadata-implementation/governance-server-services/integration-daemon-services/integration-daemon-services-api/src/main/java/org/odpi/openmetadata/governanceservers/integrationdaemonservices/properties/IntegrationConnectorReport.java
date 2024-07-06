/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * IntegrationConnectorReport provides information on the operation of a single connector within an integration service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IntegrationConnectorReport
{
    private String                     connectorId              = null;
    private String                     connectorGUID            = null;
    private String                     connectorName            = null;
    private Connection                 connection               = null;
    private String                     connectorInstanceId      = null;
    private IntegrationConnectorStatus connectorStatus          = null;
    private Date                       lastStatusChange         = null;
    private Date                       lastRefreshTime          = null;
    private long                       minMinutesBetweenRefresh = 0L;
    private String                     failingExceptionMessage  = null;
    private Map<String, Object>        statistics               = null;


    /**
     * Default constructor does nothing.
     */
    public IntegrationConnectorReport()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IntegrationConnectorReport(IntegrationConnectorReport template)
    {
        if (template != null)
        {
            connectorId              = template.getConnectorId();
            connectorGUID            = template.getConnectorGUID();
            connectorName            = template.getConnectorName();
            connection               = template.getConnection();
            connectorInstanceId      = template.getConnectorInstanceId();
            connectorStatus          = template.getConnectorStatus();
            lastStatusChange         = template.getLastStatusChange();
            lastRefreshTime          = template.getLastRefreshTime();
            minMinutesBetweenRefresh = template.getMinMinutesBetweenRefresh();
            failingExceptionMessage  = template.getFailingExceptionMessage();
            statistics               = template.getStatistics();
        }
    }


    /**
     * Return the unique identifier of the connector - set up in the configuration.
     *
     * @return string guid
     */
    public String getConnectorId()
    {
        return connectorId;
    }


    /**
     * Set up the unique identifier of the connector - set up in the configuration.
     *
     * @param connectorId string guid
     */
    public void setConnectorId(String connectorId)
    {
        this.connectorId = connectorId;
    }

    /**
     * Return the unique identifier of the connector entity - set up in the metadata store.
     *
     * @return string guid
     */
    public String getConnectorGUID()
    {
        return connectorGUID;
    }


    /**
     * Set up the unique identifier of the connector entity - set up in the metadata store.
     *
     * @param connectorGUID string guid
     */
    public void setConnectorGUID(String connectorGUID)
    {
        this.connectorGUID = connectorGUID;
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
     * Return the connection used to create the instance of the connector.
     *
     * @return connection object
     */
    public Connection getConnection()
    {
        return connection;
    }


    /**
     * Set up the connection object used to create the instance of the connector.
     *
     * @param connection connection object
     */
    public void setConnection(Connection connection)
    {
        this.connection = connection;
    }


    /**
     * Return the unique identifier of the connector instance.
     *
     * @return string guid
     */
    public String getConnectorInstanceId()
    {
        return connectorInstanceId;
    }


    /**
     * Set up the unique identifier of the connector instance.
     *
     * @param connectorInstanceId string guid
     */
    public void setConnectorInstanceId(String connectorInstanceId)
    {
        this.connectorInstanceId = connectorInstanceId;
    }



    /**
     * Set up the status for the integration connector.
     *
     * @return status object
     */
    public IntegrationConnectorStatus getConnectorStatus()
    {
        return connectorStatus;
    }


    /**
     * Set up the status for the integration connector.
     *
     * @param connectorStatus status object
     */
    public void setConnectorStatus(IntegrationConnectorStatus connectorStatus)
    {
        this.connectorStatus = connectorStatus;
    }


    /**
     * Return the date/time when the status was last changed.
     *
     * @return timestamp
     */
    public Date getLastStatusChange()
    {
        return lastStatusChange;
    }


    /**
     * Set up the date/time when the status was last changed.
     *
     * @param lastStatusChange timestamp
     */
    public void setLastStatusChange(Date lastStatusChange)
    {
        this.lastStatusChange = lastStatusChange;
    }


    /**
     * Return the date/time when the connector was last refreshed.  Null means it has never been refreshed.
     *
     * @return timestamp
     */
    public Date getLastRefreshTime()
    {
        return lastRefreshTime;
    }


    /**
     * Set up the date/time when the connector was last refreshed.  Null means it has never been refreshed.
     *
     * @param lastRefreshTime timestamp
     */
    public void setLastRefreshTime(Date lastRefreshTime)
    {
        this.lastRefreshTime = lastRefreshTime;
    }


    /**
     * Return the configured minimum time between calls to refresh.  This gives an indication of when the
     * next refresh is due.  Null means refresh is only called at server start up and in response to an API request.
     *
     * @return count
     */
    public long getMinMinutesBetweenRefresh()
    {
        return minMinutesBetweenRefresh;
    }


    /**
     * Set up the configured minimum time between calls to refresh.  This gives an indication of when the
     * next refresh is due.  Null means refresh is only called at server start up and in response to an API request.

     * @param minMinutesBetweenRefresh count
     */
    public void setMinMinutesBetweenRefresh(long minMinutesBetweenRefresh)
    {
        this.minMinutesBetweenRefresh = minMinutesBetweenRefresh;
    }


    /**
     * Return the message extracted from an exception returned by the connector.  This is only set if the connectorStatus
     * is FAILED.  The full exception is logged in the server's audit log.
     *
     * @return string message
     */
    public String getFailingExceptionMessage()
    {
        return failingExceptionMessage;
    }


    /**
     * Set up the message extracted from an exception returned by the connector.  This is only set if the connectorStatus
     * is FAILED.  The full exception is logged in the server's audit log.
     *
     * @param failingExceptionMessage string message
     */
    public void setFailingExceptionMessage(String failingExceptionMessage)
    {
        this.failingExceptionMessage = failingExceptionMessage;
    }


    /**
     * Return the statistics logged by the connector through the context.
     *
     * @return name value pairs for the statistics
     */
    public Map<String, Object> getStatistics()
    {
        return statistics;
    }


    /**
     * Set up if the connector should be started in its own thread to allow it to block on a listening call.
     *
     * @param statistics boolean flag
     */
    public void setStatistics(Map<String, Object> statistics)
    {
        this.statistics = statistics;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "IntegrationConnectorReport{" +
                       "connectorId='" + connectorId + '\'' +
                       ", connectorGUID='" + connectorGUID + '\'' +
                       ", connectorName='" + connectorName + '\'' +
                       ", connection=" + connection +
                       ", connectorInstanceId='" + connectorInstanceId + '\'' +
                       ", connectorStatus=" + connectorStatus +
                       ", lastStatusChange=" + lastStatusChange +
                       ", lastRefreshTime=" + lastRefreshTime +
                       ", minMinutesBetweenRefresh=" + minMinutesBetweenRefresh +
                       ", failingExceptionMessage='" + failingExceptionMessage + '\'' +
                       ", statistics=" + statistics +
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
        IntegrationConnectorReport that = (IntegrationConnectorReport) objectToCompare;
        return minMinutesBetweenRefresh == that.minMinutesBetweenRefresh &&
                       Objects.equals(connectorId, that.connectorId) &&
                       Objects.equals(connectorGUID, that.connectorGUID) &&
                       Objects.equals(connectorName, that.connectorName) &&
                       Objects.equals(connection, that.connection) &&
                       Objects.equals(connectorInstanceId, that.connectorInstanceId) &&
                       connectorStatus == that.connectorStatus &&
                       Objects.equals(lastStatusChange, that.lastStatusChange) &&
                       Objects.equals(lastRefreshTime, that.lastRefreshTime) &&
                       Objects.equals(failingExceptionMessage, that.failingExceptionMessage) &&
                       Objects.equals(statistics, that.statistics);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(connectorId, connectorGUID, connectorName, connection, connectorInstanceId, connectorStatus, lastStatusChange,
                            lastRefreshTime, minMinutesBetweenRefresh, failingExceptionMessage, statistics);
    }
}

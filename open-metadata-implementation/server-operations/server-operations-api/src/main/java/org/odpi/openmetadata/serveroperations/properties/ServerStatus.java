/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serveroperations.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ServerStatus documents the start and end of a server instance.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ServerStatus
{
    private String                          serverName      = null;
    private String                          serverType      = null;
    private boolean                         isActive        = true;
    private Date                            serverStartTime = null;
    private Date                            serverEndTime   = null;
    private List<OMAGServerInstanceHistory> serverHistory   = null;

    /**
     * Default constructor for Jackson
     */
    public ServerStatus()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ServerStatus(ServerStatus template)
    {

        if (template != null)
        {
            serverName      = template.getServerName();
            serverType      = template.getServerType();
            isActive        = template.isActive;
            serverStartTime = template.getServerStartTime();
            serverEndTime   = template.getServerEndTime();
            serverHistory   = template.getServerHistory();
        }
    }


    /**
     * Return the server name
     *
     * @return String
     */
    public String getServerName()
    {
        return serverName;
    }


    /**
     * Set the name of the server
     *
     * @param serverName the name of the server
     */
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }


    /**
     * Return the type of server that is hosting these services.
     *
     * @return string name
     */
    public String getServerType()
    {
        return serverType;
    }


    /**
     * Set up the type of server that is hosting these services.
     *
     * @param serverType string name
     */
    public void setServerType(String serverType)
    {
        this.serverType = serverType;
    }


    /**
     * Return an indication of whether the server is active
     *
     * @return boolean
     */
    public boolean getIsActive()
    {
        return isActive;
    }


    /**
     * Set the indication of whether the server is active
     *
     * @param isActive indicating whether the server is active
     */
    public void setIsActive(boolean isActive)
    {
        this.isActive = isActive;
    }


    /**
     * Return the time that this instance of the server started.
     *
     * @return date/time object
     */
    public Date getServerStartTime()
    {
        return serverStartTime;
    }


    /**
     * Set up the time that this instance of the server started.
     *
     * @param serverStartTime date/time object
     */
    public void setServerStartTime(Date serverStartTime)
    {
        this.serverStartTime = serverStartTime;
    }


    /**
     * Return the time when this instance of the server ended.
     *
     * @return date/time object
     */
    public Date getServerEndTime()
    {
        return serverEndTime;
    }


    /**
     * Set up the time when this instance of the server ended.
     *
     * @param serverEndTime date/time object
     */
    public void setServerEndTime(Date serverEndTime)
    {
        this.serverEndTime = serverEndTime;
    }


    /**
     * Return the list of start and stop times for the previous restarts of the server.
     *
     * @return server history
     */
    public List<OMAGServerInstanceHistory> getServerHistory()
    {
        if (serverHistory == null)
        {
            return null;
        }
        else if (serverHistory.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(serverHistory);
        }
    }


    /**
     * Set the history of server start and stop times
     *
     * @param serverHistory date/time object
     */
    public void setServerHistory(List<OMAGServerInstanceHistory>  serverHistory)
    {
        this.serverHistory = serverHistory;
    }


    /**
     * JSON like toString method
     *
     * @return string representing the local variables
     */
    @Override
    public String toString()
    {
        return "ServerStatus{" +
                       "serverName='" + serverName + '\'' +
                       ", serverType='" + serverType + '\'' +
                       ", isActive=" + isActive +
                       ", serverStartTime=" + serverStartTime +
                       ", serverEndTime=" + serverEndTime +
                       ", serverHistory=" + serverHistory +
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
        ServerStatus that = (ServerStatus) objectToCompare;
        return getIsActive() == that.getIsActive() &&
                       Objects.equals(getServerName(), that.getServerName()) &&
                       Objects.equals(getServerType(), that.getServerType()) &&
                       Objects.equals(getServerStartTime(), that.getServerStartTime()) &&
                       Objects.equals(getServerEndTime(), that.getServerEndTime()) &&
                       Objects.equals(getServerHistory(), that.getServerHistory());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getServerName(), getServerType(), getIsActive(), getServerStartTime(), getServerEndTime(), getServerHistory());
    }
}

/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMAGServerInstanceHistory documents the start and end of a server instance.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ServerStatus implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private String                          serverName      = null;
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
            isActive        = template.isActive;
            serverStartTime = template.getServerStartTime();
            serverEndTime   = template.getServerEndTime();
            serverHistory   = template.getServerHistory();
        }
    }


    /**
     * Constructor used to create the history.
     *
     * @param serverName the name of the server
     * @param isActive whether the server is active
     * @param serverStartTime the time the server last started
     * @param serverEndTime the time the server last stopped
     * @param serverHistory the history of start and end times for the server
     */
    public ServerStatus(String serverName, boolean isActive, Date serverStartTime, Date serverEndTime, List<OMAGServerInstanceHistory> serverHistory)
    {
        this.serverName      = serverName;
        this.isActive        = isActive;
        this.serverStartTime = serverStartTime;
        this.serverEndTime   = serverEndTime;
        this.serverHistory   = serverHistory;
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
                "serverName=" + serverName +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ServerStatus that = (ServerStatus) objectToCompare;
        return getIsActive() == that.getIsActive() &&
                Objects.equals(getServerName(), that.getServerName()) &&
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
        return Objects.hash(super.hashCode(), getServerName(), getIsActive(), getServerStartTime(), getServerEndTime(), getServerHistory());
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.platformservices.properties.OMAGServerInstanceHistory;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ServerStatusResponse returns the status of a server running in a platform.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ServerStatusResponse extends FFDCResponseBase
{
    private static final long    serialVersionUID = 1L;

    private String                          serverName      = null;
    private String                          serverType      = null;
    private boolean                         isActive        = true;
    private Date                            serverStartTime = null;
    private Date                            serverEndTime   = null;
    private List<OMAGServerInstanceHistory> serverHistory   = null;


    /**
     * Default constructor
     */
    public ServerStatusResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ServerStatusResponse(ServerStatusResponse template)
    {
        super(template);

        if (template != null)
        {
            serverName = template.getServerName();
            isActive = template.isActive();
            serverStartTime = template.getServerStartTime();
            serverEndTime = template.getServerEndTime();
            serverHistory = template.getServerHistory();
        }
    }

    /**
     * Return the name of the server where the services are running.
     *
     * @return name of server
     */
    public String getServerName()
    {
        return serverName;
    }


    /**
     * Set up the name of the server where the services are running.
     *
     * @param serverName name of server
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
     * Is the server instance currently running?
     *
     * @return boolean
     */
    public boolean isActive()
    {
        return isActive;
    }


    /**
     * Set up whether the server is currently running.
     *
     * @param active boolean
     */
    public void setActive(boolean active)
    {
        isActive = active;
    }

    /**
     * Return the time that the server last started.
     *
     * @return date/time
     */
    public Date getServerStartTime()
    {
        return serverStartTime;
    }


    /**
     * Set up the time that the server last started.
     *
     * @param serverStartTime date/time
     */
    public void setServerStartTime(Date serverStartTime)
    {
        this.serverStartTime = serverStartTime;
    }


    /**
     * Return the time that the server last ended - it is null if the server is currently active.
     *
     * @return date/time or null
     */
    public Date getServerEndTime()
    {
        return serverEndTime;
    }


    /**
     * Set up the time that the server last ended - it is null if the server is currently active.
     *
     * @param serverEndTime date/time
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
     * Set up the list of start and stop times for the previous restarts of the server.
     *
     * @param serverHistory server history
     */
    public void setServerHistory(List<OMAGServerInstanceHistory> serverHistory)
    {
        this.serverHistory = serverHistory;
    }



    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ServerStatusResponse{" +
                "serverName='" + serverName + '\'' +
                ", isActive=" + isActive +
                ", serverStartTime=" + serverStartTime +
                ", serverEndTime=" + serverEndTime +
                ", serverHistory=" + serverHistory +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
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
        ServerStatusResponse that = (ServerStatusResponse) objectToCompare;
        return isActive() == that.isActive() &&
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
        return Objects.hash(super.hashCode(), getServerName(), isActive(), getServerStartTime(), getServerEndTime(), getServerHistory());
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ServerServicesListResponse returns the list of services active in a particular server running in a platform.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ServerServicesListResponse extends FFDCResponseBase
{
    private String       serverName         = null;
    private List<String> serverServicesList = null;


    /**
     * Default constructor
     */
    public ServerServicesListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ServerServicesListResponse(ServerServicesListResponse template)
    {
        super(template);

        if (template != null)
        {
            serverServicesList = template.getServerServicesList();
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
     * Return the list of servers (or null if none running).
     *
     * @return string list
     */
    public List<String> getServerServicesList()
    {
        if (serverServicesList == null)
        {
            return null;
        }
        else if (serverServicesList.isEmpty())
        {
            return null;
        }
        else
        {
            return serverServicesList;
        }
    }


    /**
     * Set up the list of servers.
     *
     * @param serverServicesList string list
     */
    public void setServerServicesList(List<String> serverServicesList)
    {
        this.serverServicesList = serverServicesList;
    }


    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ServerServicesListResponse{" +
                "serverName='" + serverName + '\'' +
                ", serverServicesList=" + serverServicesList +
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
        ServerServicesListResponse that = (ServerServicesListResponse) objectToCompare;
        return Objects.equals(getServerName(), that.getServerName()) &&
                Objects.equals(getServerServicesList(), that.getServerServicesList());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getServerName(), getServerServicesList());
    }
}

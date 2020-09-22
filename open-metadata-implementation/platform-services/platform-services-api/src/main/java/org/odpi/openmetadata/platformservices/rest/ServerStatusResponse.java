/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.platformservices.properties.OMAGServerInstanceHistory;
import org.odpi.openmetadata.platformservices.properties.ServerStatus;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ServerListResponse returns the list of servers running in a platform.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ServerStatusResponse extends FFDCResponseBase
{
    private static final long    serialVersionUID = 1L;

    ServerStatus serverStatus = null;


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
            serverStatus = template.getServerStatus();
        }
    }


    /**
     * Return the status of the server
     *
     * @return name of server
     */
    public ServerStatus getServerStatus()
    {
        return serverStatus;
    }


    /**
     * Set the status of the server
     *
     * @param serverStatus of server
     */
    public void setServerStatus(ServerStatus serverStatus)
    {
        this.serverStatus = serverStatus;
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
                "serverStatus='" + serverStatus + '\'' +
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
        return getServerStatus() == that.getServerStatus() ;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getServerStatus());
    }
}

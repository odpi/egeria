/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ServerServicesStatus documents the status of a server and the services within it.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ServerServicesStatus implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private String                        serverName         = null;
    private String                        serverType         = null;
    private ServerActiveStatus            serverActiveStatus = ServerActiveStatus.UNKNOWN;
    private List<OMAGServerServiceStatus> services           = null;

    /**
     * Default constructor for Jackson
     */
    public ServerServicesStatus()
    {
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ServerServicesStatus(ServerServicesStatus template)
    {

        if (template != null)
        {
            serverName           = template.getServerName();
            serverType           = template.getServerType();
            serverActiveStatus = template.getServerActiveStatus();
            services             = template.getServices();
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
     * Set up the current status of the server.
     *
     * @param serverActiveStatus new status
     */
    public void setServerActiveStatus(ServerActiveStatus serverActiveStatus)
    {
        this.serverActiveStatus = serverActiveStatus;
    }

    /**
     * Return the current status of the server.
     *
     * @return server instance status enum
     */
    public ServerActiveStatus getServerActiveStatus()
    {
        return serverActiveStatus;
    }


    /**
     * Return the services and their status.
     *
     * @return services list
     */
    public List<OMAGServerServiceStatus> getServices()
    {
        return services;
    }


    /**
     * Set up the services and their status.
     *
     * @param services services list
     */
    public void setServices(List<OMAGServerServiceStatus> services)
    {
        this.services = services;
    }


    /**
     * JSON like toString method
     *
     * @return string representing the local variables
     */
    @Override
    public String toString()
    {
        return "ServerServicesStatus{" +
                       "serverName='" + serverName + '\'' +
                       ", serverType='" + serverType + '\'' +
                       ", serverActiveStatus=" + serverActiveStatus +
                       ", services=" + services +
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
        ServerServicesStatus that = (ServerServicesStatus) objectToCompare;
        return Objects.equals(serverName, that.serverName) &&
                       Objects.equals(serverType, that.serverType) &&
                       serverActiveStatus == that.serverActiveStatus &&
                       Objects.equals(services, that.services);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(serverName, serverType, serverActiveStatus, services);
    }
}

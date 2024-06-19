/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.serveroperations.properties.OMAGServerInstanceHistory;
import org.odpi.openmetadata.serveroperations.properties.OMAGServerServiceStatus;
import org.odpi.openmetadata.serveroperations.properties.ServerActiveStatus;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMAGServerProperties provides a cache to assemble details about a server.  It is initialized through a
 * series of set method calls that pass information retrieved from the OMAG Server Platform.  It extracts the
 * interesting values that are to form part of the server report.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = OMAGIntegrationDaemonProperties.class, name = "OMAGIntegrationDaemonProperties"),
                @JsonSubTypes.Type(value = OMAGEngineHostProperties.class,        name = "OMAGEngineHostProperties"),
                @JsonSubTypes.Type(value = OMAGMetadataStoreProperties.class,     name = "OMAGMetadataStoreProperties"),
        })
public class OMAGServerProperties
{
    private String                          serverName               = null;
    private String                          serverType               = null;
    private String                          description              = null;
    private String                          userId                   = null;
    private String                          serverId                 = null;
    private String                          organizationName         = null;
    private int                             maxPageSize              = 0;
    private Connection                      securityConnection       = null;
    private ServerActiveStatus              serverActiveStatus       = ServerActiveStatus.UNKNOWN;
    private Date                            lastStartTime            = null;
    private Date                            lastShutdownTime         = null;
    private List<OMAGServerInstanceHistory> serverHistory = null;
    private List<OMAGServerServiceStatus>   services      = null;
    private List<OMAGCohortProperties>      cohorts       = null;


    /**
     * Default constructor
     */
    public OMAGServerProperties()
    {
    }


    /**
     * Return the name of the server.
     *
     * @return server name
     */
    public String getServerName()
    {
        return serverName;
    }


    /**
     * Set up the name of the server.
     *
     * @param serverName server name
     */
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    public String getServerType()
    {
        return serverType;
    }

    public void setServerType(String serverType)
    {
        this.serverType = serverType;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getServerId()
    {
        return serverId;
    }

    public void setServerId(String serverId)
    {
        this.serverId = serverId;
    }

    public String getOrganizationName()
    {
        return organizationName;
    }

    public void setOrganizationName(String organizationName)
    {
        this.organizationName = organizationName;
    }

    public int getMaxPageSize()
    {
        return maxPageSize;
    }

    public void setMaxPageSize(int maxPageSize)
    {
        this.maxPageSize = maxPageSize;
    }

    public Connection getSecurityConnection()
    {
        return securityConnection;
    }

    public void setSecurityConnection(Connection securityConnection)
    {
        this.securityConnection = securityConnection;
    }

    public ServerActiveStatus getServerActiveStatus()
    {
        return serverActiveStatus;
    }

    public void setServerActiveStatus(ServerActiveStatus serverActiveStatus)
    {
        this.serverActiveStatus = serverActiveStatus;
    }

    public Date getLastStartTime()
    {
        return lastStartTime;
    }

    public void setLastStartTime(Date lastStartTime)
    {
        this.lastStartTime = lastStartTime;
    }

    public Date getLastShutdownTime()
    {
        return lastShutdownTime;
    }

    public void setLastShutdownTime(Date lastShutdownTime)
    {
        this.lastShutdownTime = lastShutdownTime;
    }

    public List<OMAGServerInstanceHistory> getServerHistory()
    {
        return serverHistory;
    }

    public void setServerHistory(List<OMAGServerInstanceHistory> serverHistory)
    {
        this.serverHistory = serverHistory;
    }

    public List<OMAGServerServiceStatus> getServices()
    {
        return services;
    }

    public void setServices(List<OMAGServerServiceStatus> services)
    {
        this.services = services;
    }

    public List<OMAGCohortProperties> getCohorts()
    {
        return cohorts;
    }

    public void setCohorts(List<OMAGCohortProperties> cohorts)
    {
        this.cohorts = cohorts;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OMAGServerProperties{" +
                "serverName='" + serverName + '\'' +
                ", serverType='" + serverType + '\'' +
                ", description='" + description + '\'' +
                ", userId='" + userId + '\'' +
                ", serverId='" + serverId + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", maxPageSize=" + maxPageSize +
                ", securityConnection=" + securityConnection +
                ", serverActiveStatus=" + serverActiveStatus +
                ", lastStartTime=" + lastStartTime +
                ", lastShutdownTime=" + lastShutdownTime +
                ", serverHistory=" + serverHistory +
                ", services=" + services +
                ", cohorts=" + cohorts +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        OMAGServerProperties that = (OMAGServerProperties) objectToCompare;
        return maxPageSize == that.maxPageSize && Objects.equals(serverName, that.serverName) && Objects.equals(serverType, that.serverType) && Objects.equals(description, that.description) && Objects.equals(userId, that.userId) && Objects.equals(serverId, that.serverId) && Objects.equals(organizationName, that.organizationName) && Objects.equals(securityConnection, that.securityConnection) && serverActiveStatus == that.serverActiveStatus && Objects.equals(lastStartTime, that.lastStartTime) && Objects.equals(lastShutdownTime, that.lastShutdownTime) && Objects.equals(serverHistory, that.serverHistory) && Objects.equals(services, that.services) && Objects.equals(cohorts, that.cohorts);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(serverName, serverType, description, userId, serverId, organizationName, maxPageSize, securityConnection, serverActiveStatus, lastStartTime, lastShutdownTime, serverHistory, services, cohorts);
    }
}

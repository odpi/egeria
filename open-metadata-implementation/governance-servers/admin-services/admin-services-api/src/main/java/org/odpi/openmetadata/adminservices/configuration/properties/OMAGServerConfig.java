/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMAGServerConfig provides the properties used to initialize an open metadata and governance (OMAG) server.
 *
 * The OMAG server configuration has the following properties:
 * <ul>
 *     <li>
 *         localServerId - Unique identifier for this server.
 *     </li>
 *     <li>
 *         localServerName - meaningful name for the server for use in messages and UIs.
 *         Ideally this value is unique to aid administrators in understanding the behavior of the local
 *         server/repository in the open metadata cluster.
 *
 *         The default value is "Default Server".
 *     </li>
 *     <li>
 *         localServerType - descriptive type name for the local server.  Again this is useful information for the
 *         administrator to understand which vendor implementation, or version of the vendor implementation, is
 *         in operation.
 *
 *         The default value is "Open Metadata and Governance Server".
 *     </li>
 *     <li>
 *         organizationName - descriptive name for the organization that owns the local server/repository.
 *         This is useful when the open metadata repository cluster consists of metadata servers from different
 *         organizations, or different departments of an enterprise.
 *
 *         The default value is null.
 *     </li>
 *     <li>
 *         localServerURL - network address of the server (typically URL and port number).
 *     </li>
 *     <li>
 *         localServerUserId - UserId to use for server initiated REST calls.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMAGServerConfig implements Serializable
{
    private static final long serialVersionUID = 1L;

    /*
     * Default values used when the server configuration does not provide a value.
     */
    private static final String  defaultLocalServerType                   = "Open Metadata and Governance Server";
    private static final String  defaultLocalOrganizationName             = null;
    private static final String  defaultLocalServerURL                    = "http://localhost:8080";
    private static final String  defaultLocalServerUserId                 = "OMAGServer";
    private static final int     defaultMaxPageSize                       = 1000;


    /*
     * Values in use by this server.
     */
    private String                    localServerId            = UUID.randomUUID().toString();
    private String                    localServerName          = null;
    private String                    localServerType          = defaultLocalServerType;
    private String                    organizationName         = defaultLocalOrganizationName;
    private String                    localServerURL           = defaultLocalServerURL;
    private String                    localServerUserId        = defaultLocalServerUserId;
    private int                       maxPageSize              = defaultMaxPageSize;
    private EventBusConfig            eventBusConfig           = null;
    private List<AccessServiceConfig> accessServicesConfig     = null;
    private RepositoryServicesConfig  repositoryServicesConfig = null;
    private VirtualiserConfig         virtualiserConfig        = null;


    /**
     * Default constructor used to set all properties to their default value.  This means the server can connect to the
     * cluster (assuming the default topic name is used by the cluster) and replicate metadata, but it will not be
     * remotely callable through an OMRS connector.
     */
    public OMAGServerConfig()
    {

    }


    /**
     * Copy/clone constructor.
     */
    public OMAGServerConfig(OMAGServerConfig  template)
    {
        localServerId = template.getLocalServerId();
        localServerName = template.getLocalServerName();
        localServerType = template.getLocalServerType();
        organizationName = template.getOrganizationName();
        localServerURL = template.getLocalServerURL();
        localServerUserId = template.getLocalServerUserId();
        maxPageSize = template.getMaxPageSize();
        eventBusConfig = template.getEventBusConfig();
        accessServicesConfig = template.getAccessServicesConfig();
        repositoryServicesConfig = template.getRepositoryServicesConfig();
        virtualiserConfig = template.getVirtualiserConfig();
    }


    /**
     * Return an unique identifier for this server.
     *
     * @return string guid
     */
    public String getLocalServerId()
    {
        return localServerId;
    }


    /**
     * Set up a unique identifier for this server.
     *
     * @param localServerId string guid
     */
    public void setLocalServerId(String localServerId)
    {
        this.localServerId = localServerId;
    }


    /**
     * Return the name of the local server.
     *
     * @return String server name
     */
    public String getLocalServerName()
    {
        return localServerName;
    }


    /**
     * Set up the name of the local server.
     *
     * @param localServerName String local server name
     */
    public void setLocalServerName(String localServerName)
    {
        this.localServerName = localServerName;
    }


    /**
     * Return the descriptive name for the server type.
     *
     * @return String server type
     */
    public String getLocalServerType()
    {
        return localServerType;
    }


    /**
     * Set up the descriptive name for the server type.
     *
     * @param localServerType String server type
     */
    public void setLocalServerType(String localServerType)
    {
        this.localServerType = localServerType;
    }


    /**
     * Return the name of the organization that is running the server.
     *
     * @return String organization name
     */
    public String getOrganizationName()
    {
        return organizationName;
    }


    /**
     * Set up the name of the organization that is running the server.
     *
     * @param organizationName String organization name
     */
    public void setOrganizationName(String organizationName)
    {
        this.organizationName = organizationName;
    }


    /**
     * Return the base URL for calling the local server.
     *
     * @return String URL
     */
    public String getLocalServerURL()
    {
        return localServerURL;
    }


    /**
     * Set up the base URL for calling the local server.
     *
     * @param localServerURL String URL
     */
    public void setLocalServerURL(String localServerURL)
    {
        this.localServerURL = localServerURL;
    }


    /**
     * Return the userId that the local server should use when processing events and there is no external user
     * driving the operation.
     *
     * @return user id
     */
    public String getLocalServerUserId()
    {
        return localServerUserId;
    }


    /**
     * Set up the userId that the local server should use when processing events and there is no external user
     * driving the operation.
     *
     * @param localServerUserId string user id
     */
    public void setLocalServerUserId(String localServerUserId)
    {
        this.localServerUserId = localServerUserId;
    }


    /**
     * Return the maximum page size supported by this server.
     *
     * @return int number of elements
     */
    public int getMaxPageSize()
    {
        return maxPageSize;
    }


    /**
     * Set up the  maximum page size supported by this server.
     *
     * @param maxPageSize int number of elements
     */
    public void setMaxPageSize(int maxPageSize)
    {
        this.maxPageSize = maxPageSize;
    }


    /**
     * Set up the information used to create connections to an event bus.
     *
     * @return EventBusConfig object
     */
    public EventBusConfig getEventBusConfig()
    {
        return eventBusConfig;
    }


    /**
     * Set up the information used to create connections to an event bus.
     *
     * @param eventBusConfig EventBusConfig object
     */
    public void setEventBusConfig(EventBusConfig eventBusConfig)
    {
        this.eventBusConfig = eventBusConfig;
    }

    /**
     * Return the configuration for the registered Open Metadata Access Services (OMAS).
     *
     * @return array of configuration properties one for each OMAS
     */
    public List<AccessServiceConfig> getAccessServicesConfig()
    {
        return accessServicesConfig;
    }


    /**
     * Set up the configuration for the registered Open Metadata Access Services (OMAS).
     *
     * @param accessServicesConfig array of configuration properties one for each OMAS
     */
    public void setAccessServicesConfig(List<AccessServiceConfig> accessServicesConfig)
    {
        this.accessServicesConfig = accessServicesConfig;
    }


    /**
     * Return the Open Metadata Repository Services (OMRS) config.
     *
     * @return configuration properties that control OMRS
     */
    public RepositoryServicesConfig getRepositoryServicesConfig()
    {
        return repositoryServicesConfig;
    }


    /**
     * Set up the Open Metadata Repository Services (OMRS) config.
     *
     * @param repositoryServicesConfig configuration properties that control OMRS
     */
    public void setRepositoryServicesConfig(RepositoryServicesConfig repositoryServicesConfig)
    {
        this.repositoryServicesConfig = repositoryServicesConfig;
    }


    /**
     * Set up the virtualisation solution connector config
     *
     * @param virtualiserConfig configuration properties that set up the connector to virualisation solutions
     */
    public void setVirtualiserConfig(VirtualiserConfig virtualiserConfig) {
        this.virtualiserConfig = virtualiserConfig;
    }


    /**
     * Return the virtualisation solution connector config
     *
     * @return configuration properties that set up the connector to virualisation solutions
     */
    public VirtualiserConfig getVirtualiserConfig() {
        return virtualiserConfig;
    }
}

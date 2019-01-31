/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
public class OMAGServerConfig extends AdminServicesConfigHeader
{
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
    private String                    localServerId             = UUID.randomUUID().toString();
    private String                    localServerName           = null;
    private String                    localServerType           = defaultLocalServerType;
    private String                    organizationName          = defaultLocalOrganizationName;
    private String                    localServerURL            = defaultLocalServerURL;
    private String                    localServerUserId         = defaultLocalServerUserId;
    private int                       maxPageSize               = defaultMaxPageSize;
    private EventBusConfig            eventBusConfig            = null;
    private List<AccessServiceConfig> accessServicesConfig      = null;
    private RepositoryServicesConfig  repositoryServicesConfig  = null;
    private DiscoveryEngineConfig     discoveryEngineConfig     = null;
    private StewardshipServicesConfig stewardshipServicesConfig = null;
    private SecuritySyncConfig        securitySyncConfig        = null;
    private List<String>              auditLog                  = null;


    /**
     * Default constructor used to set all properties to their default value.  This means the server can connect to the
     * cluster (assuming the default topic name is used by the cluster) and replicate metadata, but it will not be
     * remotely callable through an OMRS connector.
     */
    public OMAGServerConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     */
    public OMAGServerConfig(OMAGServerConfig  template)
    {
        super(template);

        if (template != null)
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
            discoveryEngineConfig = template.getDiscoveryEngineConfig();
            stewardshipServicesConfig = template.getStewardshipServicesConfig();
            securitySyncConfig = template.getSecuritySyncConfig();
            auditLog = template.getAuditLog();
        }
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
     * Return the configuration for a discovery engine.
     *
     * @return DiscoveryEngineConfig properties
     */
    public DiscoveryEngineConfig getDiscoveryEngineConfig()
    {
        return discoveryEngineConfig;
    }


    /**
     * Set up the configuration for a discovery engine.
     *
     * @param discoveryEngineConfig DiscoveryEngineConfig properties
     */
    public void setDiscoveryEngineConfig(DiscoveryEngineConfig discoveryEngineConfig)
    {
        this.discoveryEngineConfig = discoveryEngineConfig;
    }


    /**
     * Return the configuration for the stewardship services in a server.
     *
     * @return StewardshipServicesConfig properties
     */
    public StewardshipServicesConfig getStewardshipServicesConfig()
    {
        return stewardshipServicesConfig;
    }


    /**
     * Set up the configuration for the stewardship services in a server.
     *
     * @param stewardshipServicesConfig StewardshipServicesConfig properties
     */
    public void setStewardshipServicesConfig(StewardshipServicesConfig stewardshipServicesConfig)
    {
        this.stewardshipServicesConfig = stewardshipServicesConfig;
    }


    /**
     * Return the configuration for the security synchronization services.
     *
     * @return SecuritySyncConfig properties
     */
    public SecuritySyncConfig getSecuritySyncConfig()
    {
        return securitySyncConfig;
    }


    /**
     * Set up the configuration for the security synchronization services.
     *
     * @param securitySyncConfig SecuritySyncConfig properties
     */
    public void setSecuritySyncConfig(SecuritySyncConfig securitySyncConfig)
    {
        this.securitySyncConfig = securitySyncConfig;
    }


    /**
     * Return the list of audit log entries associated with this config file.
     * The audit log simply keep track of the changed to the configuration.
     *
     * @return list of audit messages
     */
    public List<String> getAuditLog()
    {
        return auditLog;
    }


    /**
     * Set up the audit log messages.
     *
     * @param auditLog list of audit messages
     */
    public void setAuditLog(List<String> auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OMAGServerConfig{" +
                "localServerId='" + localServerId + '\'' +
                ", localServerName='" + localServerName + '\'' +
                ", localServerType='" + localServerType + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", localServerURL='" + localServerURL + '\'' +
                ", localServerUserId='" + localServerUserId + '\'' +
                ", maxPageSize=" + maxPageSize +
                ", eventBusConfig=" + eventBusConfig +
                ", accessServicesConfig=" + accessServicesConfig +
                ", repositoryServicesConfig=" + repositoryServicesConfig +
                ", discoveryEngineConfig=" + discoveryEngineConfig +
                ", stewardshipServicesConfig=" + stewardshipServicesConfig +
                ", securitySyncConfig=" + securitySyncConfig +
                ", auditLog=" + auditLog +
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
        OMAGServerConfig that = (OMAGServerConfig) objectToCompare;
        return getMaxPageSize() == that.getMaxPageSize() &&
                Objects.equals(getLocalServerId(), that.getLocalServerId()) &&
                Objects.equals(getLocalServerName(), that.getLocalServerName()) &&
                Objects.equals(getLocalServerType(), that.getLocalServerType()) &&
                Objects.equals(getOrganizationName(), that.getOrganizationName()) &&
                Objects.equals(getLocalServerURL(), that.getLocalServerURL()) &&
                Objects.equals(getLocalServerUserId(), that.getLocalServerUserId()) &&
                Objects.equals(getEventBusConfig(), that.getEventBusConfig()) &&
                Objects.equals(getAccessServicesConfig(), that.getAccessServicesConfig()) &&
                Objects.equals(getRepositoryServicesConfig(), that.getRepositoryServicesConfig()) &&
                Objects.equals(getDiscoveryEngineConfig(), that.getDiscoveryEngineConfig()) &&
                Objects.equals(getStewardshipServicesConfig(), that.getStewardshipServicesConfig()) &&
                Objects.equals(getSecuritySyncConfig(), that.getSecuritySyncConfig()) &&
                Objects.equals(getAuditLog(), that.getAuditLog());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getLocalServerId(), getLocalServerName(), getLocalServerType(), getOrganizationName(),
                            getLocalServerURL(), getLocalServerUserId(), getMaxPageSize(), getEventBusConfig(),
                            getAccessServicesConfig(), getRepositoryServicesConfig(), getDiscoveryEngineConfig(),
                            getStewardshipServicesConfig(), getSecuritySyncConfig(), getAuditLog());
    }
}

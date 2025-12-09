/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMAGServerConfig provides the properties used to initialize an open metadata and governance (OMAG) server.
 * The OMAG server configuration has the following basic properties, plus configuration for the subsystems
 * that should be activated in the OMAG Server:
 * <ul>
 *     <li>
 *         localServerId - Unique identifier for this server.
 *         By default, this is initialized to a randomly generated Universal Unique identifier (UUID).
 *     </li>
 *     <li>
 *         localServerName - meaningful name for the server for use in messages and UIs.
 *         Ideally this value is unique to aid administrators in understanding the source of messages and events
 *         from the server.
 *         This value is set to the server name assigned when the configuration is created.
 *     </li>
 *     <li>
 *         localServerType - descriptive type name for the server.  Again this is useful information for the
 *         administrator to understand the role of the server.
 *         The default value is "Open Metadata and Governance Server".
 *     </li>
 *     <li>
 *         organizationName - descriptive name for the organization that owns the local server/repository.
 *         This is useful when the open metadata repository cohort consists of metadata servers from different
 *         organizations, or different departments of an enterprise.
 *         The default value is null.
 *     </li>
 *     <li>
 *         localServerURL - network address of the OMAG server platform where this server runs
 *         (typically host and port number but may also include the initial part of the URL before "open-metadata").
 *         The default value is "<a href="https://localhost:9443">...</a>".
 *     </li>
 *     <li>
 *         localServerUserId - UserId to use for server initiated REST calls.
 *         The default is "OMAGServer".
 *     </li>
 *     <li>
 *         maxPageSize - the maximum page size that can be set on requests to the server.
 *         The default value is 1000.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMAGServerConfig extends BasicServerProperties
{
    public static final String       VERSION_ONE         = "V1.0";
    public static final String       VERSION_TWO         = "V2.0";
    public static final String       VERSION_SIX         = "V6.0";
    public static final List<String> COMPATIBLE_VERSIONS = new ArrayList<>(List.of(VERSION_SIX));



    /*
     * Configuration document version number - if not in document then assume V1.0.
     */
    private String                    versionId                 = null;

    /*
     * Values in use by this server.
     */
    private String                          localServerId                   = null; // set on first server startup
    private String                          localServerName                 = null;

    private Connection                      serverSecurityConnection        = null;
    private EventBusConfig                  eventBusConfig                  = null;
    private List<AccessServiceConfig>       accessServicesConfig            = null;
    private List<IntegrationGroupConfig>    dynamicIntegrationGroupsConfig  = null;
    private List<EngineConfig>              governanceEnginesConfig         = null;
    private List<ViewServiceConfig>         viewServicesConfig              = null;
    private RepositoryServicesConfig        repositoryServicesConfig        = null;
    private ConformanceSuiteConfig          conformanceSuiteConfig          = null;
    private List<String>                    auditTrail                      = null;


    /**
     * Default constructor.
     */
    public OMAGServerConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public OMAGServerConfig(OMAGServerConfig  template)
    {
        super(template);

        if (template != null)
        {
            versionId                       = template.getVersionId();
            localServerId                   = template.getLocalServerId();

            serverSecurityConnection        = template.getServerSecurityConnection();
            eventBusConfig                  = template.getEventBusConfig();
            accessServicesConfig            = template.getAccessServicesConfig();
            dynamicIntegrationGroupsConfig = template.getDynamicIntegrationGroupsConfig();
            governanceEnginesConfig        = template.getGovernanceEnginesConfig();
            viewServicesConfig             = template.getViewServicesConfig();
            repositoryServicesConfig        = template.getRepositoryServicesConfig();
            conformanceSuiteConfig          = template.getConformanceSuiteConfig();
            auditTrail                      = template.getAuditTrail();
        }
    }


    /**
     * Return the versionId of this document.
     *
     * @return string
     */
    public String getVersionId()
    {
        return versionId;
    }


    /**
     * Set up the versionId of this document.
     *
     * @param versionId string
     */
    public void setVersionId(String versionId)
    {
        this.versionId = versionId;
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
     * Return the connection for the optional server security connector that validates calls to
     * this server from admin to operations to metadata and governance services.
     *
     * @return Connection bean.
     */
    public Connection getServerSecurityConnection()
    {
        return serverSecurityConnection;
    }


    /**
     * Set up the connection for the optional server security connector that validates calls to
     * this server from admin to operations to metadata and governance services.
     *
     * @param serverSecurityConnection connection bean
     */
    public void setServerSecurityConnection(Connection serverSecurityConnection)
    {
        this.serverSecurityConnection = serverSecurityConnection;
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
     * Return the configuration for the registered Open Metadata Access Services (OMASs).  Used in a metadata access server.
     *
     * @return list of configuration properties, one for each OMAS
     */
    public List<AccessServiceConfig> getAccessServicesConfig()
    {
        return accessServicesConfig;
    }


    /**
     * Set up the configuration for the registered Open Metadata Access Services (OMASs).  Used in a metadata access server.
     *
     * @param accessServicesConfig list of configuration properties, one for each OMAS
     */
    public void setAccessServicesConfig(List<AccessServiceConfig> accessServicesConfig)
    {
        this.accessServicesConfig = accessServicesConfig;
    }


    /**
     * Return the optional list of dynamic integration groups.  Used in an integration daemon.
     *
     * @return list of configuration properties, one for each integration group
     */
    public List<IntegrationGroupConfig> getDynamicIntegrationGroupsConfig()
    {
        return dynamicIntegrationGroupsConfig;
    }


    /**
     * Set up the optional list of dynamic integration groups.  Used in an integration daemon.
     *
     * @param dynamicIntegrationGroupsConfig list of configuration properties, one for each integration group
     */
    public void setDynamicIntegrationGroupsConfig(List<IntegrationGroupConfig> dynamicIntegrationGroupsConfig)
    {
        this.dynamicIntegrationGroupsConfig = dynamicIntegrationGroupsConfig;
    }


    /**
     * Return the configuration for the registered Open Metadata View Services (OMVSs).  Used in a view server.
     *
     * @return list of configuration properties, one for each OMVS
     */
    public List<ViewServiceConfig> getViewServicesConfig()
    {
        return viewServicesConfig;
    }


    /**
     * Set up the configuration for the registered Open Metadata View Services (OMVSs).  Used in a view server.
     *
     * @param viewServicesConfig array of configuration properties, one for each OMVS
     */
    public void setViewServicesConfig(List<ViewServiceConfig> viewServicesConfig)
    {
        this.viewServicesConfig = viewServicesConfig;
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
     * Return the configuration for the open metadata conformance suite services.  Used in a conformance test server.
     *
     * @return configuration properties that control the operation of the open metadata test labs.
     */
    public ConformanceSuiteConfig getConformanceSuiteConfig()
    {
        return conformanceSuiteConfig;
    }


    /**
     * Set up the configuration for the open metadata conformance suite services.  Used in a conformance test server.
     *
     * @param conformanceSuiteConfig configuration properties that control the operation of the open metadata test labs.
     */
    public void setConformanceSuiteConfig(ConformanceSuiteConfig conformanceSuiteConfig)
    {
        this.conformanceSuiteConfig = conformanceSuiteConfig;
    }


    /**
     * Return the configuration for the engine host services that control an engine host OMAG Server.
     *
     * @return properties for an engine host server
     */
    public List<EngineConfig> getGovernanceEnginesConfig()
    {
        return governanceEnginesConfig;
    }


    /**
     * Set up the configuration for the engine host services that control an engine host OMAG Server.
     *
     * @param governanceEnginesConfig properties for an engine host server
     */
    public void setGovernanceEnginesConfig(List<EngineConfig> governanceEnginesConfig)
    {
        this.governanceEnginesConfig = governanceEnginesConfig;
    }


    /**
     * Return the list of audit log entries associated with this config file.
     * The audit log simply keep track of the changed to the configuration.
     *
     * @return list of audit messages
     */
    public List<String> getAuditTrail()
    {
        return auditTrail;
    }


    /**
     * Set up the audit log messages.
     *
     * @param auditTrail list of audit messages
     */
    public void setAuditTrail(List<String> auditTrail)
    {
        this.auditTrail = auditTrail;
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
                "versionId='" + versionId + '\'' +
                ", localServerId='" + localServerId + '\'' +
                ", localServerName='" + localServerName + '\'' +
                ", serverSecurityConnection=" + serverSecurityConnection +
                ", eventBusConfig=" + eventBusConfig +
                ", accessServicesConfig=" + accessServicesConfig +
                ", dynamicIntegrationGroupsConfig=" + dynamicIntegrationGroupsConfig +
                ", viewServicesConfig=" + viewServicesConfig +
                ", repositoryServicesConfig=" + repositoryServicesConfig +
                ", conformanceSuiteConfig=" + conformanceSuiteConfig +
                ", engineHostServicesConfig=" + governanceEnginesConfig +
                ", auditTrail=" + auditTrail +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        OMAGServerConfig that = (OMAGServerConfig) objectToCompare;
        return Objects.equals(versionId, that.versionId) &&
                Objects.equals(localServerId, that.localServerId) &&
                Objects.equals(localServerName, that.localServerName) &&
                Objects.equals(serverSecurityConnection, that.serverSecurityConnection) &&
                Objects.equals(eventBusConfig, that.eventBusConfig) &&
                Objects.equals(accessServicesConfig, that.accessServicesConfig) &&
                Objects.equals(dynamicIntegrationGroupsConfig, that.dynamicIntegrationGroupsConfig) &&
                Objects.equals(viewServicesConfig, that.viewServicesConfig) &&
                Objects.equals(repositoryServicesConfig, that.repositoryServicesConfig) &&
                Objects.equals(conformanceSuiteConfig, that.conformanceSuiteConfig) &&
                Objects.equals(governanceEnginesConfig, that.governanceEnginesConfig) &&
                Objects.equals(auditTrail, that.auditTrail);
    }

    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), versionId, localServerId, localServerName, serverSecurityConnection,
                            eventBusConfig, accessServicesConfig, dynamicIntegrationGroupsConfig, viewServicesConfig,
                            repositoryServicesConfig, conformanceSuiteConfig, governanceEnginesConfig, auditTrail);
    }
}

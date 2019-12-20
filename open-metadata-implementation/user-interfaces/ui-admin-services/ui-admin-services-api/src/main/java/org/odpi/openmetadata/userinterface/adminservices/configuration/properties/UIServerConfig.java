/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig provides the properties used to initialize a user interface open metadata and governance (OMAG) server.
 *
 * The UI server configuration has the following basic properties, plus configuration for the subsystems
 * that should be activated in the UI Server:
 * <ul>
 *     <li>
 *         localServerId - Unique identifier for this server.
 *
 *         By default, this is initialized to a randomly generated Universal Unique identifier (UUID).
 *     </li>
 *     <li>
 *         localServerName - meaningful name for the server for use in messages and UIs.
 *         Ideally this value is unique to aid administrators in understanding the source of messages and events
 *         from the server.
 *
 *         This value is set to the server name assigned when the configuration is created.
 *     </li>
 *     <li>
 *         organizationName - descriptive name for the organization that owns the ui server/repository.
 *         This is useful when the open metadata repository cluster consists of metadata servers from different
 *         organizations, or different departments of an enterprise.
 *
 *         The default value is null.
 *     </li>
 *     <li>
 *         localServerType - descriptive type name for the UI server.  Again this is useful information for the
 *         administrator to understand the role of the server.
 *
 *         The default value is "User Interface for the Open Metadata and Governance Server".
 *     </li>
 *     <li>
 *         localServerURL - network address of the UI server platform where this server runs
 *         (typically host and port number but may also include the initial part of the URL before "open-metadata").
 *
 *         The default value is "http://localhost:8443".
 *     </li>
 *     <li>
 *         localServerUserId - UserId to use for server initiated REST calls.
 *
 *         The default is "UIServer".
 *     </li>
 *     <li>
 *         maxPageSize - the maximum page size that can be set on requests to the server.
 *
 *         The default value is 1000.
 *     </li>
 *     <li>
 *         metadataServerEndpointConfig - the endpoint used to connect to the Metadata server.
 *
 *     </li>
 *     <li>
 *         ViewServicesConfig - configuration of the view services
 *     </li>    
 *
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UIServerConfig extends UIAdminServicesConfigHeader
{
    public static final String         VERSION_ONE = "V1.0";

    /*
     * Configuration document version number - if not in document then assume V1.0.
     */
    private String                    versionId                 = null;
    /*
     * Default values used when the server configuration does not provide a value.
     */
    private static final String  defaultOrganizationName                = null;
    private static final String  defaultLocalServerUserId              = "UIServer";
    private static final int     defaultMaxPageSize                    = 1000;
    private static final String  defaultLocalServerType = "User Interface for the Open Metadata and Governance Server";

    /*
     * Values in use by this server.
     */
    private String                    serviceName = null;
    private String                    localServerId = UUID.randomUUID().toString();
    private String                    localServerName = null;
    private String                    localServerUserId = defaultLocalServerUserId;
    private String                    localServerPassword = null;
    private String                    localServerType = defaultLocalServerType;
    private String                    localServerURL = null;
    private int                       maxPageSize = defaultMaxPageSize;
    private String                    organizationName = defaultOrganizationName;
    private Connection                serverSecurityConnection = null;
    private String                    metadataServerName;
    private String                    metadataServerURL;
    private String                    authenticationSource = null;
    private String                    description = null;
    private List<Connection>          auditLogConnections            = new ArrayList<>();

    // views
    private List<ViewServiceConfig>   viewServiceConfigs = null;

    // audit
    private List<String>              auditTrail                = null;



    /**
     * Default constructor.
     */
    public UIServerConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public UIServerConfig(UIServerConfig template)
    {
        super(template);

        if (template != null)
        {
            versionId = template.getVersionId();
            serviceName = template.getServiceName();
            localServerId = template.getLocalServerId();
            localServerName = template.getLocalServerName();
            localServerURL = template.getLocalServerURL();
            localServerUserId = template.getLocalServerUserId();
            localServerPassword = template.getLocalServerPassword();
            localServerType = template.getLocalServerType();
            organizationName = template.getOrganizationName();
            maxPageSize = template.getMaxPageSize();
            serverSecurityConnection = template.getServerSecurityConnection();
            metadataServerName = template.getMetadataServerName();
            metadataServerURL = template.getMetadataServerURL();
            // View services
            viewServiceConfigs = template.getViewServiceConfigs();
            // audit
            auditLogConnections = template.getAuditLogConnections();
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

    public String getServiceName() {
        return serviceName;
    }
    /**
     * Set up the service name (the UI calls the services views).
     *
     * @param serviceName name of the service
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * Return the service name (the UI calls the services views).
     *
     * @return string name of the service
     */
    public String getLocalServerId()
    {
        return localServerId;
    }


    /**
     * Set up a unique identifier for this server.
     *
     * @param uiServerId string guid
     */
    public void setLocalServerId(String uiServerId)
    {
        this.localServerId = uiServerId;
    }


    /**
     * Return the name of the ui server.
     *
     * @return String server name
     */
    public String getLocalServerName()
    {
        return localServerName;
    }


    /**
     * Set up the name of the ui server.
     *
     * @param uiServerName String ui server name
     */
    public void setLocalServerName(String uiServerName)
    {
        this.localServerName = uiServerName;
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
     * Return the base URL for calling the ui server.
     *
     * @return String URL
     */
    public String getLocalServerURL()
    {
        return localServerURL;
    }


    /**
     * Set up the base URL for calling the ui server.
     *
     * @param uiServerURL String URL
     */
    public void setLocalServerURL(String uiServerURL)
    {
        this.localServerURL = uiServerURL;
    }


    /**
     * Return the userId that the ui server should use when processing events and there is no external user
     * driving the operation.
     *
     * @return user id
     */
    public String getLocalServerUserId()
    {
        return localServerUserId;
    }


    /**
     * Set up the userId that the ui server should use when processing events and there is no external user
     * driving the operation.
     *
     * @param uiServerUserId string user id
     */
    public void setLocalServerUserId(String uiServerUserId)
    {
        this.localServerUserId = uiServerUserId;
    }


    /**
     * Return the password that the ui server should use on outbound REST calls (this is the password for
     * the localServerUserId).  Using userIds and passwords for server authentication is not suitable
     * for production environments.
     *
     * @return password in clear
     */
    public String getLocalServerPassword()
    {
        return localServerPassword;
    }


    /**
     * Set up the password that the ui server should use on outbound REST calls (this is the password for
     * the localServerUserId).  Using userIds and passwords for server authentication is not suitable
     * for production environments.
     *
     * @param uiServerPassword password in clear
     */
    public void setLocalServerPassword(String uiServerPassword)
    {
        this.localServerPassword = uiServerPassword;
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
     * get the metadata server
     * @return metadata server name
     */
    public String getMetadataServerName() {
        return metadataServerName;
    }
    /**
     * Set up the localServerName for the metadata server.
     *
     * @param metadataServerName metadata server name
     */
    public void setMetadataServerName(String metadataServerName) {
        this.metadataServerName = metadataServerName;
    }

    /**
     * The metadata server url
     *
     * @return metadata server URL
     */
    public String getMetadataServerURL() {
        return metadataServerURL;
    }
    /**
     * Set up the connection for the optional server security connector that validates calls to
     * this server from admin to operations to metadata and governance services.
     *
     * @param metadataServerURL connection bean
     */
    public void setMetadataServerURL(String metadataServerURL) {
        this.metadataServerURL = metadataServerURL;
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
     * Return the authentication source. The valid values are
     *  <ul>
     *   <li> db - to use the h2 database </li>
     *   <li> ldap - to use LDAP</li>
     * </ul>
     *
     * Any configuration information associated with the specified authentication source should be supplied in UserStoreConfig.
     *
     * @return String the name of the authentication source
     */
    public String getAuthenticationSource() {
        return authenticationSource;
    }
    /**
     * Set up the authentication source. The valid values are
     *  <ul>
     *   <li> db - to use the h2 database </li>
     *   <li> ldap - to use LDAP</li>
     * </ul>
     *
     * Any configuration information associated with the specified authentication source should be supplied in UserStoreConfig.
     *
     * @param authenticationSource the name of the authentication source
     */
    public void setAuthenticationSource(String authenticationSource) {
        this.authenticationSource = authenticationSource;
    }

    /**
     * Server description
     * @return server description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set up server description
     * @param description String description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Return the Connection properties used to create an OCF Connector to the AuditLog.
     *
     * @return Connection object
     */
    public List<Connection> getAuditLogConnections()
    {
        if (auditLogConnections == null)
        {
            return null;
        }
        else if (auditLogConnections.isEmpty())
        {
            return null;
        }
        else
        {
            return auditLogConnections;
        }
    }

    /**
     * Set up the Connection properties used to create an OCF Connector to the AuditLog.
     *
     * @param auditLogConnections list of Connection objects
     */
    public void setAuditLogConnections(List<Connection> auditLogConnections)
    {
        this.auditLogConnections = auditLogConnections;
    }
    /**
     * Return the configuration for the registered view services.
     *
     * @return array of configuration properties one for each view service.
     */
    public List<ViewServiceConfig> getViewServiceConfigs()
    {
        return viewServiceConfigs;
    }


    /**
     * Set up the configuration for the registered view services.
     *
     * @param viewServiceConfigs array of configuration properties one for each view service
     */
    public void setViewServiceConfigs(List<ViewServiceConfig> viewServiceConfigs)
    {
        this.viewServiceConfigs = viewServiceConfigs;
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
    public String toString() {
        return "UIServerConfig{" +
                "versionId='" + versionId + '\'' +
                ", serverName='" + serviceName + '\'' +
                ", localServerId='" + localServerId + '\'' +
                ", localServerName='" + localServerName + '\'' +
                ", localServerUserId='" + localServerUserId + '\'' +
                ", localServerPassword='" + localServerPassword + '\'' +
                ", localServerType='" + localServerType + '\'' +
                ", localServerURL='" + localServerURL + '\'' +
                ", maxPageSize=" + maxPageSize +
                ", organizationName='" + organizationName + '\'' +
                ", serverSecurityConnection=" + serverSecurityConnection +
                ", authenticationSource='" + authenticationSource + '\'' +
                ", description=" + description +
                ", auditTrail=" + auditTrail +
                ", auditLogConnections=" + auditLogConnections +
                ", viewServiceConfigs" + viewServiceConfigs + '\'' +
                ", auditTrail=" + auditTrail +
                '}';
    }
/**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare) {
        if (this == objectToCompare) return true;
        if (!(objectToCompare instanceof UIServerConfig)) return false;
        UIServerConfig that = (UIServerConfig) objectToCompare;
        return getMaxPageSize() == that.getMaxPageSize() &&
                Objects.equals(getServiceName(), that.getServiceName()) &&
                Objects.equals(getLocalServerId(), that.getLocalServerId()) &&
                Objects.equals(getLocalServerName(), that.getLocalServerName()) &&
                Objects.equals(getLocalServerUserId(), that.getLocalServerUserId()) &&
                Objects.equals(getLocalServerPassword(), that.getLocalServerPassword()) &&
                Objects.equals(getLocalServerType(), that.getLocalServerType()) &&
                Objects.equals(getLocalServerURL(), that.getLocalServerURL()) &&
                Objects.equals(getOrganizationName(), that.getOrganizationName()) &&
                Objects.equals(getServerSecurityConnection(), that.getServerSecurityConnection()) &&
                Objects.equals(getMetadataServerName(), that.getMetadataServerName()) &&
                Objects.equals(getMetadataServerURL(), that.getMetadataServerURL()) &&
                Objects.equals(getAuthenticationSource(), that.getAuthenticationSource()) &&
                Objects.equals(getDescription(),this.getDescription()) &&
                Objects.equals(getAuditLogConnections(), that.getAuditLogConnections()) &&
                Objects.equals(getVersionId(), that.getVersionId()) &&
                Objects.equals(getViewServiceConfigs(), that.getViewServiceConfigs());
    }
    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getVersionId(),
                Objects.hash(getServiceName(), getLocalServerId(), getLocalServerName(), getLocalServerUserId(),
                        getLocalServerPassword(), getLocalServerType(), getLocalServerURL(), getMaxPageSize(),
                        getOrganizationName(), getServerSecurityConnection(), getMetadataServerName(),
                        getMetadataServerURL(), getAuthenticationSource(), getAuditLogConnections(), getDescription(),
                        getViewServiceConfigs(), getAuditTrail()));
    }


}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform;

import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.extractor.EgeriaExtractor;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.ffdc.OMAGConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.ffdc.OMAGConnectorErrorCode;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.properties.OMAGServerPlatformProperties;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataSecurityAccessControl;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataUserAccount;

import java.util.*;

/**
 * Connector to access an OMAG Server Platform.
 */
public class OMAGServerPlatformConnector extends ConnectorBase implements AuditLoggingComponent
{
    private AuditLog auditLog = null;
    private String   connectorName = "OMAG Server Platform Connector";

    private String   targetRootURL = null;
    private String   platformName = "Local OMAG Server Platform";
    private String   delegatingUserId = null;

    private EgeriaExtractor extractor = null;


    public OMAGServerPlatformConnector()
    {
    }


    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Return the component description that is used by this connector in the audit log.
     *
     * @return id, name, description, wiki page URL.
     */
    @Override
    public ComponentDescription getConnectorComponentDescription()
    {
        if ((this.auditLog != null) && (this.auditLog.getReport() != null))
        {
            return auditLog.getReport().getReportingComponent();
        }

        return null;
    }


    /**
     * Set up a new platform name (must be called before start()).
     *
     * @param platformName new platform name
     */
    public void setPlatformName(String platformName)
    {
        this.platformName = platformName;
    }


    /**
     * Set up the delegating userId - needed for operational calls.
     *
     * @param delegatingUserId user id
     */
    public void setDelegatingUserId(String delegatingUserId)
    {
        this.delegatingUserId = delegatingUserId;
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        if (connectionBean.getDisplayName() != null)
        {
            connectorName = connectionBean.getDisplayName();
        }

        /*
         * Retrieve the configuration
         */
        Endpoint endpoint = connectionBean.getEndpoint();

        if (endpoint != null)
        {
            targetRootURL = endpoint.getNetworkAddress();
        }

        if (targetRootURL == null)
        {
            throw new ConnectorCheckedException(OMAGConnectorErrorCode.NULL_URL.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        /*
         * Set up the extractor client.
         */
        try
        {
            extractor = new EgeriaExtractor(targetRootURL, platformName, null, delegatingUserId, secretsStoreConnectorMap, auditLog);
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName, OMAGConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                 error.getClass().getName(),
                                                                                                                 methodName,
                                                                                                                 error.getMessage()));
            }
            throw new ConnectorCheckedException(OMAGConnectorErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                 error.getClass().getName(),
                                                                                                                 methodName,
                                                                                                                 error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }

    /*
     *===========================================================================
     * Specialized methods
     */

    /**
     * Return the latest status information for the platform.
     *
     * @return platform report
     * @throws Exception a variety of exceptions from the different clients
     */
    public OMAGServerPlatformProperties getPlatformReport() throws Exception
    {
        return extractor.getPlatformReport();
    }


    /**
     * Return the connector type for the requested connector provider after validating that the
     * connector provider is available on the OMAGServerPlatform's class path.  This method is for tools that are configuring
     * connectors into an Egeria server.  It does not validate that the connector will load and initialize.
     *
     * @param connectorProviderClassName name of the connector provider class
     * @return ConnectorType bean or exceptions that occur when trying to create the connector
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */

    public ConnectorType getConnectorType(String connectorProviderClassName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return extractor.getConnectorType(connectorProviderClassName);
    }


    /**
     * Set up a new user account or update an existing one.
     * This is account is registered with the platform security connector.  The user
     * requires operator permission for the platform unless it is their own user account they are updating.
     *
     * @param userAccount details about the user to update
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */

    public void setUserAccount(OpenMetadataUserAccount userAccount) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        extractor.setUserAccount(userAccount);
    }


    /**
     * Return details of a user account registered with the platform security connector.
     *
     * @param accountUserId identifier for the user to update
     * @throws UserNotAuthorizedException the supplied user id (from bearer token) is not authorized to issue this command.
     * @throws InvalidParameterException  invalid parameter.
     * @throws PropertyServerException    unusual state in the platform.
     */
    public OpenMetadataUserAccount getUserAccount(String accountUserId) throws UserNotAuthorizedException,
                                                                               InvalidParameterException,
                                                                               PropertyServerException
    {
        return extractor.getUserAccount(accountUserId);
    }


    /**
     * Return the list of users registered with the platform security connector.
     *
     * @param status status of the user - or null for any status
     * @param type   type of user - or null for any type
     * @return list of matching userIds in the user directory
     * @throws UserNotAuthorizedException the supplied user id (from bearer token) is not authorized to issue this command.
     * @throws InvalidParameterException  invalid parameter.
     * @throws PropertyServerException    unusual state in the platform.
     */
    public List<String> getUserList(UserAccountStatus status,
                                    UserAccountType type) throws UserNotAuthorizedException,
                                                                 InvalidParameterException,
                                                                 PropertyServerException
    {
        return extractor.getUserList(status, type);
    }


    /**
     * Clear the account for a user with the platform security connector.
     *
     * @param accountUserId identifier for the user to update
     * @throws UserNotAuthorizedException the supplied user is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void deleteUserAccount(String accountUserId) throws UserNotAuthorizedException,
                                                               InvalidParameterException,
                                                               PropertyServerException
    {
        extractor.deleteUserAccount(accountUserId);
    }



    /**
     * Set up a new security access control or update an existing one.
     * This is control is registered with the platform security connector.  The user
     * requires operator permission for the platform.
     *
     * @param securityAccessControl details about the control to update
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */

    public void setSecurityAccessControl(OpenMetadataSecurityAccessControl securityAccessControl) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        extractor.setSecurityAccessControl(securityAccessControl);
    }


    /**
     * Return details of a security access control registered with the platform security connector.
     *
     * @param controlName identifier for the control to retrieve
     * @throws UserNotAuthorizedException the supplied user id (from bearer token) is not authorized to issue this command.
     * @throws InvalidParameterException  invalid parameter.
     * @throws PropertyServerException    unusual state in the platform.
     */
    public OpenMetadataSecurityAccessControl getSecurityAccessControl(String controlName) throws UserNotAuthorizedException,
                                                                                                 InvalidParameterException,
                                                                                                 PropertyServerException
    {
        return extractor.getSecurityAccessControl(controlName);
    }


    /**
     * Clear the requested security access control with the platform security connector.
     *
     * @param controlName identifier for the control to remove
     * @throws UserNotAuthorizedException the supplied user is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void deleteSecurityAccessControl(String controlName) throws UserNotAuthorizedException,
                                                                       InvalidParameterException,
                                                                       PropertyServerException
    {
        extractor.deleteSecurityAccessControl(controlName);
    }


    /**
     * Retrieve a list of the access services registered on the platform
     *
     *
     * @return List of access services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getAccessServices() throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        return extractor.getAccessServices();
    }


    /**
     * Retrieve a list of the engine services registered on the platform
     *
     * @return List of engine services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getEngineServices() throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        return extractor.getEngineServices();
    }


    /**
     * Retrieve a list of the view services registered on the platform
     *
     * @return List of view services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getViewServices() throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        return extractor.getViewServices();
    }


    /**
     * Retrieve a list of the services known on the platform
     *
     * @return List of common services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getAllServices() throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        return extractor.getAllServices();
    }


    /*
     * ========================================================================================
     * Find out about a specific OMAG Server's configuration
     */

    /**
     * Return all the OMAG Server configurations that are stored on this platform.
     *
     * @return the OMAG Server configurations that are stored on this platform
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public Set<OMAGServerConfig> getAllServerConfigurations() throws UserNotAuthorizedException,
                                                                     OMAGConfigurationErrorException,
                                                                     InvalidParameterException
    {
        return extractor.getAllServerConfigurations();
    }


    /**
     * Push the configuration for the server to another OMAG Server Platform.
     *
     * @param serverName                 local server name
     * @param destinationPlatformURLRoot location of the platform where the config is to be deployed to
     * @throws UserNotAuthorizedException      the supplied userId is not authorized to issue this command.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     * @throws InvalidParameterException   invalid serverName or destinationPlatform parameter.
     */
    public void deployOMAGServerConfig(String serverName,
                                       String destinationPlatformURLRoot) throws UserNotAuthorizedException,
                                                                                 OMAGConfigurationErrorException,
                                                                                 InvalidParameterException
    {
        extractor.deployOMAGServerConfig(serverName, destinationPlatformURLRoot);
    }


    /**
     * Return the complete set of configuration properties in use by the server.
     *
     * @param serverName local server name
     * @return OMAGServerConfig properties
     * @throws UserNotAuthorizedException      the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException   invalid serverName parameter.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public OMAGServerConfig getStoredOMAGServerConfig(String serverName) throws UserNotAuthorizedException,
                                                                                InvalidParameterException,
                                                                                OMAGConfigurationErrorException
    {
        return extractor.getStoredOMAGServerConfig(serverName);
    }


    /**
     * Return the complete set of configuration properties in use by the server.
     *
     * @param serverName local server name
     * @return OMAGServerConfig properties
     * @throws UserNotAuthorizedException      the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException   invalid serverName parameter.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public OMAGServerConfig getResolvedOMAGServerConfig(String serverName) throws UserNotAuthorizedException,
                                                                                  InvalidParameterException,
                                                                                  OMAGConfigurationErrorException
    {
        return extractor.getResolvedOMAGServerConfig(serverName);
    }



    /*
     * ========================================================================================
     * Activate and deactivate the open metadata and governance capabilities in the OMAG Server
     */

    /**
     * Activate the Open Metadata and Governance (OMAG) server using the configuration document stored for this server.
     *
     * @param serverName server to start
     * @return success message
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the server.
     */
    public String activateServer(String serverName) throws UserNotAuthorizedException,
                                                                     InvalidParameterException,
                                                                     PropertyServerException
    {
        return extractor.activateServer(serverName);
    }


    /**
     * Temporarily deactivate any open metadata and governance services.
     *
     * @param serverName server to start
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void shutdownServer(String serverName) throws UserNotAuthorizedException,
                                                         InvalidParameterException,
                                                         PropertyServerException
    {
        extractor.shutdownServer(serverName);
    }


    /**
     * Temporarily shutdown all running servers.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void shutdownAllServers() throws UserNotAuthorizedException,
                                                          InvalidParameterException,
                                                          PropertyServerException
    {
        extractor.shutdownAllServers();
    }


    /**
     * Permanently deactivate any open metadata and governance services and unregister from
     * any cohorts.
     *
     * @param serverName server to start
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */

    public void shutdownAndUnregisterServer(String serverName) throws UserNotAuthorizedException,
                                                                      InvalidParameterException,
                                                                      PropertyServerException
    {
        extractor.shutdownAndUnregisterServer(serverName);
    }


    /**
     * Shutdown any active servers and unregister them from
     * any cohorts.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void shutdownAndUnregisterAllServers() throws UserNotAuthorizedException,
                                                         InvalidParameterException,
                                                         PropertyServerException
    {
        extractor.shutdownAndUnregisterAllServers();
    }
    

}

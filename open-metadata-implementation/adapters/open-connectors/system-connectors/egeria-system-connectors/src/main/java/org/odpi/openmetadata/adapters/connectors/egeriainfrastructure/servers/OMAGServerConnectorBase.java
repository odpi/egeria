/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers;

import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.extractor.EgeriaExtractor;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.ffdc.OMAGConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.ffdc.OMAGConnectorErrorCode;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.properties.OMAGServerProperties;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.serveroperations.properties.ServerServicesStatus;
import org.odpi.openmetadata.serveroperations.properties.ServerStatus;

import java.util.List;

public abstract class OMAGServerConnectorBase extends ConnectorBase implements AuditLoggingComponent,
                                                                               VirtualConnectorExtension
{
    private AuditLog auditLog      = null;
    private String   connectorName;
    private String   clientUserId = null;
    private String   targetRootURL = null;

    protected EgeriaExtractor extractor = null;


    public OMAGServerConnectorBase(String connectorName)
    {
        this.connectorName = connectorName;
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
     * Set up new calling user.
     *
     * @param clientUserId caller's userId
     */
    public void setClientUserId(String clientUserId)
    {
        this.clientUserId = clientUserId;
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        if (connectionProperties.getConnectionName() != null)
        {
            connectorName = connectionProperties.getConnectionName();
        }

        /*
         * Retrieve the configuration
         */
        EndpointProperties endpoint = connectionProperties.getEndpoint();

        if (endpoint != null)
        {
            targetRootURL = endpoint.getAddress();
        }

        if (targetRootURL == null)
        {
            throw new ConnectorCheckedException(OMAGConnectorErrorCode.NULL_URL.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        String serverName = null;

        if ((connectionProperties.getConfigurationProperties() != null) &&
                (connectionProperties.getConfigurationProperties().get("serverName") != null))
        {
            serverName = connectionProperties.getConfigurationProperties().get("serverName").toString();
        }

        if (serverName == null)
        {
            throw new ConnectorCheckedException(OMAGConnectorErrorCode.NULL_SERVER_NAME.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        /*
         * Set up the extractor client.
         */
        try
        {
            if (clientUserId != null)
            {
                extractor = new EgeriaExtractor(targetRootURL,
                                                null,
                                                serverName,
                                                clientUserId);
            }
            else
            {
                extractor = new EgeriaExtractor(targetRootURL,
                                                null,
                                                serverName,
                                                connectionProperties.getUserId());
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName, OMAGConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                   error.getClass().getName(),
                                                                                                                   methodName,
                                                                                                                   error.getMessage()), error);
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
     * Return the latest status information about a particular service.
     *
     * @return server report
     * @throws Exception a variety of exceptions from the different clients
     */
    public OMAGServerProperties getServerReport() throws Exception
    {
        return extractor.getServerReport();
    }


    /*
     * ========================================================================================
     * Activate and deactivate the open metadata and governance capabilities in the OMAG Server
     */


    /**
     * Return the complete set of configuration properties in use by the server.
     *
     * @return OMAGServerConfig properties
     * @throws OMAGNotAuthorizedException      the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException   invalid serverName parameter.
     * @throws OMAGConfigurationErrorException something went wrong with the REST call stack.
     */
    public OMAGServerConfig getOMAGServerConfig() throws OMAGNotAuthorizedException,
                                                         OMAGInvalidParameterException,
                                                         OMAGConfigurationErrorException
    {
        return extractor.getOMAGServerConfig();
    }


    /**
     * Activate the Open Metadata and Governance (OMAG) server using the configuration document stored for this server.
     *
     * @return success message
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the server.
     */
    public String activateServer() throws UserNotAuthorizedException,
                                          InvalidParameterException,
                                          PropertyServerException
    {
        return extractor.activateServer();
    }



    /**
     * Temporarily deactivate any open metadata and governance services.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void shutdownServer() throws UserNotAuthorizedException,
                                        InvalidParameterException,
                                        PropertyServerException
    {
        extractor.shutdownServer();
    }


    /**
     * Permanently deactivate any open metadata and governance services and unregister from
     * any cohorts.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */

    public void shutdownAndUnregisterServer() throws UserNotAuthorizedException,
                                                     InvalidParameterException,
                                                     PropertyServerException
    {
        extractor.shutdownAndUnregisterServer();
    }



    /*
     * =============================================================
     * Operational status and control
     */

    /**
     * Retrieve the server status
     *
     * @return The server status
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ServerStatus getServerStatus() throws InvalidParameterException,
                                                 UserNotAuthorizedException,
                                                 PropertyServerException
    {
        return extractor.getServerStatus();
    }


    /**
     * Return the configuration used for the current active instance of the server.  Null is returned if
     * the server instance is not running.
     *
     * @return configuration properties used to initialize the server or null if not running
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public OMAGServerConfig getActiveConfiguration() throws UserNotAuthorizedException,
                                                            InvalidParameterException,
                                                            PropertyServerException
    {
        return extractor.getActiveConfiguration();
    }



    /**
     * Return the status of a running server (use platform services to find out if the server is running).
     *
     * @return status of the server
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public ServerServicesStatus getActiveServerStatus() throws UserNotAuthorizedException,
                                                               InvalidParameterException,
                                                               PropertyServerException
    {
        return extractor.getActiveServerStatus();
    }


    /**
     * Retrieve a list of the active services on a server
     *
     * @return List of service names
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<String> getActiveServices() throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException
    {
        return extractor.getActiveServices();
    }


    /**
     * A new server needs to register the metadataCollectionId for its metadata repository with the other servers in the
     * open metadata repository.  It only needs to do this once and uses a timestamp to record that the registration
     * event has been sent.
     * If the server has already registered in the past, it sends a reregistration request.
     *
     * @param userId calling user
     * @param cohortName name of cohort
     *
     * @return boolean to indicate that the request has been issued.  If false it is likely that the cohort name is not known
     *
     * @throws InvalidParameterException one of the supplied parameters caused a problem
     * @throws PropertyServerException there is a problem communicating with the remote server.
     * @throws UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public boolean connectToCohort(String userId,
                                   String cohortName) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        return extractor.connectToCohort(userId, cohortName);
    }



    /**
     * Disconnect communications from a specific cohort.
     *
     * @param userId calling user
     * @param cohortName name of cohort
     * @return boolean flag to indicate success.
     * @throws InvalidParameterException one of the supplied parameters caused a problem
     * @throws PropertyServerException there is a problem communicating with the remote server.
     * @throws UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public boolean disconnectFromCohort(String userId,
                                        String cohortName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        return extractor.disconnectFromCohort(userId, cohortName);
    }


    /**
     * Unregister from a specific cohort and disconnect from cohort communications.
     *
     * @param userId calling user
     * @param cohortName name of cohort
     * @return boolean flag to indicate success.
     * @throws InvalidParameterException one of the supplied parameters caused a problem
     * @throws PropertyServerException there is a problem communicating with the remote server.
     * @throws UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public boolean unregisterFromCohort(String userId,
                                        String cohortName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        return extractor.unregisterFromCohort(userId, cohortName);
    }
}

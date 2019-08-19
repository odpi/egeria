/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.discoveryserver.server;

import org.odpi.openmetadata.accessservices.discoveryengine.client.DiscoveryConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.DiscoveryServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.client.ODFRESTClient;
import org.odpi.openmetadata.discoveryserver.auditlog.DiscoveryServerAuditCode;
import org.odpi.openmetadata.discoveryserver.ffdc.DiscoveryServerErrorCode;
import org.odpi.openmetadata.discoveryserver.handlers.DiscoveryEngineHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * DiscoveryServerOperationalServices is responsible for controlling the startup and shutdown of
 * a discovery server instance.  It is passed the discovery server configuration. This configuration provides
 * bootstrap information to connect to an open metadata repository to retrieve detailed information
 * about the discovery engines that should be hosted in this discovery server
 */
public class DiscoveryServerOperationalServices
{
    private String                  localServerName;               /* Initialized in constructor */
    private String                  localServerUserId;             /* Initialized in constructor */
    private String                  localServerPassword;           /* Initialized in constructor */
    private int                     maxPageSize;                   /* Initialized in constructor */

    private DiscoveryServerInstance discoveryServerInstance = null;

    private OMRSAuditLog            auditLog                = null;




    /**
     * Constructor used at server startup.
     *
     * @param localServerName name of the local server
     * @param localServerUserId user id for this server to use if sending REST requests and
     *                          processing inbound messages.
     * @param localServerPassword password for this server to use if sending REST requests.
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     */
    public DiscoveryServerOperationalServices(String                   localServerName,
                                              String                   localServerUserId,
                                              String                   localServerPassword,
                                              int                      maxPageSize)
    {
        this.localServerName       = localServerName;
        this.localServerUserId     = localServerUserId;
        this.localServerPassword   = localServerPassword;
        this.maxPageSize           = maxPageSize;
    }


    /**
     * Initialize the service.
     *
     * @param discoveryServerConfig config properties
     * @param auditLog destination for audit log messages.
     * @throws OMAGConfigurationErrorException there are no discovery engines defined for this server,
     * or the requested discovery engines are not recognized or are not configured properly.
     */
    public void initialize(DiscoveryServerConfig discoveryServerConfig,
                           OMRSAuditLog          auditLog) throws OMAGConfigurationErrorException
    {
        final String             actionDescription = "initialize";
        final String             methodName = "initialize";

        DiscoveryServerAuditCode auditCode;
        ODFRESTClient            ODFRESTClient;

        this.auditLog = auditLog;

        auditCode = DiscoveryServerAuditCode.SERVER_INITIALIZING;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(localServerName),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        if (discoveryServerConfig == null)
        {
            DiscoveryServerErrorCode errorCode    = DiscoveryServerErrorCode.NO_CONFIG_DOC;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(localServerName);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 errorMessage,
                                                 errorCode.getSystemAction(),
                                                 errorCode.getUserAction());
        }
        else if (discoveryServerConfig.getAccessServiceRootURL() == null)
        {
            auditCode = DiscoveryServerAuditCode.NO_OMAS_SERVER_URL;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(localServerName),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());

            DiscoveryServerErrorCode errorCode    = DiscoveryServerErrorCode.NO_OMAS_SERVER_URL;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(localServerName);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction());
        }
        else if (discoveryServerConfig.getAccessServiceServerName() == null)
        {
            auditCode = DiscoveryServerAuditCode.NO_OMAS_SERVER_NAME;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(localServerName),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());

            DiscoveryServerErrorCode errorCode    = DiscoveryServerErrorCode.NO_OMAS_SERVER_NAME;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(localServerName);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction());
        }


        /*
         * Create the client information
         */
        DiscoveryConfigurationClient configurationClient;
        try
        {
            if ((localServerName != null) && (localServerPassword != null))
            {
                ODFRESTClient = new ODFRESTClient(discoveryServerConfig.getAccessServiceServerName(),
                                                  discoveryServerConfig.getAccessServiceRootURL(),
                                                  localServerUserId,
                                                  localServerPassword);
            }
            else
            {
                ODFRESTClient = new ODFRESTClient(discoveryServerConfig.getAccessServiceServerName(),
                                                  discoveryServerConfig.getAccessServiceRootURL());
            }

            configurationClient = new DiscoveryConfigurationClient(discoveryServerConfig.getAccessServiceServerName(),
                                                                   discoveryServerConfig.getAccessServiceRootURL(),
                                                                   ODFRESTClient,
                                                                   maxPageSize);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGConfigurationErrorException(error.getReportedHTTPCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      error.getErrorMessage(),
                                                      error.getReportedSystemAction(),
                                                      error.getReportedUserAction(),
                                                      error);
        }

        List<String> discoveryEngineGUIDs = discoveryServerConfig.getDiscoveryEngineGUIDs();

        if ((discoveryEngineGUIDs == null) || (discoveryEngineGUIDs.isEmpty()))
        {
            auditCode = DiscoveryServerAuditCode.NO_DISCOVERY_ENGINES;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());

            DiscoveryServerErrorCode errorCode    = DiscoveryServerErrorCode.NO_DISCOVERY_ENGINES;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(localServerName);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction());
        }



        Map<String, DiscoveryEngineHandler> discoveryEngineHandlers = new HashMap<>();

        try
        {
            for (String   discoveryEngineGUID : discoveryEngineGUIDs)
            {
                if (discoveryEngineGUID != null)
                {
                    DiscoveryEngineHandler  handler = new DiscoveryEngineHandler(discoveryEngineGUID,
                                                                                 discoveryServerConfig.getAccessServiceRootURL(),
                                                                                 discoveryServerConfig.getAccessServiceServerName(),
                                                                                 localServerUserId,
                                                                                 configurationClient,
                                                                                 ODFRESTClient,
                                                                                 auditLog,
                                                                                 maxPageSize);

                    discoveryEngineHandlers.put(discoveryEngineGUID, handler);
                }
            }
        }
        catch (Throwable  error)
        {
            auditCode = DiscoveryServerAuditCode.SERVICE_INSTANCE_FAILURE;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(localServerName, error.getMessage()),
                               error.toString(),
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());

            DiscoveryServerErrorCode errorCode    = DiscoveryServerErrorCode.SERVICE_INSTANCE_FAILURE;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(localServerName, error.getMessage());

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction());
        }


        if (discoveryEngineHandlers.isEmpty())
        {
            auditCode = DiscoveryServerAuditCode.NO_DISCOVERY_ENGINES_STARTED;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(localServerName),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());

            DiscoveryServerErrorCode errorCode    = DiscoveryServerErrorCode.NO_DISCOVERY_ENGINES_STARTED;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(localServerName);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction());        }
        else
        {
            discoveryServerInstance = new DiscoveryServerInstance(localServerName,
                                                                  GovernanceServicesDescription.DISCOVERY_ENGINE_SERVICES.getServiceName(),
                                                                  auditLog,
                                                                  localServerUserId,
                                                                  maxPageSize,
                                                                  discoveryServerConfig.getAccessServiceRootURL(),
                                                                  discoveryServerConfig.getAccessServiceServerName(),
                                                                  discoveryEngineHandlers);
        }
    }


    /**
     * Shutdown the service.
     */
    public void terminate()
    {

        final String             actionDescription = "terminate";
        DiscoveryServerAuditCode auditCode;

        auditCode = DiscoveryServerAuditCode.SERVER_SHUTTING_DOWN;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(localServerName),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        discoveryServerInstance.shutdown();

        auditCode = DiscoveryServerAuditCode.SERVER_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(localServerName),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
    }
}

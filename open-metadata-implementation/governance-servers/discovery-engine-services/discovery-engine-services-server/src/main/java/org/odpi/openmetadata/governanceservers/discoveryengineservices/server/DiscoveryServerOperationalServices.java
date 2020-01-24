/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.discoveryserver.server;

import org.odpi.openmetadata.accessservices.discoveryengine.client.DiscoveryConfigurationClient;
import org.odpi.openmetadata.accessservices.discoveryengine.client.DiscoveryEngineClient;
import org.odpi.openmetadata.adminservices.configuration.properties.DiscoveryEngineServicesConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.client.ODFRESTClient;
import org.odpi.openmetadata.discoveryserver.auditlog.DiscoveryServerAuditCode;
import org.odpi.openmetadata.discoveryserver.ffdc.DiscoveryServerErrorCode;
import org.odpi.openmetadata.discoveryserver.handlers.DiscoveryEngineHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryEngineProperties;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

import java.util.ArrayList;
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
     * Initialize the service.  This involves setting up clients to communicate with the Discovery Engine OMAS,
     * retrieving the discovery engine configuration and setting up the discovery engine handlers ready to
     * receive new discovery requests.
     *
     * @param discoveryEngineServicesConfig config properties
     * @param auditLog destination for audit log messages.
     * @throws OMAGConfigurationErrorException there are no discovery engines defined for this server,
     * or the requested discovery engines are not recognized or are not configured properly.
     */
    public void initialize(DiscoveryEngineServicesConfig discoveryEngineServicesConfig,
                           OMRSAuditLog          auditLog) throws OMAGConfigurationErrorException
    {
        final String             actionDescription = "initialize";
        final String             methodName = "initialize";

        DiscoveryServerAuditCode auditCode;
        ODFRESTClient            restClient;

        this.auditLog = auditLog;

        auditCode = DiscoveryServerAuditCode.SERVER_INITIALIZING;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(localServerName),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());


        /*
         * Handover problem between the Admin services and the discovery engine services if the config is null.
         */
        if (discoveryEngineServicesConfig == null)
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

        /*
         * The configuration for the discovery engines is located in an open metadata server.
         * It is accessed through the Discovery Engine OMAS.  If the values needed to call
         * the Discovery Engine OMAS are not present in the configuration then there is no point in continuing
         * and an exception is thrown.
         */
        String accessServiceRootURL = this.getAccessServiceRootURL(discoveryEngineServicesConfig);
        String accessServiceServerName = this.getAccessServiceServerName(discoveryEngineServicesConfig);

        /*
         * Create the client for accessing the configuration.  The Discovery Engine OMAS has a specific client for retrieving
         * configuration. Any problems results in an exception.
         */
        DiscoveryConfigurationClient configurationClient;
        try
        {
            if ((localServerName != null) && (localServerPassword != null))
            {
                restClient = new ODFRESTClient(accessServiceServerName,
                                               accessServiceRootURL,
                                               localServerUserId,
                                               localServerPassword);
            }
            else
            {
                restClient = new ODFRESTClient(accessServiceServerName, accessServiceRootURL);
            }

            configurationClient = new DiscoveryConfigurationClient(accessServiceServerName,
                                                                   accessServiceRootURL,
                                                                   restClient,
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

        /*
         * Retrieve the list of discovery engines that this discovery server is to host.
         */
        List<String> discoveryEngineGUIDs = this.getDiscoveryEngineGUIDs(discoveryEngineServicesConfig, configurationClient);

        /*
         * Create a discovery handler for each of the discovery engines.
         */
        Map<String, DiscoveryEngineHandler> discoveryEngineHandlers = this.getDiscoveryEngineHandlers(discoveryEngineGUIDs,
                                                                                                      accessServiceRootURL,
                                                                                                      accessServiceServerName,
                                                                                                      configurationClient,
                                                                                                      restClient);

        if (discoveryEngineHandlers == null)
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
                                                                  discoveryEngineServicesConfig.getAccessServiceRootURL(),
                                                                  discoveryEngineServicesConfig.getAccessServiceServerName(),
                                                                  discoveryEngineHandlers);
        }
    }


    /**
     * Return the open metadata server's root URL from the configuration.
     *
     * @param discoveryEngineServicesConfig configuration
     * @return root URL
     * @throws OMAGConfigurationErrorException No root URL present in the config
     */
    private String getAccessServiceRootURL(DiscoveryEngineServicesConfig discoveryEngineServicesConfig) throws OMAGConfigurationErrorException
    {
        String accessServiceRootURL = discoveryEngineServicesConfig.getAccessServiceRootURL();

        if (accessServiceRootURL == null)
        {
            final String actionDescription = "initialize discovery engines";
            final String methodName        = "getAccessServiceRootURL";

            DiscoveryServerAuditCode auditCode = DiscoveryServerAuditCode.NO_OMAS_SERVER_URL;
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

        return accessServiceRootURL;
    }


    /**
     * Return the open metadata server's name from the configuration.
     *
     * @param discoveryEngineServicesConfig configuration
     * @return server name
     * @throws OMAGConfigurationErrorException No server name present in the config
     */
    private String getAccessServiceServerName(DiscoveryEngineServicesConfig discoveryEngineServicesConfig) throws OMAGConfigurationErrorException
    {
        String accessServiceServerName = discoveryEngineServicesConfig.getAccessServiceServerName();

        if (accessServiceServerName == null)
        {
            final String actionDescription = "initialize discovery engines";
            final String methodName        = "getAccessServiceServerName";

            DiscoveryServerAuditCode auditCode = DiscoveryServerAuditCode.NO_OMAS_SERVER_NAME;
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

        return accessServiceServerName;
    }


    /**
     * Retrieve the list of discovery engines for this server from the configuration.
     *
     * @param discoveryEngineServicesConfig configuration
     * @param configurationClient client of Discovery Engine OMAS that
     * @return list of discovery engine GUIDs
     */
    @SuppressWarnings("deprecation")
    private List<String>  getDiscoveryEngineGUIDs(DiscoveryEngineServicesConfig discoveryEngineServicesConfig,
                                                  DiscoveryConfigurationClient configurationClient) throws OMAGConfigurationErrorException
    {
        final String actionDescription = "initialize discovery engines";
        final String methodName        = "getDiscoveryEngineGUIDs";

        List<String> discoveryEngineNames = discoveryEngineServicesConfig.getDiscoveryEngineNames();
        List<String> discoveryEngineGUIDs = new ArrayList<>();

        if (discoveryEngineNames != null)
        {
            for (String  discoveryEngineName : discoveryEngineNames)
            {
                if (discoveryEngineName != null)
                {
                    try
                    {
                        DiscoveryEngineProperties discoveryEngineProperties = configurationClient.getDiscoveryEngineByGUID(localServerUserId,
                                                                                                                           discoveryEngineName);

                        if (discoveryEngineProperties != null)
                        {
                            discoveryEngineGUIDs.add(discoveryEngineProperties.getGUID());
                        }
                    }
                    catch (Throwable  error)
                    {
                        DiscoveryServerAuditCode auditCode = DiscoveryServerAuditCode.UNKNOWN_DISCOVERY_ENGINE_NAME;
                        auditLog.logException(actionDescription,
                                              auditCode.getLogMessageId(),
                                              auditCode.getSeverity(),
                                              auditCode.getFormattedLogMessage(discoveryEngineName,
                                                                               discoveryEngineServicesConfig.getAccessServiceServerName(),
                                                                               error.getClass().getName(),
                                                                               error.getMessage(),
                                                                               localServerName),
                                              null,
                                              auditCode.getSystemAction(),
                                              auditCode.getUserAction(),
                                              error);

                        DiscoveryServerErrorCode errorCode    = DiscoveryServerErrorCode.UNKNOWN_DISCOVERY_ENGINE_NAME;
                        String                   errorMessage = errorCode.getErrorMessageId()
                                                              + errorCode.getFormattedErrorMessage(discoveryEngineName,
                                                                                                   discoveryEngineServicesConfig.getAccessServiceServerName(),
                                                                                                   error.getClass().getName(),
                                                                                                   error.getMessage(),
                                                                                                   localServerName);

                        throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                                  this.getClass().getName(),
                                                                  methodName,
                                                                  errorMessage,
                                                                  errorCode.getSystemAction(),
                                                                  errorCode.getUserAction());
                    }
                }
            }
        }
        else
        {
            /*
             * No discovery engine names have been found, revert to the old method of passing discovery engine GUIDs.
             * Note this method call is deprecated by the warning is suppressed in this method.
             */
            discoveryEngineGUIDs = discoveryEngineServicesConfig.getDiscoveryEngineGUIDs();
        }

        if ((discoveryEngineGUIDs == null) || (discoveryEngineGUIDs.isEmpty()))
        {
            DiscoveryServerAuditCode auditCode = DiscoveryServerAuditCode.NO_DISCOVERY_ENGINES;
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
        else
        {
            return discoveryEngineGUIDs;
        }
    }


    /**
     * Create the list of discovery engine handlers.
     *
     * @param discoveryEngineGUIDs list of GUIDs for the discovery engines
     * @param accessServiceRootURL URL Root for the Discovery Engine OMAS
     * @param accessServiceServerName Server Name for the Discovery Engine OMAS
     * @param configurationClient client to retrieve configuration from
     * @param odfRESTClient client for calling REST APIs
     * @return map of discovery engine GUIDs to handlers
     * @throws OMAGConfigurationErrorException problem with config
     */
    private Map<String, DiscoveryEngineHandler>  getDiscoveryEngineHandlers(List<String>                 discoveryEngineGUIDs,
                                                                            String                       accessServiceRootURL,
                                                                            String                       accessServiceServerName,
                                                                            DiscoveryConfigurationClient configurationClient,
                                                                            ODFRESTClient                odfRESTClient) throws OMAGConfigurationErrorException
    {
        Map<String, DiscoveryEngineHandler> discoveryEngineHandlers = new HashMap<>();

        try
        {
            for (String   discoveryEngineGUID : discoveryEngineGUIDs)
            {
                if (discoveryEngineGUID != null)
                {
                    DiscoveryEngineHandler  handler = new DiscoveryEngineHandler(discoveryEngineGUID,
                                                                                 accessServiceServerName,
                                                                                 localServerUserId,
                                                                                 configurationClient,
                                                                                 new DiscoveryEngineClient(accessServiceServerName,
                                                                                                           accessServiceRootURL,
                                                                                                           odfRESTClient),
                                                                                 auditLog,
                                                                                 maxPageSize);

                    discoveryEngineHandlers.put(discoveryEngineGUID, handler);
                }
            }
        }
        catch (Throwable  error)
        {
            final String actionDescription = "initialize discovery engines";
            final String methodName        = "getDiscoveryEngineHandlers";

            DiscoveryServerAuditCode auditCode = DiscoveryServerAuditCode.SERVICE_INSTANCE_FAILURE;
            auditLog.logException(actionDescription,
                                  auditCode.getLogMessageId(),
                                  auditCode.getSeverity(),
                                  auditCode.getFormattedLogMessage(localServerName, error.getMessage()),
                                  error.toString(),
                                  auditCode.getSystemAction(),
                                  auditCode.getUserAction(),
                                  error);

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
            return null;
        }
        else
        {
            return discoveryEngineHandlers;
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

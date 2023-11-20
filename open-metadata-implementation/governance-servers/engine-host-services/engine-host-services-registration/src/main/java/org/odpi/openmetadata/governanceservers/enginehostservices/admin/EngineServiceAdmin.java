/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.admin;

import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceContextClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesAuditCode;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesErrorCode;

import java.util.List;
import java.util.Map;


/**
 * EngineServiceAdmin is the interface that an engine service implements to receive its configuration.
 * The java class that implements this interface is created with a default constructor and then
 * the initialize method is called.
 */
public abstract class EngineServiceAdmin
{
    protected String   localServerName       = null;
    protected AuditLog auditLog              = null;

    protected InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();


    /**
     * Initialize engine service.
     *
     * @param localServerId unique identifier of this server
     * @param localServerName name of this server
     * @param auditLog link to the repository responsible for servicing the REST calls
     * @param localServerUserId user id for this server to use if sending REST requests and processing inbound messages
     * @param localServerPassword password for this server to use if sending REST requests
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     * @param configurationClient client used to connect to the Governance Engine OMAS to retrieve the governance engine definitions
     * @param governanceActionClient client used to connect to the Governance Engine OMAS to manage governance actions
     * @param engineServiceConfig details of the options and the engines to run
     *
     * @return map of governance engine names to handlers
     * @throws OMAGConfigurationErrorException an issue in the configuration prevented initialization
     */
    public abstract Map<String, GovernanceEngineHandler> initialize(String                              localServerId,
                                                                    String                              localServerName,
                                                                    AuditLog                            auditLog,
                                                                    String                              localServerUserId,
                                                                    String                              localServerPassword,
                                                                    int                                 maxPageSize,
                                                                    GovernanceEngineConfigurationClient configurationClient,
                                                                    GovernanceContextClient             governanceActionClient,
                                                                    EngineServiceConfig                 engineServiceConfig) throws OMAGConfigurationErrorException;



    /**
     * Shutdown the engine service.
     */
    public abstract void shutdown();


    /**
     * Return the open metadata server's root URL from the configuration.
     *
     * @param engineServicesConfig configuration
     * @return root URL
     * @throws OMAGConfigurationErrorException No root URL present in the config
     */
    protected String getPartnerServiceRootURL(EngineServiceConfig engineServicesConfig) throws OMAGConfigurationErrorException
    {
        String accessServiceRootURL = engineServicesConfig.getOMAGServerPlatformRootURL();

        if (accessServiceRootURL == null)
        {
            final String actionDescription = "Validate engine services configuration.";
            final String methodName        = "getPartnerServiceRootURL";

            auditLog.logMessage(actionDescription,
                                EngineHostServicesAuditCode.NO_OMAS_SERVER_URL.getMessageDefinition(engineServicesConfig.getEngineServiceFullName(),
                                                                                                    localServerName,
                                                                                                    engineServicesConfig.getEngineServicePartnerOMAS()));

            throw new OMAGConfigurationErrorException(EngineHostServicesErrorCode.NO_OMAS_SERVER_URL.getMessageDefinition(engineServicesConfig.getEngineServiceFullName(),
                                                                                                                 localServerName,
                                                                                                                 engineServicesConfig.getEngineServicePartnerOMAS()),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        return accessServiceRootURL;
    }


    /**
     * Validate the content of the configuration.
     *
     * @param engineServiceConfig configuration
     * @throws InvalidParameterException Missing content from the config
     */
    protected void validateConfigDocument(EngineServiceConfig engineServiceConfig) throws InvalidParameterException
    {
        final String methodName                  = "validateConfigDocument";
        final String configPropertyName          = "engineServiceConfig";
        final String fullServiceNamePropertyName = "engineServiceConfig.engineServiceFullName";
        final String serviceNamePropertyName     = "engineServiceConfig.engineServiceName";
        final String partnerOMASPropertyName     = "engineServiceConfig.engineServicePartnerOMAS";

        if (engineServiceConfig == null)
        {
            throw new InvalidParameterException(EngineHostServicesErrorCode.NULL_SERVICE_CONFIG_VALUE.getMessageDefinition(localServerName),
                                                this.getClass().getName(),
                                                methodName,
                                                configPropertyName);
        }

        invalidParameterHandler.validateName(engineServiceConfig.getEngineServiceFullName(), fullServiceNamePropertyName, methodName);
        invalidParameterHandler.validateName(engineServiceConfig.getEngineServiceName(), serviceNamePropertyName, methodName);
        invalidParameterHandler.validateName(engineServiceConfig.getEngineServicePartnerOMAS(), partnerOMASPropertyName, methodName);
    }


    /**
     * Return the open metadata server's name from the configuration.
     *
     * @param engineServiceConfig configuration
     * @return server name
     * @throws OMAGConfigurationErrorException No server name present in the config
     */
    protected String getPartnerServiceServerName(EngineServiceConfig engineServiceConfig) throws OMAGConfigurationErrorException
    {
        String accessServiceServerName = engineServiceConfig.getOMAGServerName();

        if (accessServiceServerName == null)
        {
            final String actionDescription = "Validate engine service configuration.";
            final String methodName        = "getPartnerServiceServerName";

            auditLog.logMessage(actionDescription,
                                EngineHostServicesAuditCode.NO_OMAS_SERVER_NAME.getMessageDefinition(engineServiceConfig.getEngineServiceFullName(),
                                                                                            localServerName,
                                                                                            engineServiceConfig.getEngineServicePartnerOMAS()));

            throw new OMAGConfigurationErrorException(EngineHostServicesErrorCode.NO_OMAS_SERVER_NAME.getMessageDefinition(engineServiceConfig.getEngineServiceFullName(),
                                                                                                                  localServerName,
                                                                                                                  engineServiceConfig.getEngineServicePartnerOMAS()),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        return accessServiceServerName;
    }


    /**
     * Retrieve the list of engine names for this engine service from the configuration.
     *
     * @param engineServiceConfig configuration
     * @return list of engines
     * @throws OMAGConfigurationErrorException an issue in the configuration prevented initialization
     */
    protected List<EngineConfig> getEngines(EngineServiceConfig engineServiceConfig) throws OMAGConfigurationErrorException
    {
        List<EngineConfig> engineNames = engineServiceConfig.getEngines();

        if ((engineNames == null) || (engineNames.isEmpty()))
        {
            final String actionDescription = "Validate engine services configuration.";
            final String methodName        = "getEngines";

            auditLog.logMessage(actionDescription, EngineHostServicesAuditCode.NO_ENGINES.getMessageDefinition(engineServiceConfig.getEngineServiceFullName(),
                                                                                                      localServerName));

            throw new OMAGConfigurationErrorException(EngineHostServicesErrorCode.NO_ENGINES.getMessageDefinition(engineServiceConfig.getEngineServiceFullName(),
                                                                                                         localServerName),
                                                      this.getClass().getName(),
                                                      methodName);
        }
        else
        {
            return engineNames;
        }
    }
}

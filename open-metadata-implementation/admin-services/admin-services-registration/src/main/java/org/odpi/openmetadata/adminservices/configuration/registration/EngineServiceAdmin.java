/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminAuditCode;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import java.util.List;


/**
 * EngineServiceAdmin is the interface that an engine service implements to receive its configuration.
 * The java class that implements this interface is created with a default constructor and then
 * the initialize method is called.
 */
public abstract class EngineServiceAdmin
{
    protected String   localServerName       = null;
    protected AuditLog auditLog              = null;


    /**
     * Initialize engine service.
     *
     * @param localServerId unique identifier of this server
     * @param localServerName name of this server
     * @param auditLog link to the repository responsible for servicing the REST calls
     * @param localServerUserId user id for this server to use if sending REST requests and processing inbound messages
     * @param localServerPassword password for this server to use if sending REST requests
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     * @param engineServiceConfig details of the options and the engines to run
     *
     * @throws OMAGConfigurationErrorException an issue in the configuration prevented initialization
     */
    public abstract void initialize(String              localServerId,
                                    String              localServerName,
                                    AuditLog            auditLog,
                                    String              localServerUserId,
                                    String              localServerPassword,
                                    int                 maxPageSize,
                                    EngineServiceConfig engineServiceConfig) throws OMAGConfigurationErrorException;


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
    protected String getAccessServiceRootURL(EngineServiceConfig engineServicesConfig) throws OMAGConfigurationErrorException
    {
        String accessServiceRootURL = engineServicesConfig.getOMAGServerPlatformRootURL();

        if (accessServiceRootURL == null)
        {
            final String actionDescription = "Validate engine services configuration.";
            final String methodName        = "getAccessServiceRootURL";

            auditLog.logMessage(actionDescription,
                                OMAGAdminAuditCode.NO_OMAS_SERVER_URL.getMessageDefinition(engineServicesConfig.getEngineServiceFullName(),
                                                                                           localServerName,
                                                                                           engineServicesConfig.getEngineServicePartnerOMAS()));

            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.NO_OMAS_SERVER_URL.getMessageDefinition(engineServicesConfig.getEngineServiceFullName(),
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
     * @param methodName calling method
     * @throws OMAGConfigurationErrorException Missing content from the config
     */
    protected void validateConfigDocument(EngineServiceConfig      engineServiceConfig,
                                          String                   methodName) throws OMAGConfigurationErrorException
    {
        final String fullServiceNamePropertyName = "engineServiceConfig.engineServiceFullName";
        final String serviceNamePropertyName     = "engineServiceConfig.engineServiceName";
        final String partnerOMASPropertyName     = "engineServiceConfig.engineServicePartnerOMAS";

        /*
         * Handover problem between the admin services and the engine service if the config is null.
         */
        if (engineServiceConfig == null)
        {
            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.NO_CONFIG_DOC.getMessageDefinition(localServerName),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        if (engineServiceConfig.getEngineServiceFullName() == null)
        {
            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.NULL_PROPERTY_NAME.getMessageDefinition(fullServiceNamePropertyName,
                                                                                                                 localServerName),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        if (engineServiceConfig.getEngineServiceName() == null)
        {
            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.NULL_PROPERTY_NAME.getMessageDefinition(serviceNamePropertyName,
                                                                                                                 localServerName),
                                                      this.getClass().getName(),
                                                      methodName);
        }


        if (engineServiceConfig.getEngineServicePartnerOMAS() == null)
        {
            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.NULL_PROPERTY_NAME.getMessageDefinition(partnerOMASPropertyName,
                                                                                                                 localServerName),
                                                      this.getClass().getName(),
                                                      methodName);
        }
    }


    /**
     * Return the open metadata server's name from the configuration.
     *
     * @param engineServiceConfig configuration
     * @return server name
     * @throws OMAGConfigurationErrorException No server name present in the config
     */
    protected String getAccessServiceServerName(EngineServiceConfig engineServiceConfig) throws OMAGConfigurationErrorException
    {
        String accessServiceServerName = engineServiceConfig.getOMAGServerName();

        if (accessServiceServerName == null)
        {
            final String actionDescription = "Validate engine service configuration.";
            final String methodName        = "getAccessServiceServerName";

            auditLog.logMessage(actionDescription,
                                OMAGAdminAuditCode.NO_OMAS_SERVER_NAME.getMessageDefinition(engineServiceConfig.getEngineServiceFullName(),
                                                                                            localServerName,
                                                                                            engineServiceConfig.getEngineServicePartnerOMAS()));

            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.NO_OMAS_SERVER_NAME.getMessageDefinition(engineServiceConfig.getEngineServiceFullName(),
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

            auditLog.logMessage(actionDescription, OMAGAdminAuditCode.NO_ENGINES.getMessageDefinition(engineServiceConfig.getEngineServiceFullName(),
                                                                                                      localServerName));

            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.NO_ENGINES.getMessageDefinition(engineServiceConfig.getEngineServiceFullName(),
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

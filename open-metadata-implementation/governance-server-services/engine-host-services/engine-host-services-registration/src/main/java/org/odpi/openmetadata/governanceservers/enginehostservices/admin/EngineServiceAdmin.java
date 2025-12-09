/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.admin;


import org.odpi.openmetadata.governanceservers.enginehostservices.registration.EngineServiceDefinition;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesErrorCode;


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
     * @param localServerName name of this server
     * @param auditLog link to the repository responsible for servicing the REST calls
     * @param localServerUserId user id for this server to use if sending REST requests and processing inbound messages
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     * @param engineServiceDefinition details of the options and the engines to run
     * @throws OMAGConfigurationErrorException an issue in the configuration prevented initialization
     */
    public abstract void initialize(String                        localServerName,
                                    AuditLog                      auditLog,
                                    String                        localServerUserId,
                                    int                           maxPageSize,
                                    EngineServiceDefinition       engineServiceDefinition) throws OMAGConfigurationErrorException;



    /**
     * Shutdown the engine service.
     */
    public abstract void shutdown();


    /**
     * Validate the content of the configuration.
     *
     * @param engineServiceDefinition configuration
     * @throws InvalidParameterException Missing content from the config
     */
    protected void validateEngineDefinition(EngineServiceDefinition engineServiceDefinition) throws InvalidParameterException
    {
        final String methodName                  = "validateConfigDocument";
        final String configPropertyName          = "engineServiceConfig";
        final String fullServiceNamePropertyName = "engineServiceConfig.engineServiceFullName";
        final String serviceNamePropertyName     = "engineServiceConfig.engineServiceName";
        final String partnerOMASPropertyName     = "engineServiceConfig.engineServicePartnerOMAS";

        if (engineServiceDefinition == null)
        {
            throw new InvalidParameterException(EngineHostServicesErrorCode.NULL_SERVICE_CONFIG_VALUE.getMessageDefinition(localServerName),
                                                this.getClass().getName(),
                                                methodName,
                                                configPropertyName);
        }

        invalidParameterHandler.validateName(engineServiceDefinition.getEngineServiceFullName(), fullServiceNamePropertyName, methodName);
        invalidParameterHandler.validateName(engineServiceDefinition.getEngineServiceName(), serviceNamePropertyName, methodName);
        invalidParameterHandler.validateName(engineServiceDefinition.getEngineServicePartnerOMAS(), partnerOMASPropertyName, methodName);
    }
}

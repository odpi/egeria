/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.server;

import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMESServiceInstanceHandler;
import org.odpi.openmetadata.engineservices.governanceaction.handlers.GovernanceActionEngineHandler;
import org.odpi.openmetadata.engineservices.governanceaction.properties.ProviderReport;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;

/**
 * GovernanceActionInstanceHandler retrieves information from the instance map for the
 * governance action engine service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the GovernanceActionAdmin class.
 */
class GovernanceActionInstanceHandler extends OMESServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    GovernanceActionInstanceHandler()
    {
        super(EngineServiceDescription.GOVERNANCE_ACTION_OMES.getEngineServiceName());

        GovernanceActionRegistration.registerEngineService();
    }


    /**
     * Return the connector type for the requested connector provider after validating that the resulting
     * connector implements the correct interface.  This method is for tools that are configuring
     * connector into an Egeria governance server.  Each integration service/engine service has a specific
     * REST API endpoint for their service.  The configuration tool calls it and this method is called.
     * Because the method is in the instance handler, a result can be returned for all registered services
     * (the service does not need to be configured and running).
     *
     * @param connectorProviderClassName name of the connector provider class
     * @param requiredConnectorInterface  connector interface class
     * @param serviceName service name
     * @return ConnectorType bean
     * @throws InvalidParameterException one of the parameters is null
     * @throws ConnectionCheckedException the connection passed to the connector provider is not valid
     * @throws ConnectorCheckedException the connector is not valid
     * @throws ClassNotFoundException when the provided class cannot be found
     * @throws InstantiationException when the provided class cannot be instantiated
     * @throws IllegalAccessException when there is insufficient access to instantiate the provided class
     */
    public ProviderReport validateGovernanceActionConnector(String   connectorProviderClassName,
                                                            Class<?> requiredConnectorInterface,
                                                            String   serviceName) throws InvalidParameterException,
                                                                                         ConnectionCheckedException,
                                                                                         ConnectorCheckedException,
                                                                                         ClassNotFoundException,
                                                                                         InstantiationException,
                                                                                         IllegalAccessException
    {
        ProviderReport providerReport = new ProviderReport();
        providerReport.setConnectorType(validateConnector(connectorProviderClassName, requiredConnectorInterface, serviceName));

        Class<?> connectorProviderClass = Class.forName(connectorProviderClassName);
        Object   connectorProvider = connectorProviderClass.newInstance();

        if (connectorProvider instanceof GovernanceActionServiceProviderBase)
        {
            GovernanceActionServiceProviderBase governanceActionServiceProvider = (GovernanceActionServiceProviderBase)connectorProvider;
            providerReport.setSupportedRequestTypes(governanceActionServiceProvider.supportedRequestTypes());
            providerReport.setSupportedRequestParameters(governanceActionServiceProvider.supportedRequestParameters());
            providerReport.setSupportedRequestSourceNames(governanceActionServiceProvider.supportedRequestSourceNames());
            providerReport.setSupportedActionTargetNames(governanceActionServiceProvider.supportedActionTargetNames());
            providerReport.setSupportedGuards(governanceActionServiceProvider.supportedGuards());
        }

        return providerReport;
    }
}

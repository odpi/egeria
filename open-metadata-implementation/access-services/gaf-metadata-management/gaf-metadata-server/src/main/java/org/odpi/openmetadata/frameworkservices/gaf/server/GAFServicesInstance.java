/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server;


import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EngineActionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceActionProcessStepHandler;
import org.odpi.openmetadata.commonservices.multitenant.AccessServerServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.GovernanceActionProcessElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.GovernanceActionProcessStepElement;
import org.odpi.openmetadata.frameworkservices.gaf.converters.EngineActionConverter;
import org.odpi.openmetadata.frameworkservices.gaf.converters.GovernanceActionProcessConverter;
import org.odpi.openmetadata.frameworkservices.gaf.converters.GovernanceActionProcessStepConverter;
import org.odpi.openmetadata.frameworkservices.gaf.ffdc.OpenGovernanceErrorCode;
import org.odpi.openmetadata.frameworkservices.gaf.handlers.GovernanceEngineConfigurationHandler;
import org.odpi.openmetadata.frameworkservices.gaf.handlers.IntegrationGroupConfigurationHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * GAFServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 * It is created by the admin class during server start up and
 */
public class GAFServicesInstance extends AccessServerServiceInstance
{
    private final static AccessServiceDescription myDescription = AccessServiceDescription.GAF_METADATA_MANAGEMENT;

    private final GovernanceEngineConfigurationHandler                                   governanceEngineConfigurationHandler;
    private final IntegrationGroupConfigurationHandler                                   integrationGroupConfigurationHandler;
    private final EngineActionHandler<EngineActionElement>                               engineActionHandler;
    private final AssetHandler<GovernanceActionProcessElement>                           governanceActionProcessHandler;
    private final GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> governanceActionProcessStepHandler;

    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize max number of results to return on single request.
     *
     * @throws NewInstanceException a problem occurred during initialization
     */
    public GAFServicesInstance(OMRSRepositoryConnector repositoryConnector,
                               AuditLog                auditLog,
                               String                  localServerUserId,
                               int                     maxPageSize) throws NewInstanceException
    {
        super(myDescription.getServiceName(),
              repositoryConnector,
              auditLog,
              localServerUserId,
              maxPageSize);

        final String methodName = "new ServiceInstance";

        if (repositoryHandler != null)
        {
            this.governanceEngineConfigurationHandler = new GovernanceEngineConfigurationHandler(serviceName,
                                                                                                 serverName,
                                                                                                 invalidParameterHandler,
                                                                                                 repositoryHandler,
                                                                                                 repositoryHelper,
                                                                                                 localServerUserId,
                                                                                                 securityVerifier,
                                                                                                 auditLog);

            this.integrationGroupConfigurationHandler = new IntegrationGroupConfigurationHandler(serviceName,
                                                                                                 serverName,
                                                                                                 invalidParameterHandler,
                                                                                                 repositoryHandler,
                                                                                                 repositoryHelper,
                                                                                                 localServerUserId,
                                                                                                 securityVerifier,
                                                                                                 auditLog);

            this.engineActionHandler = new EngineActionHandler<>(new EngineActionConverter<>(repositoryHelper, serviceName, serverName),
                                                                 EngineActionElement.class,
                                                                 serviceName,
                                                                 serverName,
                                                                 invalidParameterHandler,
                                                                 repositoryHandler,
                                                                 repositoryHelper,
                                                                 localServerUserId,
                                                                 securityVerifier,
                                                                 auditLog);

            this.governanceActionProcessHandler = new AssetHandler<>(new GovernanceActionProcessConverter<>(repositoryHelper, serviceName, serverName),
                                                                     GovernanceActionProcessElement.class,
                                                                     serviceName,
                                                                     serverName,
                                                                     invalidParameterHandler,
                                                                     repositoryHandler,
                                                                     repositoryHelper,
                                                                     localServerUserId,
                                                                     securityVerifier,
                                                                     auditLog);

            this.governanceActionProcessStepHandler = new GovernanceActionProcessStepHandler<>(new GovernanceActionProcessStepConverter<>(repositoryHelper, serviceName, serverName),
                                                                                               GovernanceActionProcessStepElement.class,
                                                                                               serviceName,
                                                                                               serverName,
                                                                                               invalidParameterHandler,
                                                                                               repositoryHandler,
                                                                                               repositoryHelper,
                                                                                               localServerUserId,
                                                                                               securityVerifier,
                                                                                               auditLog);
        }
        else
        {
            throw new NewInstanceException(OpenGovernanceErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);
        }
    }

    /**
     * Return the handler for configuring governance engines.
     *
     * @return handler object
     */
    public GovernanceEngineConfigurationHandler getGovernanceConfigurationHandler()
    {
        return governanceEngineConfigurationHandler;
    }



    /**
     * Return the handler for integration group configuration requests.
     *
     * @return handler object
     */
    IntegrationGroupConfigurationHandler getIntegrationGroupConfigurationHandler()
    {
        return integrationGroupConfigurationHandler;
    }


    /**
     * Return the handler for governance action process requests.
     *
     * @return handler object
     */
    AssetHandler<GovernanceActionProcessElement> getGovernanceActionProcessHandler()
    {
        return governanceActionProcessHandler;
    }


    /**
     * Return the handler for governance action process step requests.
     *
     * @return handler object
     */
    GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> getGovernanceActionProcessStepHandler()
    {
        return governanceActionProcessStepHandler;
    }



    /**
     * Return the handler for governance action requests.
     *
     * @return handler object
     */
    public EngineActionHandler<EngineActionElement> getEngineActionHandler()
    {
        return engineActionHandler;
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server;


import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworkservices.gaf.converters.*;
import org.odpi.openmetadata.frameworkservices.gaf.ffdc.OpenGovernanceErrorCode;
import org.odpi.openmetadata.frameworkservices.gaf.handlers.GovernanceEngineConfigurationHandler;
import org.odpi.openmetadata.frameworkservices.gaf.handlers.IntegrationGroupConfigurationHandler;
import org.odpi.openmetadata.commonservices.multitenant.AccessServerServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworkservices.omf.handlers.MetadataElementHandler;
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
    private final MetadataElementHandler<OpenMetadataElement>                            metadataElementHandler;
    private final EngineActionHandler<EngineActionElement>                               engineActionHandler;
    private final AssetHandler<GovernanceActionProcessElement>                           governanceActionProcessHandler;
    private final GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> governanceActionProcessStepHandler;
    private final GovernanceActionTypeHandler<GovernanceActionTypeElement>               governanceActionTypeHandler;

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
              null,
              null,
              null,
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
                                                                                                 supportedZones,
                                                                                                 defaultZones,
                                                                                                 publishZones,
                                                                                                 auditLog);

            this.integrationGroupConfigurationHandler = new IntegrationGroupConfigurationHandler(serviceName,
                                                                                                 serverName,
                                                                                                 invalidParameterHandler,
                                                                                                 repositoryHandler,
                                                                                                 repositoryHelper,
                                                                                                 localServerUserId,
                                                                                                 securityVerifier,
                                                                                                 supportedZones,
                                                                                                 defaultZones,
                                                                                                 publishZones,
                                                                                                 auditLog);

            this.metadataElementHandler = new MetadataElementHandler<>(new MetadataElementConverter<>(repositoryHelper, serviceName, serverName),
                                                                       OpenMetadataElement.class,
                                                                       serviceName,
                                                                       serverName,
                                                                       invalidParameterHandler,
                                                                       repositoryHandler,
                                                                       repositoryHelper,
                                                                       localServerUserId,
                                                                       securityVerifier,
                                                                       supportedZones,
                                                                       defaultZones,
                                                                       publishZones,
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
                                                                 supportedZones,
                                                                 defaultZones,
                                                                 publishZones,
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
                                                                     supportedZones,
                                                                     defaultZones,
                                                                     publishZones,
                                                                     null,
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
                                                                                               supportedZones,
                                                                                               defaultZones,
                                                                                               publishZones,
                                                                                               auditLog);

            this.governanceActionTypeHandler = new GovernanceActionTypeHandler<>(new GovernanceActionTypeConverter<>(repositoryHelper, serviceName, serverName),
                                                                                 GovernanceActionTypeElement.class,
                                                                                 serviceName,
                                                                                 serverName,
                                                                                 invalidParameterHandler,
                                                                                 repositoryHandler,
                                                                                 repositoryHelper,
                                                                                 localServerUserId,
                                                                                 securityVerifier,
                                                                                 supportedZones,
                                                                                 defaultZones,
                                                                                 publishZones,
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
     * Return the handler for open metadata store requests.
     *
     * @return handler object
     */
    public MetadataElementHandler<OpenMetadataElement> getMetadataElementHandler()
    {
        return metadataElementHandler;
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
     * Return the handler for governance action type requests.
     *
     * @return handler object
     */
    GovernanceActionTypeHandler<GovernanceActionTypeElement> getGovernanceActionTypeHandler()
    {
        return governanceActionTypeHandler;
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

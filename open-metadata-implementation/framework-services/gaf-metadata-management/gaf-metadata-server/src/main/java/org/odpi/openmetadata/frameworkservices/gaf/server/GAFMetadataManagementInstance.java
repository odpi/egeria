/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server;


import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworkservices.gaf.converters.*;
import org.odpi.openmetadata.frameworkservices.gaf.ffdc.OpenMetadataStoreErrorCode;
import org.odpi.openmetadata.frameworkservices.gaf.handlers.MetadataElementHandler;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * GAFMetadataManagementInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 * It is created by the admin class during server start up and
 */
public class GAFMetadataManagementInstance extends OMASServiceInstance
{
    private final static CommonServicesDescription myDescription = CommonServicesDescription.GAF_METADATA_MANAGEMENT;

    private final MetadataElementHandler<OpenMetadataElement>                            metadataElementHandler;
    private final ValidValuesHandler<ValidMetadataValue>                                 validMetadataValuesHandler;
    private final ValidValuesHandler<ValidMetadataValueDetail>                           validMetadataValuesDetailHandler;
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
    public GAFMetadataManagementInstance(OMRSRepositoryConnector repositoryConnector,
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
              maxPageSize,
              null,
              null,
              null,
              null);

        final String methodName = "new ServiceInstance";

        if (repositoryHandler != null)
        {
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


            this.validMetadataValuesHandler = new ValidValuesHandler<>(new ValidMetadataValueConverter<>(repositoryHelper, serviceName, serverName),
                                                                       ValidMetadataValue.class,
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

            this.validMetadataValuesDetailHandler = new ValidValuesHandler<>(new ValidMetadataValueConverter<>(repositoryHelper, serviceName, serverName),
                                                                             ValidMetadataValueDetail.class,
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
            throw new NewInstanceException(OpenMetadataStoreErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);
        }
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
     * Return the handler for open metadata store requests.
     *
     * @return handler object
     */
    public ValidValuesHandler<ValidMetadataValue> getValidMetadataValuesHandler()
    {
        return validMetadataValuesHandler;
    }

    /**
     * Return the handler for open metadata store requests.
     *
     * @return handler object
     */
    public ValidValuesHandler<ValidMetadataValueDetail> getValidMetadataValuesDetailHandler()
    {
        return validMetadataValuesDetailHandler;
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

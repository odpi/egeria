/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceserver.server;


import org.odpi.openmetadata.accessservices.governanceserver.connectors.outtopic.GovernanceServerOutTopicClientProvider;
import org.odpi.openmetadata.accessservices.governanceserver.ffdc.GovernanceServerErrorCode;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.EngineActionHandler;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.governanceaction.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworkservices.gaf.converters.EngineActionConverter;
import org.odpi.openmetadata.frameworkservices.omf.converters.MetadataElementConverter;
import org.odpi.openmetadata.frameworkservices.omf.handlers.MetadataElementHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * GovernanceEngineInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 * It is created by the admin class during server start up and
 */
public class GovernanceServerInstance extends OMASServiceInstance
{
    private static final AccessServiceDescription myDescription = AccessServiceDescription.GOVERNANCE_SERVER_OMAS;

    private final MetadataElementHandler<OpenMetadataElement>  metadataElementHandler;
    private final EngineActionHandler<EngineActionElement> engineActionHandler;

    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that GovernanceEngine is allowed to serve Assets from.
     * @param defaultZones list of zones that GovernanceEngine should set in all new Assets.
     * @param publishedZones list of zones that Governance Server can use to make a governance service visible.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize max number of results to return on single request.
     * @param outTopicEventBusConnection inner event bus connection to use to build topic connection to send to client if they which
     *                                   to listen on the out topic.
     *
     * @throws NewInstanceException a problem occurred during initialization
     */
    public GovernanceServerInstance(OMRSRepositoryConnector repositoryConnector,
                                    List<String>            supportedZones,
                                    List<String>            defaultZones,
                                    List<String>            publishedZones,
                                    AuditLog                auditLog,
                                    String                  localServerUserId,
                                    int                     maxPageSize,
                                    Connection              outTopicEventBusConnection) throws NewInstanceException
    {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              supportedZones,
              defaultZones,
              publishedZones,
              auditLog,
              localServerUserId,
              maxPageSize,
              null,
              null,
              GovernanceServerOutTopicClientProvider.class.getName(),
              outTopicEventBusConnection);

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
        }
        else
        {
            throw new NewInstanceException(GovernanceServerErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
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
     * Return the handler for governance action requests.
     *
     * @return handler object
     */
    public EngineActionHandler<EngineActionElement> getEngineActionHandler()
    {
        return engineActionHandler;
    }
}

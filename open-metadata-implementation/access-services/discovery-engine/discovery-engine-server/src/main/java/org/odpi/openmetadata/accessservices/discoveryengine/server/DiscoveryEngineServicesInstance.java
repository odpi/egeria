/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.server;


import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.DiscoveryEngineErrorCode;
import org.odpi.openmetadata.accessservices.discoveryengine.handlers.DiscoveryConfigurationHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.ODFOMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * DiscoveryEngineServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 * It is created by the admin class during server start up and
 */
public class DiscoveryEngineServicesInstance extends ODFOMASServiceInstance
{
    private static AccessServiceDescription myDescription = AccessServiceDescription.DISCOVERY_ENGINE_OMAS;

    private DiscoveryConfigurationHandler discoveryConfigurationHandler;
    private Connection                    outTopicConnection;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that DiscoveryEngine is allowed to serve Assets from.
     * @param defaultZones list of zones that DiscoveryEngine should set in all new Assets.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize max number of results to return on single request.
     * @param outTopicConnection connection to send to client if they which to listen on the out topic.
     *
     * @throws NewInstanceException a problem occurred during initialization
     */
    public DiscoveryEngineServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                           List<String>            supportedZones,
                                           List<String>            defaultZones,
                                           AuditLog                auditLog,
                                           String                  localServerUserId,
                                           int                     maxPageSize,
                                           Connection              outTopicConnection) throws NewInstanceException
    {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              supportedZones,
              defaultZones,
              auditLog,
              localServerUserId,
              maxPageSize);

        final String methodName = "new ServiceInstance";

        super.supportedZones = supportedZones;
        super.defaultZones = defaultZones;

        this.outTopicConnection = outTopicConnection;

        if (repositoryHandler != null)
        {
            this.discoveryConfigurationHandler = new DiscoveryConfigurationHandler(serviceName,
                                                                                   serverName,
                                                                                   invalidParameterHandler,
                                                                                   repositoryHelper,
                                                                                   repositoryHandler,
                                                                                   errorHandler,
                                                                                   connectionHandler);
        }
        else
        {
            throw new NewInstanceException(DiscoveryEngineErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);
        }
    }


    /**
     * Return the connection used in the client to create a connector to access events from the out topic.
     *
     * @return connection object for client
     */
    Connection getOutTopicConnection() { return outTopicConnection; }


    /**
     * Return the handler for configuration requests.
     *
     * @return handler object
     */
    DiscoveryConfigurationHandler getDiscoveryConfigurationHandler()
    {
        return discoveryConfigurationHandler;
    }
}

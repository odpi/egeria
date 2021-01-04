/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securityofficer.server.services;


import org.odpi.openmetadata.accessservices.securityofficer.api.ffdc.SecurityOfficerErrorCode;
import org.odpi.openmetadata.accessservices.securityofficer.connectors.outtopic.SecurityOfficerOutTopicClientProvider;
import org.odpi.openmetadata.accessservices.securityofficer.server.handler.GovernedAssetHandler;
import org.odpi.openmetadata.accessservices.securityofficer.server.handler.SecurityOfficerRequestHandler;
import org.odpi.openmetadata.accessservices.securityofficer.server.publisher.SecurityOfficerPublisher;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * SecurityOfficerInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 * It is created by the admin class during server start up and
 */
public class SecurityOfficerInstance extends OMASServiceInstance
{
    private static AccessServiceDescription myDescription = AccessServiceDescription.SECURITY_OFFICER_OMAS;

    private SecurityOfficerRequestHandler securityOfficerRequestHandler;
    private GovernedAssetHandler governedAssetHandler;

    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that SecurityOfficer is allowed to serve Assets from.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize max number of results to return on single request.
     * @param outTopicEventBusConnection inner event bus connection to use to build topic connection to send to client if they which
     *                                   to listen on the out topic.
     * @param securityOfficerPublisher outbound published
     *
     * @throws NewInstanceException a problem occurred during initialization
     */
    public SecurityOfficerInstance(OMRSRepositoryConnector  repositoryConnector,
                                   List<String>             supportedZones,
                                   AuditLog                 auditLog,
                                   String                   localServerUserId,
                                   int                      maxPageSize,
                                   Connection               outTopicEventBusConnection,
                                   SecurityOfficerPublisher securityOfficerPublisher) throws NewInstanceException
    {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              supportedZones,
              null,
              null,
              auditLog,
              localServerUserId,
              maxPageSize,
              null,
              null,
              SecurityOfficerOutTopicClientProvider.class.getName(),
              outTopicEventBusConnection);

        final String methodName = "new ServiceInstance";

        if (repositoryHandler != null)
        {
            this.securityOfficerRequestHandler = new SecurityOfficerRequestHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler,
                    metadataCollection, repositoryHelper, errorHandler, supportedZones, securityOfficerPublisher);

            this.governedAssetHandler = new GovernedAssetHandler(serviceName, serverName, invalidParameterHandler, repositoryHandler,
                                                                 repositoryHelper, errorHandler, supportedZones);
        }
        else
        {
            throw new NewInstanceException(SecurityOfficerErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);
        }
    }



    /**
     * Return the handler for governed asset requests
     *
     * @return handler object
     */
    public GovernedAssetHandler getGovernedAssetHandler() { return governedAssetHandler; }


    /**
     * Return the handler for security officer requests
     *
     * @return handler object
     */
    public SecurityOfficerRequestHandler getSecurityOfficerRequestHandler() { return securityOfficerRequestHandler; }

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.admin;

import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.registration.AccessServiceAdmin;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.frameworkservices.omf.connectors.outtopic.OMFOutTopicServerConnector;
import org.odpi.openmetadata.frameworkservices.omf.connectors.outtopic.OMFOutTopicServerProvider;
import org.odpi.openmetadata.frameworkservices.omf.ffdc.OMFServicesAuditCode;
import org.odpi.openmetadata.frameworkservices.omf.listener.OpenMetadataOMRSTopicListener;
import org.odpi.openmetadata.frameworkservices.omf.outtopic.OpenMetadataOutTopicPublisher;
import org.odpi.openmetadata.frameworkservices.omf.server.OMFServicesInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworkservices.omf.server.OMFServicesInstanceHandler;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;


/**
 * OMFOperationalServices initializes the REST Services that support the Governance Action Framework (omf)
 * open metadata store calls.
 */
public class OMFOperationalServices extends AccessServiceAdmin
{
    private String   localServerName = null;
    private AuditLog auditLog = null;
    private OpenMetadataOutTopicPublisher eventPublisher = null;

    /**
     * Initialize the access service.
     *
     * @param accessServiceConfig  specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector  connector for receiving OMRS Events from the cohorts
     * @param enterpriseOMRSRepositoryConnector  connector for querying the cohort repositories
     * @param auditLog  audit log component for logging messages.
     * @param localServerName name of this server
     * @param localServerUserId  user id to use on OMRS calls where there is no end user.
     * @param localServerPassword  password to use on OMRS calls where there is no end user.
     * @param maxPageSize max number of results to return on single request.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    @Override
    public void initialize(AccessServiceConfig accessServiceConfig,
                           OMRSTopicConnector      enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                           AuditLog                auditLog,
                           String                  localServerName,
                           String                  localServerUserId,
                           String                  localServerPassword,
                           int                     maxPageSize) throws OMAGConfigurationErrorException
    {
        this.localServerName = localServerName;
        this.auditLog = auditLog;

        final String actionDescription = "initialize";

        auditLog.logMessage(actionDescription, OMFServicesAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        try
        {
            auditLog.logMessage(actionDescription, OMFServicesAuditCode.SERVICE_INITIALIZED.getMessageDefinition(localServerName));

            Connection outTopicEventBusConnection = accessServiceConfig.getAccessServiceOutTopic();

            OMFServicesInstance instance = new OMFServicesInstance(enterpriseOMRSRepositoryConnector, auditLog, localServerUserId, maxPageSize, outTopicEventBusConnection);

            /*
             * Only set up the listening and event publishing if requested in the config.
             */
            if (outTopicEventBusConnection != null)
            {
                Endpoint endpoint = outTopicEventBusConnection.getEndpoint();

                AuditLog outTopicAuditLog = auditLog.createNewAuditLog(OMRSAuditingComponent.METADATA_ACCESS_SERVER_OUT_TOPIC);
                Connection serverSideOutTopicConnection = this.getServerSideOutTopicConnection(outTopicEventBusConnection,
                                                                                               AccessServiceDescription.OMF_METADATA_MANAGEMENT.getServiceName(),
                                                                                               OMFOutTopicServerProvider.class.getName(),
                                                                                               auditLog);
                OMFOutTopicServerConnector outTopicServerConnector = this.getTopicConnector(serverSideOutTopicConnection,
                                                                                            OMFOutTopicServerConnector.class,
                                                                                            outTopicAuditLog,
                                                                                            AccessServiceDescription.OMF_METADATA_MANAGEMENT.getServiceName(),
                                                                                            actionDescription);
                eventPublisher = new OpenMetadataOutTopicPublisher(outTopicServerConnector,
                                                                   endpoint.getAddress(),
                                                                   outTopicAuditLog,
                                                                   enterpriseOMRSRepositoryConnector.getRepositoryHelper(),
                                                                   AccessServiceDescription.OMF_METADATA_MANAGEMENT.getServiceName(),
                                                                   localServerName);

                this.registerWithEnterpriseTopic(AccessServiceDescription.OMF_METADATA_MANAGEMENT.getServiceName(),
                                                 localServerName,
                                                 enterpriseOMRSTopicConnector,
                                                 new OpenMetadataOMRSTopicListener(AccessServiceDescription.OMF_METADATA_MANAGEMENT.getServiceName(),
                                                                                   localServerUserId,
                                                                                   eventPublisher,
                                                                                   instance.getMetadataElementHandler(),
                                                                                   outTopicAuditLog),
                                                 auditLog);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  OMFServicesAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.OMF_METADATA_MANAGEMENT.getServiceName(),
                                                         error);
        }
    }


    /**
     * Shutdown the service.
     */
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        this.auditLog.logMessage(actionDescription, OMFServicesAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(localServerName));

        new OMFServicesInstanceHandler().removeServerServiceInstance(localServerName);

        if (this.eventPublisher != null)
        {
            this.eventPublisher.disconnect();
        }
    }
}

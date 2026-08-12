/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.admin;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.registration.AccessServiceAdmin;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworkservices.gaf.connectors.outtopic.GAFOutTopicServerConnector;
import org.odpi.openmetadata.frameworkservices.gaf.connectors.outtopic.GAFOutTopicServerProvider;
import org.odpi.openmetadata.frameworkservices.gaf.ffdc.OpenGovernanceAuditCode;
import org.odpi.openmetadata.frameworkservices.gaf.listener.OpenGovernanceOMRSTopicListener;
import org.odpi.openmetadata.frameworkservices.gaf.outtopic.OpenGovernanceOutTopicPublisher;
import org.odpi.openmetadata.frameworkservices.gaf.server.GAFServicesInstance;
import org.odpi.openmetadata.frameworkservices.gaf.server.GAFServicesInstanceHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;


/**
 * GAFOperationalServices initializes the REST Services that support the Open Governance Framework (OGF)
 * open metadata store calls.
 */
public class GAFOperationalServices extends AccessServiceAdmin
{
    private String                          localServerName = null;
    private AuditLog                        auditLog        = null;
    private OpenGovernanceOutTopicPublisher eventPublisher  = null;


    /**
     * Initialize the access service.
     *
     * @param accessServiceConfig  specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector  connector for receiving OMRS Events from the cohorts
     * @param enterpriseOMRSRepositoryConnector  connector for querying the cohort repositories
     * @param auditLog  audit log component for logging messages.
     * @param localServerName name of this server
     * @param localServerUserId  user id to use on OMRS calls where there is no end user.
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize max results to return on a single request.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    public void initialize(AccessServiceConfig     accessServiceConfig,
                           OMRSTopicConnector      enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                           AuditLog                auditLog,
                           String                  localServerName,
                           String                  localServerUserId,
                           String                  localServerSecretsStoreProvider,
                           String                  localServerSecretsStoreLocation,
                           String                  localServerSecretsStoreCollection,
                           int                     maxPageSize) throws OMAGConfigurationErrorException
    {
        this.localServerName = localServerName;
        this.auditLog = auditLog;

        final String actionDescription = "initialize";

        auditLog.logMessage(actionDescription, OpenGovernanceAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        try
        {
            auditLog.logMessage(actionDescription, OpenGovernanceAuditCode.SERVICE_INITIALIZED.getMessageDefinition(localServerName));

            Connection outTopicEventBusConnection = accessServiceConfig.getAccessServiceOutTopic();

            GAFServicesInstance instance = new GAFServicesInstance(enterpriseOMRSRepositoryConnector, auditLog, localServerUserId, maxPageSize, outTopicEventBusConnection);

            /*
             * Only set up the listening and event publishing if requested in the config.
             */
            if (outTopicEventBusConnection != null)
            {
                Endpoint endpoint = outTopicEventBusConnection.getEndpoint();

                AuditLog outTopicAuditLog = auditLog.createNewAuditLog(EgeriaOpenConnectorDefinition.GAF_TOPIC_SERVER_CONNECTOR.getComponentDescription());
                Connection serverSideOutTopicConnection = this.getServerSideOutTopicConnection(outTopicEventBusConnection,
                                                                                               AccessServiceDescription.GAF_METADATA_MANAGEMENT.getServiceName(),
                                                                                               GAFOutTopicServerProvider.class.getName(),
                                                                                               auditLog);
                GAFOutTopicServerConnector outTopicServerConnector = this.getTopicConnector(serverSideOutTopicConnection,
                                                                                            GAFOutTopicServerConnector.class,
                                                                                            outTopicAuditLog,
                                                                                            AccessServiceDescription.GAF_METADATA_MANAGEMENT.getServiceName(),
                                                                                            actionDescription);
                eventPublisher = new OpenGovernanceOutTopicPublisher(outTopicServerConnector, 
                                                                     endpoint.getNetworkAddress(), 
                                                                     outTopicAuditLog, 
                                                                     enterpriseOMRSRepositoryConnector.getRepositoryHelper(),
                                                                     AccessServiceDescription.GAF_METADATA_MANAGEMENT.getServiceName(),
                                                                     localServerName);

                this.registerWithEnterpriseTopic(AccessServiceDescription.GAF_METADATA_MANAGEMENT.getServiceName(),
                                                 localServerName,
                                                 enterpriseOMRSTopicConnector,
                                                 new OpenGovernanceOMRSTopicListener(AccessServiceDescription.GAF_METADATA_MANAGEMENT.getServiceName(),
                                                                                     localServerUserId,
                                                                                     eventPublisher,
                                                                                     instance.getEngineActionHandler(),
                                                                                     enterpriseOMRSRepositoryConnector.getRepositoryHelper(), 
                                                                                     outTopicAuditLog),
                                                 auditLog);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  OpenGovernanceAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.GAF_METADATA_MANAGEMENT.getServiceName(),
                                                         error);
        }
    }


    /**
     * Shutdown the service.
     */
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        this.auditLog.logMessage(actionDescription, OpenGovernanceAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(localServerName));

        new GAFServicesInstanceHandler().removeServerServiceInstance(localServerName);

        if (this.eventPublisher != null)
        {
            this.eventPublisher.disconnect();
        }
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.admin;

import org.odpi.openmetadata.accessservices.governanceengine.connectors.outtopic.GovernanceEngineOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.governanceengine.connectors.outtopic.GovernanceEngineOutTopicServerProvider;
import org.odpi.openmetadata.accessservices.governanceengine.ffdc.GovernanceEngineAuditCode;
import org.odpi.openmetadata.accessservices.governanceengine.ffdc.GovernanceEngineErrorCode;
import org.odpi.openmetadata.accessservices.governanceengine.outtopic.GovernanceEngineOMRSTopicListener;
import org.odpi.openmetadata.accessservices.governanceengine.outtopic.GovernanceEngineOutTopicPublisher;
import org.odpi.openmetadata.accessservices.governanceengine.server.GovernanceEngineInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * GovernanceEngineAdmin is called during server start up to set up the Governance Engine OMAS.
 */
public class GovernanceEngineAdmin extends AccessServiceAdmin
{
    private AuditLog                          auditLog       = null;
    private GovernanceEngineInstance          instance       = null;
    private String                            serverName     = null;
    private GovernanceEngineOutTopicPublisher eventPublisher = null;

    /**
     * Initialize the access service.
     *
     * @param accessServiceConfig specific configuration properties for this access service
     * @param omrsTopicConnector  connector for receiving OMRS Events from the cohorts
     * @param repositoryConnector connector for querying the cohort repositories
     * @param auditLog            audit log component for logging messages.
     * @param serverUserName      user id to use on OMRS calls where there is no end user
     *                                             
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    @Override
    public void initialize(AccessServiceConfig     accessServiceConfig,
                           OMRSTopicConnector      omrsTopicConnector,
                           OMRSRepositoryConnector repositoryConnector,
                           AuditLog                auditLog,
                           String                  serverUserName) throws OMAGConfigurationErrorException
    {
        final String             actionDescription = "initialize";

        auditLog.logMessage(actionDescription, GovernanceEngineAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        this.auditLog = auditLog;

        try
        {
            /*
             * The supported zones determines which assets can be queried/analysed by any governance engine instance
             * connected to this instance of the Governance Engine OMAS.  The default zones determines the zone that
             * any governance service defined through this Governance Engine OMAS's configuration interface is
             * set to.  Putting the governance services in a different zone to the data assets means that they are
             * can be masked from view from users of other OMASs such as Asset Consumer OMAS.
             */
            List<String>   supportedZones = super.extractSupportedZones(accessServiceConfig.getAccessServiceOptions(),
                                                                        accessServiceConfig.getAccessServiceName(),
                                                                        auditLog);

            List<String>  defaultZones = super.extractDefaultZones(accessServiceConfig.getAccessServiceOptions(),
                                                                   accessServiceConfig.getAccessServiceName(),
                                                                   auditLog);

            List<String>  publishZones = super.extractPublishZones(accessServiceConfig.getAccessServiceOptions(),
                                                                   accessServiceConfig.getAccessServiceName(),
                                                                   auditLog);

            /*
             * The instance is used to support REST API calls to this server instance.  It is given the
             * OutTopic connection for the client so that the client can query it to connect to the right
             * out topic.
             */
            this.instance = new GovernanceEngineInstance(repositoryConnector,
                                                         supportedZones,
                                                         defaultZones,
                                                         publishZones,
                                                         auditLog,
                                                         serverUserName,
                                                         repositoryConnector.getMaxPageSize(),
                                                         accessServiceConfig.getAccessServiceOutTopic());
            this.serverName = instance.getServerName();

            /*
             * This piece is setting up the server-side mechanism for the out topic.
             */
            Connection outTopicEventBusConnection = accessServiceConfig.getAccessServiceOutTopic();

            if (outTopicEventBusConnection != null)
            {
                Endpoint endpoint = outTopicEventBusConnection.getEndpoint();

                AuditLog outTopicAuditLog = auditLog.createNewAuditLog(OMRSAuditingComponent.OMAS_OUT_TOPIC);
                Connection serverSideOutTopicConnection = this.getServerSideOutTopicConnection(accessServiceConfig.getAccessServiceOutTopic(),
                                                                                               AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceFullName(),
                                                                                               GovernanceEngineOutTopicServerProvider.class.getName(),
                                                                                               auditLog);
                GovernanceEngineOutTopicServerConnector outTopicServerConnector = super.getTopicConnector(serverSideOutTopicConnection,
                                                                                                          GovernanceEngineOutTopicServerConnector.class,
                                                                                                          outTopicAuditLog,
                                                                                                          AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceFullName(),
                                                                                                          actionDescription);
                eventPublisher = new GovernanceEngineOutTopicPublisher(outTopicServerConnector, endpoint.getAddress(), outTopicAuditLog);

                this.registerWithEnterpriseTopic(AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceFullName(),
                                                 serverName,
                                                 omrsTopicConnector,
                                                 new GovernanceEngineOMRSTopicListener(AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceFullName(),
                                                                                       serverName,
                                                                                       serverUserName,
                                                                                       instance.getMetadataElementHandler(),
                                                                                       instance.getEngineActionHandler(),
                                                                                       eventPublisher,
                                                                                       repositoryConnector.getRepositoryHelper(),
                                                                                       outTopicAuditLog),
                                                 auditLog);
            }

            /*
             * Initialization is complete.  The service is now waiting for REST API calls (typically from the Engine Host) and events
             * from OMRS to indicate that there are metadata changes.
             */
            auditLog.logMessage(actionDescription, GovernanceEngineAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName));
        }
        catch (OMAGConfigurationErrorException error)
        {
            auditLog.logException(actionDescription,
                                  GovernanceEngineAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  accessServiceConfig.toString(),
                                  error);
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  GovernanceEngineAuditCode.UNEXPECTED_INITIALIZATION_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                    error.getMessage()),
                                  error);

            throw new OMAGConfigurationErrorException(
                    GovernanceEngineErrorCode.UNEXPECTED_INITIALIZATION_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                       AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceFullName(),
                                                                                                       serverName,
                                                                                                       error.getMessage()),
                    this.getClass().getName(),
                    actionDescription,
                    error);
        }
    }


    /**
     * Shutdown the access service.
     */
    @Override
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        if (this.instance != null)
        {
            this.instance.shutdown();
        }

        if (this.eventPublisher != null)
        {
            this.eventPublisher.disconnect();
        }

        auditLog.logMessage(actionDescription, GovernanceEngineAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}
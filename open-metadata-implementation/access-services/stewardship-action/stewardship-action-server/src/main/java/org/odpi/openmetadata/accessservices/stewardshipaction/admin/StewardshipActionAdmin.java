/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.stewardshipaction.admin;

import org.odpi.openmetadata.accessservices.stewardshipaction.connectors.outtopic.StewardshipActionOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.stewardshipaction.connectors.outtopic.StewardshipActionOutTopicServerProvider;
import org.odpi.openmetadata.accessservices.stewardshipaction.ffdc.StewardshipActionAuditCode;
import org.odpi.openmetadata.accessservices.stewardshipaction.outtopic.StewardshipActionOMRSTopicListener;
import org.odpi.openmetadata.accessservices.stewardshipaction.outtopic.StewardshipActionOutTopicPublisher;
import org.odpi.openmetadata.accessservices.stewardshipaction.server.StewardshipActionServicesInstance;
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
 * StewardshipActionAdmin manages the start up and shutdown of the Stewardship Action OMAS.   During start up,
 * it validates the parameters and options it receives and sets up the service as requested.
 */
public class StewardshipActionAdmin extends AccessServiceAdmin
{
    private AuditLog                          auditLog   = null;
    private StewardshipActionServicesInstance instance   = null;
    private String                            serverName = null;

    private StewardshipActionOutTopicPublisher eventPublisher = null;

    /**
     * Default constructor
     */
    public StewardshipActionAdmin()
    {
    }


    /**
     * Initialize the access service.
     *
     * @param accessServiceConfig  specific configuration properties for this access service.
     * @param omrsTopicConnector  connector for receiving OMRS Events from the cohorts
     * @param repositoryConnector  connector for querying the cohort repositories
     * @param auditLog  audit log component for logging messages.
     * @param serverUserName  user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    @Override
    public void initialize(AccessServiceConfig     accessServiceConfig,
                           OMRSTopicConnector      omrsTopicConnector,
                           OMRSRepositoryConnector repositoryConnector,
                           AuditLog                auditLog,
                           String                  serverUserName) throws OMAGConfigurationErrorException
    {
        final String actionDescription = "initialize";

        auditLog.logMessage(actionDescription, StewardshipActionAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        this.auditLog = auditLog;

        try
        {
            List<String>  supportedZones = this.extractSupportedZones(accessServiceConfig.getAccessServiceOptions(),
                                                                      accessServiceConfig.getAccessServiceName(),
                                                                      auditLog);

            this.instance = new StewardshipActionServicesInstance(repositoryConnector,
                                                                  supportedZones,
                                                                  auditLog,
                                                                  serverUserName,
                                                                  repositoryConnector.getMaxPageSize());
            this.serverName = instance.getServerName();

            /*
             * Only set up the listening and event publishing if requested in the config.
             */
            if (accessServiceConfig.getAccessServiceOutTopic() != null)
            {
                Connection outTopicEventBusConnection = accessServiceConfig.getAccessServiceOutTopic();

                Endpoint endpoint = outTopicEventBusConnection.getEndpoint();

                AuditLog outTopicAuditLog = auditLog.createNewAuditLog(OMRSAuditingComponent.OMAS_OUT_TOPIC);
                Connection serverSideOutTopicConnection = this.getServerSideOutTopicConnection(accessServiceConfig.getAccessServiceOutTopic(),
                                                                                               AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceFullName(),
                                                                                               StewardshipActionOutTopicServerProvider.class.getName(),
                                                                                               auditLog);
                StewardshipActionOutTopicServerConnector outTopicServerConnector = super.getTopicConnector(serverSideOutTopicConnection,
                                                                                                           StewardshipActionOutTopicServerConnector.class,
                                                                                                           outTopicAuditLog,
                                                                                                           AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceFullName(),
                                                                                                           actionDescription);
                eventPublisher = new StewardshipActionOutTopicPublisher(outTopicServerConnector,
                                                                       endpoint.getAddress(),
                                                                       outTopicAuditLog,
                                                                       repositoryConnector.getRepositoryHelper(),
                                                                       AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceName(),
                                                                       serverName);

                this.registerWithEnterpriseTopic(AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceFullName(),
                                                 serverName,
                                                 omrsTopicConnector,
                                                 new StewardshipActionOMRSTopicListener(AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceFullName(),
                                                                                        serverUserName,
                                                                                        eventPublisher,
                                                                                        instance.getReferenceableHandler(),
                                                                                        supportedZones,
                                                                                        outTopicAuditLog),
                                                 auditLog);
            }

            auditLog.logMessage(actionDescription,
                                StewardshipActionAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName),
                                accessServiceConfig.toString());
        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  StewardshipActionAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  accessServiceConfig.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceFullName(),
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

        if (this.eventPublisher != null)
        {
            this.eventPublisher.disconnect();
        }
        
        if (instance != null)
        {
            this.instance.shutdown();
        }

        auditLog.logMessage(actionDescription, StewardshipActionAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.admin;

import org.odpi.openmetadata.accessservices.itinfrastructure.connectors.outtopic.ITInfrastructureOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.itinfrastructure.connectors.outtopic.ITInfrastructureOutTopicServerProvider;
import org.odpi.openmetadata.accessservices.itinfrastructure.ffdc.ITInfrastructureAuditCode;
import org.odpi.openmetadata.accessservices.itinfrastructure.listener.ITInfrastructureOMRSTopicListener;
import org.odpi.openmetadata.accessservices.itinfrastructure.outtopic.ITInfrastructureOutTopicPublisher;
import org.odpi.openmetadata.accessservices.itinfrastructure.server.ITInfrastructureServicesInstance;
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
 * ITInfrastructureAdmin manages the start up and shutdown of the IT Infrastructure OMAS.   During start up,
 * it validates the parameters and options it receives and sets up the service as requested.
 */
public class ITInfrastructureAdmin extends AccessServiceAdmin
{
    private AuditLog                         auditLog   = null;
    private ITInfrastructureServicesInstance instance   = null;
    private String                           serverName = null;

    private ITInfrastructureOutTopicPublisher eventPublisher = null;


    /**
     * Default constructor
     */
    public ITInfrastructureAdmin()
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

        auditLog.logMessage(actionDescription, ITInfrastructureAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        this.auditLog = auditLog;

        try
        {
            List<String> supportedZones = this.extractSupportedZones(accessServiceConfig.getAccessServiceOptions(),
                                                                     accessServiceConfig.getAccessServiceName(),
                                                                     auditLog);

            List<String> defaultZones = this.extractDefaultZones(accessServiceConfig.getAccessServiceOptions(),
                                                                 accessServiceConfig.getAccessServiceName(),
                                                                 auditLog);

            List<String> publishZones = this.extractPublishZones(accessServiceConfig.getAccessServiceOptions(),
                                                                 accessServiceConfig.getAccessServiceName(),
                                                                 auditLog);

            this.instance = new ITInfrastructureServicesInstance(repositoryConnector,
                                                                 supportedZones,
                                                                 defaultZones,
                                                                 publishZones,
                                                                 auditLog,
                                                                 serverUserName,
                                                                 repositoryConnector.getMaxPageSize(),
                                                                 accessServiceConfig.getAccessServiceOutTopic());
            this.serverName = instance.getServerName();


            /*
             * Only set up the listening and event publishing if requested in the config.
             */
            if (accessServiceConfig.getAccessServiceOutTopic() != null)
            {
                Connection outTopicEventBusConnection = accessServiceConfig.getAccessServiceOutTopic();

                Endpoint endpoint = outTopicEventBusConnection.getEndpoint();

                AuditLog outTopicAuditLog = auditLog.createNewAuditLog(OMRSAuditingComponent.OMAS_OUT_TOPIC);
                Connection serverSideOutTopicConnection = this.getOutTopicConnection(accessServiceConfig.getAccessServiceOutTopic(),
                                                                                     AccessServiceDescription.IT_INFRASTRUCTURE_OMAS.getAccessServiceFullName(),
                                                                                     ITInfrastructureOutTopicServerProvider.class.getName(),
                                                                                     auditLog);
                ITInfrastructureOutTopicServerConnector outTopicServerConnector = super.getTopicConnector(serverSideOutTopicConnection,
                                                                                                          ITInfrastructureOutTopicServerConnector.class,
                                                                                                          outTopicAuditLog,
                                                                                                          AccessServiceDescription.IT_INFRASTRUCTURE_OMAS.getAccessServiceFullName(),
                                                                                                          actionDescription);
                eventPublisher = new ITInfrastructureOutTopicPublisher(outTopicServerConnector,
                                                                       endpoint.getAddress(),
                                                                       outTopicAuditLog,
                                                                       repositoryConnector.getRepositoryHelper(),
                                                                       AccessServiceDescription.IT_INFRASTRUCTURE_OMAS.getAccessServiceName(),
                                                                       serverName);

                this.registerWithEnterpriseTopic(AccessServiceDescription.IT_INFRASTRUCTURE_OMAS.getAccessServiceFullName(),
                                                 serverName,
                                                 omrsTopicConnector,
                                                 new ITInfrastructureOMRSTopicListener(AccessServiceDescription.IT_INFRASTRUCTURE_OMAS.getAccessServiceFullName(),
                                                                                       serverUserName,
                                                                                       eventPublisher,
                                                                                       instance.getAssetHandler(),
                                                                                       supportedZones,
                                                                                       outTopicAuditLog),
                                                 auditLog);
            }

            auditLog.logMessage(actionDescription,
                                ITInfrastructureAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName),
                                accessServiceConfig.toString());
        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  ITInfrastructureAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  accessServiceConfig.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.IT_INFRASTRUCTURE_OMAS.getAccessServiceFullName(),
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
            instance.shutdown();
        }

        auditLog.logMessage(actionDescription, ITInfrastructureAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}

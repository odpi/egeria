/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.admin;

import org.odpi.openmetadata.accessservices.digitalarchitecture.connectors.outtopic.DigitalArchitectureOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.digitalarchitecture.connectors.outtopic.DigitalArchitectureOutTopicServerProvider;
import org.odpi.openmetadata.accessservices.digitalarchitecture.ffdc.DigitalArchitectureAuditCode;
import org.odpi.openmetadata.accessservices.digitalarchitecture.outtopic.DigitalArchitectureOMRSTopicListener;
import org.odpi.openmetadata.accessservices.digitalarchitecture.outtopic.DigitalArchitectureOutTopicPublisher;
import org.odpi.openmetadata.accessservices.digitalarchitecture.server.DigitalArchitectureServicesInstance;
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
 * DigitalArchitectureAdmin manages the start up and shutdown of the Digital Architecture OMAS.   During start up,
 * it validates the parameters and options it receives and sets up the service as requested.
 */
public class DigitalArchitectureAdmin extends AccessServiceAdmin
{
    private AuditLog                             auditLog   = null;
    private DigitalArchitectureServicesInstance  instance   = null;
    private String                               serverName = null;
    private DigitalArchitectureOutTopicPublisher eventPublisher = null;

    /**
     * Default constructor
     */
    public DigitalArchitectureAdmin()
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
        final String  actionDescription = "initialize";

        auditLog.logMessage(actionDescription, DigitalArchitectureAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        this.auditLog = auditLog;

        try
        {
            List<String>  supportedZones = this.extractSupportedZones(accessServiceConfig.getAccessServiceOptions(),
                                                                      accessServiceConfig.getAccessServiceName(),
                                                                      auditLog);

            List<String>  defaultZones = this.extractDefaultZones(accessServiceConfig.getAccessServiceOptions(),
                                                                      accessServiceConfig.getAccessServiceName(),
                                                                      auditLog);

            List<String>  publishZones = this.extractPublishZones(accessServiceConfig.getAccessServiceOptions(),
                                                                      accessServiceConfig.getAccessServiceName(),
                                                                      auditLog);

            this.instance = new DigitalArchitectureServicesInstance(repositoryConnector,
                                                                    supportedZones,
                                                                    defaultZones,
                                                                    publishZones,
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
                                                                                               AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceFullName(),
                                                                                               DigitalArchitectureOutTopicServerProvider.class.getName(),
                                                                                               auditLog);
                DigitalArchitectureOutTopicServerConnector outTopicServerConnector = super.getTopicConnector(serverSideOutTopicConnection,
                                                                                                             DigitalArchitectureOutTopicServerConnector.class,
                                                                                                             outTopicAuditLog,
                                                                                                             AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceFullName(),
                                                                                                             actionDescription);
                eventPublisher = new DigitalArchitectureOutTopicPublisher(outTopicServerConnector,
                                                                          endpoint.getAddress(),
                                                                          outTopicAuditLog,
                                                                          repositoryConnector.getRepositoryHelper(),
                                                                          AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceName(),
                                                                          serverName);

                this.registerWithEnterpriseTopic(AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceFullName(),
                                                 serverName,
                                                 omrsTopicConnector,
                                                 new DigitalArchitectureOMRSTopicListener(AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceFullName(),
                                                                                        serverUserName,
                                                                                        eventPublisher,
                                                                                        instance.getReferenceableHandler(),
                                                                                        supportedZones,
                                                                                        outTopicAuditLog),
                                                 auditLog);
            }

            auditLog.logMessage(actionDescription,
                                DigitalArchitectureAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName),
                                accessServiceConfig.toString());
        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  DigitalArchitectureAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  accessServiceConfig.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceFullName(),
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

        auditLog.logMessage(actionDescription, DigitalArchitectureAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}

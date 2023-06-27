/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securitymanager.admin;

import org.odpi.openmetadata.accessservices.securitymanager.connectors.outtopic.SecurityManagerOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.securitymanager.connectors.outtopic.SecurityManagerOutTopicServerProvider;
import org.odpi.openmetadata.accessservices.securitymanager.ffdc.SecurityManagerAuditCode;
import org.odpi.openmetadata.accessservices.securitymanager.outtopic.SecurityManagerOMRSTopicListener;
import org.odpi.openmetadata.accessservices.securitymanager.outtopic.SecurityManagerOutTopicPublisher;
import org.odpi.openmetadata.accessservices.securitymanager.server.SecurityManagerServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;


import java.util.List;

/**
 * SecurityManagerAdmin manages the start up and shutdown of the Security Manager OMAS. During start up,
 * it validates the parameters and options it receives and sets up the service as requested.
 */
public class SecurityManagerAdmin extends AccessServiceAdmin
{
    private AuditLog                         auditLog         = null;
    private SecurityManagerServicesInstance  instance         = null;
    private OpenMetadataTopicConnector       inTopicConnector = null;
    private String                           serverName       = null;
    private SecurityManagerOutTopicPublisher eventPublisher   = null;


    /**
     * Default constructor
     */
    public SecurityManagerAdmin()
    {
    }

    
    /**
     * Initialize the access service.
     *
     * @param accessServiceConfig specific configuration properties for this access service.
     * @param omrsTopicConnector connector for receiving OMRS Events from the cohorts
     * @param repositoryConnector  connector for querying the cohort repositories
     * @param auditLog   audit log component for logging messages.
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

        final String actionDescription = "Initialize Security Manager OMAS service.";

        auditLog.logMessage(actionDescription, SecurityManagerAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

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

            this.instance   = new SecurityManagerServicesInstance(repositoryConnector,
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
                Connection serverSideOutTopicConnection = this.getServerSideOutTopicConnection(accessServiceConfig.getAccessServiceOutTopic(),
                                                                                               AccessServiceDescription.SECURITY_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                               SecurityManagerOutTopicServerProvider.class.getName(),
                                                                                               auditLog);
                SecurityManagerOutTopicServerConnector outTopicServerConnector = super.getTopicConnector(serverSideOutTopicConnection,
                                                                                                      SecurityManagerOutTopicServerConnector.class,
                                                                                                      outTopicAuditLog,
                                                                                                      AccessServiceDescription.SECURITY_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                                      actionDescription);
                eventPublisher = new SecurityManagerOutTopicPublisher(outTopicServerConnector,
                                                                      endpoint.getAddress(),
                                                                      outTopicAuditLog,
                                                                      repositoryConnector.getRepositoryHelper(),
                                                                      AccessServiceDescription.SECURITY_MANAGER_OMAS.getAccessServiceFullName(),
                                                                      serverName);

                this.registerWithEnterpriseTopic(AccessServiceDescription.SECURITY_MANAGER_OMAS.getAccessServiceFullName(),
                                                 serverName,
                                                 omrsTopicConnector,
                                                 new SecurityManagerOMRSTopicListener(AccessServiceDescription.SECURITY_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                      serverUserName,
                                                                                      eventPublisher,
                                                                                      instance.getUserIdentityHandler(),
                                                                                      supportedZones,
                                                                                      outTopicAuditLog),
                                                 auditLog);
            }


            /*
             * Only set up the listening and event publishing if requested in the config.
             */
            if (accessServiceConfig.getAccessServiceInTopic() != null)
            {
                // todo
                inTopicConnector = super.getInTopicEventBusConnector(accessServiceConfig.getAccessServiceInTopic(),
                                                                     AccessServiceDescription.SECURITY_MANAGER_OMAS.getAccessServiceFullName(),
                                                                     auditLog);
            }

            auditLog.logMessage(actionDescription,
                                SecurityManagerAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName),
                                accessServiceConfig.toString());
        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  SecurityManagerAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  accessServiceConfig.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.SECURITY_MANAGER_OMAS.getAccessServiceFullName(),
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

        try
        {
            if (inTopicConnector != null)
            {
                inTopicConnector.disconnect();
            }
        }
        catch (ConnectorCheckedException error)
        {
            auditLog.logException(actionDescription,
                                  SecurityManagerAuditCode.SERVICE_INSTANCE_TERMINATION_FAILURE.getMessageDefinition(serverName),
                                  error);
        }

        if (this.eventPublisher != null)
        {
            this.eventPublisher.disconnect();
        }

        if (instance != null)
        {
            instance.shutdown();
        }

        auditLog.logMessage(actionDescription, SecurityManagerAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}


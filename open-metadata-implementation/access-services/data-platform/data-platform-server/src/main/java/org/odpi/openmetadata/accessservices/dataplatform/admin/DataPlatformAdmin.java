/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.admin;

import org.odpi.openmetadata.accessservices.dataplatform.connectors.outtopic.DataPlatformOutTopicClientProvider;
import org.odpi.openmetadata.accessservices.dataplatform.connectors.outtopic.DataPlatformOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.dataplatform.connectors.outtopic.DataPlatformOutTopicServerProvider;
import org.odpi.openmetadata.accessservices.dataplatform.ffdc.DataPlatformAuditCode;
import org.odpi.openmetadata.accessservices.dataplatform.listener.DataPlatformOMRSTopicListener;
import org.odpi.openmetadata.accessservices.dataplatform.outtopic.DataPlatformOutTopicPublisher;
import org.odpi.openmetadata.accessservices.dataplatform.server.DataPlatformServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
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
 * DataPlatformAdmin manages the start up and shutdown of the Data Platform OMAS. During start up,
 * it validates the parameters and options it receives and sets up the service as requested.
 */
public class DataPlatformAdmin extends AccessServiceAdmin
{
    private AuditLog                      auditLog         = null;
    private DataPlatformServicesInstance  instance         = null;
    private OpenMetadataTopicConnector    inTopicConnector = null;
    private String                        serverName       = null;
    private DataPlatformOutTopicPublisher eventPublisher   = null;


    /**
     * Default constructor
     */
    public DataPlatformAdmin()
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

        final String actionDescription = "Initialize Data Platform OMAS service.";

        auditLog.logMessage(actionDescription, DataPlatformAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

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

            this.instance   = new DataPlatformServicesInstance(repositoryConnector,
                                                               supportedZones,
                                                               defaultZones,
                                                               publishZones,
                                                               auditLog,
                                                               serverUserName,
                                                               repositoryConnector.getMaxPageSize(),
                                                               this.getOutTopicConnection(accessServiceConfig.getAccessServiceOutTopic(),
                                                                                          AccessServiceDescription.DATA_PLATFORM_OMAS.getAccessServiceFullName(),
                                                                                          DataPlatformOutTopicClientProvider.class.getName(),
                                                                                          auditLog));
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
                                                                                     AccessServiceDescription.DATA_PLATFORM_OMAS.getAccessServiceFullName(),
                                                                                     DataPlatformOutTopicServerProvider.class.getName(),
                                                                                     auditLog);
                DataPlatformOutTopicServerConnector outTopicServerConnector = super.getTopicConnector(serverSideOutTopicConnection,
                                                                                                      DataPlatformOutTopicServerConnector.class,
                                                                                                      outTopicAuditLog,
                                                                                                      AccessServiceDescription.DATA_PLATFORM_OMAS.getAccessServiceFullName(),
                                                                                                      actionDescription);
                eventPublisher = new DataPlatformOutTopicPublisher(outTopicServerConnector, endpoint.getAddress(), outTopicAuditLog);

                this.registerWithEnterpriseTopic(AccessServiceDescription.DATA_PLATFORM_OMAS.getAccessServiceFullName(),
                                                 serverName,
                                                 omrsTopicConnector,
                                                 new DataPlatformOMRSTopicListener(AccessServiceDescription.DATA_PLATFORM_OMAS.getAccessServiceFullName(),
                                                                                   eventPublisher,
                                                                                   supportedZones,
                                                                                   repositoryConnector.getRepositoryHelper(),
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
                                                                     AccessServiceDescription.DATA_PLATFORM_OMAS.getAccessServiceFullName(),
                                                                     auditLog);
            }

            auditLog.logMessage(actionDescription,
                                DataPlatformAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName),
                                accessServiceConfig.toString());
        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (Throwable error)
        {
            auditLog.logException(actionDescription,
                                  DataPlatformAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  accessServiceConfig.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.DATA_PLATFORM_OMAS.getAccessServiceFullName(),
                                                         error);
        }
    }



    /**
     * Shutdown the access service.
     */
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
                                  DataPlatformAuditCode.SERVICE_INSTANCE_TERMINATION_FAILURE.getMessageDefinition(serverName),
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

        auditLog.logMessage(actionDescription, DataPlatformAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}


/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.admin;

import org.odpi.openmetadata.accessservices.datamanager.connectors.outtopic.DataManagerOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.datamanager.connectors.outtopic.DataManagerOutTopicServerProvider;
import org.odpi.openmetadata.accessservices.datamanager.ffdc.DataManagerAuditCode;
import org.odpi.openmetadata.accessservices.datamanager.listener.DataManagerOMRSTopicListener;
import org.odpi.openmetadata.accessservices.datamanager.outtopic.DataManagerOutTopicPublisher;
import org.odpi.openmetadata.accessservices.datamanager.server.DataManagerServicesInstance;
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
 * DataManagerAdmin manages the start up and shutdown of the Data Manager OMAS. During start up,
 * it validates the parameters and options it receives and sets up the service as requested.
 */
public class DataManagerAdmin extends AccessServiceAdmin
{
    private AuditLog                     auditLog         = null;
    private DataManagerServicesInstance  instance         = null;
    private String                       serverName       = null;
    private DataManagerOutTopicPublisher eventPublisher   = null;


    /**
     * Default constructor
     */
    public DataManagerAdmin()
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

        final String actionDescription = "Initialize Data Manager OMAS service.";

        auditLog.logMessage(actionDescription, DataManagerAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

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

            this.instance   = new DataManagerServicesInstance(repositoryConnector,
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
                                                                                               AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                               DataManagerOutTopicServerProvider.class.getName(),
                                                                                               auditLog);
                DataManagerOutTopicServerConnector outTopicServerConnector = super.getTopicConnector(serverSideOutTopicConnection,
                                                                                                      DataManagerOutTopicServerConnector.class,
                                                                                                      outTopicAuditLog,
                                                                                                      AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                                      actionDescription);
                this.eventPublisher = new DataManagerOutTopicPublisher(outTopicServerConnector, endpoint.getAddress(), outTopicAuditLog);

                this.registerWithEnterpriseTopic(AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceFullName(),
                                                 serverName,
                                                 omrsTopicConnector,
                                                 new DataManagerOMRSTopicListener(supportedZones,
                                                                                  eventPublisher,
                                                                                  serverUserName,
                                                                                  outTopicAuditLog,
                                                                                  repositoryConnector.getRepositoryHelper(),
                                                                                  AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                  serverName,
                                                                                  instance),
                                                 auditLog);
            }

            auditLog.logMessage(actionDescription,
                                DataManagerAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName),
                                accessServiceConfig.toString());
        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  DataManagerAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  accessServiceConfig.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceFullName(),
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

        auditLog.logMessage(actionDescription, DataManagerAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}


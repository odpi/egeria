/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.admin;

import org.odpi.openmetadata.accessservices.assetmanager.connectors.outtopic.AssetManagerOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.assetmanager.connectors.outtopic.AssetManagerOutTopicServerProvider;
import org.odpi.openmetadata.accessservices.assetmanager.ffdc.AssetManagerAuditCode;
import org.odpi.openmetadata.accessservices.assetmanager.listener.AssetManagerOMRSTopicListener;
import org.odpi.openmetadata.accessservices.assetmanager.outtopic.AssetManagerOutTopicPublisher;
import org.odpi.openmetadata.accessservices.assetmanager.server.AssetManagerServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.registration.AccessServiceAdmin;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * AssetManagerAdmin manages the start up and shutdown of the Asset Manager OMAS. During start up,
 * it validates the parameters and options it receives and sets up the service as requested.
 */
public class AssetManagerAdmin extends AccessServiceAdmin
{
    private AuditLog                      auditLog         = null;
    private AssetManagerServicesInstance  instance         = null;
    private String                        serverName       = null;
    private AssetManagerOutTopicPublisher eventPublisher   = null;


    /**
     * Default constructor
     */
    public AssetManagerAdmin()
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

        final String actionDescription = "Initialize Asset Manager OMAS service.";

        auditLog.logMessage(actionDescription, AssetManagerAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

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

            this.instance   = new AssetManagerServicesInstance(repositoryConnector,
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
                                                                                     AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                     AssetManagerOutTopicServerProvider.class.getName(),
                                                                                     auditLog);
                AssetManagerOutTopicServerConnector outTopicServerConnector = super.getTopicConnector(serverSideOutTopicConnection,
                                                                                                      AssetManagerOutTopicServerConnector.class,
                                                                                                      outTopicAuditLog,
                                                                                                      AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                                      actionDescription);
                eventPublisher = new AssetManagerOutTopicPublisher(outTopicServerConnector,
                                                                   endpoint.getAddress(),
                                                                   outTopicAuditLog,
                                                                   repositoryConnector.getRepositoryHelper(),
                                                                   AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                                                   serverName);

                this.registerWithEnterpriseTopic(AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                 serverName,
                                                 omrsTopicConnector,
                                                 new AssetManagerOMRSTopicListener(AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
                                                                                   serverUserName,
                                                                                   eventPublisher,
                                                                                   instance.getGovernanceActionProcessHandler(),
                                                                                   supportedZones,
                                                                                   outTopicAuditLog),
                                                 auditLog);
            }


            auditLog.logMessage(actionDescription,
                                AssetManagerAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName),
                                accessServiceConfig.toString());
        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  AssetManagerAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  accessServiceConfig.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceFullName(),
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

        auditLog.logMessage(actionDescription, AssetManagerAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}


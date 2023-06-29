/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.admin;


import org.odpi.openmetadata.accessservices.assetowner.connectors.outtopic.AssetOwnerOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.assetowner.connectors.outtopic.AssetOwnerOutTopicServerProvider;
import org.odpi.openmetadata.accessservices.assetowner.ffdc.AssetOwnerAuditCode;
import org.odpi.openmetadata.accessservices.assetowner.outtopic.AssetOwnerOMRSTopicListener;
import org.odpi.openmetadata.accessservices.assetowner.outtopic.AssetOwnerOutTopicPublisher;
import org.odpi.openmetadata.accessservices.assetowner.server.AssetOwnerServicesInstance;
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
 * AssetOwnerAdmin manages the start up and shutdown of the Asset Owner OMAS.   During start up,
 * it validates the parameters and options it receives and sets up the service as requested.
 */
public class AssetOwnerAdmin extends AccessServiceAdmin
{
    private AuditLog                    auditLog       = null;
    private AssetOwnerServicesInstance  instance       = null;
    private String                      serverName     = null;
    private AssetOwnerOutTopicPublisher eventPublisher = null;

    /**
     * Default constructor
     */
    public AssetOwnerAdmin()
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
        final String             actionDescription = "initialize";

        auditLog.logMessage(actionDescription, AssetOwnerAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

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

            this.instance = new AssetOwnerServicesInstance(repositoryConnector,
                                                           supportedZones,
                                                           defaultZones,
                                                           publishZones,
                                                           auditLog,
                                                           serverUserName,
                                                           repositoryConnector.getMaxPageSize());
            this.serverName = instance.getServerName();

            /*
             * This piece is setting up the server-side mechanism for the out topic.
             */
            Connection outTopicEventBusConnection = accessServiceConfig.getAccessServiceOutTopic();

            if (outTopicEventBusConnection != null)
            {
                /*
                 * Only set up the listening and event publishing if requested in the config.
                 */
                Endpoint endpoint = outTopicEventBusConnection.getEndpoint();

                AuditLog outTopicAuditLog = auditLog.createNewAuditLog(OMRSAuditingComponent.OMAS_OUT_TOPIC);
                Connection serverSideOutTopicConnection = this.getServerSideOutTopicConnection(accessServiceConfig.getAccessServiceOutTopic(),
                                                                                               AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceFullName(),
                                                                                               AssetOwnerOutTopicServerProvider.class.getName(),
                                                                                               auditLog);
                AssetOwnerOutTopicServerConnector outTopicServerConnector = super.getTopicConnector(serverSideOutTopicConnection,
                                                                                                    AssetOwnerOutTopicServerConnector.class,
                                                                                                    outTopicAuditLog,
                                                                                                    AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceFullName(),
                                                                                                    actionDescription);
                eventPublisher = new AssetOwnerOutTopicPublisher(outTopicServerConnector,
                                                                 endpoint.getAddress(),
                                                                 outTopicAuditLog,
                                                                 repositoryConnector.getRepositoryHelper(),
                                                                 AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceName(),
                                                                 serverName);

                this.registerWithEnterpriseTopic(AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceFullName(),
                                                 serverName,
                                                 omrsTopicConnector,
                                                 new AssetOwnerOMRSTopicListener(AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceFullName(),
                                                                                 serverUserName,
                                                                                 eventPublisher,
                                                                                 instance.getAssetHandler(),
                                                                                 supportedZones,
                                                                                 outTopicAuditLog),
                                                 auditLog);
            }

            auditLog.logMessage(actionDescription,
                                AssetOwnerAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName),
                                accessServiceConfig.toString());
        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  AssetOwnerAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  accessServiceConfig.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceFullName(),
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

        auditLog.logMessage(actionDescription, AssetOwnerAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}

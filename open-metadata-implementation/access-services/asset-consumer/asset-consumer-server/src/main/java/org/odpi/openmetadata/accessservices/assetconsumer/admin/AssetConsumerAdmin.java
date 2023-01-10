/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.admin;

import org.odpi.openmetadata.accessservices.assetconsumer.connectors.outtopic.AssetConsumerOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.assetconsumer.connectors.outtopic.AssetConsumerOutTopicServerProvider;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.AssetConsumerAuditCode;
import org.odpi.openmetadata.accessservices.assetconsumer.outtopic.AssetConsumerOMRSTopicListener;
import org.odpi.openmetadata.accessservices.assetconsumer.outtopic.AssetConsumerOutTopicPublisher;
import org.odpi.openmetadata.accessservices.assetconsumer.server.AssetConsumerServicesInstance;
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
 * AssetConsumerAdmin manages the startup and shutdown of the Asset Consumer OMAS.   During start up,
 * it validates the parameters and options it receives and sets up the service as requested.
 */
public class AssetConsumerAdmin extends AccessServiceAdmin
{
    private AuditLog                       auditLog       = null;
    private AssetConsumerServicesInstance  instance       = null;
    private String                         serverName     = null;
    private AssetConsumerOutTopicPublisher eventPublisher = null;
    

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

        auditLog.logMessage(actionDescription, AssetConsumerAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        this.auditLog = auditLog;

        try
        {
            List<String>  supportedZones = this.extractSupportedZones(accessServiceConfig.getAccessServiceOptions(),
                                                                      accessServiceConfig.getAccessServiceName(),
                                                                      auditLog);

            this.instance = new AssetConsumerServicesInstance(repositoryConnector,
                                                              supportedZones,
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
                /*
                 * Only set up the listening and event publishing if requested in the config.
                 */
                Endpoint endpoint = outTopicEventBusConnection.getEndpoint();

                AuditLog outTopicAuditLog = auditLog.createNewAuditLog(OMRSAuditingComponent.OMAS_OUT_TOPIC);
                Connection serverSideOutTopicConnection = this.getOutTopicConnection(accessServiceConfig.getAccessServiceOutTopic(),
                                                                                     AccessServiceDescription.ASSET_CONSUMER_OMAS.getAccessServiceFullName(),
                                                                                     AssetConsumerOutTopicServerProvider.class.getName(),
                                                                                     auditLog);
                AssetConsumerOutTopicServerConnector outTopicServerConnector = super.getTopicConnector(serverSideOutTopicConnection,
                                                                                                          AssetConsumerOutTopicServerConnector.class,
                                                                                                          outTopicAuditLog,
                                                                                                          AccessServiceDescription.ASSET_CONSUMER_OMAS.getAccessServiceFullName(),
                                                                                                          actionDescription);
                eventPublisher = new AssetConsumerOutTopicPublisher(outTopicServerConnector,
                                                                       endpoint.getAddress(),
                                                                       outTopicAuditLog,
                                                                       repositoryConnector.getRepositoryHelper(),
                                                                       AccessServiceDescription.ASSET_CONSUMER_OMAS.getAccessServiceName(),
                                                                       serverName);

                this.registerWithEnterpriseTopic(AccessServiceDescription.ASSET_CONSUMER_OMAS.getAccessServiceFullName(),
                                                 serverName,
                                                 omrsTopicConnector,
                                                 new AssetConsumerOMRSTopicListener(AccessServiceDescription.ASSET_CONSUMER_OMAS.getAccessServiceFullName(),
                                                                                    serverUserName,
                                                                                    eventPublisher,
                                                                                    instance.getAssetHandler(),
                                                                                    supportedZones,
                                                                                    outTopicAuditLog),
                                                 auditLog);
            }

            /*
             * Initialization is complete.  The service is now waiting for REST API calls (typically from the Engine Host) and events
             * from OMRS to indicate that there are metadata changes.
             */
            auditLog.logMessage(actionDescription,
                                AssetConsumerAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName),
                                accessServiceConfig.toString());
        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  AssetConsumerAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getClass().getName(), error.getMessage()),
                                  accessServiceConfig.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.ASSET_CONSUMER_OMAS.getAccessServiceFullName(),
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

        if (instance != null)
        {
            this.instance.shutdown();
        }

        if (this.eventPublisher != null)
        {
            this.eventPublisher.disconnect();
        }

        auditLog.logMessage(actionDescription, AssetConsumerAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}

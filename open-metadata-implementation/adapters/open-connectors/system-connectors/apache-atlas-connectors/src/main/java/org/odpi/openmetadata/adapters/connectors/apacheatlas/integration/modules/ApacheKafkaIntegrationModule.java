/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.modules;


import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerOutTopicEvent;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationAuditCode;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationErrorCode;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionDetails;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;

import java.util.List;

/**
 * ApacheKafkaIntegrationModule maps Apache Kafka resources catalogued in Apache Atlas into the open metadata ecosystem.
 */
public class ApacheKafkaIntegrationModule extends AtlasRegisteredIntegrationModuleBase
{
    /**
     * Unique name of this module for messages.
     */
    private final static String kafkaModuleName     = "Apache Kafka Integration Module";
    private final static String egeriaKafkaTypeName = "KafkaTopic";
    private final static String atlasKafkaTypeName  = "kafka_topic";
    private final static String egeriaJMSTypeName   = "Topic";
    private final static String atlasJMSTypeName    = "jms_topic";


    /**
     * Constructor for the module is supplied with the runtime context in order to operate.
     *
     * @param connectorName name of the connector (for messages)
     * @param connectionDetails connection properties used to start the connector
     * @param auditLog logging destination
     * @param myContext integration context
     * @param targetRootURL URL to connect to Apache Atlas
     * @param atlasClient client to connect to Apache Atlas
     * @param embeddedConnectors list of any embedded connectors (such as secrets connector and topic connector
     * @throws UserNotAuthorizedException security problem
     */
    public ApacheKafkaIntegrationModule(String                   connectorName,
                                        ConnectionDetails connectionDetails,
                                        AuditLog                 auditLog,
                                        CatalogIntegratorContext myContext,
                                        String                   targetRootURL,
                                        ApacheAtlasRESTConnector atlasClient,
                                        List<Connector>          embeddedConnectors) throws UserNotAuthorizedException
    {
        super(connectorName,
              kafkaModuleName,
              connectionDetails,
              auditLog,
              myContext,
              targetRootURL,
              atlasClient,
              embeddedConnectors,
              new String[]{atlasKafkaTypeName, atlasJMSTypeName},
              null);
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh(" + moduleName + ")";

        /*
         * The configuration can turn off the cataloguing of assets into the open metadata ecosystem.
         */
        if ((myContext.getPermittedSynchronization() == PermittedSynchronization.BOTH_DIRECTIONS) ||
            (myContext.getPermittedSynchronization() == PermittedSynchronization.TO_THIRD_PARTY))
        {
            try
            {
                /*
                 * Retrieve the topics catalogued in Apache Atlas.  This is turned into an Open Metadata Topic entities.
                 */
                syncAtlasDataSetsAsDataSets(atlasKafkaTypeName, egeriaKafkaTypeName);
                syncAtlasDataSetsAsDataSets(atlasJMSTypeName, egeriaJMSTypeName);
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          AtlasIntegrationAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                              error.getClass().getName(),
                                                                                                              methodName,
                                                                                                              error.getMessage()),
                                          error);
                }

                throw new ConnectorCheckedException(AtlasIntegrationErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                        error.getClass().getName(),
                                                                                                                        methodName,
                                                                                                                        error.getMessage()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    error);
            }
        }
    }


    /**
     * Process an event that was published by the Asset Manager OMAS.  The listener is only registered if metadata is flowing
     * from the open metadata ecosystem to Apache Atlas.
     *
     * @param event event object
     */
    @Override
    public void processEvent(AssetManagerOutTopicEvent event)
    {
        // Ignore events
    }
}
/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apachekafka.integration;


import org.apache.kafka.clients.admin.Admin;
import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaTemplateType;
import org.odpi.openmetadata.adapters.connectors.apachekafka.integration.ffdc.KafkaIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.topics.TopicProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;


/**
 * KafkaTopicIntegrationConnector catalogues active topics in a kafka broker.
 */
public class KafkaTopicCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    /**
     * A map for template qualified name to templateGUID to remove the need to keep retrieving the template
     * on each refresh.
     */
    private final Map<String, String> templateIdentifiers = new HashMap<>();


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     * @throws ConnectorCheckedException problem connecting to topic
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    public KafkaTopicCatalogTargetProcessor(CatalogTarget        template,
                                            CatalogTargetContext catalogTargetContext,
                                            Connector            connectorToTarget,
                                            String               connectorName,
                                            AuditLog             auditLog) throws ConnectorCheckedException,
                                                                                  UserNotAuthorizedException
    {
        super(template, catalogTargetContext, connectorToTarget, connectorName, auditLog);
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        String targetRootURL = connectorToTarget.getConnection().getEndpoint().getAddress();

        this.refreshEventBroker(targetRootURL,
                                KafkaTemplateType.KAFKA_TOPIC_TEMPLATE.getTemplateGUID(),
                                KafkaTemplateType.KAFKA_SERVER_TEMPLATE.getTemplateName());
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     * This method performs two sweeps. It first retrieves the topics from the event broker (Kafka) and validates that are in the
     * catalog - adding or updating them if necessary. The second sweep is to ensure that all the topics catalogued
     * actually exist in the event broker.
     *
     * @param targetRootURL URL to the Kafka Broker
     * @param templateGUID optional template to use when creating new topics
     * @param templateQualifiedName qualifiedName for template - only set if templateGUID is set
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    public void refreshEventBroker(String targetRootURL,
                                   String templateGUID,
                                   String templateQualifiedName) throws ConnectorCheckedException
    {
        final String methodName = "refreshEventBroker";

        auditLog.logMessage(methodName,
                            KafkaIntegrationConnectorAuditCode.CONNECTOR_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                            targetRootURL,
                                                                                                            templateQualifiedName));

        try
        {
            /*
             * Retrieve the list of active topics from Kafka.
             */
            Properties properties = new Properties();
            properties.put("bootstrap.servers", targetRootURL);
            Admin            admin            = Admin.create(properties);
            Set<String>      activeTopicNames = admin.listTopics().names().get();
            admin.close();

            if (activeTopicNames != null)
            {
                auditLog.logMessage(methodName,
                                    KafkaIntegrationConnectorAuditCode.RETRIEVED_TOPICS.getMessageDefinition(connectorName,
                                                                                                             targetRootURL,
                                                                                                             Integer.toString(activeTopicNames.size())));

                /*
                 * Retrieve the topics that are catalogued for this event broker.
                 * Remove the topics from the catalog that are no longer present in the event broker.
                 * Remove the names of the topics that are cataloged from the active topic names.
                 * At the end of this loop, the active topic names will just contain the names of the
                 * topics that are not catalogued.
                 */
                AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.TOPIC.typeName);
                int                           startFrom        = 0;
                List<OpenMetadataRootElement> cataloguedTopics = assetClient.findAssets(null, assetClient.getSearchOptions(startFrom, integrationContext.getMaxPageSize()));

                while (cataloguedTopics != null)
                {
                    startFrom = startFrom + integrationContext.getMaxPageSize();

                    for (OpenMetadataRootElement topicElement : cataloguedTopics)
                    {
                        String topicName = assetClient.getQualifiedName(topicElement);
                        String topicGUID = topicElement.getElementHeader().getGUID();

                        if (! activeTopicNames.contains(topicName))
                        {
                            /*
                             * The topic no longer exists so delete it from the catalog.
                             */
                            assetClient.deleteAsset(topicGUID, assetClient.getDeleteOptions(true));

                            auditLog.logMessage(methodName,
                                                KafkaIntegrationConnectorAuditCode.TOPIC_DELETED.getMessageDefinition(connectorName,
                                                                                                                      topicName,
                                                                                                                      topicGUID));
                        }
                        else
                        {
                            activeTopicNames.remove(topicName);
                        }
                    }

                    cataloguedTopics = null; // todo assetClient.findAssets(startFrom, integrationContext.getMaxPageSize());
                }


                String topicGUID;

                /*
                 * Add the remaining active topics to the catalog.
                 */
                for (String topicName : activeTopicNames)
                {
                    if (templateGUID == null)
                    {
                        TopicProperties topicProperties = new TopicProperties();

                        topicProperties.setQualifiedName(topicName);
                        topicProperties.setTypeName(OpenMetadataType.KAFKA_TOPIC.typeName);

                        topicGUID = assetClient.createAsset(null, null, topicProperties, null);

                        if (topicGUID != null)
                        {
                            auditLog.logMessage(methodName,
                                                KafkaIntegrationConnectorAuditCode.TOPIC_CREATED.getMessageDefinition(connectorName,
                                                                                                                      topicName,
                                                                                                                      topicGUID));
                        }
                    }
                    else
                    {
                        /*
                        TemplateProperties templateProperties = new TemplateProperties();

                        templateProperties.setQualifiedName(topicName);

                        topicGUID = integrationContext.createAssetFromTemplate(templateGUID, templateProperties);

                        if (topicGUID != null)
                        {
                            auditLog.logMessage(methodName,
                                                KafkaIntegrationConnectorAuditCode.TOPIC_CREATED_FROM_TEMPLATE.getMessageDefinition(connectorName,
                                                                                                                                    topicName,
                                                                                                                                    topicGUID,
                                                                                                                                    templateQualifiedName,
                                                                                                                                    templateGUID));
                        }

                         */
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  KafkaIntegrationConnectorAuditCode.UNABLE_TO_RETRIEVE_TOPICS.getMessageDefinition(connectorName,
                                                                                                                    targetRootURL,
                                                                                                                    error.getClass().getName(),
                                                                                                                    error.getMessage()),
                                  error);
        }
    }
}

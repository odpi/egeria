/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apachekafka.integration;


import org.apache.kafka.clients.admin.Admin;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.TopicElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.TemplateProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.TopicProperties;
import org.odpi.openmetadata.adapters.connectors.apachekafka.integration.ffdc.KafkaIntegrationConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.apachekafka.resource.ApacheKafkaAdminConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.integrationservices.topic.connector.TopicIntegratorConnector;

import java.util.*;



/**
 * KafkaTopicIntegrationConnector catalogues active topics in a kafka broker.
 */
public class KafkaTopicIntegrationConnector extends TopicIntegratorConnector implements CatalogTargetIntegrator
{
    private final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * A map for template qualified name to templateGUID to remove the need to keep retrieving the template
     * on each refresh.
     */
    private final Map<String, String> templateIdentifiers = new HashMap<>();


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     * This method performs two sweeps. It first retrieves the topics from the event broker (Kafka) and validates that are in the
     * catalog - adding or updating them if necessary. The second sweep is to ensure that all the topics catalogued
     * actually exist in the event broker.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName="refresh";

        if ((embeddedConnectors != null) && (!embeddedConnectors.isEmpty()))
        {
            for (Connector embeddedConnector : embeddedConnectors)
            {
                if (embeddedConnector instanceof ApacheKafkaAdminConnector apacheKafkaAdminConnector)
                {
                    try
                    {
                        this.refreshEndpointTarget(apacheKafkaAdminConnector);
                    }
                    catch (ConnectorCheckedException exception)
                    {
                        throw exception;
                    }
                    catch (Exception exception)
                    {
                        auditLog.logException(methodName,
                                              KafkaIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                           exception.getClass().getName(),
                                                                                                                           exception.getMessage()),
                                              exception);
                    }
                }
            }
        }

        this.refreshCatalogTargets(this);
    }


    /**
     * Processes any event broker configured in this connector's connection.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void refreshEndpointTarget(ApacheKafkaAdminConnector apacheKafkaAdminConnector) throws ConnectorCheckedException
    {
        final String methodName = "refreshEndpointTarget";

        try
        {
            /*
             * Retrieve the configuration
             */
            EndpointProperties endpoint = apacheKafkaAdminConnector.getConnection().getEndpoint();

            if ((endpoint != null) && (endpoint.getAddress() != null))
            {
                String targetRootURL         = endpoint.getAddress();
                String templateQualifiedName = getTemplateQualifiedName(apacheKafkaAdminConnector.getConnection().getConfigurationProperties());
                String templateGUID          = null;

                if (templateQualifiedName != null)
                {
                    templateGUID = templateIdentifiers.get(templateQualifiedName);
                }

                this.refreshEventBroker(targetRootURL, templateGUID, templateQualifiedName);
            }
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  KafkaIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               exception.getClass().getName(),
                                                                                                               exception.getMessage()),
                                  exception);
        }
    }


    /**
     * Return the optional template qualified name from the configuration properties.  If this property is set, it
     * tries to locate the associated template.
     *
     * @param configurationProperties configuration properties
     * @return template qualified name
     */
    private String getTemplateQualifiedName(Map<String, Object> configurationProperties) throws Exception
    {
        final String methodName = "getTemplateQualifiedName";

        String templateQualifiedName = null;

        if (configurationProperties != null)
        {
            templateQualifiedName = configurationProperties.get(KafkaTopicIntegrationProvider.TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY).toString();
        }

        if (templateQualifiedName != null)
        {
            String templateGUID = templateIdentifiers.get(templateQualifiedName);

            if (templateGUID == null)
            {
                try
                {
                    List<TopicElement> templateElements = getContext().getTopicsByName(templateQualifiedName, 0, 0);

                    if (templateElements != null)
                    {
                        for (TopicElement templateElement : templateElements)
                        {
                            String qualifiedName = templateElement.getProperties().getQualifiedName();

                            if (templateQualifiedName.equals(qualifiedName))
                            {
                                templateGUID = templateElement.getElementHeader().getGUID();
                                templateIdentifiers.put(templateQualifiedName, templateGUID);
                            }
                        }
                    }
                }
                catch (ConnectorCheckedException error)
                {
                    throw error;
                }
                catch (Exception error)
                {
                    auditLog.logException(methodName,
                                          KafkaIntegrationConnectorAuditCode.MISSING_TEMPLATE.getMessageDefinition(connectorName,
                                                                                                                   templateQualifiedName),
                                          error);

                    throw error;
                }
            }
        }

        return templateQualifiedName;
    }


    /**
     * Perform the required integration logic for the assigned catalog target.
     *
     * @param requestedCatalogTarget the catalog target
     * @throws ConnectorCheckedException there is an unrecoverable error and the connector should stop processing.
     */
    @Override
    public void integrateCatalogTarget(RequestedCatalogTarget requestedCatalogTarget) throws ConnectorCheckedException
    {
        final String methodName = "integrateCatalogTarget";

        if (propertyHelper.isTypeOf(requestedCatalogTarget.getCatalogTargetElement(), OpenMetadataType.SOFTWARE_SERVER.typeName))
        {
            try
            {
                Connector connector = getContext().getConnectedAssetContext().getConnectorToAsset(requestedCatalogTarget.getCatalogTargetElement().getGUID());

                if ((connector != null) && (connector.getConnection() != null) && (connector.getConnection().getEndpoint() != null))
                {
                    String targetRootURL         = connector.getConnection().getEndpoint().getAddress();
                    String templateQualifiedName = getTemplateQualifiedName(connectionProperties.getConfigurationProperties());
                    String templateGUID          = null;

                    if (templateQualifiedName != null)
                    {
                        templateGUID = templateIdentifiers.get(templateQualifiedName);
                    }

                    this.refreshEventBroker(targetRootURL, templateGUID, templateQualifiedName);
                }
            }
            catch (Exception exception)
            {
                auditLog.logException(methodName,
                                      KafkaIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                   exception.getClass().getName(),
                                                                                                                   exception.getMessage()),
                                      exception);
            }
        }
        else
        {
            super.throwWrongTypeOfAsset(requestedCatalogTarget.getCatalogTargetElement().getGUID(),
                                        requestedCatalogTarget.getCatalogTargetElement().getType().getTypeName(),
                                        DeployedImplementationType.EVENT_BROKER.getAssociatedTypeName(),
                                        connectorName,
                                        methodName);
        }
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
                int startFrom = 0;
                List<TopicElement> cataloguedTopics = getContext().getMyTopics(startFrom, getContext().getMaxPageSize());

                while (cataloguedTopics != null)
                {
                    startFrom = startFrom + getContext().getMaxPageSize();

                    for (TopicElement topicElement : cataloguedTopics)
                    {
                        String topicName = topicElement.getProperties().getQualifiedName();
                        String topicGUID = topicElement.getElementHeader().getGUID();

                        if (! activeTopicNames.contains(topicName))
                        {
                            /*
                             * The topic no longer exists so delete it from the catalog.
                             */
                            getContext().removeTopic(topicGUID, topicName);

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

                    cataloguedTopics = getContext().getMyTopics(startFrom, getContext().getMaxPageSize());
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

                        topicGUID = getContext().createTopic(topicProperties);

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
                        TemplateProperties templateProperties = new TemplateProperties();

                        templateProperties.setQualifiedName(topicName);

                        topicGUID = getContext().createTopicFromTemplate(templateGUID, templateProperties);

                        if (topicGUID != null)
                        {
                            auditLog.logMessage(methodName,
                                                KafkaIntegrationConnectorAuditCode.TOPIC_CREATED_FROM_TEMPLATE.getMessageDefinition(connectorName,
                                                                                                                                    topicName,
                                                                                                                                    topicGUID,
                                                                                                                                    templateQualifiedName,
                                                                                                                                    templateGUID));
                        }
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



    /**
     * Shutdown kafka monitoring
     *
     * @throws ConnectorCheckedException something failed in the super class
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        final String methodName = "disconnect";


        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                KafkaIntegrationConnectorAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName));
        }

        super.disconnect();
    }
}

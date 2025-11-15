/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apachekafka.integration;


import org.apache.kafka.clients.admin.Admin;
import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaTemplateConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaTemplateType;
import org.odpi.openmetadata.adapters.connectors.apachekafka.integration.ffdc.KafkaIntegrationConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.apachekafka.integration.ffdc.KafkaIntegrationConnectorErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SoftwareCapabilityClient;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CapabilityAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.CapabilityAssetUseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.topics.TopicProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;


/**
 * KafkaTopicIntegrationConnector catalogues active topics in a kafka broker.
 */
public class KafkaTopicCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    private String serverName      = null;
    private String eventBrokerGUID = null;
    private String hostIdentifier  = null;
    private String portNumber      = null;
    private String templateGUID    = null;

    /**
     * Primary constructor
     *
     * @param template object to copy
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     */
    public KafkaTopicCatalogTargetProcessor(CatalogTarget        template,
                                            CatalogTargetContext catalogTargetContext,
                                            Connector            connectorToTarget,
                                            String               connectorName,
                                            AuditLog             auditLog)
    {
        super(template, catalogTargetContext, connectorToTarget, connectorName, auditLog);
    }


    /**
     * Indicates that the catalog target processor is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override public void start() throws UserNotAuthorizedException, ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        integrationContext.validateIsActive(methodName);

        if ((super.connectorToTarget != null) && (super.connectorToTarget.getConnection() != null) && (super.connectorToTarget.getConnection().getEndpoint() != null) && (super.connectorToTarget.getConnection().getEndpoint().getNetworkAddress() != null))
        {
            hostIdentifier = super.getHostIdentifier(super.connectorToTarget.getConnection().getEndpoint().getNetworkAddress());
            portNumber = super.getPortNumber(super.connectorToTarget.getConnection().getEndpoint().getNetworkAddress());
            templateGUID = this.getTemplateGUID();
        }
        else
        {
            super.throwMissingConnectionInfo(OpenMetadataProperty.NETWORK_ADDRESS.name, methodName);
        }

        if (propertyHelper.isTypeOf(this.getCatalogTargetElement().getElementHeader(), OpenMetadataType.SOFTWARE_SERVER.typeName))
        {
            eventBrokerGUID = this.getEventBrokerGUID(this.getCatalogTargetElement());
        }
        else if (propertyHelper.isTypeOf(this.getCatalogTargetElement().getElementHeader(), OpenMetadataType.EVENT_BROKER.typeName))
        {
            eventBrokerGUID = this.getCatalogTargetElement().getElementHeader().getGUID();
        }
        else
        {
            super.throwWrongTypeOfCatalogTarget(Arrays.asList(OpenMetadataType.SOFTWARE_SERVER.typeName,
                                                              OpenMetadataType.EVENT_BROKER.typeName).toString(),
                                                methodName);
        }

        if (this.getCatalogTargetElement().getProperties() instanceof ReferenceableProperties referenceableProperties)
        {
            serverName = referenceableProperties.getDisplayName();
        }
    }


    /**
     * Look up or create the event broker for this catalog target.
     *
     * @return qualified name of the event broker
     * @throws ConnectorCheckedException connector stopped
     */
    private String getEventBrokerGUID(OpenMetadataRootElement eventServer) throws ConnectorCheckedException
    {
        final String methodName = "getEventBrokerGUID";

        if (eventServer.getCapabilities() != null)
        {
            for (RelatedMetadataElementSummary capability : eventServer.getCapabilities())
            {
                if (capability != null)
                {
                    if (propertyHelper.isTypeOf(capability.getRelatedElement().getElementHeader(), OpenMetadataType.EVENT_BROKER.typeName))
                    {
                        return capability.getRelatedElement().getElementHeader().getGUID();
                    }
                }
            }
        }

        throw new ConnectorCheckedException(KafkaIntegrationConnectorErrorCode.MISSING_EVENT_BROKER.getMessageDefinition(connectorName,
                                                                                                                         eventServer.getElementHeader().getGUID(),
                                                                                                                         super.getCatalogTargetName()),
                                            this.getClass().getName(),
                                            methodName);
    }



    /**
     * Return the templateGUID. The default template from the core content pack is used as the default.
     * This is overridden by an optional template name from the configuration properties.
     *
     * @return template qualified name
     */
    private String getTemplateGUID()
    {
        String templateName = super.getStringConfigurationProperty(KafkaTemplateConfigurationProperty.TEMPLATE_NAME.getName(),
                                                                   super.getConfigurationProperties());

        if (templateName != null)
        {
            String templateGUID = super.getTemplates().get(templateName);

            if (templateGUID != null)
            {
                return templateGUID;
            }
        }

        return KafkaTemplateType.KAFKA_TOPIC_TEMPLATE.getTemplateGUID();
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.refresh();

        final String methodName = "refresh";

        auditLog.logMessage(methodName,
                            KafkaIntegrationConnectorAuditCode.CONNECTOR_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                            eventBrokerGUID,
                                                                                                            super.connectorToTarget.getConnection().getEndpoint().getNetworkAddress(),
                                                                                                            templateGUID));

        try
        {
            /*
             * Retrieve the list of active topics from Kafka.
             */
            Properties properties = new Properties();
            properties.put("bootstrap.servers", super.connectorToTarget.getConnection().getEndpoint().getNetworkAddress());
            Admin            admin            = Admin.create(properties);
            Set<String>      activeTopicNames = admin.listTopics().names().get();
            admin.close();

            /*
             * Compare the topics retrieved with those already catalogued.
             */
            if (activeTopicNames != null)
            {
                auditLog.logMessage(methodName,
                                    KafkaIntegrationConnectorAuditCode.RETRIEVED_TOPICS.getMessageDefinition(connectorName,
                                                                                                             super.connectorToTarget.getConnection().getEndpoint().getNetworkAddress(),
                                                                                                             Integer.toString(activeTopicNames.size())));

                /*
                 * Retrieve the topics that are catalogued for this event broker.
                 * Remove the topics from the catalog that are no longer present in the event broker.
                 * Remove the names of the topics that are cataloged from the active topic names.
                 * At the end of this loop, the active topic names will just contain the names of the
                 * topics that are not catalogued.
                 */
                AssetClient              assetClient = integrationContext.getAssetClient(OpenMetadataType.TOPIC.typeName);
                SoftwareCapabilityClient softwareCapabilityClient = integrationContext.getSoftwareCapabilityClient(OpenMetadataType.EVENT_BROKER.typeName);

                OpenMetadataRootElement eventBroker = softwareCapabilityClient.getSoftwareCapabilityByGUID(eventBrokerGUID, softwareCapabilityClient.getGetOptions());

                if (eventBroker != null)
                {
                    if (eventBroker.getCapabilityConsumedAssets() != null)
                    {
                        for (RelatedMetadataElementSummary consumedAsset : eventBroker.getCapabilityConsumedAssets())
                        {
                            if ((consumedAsset != null) && (consumedAsset.getRelatedElement().getProperties() instanceof TopicProperties topicProperties))
                            {
                                if (! activeTopicNames.contains(topicProperties.getTopicName()))
                                {
                                    /*
                                     * The topic no longer exists so unlink it from the broker.
                                     */
                                    softwareCapabilityClient.detachAssetUse(eventBrokerGUID,
                                                                            consumedAsset.getRelatedElement().getElementHeader().getGUID(),
                                                                            softwareCapabilityClient.getDeleteOptions(true));

                                    auditLog.logMessage(methodName,
                                                        KafkaIntegrationConnectorAuditCode.TOPIC_DELETED.getMessageDefinition(connectorName,
                                                                                                                              topicProperties.getTopicName(),
                                                                                                                              consumedAsset.getRelatedElement().getElementHeader().getGUID()));
                                }
                                else
                                {
                                    activeTopicNames.remove(topicProperties.getTopicName());
                                }
                            }
                        }
                    }
                }

                /*
                 * Add the remaining active topics to the catalog.
                 */
                for (String topicName : activeTopicNames)
                {
                    TemplateOptions templateOptions = new TemplateOptions(assetClient.getMetadataSourceOptions());

                    templateOptions.setIsOwnAnchor(true);
                    templateOptions.setParentGUID(eventBrokerGUID);
                    templateOptions.setParentAtEnd1(true);
                    templateOptions.setParentRelationshipTypeName(OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName);

                    Map<String, String> placeholderProperties = new HashMap<>();

                    placeholderProperties.put(PlaceholderProperty.HOST_IDENTIFIER.getName(), hostIdentifier);
                    placeholderProperties.put(PlaceholderProperty.PORT_NUMBER.getName(), portNumber);
                    placeholderProperties.put(PlaceholderProperty.SERVER_NAME.getName(), serverName);
                    placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), null);
                    placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), PlaceholderProperty.VERSION_IDENTIFIER.getExample());

                    CapabilityAssetUseProperties capabilityAssetUseProperties = new CapabilityAssetUseProperties();

                    capabilityAssetUseProperties.setUseType(CapabilityAssetUseType.OWNS);

                    String topicGUID = assetClient.createAssetFromTemplate(templateOptions,
                                                                           templateGUID,
                                                                           null,
                                                                           placeholderProperties,
                                                                           capabilityAssetUseProperties);

                    if (topicGUID != null)
                    {
                        auditLog.logMessage(methodName,
                                            KafkaIntegrationConnectorAuditCode.TOPIC_CREATED.getMessageDefinition(connectorName,
                                                                                                                  topicName,
                                                                                                                  topicGUID));
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  KafkaIntegrationConnectorAuditCode.UNABLE_TO_RETRIEVE_TOPICS.getMessageDefinition(connectorName,
                                                                                                                    super.connectorToTarget.getConnection().getEndpoint().getNetworkAddress(),
                                                                                                                    error.getClass().getName(),
                                                                                                                    error.getMessage()),
                                  error);
        }
    }
}
